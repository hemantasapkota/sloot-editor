package com.laex.cg2d.model;

import org.eclipse.ui.PlatformUI;

public class EntityManager {
  
  public static final String ENTITIES_VIEW_ID = "com.laex.cg2d.entityeditor.views.Entities";
  
  public static IEntityManager entityManager() {
    return (IEntityManager) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ENTITIES_VIEW_ID);
  }

}
