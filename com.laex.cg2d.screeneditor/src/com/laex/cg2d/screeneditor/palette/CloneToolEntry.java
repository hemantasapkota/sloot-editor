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
package com.laex.cg2d.screeneditor.palette;

import org.eclipse.gef.palette.SelectionToolEntry;

import com.laex.cg2d.model.SharedImages;

/***
 * Class has to be public.
 * 
 * @author hemantasapkota
 * 
 */
public class CloneToolEntry extends SelectionToolEntry {

  /**
   * Instantiates a new clone tool entry.
   */
  public CloneToolEntry() {
    setLabel("Clone Tool");
    setToolClass(CloneTool.class);
    setSmallIcon(SharedImages.CLONE_TOOL);
  }

}
