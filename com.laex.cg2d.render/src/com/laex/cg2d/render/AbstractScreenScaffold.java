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

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.ScreenModel.CGEntity;
import com.laex.cg2d.protobuf.ScreenModel.CGJoint;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenModel;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;
import com.laex.cg2d.protobuf.ScreenModel.CGVector2;
import com.laex.cg2d.render.util.ProtoBufTypeConversionUtil;

/**
 * The Class AbstractGameComponentManager.
 * 
 * {@link AbstractScreenScaffold}
 * 
 * @author hemantasapkota
 */
public abstract class AbstractScreenScaffold implements IScreenScaffold {

  /** The world. */
  private World world;

  /** The model. */
  private CGScreenModel model;

  /** The cam. */
  private Camera cam;

  /** The scale factor. */
  private float scaleFactor;

  /** The ptm ratio. */
  private int ptmRatio;

  /** The card height. */
  private int cardHeight;

  /**
   * Instantiates a new abstract game component manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   */
  public AbstractScreenScaffold(CGScreenModel model, World world, Camera cam) {
    this.world = world;
    this.model = model;
    this.cam = cam;

    this.cardHeight = model.getScreenPrefs().getCardPrefs().getCardHeight();
    this.ptmRatio = model.getScreenPrefs().getWorldPrefs().getPtmRatio();
    this.scaleFactor = ptmRatio / MAGIC_SCALAR;
  }

  // override
  /**
   * Creates the entity collision shape.
   * 
   * @param shape
   *          the shape
   * @param entity
   *          the entity
   * @param bodyDef
   *          the body def
   * @param fixDef
   *          the fix def
   * @param b
   *          the b
   * @param pos
   *          the pos
   */
  public void createEntityCollisionShape(CGShape shape, CGEntity entity, BodyDef bodyDef, FixtureDef fixDef, Body b,
      Vector2 pos) {

  }

  /**
   * Creates the joints.
   * 
   * @param shape
   *          the shape
   */
  protected void createJoints(CGShape shape) {
    List<CGJoint> joints = shape.getJointsList();
    for (CGJoint joint : joints) {
      String sourceId = joint.getSourceShapeId();
      String targetId = joint.getTargetShapeId();

      Body bodyA = null;
      Body bodyB = null;

      Iterator<Body> bodyList = world().getBodies();
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
        world().createJoint(jd);
      } else {
        System.err.println("JointDef is not supposed to be null at this point.");
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
  private JointDef initJoints(CGJoint joint, Body bodyA, Body bodyB) {
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
  protected float calculateRadiusOfCircleShape(float width) {
    float w = width / ptmRatio();
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
  protected Body createBody(CGShape shape, CGEntity entity) {
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
    case SIMPLE_SHAPE_BOX:
      bodyDef.position.set(position);
      b = world().createBody(bodyDef);

      PolygonShape polyShape = new PolygonShape();
      float hx = (width / ptmRatio);
      float hy = (height / ptmRatio);
      hx /= 2;
      hy /= 2;

      polyShape.setAsBox(hx, hy, new Vector2(hx, hy), 0);

      fixDef.shape = polyShape;
      b.createFixture(fixDef);
      break;

    case SIMPLE_SHAPE_CIRCLE:
      CircleShape circShape = new CircleShape();
      float radius = calculateRadiusOfCircleShape(width);

      bodyDef.position.set(position.add(radius, radius));
      b = world().createBody(bodyDef);

      circShape.setRadius(radius);
      fixDef.shape = circShape;
      b.createFixture(fixDef);
      break;

    case SIMPLE_SHAPE_HEDGE:
      EdgeShape hEdgeShape = new EdgeShape();

      Vector2 hv1 = new Vector2(x, y);
      Vector2 hv2 = new Vector2(x + width, y);
      hv1 = screenToWorldFlipped(hv1, 0);
      hv2 = screenToWorldFlipped(hv2, 0);

      hEdgeShape.set(hv1, hv2);
      b = world().createBody(bodyDef);

      fixDef.shape = hEdgeShape;
      b.createFixture(fixDef);
      break;

    case SIMPLE_SHAPE_VEDGE:
      EdgeShape edgeShape = new EdgeShape();

      Vector2 vv1 = new Vector2(x, y);
      Vector2 vv2 = new Vector2(x, y + height);
      vv1 = screenToWorldFlipped(vv1, 0);
      vv2 = screenToWorldFlipped(vv2, 0);

      edgeShape.set(vv1, vv2);
      b = world().createBody(bodyDef);

      fixDef.shape = edgeShape;
      b.createFixture(fixDef);

      break;

    case ENTITY_SHAPE:
      bodyDef.position.set(position);
      b = world().createBody(bodyDef);
      createEntityCollisionShape(shape, entity, bodyDef, fixDef, b, position);
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
  protected Vector2[] normalizeVertices(List<CGVector2> vertices, float height) {
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
  protected Vector2 screenToWorld(Vector2 argScreen) {
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
  protected Vector2 screenToWorldFlipped(Vector2 argScreen, float height) {
    float x = argScreen.x / ptmRatio;
    float y = (cardHeight - height - argScreen.y) / ptmRatio;

    return new Vector2(x, y);
  }

  /**
   * World to screen.
   * 
   * @param argWorld
   *          the arg world
   * @return the vector2
   */
  protected Vector2 worldToScreen(Vector2 argWorld) {
    Vector2 v2 = new Vector2(argWorld.x * ptmRatio, argWorld.y * ptmRatio);
    return v2;
  }

  /**
   * World.
   * 
   * @return the world
   */
  protected World world() {
    return world;
  }

  /**
   * Model.
   * 
   * @return the cG game model
   */
  protected CGScreenModel model() {
    return model;
  }

  /**
   * Camera.
   * 
   * @return the camera
   */
  protected Camera camera() {
    return cam;
  }

  /**
   * Scale factor.
   * 
   * @return the float
   */
  protected float scaleFactor() {
    return scaleFactor;
  }

  /**
   * Ptm ratio.
   * 
   * @return the int
   */
  protected int ptmRatio() {
    return ptmRatio;
  }

}
