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
package com.laex.cg2d.entityeditor.pages;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.EntityAnimation;

/**
 * The Class AnimationListViewItem.
 */
class AnimationListViewItem {

  /** The name. */
  private String name;

  /** The first frame. */
  private Image collisionPreviewFrame;

  /** The animation. */
  private EntityAnimation animation;

  /**
   * Gets the name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * Gets the collision preview frame.
   *
   * @return the collision preview frame
   */
  public Image getCollisionPreviewFrame() {
    return collisionPreviewFrame;
  }
  
  /**
   * Sets the collision preview frame.
   *
   * @param collisionPreviewFrame the new collision preview frame
   */
  public void setCollisionPreviewFrame(Image collisionPreviewFrame) {
    this.collisionPreviewFrame = collisionPreviewFrame;
  }
  
  /**
   * Gets the animation.
   * 
   * @return the animation
   */
  public EntityAnimation getAnimation() {
    return animation;
  }

  /**
   * Sets the animation.
   * 
   * @param animation
   *          the new animation
   */
  public void setAnimation(EntityAnimation animation) {
    this.animation = animation;
  }

}
