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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.laex.cg2d.shared.CGCProject;

/**
 * The Class ProjectExportWizardPage1.
 */
public class ProjectExportWizardPage1 extends WizardPage {

  /** The project. */
  private IProject project;
  
  /** The combo. */
  private Combo combo;

  /**
   * Create the wizard.
   *
   * @param project the project
   */
  public ProjectExportWizardPage1(IProject project) {
    super("wizardPage");
    this.project = project;

    setTitle("Export " + ((project == null) ? "casual games 2d project" : project.getName()));
    setDescription("Export the casual games 2d project to CGAPP format.");
  }

  /**
   * Create contents of the wizard.
   *
   * @param parent the parent
   */
  public void createControl(Composite parent) {
    Composite container = new Composite(parent, SWT.NULL);

    setControl(container);
    container.setLayout(new GridLayout(2, false));

    Label lblProject = new Label(container, SWT.NONE);
    lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblProject.setText("Project");

    combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
    combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    loadProjects();
  }

  /**
   * Load projects.
   */
  private void loadProjects() {
    combo.removeAll();
    combo.clearSelection();

    if (project != null) {
      combo.add(project.getName());
      combo.select(0);
      return;
    }

    for (IProject prj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
      try {
        String prop = prj.getPersistentProperty(CGCProject.PROJECT_QUALIFIER);
        if (prop != null && prop.equals(CGCProject.PROJECT_TYPE)) {
          combo.add(prj.getName());
        }
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }

    if (combo.getItemCount() >= 0) {
      combo.select(0);
    }
  }

}
