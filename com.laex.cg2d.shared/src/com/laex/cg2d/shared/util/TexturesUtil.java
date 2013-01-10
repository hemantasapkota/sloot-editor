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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

/**
 * The Class TexturesUtil.
 */
public class TexturesUtil {

  /**
   * To absolute path.
   *
   * @param relativeFile the relative file
   * @return the string
   */
  public static String toAbsolutePath(String relativeFile) {
    if (StringUtils.isEmpty(relativeFile)) {
      return null;
    }

    IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(relativeFile));
    String path = file.getLocation().toOSString();
    return path;
  }

}
