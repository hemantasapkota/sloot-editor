package com.laex.cg2d.render.util;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;

public final class ScreenToWorld {

  private int ptmRatio;
  private int cardHeight;

  private static ScreenToWorld instance;

  public static ScreenToWorld inst(CGScreenModel screenModel) {
    if (instance == null) {
      instance = new ScreenToWorld(screenModel);
    }
    return instance;
  }

  private ScreenToWorld(CGScreenModel screenModel) {
    this.ptmRatio = screenModel.getScreenPrefs().getWorldPrefs().getPtmRatio();
    this.cardHeight = screenModel.getScreenPrefs().getCardPrefs().getCardHeight();
  }

  public Vector2 screenToWorld(Vector2 argScreen) {
    return new Vector2(argScreen.x / ptmRatio, argScreen.y / ptmRatio);
  }

  public Vector2 screenToWorldFlipped(Vector2 argScreen, float height) {
    float x = argScreen.x;
    float y = (cardHeight - height - argScreen.y);

    return new Vector2(x / ptmRatio, y / ptmRatio);
  }

}
