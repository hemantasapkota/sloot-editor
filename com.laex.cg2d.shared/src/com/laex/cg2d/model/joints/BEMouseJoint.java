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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BEMouseJoint.
 */
public class BEMouseJoint extends Joint {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1124997086104314523L;

  /** The Constant MAX_FORCE_PROP. */
  public static final String MAX_FORCE_PROP = "MouseJoint.MaxForce";

  /** The Constant FREQUENCY_PROP. */
  public static final String FREQUENCY_PROP = "MouseJoint.Frequency";

  /** The Constant DAMPING_RATIO_PROP. */
  public static final String DAMPING_RATIO_PROP = "MouseJoint.DampingRatio";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor maxForceProp = new TextPropertyDescriptor(MAX_FORCE_PROP, "Max Force");
    PropertyDescriptor frequencyProp = new TextPropertyDescriptor(FREQUENCY_PROP, "Frequency");
    PropertyDescriptor dampingRatioProp = new TextPropertyDescriptor(DAMPING_RATIO_PROP, "Damping Ratio");

    descriptors = new IPropertyDescriptor[]
      { maxForceProp, frequencyProp, dampingRatioProp };
  }

  /** The mouse joint def. */
  private MouseJointDef mouseJointDef = new MouseJointDef();

  /**
   * Instantiates a new bE mouse joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEMouseJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return mouseJointDef;
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
      mouseJointDef.maxForce = FloatUtil.toFloat(value);
    } else if (isFrequencyProp(id)) {
      mouseJointDef.frequencyHz = FloatUtil.toFloat(value);
    } else if (isDampingRatioProp(id)) {
      mouseJointDef.dampingRatio = FloatUtil.toFloat(value);
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
      return Float.toString(mouseJointDef.maxForce);
    }
    if (isFrequencyProp(id)) {
      return Float.toString(mouseJointDef.frequencyHz);
    }
    if (isDampingRatioProp(id)) {
      return Float.toString(mouseJointDef.dampingRatio);
    }

    return super.getPropertyValue(id);
  }

  /**
   * Checks if is damping ratio prop.
   * 
   * @param id
   *          the id
   * @return true, if is damping ratio prop
   */
  private boolean isDampingRatioProp(Object id) {
    return DAMPING_RATIO_PROP.equals(id);
  }

  /**
   * Checks if is frequency prop.
   * 
   * @param id
   *          the id
   * @return true, if is frequency prop
   */
  private boolean isFrequencyProp(Object id) {
    return FREQUENCY_PROP.equals(id);
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

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.MouseJoint;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.model.model.Joint#computeLocalAnchors(int)
   */
  @Override
  public void computeLocalAnchors(int ptmRatio) {
    // TODO Auto-generated method stub
    
  }

}
