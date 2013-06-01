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
package com.laex.cg2d.screeneditor.actions;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.badlogic.gdx.math.Rectangle;
import com.laex.cg2d.model.ILayerManager;
import com.laex.cg2d.model.ScreenModel.CGBodyDef;
import com.laex.cg2d.model.ScreenModel.CGFixtureDef;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.EntitiesUtil;
import com.laex.cg2d.model.util.PlatformUtil;
import com.laex.cg2d.screeneditor.commands.LayerAddCommand;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;

/**
 * The Class ListExistingScreensDialog.
 */
public class ListExistingScreensDialog extends Dialog {

  /** The list. */
  private List list;

  /** The list viewer. */
  private ListViewer listViewer;

  /** The res list. */
  private java.util.List<IResource> resList;

  /** The command stack. */
  private CommandStack commandStack;

  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /** The btn import in new. */
  private Button btnImportInNew;

  /** The txt new layer name. */
  private Text txtNewLayerName;

  /** The lbl new label. */
  private Label lblNewLabel;

  /** The new layer name composite. */
  private Composite newLayerNameComposite;

  /** The lbl shape prefix. */
  private Label lblShapePrefix;

  /** The txt suffix. */
  private Text txtSuffix;
  private Label lblColumns;
  private Spinner txtColumnsRepeat;
  private Label label;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   * @param cmdStack
   *          the cmd stack
   */
  public ListExistingScreensDialog(Shell parentShell, CommandStack cmdStack) {
    super(parentShell);
    this.commandStack = cmdStack;
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   *          the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(2, false));

    listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
    list = listViewer.getList();
    GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
    gd_list.widthHint = 176;
    gd_list.heightHint = 144;
    list.setLayoutData(gd_list);

    lblShapePrefix = formToolkit.createLabel(container, "Suffix", SWT.NONE);
    lblShapePrefix.setBackground(null);

