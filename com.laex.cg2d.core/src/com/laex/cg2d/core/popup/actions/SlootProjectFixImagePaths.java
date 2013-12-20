package com.laex.cg2d.core.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.laex.cg2d.core.Activator;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGResourceFile;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;

public class SlootProjectFixImagePaths implements IObjectActionDelegate {

  private List<IProject> selectedResources;
  private IWorkbenchPart targetPart;
  protected int totalWork;

  static class EntitiesFixer {

    static class SlootProjectVisitor implements IResourceVisitor {

      private IProgressMonitor monitor;

      public SlootProjectVisitor(IProgressMonitor monitor) {
        this.monitor = monitor;
      }

      @Override
      public boolean visit(IResource resource) throws CoreException {
        try {

          if (resource.getType() != IResource.FILE) {
            return true;
          }

          if (CGCProject.getInstance().isEntityFile((IFile) resource))
            fixEntity(resource);

          if (CGCProject.getInstance().isScreenFile((IFile) resource))
            fixScreen(resource);

        } catch (IOException e) {
          Activator.log(e);
        }

        return true;
      }

      private void fixEntity(IResource resource) throws CoreException, IOException {
        IFile entityFile = (IFile) resource;

        CGEntity ent = CGEntity.parseFrom(entityFile.getContents());
        monitor.subTask(entityFile.getName());

        CGEntity.Builder entBuilder = ent.toBuilder();

        for (CGEntityAnimation.Builder ea : entBuilder.getAnimationsBuilderList()) {
          CGResourceFile.Builder ssFile = ea.getSpritesheetFileBuilder();
          fixImagePath(ssFile);
          ea.setSpritesheetFile(ssFile.build());

          ssFile = ea.getFixtureFileBuilder();
          fixImagePath(ssFile);
          ea.setFixtureFile(ssFile.build());

          ssFile = ea.getSpritesheetJsonFileBuilder();
          fixImagePath(ssFile);
          ea.setSpritesheetJsonFile(ssFile.build());
        }

        // Save
        ent = entBuilder.build();
        ByteArrayInputStream bais = new ByteArrayInputStream(ent.toByteArray());
        entityFile.setContents(bais, true, false, new NullProgressMonitor());
        monitor.worked(1);
      }

      private void fixScreen(IResource resource) throws CoreException, IOException {
        IFile screenFile = (IFile) resource;

        CGScreenModel screenModel = CGScreenModel.parseFrom(screenFile.getContents());
        CGScreenModel.Builder screenModelBuilder = CGScreenModel.newBuilder(screenModel);

        for (CGLayer.Builder layer : screenModelBuilder.getLayersBuilderList()) {
          for (CGShape.Builder shape : layer.getShapeBuilderList()) {
            CGResourceFile.Builder resBldr = shape.getEntityRefFile().toBuilder();

            if (resBldr.getResourceFile().isEmpty()) {
              continue;
            }

            fixImagePath(resBldr);
            shape.setEntityRefFile(resBldr.build());
          }
        }

        screenModel = screenModelBuilder.build();

        // Save
        ByteArrayInputStream bais = new ByteArrayInputStream(screenModel.toByteArray());
        screenFile.setContents(bais, true, false, new NullProgressMonitor());
      }

      private void fixImagePath(CGResourceFile.Builder ssFile) {
        if (ssFile.getResourceFile().isEmpty()) {
          return;
        }

        IPath p = new Path(ssFile.getResourceFile());
        IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(p);

        ssFile.setResourceFileAbsolute(f.getLocation().toString());

        Activator.log("Fixed image path - " + ssFile.getResourceFileAbsolute());
      }
    }

    public static void fix(IProgressMonitor monitor, List<IProject> projects) throws CoreException {
      SlootProjectVisitor spv = new EntitiesFixer.SlootProjectVisitor(monitor);
      for (IProject p : projects) {
        p.accept(spv);
      }
    }

  }

  public SlootProjectFixImagePaths() {
  }

  @Override
  public void run(IAction action) {
    if (selectedResources.size() <= 0) {
      return;
    }

    ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.targetPart.getSite().getShell());
    try {

      calculateWork();
      pmd.run(false, true, new IRunnableWithProgress() {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
          monitor.beginTask("Fixing absolute paths", totalWork);
          try {
            SlootProjectFixImagePaths.EntitiesFixer.fix(monitor, selectedResources);
          } catch (CoreException e) {
            Activator.log(e);
          }
          monitor.done();
        }
      });
    } catch (InvocationTargetException e) {
      Activator.log(e);
    } catch (InterruptedException e) {
      Activator.log(e);
    } catch (CoreException e1) {
      Activator.log(e1);
    }

  }

  private void calculateWork() throws CoreException {
    for (IProject p : selectedResources) {
      p.accept(new IResourceVisitor() {
        @Override
        public boolean visit(IResource resource) throws CoreException {
          if (resource.getType() == IResource.FILE) {

            if (CGCProject.getInstance().isEntityFile((IFile) resource)) {
              totalWork++;
            }

            if (CGCProject.getInstance().isScreenFile((IFile) resource)) {
              totalWork++;
            }

          }
          return false;
        }
      });
    }
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    selectedResources = (List<IProject>) strucSel.toList();
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    this.targetPart = targetPart;
  }

}
