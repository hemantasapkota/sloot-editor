package com.laex.cg2d.render;

import java.util.Iterator;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;
import com.laex.cg2d.protobuf.GameObject.CGShape;

public class LuaScriptManager extends AbstractGameComponentManager implements IGameScript {

  private Globals globals = JsePlatform.standardGlobals();
  private LuaValue chunk;
  private boolean scriptFileExists = false;

  public LuaScriptManager(CGGameModel model, World world, Camera cam) {
    super(model, world, cam);
  }

  public LuaScriptManager(CGGameModel model, World world, Camera cam, String scriptFileName) {
    super(model, world, cam);

    if (!Gdx.files.absolute(scriptFileName).exists()) {
      scriptFileExists = false;
      return;
    } else {
      scriptFileExists = true;
    }

    chunk = globals.loadFile(scriptFileName);
    // very important step. subsequent calls to method do not work if the chunk
    // is not called here
    chunk.call();

  }

  @Override
  public void create() {
    if (!scriptFileExists)
      return;

    executeInit();

    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      Object userData = b.getUserData();

      if (userData instanceof CGShape) {
        CGShape shape = (CGShape) userData;
        this.executeInitBody(b, shape.getId());
      }

    }
  }

  public void render() {
    if (!canExecute())
      return;

    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      Object userData = b.getUserData();

      if (userData instanceof CGShape) {
        CGShape shape = (CGShape) userData;
        executeUpdate(b, shape.getId());
      }

    }
  }

  @Override
  public void dispose() {
  }

  @Override
  public void executeInit() {
    globals.get("init").invoke(new LuaValue[]
      { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()) });
  }

  @Override
  public void executeInitBody(Body body, String bodyId) {
    LuaValue bodyLua = CoerceJavaToLua.coerce(body);

    globals.get("initBody").invoke(new LuaValue[]
      { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), bodyLua, LuaValue.valueOf(bodyId) });
  }

  @Override
  public void executeUpdate(Body body, String bodyId) {
    LuaValue bodyLua = CoerceJavaToLua.coerce(body);

    globals.get("update").invoke(new LuaValue[]
      { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), bodyLua, LuaValue.valueOf(bodyId) });
  }

  @Override
  public void executeKeyPressed(String key) {
    globals.get("keyPressed").invoke(new LuaValue[]
      { CoerceJavaToLua.coerce(world()), CoerceJavaToLua.coerce(camera()), LuaValue.valueOf(key) });

  }

  @Override
  public boolean canExecute() {
    return scriptFileExists;
  }
}
