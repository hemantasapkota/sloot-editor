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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.ShapesDiagram;
import com.laex.cg2d.screeneditor.editparts.policies.ShapesXYLayoutEditPolicy;

/**
 * The Class ShapesDiagramEditPart.
 */
public class ScreenEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

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
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
   */
  public void deactivate() {
    if (isActive()) {
      super.deactivate();
      ((ModelElement) getModel()).removePropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies() {
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new ShapesXYLayoutEditPolicy());
    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang
   * .Class)
   */
  @Override
  public Object getAdapter(Class adapter) {
    if (adapter == ShapesDiagram.class) {
      return getModel();
    }
    
    if (adapter == SnapToHelper.class) {
      List<SnapToHelper> snapStragies = new ArrayList<SnapToHelper>();

      boolean gridEnabled = (Boolean) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_VISIBLE);
      if (gridEnabled) {
        snapStragies.add(new SnapToGrid(this));
      }

      boolean snapEnabled = (Boolean) getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
      if (snapEnabled) {
        snapStragies.add(new SnapToGeometry(this));
      }

      if (snapStragies.size() == 0) {
        return null;
      }

      if (snapStragies.size() == 1) {
        return snapStragies.get(0);
      }

      SnapToHelper[] ss = new SnapToHelper[snapStragies.size()];
      for (int i = 0; i < ss.length; i++) {
        ss[i] = snapStragies.get(i);
      }
      return new CompoundSnapToHelper(ss);
    }
    return super.getAdapter(adapter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure() {
    FreeformLayer f = new FreeformLayer();
    f.setBorder(new MarginBorder(1));

    FreeformLayout fl = new FreeformLayout();
    f.setLayoutManager(fl);

    // Create the static router for the connection layer
    ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
    connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));

    return f;
  }

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private ShapesDiagram getCastedModel() {
    return (ShapesDiagram) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
   */
  protected List getModelChildren() {
    return getCastedModel().getChildren();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractEditPart#getCommand(org.eclipse.gef.Request
   * )
   */
  @Override
  public Command getCommand(Request request) {
    return super.getCommand(request);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  public void propertyChange(PropertyChangeEvent evt) {
    String prop = evt.getPropertyName();
    // these properties are fired when Shapes are added into or removed from
    // the ShapeDiagram instance and must cause a call of refreshChildren()
    // to update the shapes's contents.
    if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop) || ShapesDiagram.CHILD_REMOVED_PROP.equals(prop)) {
      refreshChildren();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
   */
  @Override
  protected void refreshChildren() {
    super.refreshChildren();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getLayer(java.lang.
   * Object)
   */
  @Override
  protected IFigure getLayer(Object layer) {
    return super.getLayer(layer);
  }

}