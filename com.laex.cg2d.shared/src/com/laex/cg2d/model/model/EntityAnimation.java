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

import com.badlogic.gdx.math.Vector2;

/**
 * The Class EntityAnimation.
 */
public class EntityAnimation {

  /** The animation name. */
  private String animationName;

  /** The animation resource file. */
  private ResourceFile animationResourceFile;

  /** The fixture resource file. */
  private ResourceFile fixtureResourceFile;

  /** The animation duration. */
  private float animationDuration;

  /** The default animation. */
  private boolean defaultAnimation;

  /** The cols. */
  private int cols;

  /** The rows. */
  private int rows;

  /** The shape type. */
  private EntityCollisionType shapeType;

  /** The shp height. */
  int shpX, shpY, shpWidth, shpHeight;

  private List<Integer> frameIndices = new ArrayList<Integer>();

  /** The vertices. */
  private List<Vector2> vertices = new ArrayList<Vector2>();

  /**
   * Instantiates a new entity animation.
   */
  public EntityAnimation() {
    animationResourceFile = ResourceFile.emptyResourceFile();
    fixtureResourceFile = ResourceFile.emptyResourceFile();
    shapeType = EntityCollisionType.NONE;
  }

  /**
   * Gets the fixture resource file.
   * 
   * @return the fixture resource file
   */
  public ResourceFile getFixtureResourceFile() {
    return fixtureResourceFile;
  }

  /**
   * Sets the fixture resource file.
   * 
   * @param fixtureResourceFile
   *          the new fixture resource file
   */
  public void setFixtureResourceFile(ResourceFile fixtureResourceFile) {
    this.fixtureResourceFile = fixtureResourceFile;
  }

  /**
   * Gets the shp x.
   * 
   * @return the shp x
   */
  public int getShpX() {
    return shpX;
  }

  /**
   * Sets the shp x.
   * 
   * @param shpX
   *          the new shp x
   */
  public void setShpX(int shpX) {
    this.shpX = shpX;
  }

  /**
   * Gets the shp y.
   * 
   * @return the shp y
   */
  public int getShpY() {
    return shpY;
  }

  /**
   * Sets the shp y.
   * 
   * @param shpY
   *          the new shp y
   */
  public void setShpY(int shpY) {
    this.shpY = shpY;
  }

  /**
   * Gets the shp width.
   * 
   * @return the shp width
   */
  public int getShpWidth() {
    return shpWidth;
  }

  /**
   * Sets the shp width.
   * 
   * @param shpWidth
   *          the new shp width
   */
  public void setShpWidth(int shpWidth) {
    this.shpWidth = shpWidth;
  }

  /**
   * Gets the shp height.
   * 
   * @return the shp height
   */
  public int getShpHeight() {
    return shpHeight;
  }

  /**
   * Sets the shp height.
   * 
   * @param shpHeight
   *          the new shp height
   */
  public void setShpHeight(int shpHeight) {
    this.shpHeight = shpHeight;
  }

  /**
   * Gets the vertices.
   * 
   * @return the vertices
   */
  public List<Vector2> getVertices() {
    return vertices;
  }

  /**
   * Sets the vertices.
   * 
   * @param vertices
   *          the new vertices
   */
  public void setVertices(List<Vector2> vertices) {
    this.vertices = vertices;
  }

  /**
   * Gets the shape type.
   * 
   * @return the shape type
   */
  public EntityCollisionType getShapeType() {
    return shapeType;
  }

  /**
   * Sets the shape type.
   * 
   * @param shapeType
   *          the new shape type
   */
  public void setShapeType(EntityCollisionType shapeType) {
    this.shapeType = shapeType;
  }

  /**
   * Checks if is default animation.
   * 
   * @return true, if is default animation
   */
  public boolean isDefaultAnimation() {
    return defaultAnimation;
  }

  /**
   * Sets the default animation.
   * 
   * @param defaultAnimation
   *          the new default animation
   */
  public void setDefaultAnimation(boolean defaultAnimation) {
    this.defaultAnimation = defaultAnimation;
  }

  /**
   * Gets the animation name.
   * 
   * @return the animation name
   */
  public String getAnimationName() {
    return animationName;
  }

  /**
   * Sets the animation name.
   * 
   * @param animationName
   *          the new animation name
   */
  public void setAnimationName(String animationName) {
    this.animationName = animationName;
  }

  /**
   * Gets the animation duration.
   * 
   * @return the animation duration
   */
  public float getAnimationDuration() {
    return animationDuration;
  }

  /**
   * Sets the animation delay.
   * 
   * @param duration
   *          the new animation delay
   */
  public void setAnimationDelay(float duration) {
    this.animationDuration = duration;
  }

  /**
   * Gets the animation resource file.
   * 
   * @return the animation resource file
   */
  public ResourceFile getAnimationResourceFile() {
    return animationResourceFile;
  }

  /**
   * Sets the animation resource file.
   * 
   * @param animationResourceFile
   *          the new animation resource file
   */
  public void setAnimationResourceFile(ResourceFile animationResourceFile) {
    this.animationResourceFile = animationResourceFile;
  }

  /**
   * Gets the cols.
   * 
   * @return the cols
   */
  public int getCols() {
    return cols;
  }

  /**
   * Sets the cols.
   * 
   * @param cols
   *          the new cols
   */
  public void setCols(int cols) {
    this.cols = cols;
  }

  /**
   * Gets the rows.
   * 
   * @return the rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Sets the rows.
   * 
   * @param rows
   *          the new rows
   */
  public void setRows(int rows) {
    this.rows = rows;
  }

  public void setAnimationDuration(float animationDuration) {
    this.animationDuration = animationDuration;
  }

  public List<Integer> getFrameIndices() {
    return frameIndices;
  }

  public void setFrameIndices(List<Integer> frameIndices) {
    this.frameIndices = frameIndices;
  }

}
