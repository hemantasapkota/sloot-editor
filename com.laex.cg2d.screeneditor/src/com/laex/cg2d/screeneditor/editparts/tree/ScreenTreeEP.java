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
package com.laex.cg2d.screeneditor.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;

/**
 * The Class ScreenTreeEP.
 */
public class ScreenTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  /**
   * Instantiates a new screen tree ep.
   * 
   * @param model
   *          the model
   */
  public ScreenTreeEP(ShapesDiagram model) {
    super(model);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
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
   * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
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
   * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
   */
  @Override
  protected List<?> getModelChildren() {
    return (List<?>) getCastedModel().getLayers();
  }

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private ShapesDiagram getCastedModel() {
    return (ShapesDiagram) getModel();
  }

  /**
   * Gets the edits the part for child.
   * 
   * @param child
   *          the child
   * @return the edits the part for child
   */
  private EditPart getEditPartForChild(Object child) {
    return (EditPart) getViewer().getEditPartRegistry().get(child);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String prop = evt.getPropertyName();
    if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop) || ShapesDiagram.LAYER_ADDED.equals(prop)) {
      // add a child to this edit part
      addChild(createChild(evt.getNewValue()), -1);
    } else if (ShapesDiagram.CHILD_REMOVED_PROP.equals(prop) || ShapesDiagram.LAYER_REMOVED.equals(prop)) {
      // remove a child from this edit part
      EditPart ep = getEditPartForChild(evt.getNewValue());
      if (ep != null) {
        removeChild(ep);
      }
    } else {
      refreshVisuals();
    }

  }

  @Override
  protected void removeChild(EditPart child) {

    /* Special treatment for removing ShapeTreeEP */
    if (child instanceof ShapeTreeEP) {

      int index = child.getParent().getChildren().indexOf(child);
      fireRemovingChild(child, index);
      if (isActive())
        child.deactivate();
      child.removeNotify();
      removeChildVisual(child);
      child.getParent().getChildren().remove(child);
      child.setParent(null);

    }

    super.removeChild(child);

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractTreeEditPart#addChildVisual(org.eclipse
   * .gef.EditPart, int)
   */
  @Override
  protected void addChildVisual(EditPart childEditPart, int index) {
    Widget widget = getWidget();
    TreeItem item = null;

    if (childEditPart.getModel() instanceof Layer) {
      Tree t = (Tree) widget;
      int trueIndex = index;

      if (trueIndex >= t.getItemCount()) {
        trueIndex = (t.getItemCount() == 0) ? 0 : t.getItemCount() - 1;
      }

      item = new TreeItem(t, 0, trueIndex);
    }

    // If it is shape model, then we put it inside its layer
    Object model = childEditPart.getModel();
    if (model instanceof Shape) {
      Shape shpM = (Shape) childEditPart.getModel();
      Layer layer = shpM.getParentLayer();
      LayerTreeEP ltep = (LayerTreeEP) getViewer().getEditPartRegistry().get(layer);

      int trueIndex = (ltep.getChildren().size() == 0) ? 0 : ltep.getChildren().size() - 1;
      item = new TreeItem((TreeItem) ltep.getWidget(), 0, (trueIndex <= 0) ? 0 : trueIndex);
    }

    ((TreeEditPart) childEditPart).setWidget(item);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
   */
  @Override
  protected void createEditPolicies() {
    if (getParent() instanceof RootEditPart) {
      installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    }
  }

}
