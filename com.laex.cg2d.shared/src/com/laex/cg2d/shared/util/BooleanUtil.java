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

import org.apache.commons.lang.StringUtils;

/**
 * The Class BooleanUtil.
 */
public class BooleanUtil {

  /** The Constant BOOLEAN_STRING_VALUES. */
  public static final String[] BOOLEAN_STRING_VALUES = new String[]
    { "TRUE", "FALSE" };

  /**
   * To bool.
   *
   * @param value the value
   * @return the boolean
   */
  public static Boolean toBool(Object value) {
    String val = value.toString();

    if (StringUtils.isNumeric(val)) {
      return parseInteger(value);
    }

    return Boolean.valueOf(value.toString());
  }

  /**
   * Parses the integer.
   *
   * @param value the value
   * @return the boolean
   */
  private static Boolean parseInteger(Object value) {
    int v = IntegerUtil.toInt(value);
    if (v == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the integer from boolean.
   *
   * @param value the value
   * @return the integer from boolean
   */
  public static Integer getIntegerFromBoolean(Boolean value) {
    if (value) {
      return 0;
    } else {
      return 1;
    }
  }

  /**
   * To string.
   *
   * @param value the value
   * @return the string
   */
  public static String toString(Boolean value) {
    return value.toString();
  }

}
