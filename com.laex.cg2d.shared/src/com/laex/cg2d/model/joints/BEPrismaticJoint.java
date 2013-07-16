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
package com.laex.cg2d.model.joints;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.laex.cg2d.model.descs.Vec2PropertySource;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.BooleanUtil;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BEPrismaticJoint.
 */
public class BEPrismaticJoint extends Joint {

  /** The Constant AXIS_PROP. */
  public static final String AXIS_PROP = "PrismaticJoint.WorldAxis";

  /** The Constant ANCHOR_PROP. */
  public static final String ANCHOR_PROP = "PrismaticJoint.WorldAnchor";

  /** The Constant REFERENCE_ANGLE_PROP. */
  public static final String REFERENCE_ANGLE_PROP = "PrismaticJoint.ReferenceAngle";

  /** The Constant ENABLE_LIMIT_PROP. */
  public static final String ENABLE_LIMIT_PROP = "PrismaticJoint.EnableLimit";

  /** The Constant LOWER_TRANSLATION_PROP. */
  public static final String LOWER_TRANSLATION_PROP = "PrismaticJoint.LowerTranslation";

  /** The Constant UPPER_TRANSLATION_PROP. */
  public static final String UPPER_TRANSLATION_PROP = "PrismaticJoint.UpperTranslation";

  /** The Constant ENABLE_MOTOR_PROP. */
  public static final String ENABLE_MOTOR_PROP = "PrismaticJoint.EnableMotor";

  /** The Constant MAX_MOTOR_FORCE_PROP. */
  public static final String MAX_MOTOR_FORCE_PROP = "PrismaticJoint.MaxMotorForce";

