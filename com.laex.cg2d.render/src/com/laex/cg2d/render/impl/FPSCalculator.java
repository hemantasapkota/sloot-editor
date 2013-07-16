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
package com.laex.cg2d.render.impl;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import com.laex.cg2d.render.ScreenScaffold;

/**
 * The Class FPSCalculator.
 */
public class FPSCalculator implements ScreenScaffold {

  /** The last fps. */
  private long lastFps = 0;

  /** The fps. */
  private int fps = 0;

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#create()
   */
  @Override
  public void create() {
    lastFps = getTime();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#render()
   */
  @Override
  public void render() {
    updateFPS();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.ScreenScaffold#dispose()
   */
  @Override
  public void dispose() {
  }

  /**
   * Gets the time.
   * 
   * @return the time
   */
  public long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  /**
   * Update fps.
   */
  public void updateFPS() {
    if (getTime() - lastFps > 1000) {
      Display.setTitle("FPS: " + fps);
      fps = 0;
      lastFps += 1000;
    }
    fps++;
  }

}
