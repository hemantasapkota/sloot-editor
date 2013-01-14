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

import com.laex.cg2d.protobuf.GameObject.CGResourceFile;
import com.laex.cg2d.shared.model.ResourceFile;

/**
 * The Class ResourceFileAdapter.
 */
public class ResourceFileAdapter {

  /**
   * As cg resource file.
   * 
   * @param rf
   *          the rf
   * @return the cG resource file
   */
  public static CGResourceFile asCGResourceFile(ResourceFile rf) {
    return CGResourceFile.newBuilder().setResourceFile(rf.getResourceFile())
        .setResourceFileAbsolute(rf.getResourceFileAbsolute()).build();
  }

  /**
   * As resource file.
   * 
   * @param crf
   *          the crf
   * @return the resource file
   */
  public static ResourceFile asResourceFile(CGResourceFile crf) {
    return ResourceFile.create(crf.getResourceFile(), crf.getResourceFileAbsolute());
  }
}
