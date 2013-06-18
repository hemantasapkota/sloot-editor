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
package com.laex.cg2d.screeneditor.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.googlecode.protobuf.format.JsonFormat;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;

/**
 * The Class ConvertToJsonAction.
 */
public class ConvertToJsonAction implements IObjectActionDelegate {

  /** The shell. */
  private Shell shell;

  /** The struc sel. */
  private IStructuredSelection strucSel;

  /**
   * Constructor for Action1.
   */
  public ConvertToJsonAction() {
    super();
  }

  /**
   * Sets the active part.
   * 
   * @param action
   *          the action
   * @param targetPart
   *          the target part
   * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    shell = targetPart.getSite().getShell();
  }

  /**
   * Run.
   * 
   * @param action
   *          the action
   * @see IActionDelegate#run(IAction)
   */
  public void run(IAction action) {
    if (!action.isEnabled()) {
      return;
    }

    if (strucSel.isEmpty()) {
      return;
    }

    Iterator itr = strucSel.iterator();

    ICGCProject cgcPrj = CGCProject.getInstance();

    while (itr.hasNext()) {
      IFile file = (IFile) itr.next();

      String jsonFormat = "";

      if (cgcPrj.isScreenFile(file)) {

        CGScreenModel model = null;
        try {
          model = CGScreenModel.parseFrom(file.getContents());
        } catch (IOException e1) {
          e1.printStackTrace();
        } catch (CoreException e1) {
          e1.printStackTrace();
        }

        if (model == null) {
          // todo: log this result
          return;
        }

        // Print it
        jsonFormat = JsonFormat.printToString(model);
      } else if (cgcPrj.isEntityFile(file)) {
        CGEntity entity = null;

        try {
          entity = CGEntity.parseFrom(file.getContents());
        } catch (IOException e) {
          e.printStackTrace();
        } catch (CoreException e) {
          e.printStackTrace();
        }

        if (entity == null) {
          // todo log this result
          return;
        }

        jsonFormat = JsonFormat.printToString(entity);
      }

      // Save it
      IPath path = file.getParent().getFullPath().append(file.getName()).addFileExtension("json");
      IFile file1 = file.getProject().getFile(path);
      ICGCProject prj = CGCProject.getInstance();

      if (prj.exists(path, false)) {

        MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL);
        mb.setText("File exists");
        mb.setMessage("File already exists. Will be overrridden. Do you want to continue ?");
        int resp = mb.open();
        if (resp == SWT.CANCEL) {
          return;
        }

        try {
          file1.delete(true, null);
        } catch (CoreException e) {
          e.printStackTrace();
        }

//        createFile(jsonFormat, path, prj);

      } else {

        createFile(jsonFormat, path, prj);

      }

    }

  }

  /**
   * Creates the file.
   *
   * @param jsonFormat the json format
   * @param path the path
   * @param prj the prj
   */
  private void createFile(String jsonFormat, IPath path, ICGCProject prj) {
    try {
      prj.createFile(path, new ByteArrayInputStream(jsonFormat.getBytes("utf-8")));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Selection changed.
   * 
   * @param action
   *          the action
   * @param selection
   *          the selection
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    strucSel = (IStructuredSelection) selection;
  }

}
