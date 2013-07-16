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
package com.laex.cg2d.model;

import com.laex.cg2d.model.model.Layer;

/**
 * The Interface ILayerManager.
 */
public interface ILayerManager {

  /**
   * Gets the new layer id.
   * 
   * @return the new layer id
   */
  int getNewLayerId();

  /**
   * Layer count.
   * 
   * @return the int
   */
  int layerCount();

  /**
   * Gets the layer at.
   * 
   * @param index
   *          the index
   * @return the layer at
   */
  Layer getLayerAt(int index);

  /**
   * Adds the layer.
   * 
   * @param layer
   *          the layer
   */
  void addLayer(Layer layer);

  /**
   * Removes the layer.
   * 
   * @param layer
   *          the layer
   */
  void removeLayer(Layer layer);

  /**
   * Removes the all.
   */
  void removeAll();

  /**
   * Change layer properties.
   * 
   * @param changedLayer
   *          the changed layer
   */
  void changeLayerProperties(Layer changedLayer);

  /**
   * Change layer order.
   * 
   * @param orderedLayers
   *          the ordered layers
   */
  void changeLayerOrder(Layer[] orderedLayers);

  /**
   * Gets the current layer.
   * 
   * @return the current layer
   */
  Layer getCurrentLayer();

}
