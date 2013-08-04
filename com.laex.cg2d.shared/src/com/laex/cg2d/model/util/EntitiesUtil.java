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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.EntityAnimation;
import com.laex.cg2d.model.resources.ResourceManager;

/**
 * The Class EntitiesUtil.
 */
public class EntitiesUtil {

  private static Map<String, Image> ssMap = new HashMap<String, Image>();
  private static boolean shouldCacheLargeSpritesheet = false;

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
        final ImageData id = ResourceManager.extractImageFromBounds(strip.getImageData(), RectAdapter.swtRect(r));
        Image extractImage = ResourceManager.getImage(id);
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
  public static String getInternalName(IPath path) {
    return path.toOSString();
  }

  public static String getDisplayName(IPath path) {
    return path.removeFileExtension().segment(path.segmentCount() - 1);
  }

  public static void startCacheSpritesheets() {
    shouldCacheLargeSpritesheet = true;
  }

  public static void stopCacheSpritesheet() {
    shouldCacheLargeSpritesheet = false;

    for (String key : ssMap.keySet()) {
      ssMap.get(key).dispose();
    }
    ssMap.clear();
  }

  /**
   * Gets the default frame.
   * 
   * @param e
   *          the e
   * @return the default frame
   */
  public static Image getDefaultFrame(Entity e, float scaleFactor) {
    EntityAnimation eaim = EntitiesUtil.getDefaultAnimation(e);
    // if no default exists, return null
    if (eaim == null) {
      return ResourceManager.getImage("missing");
    }
    // if no entity resource file has been defined, return null
    if (eaim.getSpritesheetFile().isEmpty()) {
      return ResourceManager.getImage("missing");
    }

    Image img = null;
    String key = eaim.getSpritesheetFile().getResourceFile();

    if (shouldCacheLargeSpritesheet) {
      img = ssMap.get(key);
    } 

    if (img == null || img.isDisposed()) {
      img = ResourceManager.getImageOfRelativePath(eaim.getSpritesheetFile().getResourceFile());
      ssMap.put(key, img);
    }

    if (eaim.getSpritesheetItems().size() > 0) {

      Rectangle extractBounds = RectAdapter.d2dRect(eaim.getSpritesheetItems().get(0).getExtractBounds());

      ImageData id = ResourceManager.extractImageFromBounds(img.getImageData(), RectAdapter.swtRect(extractBounds));
      Image i = null;
      
      if (scaleFactor == 1) {
        i = ResourceManager.getImage(id);
      } else {
        i = ResourceManager.scaleImage(id, scaleFactor);
      }

      /* Very important to dispose this image */
      if (!shouldCacheLargeSpritesheet) {
        img.dispose();
      }

      return i;
    }

    return null;
  }
}
