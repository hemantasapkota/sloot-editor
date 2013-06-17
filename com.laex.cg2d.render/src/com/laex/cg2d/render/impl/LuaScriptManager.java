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

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.AbstractScreenScaffold;
import com.laex.cg2d.render.BodyVisitor;
import com.laex.cg2d.render.EntityQueryable;
import com.laex.cg2d.render.MyGdxGameDesktop;
import com.laex.cg2d.render.ScreenControllerScript;

/**
 * The Class LuaScriptManager.
 */
public class LuaScriptManager extends AbstractScreenScaffold implements ScreenControllerScript {

  /** The globals. */
  private Globals globals = JsePlatform.standardGlobals();

  /** The chunk. */
  private LuaValue chunk;

  /** The script file exists. */
  private boolean scriptFileExists = false;

  /** The query mgr. */
  private EntityQueryable queryMgr;

  /**
   * Instantiates a new lua script manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   */
  public LuaScriptManager(CGScreenModel model, World world, Camera cam) {
    super(model, world, cam);
  }

  /**
   * Instantiates a new lua script manager.
   * 
   * @param model
   *          the model
   * @param queryMgr
   *          the query mgr
   * @param world
   *          the world
   * @param cam
   *          the cam
   * @param scriptFileName
   *          the script file name
   */
  public LuaScriptManager(CGScreenModel model, EntityQueryable queryMgr, World world, Camera cam, String scriptFileName) {
    super(model, world, cam);

    this.queryMgr = queryMgr;

    if (scriptFileName == null || !Gdx.files.absolute(scriptFileName).exists()) {
      scriptFileExists = false;
      return;
    } else {
      scriptFileExists = true;
    }

    try {

      chunk = globals.loadFile(scriptFileName);

      // very important step. subsequent calls to method do not work if the
      // chunk
      // is not called here
      chunk.call();

    } catch (Throwable t) {
      
      handleException(t);

    }

  }


  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#create()
   */
  @Override
  public void create() {
    if (!canExecute())
      return;

    executeInit(model(), queryMgr);

    super.acceptBodyVisitor(new BodyVisitor() {

      @Override
      public void visit(Body b, CGShape shape) {

        executeInitBody(b, shape.getId());

      }
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#render()
   */
  public void render() {
    if (!canExecute())
      return;

    super.acceptBodyVisitor(new BodyVisitor() {

      @Override
      public void visit(Body b, CGShape shape) {
        executeUpdate(b, shape.getId());
        
      }
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameScript#executeInit()
   */
  @Override
  public void executeInit(CGScreenModel screenModel, EntityQueryable entityMgr) {
    if (!canExecute()) {
      return;
    }

    try {

      globals.get("init").invoke(
          new LuaValue[]
            {
                CoerceJavaToLua.coerce(screenModel),
                CoerceJavaToLua.coerce(entityMgr),
                CoerceJavaToLua.coerce(world()),
                CoerceJavaToLua.coerce(camera()) });

    } catch (Throwable t) {

      handleException(t);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.IGameScript#executeInitBody(com.badlogic.gdx.physics
   * .box2d.Body, java.lang.String)
   */
  @Override
  public void executeInitBody(Body body, String bodyId) {
    if (!canExecute()) {
      return;
    }

    LuaValue bodyLua = CoerceJavaToLua.coerce(body);

    try {

      globals.get("initBody").invoke(new LuaValue[]
        { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), bodyLua, LuaValue.valueOf(bodyId) });

    } catch (Throwable t) {
      handleException(t);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.IGameScript#executeUpdate(com.badlogic.gdx.physics
   * .box2d.Body, java.lang.String)
   */
  @Override
  public void executeUpdate(Body body, String bodyId) {
    if (!canExecute()) {
      return;
    }

    LuaValue bodyLua = CoerceJavaToLua.coerce(body);

    try {

      globals.get("update").invoke(new LuaValue[]
        { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), bodyLua, LuaValue.valueOf(bodyId) });

    } catch (Throwable t) {

      handleException(t);

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameScript#executeKeyPressed(java.lang.String)
   */
  @Override
  public void executeKeyPressed(String key) {
    if (!canExecute()) {
      return;
    }

    try {

      globals.get("keyPressed").invoke(new LuaValue[]
        { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), LuaValue.valueOf(key) });

    } catch (Throwable t) {
      
      handleException(t);

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameScript#canExecute()
   */
  @Override
  public boolean canExecute() {
    return scriptFileExists;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.render.ScreenControllerScript#collisionCallback(java.lang
   * .String, java.lang.String, com.badlogic.gdx.physics.box2d.Body,
   * com.badlogic.gdx.physics.box2d.Body)
   */
  @Override
  public void collisionCallback(String idA, String idB, Body bodyA, Body bodyB) {
    if (!canExecute()) {
      return;
    }

    try {

      globals.get("collisionCallback").invoke(
          new LuaValue[]
            {
                LuaValue.valueOf(idA),
                LuaValue.valueOf(idB),
                CoerceJavaToLua.coerce(bodyA),
                CoerceJavaToLua.coerce(bodyB),
                CoerceJavaToLua.coerce(world()),
                CoerceJavaToLua.coerce(camera()) });

    } catch (Throwable t) {
      
      handleException(t);

    }

  }

}
