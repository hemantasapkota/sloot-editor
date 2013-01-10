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
package com.laex.cg2d.screeneditor.edit;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

import com.laex.cg2d.shared.figures.BoxFigure;

/**
 * The Class ShapeResizableEditPolicy.
 */
class ShapeResizableEditPolicy extends ResizableEditPolicy {

  /**
   * Instantiates a new shape resizable edit policy.
   */
  public ShapeResizableEditPolicy() {
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createDragSourceFeedbackFigure()
   */
  @Override
  protected IFigure createDragSourceFeedbackFigure() {
    // return super.createDragSourceFeedbackFigure();
    // Use a non-ghost rectangle for feedback
    // TODO: dynamic feedback for triangles and polygons

    // RectangleFigure r = new RectangleFigure();
    BoxFigure r = new BoxFigure();
    r.setLineStyle(Graphics.LINE_DOT);
    r.setFill(true);
    r.setBackgroundColor(ColorConstants.blue);
    r.setAlpha(255 / 2);
    r.setBounds(getInitialFeedbackBounds());

    addFeedback(r);
    return r;
  }

}
