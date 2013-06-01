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

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.util.BooleanUtil;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class FixtureDefPropertySource.
 */
public class FixtureDefPropertySource implements IPropertySource {

  /** The Constant FRICITON_PROP. */
  public static final String FRICITON_PROP = "FixtureDef.friction";

  /** The Constant RESTIUTION_PROP. */
  public static final String RESTIUTION_PROP = "FixtureDef.restitution";

  /** The Constant DENSITY_PROP. */
  public static final String DENSITY_PROP = "FixtureDef.density";

  /** The Constant SENSOR_PROP. */
  public static final String SENSOR_PROP = "FixtureDef.isSensor";

  /** The Constant FILTER_PROP. */
  public static final String FILTER_PROP = "FixtureDef.filter";

  /** The descriptors. */
  protected static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor frictionProp = new TextPropertyDescriptor(FRICITON_PROP, "Friction");
    PropertyDescriptor restitutionProp = new TextPropertyDescriptor(RESTIUTION_PROP, "Restitution");
    PropertyDescriptor densityProp = new TextPropertyDescriptor(DENSITY_PROP, "Density");
    PropertyDescriptor isSensorProp = new ComboBoxPropertyDescriptor(SENSOR_PROP, "Sensor",
        BooleanUtil.BOOLEAN_STRING_VALUES);
    PropertyDescriptor filterProp = new PropertyDescriptor(FILTER_PROP, "Filter");

    descriptors = new IPropertyDescriptor[]
      { frictionProp, restitutionProp, densityProp, isSensorProp, filterProp };
  }

  /** The fixture def. */
  private FixtureDef fixtureDef = new FixtureDef();

  /**
   * Instantiates a new fixture def property source.
   * 
   * @param fixtureDef
   *          the fixture def
   */
  public FixtureDefPropertySource(FixtureDef fixtureDef) {
    this.fixtureDef = fixtureDef;

    if (this.fixtureDef.density == 0) {
      this.fixtureDef.density = 1;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return fixtureDef;
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
    if (isFrictionProp(id)) {
      return FloatUtil.toString(this.fixtureDef.friction);
    }
    if (isDensityProp(id)) {
      return FloatUtil.toString(this.fixtureDef.density);
    }
    if (isRestitutionProp(id)) {
      return FloatUtil.toString(this.fixtureDef.restitution);
    }
    if (isSensorProp(id)) {
      return BooleanUtil.getIntegerFromBoolean(this.fixtureDef.isSensor);
    }
    if (isFilterProp(id)) {
      return new FixtureFilterPropertySource(this.fixtureDef.filter);
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
    if (isFrictionProp(id)) {
      this.fixtureDef.friction = FloatUtil.toFloat(value);
    } else if (isDensityProp(id)) {
      this.fixtureDef.density = FloatUtil.toFloat(value);
    } else if (isRestitutionProp(id)) {
      this.fixtureDef.restitution = FloatUtil.toFloat(value);
    } else if (isSensorProp(id)) {
      this.fixtureDef.isSensor = BooleanUtil.toBool(value);
    } else if (isFilterProp(id)) {
      // this.fixtureDef.filter = (Filter) value;
    }
  }

  /**
   * Checks if is restitution prop.
   * 
   * @param id
   *          the id
   * @return true, if is restitution prop
   */
  private boolean isRestitutionProp(Object id) {
    return RESTIUTION_PROP.equals(id);
  }

  /**
   * Checks if is density prop.
   * 
   * @param id
   *          the id
   * @return true, if is density prop
   */
  private boolean isDensityProp(Object id) {
    return DENSITY_PROP.equals(id);
  }

  /**
   * Checks if is friction prop.
   * 
   * @param id
   *          the id
   * @return true, if is friction prop
   */
  private boolean isFrictionProp(Object id) {
    return FRICITON_PROP.equals(id);
  }

  /**
   * Checks if is sensor prop.
   * 
   * @param id
   *          the id
   * @return true, if is sensor prop
   */
  private boolean isSensorProp(Object id) {
    return SENSOR_PROP.equals(id);
  }

  /**
   * Checks if is filter prop.
   * 
   * @param id
   *          the id
   * @return true, if is filter prop
   */
  private boolean isFilterProp(Object id) {
    return FILTER_PROP.equals(id);
  }

}
