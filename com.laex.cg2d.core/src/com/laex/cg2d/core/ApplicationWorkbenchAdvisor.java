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
package com.laex.cg2d.core;

import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.osgi.framework.Bundle;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

  /* (non-Javadoc)
   * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
   */
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    return new ApplicationWorkbenchWindowAdvisor(configurer);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
   */
  @Override
  public void initialize(IWorkbenchConfigurer configurer) {
    super.initialize(configurer);
    IDE.registerAdapters();
    hackForShowingProjectIconInRCPApplications(configurer);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
   */
  public String getInitialWindowPerspectiveId() {
    return Perspective.ID;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.application.WorkbenchAdvisor#getDefaultPageInput()
   */
  @Override
  public IAdaptable getDefaultPageInput() {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    return workspace.getRoot();
  }
  
  /**
   * Hack taken from
   * <url>http://francisu.wordpress.com/2008/05/27/magic-required
   * -to-use-the-common-navigator-in-an-rcp-application/</url> Hopefully it will
   * be solved on Indigo.
   *
   * @param configurer the configurer
   */
  private void hackForShowingProjectIconInRCPApplications(IWorkbenchConfigurer configurer) {
    final String ICONS_PATH = "icons/full/";
    final String PATH_OBJECT = ICONS_PATH + "obj16/";
    Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
    declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT, PATH_OBJECT + "prj_obj.gif", true);
    declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, PATH_OBJECT + "cprj_obj.gif",
        true);
  }

  /**
   * Declare workbench image.
   *
   * @param configurer_p the configurer_p
   * @param ideBundle the ide bundle
   * @param symbolicName the symbolic name
   * @param path the path
   * @param shared the shared
   */
  private void declareWorkbenchImage(IWorkbenchConfigurer configurer_p, Bundle ideBundle, String symbolicName,
      String path, boolean shared) {
    URL url = ideBundle.getEntry(path);
    ImageDescriptor desc = ImageDescriptor.createFromURL(url);
    configurer_p.declareImage(symbolicName, desc, shared);
  }

}
