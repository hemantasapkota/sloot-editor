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

  private ResourceFile spritesheetMapperFile = ResourceFile.create("", "");

  /** The shape type. */
  private EntityCollisionType shapeType = EntityCollisionType.NONE;
  
  private List<Vector2> vertices = new ArrayList<Vector2>();
  
  private List<Integer> frameIndices = new ArrayList<Integer>();

  private List<EntitySpritesheetItem> spritesheetItems = new ArrayList<EntitySpritesheetItem>();

  /**
   * Instantiates a new entity animation.
   */
  public EntityAnimation() {
  }

  public String getAnimationName() {
    return animationName;
  }

  public void setAnimationName(String animationName) {
    this.animationName = animationName;
  }

  public float getAnimationDuration() {
    return animationDuration;
  }

  public void setAnimationDuration(float animationDuration) {
    this.animationDuration = animationDuration;
  }

  public boolean isDefaultAnimation() {
    return defaultAnimation;
  }

  public void setDefaultAnimation(boolean defaultAnimation) {
    this.defaultAnimation = defaultAnimation;
  }

  public ResourceFile getSpritesheetFile() {
    return spritesheetFile;
  }
  
  public void setSpritesheetFile(ResourceFile spritesheetFile) {
    this.spritesheetFile = spritesheetFile;
  }

  public ResourceFile getFixtureResourceFile() {
    return fixtureResourceFile;
  }

  public void setFixtureResourceFile(ResourceFile fixtureResourceFile) {
    this.fixtureResourceFile = fixtureResourceFile;
  }

  public ResourceFile getSpritesheetMapperFile() {
    return spritesheetMapperFile;
  }

  public void setSpritesheetMapperFile(ResourceFile spritesheetMapperFile) {
    this.spritesheetMapperFile = spritesheetMapperFile;
  }

  public EntityCollisionType getShapeType() {
    return shapeType;
  }

  public void setShapeType(EntityCollisionType shapeType) {
    this.shapeType = shapeType;
  }

  public List<Integer> getFrameIndices() {
    return frameIndices;
  }

  public void setFrameIndices(List<Integer> frameIndices) {
    this.frameIndices = frameIndices;
  }

  public List<EntitySpritesheetItem> getSpritesheetItems() {
    return spritesheetItems;
  }

  public void setSpritesheetItems(List<EntitySpritesheetItem> spritesheetItems) {
    this.spritesheetItems = spritesheetItems;
  }

  public List<Vector2> getVertices() {
    return vertices;
  }

  public void setVertices(List<Vector2> vertices) {
    this.vertices = vertices;
  }
  
  
}
