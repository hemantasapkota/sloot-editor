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

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;

/**
 * The Class ShapeDeleteCommand.
 */
public class ShapeDeleteCommand extends Command {

  /** The child. */
  private final Shape child;

  /** The parent layer. */
  private Layer parentLayer;

  /** The parent. */
  private final ShapesDiagram parent;

  /** The source connections. */
  private List sourceConnections;

  /** The target connections. */
  private List targetConnections;

  /** The was removed. */
  private boolean wasRemoved;

  /**
   * The Enum DeleteCommandType.
   */
  public enum DeleteCommandType {

    /** The undoable. */
    UNDOABLE,
    /** The non undoable. */
    NON_UNDOABLE;

    /**
     * Checks if is undoable.
     * 
     * @return true, if is undoable
     */
    boolean isUndoable() {
      return this == UNDOABLE;
    }

    /**
     * Checks if is non undoable.
     * 
     * @return true, if is non undoable
     */
    boolean isNonUndoable() {
      return this == NON_UNDOABLE;
    }
  };

  /** The cmd type. */
  private DeleteCommandType cmdType;

  /**
   * Instantiates a new shape delete command.
   * 
   * @param parent
   *          the parent
   * @param child
   *          the child
   * @param delCmdType
   *          the del cmd type
   */
  public ShapeDeleteCommand(ShapesDiagram parent, Shape child, DeleteCommandType delCmdType) {
    if (parent == null || child == null) {
      throw new IllegalArgumentException();
    }
    setLabel("shape deletion");
    this.parent = parent;
    this.child = child;
    this.cmdType = delCmdType;
  }

  /**
   * Adds the connections.
   * 
   * @param connections
   *          the connections
   */
  private void addConnections(List connections) {
    for (int i = 0; i < connections.size(); i++) {
      Joint conn = (Joint) connections.get(i);
      conn.reconnect();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  public boolean canUndo() {
    if (cmdType.isNonUndoable())
      return false;

    return wasRemoved;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute() {
    // store a copy of incoming & outgoing connections before proceeding
    sourceConnections = child.getSourceJoints();
    targetConnections = child.getTargetJoints();
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo() {
    // remove the child and disconnect its connections
    wasRemoved = parent.removeChild(child);
    if (wasRemoved) {
      parent.removeChild(child);
	    parentLayer = child.getParentLayer();
	    parentLayer.remove(child);
	    child.setParentLayer(null);
      removeConnections(sourceConnections);
      removeConnections(targetConnections);
    }
  }

  /**
   * Disconnects a List of Connections from their endpoints.
   * 
   * @param connections
   *          a non-null List of connections
   */
  private void removeConnections(List connections) {
    for (int i = 0; i < connections.size(); i++) {
      Joint conn = (Joint) connections.get(i);
      conn.disconnect();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo() {
    // add the child and reconnect its connections

    /*
     * First add the layers. When we add the shape, the shape tree edit part
     * requires a layer to add the child to.
     */
    parentLayer.add(child);
    child.setParentLayer(parentLayer);

    if (parent.addChild(child)) {
      addConnections(sourceConnections);
      addConnections(targetConnections);
    }
  }

  /**
   * The main method.
   * 
   * @param args
   *          the arguments
   */
  public static void main(String[] args) {
  }
}