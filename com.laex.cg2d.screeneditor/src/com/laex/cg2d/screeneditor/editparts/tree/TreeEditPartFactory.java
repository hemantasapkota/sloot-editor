package com.laex.cg2d.screeneditor.editparts.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;

public class TreeEditPartFactory implements EditPartFactory {

  @Override
  public EditPart createEditPart(EditPart context, Object model) {
    if (model instanceof GameModel) {
      return new ShapesDiagramTreeEditPart(((GameModel) model).getDiagram());
    } else if (model instanceof Layer) {
      return new LayerTreeEditPart((Layer) model);
    } else if (model instanceof Shape) {
      return new ShapeTreeEditPart((Shape) model);
    }
    return null;
  }
  
  
}
