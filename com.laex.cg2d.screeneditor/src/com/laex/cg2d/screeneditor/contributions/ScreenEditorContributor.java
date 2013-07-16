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
package com.laex.cg2d.screeneditor.contributions;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.laex.cg2d.screeneditor.ScreenEditor;

/**
 * The Class ScreenEditorContributor.
 */
public class ScreenEditorContributor extends ActionBarContributor {

  /** The shapes editor. */
  private ScreenEditor shapesEditor;

  /** The grid16x16 action. */
  private Action grid16x16Action;

  /** The grid32x32 action. */
  private Action grid32x32Action;

  /** The grid custom size action. */
  private Action gridCustomSizeAction;

  /**
   * Instantiates a new screen editor contributor.
   */
  public ScreenEditorContributor() {
  }

  /**
   * Creates the actions.
   */
  private void createActions() {

    grid16x16Action = new Action("16x16") {
      @Override
      public void run() {
        shapesEditor.setGridDimension(16, 16);
      }
    };

    grid32x32Action = new Action("32x32") {
      @Override
      public void run() {
        shapesEditor.setGridDimension(32, 32);
      }
    };

    gridCustomSizeAction = new Action("Custom...") {
      @Override
      public void run() {
        CustomGridSizeDialog customGridSizeDialog = new CustomGridSizeDialog(shapesEditor.getSite().getShell());
        int response = customGridSizeDialog.open();
        if (response != CustomGridSizeDialog.OK) {
          return;
        }
        shapesEditor.setGridDimension(customGridSizeDialog.getWidth(), customGridSizeDialog.getHeight());
      }
    };

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.actions.ActionBarContributor#setActiveEditor(org.eclipse
   * .ui.IEditorPart)
   */
  @Override
  public void setActiveEditor(IEditorPart targetEditor) {
    this.shapesEditor = (ScreenEditor) targetEditor;
    super.setActiveEditor(targetEditor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.
   * eclipse.jface.action.IToolBarManager)
   */
  @Override
  public void contributeToToolBar(IToolBarManager mgr) {
    super.contributeToToolBar(mgr);

    // mgr.add(getAction(ActionFactory.UNDO.getId()));
    // mgr.add(getAction(ActionFactory.REDO.getId()));

    String[] zoomStrings = new String[]
      { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
    mgr.add(new ZoomComboContributionItem(getPage(), zoomStrings));

    mgr.update(true);

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToCoolBar(org.
   * eclipse.jface.action.ICoolBarManager)
   */
  @Override
  public void contributeToCoolBar(ICoolBarManager coolBarManager) {
    super.contributeToCoolBar(coolBarManager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse
   * .jface.action.IMenuManager)
   */
  @Override
  public void contributeToMenu(IMenuManager menuManager) {
    super.contributeToMenu(menuManager);

    MenuManager gridMenu = new MenuManager("Grid");
    gridMenu.add(grid16x16Action);
    gridMenu.add(grid32x32Action);
    gridMenu.add(gridCustomSizeAction);
    menuManager.insertBefore(IWorkbenchActionConstants.M_WINDOW, gridMenu);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
   */
  @Override
  protected void buildActions() {
    createActions();

    // Add these retarget actions
    // addRetargetAction(new DeleteRetargetAction());
    // addRetargetAction(new UndoRetargetAction());
    // addRetargetAction(new RedoRetargetAction());

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
   */
  @Override
  protected void declareGlobalActionKeys() {
  }
}
