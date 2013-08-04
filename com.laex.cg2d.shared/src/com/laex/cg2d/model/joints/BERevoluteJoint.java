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

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.BooleanUtil;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BERevoluteJoint.
 */
public class BERevoluteJoint extends Joint {

  /** The Constant REFERENCE_ANGLE_PROP. */
  public static final String REFERENCE_ANGLE_PROP = "RevoluteJoint.ReferenceAngle";

  /** The Constant ENABLE_LIMIT_PROP. */
  public static final String ENABLE_LIMIT_PROP = "RevoluteJoint.EnableLimit";

  /** The Constant LOWER_ANGLE_PROP. */
  public static final String LOWER_ANGLE_PROP = "RevoluteJoint.LowerAngle";

  /** The Constant UPPER_ANGLE_PROP. */
  public static final String UPPER_ANGLE_PROP = "RevoluteJoint.UpperAngle";

  /** The Constant ENABLE_MOTOR_PROP. */
  public static final String ENABLE_MOTOR_PROP = "RevoluteJoint.EnableMotor";

  /** The Constant MOTOR_SPEED_PROP. */
  public static final String MOTOR_SPEED_PROP = "RevoluteJoint.MotorSpeed";

  /** The Constant MAX_MOTOR_TORQUE_PROP. */
  public static final String MAX_MOTOR_TORQUE_PROP = "RevoluteJoint.MaxMotorTorque";


  /** The descriptor. */
  private static IPropertyDescriptor[] descriptor;

  static {
    PropertyDescriptor referenceAngleProp = new TextPropertyDescriptor(REFERENCE_ANGLE_PROP, "Reference Angle");
    PropertyDescriptor enableLimitProp = new ComboBoxPropertyDescriptor(ENABLE_LIMIT_PROP, "Enable Limit",
        BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor lowerAngleProp = new TextPropertyDescriptor(LOWER_ANGLE_PROP, "Lower Angle");
    PropertyDescriptor upperAngleProp = new TextPropertyDescriptor(UPPER_ANGLE_PROP, "Upper Angle");
    PropertyDescriptor enableMotorProp = new ComboBoxPropertyDescriptor(ENABLE_MOTOR_PROP, "Enable Motor",
        BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor motorSpeedProp = new TextPropertyDescriptor(MOTOR_SPEED_PROP, "Motor Speed");
    PropertyDescriptor maxMotorTorqueProp = new TextPropertyDescriptor(MAX_MOTOR_TORQUE_PROP, "Max Motor Torque");

    descriptor = new IPropertyDescriptor[]
      {
          referenceAngleProp,
          enableLimitProp,
          lowerAngleProp,
          upperAngleProp,
          enableMotorProp,
          motorSpeedProp,
          maxMotorTorqueProp
          };
  };

  /** The revolute joint def. */
  private RevoluteJointDef revoluteJointDef = new RevoluteJointDef();

  /**
   * Instantiates a new bE revolute joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BERevoluteJoint(Shape source, Shape target) {
    super(source, target);
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return revoluteJointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return (IPropertyDescriptor[]) ArrayUtils.addAll(super.getPropertyDescriptors(), descriptor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isReferenceAngleProp(id)) {
      revoluteJointDef.referenceAngle = (float) Math.toRadians(FloatUtil.toFloat(value));
    } else if (isEnableLimitProp(id)) {
      revoluteJointDef.enableLimit = BooleanUtil.toBool(value);
    } else if (isLowerAngleProp(id)) {
      revoluteJointDef.lowerAngle = FloatUtil.toFloat(value);
    } else if (isUpperAngleProp(id)) {
      revoluteJointDef.upperAngle = FloatUtil.toFloat(value);
    } else if (isEnableMotorProp(id)) {
      revoluteJointDef.enableMotor = BooleanUtil.toBool(value);
    } else if (isMotorSpeedProp(id)) {
      revoluteJointDef.motorSpeed = FloatUtil.toFloat(value);
    } else if (isMaxMotorTorqueProp(id)) {
      revoluteJointDef.maxMotorTorque = FloatUtil.toFloat(value);
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
    if (isReferenceAngleProp(id)) {
      return Double.toString(Math.toDegrees(revoluteJointDef.referenceAngle));
    }
    if (isEnableLimitProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(revoluteJointDef.enableLimit);
    }
    if (isLowerAngleProp(id)) {
      return Float.toString(revoluteJointDef.lowerAngle);
    }
    if (isUpperAngleProp(id)) {
      return Float.toString(revoluteJointDef.upperAngle);
    }
    if (isEnableMotorProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(revoluteJointDef.enableMotor);
    }
    if (isMotorSpeedProp(id)) {
      return Float.toString(revoluteJointDef.motorSpeed);
    }
    if (isMaxMotorTorqueProp(id)) {
      return Float.toString(revoluteJointDef.maxMotorTorque);
    }
    return super.getPropertyValue(id);
  }


  /**
   * Checks if is max motor torque prop.
   * 
   * @param id
   *          the id
   * @return true, if is max motor torque prop
   */
  private boolean isMaxMotorTorqueProp(Object id) {
    return MAX_MOTOR_TORQUE_PROP.equals(id);
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
   * Checks if is upper angle prop.
   * 
   * @param id
   *          the id
   * @return true, if is upper angle prop
   */
  private boolean isUpperAngleProp(Object id) {
    return UPPER_ANGLE_PROP.equals(id);
  }

  /**
   * Checks if is lower angle prop.
   * 
   * @param id
   *          the id
   * @return true, if is lower angle prop
   */
  private boolean isLowerAngleProp(Object id) {
    return LOWER_ANGLE_PROP.equals(id);
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
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.RevoluteJoint;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.model.model.Joint#computeLocalAnchors(int)
   */
  @Override
  public void computeLocalAnchors(int ptmRatio) {
    getLocalAnchorA().x = (getSource().getBounds().width / ptmRatio) / 2;
    getLocalAnchorA().y = (getSource().getBounds().height / ptmRatio) / 2;

    getLocalAnchorB().x = getLocalAnchorA().x;
    getLocalAnchorB().y = getLocalAnchorA().y;
  }
}
