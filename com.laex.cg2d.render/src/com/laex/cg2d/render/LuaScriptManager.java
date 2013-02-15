package com.laex.cg2d.render;

import java.util.Iterator;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;
import com.laex.cg2d.protobuf.GameObject.CGShape;

public class LuaScriptManager extends AbstractGameComponentManager {

  Globals globals = JsePlatform.standardGlobals();
  LuaValue chunk;

  public LuaScriptManager(CGGameModel model, World world, Camera cam) {
    super(model, world, cam);
  }

  public LuaScriptManager(CGGameModel model, World world, Camera cam, String scriptFileName) {
    super(model, world, cam);

    chunk = globals.loadFile(scriptFileName);
    // very important step. subsequent calls to method do not work if the chunk
    // is not called here
    chunk.call();

  }

  @Override
  public void create() {
    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      Object userData = b.getUserData();

      if (userData instanceof CGShape) {
        CGShape shape = (CGShape) userData;
        this.execute("init", b, camera(), shape.getId());
      }

    }
  }

  public void render() {
    Iterator<Body> itr = world().getBodies();
    while (itr.hasNext()) {
      Body b = itr.next();
      Object userData = b.getUserData();

      if (userData instanceof CGShape) {
        CGShape shape = (CGShape) userData;
        this.execute("update", b, camera(), shape.getId());
      }

    }
  }

  private void execute(String functionName, Body body, Camera cam, String id) {
    globals.get(functionName).call(CoerceJavaToLua.coerce(body), CoerceJavaToLua.coerce(cam), LuaValue.valueOf(id));
  }

  @Override
  public void dispose() {
  }
}
