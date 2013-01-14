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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;

/**
 * The Class HorizontalEdgeFigure.
 */
public class HorizontalEdgeFigure extends Shape {

  /**
   * Instantiates a new horizontal edge figure.
   */
  public HorizontalEdgeFigure() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
   */
  @Override
  protected void fillShape(Graphics graphics) {
    graphics.setBackgroundColor(ColorConstants.cyan);
    graphics.setForegroundColor(ColorConstants.blue);

    int w = getBounds().x + getBounds().width;
    int h = getBounds().y + getBounds().height;

    graphics.drawLine(getBounds().x, getBounds().y, w, getBounds().y);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
   */
  @Override
  protected void outlineShape(Graphics graphics) {
  }

}
