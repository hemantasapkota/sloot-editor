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
package com.laex.cg2d.screeneditor.wizards;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.laex.cg2d.model.CGCProject;

/**
 * The Class NewProjectPage.
 */
public class NewProjectPage extends WizardPage {

  /** The lbl project name. */
  private Label lblProjectName;

  /** The txt project name. */
  private Text txtProjectName;

  /**
   * Create the wizard.
   */
  public NewProjectPage() {
    super("wizardPage");
    setTitle("New Project");
    setDescription("Create a new Project");
    setPageComplete(false);
  }

  /**
   * Create contents of the wizard.
   * 
   * @param parent
   *          the parent
   */
  public void createControl(Composite parent) {
    Composite container = new Composite(parent, SWT.NULL);

    setControl(container);
    container.setLayout(new GridLayout(2, false));

    lblProjectName = new Label(container, SWT.NONE);
    lblProjectName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblProjectName.setText("Project Name");

    txtProjectName = new Text(container, SWT.BORDER);
    txtProjectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtProjectName.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {

        try {
          validateProjectName();
        } catch (CoreException e1) {
          e1.printStackTrace();
        }

      }
    });

    txtProjectName.setFocus();
  }

  /**
   * Validate project name.
   * 
   * @throws CoreException
   *           the core exception
   */
  private void validateProjectName() throws CoreException {
    String prjName = txtProjectName.getText();

    if (StringUtils.isEmpty(prjName)) {
      setErrorMessage("Project Name empty");
      setPageComplete(false);
      return;
    } else {
      setErrorMessage(null);
    }

    IPath path = new Path("/" + prjName);
    boolean exists = CGCProject.getInstance().exists(path, true);
    if (exists) {
      setErrorMessage("Project already exists. Please try a new name");
      setPageComplete(false);
      return;
    } else {
      setErrorMessage(null);
    }

    setPageComplete(true);
  }

  /**
   * Gets the project name.
   * 
   * @return the project name
   */
  public String getProjectName() {
    return txtProjectName.getText().trim();
  }

}