  /** The Constant MOTOR_SPEED_PROP. */
  public static final String MOTOR_SPEED_PROP = "PrismaticJoint.MotorSpeed";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor axisProp = new PropertyDescriptor(AXIS_PROP, "WorldAxis");
    PropertyDescriptor anchorProp = new PropertyDescriptor(ANCHOR_PROP, "WorldAnchor");
    PropertyDescriptor referenceAngleProp = new TextPropertyDescriptor(REFERENCE_ANGLE_PROP, "Reference Angle");
    PropertyDescriptor enableLimitProp = new ComboBoxPropertyDescriptor(ENABLE_LIMIT_PROP, "Enable Limit",
        BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor lowerTranslationProp = new TextPropertyDescriptor(LOWER_TRANSLATION_PROP, "Lower Translation");
    PropertyDescriptor upperTranslationProp = new TextPropertyDescriptor(UPPER_TRANSLATION_PROP, "Upper Translation");
    PropertyDescriptor enableMotorProp = new ComboBoxPropertyDescriptor(ENABLE_MOTOR_PROP, "Enable Motor",
        BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor maxMotorForceProp = new TextPropertyDescriptor(MAX_MOTOR_FORCE_PROP, "Max Motor Force");
    PropertyDescriptor motorSpeedProp = new TextPropertyDescriptor(MOTOR_SPEED_PROP, "Motor Speed");

    descriptors = new IPropertyDescriptor[]
      {
          axisProp,
          anchorProp,
          referenceAngleProp,
          enableLimitProp,
          lowerTranslationProp,
          upperTranslationProp,
          enableMotorProp,
          maxMotorForceProp,
          motorSpeedProp };
  }

  /** The prismatic joint def. */
  private PrismaticJointDef prismaticJointDef = new PrismaticJointDef();

  /** The world axis. */
  private Vector2 worldAxis = new Vector2();

  /** The world anchor. */
  private Vector2 worldAnchor = new Vector2();

  /**
   * Instantiates a new bE prismatic joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEPrismaticJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return prismaticJointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isWorldAnchorProp(id)) {
      this.worldAnchor = (Vector2) value;
    } else if (isWorldAxisProp(id)) {
      this.worldAxis = (Vector2) value;
    } else if (isReferenceAngleProp(id)) {
      prismaticJointDef.referenceAngle = (float) Math.toRadians(FloatUtil.toFloat(value));
    } else if (isEnableLimitProp(id)) {
      prismaticJointDef.enableLimit = BooleanUtil.toBool(value);
    } else if (isLowerTranslationProp(id)) {
      prismaticJointDef.lowerTranslation = FloatUtil.toFloat(value);
    } else if (isUpperTranslationProp(id)) {
      prismaticJointDef.upperTranslation = FloatUtil.toFloat(value);
    } else if (isEnableMotorProp(id)) {
      prismaticJointDef.enableMotor = BooleanUtil.toBool(value);
    } else if (isMaxMotorForceProp(id)) {
      prismaticJointDef.maxMotorForce = FloatUtil.toFloat(value);
    } else if (isMotorSpeedProp(id)) {
      prismaticJointDef.motorSpeed = FloatUtil.toFloat(value);
    } else {
      super.setPropertyValue(id, value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (isWorldAnchorProp(id)) {
      return new Vec2PropertySource(this.worldAnchor);
    } else if (isWorldAxisProp(id)) {
      return new Vec2PropertySource(this.worldAxis);
    } else if (isReferenceAngleProp(id)) {
      return Float.toString((float) Math.toDegrees(this.prismaticJointDef.referenceAngle));
    } else if (isEnableLimitProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(prismaticJointDef.enableLimit);
    } else if (isLowerTranslationProp(id)) {
      return Float.toString(prismaticJointDef.lowerTranslation);
    } else if (isUpperTranslationProp(id)) {
      return Float.toString(prismaticJointDef.upperTranslation);
    } else if (isEnableMotorProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(prismaticJointDef.enableMotor);
    } else if (isMaxMotorForceProp(id)) {
      return Float.toString(prismaticJointDef.maxMotorForce);
    } else if (isMotorSpeedProp(id)) {
      return Float.toString(prismaticJointDef.motorSpeed);
    }
    return super.getPropertyValue(id);
  }

  /**
   * Checks if is world anchor prop.
   * 
   * @param id
   *          the id
   * @return true, if is world anchor prop
   */
  private boolean isWorldAnchorProp(Object id) {
    return ANCHOR_PROP.equals(id);
  }

  /**
   * Checks if is world axis prop.
   * 
   * @param id
   *          the id
   * @return true, if is world axis prop
   */
  private boolean isWorldAxisProp(Object id) {
    return AXIS_PROP.equals(id);
  }

  /**
   * Checks if is motor speed prop.
   * 
   * @param id
   *          the id
   * @return true, if is motor speed prop
   */
  private boolean isMotorSpeedProp(Object id) {
    return MOTOR_SPEED_PROP.equals(id);
  }

  /**
   * Checks if is max motor force prop.
   * 
   * @param id
   *          the id
   * @return true, if is max motor force prop
   */
  private boolean isMaxMotorForceProp(Object id) {
    return MAX_MOTOR_FORCE_PROP.equals(id);
  }

  /**
   * Checks if is enable motor prop.
   * 
   * @param id
   *          the id
   * @return true, if is enable motor prop
   */
  private boolean isEnableMotorProp(Object id) {
    return ENABLE_MOTOR_PROP.equals(id);
  }

  /**
   * Checks if is upper translation prop.
   * 
   * @param id
   *          the id
   * @return true, if is upper translation prop
   */
  private boolean isUpperTranslationProp(Object id) {
    return UPPER_TRANSLATION_PROP.equals(id);
  }

  /**
   * Checks if is lower translation prop.
   * 
   * @param id
   *          the id
   * @return true, if is lower translation prop
   */
  private boolean isLowerTranslationProp(Object id) {
    return LOWER_TRANSLATION_PROP.equals(id);
  }

  /**
   * Checks if is enable limit prop.
   * 
   * @param id
   *          the id
   * @return true, if is enable limit prop
   */
  private boolean isEnableLimitProp(Object id) {
    return ENABLE_LIMIT_PROP.equals(id);
  }

  /**
   * Checks if is reference angle prop.
   * 
   * @param id
   *          the id
   * @return true, if is reference angle prop
   */
  private boolean isReferenceAngleProp(Object id) {
    return REFERENCE_ANGLE_PROP.equals(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return (IPropertyDescriptor[]) ArrayUtils.addAll(super.getPropertyDescriptors(), descriptors);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.PrismaticJoint;
  }

  /**
   * Gets the world anchor.
   * 
   * @return the world anchor
   */
  public Vector2 getWorldAnchor() {
    return worldAnchor;
  }

  /**
   * Gets the world axis.
   * 
   * @return the world axis
   */
  public Vector2 getWorldAxis() {
    return worldAxis;
  }

  @Override
  public void computeLocalAnchors(int ptmRatio) {
    getLocalAnchorA().x = (getSource().getBounds().width / ptmRatio) / 2;
    getLocalAnchorA().y = (getSource().getBounds().height / ptmRatio) / 2;

    getLocalAnchorB().x = getLocalAnchorA().x;
    getLocalAnchorB().y = getLocalAnchorA().y;
    
    /* Compute world anchor for prismastic joint */
    Vector2 a1 = new Vector2(getSource().getBounds().x, getSource().getBounds().y);
    Vector2 a2 = new Vector2(getTarget().getBounds().x, getTarget().getBounds().y);
    
    Vector2 avg = a1.add(a2).div(2).div(ptmRatio);
    
    getWorldAnchor().x = avg.x;
    getWorldAnchor().y = avg.y;
  }

}
