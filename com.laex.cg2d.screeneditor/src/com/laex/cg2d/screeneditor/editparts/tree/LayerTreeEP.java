package com.laex.cg2d.screeneditor.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.Layer;

public class LayerTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  public LayerTreeEP(Layer layer) {
    super(layer);
  }

  @Override
  protected List getModelChildren() {
    return getCastedModel().getChildren();
  }

  private Layer getCastedModel() {
    return (Layer) getModel();
  }

  @Override
  protected Image getImage() {
    return SharedImages.LAYER.createImage();
  }

  @Override
  protected String getText() {
    return getCastedModel().getName();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    refreshVisuals();
  }

}
