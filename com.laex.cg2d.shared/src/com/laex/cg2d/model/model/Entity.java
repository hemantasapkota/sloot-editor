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
package com.laex.cg2d.model.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.adapter.EntityAdapter;

/**
 * The Class Entity.
 * 
 * @author hemantasapkota
 */
public class Entity {

  /** The animation list. */
  private List<EntityAnimation> animationList;

  /** The default frame. */
  private Image defaultFrame;

  // Factory methods
  /**
   * Creates the from file.
   * 
   * @param filename
   *          the filename
   * @return the entity
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Entity createFromFile(String filename) throws CoreException, IOException {
    IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filename));
    if (!f.exists())
      return null;
    return Entity.createFromFile(f);
  }

  /**
   * Creates the from file.
   * 
   * @param res
   *          the res
   * @return the entity
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Entity createFromFile(IFile res) throws CoreException, IOException {
    if (res == null || res.getContents() == null) {
      return null;
    }

    CGEntity cgEntityModel = CGEntity.parseFrom(res.getContents());
    
    Entity entityModel = EntityAdapter.asEntity(cgEntityModel);
    
    cgEntityModel = null;

    return entityModel;
  }

  /**
   * Instantiates a new entity.
   */
  public Entity() {
    animationList = new ArrayList<EntityAnimation>();
  }


  /**
   * Adds the entity animation.
   * 
   * @param anim
   *          the anim
   */
  public void addEntityAnimation(EntityAnimation anim) {
    animationList.add(anim);
  }

  /**
   * Gets the default frame.
   * 
   * @return the default frame
   */
  public Image getDefaultFrame() {
    return defaultFrame;
  }

  /**
   * Sets the default frame.
   * 
   * @param defaultFrame
   *          the new default frame
   */
  public void setDefaultFrame(Image defaultFrame) {
    this.defaultFrame = defaultFrame;
  }

  /**
   * Gets the animation list.
   * 
   * @return the animation list
   */
  public List<EntityAnimation> getAnimationList() {
    return animationList;
  }

  public void dispose() {
    if (defaultFrame != null) {
      defaultFrame.dispose();
    }
  }

}
