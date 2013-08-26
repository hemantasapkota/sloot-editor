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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;

/**
 * The Class BoxFigure.
 */
public class BoxFigure extends RectangleFigure {

  private Label textLabel;

  /**
   * Instantiates a new box figure.
   */
  public BoxFigure(String label) {
    setFill(true);
    setBackgroundColor(ColorConstants.green);
    setLayoutManager(new StackLayout());

    textLabel = new Label(label == null ? "" : label);
    add(textLabel);
  }

  public void updateLabel(String text) {
    if (text == null) text = "";

    textLabel.setText(text);
    repaint();
  }

}
