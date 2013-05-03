package com.laex.cg2d.render.impl;

import java.util.Iterator;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;
import com.laex.cg2d.render.EntityQueryable;

public class EntityQueryManager implements EntityQueryable {

  private World world;

  public EntityQueryManager(World world) {
    this.world = world;
  }

  @Override
  public Body getEntityById(String id) {
    Iterator<Body> body = world.getBodies();

    while (body.hasNext()) {
      Body b = body.next();

      CGShape shape = (CGShape) b.getUserData();

      if (shape.getId().equals(id)) {
        return b;
      }
    }

    return null;
  }

  @Override
  public String getEntityIdByBody(Body b) {
    return ((CGShape) b.getUserData()).getId();
  }

}
