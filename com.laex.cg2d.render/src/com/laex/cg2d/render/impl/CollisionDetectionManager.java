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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenModel;
import com.laex.cg2d.render.AbstractScreenScaffold;
import com.laex.cg2d.render.IEntityQueryable;

/**
 * The Class CollisionDetectionManager.
 */
public class CollisionDetectionManager extends AbstractScreenScaffold {

  private class EntityContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
      if (contact.isTouching()) { 
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        
        String shapeIdA = queryMgr.getEntityIdByBody(bodyA);
        String shapeIdB = queryMgr.getEntityIdByBody(bodyB);
        
        scriptMgr.collisionCallback(shapeIdA, shapeIdB, contact.getFixtureA().getBody(), contact.getFixtureB().getBody());
      }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
  }

  private LuaScriptManager scriptMgr;
  private IEntityQueryable queryMgr;

  /**
   * Instantiates a new collision detection manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   */
  public CollisionDetectionManager(LuaScriptManager scriptMgr, IEntityQueryable queryMgr, CGScreenModel model, World world, Camera cam) {
    super(model, world, cam);
    this.scriptMgr = scriptMgr;
    this.queryMgr = queryMgr;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IScreenScaffold#create()
   */
  @Override
  public void create() {
    world().setContactListener(new EntityContactListener());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IScreenScaffold#render()
   */
  @Override
  public void render() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IScreenScaffold#dispose()
   */
  @Override
  public void dispose() {
  }

}
