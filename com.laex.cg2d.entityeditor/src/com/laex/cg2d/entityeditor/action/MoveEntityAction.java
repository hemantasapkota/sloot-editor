package com.laex.cg2d.entityeditor.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class MoveEntityAction implements IObjectActionDelegate {

  private IWorkbenchPart targetPart;
  private Object[] selectedFiles;

  public MoveEntityAction() {
  }

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

    List<CGEntity> models = makeEntityModelsFromSelection(selectedFiles);

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

  private void copyEntityModels(List<CGEntity> models, IProject destinationProject) {
    for (CGEntity emodel : models) {

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

      IPath ePath = entitiesFolderPath.append(e.getInternalName()).addFileExtension(ICGCProject.ENTITIES_EXTENSION);
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ePath);
      ByteArrayInputStream bais = new ByteArrayInputStream(e.toByteArray());

      try {

        if (file.exists()) {
          file.setContents(bais, true, false, new NullProgressMonitor());
        } else {
          file.create(bais, true, new NullProgressMonitor());
        }

      } catch (CoreException ce) {
        ce.printStackTrace();
      }

    }
  }

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

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    selectedFiles = strucSel.toArray();

  }

  private List<CGEntity> makeEntityModelsFromSelection(Object[] selecObjects) {
    List<CGEntity> entityModels = new ArrayList<ScreenModel.CGEntity>();
    for (Object sel : selecObjects) {
      IFile file = (IFile) sel;

      try {
        CGEntity entity = CGEntity.parseFrom(file.getContents());
        entityModels.add(entity);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }

    return entityModels;
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    this.targetPart = targetPart;
  }

}
