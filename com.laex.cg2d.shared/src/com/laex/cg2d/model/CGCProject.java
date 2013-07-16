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
package com.laex.cg2d.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.model.activator.Activator;

/**
 * The Class CGCProject.
 */
public class CGCProject implements ICGCProject {

  /** The instance. */
  private static ICGCProject instance;

  /** The Constant PROJECT_TYPE. */
  public static final String PROJECT_TYPE = "cg2d-project";

  /** The Constant PROJECT_QUALIFIER. */
  public static final QualifiedName PROJECT_QUALIFIER = new QualifiedName("", "project-type");

  /**
   * Instantiates a new cGC project.
   */
  private CGCProject() {
  }

  /**
   * Gets the single instance of CGCProject.
   * 
   * @return single instance of CGCProject
   */
  public static ICGCProject getInstance() {
    if (instance == null) {
      instance = new CGCProject();
    }
    return instance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#createProject(org.eclipse.core.runtime
   * .IProgressMonitor, java.lang.String)
   */
  @Override
  public IProject createProject(IProgressMonitor monitor, String projectName) throws CoreException {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject project = root.getProject(projectName);

    if (project.exists()) {
      return project;
    }

    project.create(monitor);
    project.open(monitor);
    project.setPersistentProperty(PROJECT_QUALIFIER, PROJECT_TYPE);
    project.setDefaultCharset("UTF-8", monitor);
    return project;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#createFolder(org.eclipse.core.resources
   * .IProject, org.eclipse.core.runtime.IProgressMonitor, java.lang.String)
   */
  @Override
  public void createFolder(IProject project, IProgressMonitor monitor, String which) throws CoreException {
    IFolder folder = null;
    boolean valid = isEntitiesFolder(which) || isMapsFolder(which) || isScriptsFolder(which) || isTexturesFolder(which)
        || isTmpFolder(which);
    if (!valid) {
      throw new IllegalArgumentException();
    }
    folder = project.getFolder(which);
    if (folder.exists()) {
      return;
    }
    folder.create(true, true, monitor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#createFile(org.eclipse.core.resources.
   * IProject, java.lang.String, java.lang.String, java.io.ByteArrayInputStream)
   */
  @Override
  public IFile createFile(IProject project, String inWhichFolder, String filename, final ByteArrayInputStream source)
      throws CoreException {

    StringBuffer sb = new StringBuffer();
    sb.append(inWhichFolder).append('/').append(filename);
    Path path = new Path(sb.toString());
    final IFile file = project.getFile(path);

    if (file.exists()) {
      deleteFile(file);
    }

    file.create(source, false, null);
    file.setCharset("UTF-8", null);

    return file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#createFile(org.eclipse.core.runtime.IPath,
   * java.io.ByteArrayInputStream)
   */
  @Override
  public IFile createFile(IPath path, final ByteArrayInputStream source) throws CoreException {
    final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
    if (!file.exists()) {
      file.create(source, true, null);
    }
    return file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#getCurrentProject(org.eclipse.ui.IEditorInput
   * )
   */
  @Override
  public IProject getCurrentProject(IEditorInput fileEditorInput) {
    return ((FileEditorInput) fileEditorInput).getFile().getProject();
  }

  /**
   * Checks if is textures folder.
   * 
   * @param which
   *          the which
   * @return true, if is textures folder
   */
  private boolean isTexturesFolder(String which) {
    return TEXTURES_FOLDER.equals(which);
  }

  /**
   * Checks if is scripts folder.
   * 
   * @param which
   *          the which
   * @return true, if is scripts folder
   */
  private boolean isScriptsFolder(String which) {
    return SCRIPTS_FOLDER.equals(which);
  }

  /**
   * Checks if is entities folder.
   * 
   * @param which
   *          the which
   * @return true, if is entities folder
   */
  private boolean isEntitiesFolder(String which) {
    return ENTITIES_FOLDER.equals(which);
  }

  /**
   * Checks if is maps folder.
   * 
   * @param which
   *          the which
   * @return true, if is maps folder
   */
  private boolean isMapsFolder(String which) {
    return SCREEN_FOLDER.equals(which);
  }

  /**
   * Checks if is tmp folder.
   * 
   * @param which
   *          the which
   * @return true, if is tmp folder
   */
  private boolean isTmpFolder(String which) {
    return TMP_FOLDER.equals(which);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#getFileContents(org.eclipse.ui.IEditorInput
   * , org.eclipse.core.runtime.IPath)
   */
  @Override
  public InputStream getFileContents(IEditorInput fileEditorInput, IPath path) throws CoreException {
    IFile file = getCurrentProject(fileEditorInput).getFile(path);
    return file.getContents();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#getTexturesFolder(org.eclipse.ui.IEditorInput
   * )
   */
  @Override
  public IFolder getTexturesFolder(IEditorInput fileEditorInput) throws CoreException {
    IProject project = getCurrentProject(fileEditorInput);
    IFolder texturesFolder = project.getFolder(TEXTURES_FOLDER);
    if (!texturesFolder.exists()) {
      IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Textures folder does not exist.");
      throw new CoreException(status);
    }
    return texturesFolder;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#exists(org.eclipse.core.runtime.IPath,
   * boolean)
   */
  @Override
  public boolean exists(IPath pathToResource, boolean isProject) {
    if (isProject) {
      for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
        if (p.getFullPath().toOSString().equals(pathToResource.toOSString())) {
          return true;
        }
      }
    } else {
      // check for the file
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(pathToResource);
      if (file.exists()) {
        return true;
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#getEntititesFolder(org.eclipse.ui.IEditorInput
   * )
   */
  @Override
  public IFolder getEntititesFolder(IEditorInput fileEditorInput) throws CoreException {
    IProject project = getCurrentProject(fileEditorInput);
    IFolder entFolder = project.getFolder(ENTITIES_FOLDER);
    if (!entFolder.exists()) {
      throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Textures folder doesn't exists"));
    }
    return entFolder;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#getScreensFolder(org.eclipse.ui.IEditorInput
   * )
   */
  @Override
  public IFolder getScreensFolder(IEditorInput fileEditorInput) throws CoreException {
    IProject project = getCurrentProject(fileEditorInput);
    IFolder entFolder = project.getFolder(SCREEN_FOLDER);
    if (!entFolder.exists()) {
      throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Screen folder doesn't exists"));
    }
    return entFolder;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#deleteFile(org.eclipse.core.resources.
   * IFile)
   */
  @Override
  public void deleteFile(final IFile file) {
    // Delete existing file
    WorkspaceModifyOperation wmo = new WorkspaceModifyOperation() {
      @Override
      protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
          InterruptedException {
        file.delete(true, monitor);
      }
    };

    try {
      wmo.run(null);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#isEntityFile(org.eclipse.core.resources
   * .IFile)
   */
  @Override
  public boolean isEntityFile(IFile resource) {
    if (resource.getName().endsWith(ENTITIES_EXTENSION)) {
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ICGCProject#isScreenFile(org.eclipse.core.resources
   * .IFile)
   */
  @Override
  public boolean isScreenFile(IFile resource) {
    if (resource.getName().endsWith(SCREEN_EXTENSION)) {
      return true;
    }
    return false;
  }

}
