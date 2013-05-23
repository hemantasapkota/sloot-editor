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
package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;

/**
 * The Interface BodyVisitor.
 */
public interface BodyVisitor {
  
  /**
   * Visit.
   *
   * @param b the b
   * @param shape the shape
   */
  void visit(Body b, CGShape shape);

}
