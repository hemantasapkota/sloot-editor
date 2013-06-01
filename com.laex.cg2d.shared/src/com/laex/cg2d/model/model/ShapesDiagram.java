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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The Class ShapesDiagram.
 */
public class ShapesDiagram extends ModelElement {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -5281704420611797268L;

  /** The Constant CHILD_ADDED_PROP. */
  public static final String CHILD_ADDED_PROP = "ShapesDiagram.ChildAdded";

  /** The Constant CHILD_REMOVED_PROP. */
  public static final String CHILD_REMOVED_PROP = "ShapesDiagram.ChildRemoved";

  /** The shapes. */
  private List<Shape> shapes = new ArrayList<Shape>();

  /** The layers. */
  private Queue<Layer> layers = new LinkedList<Layer>();

  /**
   * Checks if is current layer locked.
   * 
   * @return true, if is current layer locked
   */
  public boolean isCurrentLayerLocked() {
    for (Layer l : layers) {
      if (l.isCurrent()) {
        if (l.isLocked()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Adds the child.
   * 
   * @param s
   *          the s
   * @return true, if successful
   */
  public boolean addChild(Shape s) {
    if (s != null && shapes.add(s)) {
      firePropertyChange(CHILD_ADDED_PROP, null, s);
      return true;
    }
    return false;
  }

  /**
   * Removes the child.
   * 
   * @param s
   *          the s
   * @return true, if successful
   */
  public boolean removeChild(Shape s) {
    if (s != null && shapes.remove(s)) {
      firePropertyChange(CHILD_REMOVED_PROP, null, s);
      return true;
    }
    return false;
  }

  /**
   * Gets the children.
   * 
   * @return the children
   */
  public List<Shape> getChildren() {
    return shapes;
  }

  /**
   * Gets the layers.
   * 
   * @return the layers
   */
  public Queue<Layer> getLayers() {
    return layers;
  }

}