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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;

/**
 * The Interface ICGCProject.
 */
public interface ICGCProject {

  /** The screen folder. */
  String SCREEN_FOLDER = "/screens";

  /** The scripts folder. */
  String SCRIPTS_FOLDER = "/scripts";

  /** The textures folder. */
  String TEXTURES_FOLDER = "/textures";

  /** The tmp folder. */
  String TMP_FOLDER = "/tmp";

  /** The entities folder. */
  String ENTITIES_FOLDER = "/entities";

  /** The screen extension. */
  String SCREEN_EXTENSION = "cgs";

  /** The globals extension. */
  String GLOBALS_EXTENSION = "globals";

  /** The script extension. */
  String SCRIPT_EXTENSION = "lua";

  /** The png extension. */
  String PNG_EXTENSION = "png";

  /** The entities extension. */
  String ENTITIES_EXTENSION = "cge";

  /**
   * Creates the project.
   * 
   * @param monitor
   *          the monitor
   * @param projectName
   *          the project name
   * @return the i project
   * @throws CoreException
   *           the core exception
   */
  IProject createProject(IProgressMonitor monitor, String projectName) throws CoreException;

  /**
   * Creates the folder.
   * 
   * @param project
   *          the project
   * @param monitor
   *          the monitor
   * @param which
   *          the which
   * @throws CoreException
   *           the core exception
   */
  void createFolder(IProject project, IProgressMonitor monitor, String which) throws CoreException;

  /**
   * Creates the file.
   * 
   * @param project
   *          the project
   * @param inWhichFolder
   *          the in which folder
   * @param filename
   *          the filename
   * @param source
   *          the source
   * @return the i file
   * @throws CoreException
   *           the core exception
   */
  IFile createFile(IProject project, String inWhichFolder, String filename, ByteArrayInputStream source)
      throws CoreException;

  /**
   * Gets the textures folder.
   * 
   * @param fileEditorInput
   *          the file editor input
   * @return the textures folder
   * @throws CoreException
   *           the core exception
   */
  IFolder getTexturesFolder(IEditorInput fileEditorInput) throws CoreException;

  /**
   * Gets the entitites folder.
   * 
   * @param fileEditorInput
   *          the file editor input
   * @return the entitites folder
   * @throws CoreException
   *           the core exception
   */
  IFolder getEntititesFolder(IEditorInput fileEditorInput) throws CoreException;

  /**
   * Gets the screens folder.
   * 
   * @param fileEditorInput
   *          the file editor input
   * @return the screens folder
   * @throws CoreException
   *           the core exception
   */
  IFolder getScreensFolder(IEditorInput fileEditorInput) throws CoreException;

  /**
   * Gets the current project.
   * 
   * @param fileEditorInput
   *          the file editor input
   * @return the current project
   */
  IProject getCurrentProject(IEditorInput fileEditorInput);

  /**
   * Gets the file contents.
   * 
   * @param fileEditorInput
   *          the file editor input
   * @param path
   *          the path
   * @return the file contents
   * @throws CoreException
   *           the core exception
   */
  InputStream getFileContents(IEditorInput fileEditorInput, IPath path) throws CoreException;

  /**
   * Creates the file.
   * 
   * @param path
   *          the path
   * @param source
   *          the source
   * @return the i file
   * @throws CoreException
   *           the core exception
   */
  IFile createFile(IPath path, ByteArrayInputStream source) throws CoreException;

  /**
   * Delete file.
   * 
   * @param file
   *          the file
   */
  void deleteFile(IFile file);

  /**
   * Exists.
   * 
   * @param pathToResource
   *          the path to resource
   * @param isProject
   *          the is project
   * @return true, if successful
   */
  boolean exists(IPath pathToResource, boolean isProject);

  /**
   * Checks if is entity file.
   * 
   * @param resource
   *          the resource
   * @return true, if is entity file
   */
  boolean isEntityFile(IFile resource);

  /**
   * Checks if is screen file.
   * 
   * @param resource
   *          the resource
   * @return true, if is screen file
   */
  boolean isScreenFile(IFile resource);

}
