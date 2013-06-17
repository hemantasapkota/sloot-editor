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
package com.laex.cg2d.model.descs;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.laex.cg2d.model.util.BooleanUtil;

/**
 * The Class JointDefPropertySource.
 */
public class JointDefPropertySource implements IPropertySource {

  /** The Constant JOINT_TYPE_PROP. */
  public static final String JOINT_TYPE_PROP = "JointDef.JointType";

  /** The Constant COLLIDE_CONNECTED_PROP. */
  public static final String COLLIDE_CONNECTED_PROP = "JointDef.CollideConnected";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor collideConnectedProp = new ComboBoxPropertyDescriptor(COLLIDE_CONNECTED_PROP,
        "Collided Connected", BooleanUtil.BOOLEAN_STRING_VALUES);

    descriptors = new PropertyDescriptor[]
      { collideConnectedProp };
  }

  /** The joint def. */
  protected JointDef jointDef;

  /**
   * Instantiates a new joint def property source.
   * 
   * @param jointDef
   *          the joint def
   */
  public JointDefPropertySource(JointDef jointDef) {
    this.jointDef = jointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return jointDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang
   * .Object, java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isCollideConnected(id)) {
      this.jointDef.collideConnected = BooleanUtil.toBool(value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang
   * .Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (isCollideConnected(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.jointDef.collideConnected);
    }
    return null;
  }

  /**
   * Checks if is collide connected.
   * 
   * @param id
   *          the id
   * @return true, if is collide connected
   */
  private boolean isCollideConnected(Object id) {
    return COLLIDE_CONNECTED_PROP.equals(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
   * .Object)
   */
  @Override
  public boolean isPropertySet(Object id) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
   * .lang.Object)
   */
  @Override
  public void resetPropertyValue(Object id) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return descriptors;
  }

}
