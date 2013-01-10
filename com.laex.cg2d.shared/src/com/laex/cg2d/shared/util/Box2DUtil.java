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
package com.laex.cg2d.shared.util;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * The Class Box2DUtil.
 */
public class Box2DUtil {

  /**
   * Gets the box2 d types.
   *
   * @return the box2 d types
   */
  public static String[] getBox2DTypes() {
    return new String[]
      { BodyType.StaticBody.toString(), BodyType.KinematicBody.toString(), BodyType.DynamicBody.toString() };
  }

}
