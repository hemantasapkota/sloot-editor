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
package com.laex.cg2d.entityeditor.ui;

import java.util.List;
import java.util.Queue;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.ResourceFile;

/**
 * The Interface ImportSpriteCompositeDelegate.
 */
public interface ImportSpriteCompositeDelegate {

  /**
   * Sprite extraction complete.
   *
   * @param spritesheetFile the spritesheet file
   * @param spritesheetJsonFile the spritesheet json file
   * @param spritesheetItems the spritesheet items
   * @param extractedImages the extracted images
   */
  void spriteExtractionComplete(ResourceFile spritesheetFile, ResourceFile spritesheetJsonFile,
      List<EntitySpritesheetItem> spritesheetItems, Queue<Image> extractedImages);

}
