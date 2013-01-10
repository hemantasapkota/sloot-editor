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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * The Class Entity.
 *
 * @author hemantasapkota
 */
public class Entity {

  /** The internal name. */
  private String internalName;
  
  /** The animation list. */
  private List<EntityAnimation> animationList;

  // transient variables required in runtime
  /** The default frame. */
  private transient Image defaultFrame;

  /**
   * Instantiates a new entity.
   */
  public Entity() {
    animationList = new ArrayList<EntityAnimation>();
  }

  /**
   * Gets the internal name.
   *
   * @return the internal name
   */
  public String getInternalName() {
    return internalName;
  }

  /**
   * Sets the internal name.
   *
   * @param internalName the new internal name
   */
  public void setInternalName(String internalName) {
    this.internalName = internalName;
  }

  /**
   * Adds the entity animation.
   *
   * @param anim the anim
   */
  public void addEntityAnimation(EntityAnimation anim) {
    animationList.add(anim);
  }

  /**
   * Gets the default frame.
   *
   * @return the default frame
   */
  public Image getDefaultFrame() {
    return defaultFrame;
  }

  /**
   * Sets the default frame.
   *
   * @param defaultFrame the new default frame
   */
  public void setDefaultFrame(Image defaultFrame) {
    this.defaultFrame = defaultFrame;
  }

  /**
   * Gets the animation list.
   *
   * @return the animation list
   */
  public List<EntityAnimation> getAnimationList() {
    return animationList;
  }

}
