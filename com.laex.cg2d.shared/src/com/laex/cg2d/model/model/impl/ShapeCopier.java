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
package com.laex.cg2d.model.model.impl;

import com.google.common.base.Throwables;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.ModelCopier;
import com.laex.cg2d.model.model.Shape;

/**
 * The Class ShapeCopier.
 */
public class ShapeCopier implements ModelCopier {

  /**
   * Instantiates a new shape copier.
   *
   * @param gameModel the game model
   */
  public ShapeCopier() {
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.model.model.ModelCopier#copy(java.lang.Object)
   */
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

    //args[0] should be the layer to copy this shape into
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
