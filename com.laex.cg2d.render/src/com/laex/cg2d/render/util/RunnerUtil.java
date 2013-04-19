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
package com.laex.cg2d.render.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.laex.cg2d.protobuf.GameObject.CGEntity;
import com.laex.cg2d.protobuf.GameObject.CGEntityAnimation;

/**
 * The Class RunnerUtil.
 * 
 * @author hemantasapkota
 */
public class RunnerUtil {

  /**
   * identical to EntitiesUtil.getDefaultAnimtion()
   * 
   * @param e
   *          the e
   * @return the default animation
   */
  public static CGEntityAnimation getDefaultAnimation(CGEntity e) {
    for (CGEntityAnimation ea : e.getAnimationsList()) {
      if (ea.getDefaultAnimation()) {
        return ea;
      }
    }
    return null;
  }

  /**
   * To int.
   * 
   * @param value
   *          the value
   * @return the integer
   */
  public static Integer toInt(Object value) {
    return Integer.parseInt(value.toString());
  }

  /**
   * To float.
   * 
   * @param value
   *          the value
   * @return the float
   */
  public static Float toFloat(Object value) {
    return Float.valueOf(value.toString());
  }

  /**
   * identical to EntitiesUtil.createEntityModelFromFile()
   * 
   * @param entityFileName
   *          the entity file name
   * @return the cG entity
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static CGEntity createEntityModelFromFile(String entityFileName) throws IOException {
    return CGEntity.parseFrom(Gdx.files.absolute(entityFileName).read());

  }

}
