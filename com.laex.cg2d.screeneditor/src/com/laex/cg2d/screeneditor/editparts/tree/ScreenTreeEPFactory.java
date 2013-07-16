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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;

/**
 * A factory for creating ScreenTreeEP objects.
 */
public class ScreenTreeEPFactory implements EditPartFactory {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
   * java.lang.Object)
   */
  @Override
  public EditPart createEditPart(EditPart context, Object model) {
    if (model instanceof GameModel) {
      return new ScreenTreeEP(((GameModel) model).getDiagram());
    } else if (model instanceof Layer) {
      return new LayerTreeEP((Layer) model);
    } else if (model instanceof Shape) {
      return new ShapeTreeEP((Shape) model);
    }
    return null;
  }

}
