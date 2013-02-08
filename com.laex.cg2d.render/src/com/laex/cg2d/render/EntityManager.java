/*
 * Copyright (c) 2012, 2013 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package com.laex.cg2d.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.GameObject;
import com.laex.cg2d.protobuf.GameObject.CGEditorShapeType;
import com.laex.cg2d.protobuf.GameObject.CGEntity;
import com.laex.cg2d.protobuf.GameObject.CGEntityAnimation;
import com.laex.cg2d.protobuf.GameObject.CGEntityCollisionType;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;
import com.laex.cg2d.protobuf.GameObject.CGLayer;
import com.laex.cg2d.protobuf.GameObject.CGShape;

/**
 * The Class EntityManager.
 */
public class EntityManager extends AbstractGameComponentManager {

  /** The batch. */
  private SpriteBatch batch;

  /** The state time. */
  float stateTime = 0;

  /** The draw bodies. */
  boolean drawBodies;

  /** The shape to sprite map. */
  private Map<CGShape, Sprite> shapeToSpriteMap;

  /** The shape to entity map. */
  private Map<CGShape, CGEntity> shapeToEntityMap;

  /** The entity to animation map. */
  private Map<CGEntity, Animation> entityToAnimationMap;
  // x = origin x, y = origin y, z = radius
  /** The entity animation origin map. */
  private Map<CGEntityAnimation, Vector3> entityAnimationOriginMap;

  /**
   * Instantiates a new entity manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   * @param batch
   *          the batch
   */
  public EntityManager(CGGameModel model, World world, Camera cam, SpriteBatch batch) {
    super(model, world, cam);
    this.batch = batch;

    this.drawBodies = model.getScreenPrefs().getDebugDrawPrefs().getDrawBodies();

    this.shapeToSpriteMap = new HashMap<GameObject.CGShape, Sprite>();
    this.shapeToEntityMap = new HashMap<GameObject.CGShape, GameObject.CGEntity>();
    this.entityToAnimationMap = new HashMap<GameObject.CGEntity, Animation>();
    this.entityAnimationOriginMap = new HashMap<GameObject.CGEntityAnimation, Vector3>();
  }

