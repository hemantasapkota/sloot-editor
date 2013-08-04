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
package com.laex.cg2d.model.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.model.descs.Vec2PropertySource;
import com.laex.cg2d.model.util.BooleanUtil;

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

  /** The Constant JOINT_TYPE_PROP. */
  public static final String JOINT_TYPE_PROP = "JointDef.JointType";

  /** The Constant COLLIDE_CONNECTED_PROP. */
  public static final String COLLIDE_CONNECTED_PROP = "JointDef.CollideConnected";

  /** The Constant LOCAL_ANCHOR_A. */
  public static final String LOCAL_ANCHOR_A = "JointDef.LocalAnchorA";

  /** The Constant LOCAL_ANCHOR_B. */
  public static final String LOCAL_ANCHOR_B = "JointDef.LocalAnchorB";

  /** The Constant USE_LOCAL_ANCHORS. */
  public static final String USE_LOCAL_ANCHORS = "JointDef.UseLocalAnchors";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  /** The is connected. */
  private Boolean isConnected = Boolean.FALSE;

  /** The source. */
  private Shape source;

  /** The target. */
  private Shape target;

  /** The local anchor a. */
  private Vector2 localAnchorA = new Vector2();

  /** The local anchor b. */
  private Vector2 localAnchorB = new Vector2();

  /** The joint def. */
  private BEJointDef jointDef = new BEJointDef();

  // For some strange reason, jointDef.collideConnected cannot persist the
  // boolean value.
  // Therefore, an extra booelan had to be created. This correctly persists
  // the value.
  /** The coolide connected. */
  private boolean coolideConnected = false;

  /** The use local anchors. */
  private boolean useLocalAnchors = false;

  static {
    PropertyDescriptor jointType = new TextPropertyDescriptor(JOINT_TYPE_PROP, "1.Type");
    PropertyDescriptor collideConnectedProp = new ComboBoxPropertyDescriptor(COLLIDE_CONNECTED_PROP,
        "2.Collided Connected", BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor useLocalAnchors = new ComboBoxPropertyDescriptor(USE_LOCAL_ANCHORS, "3.Use Local Anchors",
        BooleanUtil.BOOLEAN_STRING_VALUES);

    PropertyDescriptor locAncA = new PropertyDescriptor(LOCAL_ANCHOR_A, "4.localAnchorA");
    PropertyDescriptor locAncB = new PropertyDescriptor(LOCAL_ANCHOR_B, "5.localAnchorB");

    descriptors = new PropertyDescriptor[]
      { jointType, collideConnectedProp, useLocalAnchors, locAncA, locAncB };
  }

  /**
   * Instantiates a new joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
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

  /**
   * Compute local anchors.
   *
   * @param ptmRatio the ptm ratio
   */
  public abstract void computeLocalAnchors(int ptmRatio);

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.ModelElement#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return jointDef;
  }

  /**
   * Checks if is collide connected.
   * 
   * @return true, if is collide connected
   */
  public boolean isCollideConnected() {
    return coolideConnected;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  public void setPropertyValue(Object id, Object value) {
    
    if (shouldUseLocalAnchors(id)) {
      
      this.useLocalAnchors = BooleanUtil.toBool(value);
      
    } else
    if (isCollideConnected(id)) {

      this.jointDef.collideConnected = BooleanUtil.toBool(value);
      this.coolideConnected = BooleanUtil.toBool(value);

    } else if (isLocalAnchorA(id)) {

      this.localAnchorA = (Vector2) value;

    } else if (isLocalAnchorB(id)) {

      this.localAnchorB = (Vector2) value;

    } else {
      super.setPropertyValue(id, value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#getPropertyValue(java.lang.Object)
   */
  public Object getPropertyValue(Object id) {
    
    if (shouldUseLocalAnchors(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.useLocalAnchors);
    }
    
    
    if (isCollideConnected(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.coolideConnected);
    }

    if (isLocalAnchorA(id)) {
      return new Vec2PropertySource(localAnchorA);
    }

    if (isLocalAnchorB(id)) {
      return new Vec2PropertySource(localAnchorB);
    }

    if (isJointTypeProp(id)) {
      return jointDef.type.toString();
    }
    return super.getPropertyValue(id);
  }

  /**
   * Checks if is joint type prop.
   * 
   * @param id
   *          the id
   * @return true, if is joint type prop
   */
  private boolean isJointTypeProp(Object id) {
    return JOINT_TYPE_PROP.equals(id);
  }

  /**
   * Checks if is local anchor a.
   *
   * @param id the id
   * @return true, if is local anchor a
   */
  private boolean isLocalAnchorA(Object id) {
    return LOCAL_ANCHOR_A.equals(id);
  }

  /**
   * Checks if is local anchor b.
   *
   * @param id the id
   * @return true, if is local anchor b
   */
  private boolean isLocalAnchorB(Object id) {
    return LOCAL_ANCHOR_B.equals(id);
  }

  /**
   * Should use local anchors.
   *
   * @param id the id
   * @return true, if successful
   */
  private boolean shouldUseLocalAnchors(Object id) {
    return USE_LOCAL_ANCHORS.equals(id);
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
   * Gets the local anchor a.
   *
   * @return the local anchor a
   */
  public Vector2 getLocalAnchorA() {
    return localAnchorA;
  }

  /**
   * Sets the local anchor a.
   *
   * @param localAnchorA the new local anchor a
   */
  public void setLocalAnchorA(Vector2 localAnchorA) {
    this.localAnchorA = localAnchorA;
  }

  /**
   * Gets the local anchor b.
   *
   * @return the local anchor b
   */
  public Vector2 getLocalAnchorB() {
    return localAnchorB;
  }
  
  /**
   * Should use local anchors.
   *
   * @return true, if successful
   */
  public boolean shouldUseLocalAnchors() {
    return useLocalAnchors;
  }
  
  /**
   * Sets the use local anchors.
   *
   * @param useLocalAnchors the new use local anchors
   */
  public void setUseLocalAnchors(boolean useLocalAnchors) {
    this.useLocalAnchors = useLocalAnchors;
  }

  /**
   * Sets the local anchor b.
   *
   * @param localAnchorB the new local anchor b
   */
  public void setLocalAnchorB(Vector2 localAnchorB) {
    this.localAnchorB = localAnchorB;
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
   * @param newSource
   *          the new source
   * @param newTarget
   *          the new target
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
