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

/**
 * The Class ShapeChangeIDCommand.
 */
public class ShapeChangeIDCommand extends Command {

  /** The shape. */
  private Shape shape;

  /** The new id. */
  private String newId;

  /** The old id. */
  private String oldId;

  /**
   * Instantiates a new shape change id command.
   * 
   * @param shape
   *          the shape
   * @param newId
   *          the new id
   */
  public ShapeChangeIDCommand(Shape shape, String newId) {
    this.shape = shape;
    this.newId = newId;
    this.oldId = shape.getId();
    setLabel("Change ID");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  @Override
  public void execute() {
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  @Override
  public void redo() {
    shape.setId(newId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
    shape.setId(oldId);
  }

}
