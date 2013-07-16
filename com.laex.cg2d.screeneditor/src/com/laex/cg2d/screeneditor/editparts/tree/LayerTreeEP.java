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

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ModelElement;

/**
 * The Class LayerTreeEP.
 */
public class LayerTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  /**
   * Instantiates a new layer tree ep.
   * 
   * @param layer
   *          the layer
   */
  public LayerTreeEP(Layer layer) {
    super(layer);
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
    return getCastedModel().getChildren();
  }

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private Layer getCastedModel() {
    return (Layer) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
   */
  @Override
  protected Image getImage() {
    return SharedImages.LAYER.createImage();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
   */
  @Override
  protected String getText() {
    return getCastedModel().getName();
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
    if (evt.getPropertyName().equals(Layer.LAYER_NAME_CHANGED)) {
      refreshVisuals();
    }
  }

}
