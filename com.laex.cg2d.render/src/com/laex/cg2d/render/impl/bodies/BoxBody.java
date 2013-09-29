package com.laex.cg2d.render.impl.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.ScreenManager;
import com.laex.cg2d.render.impl.AbstractBox2DBody;
import com.laex.cg2d.render.util.ScreenToWorld;

public class BoxBody extends AbstractBox2DBody {

  public BoxBody(CGShape shape, ScreenManager screenManager) {
    super(shape, screenManager);
  }

  @Override
  public Body create(BodyDef bodyDef, FixtureDef fixtureDef) {
    Vector2 position = ScreenToWorld.inst(screenMgr().model()).screenToWorldFlipped(
        new Vector2(shape().getBounds().getX(), shape().getBounds().getY()), shape().getBounds().getHeight());

    bodyDef.position.set(position);
    Body b = screenMgr().world().createBody(bodyDef);

    PolygonShape polyShape = new PolygonShape();
    float hx = (shape().getBounds().getWidth() / ptmRatio());
    float hy = (shape().getBounds().getHeight() / ptmRatio());

    hx = hx / 2;
    hy = hy / 2;

    polyShape.setAsBox(hx, hy, new Vector2(hx, hy), 0);

    fixtureDef.shape = polyShape;
    b.createFixture(fixtureDef);

    return b;
  }

  @Override
  public boolean isValid() {
    return false;
  }

}
