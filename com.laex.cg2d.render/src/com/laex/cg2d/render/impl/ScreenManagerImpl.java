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
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.util.concurrent.CycleDetectingLockFactory.WithExplicitOrdering;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGJoint;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.ScreenModel.CGVector2;
import com.laex.cg2d.render.BodyVisitor;
import com.laex.cg2d.render.ScreenManager;
import com.laex.cg2d.render.ScreenScaffold;
import com.laex.cg2d.render.util.AppExceptionUtil;
import com.laex.cg2d.render.util.ProtoBufTypeConversionUtil;

/**
 * The Class AbstractGameComponentManager.
 * 
 * {@link ScreenManagerImpl}
 * 
 * @author hemantasapkota
 */
public class ScreenManagerImpl implements ScreenScaffold, ScreenManager {

  /** The world. */
  private World world;

  /** The model. */
  private CGScreenModel model;

  /** The ptm ratio. */
  private int ptmRatio;

  /** The card height. */
  private int cardHeight;

  /** The camera. */
  private Camera camera;

  /** The entity manager. */
  private EntityManager entityManager;

  /** The background manager. */
  private BackgroundManager backgroundManager;

  private Box2DDebugRenderer debugDraw;

  private SpriteBatch spriteBatch;

  /**
   * Instantiates a new abstract game component manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param camera
   *          the camera
   * @param batch
   *          the batch
   */
  public ScreenManagerImpl(CGScreenModel model, World world, Camera camera, SpriteBatch batch) {
    this.world = world;
    this.model = model;
    this.camera = camera;
    this.spriteBatch = batch;

    this.cardHeight = model.getScreenPrefs().getCardPrefs().getCardHeight();
    this.ptmRatio = model.getScreenPrefs().getWorldPrefs().getPtmRatio();

    this.entityManager = new EntityManager(this, batch);
    this.backgroundManager = new BackgroundManager(this, batch);

    initBox2d();
  }

  private void initBox2d() {
    debugDraw = new Box2DDebugRenderer();

    debugDraw.setDrawAABBs(model.getScreenPrefs().getDebugDrawPrefs().getDrawAABB());
    debugDraw.setDrawBodies(model.getScreenPrefs().getDebugDrawPrefs().getDrawBodies());
    debugDraw.setDrawInactiveBodies(model.getScreenPrefs().getDebugDrawPrefs().getDrawInactiveBodies());
    debugDraw.setDrawJoints(model.getScreenPrefs().getDebugDrawPrefs().getDrawJoints());
  }

  // Standard visitor to box2d world
  /**
   * Accept body visitor.
   * 
   * @param bv
   *          the bv
   */
  public void acceptBodyVisitor(BodyVisitor bv) {
    Iterator<Body> bodiesIterator = world.getBodies();

    while (bodiesIterator.hasNext()) {

      Body b = bodiesIterator.next();
      if (b == null) {
        continue;
      }

      CGShape shape = (CGShape) b.getUserData();

      bv.visit(b, shape);

    }
  }

  /**
   * Creates the joints.
   * 
   * @param shape
   *          the shape
   */
  public void createJoints(CGShape shape) {
    List<CGJoint> joints = shape.getJointsList();
    for (CGJoint joint : joints) {
      String sourceId = joint.getSourceShapeId();
      String targetId = joint.getTargetShapeId();

      Body bodyA = null;
      Body bodyB = null;

      Iterator<Body> bodyList = world.getBodies();
      while (bodyList.hasNext()) {
        Body body = bodyList.next();
        CGShape shp = (CGShape) body.getUserData();

        if (shp != null) {
          String shpId = shp.getId();

          if (shpId.equals(sourceId)) {
            bodyA = body;
          } else if (shpId.equals(targetId)) {
            bodyB = body;
          }
        }
      }

      // Init the joints
      JointDef jd = initJoints(joint, bodyA, bodyB);
      if (jd != null) {
        world.createJoint(jd);
      }
    }

  }

