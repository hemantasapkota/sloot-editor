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
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.ShapesDiagram;

/**
 * The Class LayerChangePropertiesCommand.
 */
public class LayerChangePropertiesCommand extends Command {

  /** The diagram. */
  private ShapesDiagram diagram;
  
  /** The layer. */
  private Layer layer;
  
  /** The old layer. */
  private Layer oldLayer;

  /**
   * Instantiates a new layer change properties command.
   *
   * @param layer the layer
   * @param diagram the diagram
   */
  public LayerChangePropertiesCommand(Layer layer, ShapesDiagram diagram) {
    this.diagram = diagram;
    this.layer = layer;
    this.setLabel("Change Layer Properties");
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  @Override
  public boolean canExecute() {
    return layer != null && diagram != null && diagram.getLayers() != null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#execute()
   */
  @Override
  public void execute() {
    redo();
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#redo()
   */
  @Override
  public void redo() {
    for (Layer li : diagram.getLayers()) {
      if (li.compareTo(this.layer) == 0) {
        oldLayer = new Layer(li.getId(), li.getName(), li.isVisible(), li.isLocked());

        li.setName(this.layer.getName());
        li.setLocked(this.layer.isLocked());
        li.setVisible(this.layer.isVisible());

        // Communicate with EditParts of all the shapes
        for (Shape s : li.getChildren()) {
          s.setVisible(this.layer.isVisible());
          s.setLocked(this.layer.isLocked());
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
    for (Layer li : diagram.getLayers()) {
      if (li.compareTo(this.layer) == 0) {
        li.setName(oldLayer.getName());
        li.setLocked(oldLayer.isLocked());
        li.setVisible(oldLayer.isVisible());

        // Communicate with EditParts of all the shapes
        for (Shape s : li.getChildren()) {
          s.setVisible(this.layer.isVisible());
          s.setLocked(this.layer.isLocked());
        }
      }
    }
  }

}
