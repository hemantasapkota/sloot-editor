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
import com.laex.cg2d.protobuf.ScreenModel.CGScreenModel;

/**
 * The Interface IGameScript.
 */
public interface IScreenControllerScript {
  
  /**
   * Can execute.
   *
   * @return true, if successful
   */
  boolean canExecute();
  
  /**
   * Execute init.
   */
  void executeInit();
  
  /**
   * Execute init body.
   *
   * @param body the body
   * @param bodyId the body id
   */
  void executeInitBody(CGScreenModel screenModel, IEntityQueryable queryMgr, Body body, String bodyId);
  
  
  void executeUpdate(CGScreenModel screenModel, IEntityQueryable queryMgr, Body body, String bodyId);
  
  /**
   * Execute key pressed.
   *
   * @param key the key
   */
  void executeKeyPressed(CGScreenModel screenModel, IEntityQueryable queryMgr, String key);

}
