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

import org.apache.commons.lang.StringUtils;

/**
 * The Class ResourceFile.
 */
public class ResourceFile {

  /** The resource file. */
  private String resourceFile;

  /** The resource file absolute. */
  private String resourceFileAbsolute;

  /**
   * Creates the.
   * 
   * @param resFile
   *          the res file
   * @param resFileAbs
   *          the res file abs
   * @return the resource file
   */
  public static ResourceFile create(String resFile, String resFileAbs) {
    ResourceFile rf = new ResourceFile();
    rf.setResourceFile(resFile);
    rf.setResourceFileAbsolute(resFileAbs);
    return rf;
  }

  /**
   * Empty resource file.
   * 
   * @return the resource file
   */
  public static ResourceFile emptyResourceFile() {
    return ResourceFile.create(StringUtils.EMPTY, StringUtils.EMPTY);
  }

  /**
   * Instantiates a new resource file.
   */
  private ResourceFile() {
    this.resourceFile = StringUtils.EMPTY;
    this.resourceFileAbsolute = StringUtils.EMPTY;
  }

  /**
   * Checks if is empty.
   * 
   * @return true, if is empty
   */
  public boolean isEmpty() {
    if (StringUtils.isEmpty(resourceFile) && StringUtils.isEmpty(resourceFileAbsolute)) {
      return true;
    }
    return false;
  }

  /**
   * Gets the resource file.
   * 
   * @return the resource file
   */
  public String getResourceFile() {
    return resourceFile;
  }

  /**
   * Gets the resource file absolute.
   * 
   * @return the resource file absolute
   */
  public String getResourceFileAbsolute() {
    return resourceFileAbsolute;
  }

  /**
   * Sets the resource file.
   * 
   * @param resourceFile
   *          the new resource file
   */
  public void setResourceFile(String resourceFile) {
    this.resourceFile = resourceFile;
  }

  /**
   * Sets the resource file absolute.
   * 
   * @param resourceFileAbsolute
   *          the new resource file absolute
   */
  public void setResourceFileAbsolute(String resourceFileAbsolute) {
    this.resourceFileAbsolute = resourceFileAbsolute;
  }

}
