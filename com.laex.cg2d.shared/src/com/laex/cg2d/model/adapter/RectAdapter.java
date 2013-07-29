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
package com.laex.cg2d.model.adapter;

import org.eclipse.draw2d.geometry.Rectangle;

import com.laex.cg2d.model.ScreenModel.CGBounds;

/**
 * The Class RectAdapter.
 */
public class RectAdapter {
  
  public static CGBounds cgBounds(com.badlogic.gdx.math.Rectangle r) {
    return CGBounds.newBuilder().setX(r.getX()).setY(r.getY()).setWidth(r.getWidth()).setHeight(r.getHeight()).build();
  }
  
  public static com.badlogic.gdx.math.Rectangle gdxRect(CGBounds b) {
    return new com.badlogic.gdx.math.Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
  }

  /**
   * D2d rect.
   * 
   * @param r
   *          the r
   * @return the rectangle
   */
  public static Rectangle d2dRect(com.badlogic.gdx.math.Rectangle r) {
    int x = (int) r.x;
    int y = (int) r.y;
    int w = (int) r.width;
    int h = (int) r.height;

    return new Rectangle(x, y, w, h);
  }
  

  /**
   * Gdx rect.
   * 
   * @param r
   *          the r
   * @return the com.badlogic.gdx.math. rectangle
   */
  public static com.badlogic.gdx.math.Rectangle gdxRect(Rectangle r) {
    float x = r.x;
    float y = r.y;
    float w = r.width;
    float h = r.height;
    return new com.badlogic.gdx.math.Rectangle(x, y, w, h);
  }

  /**
   * Gdx rect.
   * 
   * @param r
   *          the r
   * @return the com.badlogic.gdx.math. rectangle
   */
  public static com.badlogic.gdx.math.Rectangle gdxRect(org.eclipse.swt.graphics.Rectangle r) {
    float x = r.x;
    float y = r.y;
    float w = r.width;
    float h = r.height;
    return new com.badlogic.gdx.math.Rectangle(x, y, w, h);
  }

}
