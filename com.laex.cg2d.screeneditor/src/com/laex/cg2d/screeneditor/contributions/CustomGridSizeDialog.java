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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * The Class CustomGridSizeDialog.
 */
class CustomGridSizeDialog extends Dialog {

  /** The txt width. */
  private Spinner txtWidth;

  /** The txt height. */
  private Spinner txtHeight;

  /** The width. */
  private int width;

  /** The height. */
  private int height;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   */
  public CustomGridSizeDialog(Shell parentShell) {
    super(parentShell);
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
    container.setLayout(new GridLayout(2, false));

    Label lblWidth = new Label(container, SWT.NONE);
    lblWidth.setText("Width");

    txtWidth = new Spinner(container, SWT.BORDER);
    GridData gd_txtWidth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txtWidth.widthHint = 119;
    txtWidth.setLayoutData(gd_txtWidth);
    txtWidth.setMinimum(1);
    txtWidth.setSelection(16);

    Label lblHeight = new Label(container, SWT.NONE);
    lblHeight.setText("Height");

    txtHeight = new Spinner(container, SWT.BORDER);
    txtHeight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    txtHeight.setMinimum(1);
    txtHeight.setSelection(16);

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
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(209, 144);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    width = txtWidth.getSelection();
    height = txtHeight.getSelection();
    super.okPressed();
  }

  /**
   * Gets the width.
   * 
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height.
   * 
   * @return the height
   */
  public int getHeight() {
    return height;
  }
}
