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

  /** The animation duration. */
  private float animationDuration;

  /** The default animation. */
  private boolean defaultAnimation;
  
  /** The animation resource file. */
  private ResourceFile spritesheetFile = ResourceFile.create("", "");

  /** The fixture resource file. */
  private ResourceFile fixtureResourceFile = ResourceFile.create("", "");

  /** The spritesheet mapper file. */
  private ResourceFile spritesheetMapperFile = ResourceFile.create("", "");

  /** The shape type. */
  private EntityCollisionType shapeType = EntityCollisionType.NONE;
  
  /** The vertices. */
  private List<Vector2> vertices = new ArrayList<Vector2>();
  
  /** The frame indices. */
  private List<Integer> frameIndices = new ArrayList<Integer>();

  /** The spritesheet items. */
  private List<EntitySpritesheetItem> spritesheetItems = new ArrayList<EntitySpritesheetItem>();

  /**
   * Instantiates a new entity animation.
   */
  public EntityAnimation() {
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
   * @param animationName the new animation name
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
   * Sets the animation duration.
   *
   * @param animationDuration the new animation duration
   */
  public void setAnimationDuration(float animationDuration) {
    this.animationDuration = animationDuration;
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
   * @param defaultAnimation the new default animation
   */
  public void setDefaultAnimation(boolean defaultAnimation) {
    this.defaultAnimation = defaultAnimation;
  }

  /**
   * Gets the spritesheet file.
   *
   * @return the spritesheet file
   */
  public ResourceFile getSpritesheetFile() {
    return spritesheetFile;
  }
  
  /**
   * Sets the spritesheet file.
   *
   * @param spritesheetFile the new spritesheet file
   */
  public void setSpritesheetFile(ResourceFile spritesheetFile) {
    this.spritesheetFile = spritesheetFile;
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
   * @param fixtureResourceFile the new fixture resource file
   */
  public void setFixtureResourceFile(ResourceFile fixtureResourceFile) {
    this.fixtureResourceFile = fixtureResourceFile;
  }

  /**
   * Gets the spritesheet mapper file.
   *
   * @return the spritesheet mapper file
   */
  public ResourceFile getSpritesheetMapperFile() {
    return spritesheetMapperFile;
  }

  /**
   * Sets the spritesheet mapper file.
   *
   * @param spritesheetMapperFile the new spritesheet mapper file
   */
  public void setSpritesheetMapperFile(ResourceFile spritesheetMapperFile) {
    this.spritesheetMapperFile = spritesheetMapperFile;
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
   * @param shapeType the new shape type
   */
  public void setShapeType(EntityCollisionType shapeType) {
    this.shapeType = shapeType;
  }

  /**
   * Gets the frame indices.
   *
   * @return the frame indices
   */
  public List<Integer> getFrameIndices() {
    return frameIndices;
  }

  /**
   * Sets the frame indices.
   *
   * @param frameIndices the new frame indices
   */
  public void setFrameIndices(List<Integer> frameIndices) {
    this.frameIndices = frameIndices;
  }

  /**
   * Gets the spritesheet items.
   *
   * @return the spritesheet items
   */
  public List<EntitySpritesheetItem> getSpritesheetItems() {
    return spritesheetItems;
  }

  /**
   * Sets the spritesheet items.
   *
   * @param spritesheetItems the new spritesheet items
   */
  public void setSpritesheetItems(List<EntitySpritesheetItem> spritesheetItems) {
    this.spritesheetItems = spritesheetItems;
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
   * @param vertices the new vertices
   */
  public void setVertices(List<Vector2> vertices) {
    this.vertices = vertices;
  }
  
  
}
