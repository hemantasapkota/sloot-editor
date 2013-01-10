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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.laex.cg2d.protobuf.GameObject.CGGameModel;
import com.laex.cg2d.screeneditor.commands.LayerAddCommand;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;
import com.laex.cg2d.shared.ILayerManager;
import com.laex.cg2d.shared.adapter.GameModelAdapter;
import com.laex.cg2d.shared.adapter.RectAdapter;
import com.laex.cg2d.shared.model.GameModel;
import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.util.EntitiesUtil;
import com.laex.cg2d.shared.util.PlatformUtil;

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
  
  /** The label. */
  private Label label;
  
  /** The label_1. */
  private Label label_1;
  
  /** The lbl shape prefix. */
  private Label lblShapePrefix;
  
  /** The txt suffix. */
  private Text txtSuffix;

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell
   * @param cmdStack the cmd stack
   */
  public ListExistingScreensDialog(Shell parentShell, CommandStack cmdStack) {
    super(parentShell);
    this.commandStack = cmdStack;
  }

  /**
   * Create contents of the dialog.
   *
   * @param parent the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new FormLayout());

    btnImportInNew = new Button(container, SWT.CHECK);
    btnImportInNew.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        validateNewLayerNameSelection();
      }

    });

    FormData fd_btnImportInNew = new FormData();
    fd_btnImportInNew.left = new FormAttachment(0, 5);
    btnImportInNew.setLayoutData(fd_btnImportInNew);
    formToolkit.adapt(btnImportInNew, true, true);
    btnImportInNew.setText("Import in New Layer");
    btnImportInNew.setBackground(null);

    listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
    list = listViewer.getList();
    fd_btnImportInNew.top = new FormAttachment(list, 6);
    FormData fd_list = new FormData();
    fd_list.right = new FormAttachment(0, 203);
    fd_list.bottom = new FormAttachment(0, 146);
    fd_list.top = new FormAttachment(0, 10);
    fd_list.left = new FormAttachment(0, 5);
    list.setLayoutData(fd_list);

    newLayerNameComposite = formToolkit.createComposite(container, SWT.NONE);
    newLayerNameComposite.setEnabled(false);
    FormData fd_newLayerNameComposite = new FormData();
    fd_newLayerNameComposite.bottom = new FormAttachment(btnImportInNew, 35, SWT.BOTTOM);
    fd_newLayerNameComposite.top = new FormAttachment(btnImportInNew, 6);
    fd_newLayerNameComposite.left = new FormAttachment(btnImportInNew, 0, SWT.LEFT);
    fd_newLayerNameComposite.right = new FormAttachment(100, -10);
    newLayerNameComposite.setLayoutData(fd_newLayerNameComposite);
    newLayerNameComposite.setBackground(null);
    formToolkit.paintBordersFor(newLayerNameComposite);
    newLayerNameComposite.setLayout(new GridLayout(2, false));

    lblNewLabel = formToolkit.createLabel(newLayerNameComposite, "New Layer", SWT.NONE);
    lblNewLabel.setBackground(null);

    txtNewLayerName = formToolkit.createText(newLayerNameComposite, "New Text", SWT.NONE);
    GridData gd_txtNewLayerName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_txtNewLayerName.widthHint = 125;
    txtNewLayerName.setLayoutData(gd_txtNewLayerName);
    txtNewLayerName.setText("");
    txtNewLayerName.addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        validateSuffix();
      }
    });

    try {
      resList = PlatformUtil.getListOfScreensInCurrentProject(PlatformUtil.getActiveEditorInput());
      for (IResource res : resList) {

        if (res.getName().equals(((IFileEditorInput) PlatformUtil.getActiveEditorInput()).getFile().getName())) {
          continue;
        }

        list.add(res.getName());
      }

      list.select(0);

      label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
      FormData fd_label = new FormData();
      fd_label.right = new FormAttachment(100, -10);
      fd_label.left = new FormAttachment(0, 5);
      fd_label.top = new FormAttachment(newLayerNameComposite, 6);
      label.setLayoutData(fd_label);
      formToolkit.adapt(label, true, true);
      fd_label.bottom = new FormAttachment(100, -92);

      label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
      FormData fd_label_1 = new FormData();
      fd_label_1.top = new FormAttachment(newLayerNameComposite, 6);
      fd_label_1.right = new FormAttachment(list, 0, SWT.RIGHT);
      fd_label_1.bottom = new FormAttachment(newLayerNameComposite, 8, SWT.BOTTOM);
      fd_label_1.left = new FormAttachment(0, 10);
      label_1.setLayoutData(fd_label_1);
      formToolkit.adapt(label_1, true, true);

      lblShapePrefix = formToolkit.createLabel(container, "Suffix", SWT.NONE);
      FormData fd_lblShapePrefix = new FormData();
      fd_lblShapePrefix.top = new FormAttachment(label_1, 7);
      fd_lblShapePrefix.left = new FormAttachment(0, 10);
      lblShapePrefix.setLayoutData(fd_lblShapePrefix);
      lblShapePrefix.setBackground(null);

      txtSuffix = formToolkit.createText(container, "New Text", SWT.NONE);
      txtSuffix.setText("");
      FormData fd_txtSuffix = new FormData();
      fd_txtSuffix.right = new FormAttachment(list, 0, SWT.RIGHT);
      fd_txtSuffix.top = new FormAttachment(lblShapePrefix, -3, SWT.TOP);
      fd_txtSuffix.left = new FormAttachment(list, -127);
      txtSuffix.setLayoutData(fd_txtSuffix);
      txtSuffix.addModifyListener(new ModifyListener() {
        @Override
        public void modifyText(ModifyEvent e) {
          validateSuffix();
        }
      });

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

      if (StringUtils.isEmpty(txtNewLayerName.getText())) {
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
   * Validate suffix.
   */
  private void validateSuffix() {
    // validate suffix
    if (StringUtils.isEmpty(txtSuffix.getText()) || !StringUtils.isAlphanumeric(txtSuffix.getText())) {
      getButton(OK).setEnabled(false);
    } else {
      getButton(OK).setEnabled(true);
    }

  }

  /**
   * Create contents of the button bar.
   *
   * @param parent the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

    getButton(OK).setEnabled(false);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    String resFile = list.getSelection()[0];

    for (IResource res : resList) {
      if (res.getName().equals(resFile)) {
        IFile file = (IFile) res;
        try {
          CGGameModel model = CGGameModel.parseFrom(file.getContents());

          GameModel gameModel = GameModelAdapter.asGameModel(model);

          //
          if (EntitiesUtil.visitModelAndInitEntities(gameModel)) {
            MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
            mb.setMessage("Some of the entities in the source import file are inconsistent. Please resolve the issue before proceeding.");
            mb.setText("Inconsistent entities in import source file");
            mb.open();
            return;
          }

          GameModel screenModel = PlatformUtil.getScreenModel();

          //

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

          for (Shape shape : gameModel.getDiagram().getChildren()) {
            // add suffix to id
            shape.setId(new StringBuffer(shape.getId()).append(txtSuffix.getText()).toString());

            ShapeCreateCommand scc = new ShapeCreateCommand(shape, layer, screenModel.getDiagram(),
                RectAdapter.d2dRect(shape.getBounds()));
            cc.add(scc);
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
    return new Point(213, 316);
  }
}
