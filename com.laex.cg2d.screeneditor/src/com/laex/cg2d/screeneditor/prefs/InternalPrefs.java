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

import org.eclipse.jface.preference.IPreferenceStore;

import com.laex.cg2d.model.adapter.PreferenceConstants;
import com.laex.cg2d.screeneditor.Activator;

/**
 * The Class InternalPrefs.
 */
public class InternalPrefs {

  /**
   * Pref store.
   * 
   * @return the i preference store
   */
  private static IPreferenceStore prefStore() {
    return Activator.getDefault().getPreferenceStore();
  }

  /**
   * Game runner.
   * 
   * @return the string
   */
  public static String gameRunner() {
    return prefStore().getString(PreferenceConstants.RUNNER);
  }

}