  /**
   * Inits the joints.
   * 
   * @param joint
   *          the joint
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @return the joint def
   */
  public JointDef initJoints(CGJoint joint, Body bodyA, Body bodyB) {
    JointDef jd = null;
    switch (joint.getType()) {
    case DISTANCE:
      jd = ProtoBufTypeConversionUtil.asDistanceJointDef(bodyA, bodyB, joint.getDistanceJointDef());
      break;
    case FRICTION:
      jd = ProtoBufTypeConversionUtil.asFrictionJointDef(bodyA, bodyB, joint.getFrictionJointDef());
      break;
    case PRISMATIC:
      jd = ProtoBufTypeConversionUtil.asPrimasticJointDef(bodyA, bodyB, joint.getPrismaticJointDef());
      break;
    case PULLEY:
      jd = ProtoBufTypeConversionUtil.asPulleyJointDef(bodyA, bodyB, joint.getPulleyJointDef());
      break;
    case REVOLUTE:
      jd = ProtoBufTypeConversionUtil.asRevoluteJoint(bodyA, bodyB, joint.getRevoluteJointDef());
      break;
    case WELD:
      jd = ProtoBufTypeConversionUtil.asWeldJointDef(bodyA, bodyB, joint.getWeldJointDef());
      break;
    case ROPE:
      break;
    case GEAR:
      break;
    case MOUSE:
      break;
    case UNKNOWN:
      break;
    case WHEEL:
      break;
    default:
      break;
    }
    return jd;
  }

  /**
   * Calculate radius of circle shape.
   * 
   * @param width
   *          the width
   * @return the float
   */
  public float calculateRadiusOfCircleShape(float width) {
    float w = width / this.ptmRatio;
    float rad = w / 2;
    return rad;
  }

  /**
   * Creates the body.
   * 
   * @param shape
   *          the shape
   * @param entity
   *          the entity
   * @return the body
   */
  public Body createBody(CGShape shape, CGEntity entity, CGEntityAnimation ea) {
    BodyDef bodyDef = ProtoBufTypeConversionUtil.asBodyDef(shape.getBodyDef());
    FixtureDef fixDef = ProtoBufTypeConversionUtil.asFixtureDef(shape.getFixtureDef());

    float x = shape.getBounds().getX();
    float y = shape.getBounds().getY();
    float width = shape.getBounds().getWidth();
    float height = shape.getBounds().getHeight();

    Vector2 tmp = new Vector2(x, y);
    Vector2 position = screenToWorldFlipped(tmp, height);
    Body b = null;

    switch (shape.getEditorShapeType()) {
    case SIMPLE_SHAPE_BOX: {
      bodyDef.position.set(position);
      b = world.createBody(bodyDef);

      PolygonShape polyShape = new PolygonShape();
      float hx = (width / ptmRatio);
      float hy = (height / ptmRatio);

      hx = hx / 2;
      hy = hy / 2;

      polyShape.setAsBox(hx, hy, new Vector2(hx, hy), 0);

      fixDef.shape = polyShape;
      b.createFixture(fixDef);
    }
      break;

    case SIMPLE_SHAPE_CIRCLE: {
      CircleShape circShape = new CircleShape();
      float radius = calculateRadiusOfCircleShape(width);

      bodyDef.position.set(position.add(radius, radius));
      b = world.createBody(bodyDef);

      circShape.setRadius(radius);
      fixDef.shape = circShape;
      b.createFixture(fixDef);
    }
      break;

    case SIMPLE_SHAPE_HEDGE:
    case SIMPLE_SHAPE_VEDGE: {

      EdgeShape edge = new EdgeShape();

      Vector2 v1 = new Vector2(x, y);
      Vector2 v2 = new Vector2(x + width, y);

      Vector2 v2Vertical = new Vector2(x, y + height);

      v1 = screenToWorldFlipped(v1, 0);
      v2 = screenToWorldFlipped(v2, 0);
      v2Vertical = screenToWorldFlipped(v2Vertical, 0);

      if (shape.getEditorShapeType() == CGEditorShapeType.SIMPLE_SHAPE_VEDGE) {
        edge.set(v1, v2Vertical);
      } else {
        edge.set(v1, v2);
      }

      b = world.createBody(bodyDef);

      fixDef.shape = edge;
      b.createFixture(fixDef);
    }
      break;

    case ENTITY_SHAPE: {
      bodyDef.position.set(position);
      b = world.createBody(bodyDef);
      entityManager.createEntityCollisionShape(shape, entity, ea, bodyDef, fixDef, b);
    }
      break;

    case BACKGROUND_SHAPE:
    default:
      break;
    }

    return b;
  }

  /**
   * Normalize vertices.
   * 
   * @param vertices
   *          the vertices
   * @param height
   *          the height
   * @return the vector2[]
   */
  public Vector2[] normalizeVertices(List<CGVector2> vertices, float height) {
    Vector2[] vss = new Vector2[vertices.size()];

    for (int i = 0; i < vertices.size(); i++) {
      CGVector2 vi = vertices.get(i);
      Vector2 v = new Vector2(vi.getX(), vi.getY());
      v.y = height - v.y;
      vss[i] = screenToWorld(v);
    }

    // after flipping, reverse the vertices
    Vector2[] inverse = new Vector2[vertices.size()];
    int len = vertices.size() - 1;
    for (int i = 0; i < vertices.size(); i++) {
      inverse[i] = vss[len--];
    }

    return inverse;
  }

