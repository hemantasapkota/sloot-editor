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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.physics.box2d.Filter;
import com.laex.cg2d.shared.util.IntegerUtil;

/**
 * The Class FixtureFilterPropertySource.
 */
public class FixtureFilterPropertySource implements IPropertySource {

  /** The Constant CATEGORY_BITS_PROP. */
  public static final String CATEGORY_BITS_PROP = "Filter.categoryBits";

  /** The Constant MASK_BITS. */
  public static final String MASK_BITS = "Filter.maskBits";

  /** The Constant GROUP_INDEX. */
  public static final String GROUP_INDEX = "Filter.groupIndex";

  /** The filter. */
  private Filter filter = new Filter();

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor categoryBitsProp = new TextPropertyDescriptor(CATEGORY_BITS_PROP, "Category Bits");
    PropertyDescriptor maskBitsProp = new TextPropertyDescriptor(MASK_BITS, "Mask Bits");
    PropertyDescriptor groupIndexProp = new TextPropertyDescriptor(GROUP_INDEX, "Group Index");

    descriptors = new IPropertyDescriptor[]
      { categoryBitsProp, maskBitsProp, groupIndexProp };
  }

  /**
   * Instantiates a new fixture filter property source.
   * 
   * @param filter
   *          the filter
   */
  public FixtureFilterPropertySource(Filter filter) {
    this.filter = filter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return filter;
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
    if (isCategoryBitsProp(id)) {
      return Integer.toString(this.filter.categoryBits);
    }
    if (isMaskBitsProp(id)) {
      return Integer.toString(this.filter.maskBits);
    }
    if (isGroupIndexProp(id)) {
      return Integer.toString(this.filter.groupIndex);
    }
    return null;
  }

  /**
   * Checks if is group index prop.
   * 
   * @param id
   *          the id
   * @return true, if is group index prop
   */
  private boolean isGroupIndexProp(Object id) {
    return GROUP_INDEX.equals(id);
  }

  /**
   * Checks if is mask bits prop.
   * 
   * @param id
   *          the id
   * @return true, if is mask bits prop
   */
  private boolean isMaskBitsProp(Object id) {
    return MASK_BITS.equals(id);
  }

  /**
   * Checks if is category bits prop.
   * 
   * @param id
   *          the id
   * @return true, if is category bits prop
   */
  private boolean isCategoryBitsProp(Object id) {
    return CATEGORY_BITS_PROP.equals(id);
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
    if (isCategoryBitsProp(id)) {
      this.filter.categoryBits = IntegerUtil.toShort(value);
    } else if (isMaskBitsProp(id)) {
      this.filter.maskBits = IntegerUtil.toShort(value);
    } else if (isGroupIndexProp(id)) {
      this.filter.groupIndex = IntegerUtil.toShort(value);
    }
  }

}
