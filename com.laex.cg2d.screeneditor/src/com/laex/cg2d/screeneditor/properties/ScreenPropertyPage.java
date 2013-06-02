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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
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

import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGCardPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGDebugDrawPreferences;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences.CGWorldPreferences;
import com.laex.cg2d.model.util.FloatUtil;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.prefs.PreferenceInitializer;

/**
 * The Class ScreenPropertyPage.
 */
public class ScreenPropertyPage extends PropertyPage {

  /**
   * The Class SpinnerChecker.
   */
  private final class SpinnerChecker implements ModifyListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
     * .ModifyEvent)
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
     * .ModifyEvent)
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events
     * .FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events
     * .FocusEvent)
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

  private void populateProperties() throws IOException, CoreException {
    IFile res = (IFile) getElement();

    CGScreenModel screenModel = CGScreenModel.parseFrom(res.getContents());

    CGCardPreferences cf = screenModel.getScreenPrefs().getCardPrefs();
    CGWorldPreferences wf = screenModel.getScreenPrefs().getWorldPrefs();
    CGDebugDrawPreferences df = screenModel.getScreenPrefs().getDebugDrawPrefs();

    btnAabb.setSelection(df.getDrawAABB());
    btnBodies.setSelection(df.getDrawBodies());
    btnJoints.setSelection(df.getDrawJoints());
    btnDrawDebugData.setSelection(df.getDrawDebugData());
    btnDrawEntities.setSelection(df.getDrawEntities());
    btnInactiveModies.setSelection(df.getDrawInactiveBodies());
    btnInstallMouseJoint.setSelection(df.getInstallMouseJoint());

    txtPTM.setSelection(wf.getPtmRatio());
    txtVelocityItr.setSelection(wf.getVelocityIterations());
    txtPosItr.setSelection(wf.getPositionIterations());

    txtGravityX.setText(FloatUtil.toString(wf.getGravityX()));
    txtGravityY.setText(FloatUtil.toString(wf.getGravityY()));
    txtTimeStep.setText(FloatUtil.toString(wf.getTimeStep()));
    txtCardNoX.setMinimum(1);

    txtCardNoX.setSelection(cf.getCardNoX());
    txtCardNoY.setSelection(cf.getCardNoY());
    txtCardWidth.setSelection(cf.getCardWidth());
    txtCardHeight.setSelection(cf.getCardHeight());
  }

  /**
   * Creates the contents.
   * 
   * @param parent
   *          the parent
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

    try {
      populateProperties();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return composite_2;
  }

  /*
   * (non-Javadoc)
   * 
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
    try {
      applyChanges(constructPrefs());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }

    super.performDefaults();
  }

  /**
   * Apply changes.
   * 
   * @param props
   *          the props
   * @throws CoreException 
   * @throws IOException 
   */
  private void applyChanges(CGScreenPreferences prefs) throws IOException, CoreException {
    // Update changes to the active screen editor
    if (ScreenEditorUtil.isScreenEditorActive()) {
      ScreenEditorUtil.getScreenLayerManager().updateCardLayer(txtCardNoX.getSelection(), txtCardNoY.getSelection(),
          txtCardWidth.getSelection(), txtCardHeight.getSelection());

      ScreenEditorUtil.getScreenPropertyManager().updateScreenProperties(prefs);
    } else {
      //Editor is not active. Persist the properties, by loading the model and saving it again.
      IFile file = (IFile) getElement();
      CGScreenModel model = CGScreenModel.parseFrom(file.getContents());
      
      CGScreenModel updatedModel = CGScreenModel.newBuilder(model).setScreenPrefs(constructPrefs()).build();
      file.setContents(new ByteArrayInputStream(updatedModel.toByteArray()), true, false, null);
    }
    
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.PreferencePage#performOk()
   */
  public boolean performOk() {
    // store the value in the owner text field
    try {
      applyChanges(constructPrefs());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return true;
  }

  private CGScreenPreferences constructPrefs() {
    boolean bodies = btnBodies.getSelection();
    boolean joints = btnJoints.getSelection();
    boolean aabb = btnAabb.getSelection();
    boolean debugData = btnDrawDebugData.getSelection();
    boolean entiites = btnDrawEntities.getSelection();
    boolean inactiveBodies = btnInactiveModies.getSelection();
    boolean mouseJoint = btnInstallMouseJoint.getSelection();

    int ptmRatio = txtPTM.getSelection();
    float timeStep = FloatUtil.toFloat(txtTimeStep.getText());
    float gravityX = FloatUtil.toFloat(txtGravityX.getText());
    float gravityY = FloatUtil.toFloat(txtGravityY.getText());
    int velocityItr = txtVelocityItr.getSelection();
    int posItr = txtPosItr.getSelection();

    int cardNoX = txtCardNoX.getSelection();
    int cardNoY = txtCardNoY.getSelection();
    int cardWidth = txtCardWidth.getSelection();
    int cardHeight = txtCardHeight.getSelection();

    CGCardPreferences.Builder cardBuildr = CGCardPreferences.newBuilder().setCardHeight(cardHeight)
        .setCardWidth(cardWidth).setCardNoX(cardNoX).setCardNoY(cardNoY);

    CGWorldPreferences.Builder worldBuildr = CGWorldPreferences.newBuilder().setGravityX(gravityX)
        .setGravityY(gravityY).setPtmRatio(ptmRatio)
        .setPositionIterations(posItr).setTimeStep(timeStep)
        .setVelocityIterations(velocityItr);

    CGDebugDrawPreferences.Builder dbgBuildr = CGDebugDrawPreferences.newBuilder().setDrawAABB(aabb)
        .setDrawBodies(bodies).setDrawDebugData(debugData)
        .setDrawEntities(entiites).setDrawInactiveBodies(inactiveBodies)
        .setDrawJoints(joints).setInstallMouseJoint(mouseJoint);

    return CGScreenPreferences.newBuilder().setCardPrefs(cardBuildr.build()).setDebugDrawPrefs(dbgBuildr.build())
        .setWorldPrefs(worldBuildr.build()).build();
  }

}