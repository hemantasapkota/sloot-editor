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
package com.laex.cg2d.screeneditor.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGCardPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGDebugDrawPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGWorldPreferences;
import com.laex.cg2d.model.adapter.PreferenceConstants;
import com.laex.cg2d.screeneditor.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
   * initializeDefaultPreferences()
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    store.setDefault(PreferenceConstants.RUNNER, "");

    store.setDefault(PreferenceConstants.DRAW_BODIES, defaultBodiesFlag());
    store.setDefault(PreferenceConstants.DRAW_JOINT, defaultJointsFlag());
    store.setDefault(PreferenceConstants.DRAW_DEBUG_DATA, defaultDebugDataFlag());
    store.setDefault(PreferenceConstants.DRAW_ENTITIES, defaultEntitiesFlag());
    store.setDefault(PreferenceConstants.INSTALL_MOUSE_JOINT, defaultMouseJoint());
    store.setDefault(PreferenceConstants.DRAW_AABB, defaultAABBFlag());
    store.setDefault(PreferenceConstants.DRAW_INACTIVE_BODIES, defaultInactiveBodiesFlag());

    store.setDefault(PreferenceConstants.PTM_RATIO, defaultPTMRatio());
    store.setDefault(PreferenceConstants.GRAVITY_X, defaultGravityX());
    store.setDefault(PreferenceConstants.GRAVITY_Y, defaultGravityY());
    store.setDefault(PreferenceConstants.VELOCITY_ITERATIONS, defaultVelocityIterations());
    store.setDefault(PreferenceConstants.POSITION_ITERATIONS, defaultPositionIterations());
    store.setDefault(PreferenceConstants.TIMESTEP, defaultTimeStep());

    store.setDefault(PreferenceConstants.CARD_WIDTH, defaultCardWidth());
    store.setDefault(PreferenceConstants.CARD_HEIGHT, defaultCardHeight());
    store.setDefault(PreferenceConstants.CARD_NO_X, defaultCardNoX());
    store.setDefault(PreferenceConstants.CARD_NO_Y, defaultCardNoY());
  }

  /**
   * Default screen prefs.
   * 
   * @return the cG screen preferences
   */
  public static CGScreenPreferences defaultScreenPrefs() {
    CGCardPreferences.Builder cardBuildr = CGCardPreferences.newBuilder().setCardHeight(defaultCardHeight())
        .setCardWidth(defaultCardWidth()).setCardNoX(defaultCardNoX()).setCardNoY(defaultCardNoY());

    CGWorldPreferences.Builder worldBuildr = CGWorldPreferences.newBuilder().setGravityX(defaultGravityX())
        .setGravityY(defaultGravityY()).setPtmRatio(defaultPTMRatio())
        .setPositionIterations(defaultPositionIterations()).setTimeStep(defaultTimeStep())
        .setVelocityIterations(defaultVelocityIterations());

    CGDebugDrawPreferences.Builder dbgBuildr = CGDebugDrawPreferences.newBuilder().setDrawAABB(defaultAABBFlag())
        .setDrawBodies(defaultBodiesFlag()).setDrawDebugData(defaultDebugDataFlag())
        .setDrawEntities(defaultEntitiesFlag()).setDrawInactiveBodies(defaultInactiveBodiesFlag())
        .setDrawJoints(defaultJointsFlag()).setInstallMouseJoint(defaultMouseJoint());

    return CGScreenPreferences.newBuilder().setCardPrefs(cardBuildr.build()).setDebugDrawPrefs(dbgBuildr.build())
        .setWorldPrefs(worldBuildr.build()).build();

  }

  /**
   * Default card width.
   * 
   * @return the integer
   */
  public static Integer defaultCardWidth() {
    return 480;
  }

  /**
   * Default card height.
   * 
   * @return the integer
   */
  public static Integer defaultCardHeight() {
    return 320;
  }

  /**
   * Default card no x.
   * 
   * @return the integer
   */
  public static Integer defaultCardNoX() {
    return 1;
  }

  /**
   * Default card no y.
   * 
   * @return the integer
   */
  public static Integer defaultCardNoY() {
    return 1;
  }

  /**
   * Default ptm ratio.
   * 
   * @return the integer
   */
  public static Integer defaultPTMRatio() {
    return 16;
  }

  /**
   * Default gravity x.
   * 
   * @return the float
   */
  public static Float defaultGravityX() {
    return 0.0f;
  }

  /**
   * Default gravity y.
   * 
   * @return the float
   */
  public static Float defaultGravityY() {
    return -9.0f;
  }

  /**
   * Default velocity iterations.
   * 
   * @return the integer
   */
  public static Integer defaultVelocityIterations() {
    return 200;
  }

  /**
   * Default time step.
   * 
   * @return the float
   */
  public static Float defaultTimeStep() {
    return 60.0f;
  }

  /**
   * Default position iterations.
   * 
   * @return the integer
   */
  public static Integer defaultPositionIterations() {
    return 100;
  }

  /**
   * Dedfault time step.
   * 
   * @return the float
   */
  public static Float dedfaultTimeStep() {
    return 60.0f;
  }

  /**
   * Default bodies flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultBodiesFlag() {
    return true;
  }

  /**
   * Default joints flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultJointsFlag() {
    return true;
  }

  /**
   * Default entities flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultEntitiesFlag() {
    return true;
  }

  /**
   * Default debug data flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultDebugDataFlag() {
    return true;
  }

  /**
   * Default mouse joint.
   * 
   * @return the boolean
   */
  public static Boolean defaultMouseJoint() {
    return true;
  }

  /**
   * Default aabb flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultAABBFlag() {
    return false;
  }

  /**
   * Default inactive bodies flag.
   * 
   * @return the boolean
   */
  public static Boolean defaultInactiveBodiesFlag() {
    return true;
  }

}
