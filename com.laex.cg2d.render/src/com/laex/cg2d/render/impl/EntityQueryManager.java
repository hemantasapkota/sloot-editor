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

import java.util.Iterator;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.EntityQueryable;

/**
 * The Class EntityQueryManager.
 */
public class EntityQueryManager implements EntityQueryable {

  /** The world. */
  private World world;

  /**
   * Instantiates a new entity query manager.
   *
   * @param world the world
   */
  public EntityQueryManager(World world) {
    this.world = world;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.render.EntityQueryable#getEntityById(java.lang.String)
   */
  @Override
  public Body getEntityById(String id) {
    Iterator<Body> body = world.getBodies();

    while (body.hasNext()) {
      Body b = body.next();

      CGShape shape = (CGShape) b.getUserData();

      if (shape.getId().equals(id)) {
        return b;
      }
    }

    return null;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.render.EntityQueryable#getEntityIdByBody(com.badlogic.gdx.physics.box2d.Body)
   */
  @Override
  public String getEntityIdByBody(Body b) {
    return ((CGShape) b.getUserData()).getId();
  }

}
