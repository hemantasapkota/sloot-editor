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
package com.laex.cg2d.screeneditor.palette;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Control;

import com.laex.cg2d.shared.adapter.RectAdapter;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.ShapesDiagram;

/***
 * Class has to be public.
 * 
 * @author hemantasapkota
 * 
 */
public class CloneTool extends MarqueeSelectionTool {

  /** The start location. */
  private Point startLocation;

  /** The end location. */
  private Point endLocation;

  /**
   * Instantiates a new clone tool.
   */
  public CloneTool() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.tools.AbstractTool#activate()
   */
  @Override
  public void activate() {
    super.activate();

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.tools.MarqueeSelectionTool#setViewer(org.eclipse.gef.
   * EditPartViewer)
   */
  @Override
  public void setViewer(EditPartViewer viewer) {
    super.setViewer(viewer);

    ScrollingGraphicalViewer epv = (ScrollingGraphicalViewer) getCurrentViewer();
    if (epv == null) {
      return;
    }
    Control cntrl = epv.getContents().getViewer().getControl();

    cntrl.addMouseListener(new MouseListener() {

      @Override
      public void mouseUp(MouseEvent e) {
        endLocation = new Point(e.x, e.y);
      }

      @Override
      public void mouseDown(MouseEvent e) {
        startLocation = new Point(e.x, e.y);
      }

      @Override
      public void mouseDoubleClick(MouseEvent e) {
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.tools.MarqueeSelectionTool#getCommandName()
   */
  @Override
  protected String getCommandName() {
    return "Clone";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.tools.MarqueeSelectionTool#handleButtonUp(int)
   */
  @Override
  protected boolean handleButtonUp(int button) {
    boolean fromSuper = super.handleButtonUp(button);

    if (getOperationSet().size() == 0 || getOperationSet().size() > 1) {
      // clone of multiple different shapes is not defined
      return fromSuper;
    }

    // Clone here
    Rectangle area = calculateArea();
    Shape shape = (Shape) ((IAdaptable) getOperationSet().get(0)).getAdapter(Shape.class);
    ShapesDiagram diagram = (ShapesDiagram) getCurrentViewer().getContents().getModel();

    for (int i = area.y; i < area.y + area.height; i += shape.getBounds().height) {
      for (int j = area.x; j < area.x + area.width; j += shape.getBounds().width) {
        Shape newShape = shape.cloneShape();

        Rectangle newBounds = RectAdapter.d2dRect(newShape.getBounds());
        newBounds.x = j;
        newBounds.y = i;

        newShape.setBounds(RectAdapter.gdxRect(newBounds));
        diagram.addChild(newShape);
        newShape.getParentLayer().add(newShape);
      }
    }

    // remove the cloned item
    diagram.removeChild(shape);
    shape.getParentLayer().remove(shape);

    return fromSuper;
  }

  /**
   * Calculate area.
   * 
   * @return the rectangle
   */
  private Rectangle calculateArea() {
    return new Rectangle(startLocation, getLocation());
  }

}
