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
package com.laex.cg2d.render.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.laex.cg2d.model.ScreenModel.CGBodyDef;
import com.laex.cg2d.model.ScreenModel.CGBodyType;
import com.laex.cg2d.model.ScreenModel.CGFixtureDef;
import com.laex.cg2d.model.ScreenModel.CGJoint;
import com.laex.cg2d.model.ScreenModel.CGJointType;
import com.laex.cg2d.model.ScreenModel.CGVector2;
import com.laex.cg2d.model.adapter.Vector2Adapter;

/**
 * The Class ProtoBufTypeConversionUtil.
 */
public class ProtoBufTypeConversionUtil {

  /**
   * As body type.
   * 
   * @param _type
   *          the _type
   * @return the body type
   */
  public static BodyType asBodyType(CGBodyType _type) {
    switch (_type) {
    case DYNAMIC:
      return BodyType.DynamicBody;

    case KINEMATIC:
      return BodyType.KinematicBody;

    case STATIC:
      return BodyType.StaticBody;
    }

    return BodyType.StaticBody;
  }

  /**
   * As joint type.
   * 
   * @param _type
   *          the _type
   * @return the joint type
   */
  public static JointType asJointType(CGJointType _type) {
    switch (_type) {
    case DISTANCE:
      return JointType.DistanceJoint;
    case FRICTION:
      return JointType.FrictionJoint;
    case GEAR:
      return JointType.GearJoint;
    case MOUSE:
      return JointType.MouseJoint;
    case PRISMATIC:
      return JointType.PrismaticJoint;
    case PULLEY:
      return JointType.PulleyJoint;
    case REVOLUTE:
      return JointType.RevoluteJoint;
    case ROPE:
      return JointType.RopeJoint;
    case UNKNOWN:
      return JointType.Unknown;
    case WELD:
      return JointType.WeldJoint;
    case WHEEL:
      return JointType.WheelJoint;
    default:
      break;

    }
    return JointType.Unknown;
  }

  /**
   * As vector2.
   * 
   * @param vec
   *          the vec
   * @return the vector2
   */
  public static Vector2 asVector2(CGVector2 vec) {
    return new Vector2(vec.getX(), vec.getY());
  }

  /**
   * As distance joint def.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the distance joint def
   */
  public static DistanceJointDef asDistanceJointDef(Body bodyA, Body bodyB, CGJoint _j) {
    DistanceJointDef jdef = new DistanceJointDef();
    jdef.collideConnected = _j.getDistanceJointDef().getCollideConnected();
    jdef.dampingRatio = _j.getDistanceJointDef().getDampingRatio();
    jdef.frequencyHz = _j.getDistanceJointDef().getFreqencyHz();
    jdef.initialize(bodyA, bodyB, bodyA.getWorldCenter(), bodyB.getWorldCenter());

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }

