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

import java.util.Iterator;

import org.eclipse.gef.commands.Command;

import com.laex.cg2d.shared.model.Joint;
import com.laex.cg2d.shared.model.Shape;

/**
 * The Class JointReconnectCommand.
 */
public class JointReconnectCommand extends Command {

  /** The connection instance to reconnect. */
  private Joint connection;
  /** The new source endpoint. */
  private Shape newSource;
  /** The new target endpoint. */
  private Shape newTarget;
  /** The original source endpoint. */
  private final Shape oldSource;
  /** The original target endpoint. */
  private final Shape oldTarget;

  /**
   * Instantiate a command that can reconnect a Connection instance to a
   * different source or target endpoint.
   * 
   * @param conn
   *          the connection instance to reconnect (non-null)
   */
  public JointReconnectCommand(Joint conn) {
    if (conn == null) {
      throw new IllegalArgumentException();
    }
    this.connection = conn;
    this.oldSource = conn.getSource();
    this.oldTarget = conn.getTarget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute() {
    if (newSource != null) {
      return checkSourceReconnection();
    } else if (newTarget != null) {
      return checkTargetReconnection();
    }
    return false;
  }

  /**
   * Return true, if reconnecting the connection-instance to newSource is
   * allowed.
   * 
   * @return true, if successful
   */
  private boolean checkSourceReconnection() {
    // connection endpoints must be different Shapes
    if (newSource.equals(oldTarget)) {
      return false;
    }
    // return false, if the connection exists already
    for (Iterator iter = newSource.getSourceJoints().iterator(); iter.hasNext();) {
      Joint conn = (Joint) iter.next();
      // return false if a newSource -> oldTarget connection exists
      // already
      // and it is a different instance than the connection-field
      if (conn.getTarget().equals(oldTarget) && !conn.equals(connection)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true, if reconnecting the connection-instance to newTarget is
   * allowed.
   * 
   * @return true, if successful
   */
  private boolean checkTargetReconnection() {
    // connection endpoints must be different Shapes
    if (newTarget.equals(oldSource)) {
      return false;
    }
    // return false, if the connection exists already
    for (Iterator iter = newTarget.getTargetJoints().iterator(); iter.hasNext();) {
      Joint conn = (Joint) iter.next();
      // return false if a oldSource -> newTarget connection exists
      // already
      // and it is a differenct instance that the connection-field
      if (conn.getSource().equals(oldSource) && !conn.equals(connection)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Reconnect the connection to newSource (if setNewSource(...) was invoked
   * before) or newTarget (if setNewTarget(...) was invoked before).
   */
  public void execute() {
    if (newSource != null) {
      connection.reconnect(newSource, oldTarget);
    } else if (newTarget != null) {
      connection.reconnect(oldSource, newTarget);
    } else {
      throw new IllegalStateException("Should not happen");
    }
  }

  /**
   * Set a new source endpoint for this connection. When execute() is invoked,
   * the source endpoint of the connection will be attached to the supplied
   * Shape instance.
   * <p>
   * Note: Calling this method, deactivates reconnection of the <i>target</i>
   * endpoint. A single instance of this command can only reconnect either the
   * source or the target endpoint.
   * </p>
   * 
   * @param connectionSource
   *          a non-null Shape instance, to be used as a new source endpoint
   */
  public void setNewSource(Shape connectionSource) {
    if (connectionSource == null) {
      throw new IllegalArgumentException();
    }
    setLabel("move joint startpoint");
    newSource = connectionSource;
    newTarget = null;
  }

  /**
   * Set a new target endpoint for this connection When execute() is invoked,
   * the target endpoint of the connection will be attached to the supplied
   * Shape instance.
   * <p>
   * Note: Calling this method, deactivates reconnection of the <i>source</i>
   * endpoint. A single instance of this command can only reconnect either the
   * source or the target endpoint.
   * </p>
   * 
   * @param connectionTarget
   *          a non-null Shape instance, to be used as a new target endpoint
   */
  public void setNewTarget(Shape connectionTarget) {
    if (connectionTarget == null) {
      throw new IllegalArgumentException();
    }
    setLabel("move connection endpoint");
    newSource = null;
    newTarget = connectionTarget;
  }

  /**
   * Reconnect the connection to its original source and target endpoints.
   */
  public void undo() {
    connection.reconnect(oldSource, oldTarget);
  }

}
