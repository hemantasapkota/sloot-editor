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
package com.laex.cg2d.screeneditor.editparts.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.RectangleFigure;

/**
 * The Class BoxFigure.
 */
public class BoxFigure extends RectangleFigure {

  /**
   * Instantiates a new box figure.
   */
  public BoxFigure() {
    setFill(true);
    setBackgroundColor(ColorConstants.green);
  }

}
