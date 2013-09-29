package com.laex.cg2d.render.impl;

import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.Box2DBody;
import com.laex.cg2d.render.ScreenManager;

public abstract class AbstractBox2DBody implements Box2DBody {

  private ScreenManager screenMgr;

  private CGShape shape;

  private int ptmRatio;


  public AbstractBox2DBody(CGShape shape, ScreenManager screenManager) {
    this.screenMgr = screenManager;
    this.shape = shape;

    this.ptmRatio = this.screenMgr.model().getScreenPrefs().getWorldPrefs().getPtmRatio();
  }


  protected int ptmRatio() {
    return ptmRatio;
  }

  protected CGShape shape() {
    return shape;
  }

  protected ScreenManager screenMgr() {
    return screenMgr;
  }

}
