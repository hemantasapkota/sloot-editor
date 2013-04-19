package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;

public interface IEntityQueryable {
  
  void test();
  
  Body getEntityById(String id);
  
}
