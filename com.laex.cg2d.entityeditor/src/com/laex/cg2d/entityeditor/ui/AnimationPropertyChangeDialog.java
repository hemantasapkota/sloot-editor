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
package com.laex.cg2d.entityeditor.ui;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class AnimationPropertyChangeDialog.
 */
public class AnimationPropertyChangeDialog extends TitleAreaDialog {
  
  /** The txt name. */
  private Text txtName;
  
  /** The name. */
  private String name;
  
  /** The ok button. */
  private Button okButton;

  /** The existing name. */
  private String existingName;
  
  /** The anim duration. */
  private String animDuration;

  /** The lbl animation name. */
  private Label lblAnimationName;
  
  /** The lbl animation duration. */
  private Label lblAnimationDuration;
  
  /** The txt animation duration. */
  private Text txtAnimationDuration;

  /**
   * The Class Validator.
   */
  class Validator implements ModifyListener {
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    @Override
    public void modifyText(ModifyEvent e) {
      if (StringUtils.isEmpty(txtName.getText()) || StringUtils.isEmpty(txtAnimationDuration.getText())) {
        setErrorMessage("Value hould not be empty");
        okButton.setEnabled(false);
        return;
      }

      try {
        Float.parseFloat(txtAnimationDuration.getText());
      } catch (NumberFormatException nex) {
        setErrorMessage("Value must be a valid floating point number");
        okButton.setEnabled(false);
        return;
      }

      setErrorMessage(null);
      okButton.setEnabled(true);
    }
  }

  /** The validator. */
  private final Validator validator = new Validator();

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell
   * @param existingName the existing name
   * @param animDuration the anim duration
   */
  public AnimationPropertyChangeDialog(Shell parentShell, String existingName, String animDuration) {
    super(parentShell);
    this.existingName = existingName;
    this.animDuration = animDuration;
  }

  /**
   * Create contents of the dialog.
   *
   * @param parent the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setTitle("Set new values");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(2, false));
    GridData gd_container = new GridData(GridData.FILL_BOTH);
    gd_container.grabExcessVerticalSpace = false;
    gd_container.heightHint = 55;
    container.setLayoutData(gd_container);

    lblAnimationName = new Label(container, SWT.NONE);
    lblAnimationName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAnimationName.setText("Animation Name");

    txtName = new Text(container, SWT.BORDER);
    txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtName.setText(existingName);
    txtName.addModifyListener(validator);

    lblAnimationDuration = new Label(container, SWT.NONE);
    lblAnimationDuration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAnimationDuration.setText("Animation Duration");

    txtAnimationDuration = new Text(container, SWT.BORDER);
    txtAnimationDuration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtAnimationDuration.setText(animDuration);
    txtAnimationDuration.addModifyListener(validator);

    return area;
  }

  /**
   * Create contents of the button bar.
   *
   * @param parent the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    name = txtName.getText();
    animDuration = txtAnimationDuration.getText();
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
   * Gets the animation duration.
   *
   * @return the animation duration
   */
  public String getAnimationDuration() {
    return animDuration;
  }

  /**
   * Return the initial size of the dialog.
   *
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 197);
  }

}
