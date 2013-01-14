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

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.shared.util.FloatUtil;

/**
 * The Class Vec2PropertySource.
 */
public class Vec2PropertySource implements IPropertySource {

  /** The Constant TO_STRING. */
  private static final String TO_STRING = "(%f, %f)";

  /** The Constant X_PROP. */
  public static final String X_PROP = "x";

  /** The Constant Y_PROP. */
  public static final String Y_PROP = "y";

  /** The descriptor. */
  protected static IPropertyDescriptor[] descriptor;

  static {
    PropertyDescriptor xProp = new TextPropertyDescriptor(X_PROP, "X");
    PropertyDescriptor yProp = new TextPropertyDescriptor(Y_PROP, "Y");
    descriptor = new IPropertyDescriptor[]
      { xProp, yProp };

    for (int i = 0; i < descriptor.length; i++) {
      ((PropertyDescriptor) descriptor[i]).setValidator(new ICellEditorValidator() {
        @Override
        public String isValid(Object value) {
          return null;
        }
      });
    }
  }

  /** The vec2. */
  protected Vector2 vec2 = null;

  /**
   * Instantiates a new vec2 property source.
   * 
   * @param vec2
   *          the vec2
   */
  public Vec2PropertySource(Vector2 vec2) {
    this.vec2 = vec2;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return new Vector2(this.vec2.x, this.vec2.y);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
   */
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return descriptor;
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
    if (isXProp(id)) {
      return Float.toString(vec2.x);
    }
    if (isYProp(id)) {
      return Float.toString(vec2.y);
    }
    return null;
  }

  /**
   * Checks if is y prop.
   * 
   * @param id
   *          the id
   * @return true, if is y prop
   */
  private boolean isYProp(Object id) {
    return id.equals(Y_PROP);
  }

  /**
   * Checks if is x prop.
   * 
   * @param id
   *          the id
   * @return true, if is x prop
   */
  private boolean isXProp(Object id) {
    return id.equals(X_PROP);
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
    if (isXProp(id)) {
      vec2.x = FloatUtil.toFloat(value);
    }
    if (isYProp(id)) {
      vec2.y = FloatUtil.toFloat(value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format(TO_STRING, vec2.x, vec2.y);
  }

}
