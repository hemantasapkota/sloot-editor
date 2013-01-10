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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * The Class ProjectExportWizard.
 */
public class ProjectExportWizard extends Wizard implements IExportWizard {

  /** The page1. */
  ProjectExportWizardPage1 page1;
  
  /** The selected project. */
  IProject selectedProject;

  /**
   * Instantiates a new project export wizard.
   */
  public ProjectExportWizard() {
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages() {
    page1 = new ProjectExportWizardPage1(selectedProject);
    addPage(page1);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish() {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
   */
  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    selectedProject = (IProject) selection.getFirstElement();
    
    if (selectedProject != null) {
      setWindowTitle("Export " + selectedProject.getName());
    } else {
      setWindowTitle("Export Casual Games 2D Projects");
    }
  }

}
