package com.laex.cg2d.model.model;

import com.laex.cg2d.model.model.impl.ShapeCopier;

public class ModelCopierFactory {
  
  public static ModelCopier getModelCopier(Class<?> modelType, GameModel model) {
    if (modelType == Shape.class) {
      return new ShapeCopier(model);
    }
    
    return null;
  }

}
