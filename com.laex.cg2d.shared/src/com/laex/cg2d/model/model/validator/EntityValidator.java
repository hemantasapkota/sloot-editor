package com.laex.cg2d.model.model.validator;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.IModelValidator;
import com.laex.cg2d.model.util.EntitiesUtil;

public class EntityValidator implements IModelValidator {
  
  private Entity entity;

  public EntityValidator(Entity e) {
    this.entity = e;
  }

  @Override
  public boolean isValid() {
    if (entity == null) {
      return false;
    }
    
    if (entity.getAnimationList() == null || entity.getAnimationList().size() <= 0) {
      return false;
    }

    Image frame = EntitiesUtil.getDefaultFrame(entity);
    if (frame == null) {
      return false;
    }

    return true;
  }

}
