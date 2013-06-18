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


/**
 * The Class IDCreationStrategy.
 */
public interface IDCreationStrategy {

  
  /**
   * New id.
   *
   * @param type the type
   * @return the string
   */
  String newId(EditorShapeType type);
    
  
  /**
   * Checks if is id used.
   *
   * @param type the type
   * @param id the id
   * @return true, if is id used
   */
  boolean isIdUsed(EditorShapeType type, String id);


}
