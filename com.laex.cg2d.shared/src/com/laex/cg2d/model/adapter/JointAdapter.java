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
package com.laex.cg2d.model.adapter;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.laex.cg2d.model.ScreenModel.CGDistanceJointDef;
import com.laex.cg2d.model.ScreenModel.CGFrictionJointDef;
import com.laex.cg2d.model.ScreenModel.CGJoint;
import com.laex.cg2d.model.ScreenModel.CGJointType;
import com.laex.cg2d.model.ScreenModel.CGPrismaticJointDef;
import com.laex.cg2d.model.ScreenModel.CGPulleyJointDef;
import com.laex.cg2d.model.ScreenModel.CGRevoluteJointDef;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.ScreenModel.CGWeldJointDef;
import com.laex.cg2d.model.joints.BEDistanceJoint;
import com.laex.cg2d.model.joints.BEFrictionJoint;
import com.laex.cg2d.model.joints.BEPrismaticJoint;
import com.laex.cg2d.model.joints.BEPulleyJoint;
import com.laex.cg2d.model.joints.BERevoluteJoint;
import com.laex.cg2d.model.joints.BEWeldJoint;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.BooleanUtil;

/**
 * The Class JointAdapter.
 */
public class JointAdapter {

  /**
   * As joint type.
   * 
   * @param cjt
   *          the cjt
   * @return the joint type
   */
  private static JointType asJointType(CGJointType cjt) {
    switch (cjt) {
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
   * As cg joint type.
   * 
   * @param jt
   *          the jt
   * @return the cG joint type
   */
  private static CGJointType asCGJointType(JointType jt) {
    switch (jt) {
    case DistanceJoint:
      return CGJointType.DISTANCE;
    case FrictionJoint:
      return CGJointType.FRICTION;
    case GearJoint:
      return CGJointType.GEAR;
    case MouseJoint:
      return CGJointType.MOUSE;
    case PrismaticJoint:
      return CGJointType.PRISMATIC;
    case PulleyJoint:
      return CGJointType.PULLEY;
    case RevoluteJoint:
      return CGJointType.REVOLUTE;
    case RopeJoint:
      return CGJointType.ROPE;
    case Unknown:
      return CGJointType.UNKNOWN;
    case WeldJoint:
      return CGJointType.WELD;
    case WheelJoint:
      return CGJointType.WHEEL;
    default:
      break;
    }
    return CGJointType.UNKNOWN;
  }

  /**
   * As joint.
   * 
   * @param cj
   *          the cj
   * @param source
   *          the source
   * @param target
   *          the target
   * @return the joint
   */
  public static Joint asJoint(CGJoint cj, Shape source, Shape target) {
    Joint j = null;
    JointType jt = asJointType(cj.getType());

    switch (cj.getType()) {
    case DISTANCE:
      j = new BEDistanceJoint(source, target);
      j.setPropertyValue(BEDistanceJoint.DAMPING_RATIO, Float.toString(cj.getDistanceJointDef().getDampingRatio()));
      j.setPropertyValue(BEDistanceJoint.FREUENCY_HZ_PROP, Float.toString(cj.getDistanceJointDef().getFreqencyHz()));
      j.setPropertyValue(BEDistanceJoint.COLLIDE_CONNECTED_PROP,
          BooleanUtil.toString(cj.getDistanceJointDef().getCollideConnected()));
      break;
    case FRICTION:
      j = new BEFrictionJoint(source, target);
      j.setPropertyValue(BEFrictionJoint.MAX_FORCE_PROP, cj.getFrictionJointDef().getMaxForce());
      j.setPropertyValue(BEFrictionJoint.MAX_TORQUE_PROP, cj.getFrictionJointDef().getMaxTorque());
      j.setPropertyValue(BEFrictionJoint.COLLIDE_CONNECTED_PROP, cj.getFrictionJointDef().getCollideConnected());
      j.setPropertyValue(BEFrictionJoint.JOINT_TYPE_PROP, jt);
      break;
    case PRISMATIC:
      j = new BEPrismaticJoint(source, target);
      j.setPropertyValue(BEPrismaticJoint.ANCHOR_PROP, Vector2Adapter.asVector2(cj.getPrismaticJointDef().getAnchor()));
      j.setPropertyValue(BEPrismaticJoint.AXIS_PROP, Vector2Adapter.asVector2(cj.getPrismaticJointDef().getAxis()));
      j.setPropertyValue(BEPrismaticJoint.ENABLE_LIMIT_PROP, cj.getPrismaticJointDef().getEnableLimit());
      j.setPropertyValue(BEPrismaticJoint.ENABLE_MOTOR_PROP, cj.getPrismaticJointDef().getEnableMotor());
      j.setPropertyValue(BEPrismaticJoint.LOWER_TRANSLATION_PROP, cj.getPrismaticJointDef().getLowerTranslation());
      j.setPropertyValue(BEPrismaticJoint.UPPER_TRANSLATION_PROP, cj.getPrismaticJointDef().getUpperTranslation());
      j.setPropertyValue(BEPrismaticJoint.MAX_MOTOR_FORCE_PROP, cj.getPrismaticJointDef().getMaxMotorForce());
      j.setPropertyValue(BEPrismaticJoint.MOTOR_SPEED_PROP, cj.getPrismaticJointDef().getMotorSpeed());
      j.setPropertyValue(BEPrismaticJoint.REFERENCE_ANGLE_PROP, cj.getPrismaticJointDef().getReferenceAngle());
      j.setPropertyValue(BEPrismaticJoint.COLLIDE_CONNECTED_PROP, cj.getPrismaticJointDef().getCollideConnected());
      j.setPropertyValue(BEPrismaticJoint.JOINT_TYPE_PROP, jt);
      break;
    case PULLEY:
      j = new BEPulleyJoint(source, target);
      j.setPropertyValue(BEPulleyJoint.GROUND_ANCHOR_A_PROP,
          Vector2Adapter.asVector2(cj.getPulleyJointDef().getGroundAnchorA()));
      j.setPropertyValue(BEPulleyJoint.GROUND_ANCHOR_B_PROP,
          Vector2Adapter.asVector2(cj.getPulleyJointDef().getGroundAnchorB()));
      j.setPropertyValue(BEPulleyJoint.RATIO_PROP, cj.getPulleyJointDef().getRatio());
      j.setPropertyValue(BEPulleyJoint.COLLIDE_CONNECTED_PROP, cj.getPulleyJointDef().getCollideConnected());
      j.setPropertyValue(BEPulleyJoint.JOINT_TYPE_PROP, jt);
      break;
    case REVOLUTE:
      j = new BERevoluteJoint(source, target);
      j.setPropertyValue(BERevoluteJoint.ENABLE_LIMIT_PROP, cj.getRevoluteJointDef().getEnableLimit());
      j.setPropertyValue(BERevoluteJoint.ENABLE_MOTOR_PROP, cj.getRevoluteJointDef().getEnableMotor());
      j.setPropertyValue(BERevoluteJoint.LOWER_ANGLE_PROP, cj.getRevoluteJointDef().getLowerAngle());
      j.setPropertyValue(BERevoluteJoint.MAX_MOTOR_TORQUE_PROP, cj.getRevoluteJointDef().getMaxMotorTorque());
      j.setPropertyValue(BERevoluteJoint.MOTOR_SPEED_PROP, cj.getRevoluteJointDef().getMotorSpeed());
      j.setPropertyValue(BERevoluteJoint.REFERENCE_ANGLE_PROP, cj.getRevoluteJointDef().getReferenceAngle());
      j.setPropertyValue(BERevoluteJoint.UPPER_ANGLE_PROP, cj.getRevoluteJointDef().getUpperAngle());
      j.setPropertyValue(BERevoluteJoint.COLLIDE_CONNECTED_PROP, cj.getRevoluteJointDef().getCollideConnected());
      j.setPropertyValue(BERevoluteJoint.JOINT_TYPE_PROP, jt);

      break;
    case WELD:
      j = new BEWeldJoint(source, target);
      j.setPropertyValue(BEWeldJoint.COLLIDE_CONNECTED_PROP, cj.getWeldJointDef().getCollideConnected());
      j.setPropertyValue(BEWeldJoint.JOINT_TYPE_PROP, jt);
      break;
    case GEAR:
      break;
    case MOUSE:
      break;
    case ROPE:
      break;
    case UNKNOWN:
      break;
    case WHEEL:
      break;
    default:
      break;
    }
    
    j.setLocalAnchorA(Vector2Adapter.asVector2(cj.getLocalAnchorA()));
    j.setLocalAnchorB(Vector2Adapter.asVector2(cj.getLocalAnchorB()));
    j.setUseLocalAnchors(cj.getUseLocalAnchors());
    
    return j;
  }

  /**
   * As cg joint.
   * 
   * @param j
   *          the j
   * @param s
   *          the s
   * @param source
   *          the source
   * @param target
   *          the target
   * @return the cG joint
   */
  public static CGJoint asCGJoint(Joint j, Shape s, CGShape source, CGShape target) {
    CGJoint.Builder jointBuilder = CGJoint.newBuilder().setSourceShapeId(source.getId())
        .setTargetShapeId(target.getId()).setType(asCGJointType(j.getJointType()))
        .setLocalAnchorA(Vector2Adapter.asCGVector2(j.getLocalAnchorA()))
        .setLocalAnchorB(Vector2Adapter.asCGVector2(j.getLocalAnchorB()))
        .setUseLocalAnchors(j.shouldUseLocalAnchors());

    switch (j.getJointType()) {
    case DistanceJoint:
      DistanceJointDef dj = (DistanceJointDef) j.getEditableValue();
      CGDistanceJointDef cdj = CGDistanceJointDef.newBuilder().setCollideConnected(j.isCollideConnected())
          .setFreqencyHz(dj.frequencyHz).setDampingRatio(dj.dampingRatio).build();
      jointBuilder = jointBuilder.setDistanceJointDef(cdj);
      break;
    case FrictionJoint:
      FrictionJointDef fj = (FrictionJointDef) j.getEditableValue();
      CGFrictionJointDef fdj = CGFrictionJointDef.newBuilder().setCollideConnected(j.isCollideConnected())
          .setMaxForce(fj.maxForce).setMaxTorque(fj.maxTorque).build();
      jointBuilder = jointBuilder.setFrictionJointDef(fdj);
      break;
    case PrismaticJoint:
      BEPrismaticJoint bpj = (BEPrismaticJoint) j;
      PrismaticJointDef pj = (PrismaticJointDef) j.getEditableValue();
      CGPrismaticJointDef cpj = CGPrismaticJointDef.newBuilder().setCollideConnected(j.isCollideConnected())
          .setEnableLimit(pj.enableLimit).setReferenceAngle(pj.referenceAngle).setEnableMotor(pj.enableMotor)
          .setLowerTranslation(pj.lowerTranslation).setMaxMotorForce(pj.maxMotorForce)
          .setUpperTranslation(pj.upperTranslation).setMotorSpeed(pj.motorSpeed)
          .setAnchor(Vector2Adapter.asCGVector2(bpj.getWorldAnchor()))
          .setAxis(Vector2Adapter.asCGVector2(bpj.getWorldAxis())).build();
      jointBuilder = jointBuilder.setPrismaticJointDef(cpj);
      break;
    case PulleyJoint:
      BEPulleyJoint bplj = (BEPulleyJoint) j;
      PulleyJointDef pjd = (PulleyJointDef) j.getEditableValue();
      CGPulleyJointDef cplj = CGPulleyJointDef.newBuilder().setCollideConnected(j.isCollideConnected())
          .setGroundAnchorA(Vector2Adapter.asCGVector2(bplj.getGroundAnchorA()))
          .setGroundAnchorB(Vector2Adapter.asCGVector2(bplj.getGroundAnchorB())).setRatio(pjd.ratio).build();
      jointBuilder = jointBuilder.setPulleyJointDef(cplj);
      break;
    case RevoluteJoint:
      RevoluteJointDef rjd = (RevoluteJointDef) j.getEditableValue();
      CGRevoluteJointDef crj = CGRevoluteJointDef.newBuilder().setCollideConnected(j.isCollideConnected())
          .setEnableLimit(rjd.enableLimit).setEnableMotor(rjd.enableMotor).setLowerAngle(rjd.lowerAngle)
          .setMaxMotorTorque(rjd.maxMotorTorque).setMotorSpeed(rjd.motorSpeed).setReferenceAngle(rjd.referenceAngle)
          .setUpperAngle(rjd.upperAngle).build();
      jointBuilder = jointBuilder.setRevoluteJointDef(crj);
      break;
    case WeldJoint:
      WeldJointDef wjd = (WeldJointDef) j.getEditableValue();
      CGWeldJointDef wj = CGWeldJointDef.newBuilder().setCollideConnected(j.isCollideConnected()).build();
      jointBuilder = jointBuilder.setWeldJointDef(wj);
      break;
    case GearJoint:
      break;
    case MouseJoint:
      break;
    case RopeJoint:
      break;
    case WheelJoint:
      break;
    case Unknown:
      break;
    default:
      break;
    }

    return jointBuilder.build();
  }

}
