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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * The Class AddLayerDialog.
 */
class AddLayerDialog extends Dialog {
  
  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  
  /** The text. */
  private Text text;

  /** The layer name. */
  private String layerName;

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell
   */
  public AddLayerDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   *
   * @param parent the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));

    Section sctnAddLay = formToolkit.createSection(container, Section.TITLE_BAR);
    formToolkit.paintBordersFor(sctnAddLay);
    sctnAddLay.setText("Add Layer");

    Composite composite = formToolkit.createComposite(sctnAddLay, SWT.NONE);
    formToolkit.paintBordersFor(composite);
    sctnAddLay.setClient(composite);
    composite.setLayout(new GridLayout(2, false));

    Label lblLayerName = formToolkit.createLabel(composite, "Layer Name", SWT.NONE);
    lblLayerName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

    text = formToolkit.createText(composite, "New Text", SWT.NONE);
    text.setText("");
    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    return container;
  }

  /**
   * Create contents of the button bar.
   *
   * @param parent the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Add", true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   *
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 142);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    layerName = text.getText();
    super.okPressed();
  }

  /**
   * Gets the layer name.
   *
   * @return the layer name
   */
  public String getLayerName() {
    return layerName;
  }

}
