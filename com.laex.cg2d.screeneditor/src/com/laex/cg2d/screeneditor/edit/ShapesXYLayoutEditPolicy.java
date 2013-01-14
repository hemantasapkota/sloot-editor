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
package com.laex.cg2d.screeneditor.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;
import com.laex.cg2d.screeneditor.commands.ShapeSetConstraintCommand;
import com.laex.cg2d.screeneditor.factory.ShapeCreationInfo;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.ShapesDiagram;

/**
 * The Class ShapesXYLayoutEditPolicy.
 */
public class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
   * createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest,
   * org.eclipse.gef.EditPart, java.lang.Object)
   */
  protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
    boolean childTest = child instanceof ShapeEditPart;

    if (childTest && constraint instanceof Rectangle && child.isActive()) {
      // return a command that can move and/or resize a Shape
      return new ShapeSetConstraintCommand((Shape) child.getModel(), request, (Rectangle) constraint);
    }
    return super.createChangeConstraintCommand(request, child, constraint);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
   * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
   */
  protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy
   * (org.eclipse.gef.EditPart)
   */
  @Override
  protected EditPolicy createChildEditPolicy(EditPart child) {
    Shape model = (Shape) child.getModel();

    if (child instanceof ShapeEditPart) {
      if (model.getEditorShapeType().isBox() || model.getEditorShapeType().isCircle()
          || model.getEditorShapeType().isEdge()) {
        return new ShapeResizableEditPolicy();
      } else {
        return new NonResizableEditPolicy();
      }
    }

    return super.createChildEditPolicy(child);
  }

  /*
   * (non-Javadoc)
   * 
   * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
   */
  protected Command getCreateCommand(CreateRequest request) {
    ShapesDiagram parent = (ShapesDiagram) getHost().getModel();
    Rectangle constraint = (Rectangle) getConstraintFor(request);
    ShapeCreationInfo tci = (ShapeCreationInfo) request.getNewObject();

    ShapeCreateCommand createCommand = new ShapeCreateCommand(tci.getShape(), tci.getShape().getParentLayer(), parent,
        constraint);
    return createCommand;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.GraphicalEditPolicy#getFeedbackLayer()
   */
  @Override
  protected IFigure getFeedbackLayer() {
    return super.getFeedbackLayer();
  }

}