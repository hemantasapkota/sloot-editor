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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.apache.commons.collections.primitives.adapters.io.InputStreamByteIterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.screeneditor.Activator;

/**
 * The Class NewScriptWizard.
 */
public class NewScriptWizard extends Wizard implements INewWizard {

  /** The page. */
  private NewScriptPage page;

  /** The path to project. */
  private IPath pathToProject;

  /**
   * Instantiates a new new screen wizard.
   */
  public NewScriptWizard() {
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
      page = new NewScriptPage(pathToProject);
    } else {
      page = new NewScriptPage();
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

    if (!(selection.getFirstElement() instanceof IResource)) {
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
        monitor.beginTask("Create file", 1);

        IPath npath = path.append(filename);
        npath = npath.addFileExtension(ICGCProject.SCRIPT_EXTENSION);

        ICGCProject b2dMgr = CGCProject.getInstance();

        byte[] barr = null;
        try {
          InputStream io = FileLocator.openStream(Activator.getDefault().getBundle(), new Path("luaScriptTemplate.lua"), false);
          barr = IOUtils.toByteArray(io);
        } catch (IOException e1) {
          Activator.log(e1);
        }

        ByteArrayInputStream bios = new ByteArrayInputStream(barr);
        final IFile createdFile = b2dMgr.createFile(npath, bios);

        // Open the file in the editor
        getShell().getDisplay().asyncExec(new Runnable() {
          @Override
          public void run() {
            IEditorInput edInp = new FileEditorInput(createdFile);
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage page = window.getActivePage();

            try {
              page.openEditor(edInp, IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
            } catch (PartInitException e) {
              Activator.log(e);
            }

          }
        });

        //
        monitor.worked(1);
        monitor.done();
      }
    };

    try {
      getContainer().run(false, false, wop);
      return true;
    } catch (InvocationTargetException e) {
      Activator.log(e);
    } catch (InterruptedException e) {
      Activator.log(e);
    }

    return false;
  }
}
