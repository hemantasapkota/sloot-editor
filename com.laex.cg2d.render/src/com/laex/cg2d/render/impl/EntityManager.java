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
package com.laex.cg2d.render.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.laex.cg2d.model.ScreenModel.CGBounds;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGEntityCollisionType;
import com.laex.cg2d.model.ScreenModel.CGEntitySpritesheetItem;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.ScreenModel.CGVector2;
import com.laex.cg2d.render.BodyVisitor;
import com.laex.cg2d.render.ScreenScaffold;
import com.laex.cg2d.render.util.RunnerUtil;

/**
 * The Class EntityManager.
 */
public class EntityManager implements ScreenScaffold {

  /** The batch. */
  private SpriteBatch batch;

  /** The state time. */
  float stateTime = 0;

  /** The shape to sprite map. */
  private Map<CGShape, Sprite> shapeToSpriteMap;

  /** The shape to entity map. */
  private Map<CGShape, CGEntity> shapeToEntityMap;
  
  /** The shape to animation map. */
  private Map<CGShape, Animation> shapeToAnimationMap;
  
  // x = origin x, y = origin y, z = radius
  /** The shape to animation origin map. */
  private Map<CGShape, Vector3> shapeToAnimationOriginMap;


  /** The draw entities. */
  private boolean drawEntities;

  /** The manipulator. */
  private ScreenManagerImpl manipulator;

  /**
   * Instantiates a new entity manager.
   * 
   * @param manipulator
   *          the manipulator
   * @param batch
   *          the batch
   */
  public EntityManager(ScreenManagerImpl manipulator, SpriteBatch batch) {
    this.batch = batch;
    this.manipulator = manipulator;

    this.drawEntities = manipulator.model().getScreenPrefs().getDebugDrawPrefs().getDrawEntities();

    this.shapeToSpriteMap = new HashMap<CGShape, Sprite>();
    this.shapeToEntityMap = new HashMap<CGShape, CGEntity>();
    this.shapeToAnimationMap = new HashMap<CGShape, Animation>();
    this.shapeToAnimationOriginMap = new HashMap<CGShape, Vector3>();
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
    for (CGLayer layer : manipulator.model().getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {
        manipulator.createJoints(shape);
      }
    }
  }

  /**
   * Creates the entity.
   *
   * @param shape the shape
   * @param animationName the animation name
   * @return the body
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Body createEntity(CGShape shape, String animationName) throws IOException {
    CGEditorShapeType eType = shape.getEditorShapeType();

    if (eType == CGEditorShapeType.BACKGROUND_SHAPE) {
      return null;
    }

    /* We alo create shapes */
    if (eType == CGEditorShapeType.SIMPLE_SHAPE_BOX || eType == CGEditorShapeType.SIMPLE_SHAPE_CIRCLE
        || eType == CGEditorShapeType.SIMPLE_SHAPE_HEDGE || eType == CGEditorShapeType.SIMPLE_SHAPE_VEDGE) {
      Body b = manipulator.createBody(shape, null, null);
      b.setUserData(shape);
      return b;
    }

    /* For Entities */
    final CGEntity entity = CGEntity.parseFrom(Gdx.files.absolute(shape.getEntityRefFile().getResourceFileAbsolute())
        .read());

    shapeToEntityMap.put(shape, entity);

    /* Entity Animation */
    CGEntityAnimation ea = null;

    /* If name is not provided, get default animation */
    if (animationName == null) {
      ea = RunnerUtil.getDefaultAnimation(entity);
    } else {
      ea = RunnerUtil.getAnimationFrom(entity, animationName);
    }

    Body b = manipulator.createBody(shape, entity, ea);
    b.setUserData(shape);

    // Resource file empty indicates this entity might not have
    // image & collision shape defined.
    if (ea.getSpritesheetFile().getResourceFileAbsolute().trim().isEmpty()) {
      manipulator.world().destroyBody(b);
      return null;
    }

    FileHandle handle = Gdx.files.absolute(ea.getSpritesheetFile().getResourceFileAbsolute());

