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
package com.laex.cg2d.screeneditor.contributions;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.screeneditor.Activator;
import com.laex.cg2d.screeneditor.ScreenEditor;
import com.laex.cg2d.screeneditor.prefs.InternalPrefs;

/**
 * The Class ScreenEditorContributor.
 */
public class ScreenEditorContributor extends ActionBarContributor {

  /** The shapes editor. */
  private ScreenEditor shapesEditor;

  /** The togggle grid action. */
  private Action togggleGridAction;

  /** The grid16x16 action. */
  private Action grid16x16Action;

  /** The grid32x32 action. */
  private Action grid32x32Action;

  /** The grid custom size action. */
  private Action gridCustomSizeAction;

  /** The render action. */
  private Action renderAction;

  /**
   * Instantiates a new screen editor contributor.
   */
  public ScreenEditorContributor() {
  }

  /**
   * Creates the actions.
   */
  private void createActions() {
    togggleGridAction = new Action("Toggle") {
      public void run() {
        shapesEditor.setGridState(!shapesEditor.getGridState());
      };
    };
    togggleGridAction.setImageDescriptor(SharedImages.TOGGLE_GRID);

    renderAction = new Action("Render") {
      public void run() {
        renderAction.setEnabled(false);
        // Eclipse Jobs API
        final Job job = new Job("Render Game") {
          @Override
          protected IStatus run(IProgressMonitor monitor) {
            
            if (monitor.isCanceled()) {
              return Status.CANCEL_STATUS;
            }

            try {
              renderAction.setEnabled(false);

              IFile file = ((IFileEditorInput) shapesEditor.getEditorInput()).getFile();

              monitor.beginTask("Building rendering command", 5);
              ILog log = Activator.getDefault().getLog();

              String mapFile = file.getLocation().makeAbsolute().toOSString();
              String controllerFile = file.getLocation().removeFileExtension().addFileExtension("lua").makeAbsolute()
                  .toOSString();

              String[] command = buildRunnerCommandFromProperties(mapFile, controllerFile);
              monitor.worked(5);

              monitor.beginTask("Rendering external", 5);

              ProcessBuilder pb = new ProcessBuilder(command);
              Process p = pb.start();

              monitor.worked(4);
              Scanner scn = new Scanner(p.getErrorStream());
              while (scn.hasNext()) {
                log.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, scn.nextLine()));
              }
              monitor.worked(5);

              monitor.done();
              this.done(Status.OK_STATUS);
              renderAction.setEnabled(true);
              return Status.OK_STATUS;

            } catch (RuntimeException e) {
              return handleException(e);
            } catch (IOException e) {
              return handleException(e);
            } catch (CoreException e) {
              return handleException(e);
            }
          }

          private IStatus handleException(Exception e) {
            e.printStackTrace();
            renderAction.setEnabled(true);
            return Status.CANCEL_STATUS;
          }
        };

        job.setUser(true);
        job.setPriority(Job.INTERACTIVE);
        job.schedule();
      };
    };
    renderAction.setImageDescriptor(SharedImages.RENDER);

    grid16x16Action = new Action("16x16") {
      @Override
      public void run() {
        shapesEditor.setGridDimension(16, 16);
      }
    };

    grid32x32Action = new Action("32x32") {
      @Override
      public void run() {
        shapesEditor.setGridDimension(32, 32);
      }
    };

    gridCustomSizeAction = new Action("Custom...") {
      @Override
      public void run() {
        CustomGridSizeDialog customGridSizeDialog = new CustomGridSizeDialog(shapesEditor.getSite().getShell());
        int response = customGridSizeDialog.open();
        if (response != CustomGridSizeDialog.OK) {
          return;
        }
        shapesEditor.setGridDimension(customGridSizeDialog.getWidth(), customGridSizeDialog.getHeight());
      }
    };

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.actions.ActionBarContributor#setActiveEditor(org.eclipse
   * .ui.IEditorPart)
   */
  @Override
  public void setActiveEditor(IEditorPart targetEditor) {
    super.setActiveEditor(targetEditor);
    this.shapesEditor = (ScreenEditor) targetEditor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.
   * eclipse.jface.action.IToolBarManager)
   */
  @Override
  public void contributeToToolBar(IToolBarManager mgr) {
    mgr.add(togggleGridAction);
    mgr.add(renderAction);
    String[] zoomStrings = new String[]
      { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
    mgr.add(new ZoomComboContributionItem(getPage(), zoomStrings));

    mgr.update(true);

    super.contributeToToolBar(mgr);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToCoolBar(org.
   * eclipse.jface.action.ICoolBarManager)
   */
  @Override
  public void contributeToCoolBar(ICoolBarManager coolBarManager) {
    super.contributeToCoolBar(coolBarManager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse
   * .jface.action.IMenuManager)
   */
  @Override
  public void contributeToMenu(IMenuManager menuManager) {
    super.contributeToMenu(menuManager);

    MenuManager gridMenu = new MenuManager("Grid");
    gridMenu.add(grid16x16Action);
    gridMenu.add(grid32x32Action);
    gridMenu.add(gridCustomSizeAction);
    menuManager.insertBefore(IWorkbenchActionConstants.M_WINDOW, gridMenu);
  }

  /**
   * Builds the runner command from properties.
   * 
   * @param props
   *          the props
   * @param screenFile
   *          the screen file
   * @param controllerFile
   *          the controller file
   * @return the string
   * @throws CoreException
   *           the core exception
   */
  private String[] buildRunnerCommandFromProperties(String screenFile, String controllerFile)
      throws CoreException {
    StringBuilder cmd = new StringBuilder("java -jar ");

    cmd.append(InternalPrefs.gameRunner());
    cmd.append(" -screenFile ").append(screenFile);
    cmd.append(" -screenController ").append(controllerFile);

    Activator.getDefault().getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, cmd.toString()));

    return new String[]
      { "java", "-jar", InternalPrefs.gameRunner(), "-screenFile", screenFile, "-screenController", controllerFile };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
   */
  @Override
  protected void buildActions() {
    createActions();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
   */
  @Override
  protected void declareGlobalActionKeys() {
  }
}
