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
package com.laex.cg2d.entityeditor.wizards;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.resources.SWTResourceManager;

/**
 * The Class NewEntityPage.
 */
public class NewEntityPage extends WizardPage {

  /** The lbl project name. */
  private Label lblProjectName;

  /** The txt project name. */
  private Text txtProjectName;

  /** The lbl file name. */
  private Label lblFileName;

  /** The txt file name. */
  private Text txtFileName;

  /** The btn browse. */
  private Button btnBrowse;

  /** The path to project. */
  private IPath pathToProject;

  /**
   * Create the wizard.
   * 
   * @wbp.parser.constructor
   */
  public NewEntityPage() {
    super("wizardPage");
    setTitle("New Entities File");
    setDescription("Create a new entities file");
    setPageComplete(false);
  }

  /**
   * Instantiates a new new entity page.
   * 
   * @param pathToProject
   *          the path to project
   */
  public NewEntityPage(IPath pathToProject) {
    this();
    this.pathToProject = pathToProject;
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
    container.setLayout(new GridLayout(3, false));

    lblProjectName = new Label(container, SWT.NONE);
    lblProjectName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblProjectName.setText("Project");

    txtProjectName = new Text(container, SWT.BORDER);
    txtProjectName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    txtProjectName.setEnabled(false);
    txtProjectName.setEditable(false);
    txtProjectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    btnBrowse = new Button(container, SWT.NONE);
    btnBrowse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        ContainerSelectionDialog csd = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace()
            .getRoot(), false, "Select a project");
        int response = csd.open();
        if (response == ContainerSelectionDialog.CANCEL) {
          return;
        }
        
        pathToProject = (IPath) csd.getResult()[0];
        txtProjectName.setText(pathToProject.toOSString());
      }
    });
    btnBrowse.setText("Browse...");

    lblFileName = new Label(container, SWT.NONE);
    lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblFileName.setText("File Name");

    txtFileName = new Text(container, SWT.BORDER);
    txtFileName.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        validateFileName();
      }
    });
    txtFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);

    if (pathToProject != null) {
      txtProjectName.setText(pathToProject.toOSString());
    }

    txtFileName.setFocus();
  }

  /**
   * Validate file name.
   */
  private void validateFileName() {
    String filename = txtFileName.getText();

    if (StringUtils.isEmpty(filename)) {
      setErrorMessage("Filename empty");
      setPageComplete(false);
      return;
    } else {
      setErrorMessage(null);
    }

    IPath npath = pathToProject.append(filename);
    npath = npath.addFileExtension(ICGCProject.ENTITIES_EXTENSION);

    boolean exists = CGCProject.getInstance().exists(npath, false);
    if (exists) {
      setErrorMessage("Resource already exists. Please try a new name");
      setPageComplete(false);
      return;
    } else {
      setErrorMessage(null);
    }

    setPageComplete(true);
  }

  /**
   * Open container resource dialog.
   */
  protected void openContainerResourceDialog() {
    ContainerSelectionDialog csd = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(),
        false, "Select a project");
    int response = csd.open();
    if (response == ContainerSelectionDialog.CANCEL) {
      return;
    }
    pathToProject = (IPath) csd.getResult()[0];
    txtProjectName.setText(pathToProject.toOSString());
  }

  /**
   * Gets the project name.
   * 
   * @return the project name
   */
  public String getProjectName() {
    return txtProjectName.getText().trim();
  }

  /**
   * Gets the file name.
   * 
   * @return the file name
   */
  public String getFileName() {
    return txtFileName.getText().trim();
  }

  /**
   * Gets the path to project.
   * 
   * @return the path to project
   */
  public IPath getPathToProject() {
    return pathToProject;
  }

}
