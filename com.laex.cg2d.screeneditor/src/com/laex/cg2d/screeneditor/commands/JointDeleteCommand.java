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

import com.laex.cg2d.model.model.Joint;

/**
 * The Class JointDeleteCommand.
 */
public class JointDeleteCommand extends Command {

  /** The joint. */
  private final Joint joint;

  /**
   * Instantiates a new joint delete command.
   * 
   * @param joint
   *          the joint
   */
  public JointDeleteCommand(Joint joint) {
    if (joint == null) {
      throw new IllegalArgumentException();
    }
    setLabel("joint deletion");
    this.joint = joint;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  @Override
  public void execute() {
    joint.disconnect();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  @Override
  public void undo() {
    joint.reconnect();
  }

}
