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
package com.laex.cg2d.shared.adapter;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.protobuf.GameObject.CGVector2;

/**
 * The Class Vector2Adapter.
 */
public class Vector2Adapter {

  /**
   * As cg vector2.
   *
   * @param v the v
   * @return the cG vector2
   */
  public static CGVector2 asCGVector2(Vector2 v) {
    return CGVector2.newBuilder().setX(v.x).setY(v.y).build();
  }

  /**
   * As vector2.
   *
   * @param v the v
   * @return the vector2
   */
  public static Vector2 asVector2(CGVector2 v) {
    return new Vector2(v.getX(), v.getY());
  }

}
