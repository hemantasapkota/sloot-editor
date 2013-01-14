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

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.commands.Command;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.shared.model.Joint;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.joints.BEDistanceJoint;
import com.laex.cg2d.shared.model.joints.BEFrictionJoint;
import com.laex.cg2d.shared.model.joints.BEMouseJoint;
import com.laex.cg2d.shared.model.joints.BEPrismaticJoint;
import com.laex.cg2d.shared.model.joints.BEPulleyJoint;
import com.laex.cg2d.shared.model.joints.BERevoluteJoint;
import com.laex.cg2d.shared.model.joints.BERopeJoint;
import com.laex.cg2d.shared.model.joints.BEWeldJoint;

/**
 * The Class JointCreateCommand.
 */
public class JointCreateCommand extends Command {
  /** The connection instance. */
  private Joint connection;

  /** The type. */
  private JointType type;

  /** Start endpoint for the connection. */
  private final Shape source;
  /** Target endpoint for the connection. */
  private Shape target;

  /**
   * Instantiate a command that can create a connection between two shapes.
   * 
   * @param source
   *          the source endpoint (a non-null Shape instance)
   * @param type
   *          the type
   * @see Connection#setLineStyle(int)
   */
  public JointCreateCommand(Shape source, JointType type) {
    if (source == null) {
      throw new IllegalArgumentException();
    }
    setLabel("connection creation");
    this.source = source;
    this.type = type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute() {
    // disallow source -> source connections
    if (source.equals(target)) {
      return false;
    }
    // return false, if the source -> target connection exists already
    for (Iterator iter = source.getSourceJoints().iterator(); iter.hasNext();) {
      Joint conn = (Joint) iter.next();
      if (conn.getTarget().equals(target)) {
        return false;
      }
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute() {
    // use the supplied line style
    switch (type) {
    case DistanceJoint:
      connection = new BEDistanceJoint(source, target);
      break;
    case RevoluteJoint:
      connection = new BERevoluteJoint(source, target);
      break;
    case PrismaticJoint:
      connection = new BEPrismaticJoint(source, target);
      break;
    case PulleyJoint:
      connection = new BEPulleyJoint(source, target);
      break;
    case MouseJoint:
      connection = new BEMouseJoint(source, target);
      break;
    case FrictionJoint:
      connection = new BEFrictionJoint(source, target);
      break;
    case WeldJoint:
      connection = new BEWeldJoint(source, target);
      break;
    case GearJoint:
      break;
    case RopeJoint:
      connection = new BERopeJoint(source, target);
      break;
    case WheelJoint:
      break;
    case Unknown:
      break;
    default:
      break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo() {
    connection.reconnect();
  }

  /**
   * Set the target endpoint for the connection.
   * 
   * @param target
   *          that target endpoint (a non-null Shape instance)
   */
  public void setTarget(Shape target) {
    if (target == null) {
      throw new IllegalArgumentException();
    }
    this.target = target;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo() {
    connection.disconnect();
  }
}
