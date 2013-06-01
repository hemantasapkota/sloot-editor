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
package com.laex.cg2d.screeneditor.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.screeneditor.commands.JointDeleteCommand;

/**
 * The Class JointEditPart.
 */
class JointEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
   */
  public void activate() {
    if (!isActive()) {
      super.activate();
      ((ModelElement) getModel()).addPropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent arg0) {
    refreshVisuals();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
   */
  @Override
  protected IFigure createFigure() {
    PolylineConnection jointCon = (PolylineConnection) super.createFigure();
    jointCon.setLineStyle(Graphics.LINE_SOLID);
    return jointCon;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  @Override
  protected void createEditPolicies() {
    // Selection handle edit policy.
    // Makes the connection show a feedback, when selected by the user.
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    // Allows the removal of the connection model element
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
      protected Command getDeleteCommand(GroupRequest request) {
        return new JointDeleteCommand(getCastedModel());
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
   */
  public void deactivate() {
    if (isActive()) {
      super.deactivate();
      ((ModelElement) getModel()).removePropertyChangeListener(this);
    }
  }

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private Joint getCastedModel() {
    return (Joint) getModel();
  }
}
