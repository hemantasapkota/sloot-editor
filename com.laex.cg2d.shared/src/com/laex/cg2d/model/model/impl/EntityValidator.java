/*
 * Copyright (c) 2012, 2013 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package com.laex.cg2d.model.model.impl;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.ModelValidator;
import com.laex.cg2d.model.util.EntitiesUtil;

/**
 * The Class EntityValidator.
 */
public class EntityValidator implements ModelValidator {
  
  /** The entity. */
  private Entity entity;

  /**
   * Instantiates a new entity validator.
   *
   * @param e the e
   */
  public EntityValidator(Entity e) {
    this.entity = e;
  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.model.model.ModelValidator#isValid()
   */
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
