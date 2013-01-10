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

/**
 * The Class IntegerUtil.
 */
public class IntegerUtil {

  /**
   * To int.
   *
   * @param value the value
   * @return the integer
   */
  public static Integer toInt(Object value) {
    return Integer.parseInt(value.toString());
  }

  /**
   * To short.
   *
   * @param value the value
   * @return the short
   */
  public static short toShort(Object value) {
    return Short.parseShort(value.toString());
  }

  /**
   * To string.
   *
   * @param value the value
   * @return the string
   */
  public static String toString(Integer value) {
    return value.toString();
  }

}
