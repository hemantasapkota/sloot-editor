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
package com.laex.cg2d.model.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ResourceManager;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.EntityAnimation;

/**
 * The Class EntitiesUtil.
 */
public class EntitiesUtil {

  /**
   * Extract sprite2.
   * 
   * @param baseImageData
   *          the base image data
   * @param entityBounds
   *          the entity bounds
   * @return the image data
   */
  private static ImageData extractSprite2(ImageData baseImageData, Rectangle entityBounds) {
    final ImageData id = new ImageData(entityBounds.width, entityBounds.height, baseImageData.depth,
        baseImageData.palette);

    int ey = entityBounds.y;
    int ex = entityBounds.x;

    for (int i = 0; i < entityBounds.height; i++) {
      int py = ey + i;
      for (int j = 0; j < entityBounds.width; j++) {
        int px = ex + j;
        id.setPixel(j, i, baseImageData.getPixel(px, py));
        id.setAlpha(j, i, baseImageData.getAlpha(px, py));
      }
    }
    return id;
  }

  /**
   * Creates the image.
   * 
   * @param id
   *          the id
   * @return the image
   */
  public static Image createImage(final ImageData id) {
    ImageDescriptor idd = new ImageDescriptor() {
      @Override
      public ImageData getImageData() {
        return id;
      }
    };
    return idd.createImage();
  }

  /**
   * Creates the image strip.
   * 
   * @param strip
   *          the strip
   * @param cols
   *          the cols
   * @param rows
   *          the rows
   * @return the queue
   */
  public static Queue<Image> createImageStrip(Image strip, int cols, int rows) {
    int imgWidth = strip.getBounds().width;
    int imgHeight = strip.getBounds().height;

    int tileWidth = imgWidth / cols;
    int tileHeight = imgHeight / rows;

    Queue<Image> imgStip = new LinkedList<Image>();

    for (int y = 0; y < imgHeight; y += tileHeight) {
      for (int x = 0; x < imgWidth; x += tileWidth) {
        Rectangle r = new Rectangle(x, y, tileWidth, tileHeight);
        final ImageData id = extractSprite2(strip.getImageData(), r);
        Image extractImage = EntitiesUtil.createImage(id);
        imgStip.add(extractImage);
      }
    }

    return imgStip;
  }

  /**
   * Gets the default animation.
   * 
   * @param e
   *          the e
   * @return the default animation
   */
  public static EntityAnimation getDefaultAnimation(Entity e) {
    for (EntityAnimation ea : e.getAnimationList()) {
      if (ea.isDefaultAnimation()) {
        return ea;
      }
    }
    return null;
  }

  /**
   * Gets the internal name.
   * 
   * @param resourceName
   *          the resource name
   * @return the internal name
   */
  public static String getInternalName(String resourceName) {
    return resourceName.replaceFirst("\\." + ICGCProject.ENTITIES_EXTENSION, "").trim();
  }

  /**
   * Gets the default frame.
   * 
   * @param e
   *          the e
   * @return the default frame
   */
  public static Image getDefaultFrame(Entity e) {
    EntityAnimation eaim = EntitiesUtil.getDefaultAnimation(e);
    // if no default exists, return null
    if (eaim == null) {
      return null;
    }
    // if no entity resource file has been defined, return null
    if (eaim.getAnimationResourceFile().isEmpty()) {
      return null;
    }

    Image img = ResourceManager.getImageOfRelativePath(eaim.getAnimationResourceFile().getResourceFile());

    Queue<Image> strip = EntitiesUtil.createImageStrip(img, eaim.getCols(), eaim.getRows());

    int frameIndexToCheck = 1;
    List<Integer> frameIndices = eaim.getFrameIndices();

    for (Image i : strip) {
      boolean c = frameIndices.contains(frameIndexToCheck);
      if (c) {
        return i;
      }
      frameIndexToCheck++;
    }

    return null;
  }

  public static Queue<Image> extract(Image spriteSheet, int cols, int rows, List<Integer> frameIndices) {
    Queue<Image> extracted = EntitiesUtil.createImageStrip(spriteSheet, cols, rows);

    Queue<Image> includeList = new LinkedList<Image>();

    Object[] explodeExtraced = extracted.toArray();

    int indexToCheck = 1;

    while (indexToCheck <= frameIndices.size()) {

      boolean shouldInclude = frameIndices.contains(indexToCheck);

      if (shouldInclude) {

        includeList.add((Image) explodeExtraced[indexToCheck]);

      }

      indexToCheck++;
    }

    return includeList;

  }
}