  /**
   * Screen to world.
   * 
   * @param argScreen
   *          the arg screen
   * @return the vector2
   */
  public Vector2 screenToWorld(Vector2 argScreen) {
    return new Vector2(argScreen.x / ptmRatio, argScreen.y / ptmRatio);
  }

  /**
   * Screen to world flipped.
   * 
   * @param argScreen
   *          the arg screen
   * @param height
   *          the height
   * @return the vector2
   */
  public Vector2 screenToWorldFlipped(Vector2 argScreen, float height) {
    float x = argScreen.x;
    float y = (cardHeight - height - argScreen.y);

    return new Vector2(x / ptmRatio, y / ptmRatio);
  }

  /**
   * World to screen.
   * 
   * @param argWorld
   *          the arg world
   * @return the vector2
   */
  public Vector2 worldToScreen(Vector2 argWorld) {
    return new Vector2(argWorld.x * ptmRatio, argWorld.y * ptmRatio);
  }

  /**
   * Handle exception.
   * 
   * @param t
   *          the t
   */
  public void handleException(Throwable t) {
    AppExceptionUtil.handle(t);
  }

  /**
   * Update state time.
   * 
   * @param stateTime
   *          the state time
   */
  public void updateStateTime(float stateTime) {
    this.entityManager.updateStateTime(stateTime);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#create()
   */
  @Override
  public void create() {
    backgroundManager.create();
    entityManager.create();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#render()
   */
  @Override
  public void render() {
    backgroundManager.render();
    entityManager.render();

    if (model.getScreenPrefs().getDebugDrawPrefs().getDrawDebugData()) {
      debugDraw.render(world, camera.combined);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#dispose()
   */
  @Override
  public void dispose() {
//    backgroundManager.dispose();
    entityManager.dispose();
  }

  /**
   * Ptm ratio.
   * 
   * @return the int
   */
  public int ptmRatio() {
    return this.ptmRatio;
  }

  /**
   * Camera.
   * 
   * @return the camera
   */
  public Camera camera() {
    return this.camera;
  }

  /**
   * World.
   * 
   * @return the world
   */
  public World world() {
    return this.world;
  }

  /**
   * Model.
   * 
   * @return the cG screen model
   */
  public CGScreenModel model() {
    return this.model;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenManager#createEntityFrom(java.lang.String)
   */
  @Override
  public Body createEntityFrom(final String id, final String animationName) {
    Body b = null;

    for (CGLayer layer : model.getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {

        if (!shape.getId().equals(id)) {
          continue;
        }

        try {

          b = entityManager.createEntity(shape, animationName);
          createJoints(shape);

        } catch (IOException e) {
          AppExceptionUtil.handle(e);
        }
      }
    }

    return b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenManager#switchAnimation(java.lang.String,
   * java.lang.String)
   */
  @Override
  public Body switchAnimation(final String id, final String animationName) {
    Body switchedBody = null;
    Iterator<Body> bodyItr = world.getBodies();

    while (bodyItr.hasNext()) {
      Body bod = bodyItr.next();
      CGShape shape = (CGShape) bod.getUserData();

      if (id.equals(shape.getId())) {
        try {
          
          Vector2 positionToCopy = new Vector2(bod.getTransform().getPosition());
          float rotation = bod.getTransform().getRotation();
          
          world.destroyBody(bod);
          entityManager.removeEntity(shape);
          switchedBody = entityManager.createEntity(shape, animationName);
          
          if (switchedBody != null) {
            switchedBody.setTransform(positionToCopy, rotation);
          }
          
        } catch (IOException e) {
          AppExceptionUtil.handle(e);
        }
      }

    }

    return switchedBody;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenManager#getEntityById(java.lang.String)
   */
  @Override
  public Body getEntityById(String id) {
    Iterator<Body> body = world.getBodies();

    while (body.hasNext()) {
      Body b = body.next();

      CGShape shape = (CGShape) b.getUserData();

      if (shape.getId().equals(id)) {
        return b;
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.ScreenManager#getEntityId(com.badlogic.gdx.physics
   * .box2d.Body)
   */
  @Override
  public String getEntityId(Body b) {
    return ((CGShape) b.getUserData()).getId();
  }

  @Override
  public Vector2 newVector(float x, float y) {
    return new Vector2(x, y);
  }

  @Override
  public void drawText(String text, float x, float y) {
    BitmapFont font = new BitmapFont();
    spriteBatch.begin();
    font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    font.setScale(0.1f);
    font.draw(spriteBatch, text, x, y);
    spriteBatch.end();
  }

}
