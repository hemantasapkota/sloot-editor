package com.laex.cg2d.model.model;

import com.laex.cg2d.model.model.impl.EntityValidator;

public class ModelValidatorFactory {
  
  public static ModelValidator getValidator(Class<?> modelType, Object model) {
    if (modelType == Entity.class) {
      return new EntityValidator((Entity) model); 
    }
    return null;
  }

}
