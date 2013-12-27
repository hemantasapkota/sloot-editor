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
package com.laex.cg2d.screeneditor.handlers;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.adapter.PreferenceConstants;
import com.laex.cg2d.screeneditor.Activator;
import com.laex.cg2d.screeneditor.prefs.InternalPrefs;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RenderHandler extends AbstractHandler {
  /**
   * The constructor.
   */
  public RenderHandler() {
  }

  /**
   * Validate.
   * 
   * @param shell
   *          the shell
   */
  private void validate(final Shell shell) {
    String runnerFile = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.RUNNER);
    if (StringUtils.isEmpty(runnerFile)) {
      shell.getDisplay().asyncExec(new Runnable() {
        @Override
        public void run() {
          MessageBox mb = new MessageBox(shell, SWT.ERROR);
          mb.setMessage("The runner JAR file has not been specified. Please ensure a runner is specified in global preferences.");
          mb.setText("Runner file not specified");
          mb.open();
        }
      });
    }
  }

  /**
   * the command has been executed, so extract extract the needed information
   * from the application context.
   * 
   * @param event
   *          the event
   * @return the object
   * @throws ExecutionException
   *           the execution exception
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    final IEditorPart editorPart = window.getActivePage().getActiveEditor();
    
    editorPart.doSave(new NullProgressMonitor());

    validate(window.getShell());

    // Eclipse Jobs API
    final Job job = new Job("Render Game") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {

        if (monitor.isCanceled()) {
          return Status.CANCEL_STATUS;
        }

        try {
          IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();

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
          while (scn.hasNext() && !monitor.isCanceled()) {

            if (monitor.isCanceled()) {
              throw new InterruptedException("Cancelled");
            }

            log.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, scn.nextLine()));
          }
          monitor.worked(5);

          monitor.done();
          this.done(Status.OK_STATUS);
          return Status.OK_STATUS;

        } catch (RuntimeException e) {
          return handleException(e);
        } catch (IOException e) {
          return handleException(e);
        } catch (CoreException e) {
          return handleException(e);
        } catch (InterruptedException e) {
          return handleException(e);
        }
      }

      private IStatus handleException(Exception e) {
        Activator.log(e);
        return Status.CANCEL_STATUS;
      }
    };

    job.setUser(true);
    job.setPriority(Job.INTERACTIVE);
    job.schedule();

    return null;
  };

  /**
   * Builds the runner command from properties.
   * 
   * @param screenFile
   *          the screen file
   * @param controllerFile
   *          the controller file
   * @return the string[]
   * @throws CoreException
   *           the core exception
   */
  private String[] buildRunnerCommandFromProperties(String screenFile, String controllerFile) throws CoreException {
    StringBuilder cmd = new StringBuilder("java -jar ");

    cmd.append(InternalPrefs.gameRunner());
    cmd.append(" -screenFile ").append(screenFile);
    cmd.append(" -screenController ").append(controllerFile);

    Status renderStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, cmd.toString());
    Activator.getDefault().getLog().log(renderStatus);

    return new String[]
      { "java", "-jar", InternalPrefs.gameRunner(), "-screenFile", screenFile, "-screenController", controllerFile };
  }
}