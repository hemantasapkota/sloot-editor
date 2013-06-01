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
 * The Enum EntityCollisionType.
 */
public enum EntityCollisionType {

  /** The none. */
  NONE,
  /** The box. */
  BOX,
  /** The circle. */
  CIRCLE,
  /** The custom. */
  CUSTOM;

  /** The sarr. */
  private static String[] sarr;

  /**
   * Checks if is none.
   * 
   * @return true, if is none
   */
  public boolean isNone() {
    return NONE.equals(this);
  }

  /**
   * Checks if is box.
   * 
   * @return true, if is box
   */
  public boolean isBox() {
    return BOX.equals(this);
  }

  /**
   * Checks if is circle.
   * 
   * @return true, if is circle
   */
  public boolean isCircle() {
    return CIRCLE.equals(this);
  }

  /**
   * Checks if is custom.
   * 
   * @return true, if is custom
   */
  public boolean isCustom() {
    return CUSTOM.equals(this);
  }

  /**
   * Shape type from ordinal.
   * 
   * @param ord
   *          the ord
   * @return the entity collision type
   */
  public static EntityCollisionType shapeTypeFromOrdinal(int ord) {
    return EntityCollisionType.valueOf(sarr[ord]);
  }

  /**
   * String values.
   * 
   * @return the string[]
   */
  public static String[] stringValues() {
    if (sarr == null) {
      sarr = new String[5];
      sarr[0] = "NONE";
      sarr[1] = "BOX";
      sarr[2] = "CIRCLE";
      sarr[3] = "CUSTOM";
    }

    return sarr;
  }

}
