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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.laex.cg2d.model.descs.Vec2PropertySource;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BEPulleyJoint.
 */
public class BEPulleyJoint extends Joint {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 677561183649479725L;

  /** The Constant GROUND_ANCHOR_A_PROP. */
  public static final String GROUND_ANCHOR_A_PROP = "PulleyJoint.GroundAnchorA";

  /** The Constant GROUND_ANCHOR_B_PROP. */
  public static final String GROUND_ANCHOR_B_PROP = "PulleyJoint.GroundAnchorB";

  /** The Constant RATIO_PROP. */
  public static final String RATIO_PROP = "PulleyJoint.Ratio";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor groundAnchorAProp = new PropertyDescriptor(GROUND_ANCHOR_A_PROP, "Ground Anchor A");
    PropertyDescriptor groundAnchorBProp = new PropertyDescriptor(GROUND_ANCHOR_B_PROP, "Ground Anchor B");

    PropertyDescriptor ratioProp = new TextPropertyDescriptor(RATIO_PROP, "Ratio");

    descriptors = new IPropertyDescriptor[]
      { groundAnchorAProp, groundAnchorBProp, ratioProp };
  }

  /** The pulley joint def. */
  private PulleyJointDef pulleyJointDef = new PulleyJointDef();

  /** The ground anchor b. */
  private Vector2 groundAnchorA, groundAnchorB;

  /** The ratio. */
  float ratio;

  /**
   * Instantiates a new bE pulley joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEPulleyJoint(Shape source, Shape target) {
    super(source, target);

    groundAnchorA = new Vector2(-1, 1);
    groundAnchorB = new Vector2(1, 1);
    ratio = 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return pulleyJointDef;
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

    if (isGroundAnchorA(id)) {
      this.groundAnchorA = (Vector2) value;
    } else if (isGroundAnchorB(id)) {
      this.groundAnchorB = (Vector2) value;
    } else if (isRatioProp(id)) {
      this.ratio = FloatUtil.toFloat(value);
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
    if (isGroundAnchorA(id)) {
      return new Vec2PropertySource(groundAnchorA);
    } else if (isGroundAnchorB(id)) {
      return new Vec2PropertySource(groundAnchorB);
    } else if (isRatioProp(id)) {
      return Float.toString(ratio);
    }

    return super.getPropertyValue(id);
  }

  /**
   * Checks if is ratio prop.
   * 
   * @param id
   *          the id
   * @return true, if is ratio prop
   */
  private boolean isRatioProp(Object id) {
    return RATIO_PROP.equals(id);
  }

  /**
   * Checks if is ground anchor a.
   * 
   * @param id
   *          the id
   * @return true, if is ground anchor a
   */
  private boolean isGroundAnchorA(Object id) {
    return GROUND_ANCHOR_A_PROP.equals(id);
  }

  /**
   * Checks if is ground anchor b.
   * 
   * @param id
   *          the id
   * @return true, if is ground anchor b
   */
  private boolean isGroundAnchorB(Object id) {
    return GROUND_ANCHOR_B_PROP.equals(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.PulleyJoint;
  }

  /**
   * Gets the ground anchor a.
   * 
   * @return the ground anchor a
   */
  public Vector2 getGroundAnchorA() {
    return groundAnchorA;
  }

  /**
   * Gets the ground anchor b.
   * 
   * @return the ground anchor b
   */
  public Vector2 getGroundAnchorB() {
    return groundAnchorB;
  }

  /**
   * Gets the ratio.
   * 
   * @return the ratio
   */
  public float getRatio() {
    return ratio;
  }

  @Override
  public void computeLocalAnchors(int ptmRatio) {
    // TODO Auto-generated method stub
    
  }

}
