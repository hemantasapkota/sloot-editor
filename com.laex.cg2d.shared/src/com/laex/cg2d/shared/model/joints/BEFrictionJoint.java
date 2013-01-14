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
package com.laex.cg2d.shared.model.joints;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.laex.cg2d.shared.model.Joint;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.util.FloatUtil;

/**
 * The Class BEFrictionJoint.
 */
public class BEFrictionJoint extends Joint {

  /** The Constant MAX_FORCE_PROP. */
  public static final String MAX_FORCE_PROP = "FrictionJoint.MaxForce";

  /** The Constant MAX_TORQUE_PROP. */
  public static final String MAX_TORQUE_PROP = "FrictionJoint.MaxTorque";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor maxForceProp = new TextPropertyDescriptor(MAX_FORCE_PROP, "Max Force");
    PropertyDescriptor maxTorqueProp = new TextPropertyDescriptor(MAX_TORQUE_PROP, " Max Torque");

    descriptors = new IPropertyDescriptor[]
      { maxForceProp, maxTorqueProp };
  }

  /** The friction joint def. */
  private FrictionJointDef frictionJointDef = new FrictionJointDef();

  /**
   * Instantiates a new bE friction joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEFrictionJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return frictionJointDef;
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
   * @see com.laex.cg2d.shared.model.Joint#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isMaxForceProp(id)) {
      frictionJointDef.maxForce = FloatUtil.toFloat(value);
    } else if (isMaxTorqueProp(id)) {
      frictionJointDef.maxTorque = FloatUtil.toFloat(value);
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
    if (isMaxForceProp(id)) {
      return Float.toString(frictionJointDef.maxForce);
    }
    if (isMaxTorqueProp(id)) {
      return Float.toString(frictionJointDef.maxTorque);
    }
    return super.getPropertyValue(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.FrictionJoint;
  }

  /**
   * Checks if is max force prop.
   * 
   * @param id
   *          the id
   * @return true, if is max force prop
   */
  private boolean isMaxForceProp(Object id) {
    return MAX_FORCE_PROP.equals(id);
  }

  /**
   * Checks if is max torque prop.
   * 
   * @param id
   *          the id
   * @return true, if is max torque prop
   */
  private boolean isMaxTorqueProp(Object id) {
    return MAX_TORQUE_PROP.equals(id);
  }

}
