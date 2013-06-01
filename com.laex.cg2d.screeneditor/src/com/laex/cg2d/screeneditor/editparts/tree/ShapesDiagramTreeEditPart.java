package com.laex.cg2d.screeneditor.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.ShapesDiagram;

public class ShapesDiagramTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

  public ShapesDiagramTreeEditPart(ShapesDiagram model) {
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
    if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop)) {
      // add a child to this edit part
      // causes an additional entry to appear in the tree of the outline
      // view
      addChild(createChild(evt.getNewValue()), -1);
    } else if (ShapesDiagram.CHILD_REMOVED_PROP.equals(prop)) {
      // remove a child from this edit part
      // causes the corresponding edit part to disappear from the tree in
      // the outline view
      removeChild(getEditPartForChild(evt.getNewValue()));
    } else {
      refreshVisuals();
    }
  }

  @Override
  protected void createEditPolicies() {
    if (getParent() instanceof RootEditPart) {
      installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    }
  }

}