    Texture tex = new Texture(handle);
    tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    Array<TextureRegion> walkFrames = new Array<TextureRegion>();

    for (CGEntitySpritesheetItem cgEi : ea.getSpritesheetItemsList()) {
      CGBounds bnds = cgEi.getExtractBounds();
      TextureRegion tr = new TextureRegion(tex, (int) bnds.getX(), (int) bnds.getY(), (int) bnds.getWidth(),
          (int) bnds.getHeight());
      walkFrames.add(tr);
    }

    Animation spriteAnimation = new Animation(ea.getAnimationDuration(), walkFrames);
    shapeToAnimationMap.put(shape, spriteAnimation);

    Sprite spr = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));

    // Set the position & size
    float x = shape.getBounds().getX();
    float y = shape.getBounds().getY();
    float width = shape.getBounds().getWidth();
    float height = shape.getBounds().getHeight();

    Vector2 scrPos = new Vector2(x, y);
    Vector2 worldPos = manipulator.screenToWorldFlipped(scrPos, height);
    spr.setPosition(worldPos.x, worldPos.y);

    // the position circle (collision shape) will vary with that of
    // a box. so
    // we need to check and set position for each types
    if (ea.getCollisionType() == CGEntityCollisionType.CIRCLE) {
      Vector3 origin = shapeToAnimationOriginMap.get(shape);

      float radius = origin.z;

      float x1 = (worldPos.x - radius);
      float y1 = (worldPos.y - radius);
      spr.setPosition(x1, y1);
    }

    float w = ((float) width / manipulator.ptmRatio());
    float h = ((float) height / manipulator.ptmRatio());

    spr.setSize(w, h);

    shapeToSpriteMap.put(shape, spr);

    return b;
  }

  /**
   * Removes the entity.
   *
   * @param shape the shape
   */
  public void removeEntity(CGShape shape) {
    shapeToAnimationMap.remove(shape);
    shapeToSpriteMap.remove(shape);
    shapeToEntityMap.remove(shape);
  }

  /**
   * Creates the bodies.
   * 
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void createBodies() throws IOException {
    for (CGLayer layer : manipulator.model().getLayersList()) {
      for (final CGShape shape : layer.getShapeList()) {
        /* Create entity with default animation */
        createEntity(shape, null);
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
    if (!drawEntities) {
      return;
    }

    batch.begin();

    manipulator.acceptBodyVisitor(new BodyVisitor() {

      @Override
      public void visit(Body b, CGShape shape) {
        if (!(shape.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE)) {
          return;
        }

        Vector2 pos = b.getPosition();

        // position for circle collision shape & box collision shape differ.
        CGEntity e = shapeToEntityMap.get(shape);
        CGEntityAnimation ea = RunnerUtil.getDefaultAnimation(e);

        // Position of circle should be adjusted of radius.
        if (ea.getCollisionType() == CGEntityCollisionType.CIRCLE) {
          float radius = shapeToAnimationOriginMap.get(shape).z;
          pos.x = pos.x - radius;
          pos.y = pos.y - radius;
        }

        Sprite spr = shapeToSpriteMap.get(shape);
        if (spr == null) {
          return;
        }

        spr.setPosition(pos.x, pos.y);
        // setting origin important for rotations to work properly
        Vector3 origin = shapeToAnimationOriginMap.get(shape);
        spr.setOrigin(origin.x, origin.y);
        spr.setRotation(b.getAngle() * MathUtils.radiansToDegrees);

        Animation anim = shapeToAnimationMap.get(shape);

        TextureRegion tr = anim.getKeyFrame(stateTime, true);
        spr.setRegion(tr);
        spr.draw(batch);
      }
    });

    batch.end();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
    manipulator.acceptBodyVisitor(new BodyVisitor() {
      @Override
      public void visit(Body b, CGShape shape) {

        Sprite spr = shapeToSpriteMap.get(shape);

        if (spr != null) {

          spr.getTexture().dispose();

        }
      }
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.AbstractGameComponentManager#createEntityCollisionShape
   * (com.laex.cg2d.protobuf.CGScreenModel.CGShape,
   * com.laex.cg2d.protobuf.CGScreenModel.CGEntity,
   * com.badlogic.gdx.physics.box2d.BodyDef,
   * com.badlogic.gdx.physics.box2d.FixtureDef,
   * com.badlogic.gdx.physics.box2d.Body, com.badlogic.gdx.math.Vector2)
   */
  /**
   * Creates the entity collision shape.
   *
   * @param shape the shape
   * @param entity the entity
   * @param ea the ea
   * @param bodyDef the body def
   * @param fixDef the fix def
   * @param b the b
   */
  public void createEntityCollisionShape(CGShape shape, CGEntity entity, CGEntityAnimation ea, BodyDef bodyDef,
      FixtureDef fixDef, Body b) {

    CGEntityCollisionType shapeType = ea.getCollisionType();
    PolygonShape polyShape = new PolygonShape();
    Vector2[] va = null;

    // check if shape type is NONE. NONE shape type means that this entity
    // has no collision parameters defined.
    // We simple ignore this and do not create any shape collision for this
    // object
    if (shapeType == CGEntityCollisionType.NONE) {
      // /set empty origin vertex so that other codes do not have to check null
      // pointer exception on its part
      shapeToAnimationOriginMap.put(shape, new Vector3(0, 0, 0));
      return;
    }

    switch (shapeType) {
    case BOX:
      va = manipulator.normalizeVertices(ea.getVerticesList(), shape.getBounds().getHeight());
      polyShape.set(va);

      shapeToAnimationOriginMap.put(shape, new Vector3(0, 0, 0));

      fixDef.shape = polyShape;
      b.createFixture(fixDef);
      break;

    case CIRCLE: {
      manipulator.world().destroyBody(b);

      CircleShape circShape = new CircleShape();

      /* Decode x,y, width, height of collision shape from the vertices */
      CGVector2 v1 = ea.getVertices(0);
      CGVector2 v2 = ea.getVertices(2);
      
      Rectangle r = new Rectangle(v1.getX(), v1.getY(), v2.getX(), v2.getY());
      r.width = r.width - r.x;
      r.height = r.height - r.y;

      // this radius is calculated for circle's collision shape data not
      // the sprite width/height data
      float radius = manipulator.calculateRadiusOfCircleShape(r.width);

      Vector2 cpos = new Vector2(r.x, r.y);
      cpos.y = shape.getBounds().getHeight() - r.height - r.y;
      cpos = manipulator.screenToWorld(cpos);

      /* Calculate origin */
      int x = (int) r.x;
      int y = (int) r.y;
      int w = (int) r.width;
      int h = (int) r.height;

      /* ptmRatioSquared should be casted to integer to avoid floating point calculation division.
       * If not, the orirgin will actully diverge and will be clearly seen in the rotation of the circle shape 
       */
      int ptmRatioSquared = (int) (manipulator.ptmRatio() * manipulator.ptmRatio());
      float ox = (x + w) / (ptmRatioSquared) + radius;
      float oy = (y + h) / (ptmRatioSquared) + radius;
      /* End origin calculatio */

      b = manipulator.world().createBody(bodyDef);

      circShape.setRadius(radius);
      circShape.setPosition(cpos);
      
      shapeToAnimationOriginMap.put(shape, new Vector3(ox, oy, radius));

      fixDef.shape = circShape;
      b.createFixture(fixDef);
    }
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
      float bodyScale = (shape.getBounds().getWidth() / manipulator.ptmRatio());
      bel.attachFixture(b, ea.getAnimationName(), fixDef, bodyScale);
      Vector2 or = bel.getOrigin(ea.getAnimationName(), bodyScale);
      shapeToAnimationOriginMap.put(shape, new Vector3(or.x, or.y, 0));
      break;

    case NONE:
    default:
      break;
    }

  }
}
