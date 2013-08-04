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
package com.laex.cg2d.screeneditor.editparts.figure;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.resources.ResourceManager;
import com.laex.cg2d.screeneditor.Activator;

/**
 * The Class JointAnchorFigure.
 */
public class JointAnchorFigure extends ImageFigure {

  /** The Constant ANCHOR_IMG. */
  private static final Image ANCHOR_IMG = ResourceManager.getPluginImage(Activator.PLUGIN_ID,
      "icons/plus-small-white.png");

  /**
   * Instantiates a new joint anchor figure.
   */
  public JointAnchorFigure() {
    super(ANCHOR_IMG);
  }

}
