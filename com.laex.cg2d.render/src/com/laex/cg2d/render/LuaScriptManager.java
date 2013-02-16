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

public class LuaScriptManager extends AbstractGameComponentManager {

  Globals globals = JsePlatform.standardGlobals();
  LuaValue chunk;
  boolean scriptFileExists = false;

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
        this.execute("initBody", b, shape.getId());
      }

    }
  }

  public void render() {
    if (!scriptFileExists)
      return;

    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      Object userData = b.getUserData();

      if (userData instanceof CGShape) {
        CGShape shape = (CGShape) userData;
        this.execute("update", b, shape.getId());
      }

    }
  }

  private void executeInit() {
    LuaValue worldLua = CoerceJavaToLua.coerce(world());
    LuaValue cameraLua = CoerceJavaToLua.coerce(camera());
 
    globals.get("init").invoke(new LuaValue[] { worldLua, cameraLua });
  }

  private void execute(String functionName, Body body, String id) {
    LuaValue worldLua = CoerceJavaToLua.coerce(world());
    LuaValue cameraLua = CoerceJavaToLua.coerce(camera());
    LuaValue bodyLua = CoerceJavaToLua.coerce(body);

    globals.get(functionName).invoke(new LuaValue[]
      { worldLua, cameraLua, bodyLua, LuaValue.valueOf(id) });
  }

  @Override
  public void dispose() {
  }
}
