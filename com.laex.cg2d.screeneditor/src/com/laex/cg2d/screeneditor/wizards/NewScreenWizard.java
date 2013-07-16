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

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.screeneditor.ScreenEditor;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.prefs.PreferenceInitializer;

/**
 * The Class NewScreenWizard.
 */
public class NewScreenWizard extends Wizard implements INewWizard {

  /** The page. */
  private NewScreenPage page;

  /** The path to project. */
  private IPath pathToProject;

  /**
   * Instantiates a new new screen wizard.
   */
  public NewScreenWizard() {
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
      page = new NewScreenPage(pathToProject);
    } else {
      page = new NewScreenPage();
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
        npath = npath.addFileExtension(ICGCProject.SCREEN_EXTENSION);

        ICGCProject b2dMgr = CGCProject.getInstance();

        String empty = "";
        ByteArrayInputStream bios = new ByteArrayInputStream(empty.getBytes());
        final IFile createdFile = b2dMgr.createFile(npath, bios);

        // create a simple model with a layer and save it to the file
        GameModel model = new GameModel(PreferenceInitializer.defaultScreenPrefs());
        model.getDiagram().getLayers().add(new Layer(0, "Layer1", true, false));

        CGScreenPreferences screenPrefs = PreferenceInitializer.defaultScreenPrefs();

        CGScreenModel cgGameModel = new CGScreenModelAdapter(model, screenPrefs).asCGGameModel();

        createdFile.setContents(new ByteArrayInputStream(cgGameModel.toByteArray()), true, false, monitor);

        // Open the file in the editor
        getShell().getDisplay().asyncExec(new Runnable() {
          @Override
          public void run() {
            IEditorInput edInp = new FileEditorInput(createdFile);
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage page = window.getActivePage();

            try {
              page.openEditor(edInp, ScreenEditor.ID);
            } catch (PartInitException e) {
              e.printStackTrace();
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
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return false;
  }
}
