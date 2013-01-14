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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.laex.cg2d.screeneditor.palette.ScreenEditorPaletteFactory;

/**
 * The Class EntityDeleteHelperDialog.
 */
public class EntityDeleteHelperDialog extends Dialog {

  /** The entity resource. */
  private IResource entityResource;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   */
  public EntityDeleteHelperDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Instantiates a new entity delete helper dialog.
   * 
   * @param entityResource
   *          the entity resource
   * @param parentShell
   *          the parent shell
   */
  public EntityDeleteHelperDialog(IResource entityResource, Shell parentShell) {
    super(parentShell);
    this.entityResource = entityResource;
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   *          the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);

    Label lblTheFollowingWill = new Label(container, SWT.NONE);
    lblTheFollowingWill.setText("The following will remove the delete entities from the palette as well.");

    return container;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   *          the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 124);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    super.okPressed();
    ScreenEditorPaletteFactory.removeEntity(entityResource);
  }

}
