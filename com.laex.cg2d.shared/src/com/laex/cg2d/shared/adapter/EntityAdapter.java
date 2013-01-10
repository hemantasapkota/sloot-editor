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
package com.laex.cg2d.shared.adapter;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.protobuf.GameObject.CGEntity;
import com.laex.cg2d.protobuf.GameObject.CGEntityAnimation;
import com.laex.cg2d.protobuf.GameObject.CGEntityCollisionType;
import com.laex.cg2d.protobuf.GameObject.CGVector2;
import com.laex.cg2d.shared.model.Entity;
import com.laex.cg2d.shared.model.EntityAnimation;
import com.laex.cg2d.shared.model.EntityCollisionType;
import com.laex.cg2d.shared.util.EntitiesUtil;

/*
 * Bi-directional adapter between Entity & CGEntity
 */
/**
 * The Class EntityAdapter.
 */
public class EntityAdapter {
  
  /**
   * To cg entity collision type.
   *
   * @param et the et
   * @return the cG entity collision type
   */
  public static CGEntityCollisionType toCGEntityCollisionType(EntityCollisionType et) {
    switch (et) {
    case BOX:
      return CGEntityCollisionType.BOX;
    case CIRCLE:
      return CGEntityCollisionType.CIRCLE;
    case CUSTOM:
      return CGEntityCollisionType.CUSTOM;
    case NONE:
      return CGEntityCollisionType.NONE;
    }
    return CGEntityCollisionType.NONE;
  }

  /**
   * To entity collision type.
   *
   * @param cgt the cgt
   * @return the entity collision type
   */
  public static EntityCollisionType toEntityCollisionType(CGEntityCollisionType cgt) {
    switch (cgt) {
    case BOX:
      return EntityCollisionType.BOX;
    case CIRCLE:
      return EntityCollisionType.CIRCLE;
    case CUSTOM:
      return EntityCollisionType.CUSTOM;
    case NONE:
      return EntityCollisionType.NONE;
    default:
      break;
    }
    return EntityCollisionType.NONE;
  }

  /**
   * New default entity.
   *
   * @param filename the filename
   * @return the entity
   */
  public static Entity newDefaultEntity(String filename) {
    Entity entityModel = new Entity();
    entityModel.setInternalName(EntitiesUtil.getInternalName(filename));

    EntityAnimation ea = new EntityAnimation();
    ea.setAnimationName("Animation 1");
    ea.setDefaultAnimation(true);
    ea.setVertices(new ArrayList<Vector2>());
    ea.setShapeType(EntityCollisionType.NONE);

    entityModel.addEntityAnimation(ea);

    return entityModel;
  }

  /**
   * As cg entity.
   *
   * @param e the e
   * @return the cG entity
   */
  public static CGEntity asCGEntity(Entity e) {
    CGEntity.Builder entityBuilder = CGEntity.newBuilder().setInternalName(e.getInternalName());

    for (EntityAnimation ea : e.getAnimationList()) {

      CGEntityAnimation.Builder eaBuilder = CGEntityAnimation.newBuilder()
          .setAnimationDuration(ea.getAnimationDuration()).setAnimationName(ea.getAnimationName())
          .setCollisionType(toCGEntityCollisionType(ea.getShapeType())).setCols(ea.getCols())
          .setDefaultAnimation(ea.isDefaultAnimation())
          .setFixtureFile(ResourceFileAdapter.asCGResourceFile(ea.getFixtureResourceFile()))
          .setAnimationResourceFile(ResourceFileAdapter.asCGResourceFile(ea.getAnimationResourceFile()))
          .setRows(ea.getRows()).setShpHeight(ea.getShpHeight()).setShpWidth(ea.getShpWidth()).setShpX(ea.getShpX())
          .setShpY(ea.getShpY());

      for (Vector2 v : ea.getVertices()) {
        eaBuilder = eaBuilder.addVertices(Vector2Adapter.asCGVector2(v));
      }

      CGEntityAnimation cgEa = eaBuilder.build();
      entityBuilder = entityBuilder.addAnimations(cgEa);
    }
    return entityBuilder.build();
  }

  /**
   * As entity.
   *
   * @param cge the cge
   * @return the entity
   */
  public static Entity asEntity(CGEntity cge) {
    Entity entityModel = new Entity();
    entityModel.setInternalName(cge.getInternalName());

    for (CGEntityAnimation cgEa : cge.getAnimationsList()) {
      EntityAnimation ea = new EntityAnimation();
      ea.setAnimationDelay(cgEa.getAnimationDuration());
      ea.setAnimationName(cgEa.getAnimationName());
      ea.setCols(cgEa.getCols());
      ea.setDefaultAnimation(cgEa.getDefaultAnimation());
      ea.setFixtureResourceFile(ResourceFileAdapter.asResourceFile(cgEa.getFixtureFile()));
      ea.setAnimationResourceFile(ResourceFileAdapter.asResourceFile(cgEa.getAnimationResourceFile()));
      ea.setRows(cgEa.getRows());
      ea.setShapeType(toEntityCollisionType(cgEa.getCollisionType()));
      ea.setShpX(cgEa.getShpX());
      ea.setShpY(cgEa.getShpY());
      ea.setShpWidth(cgEa.getShpWidth());
      ea.setShpHeight(cgEa.getShpHeight());
      for (CGVector2 cv : cgEa.getVerticesList()) {
        ea.getVertices().add(new Vector2(cv.getX(), cv.getY()));
      }
      entityModel.addEntityAnimation(ea);
    }

    return entityModel;
  }

}
