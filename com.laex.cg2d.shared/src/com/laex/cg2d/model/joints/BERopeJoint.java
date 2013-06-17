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
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class BERopeJoint.
 */
public class BERopeJoint extends Joint {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 6878388926640428584L;

  /** The Constant MAX_LENGTH_PROP. */
  public static final String MAX_LENGTH_PROP = "RopeJointDef.MaxLength";

  /** The descriptor. */
  private static PropertyDescriptor[] descriptor;
  static {
    PropertyDescriptor lengthProp = new TextPropertyDescriptor(MAX_LENGTH_PROP, "MaxLength");
    descriptor = new PropertyDescriptor[]
      { lengthProp };
  }

  /** The rope joint def. */
  private RopeJointDef ropeJointDef = new RopeJointDef();

  /**
   * Instantiates a new bE rope joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BERopeJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return this.ropeJointDef;
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
   * @see com.laex.cg2d.shared.model.Joint#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (MAX_LENGTH_PROP.equals(id)) {
      return FloatUtil.toString(ropeJointDef.maxLength);
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
    if (MAX_LENGTH_PROP.equals(id)) {
      ropeJointDef.maxLength = FloatUtil.toFloat(value);
    }
    super.setPropertyValue(id, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.RopeJoint;
  }

}
