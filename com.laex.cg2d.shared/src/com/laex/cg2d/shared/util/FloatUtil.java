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
 * The Class FloatUtil.
 */
public class FloatUtil {

  /**
   * To float.
   *
   * @param value the value
   * @return the float
   */
  public static Float toFloat(Object value) {
    return Float.valueOf(value.toString());
  }

  /**
   * To string.
   *
   * @param value the value
   * @return the string
   */
  public static String toString(Float value) {
    return String.valueOf(value);
  }

}
