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

import org.eclipse.gef.commands.Command;

import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;

/**
 * ShapeCreateCommand should only make changes relating to shape's bounds and
 * other constraints. The actual creation of shape itself is handled by
 * ShapeCreationFactory
 * 
 * @author hemantasapkota
 * 
 */
public class ShapeCreateCommand extends Command {

  /** The new shape. */
  private Shape newShape;

  /** The parent. */
  private final ShapesDiagram parent;

  /**
   * Instantiates a new shape create command.
   *
   * @param newShape the new shape
   * @param parent the parent
   */
  public ShapeCreateCommand(Shape newShape, ShapesDiagram parent) {
    this.newShape = newShape;
    this.parent = parent;
    setLabel("Create Shape");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute() {
    return newShape != null && newShape.getParentLayer() != null && parent != null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute() {
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo() {
    if (newShape.getParentLayer() != null) {
      if (!newShape.getParentLayer().getChildren().contains(newShape)) {
        newShape.getParentLayer().add(newShape);
      }
    }
    parent.addChild(newShape);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo() {
    if (newShape.getParentLayer() != null) {
      newShape.getParentLayer().remove(newShape);
    }
    parent.removeChild(newShape);
  }

}