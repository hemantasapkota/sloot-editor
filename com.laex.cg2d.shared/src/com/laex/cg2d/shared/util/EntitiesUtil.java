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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.protobuf.ScreenModel.CGEntity;
import com.laex.cg2d.shared.ICGCProject;
import com.laex.cg2d.shared.ResourceManager;
import com.laex.cg2d.shared.adapter.EntityAdapter;
import com.laex.cg2d.shared.model.Entity;
import com.laex.cg2d.shared.model.EntityAnimation;
import com.laex.cg2d.shared.model.GameModel;
import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.Shape;

/**
 * The Class EntitiesUtil.
 */
public class EntitiesUtil {

  /**
   * Empty vertices.
   * 
   * @return the vector2[]
   */
  public static Vector2[] emptyVertices() {
    Vector2[] varr = new Vector2[4];
    for (int i = 0; i < varr.length; i++) {
      varr[i] = new Vector2();
    }
    return varr;
  }

  /**
   * Checks if is valid.
   * 
   * @param e
   *          the e
   * @return true, if is valid
   */
  public static boolean isValid(Entity e) {
    if (e == null) {
      return false;
    }
    if (e.getAnimationList() == null || e.getAnimationList().size() <= 0) {
      return false;
    }

    Image frame = EntitiesUtil.getDefaultFrame(e);
    if (frame == null) {
      return false;
    }

    return true;
  }

  /**
   * Visit model and init entities.
   * 
   * @param model
   *          the model
   * @return true, if successful
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static boolean visitModelAndInitEntities(GameModel model) throws CoreException, IOException {
    boolean toRemove = false;
    for (Layer layer : model.getDiagram().getLayers()) {
      for (Shape shape : layer.getChildren()) {
        Entity e = null;

        if (!shape.getEditorShapeType().isEntity()) {
          continue;
        }

        e = EntitiesUtil.createEntityModelFromFile(shape.getEntityResourceFile().getResourceFile());
        // Validate entities
        // null entity means te corresponding entity (cge) is not valid or has
        // been deleted
        if (!EntitiesUtil.isValid(e)) {
          toRemove = true;
          continue;
        }

        Image frame = EntitiesUtil.getDefaultFrame(e);
        e.setDefaultFrame(frame);

        shape.setEntity(e);
      }
    }
    return toRemove;
  }

  /**
   * Creates the entity model from file.
   * 
   * @param file
   *          the file
   * @return the entity
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Entity createEntityModelFromFile(String file) throws CoreException, IOException {
    IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(file));
    if (!f.exists())
      return null;
    return createEntityModelFromFile(f);
  }

  /**
   * Creates the entity model from file.
   * 
   * @param file
   *          the file
   * @return the entity
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Entity createEntityModelFromFile(IFile file) throws CoreException, IOException {
    return createEntityModelFromFile(file.getContents());
  }

  /**
   * Creates the entity model from file.
   * 
   * @param contents
   *          the contents
   * @return the entity
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Entity createEntityModelFromFile(InputStream contents) throws IOException {
    if (contents == null) {
      return null;
    }

    CGEntity cgEntityModel = CGEntity.parseFrom(contents);
    Entity entityModel = EntityAdapter.asEntity(cgEntityModel);

    return entityModel;
  }

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
   * Creates the image strip.
   * 
   * @param strip
   *          the strip
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
        Image extractImage = PlatformUtil.createImage(id);
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
    
    int tileWidth = img.getBounds().width / eaim.getCols();
    int tileHeight = img.getBounds().height / eaim.getRows();
    
    ImageData id = extractSprite2(img.getImageData(), new Rectangle(0, 0, tileWidth, tileHeight));
    return PlatformUtil.createImage(id);
  }
}
