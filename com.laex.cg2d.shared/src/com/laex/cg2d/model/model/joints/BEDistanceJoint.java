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
package com.laex.cg2d.model.model.joints;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BEDistanceJoint.
 */
public class BEDistanceJoint extends Joint {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1876345217033544389L;

  /** The Constant LENGTH_PROP. */
  public static final String LENGTH_PROP = "DistanceJoint.Length";

  /** The Constant FREUENCY_HZ_PROP. */
  public static final String FREUENCY_HZ_PROP = "DistanceJoint.FrequencyHZ";

  /** The Constant DAMPING_RATIO. */
  public static final String DAMPING_RATIO = "DistanceJoint.DampingRatio";

  /** The descriptor. */
  private static PropertyDescriptor[] descriptor;

  static {
    PropertyDescriptor lengthProp = new TextPropertyDescriptor(LENGTH_PROP, "Length");
    PropertyDescriptor frequencyHzProp = new TextPropertyDescriptor(FREUENCY_HZ_PROP, "Frequency");
    PropertyDescriptor dampingRatioProp = new TextPropertyDescriptor(DAMPING_RATIO, "Damping Ratio");

    descriptor = new PropertyDescriptor[]
      { lengthProp, frequencyHzProp, dampingRatioProp };

  }

  /** The distance joint def. */
  private DistanceJointDef distanceJointDef = new DistanceJointDef();

  /**
   * Instantiates a new bE distance joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEDistanceJoint(Shape source, Shape target) {
    super(source, target);
    calculateLength();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return distanceJointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (isLengthProp(id)) {
      return FloatUtil.toString(this.distanceJointDef.length);
    }
    if (isFrequencyHzProp(id)) {
      return FloatUtil.toString(this.distanceJointDef.frequencyHz);
    }
    if (isDampingRatioProp(id)) {
      return FloatUtil.toString(this.distanceJointDef.dampingRatio);
    }
    return super.getPropertyValue(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isFrequencyHzProp(id)) {
      this.distanceJointDef.frequencyHz = FloatUtil.toFloat(value);
    } else if (isDampingRatioProp(id)) {
      this.distanceJointDef.dampingRatio = FloatUtil.toFloat(value);
    } else {
      super.setPropertyValue(id, value);
    }
  }

  /**
   * Checks if is damping ratio prop.
   * 
   * @param id
   *          the id
   * @return true, if is damping ratio prop
   */
  private boolean isDampingRatioProp(Object id) {
    return DAMPING_RATIO.equals(id);
  }

  /**
   * Checks if is frequency hz prop.
   * 
   * @param id
   *          the id
   * @return true, if is frequency hz prop
   */
  private boolean isFrequencyHzProp(Object id) {
    return FREUENCY_HZ_PROP.equals(id);
  }

  /**
   * Checks if is length prop.
   * 
   * @param id
   *          the id
   * @return true, if is length prop
   */
  private boolean isLengthProp(Object id) {
    return LENGTH_PROP.equals(id);
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
   * @see com.laex.cg2d.shared.model.Joint#reconnect()
   */
  @Override
  public void reconnect() {
    super.reconnect();
    // Calculate length in reconnection
    calculateLength();
  }

  /**
   * Calculate length.
   */
  public void calculateLength() {
    if (distanceJointDef != null) {
      Rectangle b1 = getSource().getBounds();
      Rectangle b2 = getTarget().getBounds();
      int length = (int) Math.sqrt(Math.pow(b2.x - b1.x, 2) + Math.pow(b2.y - b1.y, 2));
      distanceJointDef.length = length;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.DistanceJoint;
  }

}
