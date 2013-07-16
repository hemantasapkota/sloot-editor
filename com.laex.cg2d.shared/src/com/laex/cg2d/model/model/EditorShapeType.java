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

/**
 * The Enum EditorShapeType.
 */
public enum EditorShapeType {

  /** The simple shape circle. */
  SIMPLE_SHAPE_CIRCLE,
  /** The simple shape box. */
  SIMPLE_SHAPE_BOX,
  /** The simple shape hedge. */
  SIMPLE_SHAPE_HEDGE,
  /** The simple shape vedge. */
  SIMPLE_SHAPE_VEDGE,
  /** The background shape. */
  BACKGROUND_SHAPE,
  /** The entity shape. */
  ENTITY_SHAPE;
  
  /**
   * Checks if is box.
   * 
   * @return true, if is box
   */
  public boolean isBox() {
    return SIMPLE_SHAPE_BOX.equals(this);
  }

  /**
   * Checks if is circle.
   * 
   * @return true, if is circle
   */
  public boolean isCircle() {
    return SIMPLE_SHAPE_CIRCLE.equals(this);
  }

  /**
   * Checks if is background.
   * 
   * @return true, if is background
   */
  public boolean isBackground() {
    return BACKGROUND_SHAPE.equals(this);
  }

  /**
   * Checks if is entity.
   * 
   * @return true, if is entity
   */
  public boolean isEntity() {
    return ENTITY_SHAPE.equals(this);
  }

  /**
   * Checks if is edge.
   * 
   * @return true, if is edge
   */
  public boolean isEdge() {
    return SIMPLE_SHAPE_HEDGE.equals(this) || SIMPLE_SHAPE_VEDGE.equals(this);
  }
}
