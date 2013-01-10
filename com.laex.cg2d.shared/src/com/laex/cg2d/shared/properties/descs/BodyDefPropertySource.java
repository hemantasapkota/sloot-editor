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
package com.laex.cg2d.shared.properties.descs;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.laex.cg2d.shared.util.BooleanUtil;
import com.laex.cg2d.shared.util.Box2DUtil;
import com.laex.cg2d.shared.util.FloatUtil;
import com.laex.cg2d.shared.util.IntegerUtil;

/**
 * The Class BodyDefPropertySource.
 */
public class BodyDefPropertySource implements IPropertySource {

  /** The Constant TYPE_PROP. */
  public static final String TYPE_PROP = "BodyDef.type";
  
  /** The Constant LINEAR_VELOCITY. */
  public static final String LINEAR_VELOCITY = "BodyDef.linearVelocity";
  
  /** The Constant ANGLE_PROP. */
  public static final String ANGLE_PROP = "BodyDef.angle";
  
  /** The Constant ANGULAR_VELOCITY. */
  public static final String ANGULAR_VELOCITY = "BodyDef.angularVelocity";
  
  /** The Constant LINEAR_DAMPING_PROP. */
  public static final String LINEAR_DAMPING_PROP = "BodyDef.LinearDamping";
  
  /** The Constant ANGULAR_DAMPING_PROP. */
  public static final String ANGULAR_DAMPING_PROP = "BodyDef.AngularDamping";
  
  /** The Constant ALLOW_SLEEP_PROP. */
  public static final String ALLOW_SLEEP_PROP = "BodyDef.AllowSleep";
  
  /** The Constant AWAKE_PROP. */
  public static final String AWAKE_PROP = "BodyDef.Awake";
  
  /** The Constant FIXED_ROTATION_PROP. */
  public static final String FIXED_ROTATION_PROP = "BodyDef.FixedRotation";
  
  /** The Constant BULLET_PROP. */
  public static final String BULLET_PROP = "BodyDef.Bullet";
  
  /** The Constant ACTIVE_PROP. */
  public static final String ACTIVE_PROP = "BodyDef.Active";
  
  /** The Constant GRAVITY_SCALE_PROP. */
  private static final Object GRAVITY_SCALE_PROP = "BodyDef.gravityScale";

  /** The Constant BOOL_STR. */
  public static final String[] BOOL_STR = BooleanUtil.BOOLEAN_STRING_VALUES;

  /** The descriptors. */
  protected static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor typeProp = new ComboBoxPropertyDescriptor(TYPE_PROP, "Type", Box2DUtil.getBox2DTypes());
    PropertyDescriptor linearVelocityProp = new PropertyDescriptor(LINEAR_VELOCITY, "Linear Velocity");
    PropertyDescriptor angleProp = new TextPropertyDescriptor(ANGLE_PROP, "Angle");
    PropertyDescriptor angularVelocityProp = new TextPropertyDescriptor(ANGULAR_VELOCITY, "Angular Velocity");
    PropertyDescriptor linearDampingProp = new TextPropertyDescriptor(LINEAR_DAMPING_PROP, "Linear Damping");
    PropertyDescriptor angularDampingProp = new TextPropertyDescriptor(ANGULAR_DAMPING_PROP, "Angular Damping");
    PropertyDescriptor allowSleepProp = new ComboBoxPropertyDescriptor(ALLOW_SLEEP_PROP, "Allow Sleep", BOOL_STR);
    PropertyDescriptor awakeProp = new ComboBoxPropertyDescriptor(AWAKE_PROP, "Awake", BOOL_STR);
    PropertyDescriptor fxdRotProp = new ComboBoxPropertyDescriptor(FIXED_ROTATION_PROP, "Fixed Rotation", BOOL_STR);
    PropertyDescriptor bulletProp = new ComboBoxPropertyDescriptor(BULLET_PROP, "Bullet", BOOL_STR);
    PropertyDescriptor activeProp = new ComboBoxPropertyDescriptor(ACTIVE_PROP, "Active", BOOL_STR);
    PropertyDescriptor gravityScaleProp = new TextPropertyDescriptor(GRAVITY_SCALE_PROP, "Gravity Scale");

