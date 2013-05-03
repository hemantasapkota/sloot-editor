package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;

public interface EntityQueryable {
  
  Body getEntityById(String id);
  
  String getEntityIdByBody(Body b);
  
}
