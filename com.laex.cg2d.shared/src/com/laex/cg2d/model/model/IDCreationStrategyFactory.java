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

import com.laex.cg2d.model.model.impl.IDCreationStrategyImpl;

/**
 * A factory for creating IDCreationStrategy objects.
 */
public class IDCreationStrategyFactory {

  /**
   * Gets the iD creator.
   * 
   * @param model
   *          the model
   * @return the iD creator
   */
  public static IDCreationStrategy getIDCreator(GameModel model) {
    // We always return new instances. not singleton.
    return new IDCreationStrategyImpl(model);
  }

}
