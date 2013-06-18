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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Layer.
 */
public class Layer extends ModelElement implements Comparable<Layer> {

  /** The Constant LAYER_NAME_CHANGED. */
  public static final String LAYER_NAME_CHANGED = "LayerNameChanged";

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5186818065007054067L;

  /** The children. */
  private List<Shape> children = new ArrayList<Shape>();

  /** The id. */
  private int id;

  /** The name. */
  private String name;

  /** The visible. */
  private boolean visible;

  /** The locked. */
  private boolean locked;

  /** The current. */
  private boolean current;

  /**
   * Creates the.
   * 
   * @param id
   *          the id
   * @param name
   *          the name
   * @param visible
   *          the visible
   * @param locked
   *          the locked
   * @return the layer
   */
  public static Layer create(int id, String name, boolean visible, boolean locked) {
    return new Layer(id, name, visible, locked);
  }

  /**
   * Instantiates a new layer.
   * 
   * @param id
   *          the id
   * @param name
   *          the name
   * @param visible
   *          the visible
   * @param locked
   *          the locked
   */
  public Layer(int id, String name, boolean visible, boolean locked) {
    this.id = id;
    this.name = name;
    this.visible = visible;
    this.locked = locked;
  }

  /**
   * Adds the.
   * 
   * @param child
   *          the child
   */
  public void add(Shape child) {
    children.add(child);
  }

  /**
   * Removes the.
   * 
   * @param child
   *          the child
   */
  public void remove(Shape child) {
    children.remove(child);
  }

  /**
   * Gets the children.
   * 
   * @return the children
   */
  public List<Shape> getChildren() {
    return children;
  }

  /**
   * Sets the children.
   *
   * @param children the new children
   */
  public void setChildren(List<Shape> children) {
    this.children = children;
  }

  /**
   * Gets the name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Checks if is current.
   * 
   * @return true, if is current
   */
  public boolean isCurrent() {
    return current;
  }

  /**
   * Checks if is visible.
   * 
   * @return true, if is visible
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Checks if is locked.
   * 
   * @return true, if is locked
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * Gets the id.
   * 
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the current.
   * 
   * @param current
   *          the new current
   */
  public void setCurrent(boolean current) {
    this.current = current;
  }

  /**
   * Sets the visible.
   * 
   * @param visible
   *          the new visible
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Sets the locked.
   * 
   * @param locked
   *          the new locked
   */
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
    
    firePropertyChange(LAYER_NAME_CHANGED, null, name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Layer l) {
    if (id == l.id) {
      return 0;
    }
    return -1;
  }

}
