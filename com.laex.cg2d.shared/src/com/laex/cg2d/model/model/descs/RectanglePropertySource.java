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
package com.laex.cg2d.model.model.descs;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Rectangle;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class RectanglePropertySource.
 */
public class RectanglePropertySource implements IPropertySource {

  /** The Constant X_PROP. */
  public static final String X_PROP = "x";

  /** The Constant Y_PROP. */
  public static final String Y_PROP = "y";

  /** The Constant WIDTH_PROP. */
  public static final String WIDTH_PROP = "width";

  /** The Constant HEIGHT_PROP. */
  public static final String HEIGHT_PROP = "height";

  /** The descriptor. */
  protected static IPropertyDescriptor[] descriptor;

  static {
    PropertyDescriptor xProp = new TextPropertyDescriptor(X_PROP, "X");
    PropertyDescriptor yProp = new TextPropertyDescriptor(Y_PROP, "Y");
    PropertyDescriptor widthProp = new TextPropertyDescriptor(WIDTH_PROP, "Width");
    PropertyDescriptor heightProp = new TextPropertyDescriptor(HEIGHT_PROP, "Height");
    descriptor = new IPropertyDescriptor[]
      { xProp, yProp, widthProp, heightProp };

    for (int i = 0; i < descriptor.length; i++) {
      ((PropertyDescriptor) descriptor[i]).setValidator(new ICellEditorValidator() {
        @Override
        public String isValid(Object value) {
          return null;
        }
      });
    }

  }

  /** The rect. */
  protected Rectangle rect;

  /** The immutable size. */
  private boolean immutableSize;

  /**
   * Instantiates a new rectangle property source.
   * 
   * @param rect
   *          the rect
   * @param immutableSize
   *          the immutable size
   */
  public RectanglePropertySource(Rectangle rect, boolean immutableSize) {
    this.rect = rect;
    this.immutableSize = immutableSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return rect;
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
    if (X_PROP.equals(id)) {
      return Float.toString(rect.x);
    }
    if (Y_PROP.equals(id)) {
      return Float.toString(rect.y);
    }
    if (WIDTH_PROP.equals(id)) {
      return Float.toString(rect.width);
    }
    if (HEIGHT_PROP.equals(id)) {
      return Float.toString(rect.height);
    }
    return null;
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
   * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang
   * .Object, java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    if (isXProp(id)) {
      setXProp(FloatUtil.toFloat(value));
    } else if (isYProp(id)) {
      setYProp(FloatUtil.toFloat(value));
    } else if (isWidthProp(id)) {

      if (!immutableSize) {
        setWidthProp(FloatUtil.toFloat(value));
      }

    } else if (isHeightProp(id)) {

      if (!immutableSize) {
        setHeightProp(FloatUtil.toFloat(value));
      }

    }
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

  /**
   * Checks if is width prop.
   * 
   * @param id
   *          the id
   * @return true, if is width prop
   */
  private boolean isWidthProp(Object id) {
    return id.equals(WIDTH_PROP);
  }

  /**
   * Checks if is height prop.
   * 
   * @param id
   *          the id
   * @return true, if is height prop
   */
  private boolean isHeightProp(Object id) {
    return id.equals(HEIGHT_PROP);
  }

  /**
   * Sets the x prop.
   * 
   * @param value
   *          the new x prop
   */
  private void setXProp(Float value) {
    if (value != null) {
      this.rect.x = value;
    }
  }

  /**
   * Sets the y prop.
   * 
   * @param value
   *          the new y prop
   */
  private void setYProp(Float value) {
    if (value != null) {
      this.rect.y = value;
    }
  }

  /**
   * Sets the width prop.
   * 
   * @param value
   *          the new width prop
   */
  private void setWidthProp(Float value) {
    if (value != null) {
      this.rect.width = value;
    }
  }

  /**
   * Sets the height prop.
   * 
   * @param value
   *          the new height prop
   */
  private void setHeightProp(Float value) {
    if (value != null) {
      this.rect.height = value;
    }
  }

}
