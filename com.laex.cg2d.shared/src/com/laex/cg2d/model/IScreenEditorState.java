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
package com.laex.cg2d.model;

import org.eclipse.swt.graphics.Color;

import com.laex.cg2d.model.model.Joint;

/**
 * The Interface IScreenEditorState.
 */
public interface IScreenEditorState {

  /**
   * Toggle grid.
   */
  void toggleGrid();

  /**
   * Update card layer.
   * 
   * @param noX
   *          the no x
   * @param noY
   *          the no y
   * @param cardWidth
   *          the card width
   * @param cardHeight
   *          the card height
   */
  void updateCardLayer(int noX, int noY, int cardWidth, int cardHeight, Color bgColor);

  /**
   * Update card layer zoom.
   * 
   * @param zoom
   *          the zoom
   */
  void updateCardLayerZoom(double zoom);
  
  
  /**
   * Toggle joint layer.
   * 
   * @param joints
   *          the joints
   */
  void toggleJointLayer(Joint joint);

}
