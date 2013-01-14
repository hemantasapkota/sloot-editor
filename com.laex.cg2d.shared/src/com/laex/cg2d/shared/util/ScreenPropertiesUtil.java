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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import com.laex.cg2d.shared.prefs.PreferenceConstants;

/**
 * The Class ScreenPropertiesUtil.
 */
public class ScreenPropertiesUtil {

  /**
   * Gets the card properties.
   * 
   * @param res
   *          the res
   * @return the card properties
   * @throws CoreException
   *           the core exception
   */
  public static Map<String, String> getCardProperties(IResource res) throws CoreException {
    String cardNoX = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_X));
    String cardNoY = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_Y));
    String cardWidth = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_WIDTH));
    String cardHeight = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_HEIGHT));

    Map<String, String> props = new HashMap<String, String>();

    props.put(PreferenceConstants.CARD_NO_X, cardNoX);
    props.put(PreferenceConstants.CARD_NO_Y, cardNoY);
    props.put(PreferenceConstants.CARD_WIDTH, cardWidth);
    props.put(PreferenceConstants.CARD_HEIGHT, cardHeight);

    return props;
  }

  /**
   * Gets the screen properties.
   * 
   * @param res
   *          the res
   * @return the screen properties
   * @throws CoreException
   *           the core exception
   */
  public static Map<String, String> getScreenProperties(IResource res) throws CoreException {

    String aabb = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_AABB));
    String bodies = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_BODIES));
    String joints = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_JOINT));
    String drawDebugData = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_DEBUG_DATA));
    String drawEntities = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_ENTITIES));
    String drawInactiveBodies = res.getPersistentProperty(new QualifiedName("",
        PreferenceConstants.DRAW_INACTIVE_BODIES));
    String installMouseJoint = res
        .getPersistentProperty(new QualifiedName("", PreferenceConstants.INSTALL_MOUSE_JOINT));

    String gravityX = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.GRAVITY_X));
    String gravityY = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.GRAVITY_Y));
    String timeStep = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.TIMESTEP));

    String ptmRatio = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.PTM_RATIO));
    String velocityIterations = res
        .getPersistentProperty(new QualifiedName("", PreferenceConstants.VELOCITY_ITERATIONS));
    String positionIterations = res
        .getPersistentProperty(new QualifiedName("", PreferenceConstants.POSITION_ITERATIONS));

    String cardNoX = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_X));
    String cardNoY = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_Y));
    String cardWidth = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_WIDTH));
    String cardHeight = res.getPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_HEIGHT));

    Map<String, String> props = new HashMap<String, String>();

    props.put(PreferenceConstants.DRAW_BODIES, bodies);
    props.put(PreferenceConstants.DRAW_JOINT, joints);
    props.put(PreferenceConstants.DRAW_AABB, aabb);
    props.put(PreferenceConstants.DRAW_DEBUG_DATA, drawDebugData);
    props.put(PreferenceConstants.DRAW_INACTIVE_BODIES, drawInactiveBodies);
    props.put(PreferenceConstants.INSTALL_MOUSE_JOINT, installMouseJoint);
    props.put(PreferenceConstants.DRAW_ENTITIES, drawEntities);

    props.put(PreferenceConstants.PTM_RATIO, ptmRatio);
    props.put(PreferenceConstants.TIMESTEP, timeStep);
    props.put(PreferenceConstants.GRAVITY_X, gravityX);
    props.put(PreferenceConstants.GRAVITY_Y, gravityY);
    props.put(PreferenceConstants.POSITION_ITERATIONS, velocityIterations);
    props.put(PreferenceConstants.VELOCITY_ITERATIONS, positionIterations);

    props.put(PreferenceConstants.CARD_NO_X, cardNoX);
    props.put(PreferenceConstants.CARD_NO_Y, cardNoY);
    props.put(PreferenceConstants.CARD_WIDTH, cardWidth);
    props.put(PreferenceConstants.CARD_HEIGHT, cardHeight);

    return props;

  }

  /**
   * Persist screen properties.
   * 
   * @param res
   *          the res
   * @param props
   *          the props
   * @throws CoreException
   *           the core exception
   */
  public static void persistScreenProperties(IResource res, Map<String, String> props) throws CoreException {
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_BODIES),
        props.get(PreferenceConstants.DRAW_BODIES));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_JOINT),
        props.get(PreferenceConstants.DRAW_JOINT));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_AABB),
        props.get(PreferenceConstants.DRAW_AABB));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_DEBUG_DATA),
        props.get(PreferenceConstants.DRAW_DEBUG_DATA));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_ENTITIES),
        props.get(PreferenceConstants.DRAW_ENTITIES));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.DRAW_INACTIVE_BODIES),
        props.get(PreferenceConstants.DRAW_INACTIVE_BODIES));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.INSTALL_MOUSE_JOINT),
        props.get(PreferenceConstants.INSTALL_MOUSE_JOINT));

    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.PTM_RATIO),
        props.get(PreferenceConstants.PTM_RATIO));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.TIMESTEP),
        props.get(PreferenceConstants.TIMESTEP));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.GRAVITY_X),
        props.get(PreferenceConstants.GRAVITY_X));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.GRAVITY_Y),
        props.get(PreferenceConstants.GRAVITY_Y));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.VELOCITY_ITERATIONS),
        props.get(PreferenceConstants.VELOCITY_ITERATIONS));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.POSITION_ITERATIONS),
        props.get(PreferenceConstants.POSITION_ITERATIONS));

    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_X),
        props.get(PreferenceConstants.CARD_NO_X));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_NO_Y),
        props.get(PreferenceConstants.CARD_NO_Y));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_WIDTH),
        props.get(PreferenceConstants.CARD_WIDTH));
    res.setPersistentProperty(new QualifiedName("", PreferenceConstants.CARD_HEIGHT),
        props.get(PreferenceConstants.CARD_HEIGHT));
  }

}
