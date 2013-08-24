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
package com.laex.cg2d.entityeditor.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntity.Builder;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGResourceFile;
import com.laex.cg2d.model.model.ResourceFile;
import com.laex.cg2d.model.util.EntitiesUtil;

/**
 * The Class MoveEntityAction.
 */
public class MoveEntityAction implements IObjectActionDelegate {

  /** The target part. */
  private IWorkbenchPart targetPart;
  
  /** The selected files. */
  private Object[] selectedFiles;

  /**
   * Instantiates a new move entity action.
   */
  public MoveEntityAction() {
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
   */
  @Override
  public void run(IAction action) {
    if (!action.isEnabled()) {
      return;
    }

    ListSelectionDialog dlg = new ListSelectionDialog(targetPart.getSite().getShell(), ResourcesPlugin.getWorkspace()
        .getRoot(), new BaseWorkbenchContentProvider(), new WorkbenchLabelProvider(), "Select the Project:");
    dlg.setTitle("Project Selection");
    int resp = dlg.open();
    if (resp == ContainerSelectionDialog.CANCEL | (dlg.getResult() == null || dlg.getResult().length == 0)) {
      return;
    }

    Map<String, CGEntity> models = makeEntityModelsFromSelection(selectedFiles);

    IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject();

    for (Object dp : dlg.getResult()) {
      /* Destination path is the path to the projects in the workspace */
      IProject destinationProject = (IProject) dp;

      /* Dont copy if the destination project is same as source project */
      if (dp != prj) {
        copyEntityModels(models, destinationProject);
      }

    }

  }

  /**
   * Copy entity models.
   *
   * @param models the models
   * @param destinationProject the destination project
   */
  private void copyEntityModels(Map<String, CGEntity> models, IProject destinationProject) {
    for (String entityName : models.keySet()) {
      
      CGEntity emodel = models.get(entityName);

      IPath entitiesFolderPath = destinationProject.getFullPath().append(ICGCProject.ENTITIES_FOLDER);
      IPath texturesFolderPath = destinationProject.getFullPath().append(ICGCProject.TEXTURES_FOLDER);

      Builder newEntityBuilder = CGEntity.newBuilder(emodel);

      /*
       * Step 1: Go through all the animations and copy the animation sprite
       * sheet file and fixture file.
       */
      for (int i = 0; i < emodel.getAnimationsCount(); i++) {

        CGEntityAnimation animToCopy = emodel.getAnimations(i);

        CGResourceFile spriteSheetResourceFile = animToCopy.getSpritesheetFile();

        CGResourceFile fixtureRsourceFile = animToCopy.getFixtureFile();

        /* Work with sprite sheet path */
        IPath spriteSheetPath = copyAnimationResource(texturesFolderPath, spriteSheetResourceFile);
        CGResourceFile.Builder spriteSheetResourceBuilder = CGResourceFile.newBuilder(animToCopy.getSpritesheetFile());

        /*
         * If sprite sheet path is null, then most likely fixture file will also
         * be. So just move on to next animation
         */
        if (spriteSheetPath == null) {
          continue;
        }

        String spriteSheetPathAbsolute = ResourcesPlugin.getWorkspace().getRoot().getFile(spriteSheetPath)
            .getLocation().makeAbsolute().toString();
        spriteSheetResourceBuilder.setResourceFile(spriteSheetPath.toString());
        spriteSheetResourceBuilder.setResourceFileAbsolute(spriteSheetPathAbsolute);

        /* Work with fixture file path */
        IPath fixtureFilePath = copyAnimationResource(texturesFolderPath, fixtureRsourceFile);
        CGResourceFile.Builder fixtureResourceBuilder = CGResourceFile.newBuilder(animToCopy.getFixtureFile());

        if (fixtureFilePath != null) {
          String fixturePathAbsolute = ResourcesPlugin.getWorkspace().getRoot().getFile(fixtureFilePath).getLocation()
              .makeAbsolute().toString();

          fixtureResourceBuilder.setResourceFile(fixtureFilePath.toString());
          fixtureResourceBuilder.setResourceFileAbsolute(fixturePathAbsolute);
        }

        CGEntityAnimation finalAnimation = newEntityBuilder.getAnimationsBuilder(i)
            .setSpritesheetFile(spriteSheetResourceBuilder.build())
            .setFixtureFile(fixtureResourceBuilder.build()).build();

        newEntityBuilder.setAnimations(i, finalAnimation);

      }

      /* Looks like all the animation and fixtures have been copied */
      CGEntity e = newEntityBuilder.build();

      IPath ePath = entitiesFolderPath.append(entityName).addFileExtension(ICGCProject.ENTITIES_EXTENSION);
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ePath);
      ByteArrayInputStream bais = new ByteArrayInputStream(e.toByteArray());

      try {

        if (file.exists()) {
          file.setContents(bais, true, false, new NullProgressMonitor());
        } else {
          file.create(bais, true, new NullProgressMonitor());
        }

      } catch (CoreException ce) {
        Activator.log(ce);
      }

    }
  }

  /**
   * Copy animation resource.
   *
   * @param destinationPath the destination path
   * @param resourceFile the resource file
   * @return the i path
   */
  private IPath copyAnimationResource(IPath destinationPath, CGResourceFile resourceFile) {
    if (ResourceFile.isEmpty(resourceFile)) {
      return null;
    }

    IPath srcResPath = new Path(resourceFile.getResourceFile());

    IFile srcResFile = ResourcesPlugin.getWorkspace().getRoot().getFile(srcResPath);

    IPath destPath = destinationPath.removeLastSegments(1).append("textures").append(srcResPath.lastSegment());

    if (srcResFile.exists()) {

      try {

        boolean destFileAlreadyExists = ResourcesPlugin.getWorkspace().getRoot().getFile(destPath).exists();

        if (!destFileAlreadyExists) {

          srcResFile.copy(destPath, true, new NullProgressMonitor());

        }

      } catch (CoreException e) {

        Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage()));

      }
    }

    return destPath;

  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
   */
  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    selectedFiles = strucSel.toArray();

  }

  /**
   * Make entity models from selection.
   *
   * @param selecObjects the selec objects
   * @return the map
   */
  private Map<String, CGEntity> makeEntityModelsFromSelection(Object[] selecObjects) {
    Map<String, CGEntity> entityModels = new HashMap<String, ScreenModel.CGEntity>();
    for (Object sel : selecObjects) {
      IFile file = (IFile) sel;

      try {
        CGEntity entity = CGEntity.parseFrom(file.getContents());
        String entityName = EntitiesUtil.getDisplayName(file.getFullPath());
        System.err.println(entityName);
        entityModels.put(entityName, entity);
      } catch (IOException e) {
        Activator.log(e);
      } catch (CoreException e) {
        Activator.log(e);
      }
    }

    return entityModels;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
   */
  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    this.targetPart = targetPart;
  }

}
