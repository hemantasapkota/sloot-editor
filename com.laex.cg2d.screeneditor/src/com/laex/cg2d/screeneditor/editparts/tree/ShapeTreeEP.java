package com.laex.cg2d.screeneditor.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.adapter.ShapesDiagramAdapter;
import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.Shape;

public class ShapeTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  public ShapeTreeEP(Shape shape) {
    super(shape);
  }

  public void activate() {
    if (!isActive()) {
      super.activate();
      ((ModelElement) getModel()).addPropertyChangeListener(this);
    }
  }

  public void deactivate() {
    if (isActive()) {
      super.deactivate();
      ((ModelElement) getModel()).removePropertyChangeListener(this);
    }
  }

  private Shape getCastedModel() {
    return (Shape) getModel();
  }

  @Override
  protected String getText() {
    return getCastedModel().getId();
  }
  
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

  @Override
  public void propertyChange(PropertyChangeEvent arg0) {
    refreshVisuals();
  }

}
