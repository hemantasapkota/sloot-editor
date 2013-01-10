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
package com.laex.cg2d.shared.figures;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.graphics.Image;

/**
 * The Class TexturedBoxFigure.
 */
public class TexturedBoxFigure extends ImageFigure {

  /**
   * Instantiates a new textured box figure.
   *
   * @param texture the texture
   */
  public TexturedBoxFigure(Image texture) {
    setSize(texture.getBounds().width, texture.getBounds().height);
    setImage(texture);
    setAlignment(PositionConstants.ALWAYS_LEFT);
  }
}
