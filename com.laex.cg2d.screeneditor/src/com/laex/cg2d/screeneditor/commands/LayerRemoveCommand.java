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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand.DeleteCommandType;

/**
 * The Class LayerRemoveCommand.
 */
public class LayerRemoveCommand extends Command {

  /** The parent. */
  private ShapesDiagram parent;

  /** The layer. */
  private Layer layer;

  /** The deleted children. */
  private List<Shape> deletedChildren;

  /**
   * Instantiates a new layer remove command.
   * 
   * @param layer
   *          the layer
   * @param parent
   *          the parent
   */
  public LayerRemoveCommand(Layer layer, ShapesDiagram parent) {
    this.layer = layer;
    this.parent = parent;
    this.setLabel("Remove Layer");

  }

  /**
   * Builds the remove shape commands.
   * 
   * @param layer
   *          the layer
   * @param parent
   *          the parent
   */
  private void buildRemoveShapeCommands(Layer layer, ShapesDiagram parent) {
    CompoundCommand cc = new CompoundCommand("Remove shapes in layer");

    // make a copy of all the children in this layer
    deletedChildren = new ArrayList<Shape>();
    for (Shape s : layer.getChildren()) {
      deletedChildren.add(s);
    }

    for (int i = 0; i < layer.getChildren().size(); i++) {
      cc.add(new ShapeDeleteCommand(parent, layer.getChildren().get(i), DeleteCommandType.UNDOABLE));
    }
    cc.execute();
  }

  /**
   * Builds the add shape commands.
   * 
   * @param layer
   *          the layer
   * @param parent
   *          the parent
   */
  private void buildAddShapeCommands(Layer layer, ShapesDiagram parent) {
    CompoundCommand cc = new CompoundCommand("Add shapes in layer");
    for (int i = 0; i < deletedChildren.size(); i++) {
      cc.add(new ShapeCreateCommand(deletedChildren.get(i), parent));
    }
    cc.execute();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  @Override
  public boolean canExecute() {
    return layer != null && parent != null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  @Override
  public void execute() {
    //First remove all the shapes
    buildRemoveShapeCommands(layer, parent);
    
    //then remove the layer
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  @Override
  public void redo() {
    parent.removeLayer(layer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
    parent.addLayer(layer);
    buildAddShapeCommands(layer, parent);
  }

}
