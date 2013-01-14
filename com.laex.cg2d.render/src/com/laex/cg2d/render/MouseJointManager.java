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
package com.laex.cg2d.render;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;

/**
 * MouseJointManager. Most of the code is from the LibGDX Box2d Tests. One
 * important thing to note is that, our camera uses a scale factor. Whenever
 * projecting and unprojecting hit point from camera, a scale factor should be
 * applied. Since we multiply the camera by a scaleFactor, for unprojecting, we
 * multiply the vector points by the inverse: 1/scaleFactor
 * 
 * @author hemantasapkota
 * 
 */
public class MouseJointManager extends AbstractGameComponentManager implements InputProcessor {

  /** The mouse joint installed. */
  private boolean mouseJointInstalled;

  /** The ground body. */
  private Body groundBody;

  /** The mouse joint. */
  private MouseJoint mouseJoint;

  /** The hit body. */
  private Body hitBody;

  /** The test point. */
  Vector3 testPoint = new Vector3();

  /** The target. */
  Vector2 target = new Vector2();

  /** The callback. */
  QueryCallback callback = new QueryCallback() {
    @Override
    public boolean reportFixture(Fixture fixture) {
      // if the hit point is inside the fixture of the body
      // we report it
      if (fixture.testPoint(testPoint.x, testPoint.y)) {
        hitBody = fixture.getBody();
        return false;
      } else
        return true;
    }
  };

  /**
   * Instantiates a new mouse joint manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   */
  public MouseJointManager(CGGameModel model, World world, Camera cam) {
    super(model, world, cam);

    this.mouseJointInstalled = model.getScreenPrefs().getDebugDrawPrefs().getInstallMouseJoint();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#create()
   */
  @Override
  public void create() {
    BodyDef bodyDef = new BodyDef();
    groundBody = world().createBody(bodyDef);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#render()
   */
  @Override
  public void render() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
    hitBody = null;
    mouseJoint = null;
    world().destroyBody(groundBody);
    groundBody = null;
    callback = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
   */
  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
   */
  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    if (!mouseJointInstalled) {
      return false;
    }

    // translate the mouse coordinates to world coordinates
    camera().unproject(testPoint.set(x, y, 0));
    testPoint = testPoint.mul(1 / scaleFactor());
    // ask the world which bodies are within the given
    // bounding box around the mouse pointer
    hitBody = null;
    world().QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f,
        testPoint.y + 0.0001f);

    if (hitBody == groundBody) {
      hitBody = null;
    }

    // ignore kinematic bodies, they don't work with the mouse joint
    if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) {
      return false;
    }

    // if we hit something we create a new mouse joint
    // and attach it to the hit body.
    if (hitBody != null) {
      MouseJointDef def = new MouseJointDef();
      def.bodyA = groundBody;
      def.bodyB = hitBody;
      def.collideConnected = true;
      def.target.set(testPoint.x, testPoint.y);
      def.maxForce = 1000.0f * hitBody.getMass();

      mouseJoint = (MouseJoint) world().createJoint(def);
      hitBody.setAwake(true);
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
   */
  @Override
  public boolean touchUp(int x, int y, int pointer, int button) {
    if (!mouseJointInstalled) {
      return false;
    }

    // if a mouse joint exists we simply destroy it
    if (mouseJoint != null) {
      world().destroyJoint(mouseJoint);
      mouseJoint = null;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
   */
  @Override
  public boolean touchDragged(int x, int y, int pointer) {
    if (!mouseJointInstalled) {
      return false;
    }

    // if a mouse joint exists we simply update
    // the target of the joint based on the new
    // mouse coordinates
    if (mouseJoint != null) {
      camera().unproject(testPoint.set(x, y, 0));
      testPoint.mul(1 / scaleFactor());
      mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#touchMoved(int, int)
   */
  @Override
  public boolean touchMoved(int x, int y) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.InputProcessor#scrolled(int)
   */
  @Override
  public boolean scrolled(int amount) {
    return false;
  }

}
