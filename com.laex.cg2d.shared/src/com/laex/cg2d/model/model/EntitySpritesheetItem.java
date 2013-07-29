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

  private Rectangle extractBounds = new Rectangle();
  private int frameIndex;
  
  public Rectangle getExtractBounds() {
    return extractBounds;
  }
  public void setExtractBounds(Rectangle extractBounds) {
    this.extractBounds = extractBounds;
  }
  public int getFrameIndex() {
    return frameIndex;
  }
  public void setFrameIndex(int frameIndex) {
    this.frameIndex = frameIndex;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[frameIndex:").append(frameIndex).append(" Bounds:").append(extractBounds).append("]");
    return sb.toString();
  }

}
