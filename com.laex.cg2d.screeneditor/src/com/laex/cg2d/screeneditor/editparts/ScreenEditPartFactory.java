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
package com.laex.cg2d.screeneditor.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;

/**
 * A factory for creating ShapesEditPart objects.
 */
public class ScreenEditPartFactory implements EditPartFactory {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
   * java.lang.Object)
   */
  public EditPart createEditPart(EditPart context, Object modelElement) {
    // get EditPart for model element
    EditPart part = getPartForElement(modelElement);
    // store model element in EditPart
    part.setModel(modelElement);
    return part;
  }

  /**
   * Maps an object to an EditPart.
   * 
   * @param modelElement
   *          the model element
   * @return the part for element
   */
  private EditPart getPartForElement(Object modelElement) {
    if (modelElement instanceof ShapesDiagram) {
      return new ScreenEditPart();
    }

    if (modelElement instanceof Shape) {
      return new ShapeEditPart();
    }

    if (modelElement instanceof Joint) {
      return new JointEditPart();
    }

    throw new RuntimeException("Can't create part for model element: "
        + ((modelElement != null) ? modelElement.getClass().getName() : "null"));
  }
}