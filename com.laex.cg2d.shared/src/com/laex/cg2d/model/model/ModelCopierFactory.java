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
package com.laex.cg2d.model.model;

import com.laex.cg2d.model.model.impl.ShapeCopier;

/**
 * A factory for creating ModelCopier objects.
 */
public class ModelCopierFactory {

  /**
   * Gets the model copier.
   * 
   * @param modelType
   *          the model type
   * @param model
   *          the model
   * @return the model copier
   */
  public static ModelCopier getModelCopier(Class<?> modelType) {
    if (modelType == Shape.class) {
      return new ShapeCopier();
    }

    return null;
  }

}
