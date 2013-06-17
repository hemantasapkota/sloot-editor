package com.laex.cg2d.model.model.impl;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.ModelValidator;
import com.laex.cg2d.model.util.EntitiesUtil;

public class EntityValidator implements ModelValidator {
  
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