  /**
   * Update state time.
   * 
   * @param stateTime
   *          the state time
   */
  public void updateStateTime(float stateTime) {
    this.stateTime = stateTime;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#create()
   */
  @Override
  public void create() {

    try {
      createBodies();
    } catch (IOException e) {
      e.printStackTrace();
    }

    createJoints();
  }

  /**
   * Joints must be created after all shapes are created including entities,
   * boxes, circles. Therefore, we create joints here in EntityManager and not
   * ShapeManager or BackgroundManager.
   */
  private void createJoints() {
    for (CGLayer layer : model().getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {
        createJoints(shape);
      }
    }
  }

  /**
   * Creates the bodies.
   * 
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void createBodies() throws IOException {
    for (CGLayer layer : model().getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {
        CGEditorShapeType eType = shape.getEditorShapeType();

        if (!(eType == CGEditorShapeType.ENTITY_SHAPE)) {
          continue;
        }

        CGEntity entity = CGEntity.parseFrom(Gdx.files.absolute(shape.getEntityRefFile().getResourceFileAbsolute())
            .read());

        shapeToEntityMap.put(shape, entity);

        Body b = createBody(shape, entity);
        b.setUserData(shape);

        CGEntityAnimation ea = RunnerUtil.getDefaultAnimation(entity);

        // Resource file empty indicates this entity might not have
        // image & collision shape defined. Ignore this and do not create bodies or shape for
        // this kind of object.
        if (ea.getAnimationResourceFile().getResourceFileAbsolute().trim().isEmpty()) {
          world().destroyBody(b);
          continue;
        }
        
        FileHandle handle = Gdx.files.absolute(ea.getAnimationResourceFile().getResourceFileAbsolute());

        Texture tex = new Texture(handle);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Animation spriteAnimation = null;
        if (ea.getCols() / ea.getRows() > 1) {
          // create sprite sheet animation
          TextureRegion[][] tmp = TextureRegion.split(tex, tex.getWidth() / ea.getCols(),
              tex.getHeight() / ea.getRows());
          TextureRegion[] walkFrames = new TextureRegion[ea.getCols() * ea.getRows()];
          int index = 0;
          for (int i = 0; i < ea.getRows(); i++) {
            for (int j = 0; j < ea.getCols(); j++) {
              walkFrames[index++] = tmp[i][j];
            }
          }
          spriteAnimation = new Animation(ea.getAnimationDuration(), walkFrames);
          entityToAnimationMap.put(entity, spriteAnimation);
        }

        Sprite spr = null;
        if (spriteAnimation == null) {
          spr = new Sprite(tex);
        } else {
          spr = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));
        }

        // Set the position & size
        float x = shape.getBounds().getX();
        float y = shape.getBounds().getY();
        float width = shape.getBounds().getWidth();
        float height = shape.getBounds().getHeight();

        Vector2 scrPos = new Vector2(x, y);
        Vector2 worldPos = screenToWorldFlipped(scrPos, height);
        spr.setPosition(worldPos.x * scaleFactor(), worldPos.y * scaleFactor());

        // the position circle (collision shape) will vary with that of
        // a box. so
        // we need to check and set position for each types
        if (ea.getCollisionType() == CGEntityCollisionType.CIRCLE) {
          float radius = entityAnimationOriginMap.get(ea).z;
          float x1 = (worldPos.x - radius);
          float y1 = (worldPos.y - radius);
          spr.setPosition(x1 * scaleFactor(), y1 * scaleFactor());
        }

        float w = ((float) width / ptmRatio()) * scaleFactor();
        float h = ((float) height / ptmRatio()) * scaleFactor();

        spr.setSize(w, h);

        shapeToSpriteMap.put(shape, spr);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#render()
   */
  @Override
  public void render() {
    if (!drawBodies) {
      return;
    }

    batch.begin();
    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      //
      if (!(b.getUserData() instanceof CGShape)) {
        continue;
      }

      CGShape shape = (CGShape) b.getUserData();
      if (!(shape.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE)) {
        continue;
      }

      Vector2 pos = b.getPosition();

      // position for circle collision shape & box collision shape differ.
      CGEntity e = shapeToEntityMap.get(shape);
      CGEntityAnimation ea = RunnerUtil.getDefaultAnimation(e);

      // Position of circle should be adjusted of radius.
      if (ea.getCollisionType() == CGEntityCollisionType.CIRCLE) {
        float radius = entityAnimationOriginMap.get(ea).z;
        pos.x = pos.x - radius;
        pos.y = pos.y - radius;
      }

      Sprite spr = shapeToSpriteMap.get(shape);
      if (spr != null) {
        spr.setPosition(pos.x * scaleFactor(), pos.y * scaleFactor());
        // setting origin important for rotations to work properly
        Vector3 origin = entityAnimationOriginMap.get(ea);
        spr.setOrigin(origin.x, origin.y);
        spr.setRotation(b.getAngle() * MathUtils.radiansToDegrees);

        Animation anim = entityToAnimationMap.get(shapeToEntityMap.get(shape));
        if (anim != null) {
          TextureRegion tr = anim.getKeyFrame(stateTime, true);
          spr.setRegion(tr);
        }
        spr.draw(batch);
      }
    }
    batch.end();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      if (!(b.getUserData() instanceof CGShape)) {
        continue;
      }
      CGShape shape = (CGShape) b.getUserData();
      Sprite spr = shapeToSpriteMap.get(shape);
      if (spr != null) {
        spr.getTexture().dispose();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.AbstractGameComponentManager#createEntityCollisionShape
   * (com.laex.cg2d.protobuf.GameObject.CGShape,
   * com.laex.cg2d.protobuf.GameObject.CGEntity,
   * com.badlogic.gdx.physics.box2d.BodyDef,
   * com.badlogic.gdx.physics.box2d.FixtureDef,
   * com.badlogic.gdx.physics.box2d.Body, com.badlogic.gdx.math.Vector2)
   */
  @Override
  public void createEntityCollisionShape(CGShape shape, CGEntity entity, BodyDef bodyDef, FixtureDef fixDef, Body b,
      Vector2 pos) {

    CGEntityAnimation ea = RunnerUtil.getDefaultAnimation(entity);

    CGEntityCollisionType shapeType = ea.getCollisionType();
    PolygonShape polyShape = new PolygonShape();
    Vector2[] va = null;

    // check if shape type is NONE. NONE shape type means that this entity
    // has no collision parameters defined.
    // We simple ignore this and do not create any shape collision for this
    // object
    if (shapeType == CGEntityCollisionType.NONE) {
      ///set empty origin vertex so that other codes do not have to check null pointer exception on its part
      entityAnimationOriginMap.put(ea, new Vector3(0, 0, 0));
      return;
    }

    switch (shapeType) {
    case BOX:
      va = normalizeVertices(ea.getVerticesList(), shape.getBounds().getHeight());
      polyShape.set(va);

      entityAnimationOriginMap.put(ea, new Vector3(0, 0, 0));

      fixDef.shape = polyShape;
      b.createFixture(fixDef);
      break;

    case CIRCLE:
      // destroy the body and create a new one for circle because the
      // position has to be updated
      world().destroyBody(b);

      CircleShape circShape = new CircleShape();

      // this radius is calculated for circle's collision shape data not
      // the sprite width/height data
      float radius = calculateRadiusOfCircleShape(ea.getShpWidth());

      Vector2 cpos = new Vector2();
      cpos.y = ea.getShpY();
      cpos.x = ea.getShpX();
      cpos = screenToWorld(cpos);

      // this formula is too arcane. explain why it is needed and where
      // did it arise from.
      // float ox = ((((ea.getShpX() + ea.getShpWidth()) / ptmRatio()) /
      // scaleFactor()) / 2)
      // + (scaleFactor() * radius);
      // float oy = ((((ea.getShpY() + ea.getShpHeight()) / ptmRatio()) /
      // scaleFactor()) / 2)
      // + (scaleFactor() * radius);

      // the formulate below was reduced from the above one
      float radiusScaled = scaleFactor() * radius;

      float ox = ((5 * (ea.getShpX() + ea.getShpWidth())) / (ptmRatio() * ptmRatio())) + radiusScaled;

      float oy = ((5 * (ea.getShpY() + ea.getShpHeight())) / (ptmRatio() * ptmRatio())) + radiusScaled;

      bodyDef.position.set(pos.add(radius, radius));
      b = world().createBody(bodyDef);

      circShape.setRadius(radius);
      circShape.setPosition(cpos);

      entityAnimationOriginMap.put(ea, new Vector3(ox, oy, radius));

      fixDef.shape = circShape;
      b.createFixture(fixDef);
      break;

    case CUSTOM:
      BodyEditorLoader bel = new BodyEditorLoader(Gdx.files.absolute(ea.getFixtureFile().getResourceFileAbsolute()));
      // scale = image width or image height / ptmRatio
      /*
       * bodyScale: Consider this: scale is a factor which scales the vertices
       * created by Physcis Editor. This scale is not equals to ptmRatio or
       * scaleFactor. bodyScale is the width or height of the image / ptmRatio
       * imageWidth = 32, scale = imageWidth / ptmRatio = 32 / 16 = 2 imageWidth
       * = 32, scale = imageWidth / ptmRatio = 32 / 32 = 1
       */
      float bodyScale = (shape.getBounds().getWidth() / ptmRatio());
      bel.attachFixture(b, ea.getAnimationName(), fixDef, bodyScale);
      Vector2 or = bel.getOrigin(ea.getAnimationName(), bodyScale);
      entityAnimationOriginMap.put(ea, new Vector3(or.x, or.y, 0));
      break;

    case NONE:
    default:
      break;
    }

  }
}
