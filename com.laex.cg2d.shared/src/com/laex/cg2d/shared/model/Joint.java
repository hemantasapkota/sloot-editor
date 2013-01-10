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
package com.laex.cg2d.shared.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.shared.util.BooleanUtil;

/**
 * The Class Joint.
 */
public abstract class Joint extends ModelElement {

  /**
   * This class exists because JointDef (LibGDX Box2D) cannot be instantiated.
   * This change happened from LibGDX 0.9.6
   * 
   * @author hemantasapkota
   * 
   */
  public class BEJointDef extends JointDef {
  }

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3077525391780314571L;

  /** The Constant JOINT_TYPE_PROP. */
  public static final String JOINT_TYPE_PROP = "JointDef.JointType";
  
  /** The Constant COLLIDE_CONNECTED_PROP. */
  public static final String COLLIDE_CONNECTED_PROP = "JointDef.CollideConnected";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  /** The is connected. */
  private Boolean isConnected = Boolean.FALSE;
  
  /** The source. */
  private Shape source;
  
  /** The target. */
  private Shape target;

  /** The joint def. */
  private BEJointDef jointDef = new BEJointDef();

  // For some strange reason, jointDef.collideConnected cannot persist the
  // boolean value.
  // Therefore, an extra booelan had to be created. This correctly persists
  // the value.
  // private boolean coolideConnected = false;

  static {
    PropertyDescriptor jointType = new TextPropertyDescriptor(JOINT_TYPE_PROP, "#Type");
    PropertyDescriptor collideConnectedProp = new ComboBoxPropertyDescriptor(COLLIDE_CONNECTED_PROP,
        "#Collided Connected", BooleanUtil.BOOLEAN_STRING_VALUES);

    descriptors = new PropertyDescriptor[]
      { jointType, collideConnectedProp };
  }

  /**
   * Instantiates a new joint.
   *
   * @param source the source
   * @param target the target
   */
  public Joint(Shape source, Shape target) {
    reconnect(source, target);
    jointDef.type = getJointType();
  }

  /**
   * Gets the joint type.
   *
   * @return the joint type
   */
  public abstract JointType getJointType();

  /* (non-Javadoc)
   * @see com.laex.cg2d.shared.model.ModelElement#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return jointDef;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.shared.model.ModelElement#setPropertyValue(java.lang.Object, java.lang.Object)
   */
  public void setPropertyValue(Object id, Object value) {
    if (isCollideConnected(id)) {

      this.jointDef.collideConnected = BooleanUtil.toBool(value);
      
    } else {
      super.setPropertyValue(id, value);
    }
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.shared.model.ModelElement#getPropertyValue(java.lang.Object)
   */
  public Object getPropertyValue(Object id) {
    if (isCollideConnected(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.jointDef.collideConnected);
    }

    if (isJointTypeProp(id)) {
      return jointDef.type.toString();
    }
    return super.getPropertyValue(id);
  }

  /**
   * Checks if is joint type prop.
   *
   * @param id the id
   * @return true, if is joint type prop
   */
  private boolean isJointTypeProp(Object id) {
    return JOINT_TYPE_PROP.equals(id);
  }

  /**
   * Checks if is collide connected.
   *
   * @param id the id
   * @return true, if is collide connected
   */
  private boolean isCollideConnected(Object id) {
    return COLLIDE_CONNECTED_PROP.equals(id);
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.shared.model.ModelElement#getPropertyDescriptors()
   */
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return descriptors;
  }

  /**
   * Gets the source.
   *
   * @return the source
   */
  public Shape getSource() {
    return source;
  }

  /**
   * Gets the target.
   *
   * @return the target
   */
  public Shape getTarget() {
    return target;
  }

  /**
   * Reconnect.
   */
  public void reconnect() {
    if (!isConnected) {
      source.addJoint(this);
      target.addJoint(this);
      isConnected = true;
    }
  }

  /**
   * Disconnect.
   */
  public void disconnect() {
    if (isConnected) {
      source.removeJoint(this);
      target.removeJoint(this);
      isConnected = false;
    }
  }

  /**
   * Reconnect.
   *
   * @param newSource the new source
   * @param newTarget the new target
   */
  public void reconnect(Shape newSource, Shape newTarget) {
    if (newSource == null || newTarget == null || newSource == newTarget) {
      throw new IllegalArgumentException();
    }
    disconnect();
    this.source = newSource;
    this.target = newTarget;
    reconnect();
  }

}
