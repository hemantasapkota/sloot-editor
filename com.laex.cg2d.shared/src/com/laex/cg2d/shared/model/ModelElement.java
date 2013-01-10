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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * The Class ModelElement.
 */
public abstract class ModelElement implements IPropertySource, Serializable {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8990388779106474478L;
  
  /** The Constant EMPTY_ARRAY. */
  private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];
  
  /** The pcs delegate. */
  private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

  /**
   * Adds the property change listener.
   *
   * @param l the l
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
    if (l == null) {
      throw new IllegalArgumentException();
    }
    pcsDelegate.addPropertyChangeListener(l);
  }

  /**
   * Fire property change.
   *
   * @param property the property
   * @param oldValue the old value
   * @param newValue the new value
   */
  protected void firePropertyChange(String property, Object oldValue, Object newValue) {
    if (pcsDelegate.hasListeners(property)) {
      pcsDelegate.firePropertyChange(property, oldValue, newValue);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
   */
  public Object getEditableValue() {
    return this;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
   */
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return EMPTY_ARRAY;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
   */
  public Object getPropertyValue(Object id) {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
   */
  public boolean isPropertySet(Object id) {
    return false;
  }

  /**
   * Read object.
   *
   * @param in the in
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException the class not found exception
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    pcsDelegate = new PropertyChangeSupport(this);
  }

  /**
   * Removes the property change listener.
   *
   * @param l the l
   */
  public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
    if (l != null) {
      pcsDelegate.removePropertyChangeListener(l);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
   */
  public void resetPropertyValue(Object id) {
    // do nothing
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
   */
  public void setPropertyValue(Object id, Object value) {
    // do nothing
  }
}
