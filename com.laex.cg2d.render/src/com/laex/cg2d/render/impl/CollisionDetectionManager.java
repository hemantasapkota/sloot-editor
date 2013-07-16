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

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.laex.cg2d.render.ScreenScaffold;

/**
 * The Class CollisionDetectionManager.
 */
public class CollisionDetectionManager implements ScreenScaffold {

  /**
   * The listener interface for receiving entityContact events. The class that
   * is interested in processing a entityContact event implements this
   * interface, and the object created with that class is registered with a
   * component using the component's
   * <code>addEntityContactListener<code> method. When
   * the entityContact event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see EntityContactEvent
   */
  private class EntityContactListener implements ContactListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic
     * .gdx.physics.box2d.Contact)
     */
    @Override
    public void beginContact(Contact contact) {
      if (contact.isTouching()) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        String shapeIdA = manipulator.getEntityId(bodyA);
        String shapeIdB = manipulator.getEntityId(bodyB);

        scriptMgr.collisionCallback(shapeIdA, shapeIdB, contact.getFixtureA().getBody(), contact.getFixtureB()
            .getBody(), contact.getFixtureA(), contact.getFixtureB());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic
     * .gdx.physics.box2d.Contact)
     */
    @Override
    public void endContact(Contact contact) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.
     * gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic
     * .gdx.physics.box2d.Contact,
     * com.badlogic.gdx.physics.box2d.ContactImpulse)
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
  }

  /** The script mgr. */
  private LuaScriptManager scriptMgr;

  /** The manipulator. */
  private ScreenManagerImpl manipulator;

  /**
   * Instantiates a new collision detection manager.
   * 
   * @param manipulator
   *          the manipulator
   * @param scriptMgr
   *          the script mgr
   */
  public CollisionDetectionManager(ScreenManagerImpl manipulator, LuaScriptManager scriptMgr) {
    this.manipulator = manipulator;
    this.scriptMgr = scriptMgr;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IScreenScaffold#create()
   */
  @Override
  public void create() {
    manipulator.world().setContactListener(new EntityContactListener());
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
