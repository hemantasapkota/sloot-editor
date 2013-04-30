package com.laex.cg2d.render.impl;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.protobuf.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;
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

      if (!hasCGShape(b)) {
        continue;
      }

      CGShape shape = (CGShape) b.getUserData();
      if (!isOfEntityType(shape)) {
        continue;
      }

      if (shape.getId().equals(id)) {
        return b;
      }

    }

    return null;
  }

  @Override
  public String getEntityIdByBody(Body b) {
    if (!hasCGShape(b))
      return StringUtils.EMPTY;
    
    return ((CGShape) b.getUserData()).getId();
  }

  private boolean hasCGShape(Body b) {
    if (b.getUserData() instanceof CGShape)
      return true;

    return false;
  }

  private boolean isOfEntityType(CGShape shape) {
    if (shape.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE)
      return true;

    return false;
  }

}
