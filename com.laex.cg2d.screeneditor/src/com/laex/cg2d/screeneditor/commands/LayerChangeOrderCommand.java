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
 * The Class LayerChangeOrderCommand.
 */
public class LayerChangeOrderCommand extends Command {
  
  /** The diagram. */
  private ShapesDiagram diagram;
  
  /** The layers. */
  private Layer[] layers;

  /**
   * Instantiates a new layer change order command.
   *
   * @param diagram the diagram
   * @param layers the layers
   */
  public LayerChangeOrderCommand(ShapesDiagram diagram, Layer[] layers) {
    this.diagram = diagram;
    this.layers = layers;
    setLabel("Change layer order");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  @Override
  public boolean canExecute() {
    return (layers !=  null) && (diagram != null);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  @Override
  public boolean canUndo() {
    return false;
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
    //clear the layers
    diagram.getLayers().clear();
    
    //add the layers in the specified order
    for (Layer o : layers) {
      diagram.getLayers().add(o);
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
  }

}
