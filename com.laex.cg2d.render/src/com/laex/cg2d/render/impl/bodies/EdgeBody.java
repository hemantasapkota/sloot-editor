package com.laex.cg2d.render.impl.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.ScreenManager;
import com.laex.cg2d.render.impl.AbstractBox2DBody;
import com.laex.cg2d.render.util.ScreenToWorld;

public class EdgeBody extends AbstractBox2DBody {

  public EdgeBody(CGShape shape, ScreenManager screenManager) {
    super(shape, screenManager);
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public Body create(BodyDef bodyDef, FixtureDef fixtureDef) {
    EdgeShape edge = new EdgeShape();

    float x = shape().getBounds().getX();
    float y = shape().getBounds().getY();
    float width = shape().getBounds().getWidth();
    float height = shape().getBounds().getHeight();

    Vector2 v1 = new Vector2(x, y);
    Vector2 v2 = new Vector2(x + width, y);

    Vector2 v2Vertical = new Vector2(x, y + height);

    ScreenToWorld stw = ScreenToWorld.inst(screenMgr().model());

    v1 = stw.screenToWorldFlipped(v1, 0);
    v2 = stw.screenToWorldFlipped(v2, 0);
    v2Vertical = stw.screenToWorldFlipped(v2Vertical, 0);

    if (shape().getEditorShapeType() == CGEditorShapeType.SIMPLE_SHAPE_VEDGE) {
      edge.set(v1, v2Vertical);
    } else {
      edge.set(v1, v2);
    }

    Body b = screenMgr().world().createBody(bodyDef);

    fixtureDef.shape = edge;
    b.createFixture(fixtureDef);

    return b;
  }

}
