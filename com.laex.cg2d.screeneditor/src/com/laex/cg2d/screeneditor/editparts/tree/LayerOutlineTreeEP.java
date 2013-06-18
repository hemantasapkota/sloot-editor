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

import java.util.ArrayList;
import java.util.List;

import com.laex.cg2d.model.model.Layer;

/**
 * The Class LayerOutlineTreeEP.
 */
public class LayerOutlineTreeEP extends LayerTreeEP {

  /**
   * Instantiates a new layer outline tree ep.
   *
   * @param layer the layer
   */
  public LayerOutlineTreeEP(Layer layer) {
    super(layer);
  }
  
  /* (non-Javadoc)
   * @see com.laex.cg2d.screeneditor.editparts.tree.LayerTreeEP#getModelChildren()
   */
  @Override
  protected List getModelChildren() {
    //return empty list
    return new ArrayList();
  }

}