    return jdef;
  }

  /**
   * As pulley joint def.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the pulley joint def
   */
  public static PulleyJointDef asPulleyJointDef(Body bodyA, Body bodyB, CGJoint _j) {
    PulleyJointDef jdef = new PulleyJointDef();
    jdef.collideConnected = _j.getPulleyJointDef().getCollideConnected();
    jdef.ratio = _j.getPulleyJointDef().getRatio();
    jdef.initialize(bodyA, bodyB, ProtoBufTypeConversionUtil.asVector2(_j.getPulleyJointDef().getGroundAnchorA()),
        ProtoBufTypeConversionUtil.asVector2(_j.getPulleyJointDef().getGroundAnchorB()), bodyA.getWorldCenter(),
        bodyB.getWorldCenter(), _j.getPulleyJointDef().getRatio());

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }

    return jdef;
  }

  /**
   * As revolute joint.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the revolute joint def
   */
  public static RevoluteJointDef asRevoluteJoint(Body bodyA, Body bodyB, CGJoint _j) {
    RevoluteJointDef jdef = new RevoluteJointDef();
    jdef.collideConnected = _j.getRevoluteJointDef().getCollideConnected();
    jdef.enableLimit = _j.getRevoluteJointDef().getEnableLimit();
    jdef.enableMotor = _j.getRevoluteJointDef().getEnableMotor();
    jdef.lowerAngle = _j.getRevoluteJointDef().getLowerAngle();
    jdef.maxMotorTorque = _j.getRevoluteJointDef().getMaxMotorTorque();
    jdef.motorSpeed = _j.getRevoluteJointDef().getMotorSpeed();
    jdef.referenceAngle = _j.getRevoluteJointDef().getReferenceAngle();
    jdef.upperAngle = _j.getRevoluteJointDef().getUpperAngle();

    jdef.initialize(bodyA, bodyB, bodyA.getWorldCenter());

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }
    return jdef;

  }

  /**
   * As friction joint def.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the friction joint def
   */
  public static FrictionJointDef asFrictionJointDef(Body bodyA, Body bodyB, CGJoint _j) {
    FrictionJointDef jdef = new FrictionJointDef();
    jdef.collideConnected = _j.getFrictionJointDef().getCollideConnected();
    jdef.maxForce = _j.getFrictionJointDef().getMaxForce();
    jdef.maxTorque = _j.getFrictionJointDef().getMaxTorque();
    jdef.initialize(bodyA, bodyB, bodyA.getWorldCenter());

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }

    return jdef;
  }

  /**
   * As weld joint def.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the weld joint def
   */
  public static WeldJointDef asWeldJointDef(Body bodyA, Body bodyB, CGJoint _j) {
    WeldJointDef jdef = new WeldJointDef();
    jdef.collideConnected = _j.getWeldJointDef().getCollideConnected();
    jdef.initialize(bodyA, bodyB, bodyB.getWorldCenter());

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }

    return jdef;
  }

  /**
   * As primastic joint def.
   * 
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param _jdef
   *          the _jdef
   * @return the prismatic joint def
   */
  public static PrismaticJointDef asPrimasticJointDef(Body bodyA, Body bodyB, CGJoint _j) {
    PrismaticJointDef jdef = new PrismaticJointDef();
    jdef.collideConnected = _j.getPrismaticJointDef().getCollideConnected();
    jdef.enableLimit = _j.getPrismaticJointDef().getEnableLimit();
    jdef.enableMotor = _j.getPrismaticJointDef().getEnableMotor();
    jdef.referenceAngle = _j.getPrismaticJointDef().getReferenceAngle();
    jdef.lowerTranslation = _j.getPrismaticJointDef().getLowerTranslation();
    jdef.maxMotorForce = _j.getPrismaticJointDef().getMaxMotorForce();
    jdef.upperTranslation = _j.getPrismaticJointDef().getUpperTranslation();
    jdef.motorSpeed = _j.getPrismaticJointDef().getMotorSpeed();

    jdef.initialize(bodyA, bodyB, ProtoBufTypeConversionUtil.asVector2(_j.getPrismaticJointDef().getAnchor()),
        ProtoBufTypeConversionUtil.asVector2(_j.getPrismaticJointDef().getAxis()));

    if (_j.getUseLocalAnchors()) {
      jdef.localAnchorA.set(Vector2Adapter.asVector2(_j.getLocalAnchorA()));
      jdef.localAnchorB.set(Vector2Adapter.asVector2(_j.getLocalAnchorB()));
    }

    return jdef;
  }

  /**
   * As body def.
   * 
   * @param _bdef
   *          the _bdef
   * @return the body def
   */
  public static BodyDef asBodyDef(CGBodyDef _bdef) {
    BodyDef bdef = new BodyDef();
    bdef.active = _bdef.getActive();
    bdef.allowSleep = _bdef.getAllowSleep();
    bdef.angle = _bdef.getAngle();
    bdef.angularDamping = _bdef.getAngularDamping();
    bdef.angularVelocity = _bdef.getAngularVelocity();
    bdef.awake = _bdef.getAwake();
    bdef.bullet = _bdef.getBullet();
    bdef.fixedRotation = _bdef.getFixedRotation();
    bdef.gravityScale = _bdef.getGravityScale();
    bdef.linearDamping = _bdef.getLinearDamping();
    bdef.linearVelocity.x = _bdef.getLinearVelocity().getX();
    bdef.linearVelocity.y = _bdef.getLinearVelocity().getY();
    bdef.position.x = _bdef.getPosition().getX();
    bdef.position.y = _bdef.getPosition().getY();
    bdef.type = ProtoBufTypeConversionUtil.asBodyType(_bdef.getType());
    return bdef;
  }

  /**
   * As fixture def.
   * 
   * @param _fdef
   *          the _fdef
   * @return the fixture def
   */
  public static FixtureDef asFixtureDef(CGFixtureDef _fdef) {
    FixtureDef fdef = new FixtureDef();
    fdef.density = _fdef.getDensity();

    fdef.filter.categoryBits = (short) _fdef.getFilter().getCategoryBits();
    fdef.filter.groupIndex = (short) _fdef.getFilter().getGroupIndex();
    fdef.filter.maskBits = (short) _fdef.getFilter().getMaskBits();

    fdef.friction = _fdef.getFriction();
    fdef.isSensor = _fdef.getSensor();
    fdef.restitution = _fdef.getRestitution();

    return fdef;
  }

}
