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
package com.laex.cg2d.core;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.laex.cg2d.screeneditor.views.LayersViewPart;
import com.laex.cg2d.screeneditor.views.TexturesViewPart;

/**
 * The Class Perspective.
 */
public class Perspective implements IPerspectiveFactory {

  /**
   * The ID of the perspective as specified in the extension.
   */
  public static final String ID = "com.laex.cg2d.core.perspective";

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.
   * IPageLayout)
   */
  public void createInitialLayout(IPageLayout layout) {
    {
      IFolderLayout folderLayout = layout
          .createFolder("projectFolder", IPageLayout.LEFT, 0.14f, layout.getEditorArea());
      folderLayout.addView("com.laex.cg2d.comnav");
    }

    {
      IFolderLayout folderLayout = layout.createFolder("rightFolder", IPageLayout.RIGHT, 0.80f, layout.getEditorArea());
      folderLayout.addView("com.laex.cg2d.entityeditor.views.Entities");
      folderLayout.addView("com.laex.cg2d.core.propertyView");
      folderLayout.addView(LayersViewPart.ID);
    }

    {
      IFolderLayout folderLayout = layout.createFolder("bottomFolder", IPageLayout.BOTTOM, 0.60f,
          layout.getEditorArea());
      folderLayout.addView("com.laex.cg2d.core.LogView");
      folderLayout.addView(TexturesViewPart.ID);
      folderLayout.addView("org.eclipse.ui.views.ContentOutline");
    }

  }

}
