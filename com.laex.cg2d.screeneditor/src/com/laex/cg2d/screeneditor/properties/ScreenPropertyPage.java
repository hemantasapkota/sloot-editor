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
package com.laex.cg2d.screeneditor.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import com.laex.cg2d.shared.prefs.PreferenceConstants;
import com.laex.cg2d.shared.prefs.PreferenceInitializer;
import com.laex.cg2d.shared.util.BooleanUtil;
import com.laex.cg2d.shared.util.FloatUtil;
import com.laex.cg2d.shared.util.IntegerUtil;
import com.laex.cg2d.shared.util.PlatformUtil;
import com.laex.cg2d.shared.util.ScreenPropertiesUtil;

/**
 * The Class ScreenPropertyPage.
 */
public class ScreenPropertyPage extends PropertyPage {

  /**
   * The Class SpinnerChecker.
   */
  private final class SpinnerChecker implements ModifyListener {

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    @Override
    public void modifyText(ModifyEvent e) {
      Spinner spn = (Spinner) e.getSource();
      try {
        Integer.valueOf(spn.getText().trim());
        setMessage(null);
        setValid(true);
      } catch (NumberFormatException ex) {
        setValid(false);
      }
    }

  }

  /**
   * The Class FloatChecker.
   */
  private final class FloatChecker implements ModifyListener, FocusListener {
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    @Override
    public void modifyText(ModifyEvent e) {
      Text text = (Text) e.getSource();
      try {
        Float.valueOf(text.getText().trim());
        setMessage(null);
        setValid(true);
      } catch (NumberFormatException ex) {
        setValid(false);
        setMessage("Value must a floating point number.", DialogPage.ERROR);
      }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent e) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void focusLost(FocusEvent e) {
      if (!isValid()) {
        Text t = (Text) e.getSource();
        t.setText("0.0");
      }
      setMessage(null);
    }
  }

  /** The float checker. */
  final FloatChecker floatChecker = new FloatChecker();
  
  /** The int checker. */
  final SpinnerChecker intChecker = new SpinnerChecker();

  /** The composite_2. */
  private Composite composite_2;
  
  /** The txt ptm. */
  private Spinner txtPTM;
  
  /** The txt time step. */
  private Text txtTimeStep;
  
  /** The txt gravity x. */
  private Text txtGravityX;
  
  /** The txt gravity y. */
  private Text txtGravityY;
  
  /** The txt velocity itr. */
  private Spinner txtVelocityItr;
  
  /** The txt pos itr. */
  private Spinner txtPosItr;
  
  /** The btn bodies. */
  private Button btnBodies;
  
  /** The btn joints. */
  private Button btnJoints;
  
  /** The btn aabb. */
  private Button btnAabb;
  
  /** The btn inactive modies. */
  private Button btnInactiveModies;
  
  /** The btn install mouse joint. */
  private Button btnInstallMouseJoint;
  
  /** The btn draw debug data. */
  private Button btnDrawDebugData;
  
  /** The btn draw entities. */
  private Button btnDrawEntities;
  
  /** The grp card settings. */
  private Group grpCardSettings;
  
  /** The txt card no x. */
  private Spinner txtCardNoX;
  
  /** The txt card no y. */
  private Spinner txtCardNoY;

  /** The txt card width. */
  private Spinner txtCardWidth;

  /** The txt card height. */
  private Spinner txtCardHeight;

  /**
   * Constructor for SamplePropertyPage.
   */
  public ScreenPropertyPage() {
    super();
  }

