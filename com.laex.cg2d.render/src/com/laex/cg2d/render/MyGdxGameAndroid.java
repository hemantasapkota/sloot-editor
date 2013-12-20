package com.laex.cg2d.render;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;

public class MyGdxGameAndroid extends MyGdxGame {

  public MyGdxGameAndroid(String screenControllerFile) {
    super("");
  }

  @Override
  public CGScreenModel loadGameModel() throws GdxRuntimeException {
    return null;
  }

}