    descriptors = new IPropertyDescriptor[]
      {
          typeProp,
          linearVelocityProp,
          angleProp,
          angularVelocityProp,
          linearDampingProp,
          angularDampingProp,
          allowSleepProp,
          awakeProp,
          fxdRotProp,
          bulletProp,
          activeProp,
          gravityScaleProp };
  }

  /** The body def. */
  protected BodyDef bodyDef;

  /**
   * Instantiates a new body def property source.
   *
   * @param bodyDef the body def
   */
  public BodyDefPropertySource(BodyDef bodyDef) {
    this.bodyDef = bodyDef;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return bodyDef;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return descriptors;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (isTypeProp(id)) {
      return getIndexOfBodyType(bodyDef.type);
    }
    if (isLinearVelocityProp(id)) {
      return new Vec2PropertySource(bodyDef.linearVelocity);
    }
    if (isAngularVelocityProp(id)) {
      return Float.toString(bodyDef.angularVelocity);
    }
    if (isAwakeProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(bodyDef.awake);
    }
    if (isActiveProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(bodyDef.active);
    }
    if (isAngleProp(id)) {
      return Float.toString((float) Math.toDegrees(bodyDef.angle));
    }

    if (isLinearDampingProp(id)) {
      return String.valueOf(this.bodyDef.linearDamping);
    }
    if (isAngularDampingProp(id)) {
      return String.valueOf(this.bodyDef.angularDamping);
    }
    if (isBulletProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.bodyDef.bullet);
    }
    if (isFixedRotationProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.bodyDef.fixedRotation);
    }
    if (isAllowSleepProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.bodyDef.allowSleep);
    }

    if (isGravityScaleProp(id)) {
      return FloatUtil.toString(this.bodyDef.gravityScale);
    }
    return null;
  }

  /**
   * Checks if is gravity scale prop.
   *
   * @param id the id
   * @return true, if is gravity scale prop
   */
  private boolean isGravityScaleProp(Object id) {
    return GRAVITY_SCALE_PROP.equals(id);
  }

  /**
   * Checks if is type prop.
   *
   * @param id the id
   * @return true, if is type prop
   */
  private boolean isTypeProp(Object id) {
    return TYPE_PROP.equals(id);
  }

  /**
   * Checks if is linear velocity prop.
   *
   * @param id the id
   * @return true, if is linear velocity prop
   */
  private boolean isLinearVelocityProp(Object id) {
    return LINEAR_VELOCITY.equals(id);
  }

  /**
   * Checks if is angular velocity prop.
   *
   * @param id the id
   * @return true, if is angular velocity prop
   */
  private boolean isAngularVelocityProp(Object id) {
    return ANGULAR_VELOCITY.equals(id);
  }

  /**
   * Checks if is awake prop.
   *
   * @param id the id
   * @return true, if is awake prop
   */
  private boolean isAwakeProp(Object id) {
    return AWAKE_PROP.equals(id);
  }

  /**
   * Checks if is active prop.
   *
   * @param id the id
   * @return true, if is active prop
   */
  private boolean isActiveProp(Object id) {
    return ACTIVE_PROP.equals(id);
  }

  /**
   * Checks if is allow sleep prop.
   *
   * @param id the id
   * @return true, if is allow sleep prop
   */
  private boolean isAllowSleepProp(Object id) {
    return ALLOW_SLEEP_PROP.equals(id);
  }

  /**
   * Checks if is fixed rotation prop.
   *
   * @param id the id
   * @return true, if is fixed rotation prop
   */
  private boolean isFixedRotationProp(Object id) {
    return FIXED_ROTATION_PROP.equals(id);
  }

  /**
   * Checks if is bullet prop.
   *
   * @param id the id
   * @return true, if is bullet prop
   */
  private boolean isBulletProp(Object id) {
    return BULLET_PROP.equals(id);
  }

  /**
   * Checks if is angular damping prop.
   *
   * @param id the id
   * @return true, if is angular damping prop
   */
  private boolean isAngularDampingProp(Object id) {
    return ANGULAR_DAMPING_PROP.equals(id);
  }

  /**
   * Checks if is linear damping prop.
   *
   * @param id the id
   * @return true, if is linear damping prop
   */
  private boolean isLinearDampingProp(Object id) {
    return LINEAR_DAMPING_PROP.equals(id);
  }

  /**
   * Checks if is angle prop.
   *
   * @param id the id
   * @return true, if is angle prop
   */
  private boolean isAngleProp(Object id) {
    return ANGLE_PROP.equals(id);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
   */
  @Override
  public boolean isPropertySet(Object id) {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
   */
  @Override
  public void resetPropertyValue(Object id) {
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isTypeProp(id)) {
      this.bodyDef.type = getBodyType(IntegerUtil.toInt(value));
    } else if (isLinearVelocityProp(id)) {
      // this.bodyDef.linearVelocity = (Vector2) value;
    } else if (isAngularVelocityProp(id)) {
      this.bodyDef.angularVelocity = FloatUtil.toFloat(value);
    } else if (isActiveProp(id)) {
      this.bodyDef.active = BooleanUtil.toBool(value);
    } else if (isAwakeProp(id)) {
      this.bodyDef.awake = BooleanUtil.toBool(value);
    } else if (isAngleProp(id)) {
      this.bodyDef.angle = (float) Math.toRadians(FloatUtil.toFloat(value));
    } else if (isLinearDampingProp(id)) {
      this.bodyDef.linearDamping = FloatUtil.toFloat(value);
    } else if (isAngularDampingProp(id)) {
      this.bodyDef.angularDamping = FloatUtil.toFloat(value);
    } else if (isFixedRotationProp(id)) {
      this.bodyDef.fixedRotation = BooleanUtil.toBool(value);
    } else if (isBulletProp(id)) {
      this.bodyDef.bullet = BooleanUtil.toBool(value);
    } else if (isAllowSleepProp(id)) {
      this.bodyDef.allowSleep = BooleanUtil.toBool(value);
    } else if (isGravityScaleProp(id)) {
      this.bodyDef.gravityScale = FloatUtil.toFloat(value);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "<<body def>>";
  }

  /**
   * Gets the index of body type.
   *
   * @param type the type
   * @return the index of body type
   */
  private int getIndexOfBodyType(BodyType type) {
    switch (type) {
    case StaticBody:
      return 0;
    case KinematicBody:
      return 1;
    case DynamicBody:
      return 2;
    default:
      return -1;
    }
  }

  /**
   * Gets the body type.
   *
   * @param index the index
   * @return the body type
   */
  private BodyType getBodyType(int index) {
    switch (index) {
    case 0:
      return BodyType.StaticBody;
    case 1:
      return BodyType.KinematicBody;
    case 2:
      return BodyType.DynamicBody;
    default:
      return BodyType.StaticBody;
    }
  }

}
