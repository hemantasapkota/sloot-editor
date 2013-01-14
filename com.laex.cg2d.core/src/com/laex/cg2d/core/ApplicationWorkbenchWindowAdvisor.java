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

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * The Class ApplicationWorkbenchWindowAdvisor.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

  /**
   * Instantiates a new application workbench window advisor.
   * 
   * @param configurer
   *          the configurer
   */
  public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    super(configurer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor
   * (org.eclipse.ui.application.IActionBarConfigurer)
   */
  public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
    return new ApplicationActionBarAdvisor(configurer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
   */
  public void preWindowOpen() {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.setInitialSize(new Point(801, 550));
    configurer.setShowCoolBar(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
   */
  @Override
  public void postWindowOpen() {
    super.postWindowOpen();
    getWindowConfigurer().getWindow().getShell().setMaximized(true);
  }

}
