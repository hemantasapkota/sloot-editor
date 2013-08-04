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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.laex.cg2d.screeneditor.views.LayersViewPart;
import com.laex.cg2d.screeneditor.views.TexturesViewPart;
import org.eclipse.jface.action.ToolBarManager;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  /** The prefernces action. */
  private IWorkbenchAction preferncesAction;

  /** The show views list action. */
  private IContributionItem showViewsListAction;

  /** The show texture view action. */
  private IAction showTextureViewAction;

  /** The show layers view action. */
  private IAction showLayersViewAction;

  /** The new wizard drop down action. */
  private IAction newWizardDropDownAction;
  
  /** The open perspective dialog action. */
  private IAction openPerspectiveDialogAction;
  
  /** The save action. */
  private IAction saveAction;
  
  /** The save all action. */
  private IAction saveAllAction;

  /**
   * Instantiates a new application action bar advisor.
   * 
   * @param configurer
   *          the configurer
   */
  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.
   * IWorkbenchWindow)
   */
  protected void makeActions(final IWorkbenchWindow window) {
    super.makeActions(window);

    //
    preferncesAction = ActionFactory.PREFERENCES.create(window);
    //
    showViewsListAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    //
    showTextureViewAction = new Action("&Textures") {
      @Override
      public void run() {
        try {
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TexturesViewPart.ID);
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    };

    showLayersViewAction = new Action("&Layers") {
      @Override
      public void run() {
        try {
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LayersViewPart.ID);
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    };

    {
      newWizardDropDownAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
      register(newWizardDropDownAction);
    }
    {
      openPerspectiveDialogAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
      register(openPerspectiveDialogAction);
    }
    {
      saveAction = ActionFactory.SAVE.create(window);
      register(saveAction);
    }
    {
      saveAllAction = ActionFactory.SAVE_ALL.create(window);
      register(saveAllAction);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface
   * .action.ICoolBarManager)
   */
  @Override
  protected void fillCoolBar(ICoolBarManager coolBar) {
    
    ToolBarManager toolBarManager = new ToolBarManager();
    coolBar.add(toolBarManager);
    toolBarManager.add(newWizardDropDownAction);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface
   * .action.IMenuManager)
   */
  protected void fillMenuBar(IMenuManager menuBar) {
    MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
    MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
    MenuManager prefsMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);

    menuBar.add(fileMenu);
    // Add a group marker indicating where action set menus will appear.
    menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    menuBar.add(prefsMenu);
    menuBar.add(helpMenu);

    // fileMenu.add(saveAction);
    fileMenu.add(new Separator());
    fileMenu.add(saveAction);
    fileMenu.add(saveAllAction);


    // prefs
    prefsMenu.add(preferncesAction);
    prefsMenu.add(new Separator());
    prefsMenu.add(showTextureViewAction);
    prefsMenu.add(showLayersViewAction);
    prefsMenu.add(showViewsListAction);

  }

}
