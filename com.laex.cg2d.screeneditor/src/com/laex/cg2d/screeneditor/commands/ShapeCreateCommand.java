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
package com.laex.cg2d.screeneditor.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.laex.cg2d.shared.ResourceManager;
import com.laex.cg2d.shared.adapter.RectAdapter;
import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.ShapesDiagram;

/**
 * ShapeCreateCommand should only make changes relating to shape's bounds and
 * other constraints. The actual creation of shape itself is handled by
 * ShapeCreationFactory
 * 
 * @author hemantasapkota
 * 
 */
public class ShapeCreateCommand extends Command {

  /** The layer. */
  private Layer layer;
  
  /** The new shape. */
  private Shape newShape;
  
  /** The parent. */
  private final ShapesDiagram parent;
  
  /** The bounds. */
  private Rectangle bounds;

  /**
   * Instantiates a new shape create command.
   *
   * @param newShape the new shape
   * @param layer the layer
   * @param parent the parent
   * @param bounds the bounds
   */
  public ShapeCreateCommand(Shape newShape, Layer layer, ShapesDiagram parent, Rectangle bounds) {
    this.layer = layer;
    this.newShape = newShape;
    this.parent = parent;
    this.bounds = bounds;
    setLabel("shape creation");
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute() {
    return layer != null && newShape != null && parent != null && bounds != null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute() {
    redo();
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo() {
    switch (newShape.getEditorShapeType()) {

    case BACKGROUND_SHAPE:
      org.eclipse.swt.graphics.Rectangle b = ResourceManager.getImageOfRelativePath(
          newShape.getBackgroundResourceFile().getResourceFile()).getBounds();
      bounds.width = b.width;
      bounds.height = b.height;
      break;

    case ENTITY_SHAPE:
      bounds.width = newShape.getEntity().getDefaultFrame().getBounds().width;
      bounds.height = newShape.getEntity().getDefaultFrame().getBounds().height;
      break;

    case SIMPLE_SHAPE_BOX:
      break;

    case SIMPLE_SHAPE_CIRCLE:
      break;

    case SIMPLE_SHAPE_HEDGE:
      break;

    default:
      break;
    }

    if (bounds.width <= 0 && bounds.height <= 0) {
      bounds.width = 16;
      bounds.height = 16;
    }

    newShape.setBounds(RectAdapter.gdxRect(bounds));
    parent.addChild(newShape);
    layer.add(newShape);
    newShape.setParentLayer(layer);
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo() {
    parent.removeChild(newShape);
    layer.remove(newShape);
  }

}