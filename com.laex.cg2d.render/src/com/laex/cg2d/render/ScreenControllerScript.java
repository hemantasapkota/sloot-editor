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

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;

/**
 * The Interface IGameScript.
 */
public interface ScreenControllerScript {

  /**
   * Can execute.
   * 
   * @return true, if successful
   */
  boolean canExecute();

  /**
   * Execute init.
   * 
   * @param screenModel
   *          the screen model
   * @param screenMgr
   *          the screen mgr
   */
  void executeInit(CGScreenModel screenModel, ScreenManager screenMgr);

  /**
   * Execute init body.
   * 
   * @param body
   *          the body
   * @param bodyId
   *          the body id
   */
  void executeInitBody(Body body, String bodyId);

  /**
   * Execute update.
   * 
   * @param body
   *          the body
   * @param bodyId
   *          the body id
   */
  void executeUpdate(Body body, String bodyId);

  /**
   * Execute key pressed.
   * 
   * @param key
   *          the key
   */
  void executeKeyPressed(String key);

  /**
   * Collision callback.
   * 
   * @param idA
   *          the id a
   * @param idB
   *          the id b
   * @param bodyA
   *          the body a
   * @param bodyB
   *          the body b
   * @param fixA
   *          the fix a
   * @param fixB
   *          the fix b
   */
  void collisionCallback(String idA, String idB, Body bodyA, Body bodyB, Fixture fixA, Fixture fixB);

}
