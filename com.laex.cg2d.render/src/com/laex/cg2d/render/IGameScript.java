package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;

public interface IGameScript {
  
  boolean canExecute();
  
  void executeInit();
  void executeInitBody(Body body, String bodyId);
  void executeUpdate(Body body, String bodyId);
  void executeKeyPressed(String key);

}
