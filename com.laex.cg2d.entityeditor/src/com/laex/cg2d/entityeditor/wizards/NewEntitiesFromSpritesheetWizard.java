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
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.EntityAnimation;
import com.laex.cg2d.model.model.EntitySpritesheetItem;

/**
 * The Class NewEntityWizard.
 */
public class NewEntitiesFromSpritesheetWizard extends Wizard implements INewWizard {

  /** The page. */
  private NewEntitiesFromSpritesheetPage2 finalPage;

  /** The spritesheet page. */
  private NewEntitiesFromSpritesheetPage spritesheetPage;

  /** The path to project. */
  private IPath pathToProject;

  /** The resource container. */
  private IResource resourceContainer;

  /**
   * Instantiates a new new entity wizard.
   */
  public NewEntitiesFromSpritesheetWizard() {
    setNeedsProgressMonitor(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages() {

    spritesheetPage = new NewEntitiesFromSpritesheetPage(resourceContainer);

    addPage(spritesheetPage);

    if (pathToProject != null) {
      finalPage = new NewEntitiesFromSpritesheetPage2(pathToProject);
    } else {
      finalPage = new NewEntitiesFromSpritesheetPage2();
    }

    addPage(finalPage);

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
    resourceContainer = ires;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish() {
    final IPath path = finalPage.getPathToProject();
    final String filename = finalPage.getFileName();

    WorkspaceModifyOperation wop = new WorkspaceModifyOperation() {
      @Override
      protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
          InterruptedException {
        List<EntitySpritesheetItem> ssItems = spritesheetPage.getSpritesheetItems();

        monitor.beginTask("Create entities file", ssItems.size());

        for (int i = 0; i < ssItems.size(); i++) {

          IPath npath = path.append(filename + i);
          npath = npath.addFileExtension(ICGCProject.ENTITIES_EXTENSION);

          ICGCProject b2dMgr = CGCProject.getInstance();

          // default entity model
          monitor.subTask("Create " + npath.toString());
          Entity entityModel = EntityAdapter.newDefaultEntity(npath);
          
          EntityAnimation ea = entityModel.getAnimationList().get(0);
          ea.getSpritesheetItems().add(ssItems.get(i));
          ea.setSpritesheetFile(spritesheetPage.getSpritesheetFile());
          ea.setSpritesheetMapperFile(spritesheetPage.getSpritesheetJsonFile());
          
          CGEntity cgEntity = EntityAdapter.asCGEntity(entityModel);
          ByteArrayInputStream bios = new ByteArrayInputStream(cgEntity.toByteArray());
          b2dMgr.createFile(npath, bios);

          monitor.worked(1);
        }

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
