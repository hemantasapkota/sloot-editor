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
 * The Class JsonSprite.
 */
public class JsonSprite {
  
  /** The sprite name. */
  private String spriteName;
  
  /** The x. */
  private int x;
  
  /** The y. */
  private int y;
  
  /** The width. */
  private int width;
  
  /** The height. */
  private int height;
  
  /** The pivot x. */
  private int pivotX;
  
  /** The pivot y. */
  private int pivotY;
  
  /**
   * Instantiates a new json sprite.
   *
   * @param name the name
   * @param x the x
   * @param y the y
   * @param w the w
   * @param h the h
   * @param px the px
   * @param py the py
   */
  public JsonSprite(String name, int x, int y, int w, int h, int px, int py) {
    this.spriteName = name;
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
    this.pivotX = px;
    this.pivotY = py;
  }

  /**
   * Gets the sprite name.
   *
   * @return the sprite name
   */
  public String getSpriteName() {
    return spriteName;
  }

  /**
   * Sets the sprite name.
   *
   * @param spriteName the new sprite name
   */
  public void setSpriteName(String spriteName) {
    this.spriteName = spriteName;
  }

  /**
   * Gets the x.
   *
   * @return the x
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x.
   *
   * @param x the new x
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the y.
   *
   * @return the y
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y.
   *
   * @param y the new y
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Gets the width.
   *
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width.
   *
   * @param width the new width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Gets the height.
   *
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height.
   *
   * @param height the new height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Gets the pivot x.
   *
   * @return the pivot x
   */
  public int getPivotX() {
    return pivotX;
  }

  /**
   * Sets the pivot x.
   *
   * @param pivotX the new pivot x
   */
  public void setPivotX(int pivotX) {
    this.pivotX = pivotX;
  }

  /**
   * Gets the pivot y.
   *
   * @return the pivot y
   */
  public int getPivotY() {
    return pivotY;
  }

  /**
   * Sets the pivot y.
   *
   * @param pivotY the new pivot y
   */
  public void setPivotY(int pivotY) {
    this.pivotY = pivotY;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return spriteName + "-" + x + "-" + y + "-" + width + "-" + height;
  }

}
