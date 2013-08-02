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

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.entityeditor.EntityFormEditor;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;

/**
 * The Class NewEntityWizard.
 */
public class NewEntityWizard extends Wizard implements INewWizard {

  /** The page. */
  private NewEntityPage page;

  /** The path to project. */
  private IPath pathToProject;

  /**
   * Instantiates a new new entity wizard.
   */
  public NewEntityWizard() {
    setNeedsProgressMonitor(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages() {
    if (pathToProject != null) {
      page = new NewEntityPage(pathToProject);
    } else {
      page = new NewEntityPage();
    }
    addPage(page);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   * org.eclipse.jface.viewers.IStructuredSelection)
   */
  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    if (selection.isEmpty()) {
      return;
    }
    IResource ires = (IResource) selection.getFirstElement();
    IPath path = null;
    switch (ires.getType()) {
    case IResource.FILE:
      path = ires.getFullPath().removeLastSegments(1);
      break;
    default:
      path = ires.getFullPath();
    }
    pathToProject = path;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish() {
    final IPath path = page.getPathToProject();
    final String filename = page.getFileName();

    WorkspaceModifyOperation wop = new WorkspaceModifyOperation() {
      @Override
      protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
          InterruptedException {
        monitor.beginTask("Create entities file", 1);

        IPath npath = path.append(filename);
        npath = npath.addFileExtension(ICGCProject.ENTITIES_EXTENSION);

        ICGCProject b2dMgr = CGCProject.getInstance();

        // default entity model
        Entity entityModel = EntityAdapter.newDefaultEntity(filename);
        CGEntity cgEntity = EntityAdapter.asCGEntity(entityModel);
        ByteArrayInputStream bios = new ByteArrayInputStream(cgEntity.toByteArray());
        IFile file = b2dMgr.createFile(npath, bios);

        // open the file
        IEditorInput edInp = new FileEditorInput(file);
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(edInp, EntityFormEditor.ID);
        monitor.worked(1);
        monitor.done();
      }
    };

    try {
      getContainer().run(false, false, wop);
      return true;
    } catch (InvocationTargetException e) {
      Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage()));
    } catch (InterruptedException e) {
      Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage()));
    }

    return false;
  }
}
