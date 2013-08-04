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

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;

/**
 * The Class BEWeldJoint.
 */
public class BEWeldJoint extends Joint {

  /** The Constant REFERENCE_ANGLE_PROP. */
  public static final String REFERENCE_ANGLE_PROP = "PrismaticJoint.ReferenceAngle";

  /** The weld joint def. */
  private WeldJointDef weldJointDef = new WeldJointDef();

  /**
   * Instantiates a new bE weld joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEWeldJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return this.weldJointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return super.getPropertyDescriptors();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    // get refernce angle ; todo
    return super.getPropertyValue(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.WeldJoint;
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
