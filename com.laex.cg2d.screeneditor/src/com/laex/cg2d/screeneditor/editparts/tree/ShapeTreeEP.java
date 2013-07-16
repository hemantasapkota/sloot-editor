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

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.adapter.ShapesDiagramAdapter;
import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.Shape;

/**
 * The Class ShapeTreeEP.
 */
public class ShapeTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  /**
   * Instantiates a new shape tree ep.
   * 
   * @param shape
   *          the shape
   */
  public ShapeTreeEP(Shape shape) {
    super(shape);
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

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private Shape getCastedModel() {
    return (Shape) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
   */
  @Override
  protected String getText() {
    return getCastedModel().getId();
  }

  @Override
  public boolean isSelectable() {
    return !getCastedModel().isLocked();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#getAdapter(java.lang.Class)
   */
  @Override
  public Object getAdapter(Class key) {
    if (key == Shape.class) {
      return getModel();
    }

    if (key == ShapesDiagramAdapter.class) {
      return getParent().getModel();
    }

    return super.getAdapter(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
   */
  @Override
  protected Image getImage() {
    Shape shp = getCastedModel();

    if (shp.getEditorShapeType().isBackground()) {
      return SharedImages.BOX.createImage();
    }

    if (shp.getEditorShapeType().isBox()) {
      return SharedImages.BOX.createImage();
    }

    if (shp.getEditorShapeType().isCircle()) {
      return SharedImages.CIRCLE.createImage();
    }

    if (shp.getEditorShapeType().isEdge()) {
      return SharedImages.BOX.createImage();
    }

    if (shp.getEditorShapeType().isBackground()) {
      return SharedImages.HEXAGON.createImage();
    }

    if (shp.getEditorShapeType().isEntity()) {
      return getCastedModel().getEntity().getDefaultFrame();
    }

    return SharedImages.HEXAGON.createImage();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent prop) {
    if (prop.getPropertyName().equals(Shape.SHAPE_ID)) {
      refreshVisuals();
    }
  }

}
