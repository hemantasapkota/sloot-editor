package com.laex.cg2d.render.impl.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.ScreenManager;
import com.laex.cg2d.render.impl.AbstractBox2DBody;
import com.laex.cg2d.render.impl.EntityManager;
import com.laex.cg2d.render.util.ScreenToWorld;

public class EntityBody extends AbstractBox2DBody {

  private EntityManager entityMgr;
  private CGEntity entity;
  private CGEntityAnimation entityAnimation;

  public EntityBody(CGShape shape, ScreenManager screenManager, EntityManager entityMgr, CGEntity entity, CGEntityAnimation ea) {
    super(shape, screenManager);

    this.entityMgr = entityMgr;
    this.entity = entity;
    this.entityAnimation = ea;
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public Body create(BodyDef bodyDef, FixtureDef fixtureDef) {

    Vector2 position = new Vector2(shape().getBounds().getX(), shape().getBounds().getY());

    position = ScreenToWorld.inst(screenMgr().model()).screenToWorldFlipped(position, shape().getBounds().getHeight());

    bodyDef.position.set(position);

    Body b = screenMgr().world().createBody(bodyDef);

    entityMgr.createEntityCollisionShape(shape(), entity, entityAnimation, bodyDef, fixtureDef, b);

    return b;
  }

}
