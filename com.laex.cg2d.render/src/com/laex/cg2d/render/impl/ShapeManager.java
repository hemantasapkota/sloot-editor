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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.GameObject.CGEditorShapeType;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;
import com.laex.cg2d.protobuf.GameObject.CGLayer;
import com.laex.cg2d.protobuf.GameObject.CGShape;
import com.laex.cg2d.render.AbstractScreenScaffold;

/**
 * The Class ShapeManager.
 */
public class ShapeManager extends AbstractScreenScaffold {

  /** The debug draw. */
  private Box2DDebugRenderer debugDraw;

  /** The draw debug data. */
  private boolean drawDebugData;

  /** The draw aabb. */
  private boolean drawAABB;

  /** The draw bodies. */
  private boolean drawBodies;

  /** The draw inactive bodies. */
  private boolean drawInactiveBodies;

  /** The draw joints. */
  private boolean drawJoints;

  /**
   * Instantiates a new shape manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   */
  public ShapeManager(CGGameModel model, World world, Camera cam) {
    super(model, world, cam);

    this.drawAABB = model.getScreenPrefs().getDebugDrawPrefs().getDrawAABB();
    this.drawBodies = model.getScreenPrefs().getDebugDrawPrefs().getDrawBodies();
    this.drawInactiveBodies = model.getScreenPrefs().getDebugDrawPrefs().getDrawInactiveBodies();
    this.drawJoints = model.getScreenPrefs().getDebugDrawPrefs().getDrawJoints();
    this.drawDebugData = model.getScreenPrefs().getDebugDrawPrefs().getDrawDebugData();
  }

  /**
   * Inits the box2d.
   */
  private void initBox2d() {
    debugDraw = new Box2DDebugRenderer();

    debugDraw.setDrawAABBs(drawAABB);
    debugDraw.setDrawBodies(drawBodies);
    debugDraw.setDrawInactiveBodies(drawInactiveBodies);
    debugDraw.setDrawJoints(drawJoints);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#create()
   */
  @Override
  public void create() {
    initBox2d();
    createShapes();
  }

  /**
   * Creates the shapes.
   */
  private void createShapes() {
    for (CGLayer layer : model().getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {

        CGEditorShapeType eType = shape.getEditorShapeType();

        switch (eType) {
        case SIMPLE_SHAPE_BOX:
        case SIMPLE_SHAPE_CIRCLE:
        case SIMPLE_SHAPE_HEDGE:
        case SIMPLE_SHAPE_VEDGE:
          Body b = createBody(shape, null);
          b.setUserData(shape);
          break;
        case BACKGROUND_SHAPE:
          break;
        case ENTITY_SHAPE:
          break;
        default:
          break;
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#render()
   */
  @Override
  public void render() {
    if (drawDebugData) {
      debugDraw.render(world(), camera().combined.scl(scaleFactor()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
  }
}
