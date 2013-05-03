package com.laex.cg2d.render;

import com.badlogic.gdx.physics.box2d.Body;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;

public interface BodyVisitor {
  
  void visit(Body b, CGShape shape);

}
