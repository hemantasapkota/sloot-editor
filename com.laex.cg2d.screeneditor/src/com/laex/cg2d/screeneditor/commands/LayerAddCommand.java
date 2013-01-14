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

import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.ShapesDiagram;

/**
 * The Class LayerAddCommand.
 */
public class LayerAddCommand extends Command {

  /** The new layer. */
  private Layer newLayer;

  /** The diagram. */
  private ShapesDiagram diagram;

  /**
   * Instantiates a new layer add command.
   * 
   * @param newLayer
   *          the new layer
   * @param diagram
   *          the diagram
   */
  public LayerAddCommand(Layer newLayer, ShapesDiagram diagram) {
    this.newLayer = newLayer;
    this.diagram = diagram;
    this.setLabel("Add Layer");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  @Override
  public boolean canExecute() {
    return newLayer != null && diagram != null && diagram.getLayers() != null;
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
    diagram.getLayers().add(newLayer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
    diagram.getLayers().remove(newLayer);
  }

}
