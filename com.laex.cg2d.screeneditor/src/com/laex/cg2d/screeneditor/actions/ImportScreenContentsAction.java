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
package com.laex.cg2d.screeneditor.actions;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * The Class ImportScreenContentsAction.
 */
public class ImportScreenContentsAction extends Action {

  /** The command stak. */
  private EditPartViewer viewer;

  /**
   * Instantiates a new import screen contents action.
   * 
   * @param cmdStack
   *          the cmd stack
   */
  public ImportScreenContentsAction(EditPartViewer viewer) {
    setText("Import Screen Contents");
    this.viewer = viewer;
  }
  
  @Override
  public boolean isEnabled() {
    return viewer.getSelectedEditParts().size() == 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.action.Action#run()
   */
  @Override
  public void run() {
    final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
        .getSite().getShell();

    shell.getDisplay().asyncExec(new Runnable() {
      @Override
      public void run() {
        ListExistingScreensDialog screensDialog = new ListExistingScreensDialog(shell, viewer.getEditDomain().getCommandStack());
        int responseCode = screensDialog.open();
        if (responseCode == ListExistingScreensDialog.CANCEL) {
          return;
        }
      }
    });
  }
}
