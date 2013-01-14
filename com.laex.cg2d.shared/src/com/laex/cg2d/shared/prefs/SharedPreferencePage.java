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
package com.laex.cg2d.shared.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.laex.cg2d.shared.activator.Activator;

/**
 * The Class SharedPreferencePage.
 */
public class SharedPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  /**
   * Create the preference page.
   */
  public SharedPreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
  }

  /**
   * Initialize the preference page.
   * 
   * @param workbench
   *          the workbench
   */
  public void init(IWorkbench workbench) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
   */
  @Override
  protected void createFieldEditors() {
    addField(new FileFieldEditor(PreferenceConstants.RUNNER, "&Runner", getFieldEditorParent()));
  }

}
