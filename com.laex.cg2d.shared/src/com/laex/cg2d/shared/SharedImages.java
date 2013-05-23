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
package com.laex.cg2d.shared;

import org.eclipse.jface.resource.ImageDescriptor;

import com.laex.cg2d.shared.activator.Activator;

/**
 * The Class SharedImages.
 */
public final class SharedImages {

  /** The Constant BOX. */
  public static final ImageDescriptor BOX = get("/resources/images/box.png");

  /** The Constant CIRCLE. */
  public static final ImageDescriptor CIRCLE = get("/resources/images/circle.png");

  /** The Constant TRIANGLE. */
  public static final ImageDescriptor TRIANGLE = get("/resources/images/triangle.png");

  /** The Constant PENTAGON. */
  public static final ImageDescriptor PENTAGON = get("/resources/images/pentagon.png");

  /** The Constant HEXAGON. */
  public static final ImageDescriptor HEXAGON = get("/resources/images/pentagon.png");

  /** The Constant OCTAGON. */
  public static final ImageDescriptor OCTAGON = get("/resources/images/pentagon.png");

  /** The Constant CLONE_TOOL. */
  public static final ImageDescriptor CLONE_TOOL = get("/resources/images/clone_tool.png");

  /** The Constant JOINT. */
  public static final ImageDescriptor JOINT = get("/resources/images/joint.png");

  /** The Constant PROPERTIES. */
  public static final ImageDescriptor PROPERTIES = get("/resources/images/prop.png");

  /** The Constant RENDER. */
  public static final ImageDescriptor RENDER = get("/resources/images/98_14.png");

  /** The Constant TOGGLE_GRID. */
  public static final ImageDescriptor TOGGLE_GRID = get("/resources/images/togglegrid.png");

  /** The Constant RENDER_PROTO. */
  public static final ImageDescriptor RENDER_PROTO = get("/resources/images/renderProto.png");
  
  public static final ImageDescriptor PNG_EXPORT = get("/resources/images/png_14.png");

  /** The Constant ADD_ITEM_SMALL. */
  public static final ImageDescriptor ADD_ITEM_SMALL = get("/resources/images/112_14.png");
  
  /** The Constant ADD_ITEM_2_SMALL. */
  public static final ImageDescriptor ADD_ITEM_2_SMALL = get("/resources/images/Add_20.png");

  /** The Constant REMOVE_ITEM_SMALL. */
  public static final ImageDescriptor REMOVE_ITEM_SMALL = get("/resources/images/115_14.png");

  /** The Constant CHANGE_ITEM_SMALL. */
  public static final ImageDescriptor CHANGE_ITEM_SMALL = get("/resources/images/2_16.png");

  /** The Constant BUTTON_UP. */
  public static final ImageDescriptor BUTTON_UP = get("/resources/images/Button_Up_16.png");

  /** The Constant BUTTON_DOWN. */
  public static final ImageDescriptor BUTTON_DOWN = get("/resources/images/Button_Down_16.png");

  /** The Constant PHYSICS_BODY_EDITOR_LOGO_SMALL. */
  public static final ImageDescriptor PHYSICS_BODY_EDITOR_LOGO_SMALL = get("/resources/images/physBodEdLogoSmall.png");

  /** The Constant YES. */
  public static final ImageDescriptor YES = get("/resources/images/boolYes.png");

  /** The Constant NO. */
  public static final ImageDescriptor NO = get("/resources/images/boolNo.png");

  /** The Constant LAYER. */
  public static final ImageDescriptor LAYER = get("/resources/images/52_15.png");

  /** The Constant EYE_VISIBLE. */
  public static final ImageDescriptor EYE_VISIBLE = get("/resources/images/eye_16.png");

  /** The Constant EYE_INVISIBLE. */
  public static final ImageDescriptor EYE_INVISIBLE = get("/resources/images/eyeinvisible_16.png");

  /** The Constant LOCKED. */
  public static final ImageDescriptor LOCKED = get("/resources/images/37_14.png");

  /** The Constant UNLOCKED. */
  public static final ImageDescriptor UNLOCKED = get("/resources/images/39_14.png");

  /** The Constant AB_BG. */
  public static final ImageDescriptor AB_BG = get("/resources/images/INGAME_BIRDS.png");

  /**
   * Gets the.
   * 
   * @param path
   *          the path
   * @return the image descriptor
   */
  private static final ImageDescriptor get(String path) {
    return ResourceManager.getImageDescriptor(Activator.class, path);
  }

}
