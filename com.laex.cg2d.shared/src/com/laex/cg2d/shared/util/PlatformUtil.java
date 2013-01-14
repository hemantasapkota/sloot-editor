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
package com.laex.cg2d.shared.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.laex.cg2d.shared.CGCProject;
import com.laex.cg2d.shared.ICGCProject;
import com.laex.cg2d.shared.ILayerManager;
import com.laex.cg2d.shared.IScreenPropertyManager;
import com.laex.cg2d.shared.adapter.EntityAdapter;
import com.laex.cg2d.shared.adapter.ShapeAdapter;
import com.laex.cg2d.shared.model.GameModel;

/**
 * The Class PlatformUtil.
 */
public final class PlatformUtil {

  /** The Constant STRING_EMPTY. */
  public static final String STRING_EMPTY = "";

  /**
   * Checks if is entity editor active.
   * 
   * @return true, if is entity editor active
   */
  public static boolean isEntityEditorActive() {
    IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (editorPart != null) {
      Object isEntityEditorObj = editorPart.getAdapter(EntityAdapter.class);
      if (isEntityEditorObj != null) {
        boolean isEntityEditor = (Boolean) isEntityEditorObj;
        if (isEntityEditor)
          return true;
      }
    }
    return false;
  }

  /**
   * Checks if is screen editor active.
   * 
   * @return true, if is screen editor active
   */
  public static boolean isScreenEditorActive() {
    IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (editorPart != null) {
      Object isShapesEditorObj = editorPart.getAdapter(ShapeAdapter.class);
      if (isShapesEditorObj != null) {
        boolean isShapeEditor = (Boolean) isShapesEditorObj;
        if (isShapeEditor)
          return true;
      }
    }
    return false;
  }

  // use in conjunction with isScreenEditorActive
  /**
   * Gets the screen layer manager.
   * 
   * @return the screen layer manager
   */
  public static ILayerManager getScreenLayerManager() {
    return (ILayerManager) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
  }

  /**
   * Gets the screen property manager.
   * 
   * @return the screen property manager
   */
  public static IScreenPropertyManager getScreenPropertyManager() {
    return (IScreenPropertyManager) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor();
  }

  /**
   * Gets the screen model.
   * 
   * @return the screen model
   */
  public static GameModel getScreenModel() {
    return (GameModel) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
        .getAdapter(GameModel.class);
  }

  /**
   * Gets the active editor input.
   * 
   * @return the active editor input
   */
  public static IEditorInput getActiveEditorInput() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
  }

  /**
   * Gets the list of screens in current project.
   * 
   * @param input
   *          the input
   * @return the list of screens in current project
   * @throws CoreException
   *           the core exception
   */
  public static List<IResource> getListOfScreensInCurrentProject(IEditorInput input) throws CoreException {
    ICGCProject b2Prj = CGCProject.getInstance();
    IFolder folder = b2Prj.getScreensFolder(input);
    final List<IResource> resList = new ArrayList<IResource>();

    folder.accept(new IResourceVisitor() {
      @Override
      public boolean visit(IResource re) throws CoreException {

        if (re.getName().endsWith(ICGCProject.SCREEN_EXTENSION)) {
          resList.add(re);
        }

        return true;
      }
    });

    return resList;
  }

  /**
   * Save proto.
   * 
   * @param monitor
   *          the monitor
   * @param file
   *          the file
   * @param byteInputStream
   *          the byte input stream
   * @throws CoreException
   *           the core exception
   */
  public static void saveProto(IProgressMonitor monitor, IFile file, ByteArrayInputStream byteInputStream)
      throws CoreException {
    file.setContents(byteInputStream, true, false, monitor);
  }

  /**
   * Open container resource dialog.
   * 
   * @param shell
   *          the shell
   * @param root
   *          the root
   * @param title
   *          the title
   * @return the container selection dialog
   */
  public static ContainerSelectionDialog openContainerResourceDialog(Shell shell, IWorkspaceRoot root, String title) {
    ContainerSelectionDialog csd = new ContainerSelectionDialog(shell, root, false, "Select a project");
    int response = csd.open();
    if (response == ContainerSelectionDialog.CANCEL) {
      return null;
    }
    return csd;
  }

  /**
   * Standard method.
   * 
   * @param resource
   *          the resource
   * @return the string
   */
  public static String resourceString(IResource resource) {
    return resource.getFullPath().toOSString();
  }

  /**
   * Resource string absolute.
   * 
   * @param resource
   *          the resource
   * @return the string
   */
  public static String resourceStringAbsolute(IResource resource) {
    return resource.getLocation().makeAbsolute().toOSString();
  }

  /**
   * Creates the image.
   * 
   * @param id
   *          the id
   * @return the image
   */
  public static Image createImage(final ImageData id) {
    ImageDescriptor idd = new ImageDescriptor() {
      @Override
      public ImageData getImageData() {
        return id;
      }
    };
    return idd.createImage();
  }

  /**
   * Gets the image descriptor.
   * 
   * @param i
   *          the i
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(final Image i) {
    return new ImageDescriptor() {
      @Override
      public ImageData getImageData() {
        return i.getImageData();
      }
    };
  }

  /**
   * Gets the image descriptor.
   * 
   * @param i
   *          the i
   * @param maximumSize
   *          the maximum size
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(final Image i, final Point maximumSize) {
    return new ImageDescriptor() {
      @Override
      public ImageData getImageData() {
        if (i.getBounds().width >= maximumSize.x && i.getBounds().height >= maximumSize.y) {
          return i.getImageData().scaledTo(maximumSize.x, maximumSize.y);
        }

        return i.getImageData();
      }
    };

  }
}
