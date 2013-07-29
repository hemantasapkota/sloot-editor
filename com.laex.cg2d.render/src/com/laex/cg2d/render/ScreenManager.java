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
package com.laex.cg2d.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Interface ScreenManager.
 */
public interface ScreenManager {

  /**
   * Creates the entity from.
   * 
   * @param id
   *          the id
   */
  Body createEntityFrom(String id, String animationName);

  /**
   * Switch animation.
   * 
   * @param id
   *          the id
   * @param animationName
   *          the animation name
   */
  Body switchAnimation(String id, String animationName);

  /**
   * Gets the entity by id.
   * 
   * @param id
   *          the id
   * @return the entity by id
   */
  Body getEntityById(String id);

  /**
   * Gets the entity id.
   * 
   * @param b
   *          the b
   * @return the entity id
   */
  String getEntityId(Body b);

  /**
   * New vector.
   * 
   * @param x
   *          the x
   * @param y
   *          the y
   * @return the vector2
   */
  Vector2 newVector(float x, float y);

  /**
   * Draw text.
   *
   * @param text the text
   * @param x the x
   * @param y the y
   */
  void drawText(String text, float x, float y);
  
  /**
   * Destroy joint for entity.
   *
   * @param id the id
   */
  void destroyJointForEntity(String id);

}
