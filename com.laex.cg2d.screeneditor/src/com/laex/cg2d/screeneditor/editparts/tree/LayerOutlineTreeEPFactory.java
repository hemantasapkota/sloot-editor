package com.laex.cg2d.screeneditor.editparts.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;

public class LayerOutlineTreeEPFactory implements EditPartFactory {

  @Override
  public EditPart createEditPart(EditPart context, Object model) {
    if (model instanceof GameModel) {
      return new ScreenTreeEP(((GameModel) model).getDiagram());
    } else if (model instanceof Shape) {
      return new ShapeTreeEP((Shape) model);
    } else if (model instanceof Layer) {
      return new LayerOutlineTreeEP((Layer) model);
    }
    return null;
  }

}
