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

public class ScreenTreeEP extends AbstractTreeEditPart implements PropertyChangeListener {

  public ScreenTreeEP(ShapesDiagram model) {
    super(model);
  }

  public void activate() {
    if (!isActive()) {
      super.activate();
      ((ModelElement) getModel()).addPropertyChangeListener(this);
    }
  }

  @Override
  protected List getModelChildren() {
    return (List) getCastedModel().getLayers();
  }

  private ShapesDiagram getCastedModel() {
    return (ShapesDiagram) getModel();
  }

  private EditPart getEditPartForChild(Object child) {
    return (EditPart) getViewer().getEditPartRegistry().get(child);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String prop = evt.getPropertyName();
    if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop) || ShapesDiagram.LAYER_ADDED.equals(prop)) {
      // add a child to this edit part
      addChild(createChild(evt.getNewValue()), -1);
    } else if (ShapesDiagram.CHILD_REMOVED_PROP.equals(prop) || ShapesDiagram.LAYER_REMOVED.equals(prop)) {
      // remove a child from this edit part
      EditPart ep = getEditPartForChild(evt.getNewValue());
      removeChild(ep);
    } else {
      refreshVisuals();
    }
    
    refreshVisuals();
  }

  @Override
  protected void addChildVisual(EditPart childEditPart, int index) {
    //Oveeride from abstract class.
    Widget widget = getWidget();
    TreeItem item;

    if (widget instanceof Tree)
      item = new TreeItem((Tree) widget, 0, index);
    else
      item = new TreeItem((TreeItem) widget, 0, index);

    //If it is shape model, then we put it inside its layer
    if (childEditPart.getModel() instanceof Shape) {
      Shape shpM = (Shape) childEditPart.getModel();
      Layer layer = shpM.getParentLayer();
      LayerTreeEP ltep = (LayerTreeEP) getEditPartForChild(layer);

      int trueIndex = ltep.getChildren().size() - 1;
      item = new TreeItem((TreeItem) ltep.getWidget(), 0, (trueIndex <= 0) ? 0 : trueIndex);
    }

    ((TreeEditPart) childEditPart).setWidget(item);
  }

  @Override
  protected void createEditPolicies() {
    if (getParent() instanceof RootEditPart) {
      installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    }
  }

}
