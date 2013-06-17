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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.MassData;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class MassDataPropertySource.
 */
public class MassDataPropertySource implements IPropertySource {

  /** The Constant TO_STRING. */
  private static final String TO_STRING = "( %s, %f, %f)";

  /** The mass prop. */
  public static String MASS_PROP = "mass";

  /** The rotational inertia prop. */
  public static String ROTATIONAL_INERTIA_PROP = "I";

  /** The center prop. */
  public static String CENTER_PROP = "center";

  /** The descriptors. */
  protected static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor centerProp = new PropertyDescriptor(CENTER_PROP, "Center");
    PropertyDescriptor massProp = new TextPropertyDescriptor(MASS_PROP, "Mass");
    PropertyDescriptor riProp = new TextPropertyDescriptor(ROTATIONAL_INERTIA_PROP, "Rotational Inertia");
    descriptors = new IPropertyDescriptor[]
      { centerProp, massProp, riProp };
  }

  /** The mass data. */
  protected MassData massData = null;

  /**
   * Instantiates a new mass data property source.
   * 
   * @param massdata
   *          the massdata
   */
  public MassDataPropertySource(MassData massdata) {
    this.massData = massdata;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return this.massData;
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

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang
   * .Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (isCenterProp(id)) {
      return new Vec2PropertySource(massData.center);
    }
    if (isMassProp(id)) {
      return String.valueOf(massData.mass);
    }
    if (isRotationalInertiaProp(id)) {
      return String.valueOf(massData.I);
    }
    return null;
  }

  /**
   * Checks if is center prop.
   * 
   * @param id
   *          the id
   * @return true, if is center prop
   */
  private boolean isCenterProp(Object id) {
    return id.equals(CENTER_PROP);
  }

  /**
   * Checks if is rotational inertia prop.
   * 
   * @param id
   *          the id
   * @return true, if is rotational inertia prop
   */
  private boolean isRotationalInertiaProp(Object id) {
    return id.equals(ROTATIONAL_INERTIA_PROP);
  }

  /**
   * Checks if is mass prop.
   * 
   * @param id
   *          the id
   * @return true, if is mass prop
   */
  private boolean isMassProp(Object id) {
    return id.equals(MASS_PROP);
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
    return true;
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
   * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang
   * .Object, java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    setPropertyValue((String) id, value);
  }

  /**
   * Sets the property value.
   * 
   * @param propName
   *          the prop name
   * @param value
   *          the value
   */
  public void setPropertyValue(String propName, Object value) {
    // if (isCenterProp(propName)) {
    // massData.center = (Vec2) value;
    // }
    if (isMassProp(propName)) {
      massData.mass = FloatUtil.toFloat(value);
    }
    if (isRotationalInertiaProp(propName)) {
      massData.I = FloatUtil.toFloat(value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    // return String.format(TO_STRING, massData.center, massData.mass,
    // massData.I);
    return "mass data";
  }
}
