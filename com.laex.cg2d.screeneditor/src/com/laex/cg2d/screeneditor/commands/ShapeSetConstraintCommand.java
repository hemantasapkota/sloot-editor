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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.laex.cg2d.shared.adapter.RectAdapter;
import com.laex.cg2d.shared.model.Joint;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.joints.BEDistanceJoint;

/**
 * The Class ShapeSetConstraintCommand.
 */
public class ShapeSetConstraintCommand extends Command {
  
  /** The new bounds. */
  private final Rectangle newBounds;
  
  /** The old bounds. */
  private Rectangle oldBounds;
  
  /** The request. */
  private final ChangeBoundsRequest request;

  /** The shape. */
  private final Shape shape;

  /**
   * Instantiates a new shape set constraint command.
   *
   * @param shape the shape
   * @param req the req
   * @param newBounds the new bounds
   */
  public ShapeSetConstraintCommand(Shape shape, ChangeBoundsRequest req, Rectangle newBounds) {
    if (shape == null || req == null || newBounds == null) {
      throw new IllegalArgumentException();
    }
    this.shape = shape;
    this.request = req;
    this.newBounds = newBounds.getCopy();
    setLabel("move / resize");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute() {
    Object type = request.getType();
    // make sure the Request is of a type we support:
    boolean status = (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type)
        || RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
    return status;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute() {
    oldBounds = RectAdapter.d2dRect(shape.getBounds());
    redo();
    // Recalculate if distance joint
    updateJoints();
  }

  /**
   * Update joints.
   */
  private void updateJoints() {
    List sj = shape.getSourceJoints();
    List tj = shape.getTargetJoints();

    if (sj.size() > 0) {
      for (Object jd : shape.getSourceJoints()) {
        updateJoint((Joint) jd);
        updateDistanceJoints((Joint) jd);
      }
    }
    // This is important. A shape can be in both source and target list.
    // Therefore, the if check should be independent
    if (tj.size() > 0) {
      for (Object jd : shape.getTargetJoints()) {
        updateJoint((Joint) jd);
        updateDistanceJoints((Joint) jd);
      }
    }
  }

  /**
   * Update joint.
   *
   * @param jd the jd
   */
  private void updateJoint(Joint jd) {
    // jd.computeWorldAnchors();
  }

  /**
   * Update distance joints.
   *
   * @param jd the jd
   */
  private void updateDistanceJoints(Joint jd) {

    if (jd instanceof BEDistanceJoint) {
      BEDistanceJoint idj = (BEDistanceJoint) jd;
      if (idj.getSource() == shape || idj.getTarget() == shape) {
        idj.calculateLength();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo() {
    shape.setBounds(RectAdapter.gdxRect(newBounds));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo() {
    shape.setBounds(RectAdapter.gdxRect(oldBounds));
    // Update after a un-do
    updateJoints();
  }

}
