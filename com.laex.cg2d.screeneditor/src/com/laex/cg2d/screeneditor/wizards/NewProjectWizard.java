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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;

/**
 * The Class NewProjectWizard.
 */
public class NewProjectWizard extends Wizard implements INewWizard {

  /** The page1. */
  private NewProjectPage page1;

  /**
   * Instantiates a new new project wizard.
   */
  public NewProjectWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  public void addPages() {
    page1 = new NewProjectPage();
    addPage(page1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  public boolean performFinish() {
    final String projectName = page1.getProjectName();

    WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
      @Override
      protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
          InterruptedException {
        monitor.beginTask("Creating", 0);
        ICGCProject b2Prj = CGCProject.getInstance();
        IProject newProject = b2Prj.createProject(monitor, projectName);
        monitor.worked(1);
        b2Prj.createFolder(newProject, monitor, ICGCProject.ENTITIES_FOLDER);
        monitor.worked(1);
        b2Prj.createFolder(newProject, monitor, ICGCProject.SCREEN_FOLDER);
        monitor.worked(1);
        // b2Prj.createFolder(newProject, monitor, ICGCProject.SCRIPTS_FOLDER);
        // monitor.worked(1);
        b2Prj.createFolder(newProject, monitor, ICGCProject.TEXTURES_FOLDER);
        monitor.worked(1);
        b2Prj.createFolder(newProject, monitor, ICGCProject.TMP_FOLDER);
        monitor.worked(1);
        monitor.done();
      }
    };

    try {
      getContainer().run(true, false, op);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      return false;
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   * org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
  }

}