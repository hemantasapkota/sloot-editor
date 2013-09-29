package com.laex.cg2d.render.impl.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.ScreenManager;
import com.laex.cg2d.render.impl.AbstractBox2DBody;
import com.laex.cg2d.render.util.ScreenToWorld;

public class CircleBody extends AbstractBox2DBody {

  public CircleBody(CGShape shape, ScreenManager screenManager) {
    super(shape, screenManager);
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public Body create(BodyDef bodyDef, FixtureDef fixtureDef) {
    Vector2 position = ScreenToWorld.inst(screenMgr().model()).screenToWorldFlipped(
        new Vector2(shape().getBounds().getX(), shape().getBounds().getY()), shape().getBounds().getHeight());

    CircleShape circShape = new CircleShape();
    float radius = CircleBody.calculateRadiusOfCircleShape(shape().getBounds().getWidth(), ptmRatio());

    bodyDef.position.set(position.add(radius, radius));
    Body b = screenMgr().world().createBody(bodyDef);

    circShape.setRadius(radius);
    fixtureDef.shape = circShape;
    b.createFixture(fixtureDef);

    return b;
  }

  public static float calculateRadiusOfCircleShape(float width, int ptmRatio) {
    float w = width / ptmRatio;
    float rad = w / 2;
    return rad;
  }

}
