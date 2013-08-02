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
package com.laex.cg2d.screeneditor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.Shape;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  /** The Constant PLUGIN_ID. */
  public static final String PLUGIN_ID = "com.laex.cg2d.screeneditor"; //$NON-NLS-1$
  
  private Map<Shape, Entity> shapeToEntitiesMap = new HashMap<Shape, Entity>();

  // The shared instance
  /** The plugin. */
  private static Activator plugin;

  /**
   * The constructor.
   */
  public Activator() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
   * )
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
   * )
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }
  
  
  public Map<Shape, Entity> getShapeToEntitiesMap() {
    return shapeToEntitiesMap;
  }

}
