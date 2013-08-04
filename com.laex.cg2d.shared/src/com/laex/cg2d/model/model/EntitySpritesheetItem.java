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

import com.badlogic.gdx.math.Rectangle;

/**
 * There may be three types of entity spritesheet item:
 * 
 * 1. Items extracted from json file mapper.
 *    The information for this will be stored in extractBounds rectangle, where each rectangle indicates what area to extract subimage from.
 *    
 * 2. Frame Index. More conventional rows/columns/tiles based image extraction. The position of this entity spriteitem is the frameIndex
 * 
 * @author hemantasapkota
 *
 */
public class EntitySpritesheetItem {

  /** The extract bounds. */
  private Rectangle extractBounds = new Rectangle();
  
  /** The frame index. */
  private int frameIndex;
  
  /**
   * Gets the extract bounds.
   *
   * @return the extract bounds
   */
  public Rectangle getExtractBounds() {
    return extractBounds;
  }
  
  /**
   * Sets the extract bounds.
   *
   * @param extractBounds the new extract bounds
   */
  public void setExtractBounds(Rectangle extractBounds) {
    this.extractBounds = extractBounds;
  }
  
  /**
   * Gets the frame index.
   *
   * @return the frame index
   */
  public int getFrameIndex() {
    return frameIndex;
  }
  
  /**
   * Sets the frame index.
   *
   * @param frameIndex the new frame index
   */
  public void setFrameIndex(int frameIndex) {
    this.frameIndex = frameIndex;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[frameIndex:").append(frameIndex).append(" Bounds:").append(extractBounds).append("]");
    return sb.toString();
  }

}
