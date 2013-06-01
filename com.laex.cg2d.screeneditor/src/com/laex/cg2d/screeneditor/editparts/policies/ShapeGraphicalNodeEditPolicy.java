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
package com.laex.cg2d.screeneditor.editparts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.commands.JointCreateCommand;
import com.laex.cg2d.screeneditor.commands.JointReconnectCommand;

/**
 * The Class ShapeGraphicalNodeEditPolicy.
 */
public class ShapeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
   * getConnectionCompleteCommand
   * (org.eclipse.gef.requests.CreateConnectionRequest)
   */
  protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
    JointCreateCommand cmd = (JointCreateCommand) request.getStartCommand();
    cmd.setTarget((Shape) getHost().getModel());
    return cmd;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand
   * (org.eclipse.gef.requests.CreateConnectionRequest)
   */
  protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
    Shape source = (Shape) getHost().getModel();
    JointType type = (JointType) request.getNewObjectType();
    JointCreateCommand cmd = new JointCreateCommand(source, type);
    request.setStartCommand(cmd);
    return cmd;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand
   * (org.eclipse.gef.requests.ReconnectRequest)
   */
  protected Command getReconnectSourceCommand(ReconnectRequest request) {
    Joint jonn = (Joint) request.getConnectionEditPart().getModel();
    Shape newSource = (Shape) getHost().getModel();
    JointReconnectCommand cmd = new JointReconnectCommand(jonn);
    cmd.setNewSource(newSource);
    return cmd;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand
   * (org.eclipse.gef.requests.ReconnectRequest)
   */
  protected Command getReconnectTargetCommand(ReconnectRequest request) {
    Joint jonn = (Joint) request.getConnectionEditPart().getModel();
    Shape newTarget = (Shape) getHost().getModel();
    JointReconnectCommand cmd = new JointReconnectCommand(jonn);
    cmd.setNewTarget(newTarget);
    return cmd;
  }
}