    txtSuffix = formToolkit.createText(container, "New Text", SWT.NONE);
    txtSuffix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtSuffix.setText("");
    txtSuffix.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        validateSuffix();
      }
    });

    lblColumns = new Label(container, SWT.NONE);
    lblColumns.setToolTipText("Repeat Columns");
    formToolkit.adapt(lblColumns, true, true);
    lblColumns.setText("Columns");
    lblColumns.setBackground(null);

    txtColumnsRepeat = new Spinner(container, SWT.BORDER);
    txtColumnsRepeat.setSelection(1);
    txtColumnsRepeat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(txtColumnsRepeat);
    formToolkit.paintBordersFor(txtColumnsRepeat);

    label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    formToolkit.adapt(label, true, true);

    btnImportInNew = new Button(container, SWT.CHECK);
    btnImportInNew.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    btnImportInNew.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        validateNewLayerNameSelection();
      }
    });
    formToolkit.adapt(btnImportInNew, true, true);
    btnImportInNew.setText("Import in New Layer");
    btnImportInNew.setBackground(null);

    lblNewLabel = formToolkit.createLabel(container, "New Layer", SWT.NONE);
    lblNewLabel.setBackground(null);

    txtNewLayerName = formToolkit.createText(container, "New Text", SWT.NONE);
    txtNewLayerName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtNewLayerName.setText("");
    txtNewLayerName.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        validateSuffix();
      }
    });

    newLayerNameComposite = formToolkit.createComposite(container, SWT.NONE);
    newLayerNameComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    newLayerNameComposite.setEnabled(false);
    newLayerNameComposite.setBackground(null);
    formToolkit.paintBordersFor(newLayerNameComposite);
    newLayerNameComposite.setLayout(new GridLayout(1, false));

    try {
      resList = PlatformUtil.getListOfScreensInCurrentProject(PlatformUtil.getActiveEditorInput());
      for (IResource res : resList) {

        if (res.getName().equals(((IFileEditorInput) PlatformUtil.getActiveEditorInput()).getFile().getName())) {
          continue;
        }

        list.add(res.getName());
      }

      list.select(0);

    } catch (CoreException e) {
      e.printStackTrace();
    }

    return container;
  }

  /**
   * Validate new layer name selection.
   */
  private void validateNewLayerNameSelection() {
    if (btnImportInNew.getSelection()) {

      newLayerNameComposite.setEnabled(true);
      txtNewLayerName.setFocus();

      if (!isNewLayerNameValid()) {
        getButton(OK).setEnabled(false);
        return;
      } else {
        getButton(OK).setEnabled(true);
      }

    } else {
      getButton(OK).setEnabled(true);
      newLayerNameComposite.setEnabled(false);
    }
  }

  /**
   * Checks if is new layer name valid.
   * 
   * @return true, if is new layer name valid
   */
  private boolean isNewLayerNameValid() {
    if (StringUtils.isEmpty(txtNewLayerName.getText())) {
      return false;
    }
    return true;
  }

  /**
   * Validate suffix.
   */
  private void validateSuffix() {
    boolean isAlphaNumeric = StringUtils.isAlphanumeric(txtSuffix.getText());

    // validate suffix
    // for the suffix to be valid
    if (!isAlphaNumeric) {
      getButton(OK).setEnabled(false);
    } else {
      getButton(OK).setEnabled(true);
    }

  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   *          the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

    getButton(OK).setEnabled(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    String resFile = list.getSelection()[0];

    for (IResource res : resList) {
      if (res.getName().equals(resFile)) {
        IFile file = (IFile) res;
        try {
          CGScreenModel model = CGScreenModel.parseFrom(file.getContents());

          GameModel gameModel = ScreenModelAdapter.asGameModel(model);

          //
          if (EntitiesUtil.visitModelAndInitEntities(gameModel)) {
            MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
            mb.setMessage("Some of the entities in the source import file are inconsistent. Please resolve the issue before proceeding.");
            mb.setText("Inconsistent entities in import source file");
            mb.open();
            return;
          }

          GameModel screenModel = PlatformUtil.getScreenModel();

          CompoundCommand cc = new CompoundCommand();

          boolean createNewLayer = btnImportInNew.getSelection();

          ILayerManager layerMgr = PlatformUtil.getScreenLayerManager();
          Layer layer = null;

          if (createNewLayer) {
            int newLayerId = layerMgr.getNewLayerId();
            layer = new Layer(newLayerId, txtNewLayerName.getText(), true, false);
            LayerAddCommand layerAddCmd = new LayerAddCommand(layer, screenModel.getDiagram());
            cc.add(layerAddCmd);
          } else {
            // Use existing layer
            layer = layerMgr.getCurrentLayer();

            if (layer == null) {
              // If we have layers, use the first one
              if (layerMgr.layerCount() > 0) {
                layer = layerMgr.getLayerAt(0);
              } else {
                MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
                mb.setMessage("There are no layers in the screen. Please select the \"import\" feature before continuting");
                mb.setText("No Layers in the screen");
                mb.open();
                return;
              }
            }
          }

          for (int i = 0; i < txtColumnsRepeat.getSelection(); i++) {

            for (Shape shape : gameModel.getDiagram().getChildren()) {
              // add suffix to id
              //TODO: Create a shape cloning mechanism
              CGBodyDef cgBodyDef = CGScreenModelAdapter.makeCGBodyDef(shape.getBodyDef()).build();
              CGFixtureDef cgFixDef = CGScreenModelAdapter.makeCGFixtureDef(shape.getFixtureDef()).build();
              CGShape newShape = CGScreenModelAdapter.makeShape(cgFixDef, cgBodyDef , shape).build();
              
              Shape newShapeClone = ScreenModelAdapter.asShape(newShape, layer);
              
              newShapeClone.setId(new StringBuffer(shape.getId()).append(txtSuffix.getText()).toString());

              Rectangle r = shape.getBounds();
              r.x = i * model.getScreenPrefs().getCardPrefs().getCardWidth() - 1;
              
              newShapeClone.setBounds(r);

              ShapeCreateCommand scc = new ShapeCreateCommand(newShapeClone, screenModel.getDiagram());
              cc.add(scc);
            }
            
          }

          commandStack.execute(cc);
          cc.dispose();

        } catch (IOException e) {
          e.printStackTrace();
        } catch (CoreException e) {
          e.printStackTrace();
        }

      }
    }

    super.okPressed();
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(213, 337);
  }
}
