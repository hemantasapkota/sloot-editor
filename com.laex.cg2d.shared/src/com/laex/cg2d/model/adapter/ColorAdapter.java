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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.laex.cg2d.model.ScreenModel.CGColor;

/**
 * The Class ColorAdapter.
 */
public class ColorAdapter {
  
  /**
   * Cg color.
   *
   * @param rgb the rgb
   * @return the cG color
   */
  public static CGColor cgColor(RGB rgb) {
    return CGColor.newBuilder().setR(rgb.red).setG(rgb.green).setB(rgb.blue).build();
  }
  
  /**
   * Swt color.
   *
   * @param color the color
   * @return the color
   */
  public static Color swtColor(CGColor color) {
    return new Color(null, new RGB(color.getR(), color.getG(), color.getB()));
  }
  
  /**
   * Swt color.
   *
   * @param rgb the rgb
   * @return the color
   */
  public static Color swtColor(RGB rgb) {
    return new Color(null, rgb);
  }

}
