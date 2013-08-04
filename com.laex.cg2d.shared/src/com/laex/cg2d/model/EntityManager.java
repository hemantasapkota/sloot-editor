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
package com.laex.cg2d.model;

import org.eclipse.ui.PlatformUI;

/**
 * The Class EntityManager.
 */
public class EntityManager {
  
  /** The Constant ENTITIES_VIEW_ID. */
  public static final String ENTITIES_VIEW_ID = "com.laex.cg2d.entityeditor.views.Entities";
  
  /**
   * Entity manager.
   *
   * @return the i entity manager
   */
  public static IEntityManager entityManager() {
    return (IEntityManager) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ENTITIES_VIEW_ID);
  }

}