  /**
   * Populate properties.
   *
   * @throws CoreException the core exception
   */
  private void populateProperties() throws CoreException {
    // Populate owner text field
    IResource res = (IResource) getElement();

    Map<String, String> props = ScreenPropertiesUtil.getScreenProperties(res);

    btnAabb.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_AABB)));
    btnBodies.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_BODIES)));
    btnJoints.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_JOINT)));
    btnDrawDebugData.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_DEBUG_DATA)));
    btnDrawEntities.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_ENTITIES)));
    btnInactiveModies.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.DRAW_INACTIVE_BODIES)));
    btnInstallMouseJoint.setSelection(BooleanUtil.toBool(props.get(PreferenceConstants.INSTALL_MOUSE_JOINT)));

    txtPTM.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.PTM_RATIO)));
    txtVelocityItr.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.VELOCITY_ITERATIONS)));
    txtPosItr.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.VELOCITY_ITERATIONS)));

    txtGravityX.setText(props.get(PreferenceConstants.GRAVITY_X));
    txtGravityY.setText(props.get(PreferenceConstants.GRAVITY_Y));
    txtTimeStep.setText(props.get(PreferenceConstants.TIMESTEP));

    txtCardNoX.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.CARD_NO_X)));
    txtCardNoY.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.CARD_NO_Y)));
    txtCardWidth.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.CARD_WIDTH)));
    txtCardHeight.setSelection(IntegerUtil.toInt(props.get(PreferenceConstants.CARD_HEIGHT)));
  }

  /**
   * Creates the contents.
   *
   * @param parent the parent
   * @return the control
   * @see PreferencePage#createContents(Composite)
   */
  protected Control createContents(Composite parent) {
    composite_2 = new Composite(parent, SWT.NONE);
    GridLayout gl_composite_2 = new GridLayout();
    composite_2.setLayout(gl_composite_2);
    GridData data = new GridData(GridData.FILL);
    data.grabExcessHorizontalSpace = true;
    composite_2.setLayoutData(data);

    Group grpDebugDraw = new Group(composite_2, SWT.NONE);
    grpDebugDraw.setLayout(new GridLayout(5, false));
    grpDebugDraw.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    grpDebugDraw.setText("Debug Draw");

    btnBodies = new Button(grpDebugDraw, SWT.CHECK);
    btnBodies.setText("Bodies");

    btnJoints = new Button(grpDebugDraw, SWT.CHECK);
    btnJoints.setText("Joints");

    btnAabb = new Button(grpDebugDraw, SWT.CHECK);
    btnAabb.setText("AABB");

    btnInactiveModies = new Button(grpDebugDraw, SWT.CHECK);
    btnInactiveModies.setText("Inactive Bodies");

    btnInstallMouseJoint = new Button(grpDebugDraw, SWT.CHECK);
    btnInstallMouseJoint.setText("Mouse Joint");

    btnDrawDebugData = new Button(grpDebugDraw, SWT.CHECK);
    btnDrawDebugData.setText("Draw Debug Data");

    btnDrawEntities = new Button(grpDebugDraw, SWT.CHECK);
    btnDrawEntities.setText("Draw Entities");
    new Label(grpDebugDraw, SWT.NONE);
    new Label(grpDebugDraw, SWT.NONE);
    new Label(grpDebugDraw, SWT.NONE);

    Group grpWorldSettings = new Group(composite_2, SWT.NONE);
    grpWorldSettings.setLayout(new GridLayout(2, false));
    GridData gd_grpWorldSettings = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_grpWorldSettings.widthHint = 458;
    grpWorldSettings.setLayoutData(gd_grpWorldSettings);
    grpWorldSettings.setText("World Settings");

    Label label = new Label(grpWorldSettings, SWT.NONE);
    label.setText("PTM Ratio");

    txtPTM = new Spinner(grpWorldSettings, SWT.BORDER);
    txtPTM.setMaximum(1000);
    GridData gd_txtPTM = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txtPTM.widthHint = 356;
    txtPTM.setLayoutData(gd_txtPTM);
    txtPTM.addModifyListener(intChecker);

    Label lblVelocityItr = new Label(grpWorldSettings, SWT.NONE);
    lblVelocityItr.setText("Velocity Itr.");

    txtVelocityItr = new Spinner(grpWorldSettings, SWT.BORDER);
    txtVelocityItr.setMaximum(1000);
    txtVelocityItr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtVelocityItr.addModifyListener(intChecker);

    Label lblPositionItr = new Label(grpWorldSettings, SWT.NONE);
    lblPositionItr.setText("Position Itr.");

    txtPosItr = new Spinner(grpWorldSettings, SWT.BORDER);
    txtPosItr.setMaximum(1000);
    txtPosItr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtPosItr.addModifyListener(intChecker);

    Label label_5 = new Label(grpWorldSettings, SWT.NONE);
    label_5.setText("TimeStep");

    txtTimeStep = new Text(grpWorldSettings, SWT.BORDER);
    txtTimeStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtTimeStep.addModifyListener(floatChecker);
    txtTimeStep.addFocusListener(floatChecker);

    Label label_1 = new Label(grpWorldSettings, SWT.NONE);
    label_1.setText("Gravity X");

    txtGravityX = new Text(grpWorldSettings, SWT.BORDER);
    txtGravityX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtGravityX.addModifyListener(floatChecker);
    txtGravityX.addFocusListener(floatChecker);

    Label label_6 = new Label(grpWorldSettings, SWT.NONE);
    label_6.setText("Gravity Y");

    txtGravityY = new Text(grpWorldSettings, SWT.BORDER);
    txtGravityY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtGravityY.addModifyListener(floatChecker);
    txtGravityY.addFocusListener(floatChecker);

    grpCardSettings = new Group(composite_2, SWT.NONE);
    grpCardSettings.setLayout(new GridLayout(2, false));
    grpCardSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    grpCardSettings.setText("Card Settings");

    Label lblCardNoX = new Label(grpCardSettings, SWT.NONE);
    lblCardNoX.setText("Card No X");

    txtCardNoX = new Spinner(grpCardSettings, SWT.BORDER);
    txtCardNoX.setMaximum(2000);
    txtCardNoX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtCardNoX.addModifyListener(intChecker);

    Label lblCardNoY = new Label(grpCardSettings, SWT.NONE);
    lblCardNoY.setText("Card No Y");

    txtCardNoY = new Spinner(grpCardSettings, SWT.BORDER);
    txtCardNoY.setMaximum(2000);
    txtCardNoY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtCardNoY.addModifyListener(intChecker);

    Label lblWidth = new Label(grpCardSettings, SWT.NONE);
    lblWidth.setText("Card Width");

    txtCardWidth = new Spinner(grpCardSettings, SWT.BORDER);
    txtCardWidth.setMaximum(2000);
    txtCardWidth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtCardWidth.addModifyListener(intChecker);

    Label lblCardHeight = new Label(grpCardSettings, SWT.NONE);
    lblCardHeight.setText("Card Height");

    txtCardHeight = new Spinner(grpCardSettings, SWT.BORDER);
    txtCardHeight.setMaximum(2000);
    txtCardHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtCardHeight.addModifyListener(intChecker);

    //
    try {
      populateProperties();
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return composite_2;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
   */
  protected void performDefaults() {
    btnBodies.setSelection(PreferenceInitializer.defaultBodiesFlag());
    btnJoints.setSelection(PreferenceInitializer.defaultJointsFlag());
    btnDrawDebugData.setSelection(PreferenceInitializer.defaultDebugDataFlag());
    btnDrawEntities.setSelection(PreferenceInitializer.defaultEntitiesFlag());
    btnInactiveModies.setSelection(PreferenceInitializer.defaultInactiveBodiesFlag());
    btnInstallMouseJoint.setSelection(PreferenceInitializer.defaultMouseJoint());

    txtPTM.setSelection(PreferenceInitializer.defaultPTMRatio());
    txtGravityX.setText(FloatUtil.toString(PreferenceInitializer.defaultGravityX()));
    txtGravityY.setText(FloatUtil.toString(PreferenceInitializer.defaultGravityY()));
    txtPosItr.setSelection(PreferenceInitializer.defaultPositionIterations());
    txtVelocityItr.setSelection(PreferenceInitializer.defaultVelocityIterations());

    txtCardNoX.setSelection(PreferenceInitializer.defaultCardNoX());
    txtCardNoY.setSelection(PreferenceInitializer.defaultCardNoY());
    txtCardWidth.setSelection(PreferenceInitializer.defaultCardWidth());
    txtCardHeight.setSelection(PreferenceInitializer.defaultCardHeight());

    //
    applyChanges(constructMap());

    super.performDefaults();
  }

  /**
   * Apply changes.
   *
   * @param props the props
   */
  private void applyChanges(Map<String, String> props) {
    // Update changes to the active screen editor
    if (PlatformUtil.isScreenEditorActive()) {
      PlatformUtil.getScreenLayerManager().updateCardLayer(txtCardNoX.getSelection(), txtCardNoY.getSelection(),
          txtCardWidth.getSelection(), txtCardHeight.getSelection());

      PlatformUtil.getScreenPropertyManager().updateScreenProperties(props);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.preference.PreferencePage#performOk()
   */
  public boolean performOk() {
    // store the value in the owner text field
    IResource res = (IResource) getElement();
    Map<String, String> props = constructMap();

    try {
      ScreenPropertiesUtil.persistScreenProperties(res, props);
      applyChanges(props);
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return true;
  }

  /**
   * Construct map.
   *
   * @return the map
   */
  private Map<String, String> constructMap() {
    String bodies = BooleanUtil.toString(btnBodies.getSelection());
    String joints = BooleanUtil.toString(btnJoints.getSelection());
    String aabb = BooleanUtil.toString(btnAabb.getSelection());
    String debugData = BooleanUtil.toString(btnDrawDebugData.getSelection());
    String entiites = BooleanUtil.toString(btnDrawEntities.getSelection());
    String inactiveBodies = BooleanUtil.toString(btnInactiveModies.getSelection());
    String mouseJoint = BooleanUtil.toString(btnInstallMouseJoint.getSelection());

    String ptmRatio = txtPTM.getText();
    String timeStep = txtTimeStep.getText();
    String gravityX = txtGravityX.getText();
    String gravityY = txtGravityY.getText();
    String velocityItr = txtVelocityItr.getText();
    String posItr = txtPosItr.getText();

    String cardNoX = txtCardNoX.getText();
    String cardNoY = txtCardNoY.getText();
    String cardWidth = txtCardWidth.getText();
    String cardHeight = txtCardHeight.getText();

    Map<String, String> props = new HashMap<String, String>();

    props.put(PreferenceConstants.DRAW_BODIES, bodies);
    props.put(PreferenceConstants.DRAW_JOINT, joints);
    props.put(PreferenceConstants.DRAW_AABB, aabb);
    props.put(PreferenceConstants.DRAW_DEBUG_DATA, debugData);
    props.put(PreferenceConstants.DRAW_INACTIVE_BODIES, inactiveBodies);
    props.put(PreferenceConstants.INSTALL_MOUSE_JOINT, mouseJoint);
    props.put(PreferenceConstants.DRAW_ENTITIES, entiites);

    props.put(PreferenceConstants.PTM_RATIO, ptmRatio);
    props.put(PreferenceConstants.TIMESTEP, timeStep);
    props.put(PreferenceConstants.GRAVITY_X, gravityX);
    props.put(PreferenceConstants.GRAVITY_Y, gravityY);
    props.put(PreferenceConstants.POSITION_ITERATIONS, velocityItr);
    props.put(PreferenceConstants.VELOCITY_ITERATIONS, posItr);

    props.put(PreferenceConstants.CARD_NO_X, cardNoX);
    props.put(PreferenceConstants.CARD_NO_Y, cardNoY);
    props.put(PreferenceConstants.CARD_WIDTH, cardWidth);
    props.put(PreferenceConstants.CARD_HEIGHT, cardHeight);
    return props;
  }
}