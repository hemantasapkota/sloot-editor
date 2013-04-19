package com.laex.cg2d.render.impl;

import java.util.Iterator;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.GameObject.CGEditorShapeType;
import com.laex.cg2d.protobuf.GameObject.CGShape;
import com.laex.cg2d.render.IEntityQueryable;

public class EntityQueryManager implements IEntityQueryable {
  
  private World world;

  public EntityQueryManager(World world) {
    this.world = world;
  }

  @Override
  public Body getEntityById(String id) {
    Iterator<Body> body = world.getBodies();
    
    while (body.hasNext()) {
      Body b = body.next();
      
      if (!(b.getUserData() instanceof CGShape)) {
        continue;
      }
      
      
      CGShape shape = (CGShape) b.getUserData();
      if (!(shape.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE)) {
        continue;
      }
      
      if (shape.getId().equals(id)) {
        return b;
      }
      
    }
    
    return null;
  }

  @Override
  public void test() {
    System.err.println("Test method called");
  }

}
