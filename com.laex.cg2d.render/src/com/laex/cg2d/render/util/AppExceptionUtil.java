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

import com.laex.cg2d.render.MyGdxGameDesktop;

/**
 * The Class AppExceptionUtil.
 */
public final class AppExceptionUtil {

  /**
   * Handle.
   * 
   * @param t
   *          the t
   */
  public static void handle(Throwable t) {
    MyGdxGameDesktop.lwjglApp().error("Error Details", t.getMessage());
    MyGdxGameDesktop.lwjglApp().exit();
    System.exit(0); // terrible hack. This is needed because the above call to
                    // lwjglApp().exit() does not work.
  }

}
