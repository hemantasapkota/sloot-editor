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
package com.laex.cg2d.screeneditor.views;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class SimpleTextChangeDialog.
 */
public class SimpleTextChangeDialog extends TitleAreaDialog {

  /** The txt name. */
  private Text txtName;

  /** The name. */
  private String name;

  /** The ok button. */
  private Button okButton;

  /** The existing name. */
  private String existingName;

  /** The description. */
  private String description;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   * @param existingName
   *          the existing name
   * @param description
   *          the description
   */
  public SimpleTextChangeDialog(Shell parentShell, String existingName, String description) {
    super(parentShell);
    this.existingName = existingName;
    this.description = description;
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
    setMessage(description);
    setTitle("Set a new text");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(1, false));
    GridData gd_container = new GridData(GridData.FILL_BOTH);
    gd_container.grabExcessVerticalSpace = false;
    gd_container.heightHint = 33;
    container.setLayoutData(gd_container);

    txtName = new Text(container, SWT.BORDER);
    txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtName.setText(existingName);

    return area;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   *          the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    name = txtName.getText();
    super.okPressed();
  }

  /**
   * Gets the name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 175);
  }

}
