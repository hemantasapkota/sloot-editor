package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface Box2DBody {

  boolean isValid();

  Body create(BodyDef bodyDef, FixtureDef fixtureDef);

}
