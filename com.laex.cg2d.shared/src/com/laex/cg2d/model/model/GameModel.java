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

import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;

/**
 * The Class GameModel.
 */
public class GameModel {

  /** The shapes. */
  ShapesDiagram shapes = new ShapesDiagram();
  
  CGScreenPreferences screenPrefs;

  /**
   * Instantiates a new game model.
   */
  public GameModel(CGScreenPreferences screenPrefs) {
    this.screenPrefs = screenPrefs;
  }

  /**
   * Gets the diagram.
   * 
   * @return the diagram
   */
  public ShapesDiagram getDiagram() {
    return shapes;
  }
  
  public CGScreenPreferences getScreenPrefs() {
    return screenPrefs;
  }
  

}
