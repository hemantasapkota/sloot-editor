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
package com.laex.cg2d.model;

import java.util.Map;

/**
 * The Class XStreamProperties.
 */
public class XStreamProperties {

  /** The class loader. */
  private ClassLoader classLoader;

  /** The class aliases. */
  private Map<String, Class> classAliases;

  /**
   * Instantiates a new x stream properties.
   * 
   * @param clz
   *          the clz
   * @param aliases
   *          the aliases
   */
  public XStreamProperties(ClassLoader clz, Map<String, Class> aliases) {
    this.classLoader = clz;
    this.classAliases = aliases;
  }

  /**
   * Gets the class loader.
   * 
   * @return the class loader
   */
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  /**
   * Gets the class aliases.
   * 
   * @return the class aliases
   */
  public Map<String, Class> getClassAliases() {
    return classAliases;
  }

}
