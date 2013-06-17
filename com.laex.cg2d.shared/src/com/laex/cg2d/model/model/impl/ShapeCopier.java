package com.laex.cg2d.model.model.impl;

import com.google.common.base.Throwables;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.ModelCopier;
import com.laex.cg2d.model.model.Shape;

public class ShapeCopier implements ModelCopier {

  private GameModel gameModel;

  public ShapeCopier(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  @Override
  public Object copy(Object element) {
    if (!(element instanceof Shape)) {
      // empty object
      return new Object();
    }

    // How to copy a shape:
    // 1. Convert shape model to CGShape
    // 2. Create a new CGShape builder and modify properties
    // 3. Convert back the CGShape to Shape
    // 4. Create entities if the model is of type entity.
    Shape model = (Shape) element;
    CGShape cgModelPrototype = CGScreenModelAdapter.asCGShape(model);
    CGShape cgModelToCopy = CGShape.newBuilder(cgModelPrototype).build();

    Shape finalModelToCopy = ScreenModelAdapter.asShape(cgModelToCopy, model.getParentLayer());

    if (finalModelToCopy.getEditorShapeType().isEntity()) {
      Entity ent;
      try {
        ent = Entity.createFromFile(finalModelToCopy.getEntityResourceFile().getResourceFile());
        finalModelToCopy.setEntity(ent);
      } catch (Throwable t) {
        Throwables.propagate(t);
      }
    }
    
    /*
     * Copy everything but the id.
     * The responsibility of creating new id lies with paste handler.
     * Why is this ?
     * Because, to generate unique id for each shape.
     */

    return finalModelToCopy;

  }

}
