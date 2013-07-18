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
package com.laex.cg2d.screeneditor.handlers;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.badlogic.gdx.math.Rectangle;
import com.laex.cg2d.model.ILayerManager;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.IDCreationStrategy;
import com.laex.cg2d.model.model.IDCreationStrategyFactory;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ModelCopier;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.commands.LayerAddCommand;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;
import com.laex.cg2d.screeneditor.model.ScreenModelAdapter;
import com.laex.cg2d.screeneditor.model.ShapeCopier;

/**
 * The Class ListExistingScreensDialog.
 */
public class ImportScreenContentsDialog extends TitleAreaDialog {

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

  /** The new layer name composite. */
  private Composite newLayerNameComposite;

  /** The lbl shape prefix. */
  private Label lblShapePrefix;

  /** The txt suffix. */
  private Text txtSuffix;

  /** The lbl columns. */
  private Label lblColumns;

  /** The txt columns repeat. */
  private Spinner txtColumnsRepeat;

  /** The label. */
  private Label label;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   * @param cmdStack
   *          the cmd stack
   */
  public ImportScreenContentsDialog(Shell parentShell, CommandStack cmdStack) {
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
    setTitle("Import Screen Contents Dialog");
    setMessage("Import contents from other screens");
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(2, false));
    new Label(container, SWT.NONE);

    listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
    list = listViewer.getList();
    GridData gd_list = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
    gd_list.widthHint = 176;
    gd_list.heightHint = 144;
    list.setLayoutData(gd_list);

    lblShapePrefix = formToolkit.createLabel(container, "Suffix (Required)", SWT.NONE);
    lblShapePrefix.setBackground(null);
    new Label(container, SWT.NONE);

    txtSuffix = formToolkit.createText(container, "New Text", SWT.NONE);
    txtSuffix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtSuffix.setText("");
    txtSuffix.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        validateAll();
      }
    });
    new Label(container, SWT.NONE);

    lblColumns = new Label(container, SWT.NONE);
    lblColumns.setToolTipText("Repeat Columns");
    formToolkit.adapt(lblColumns, true, true);
    lblColumns.setText("Columns");
    lblColumns.setBackground(null);
    new Label(container, SWT.NONE);

    txtColumnsRepeat = new Spinner(container, SWT.BORDER);
    txtColumnsRepeat.setSelection(1);
    txtColumnsRepeat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(txtColumnsRepeat);
    formToolkit.paintBordersFor(txtColumnsRepeat);
    new Label(container, SWT.NONE);

    btnImportInNew = new Button(container, SWT.CHECK);
    btnImportInNew.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    btnImportInNew.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        validateAll();
      }
    });
    formToolkit.adapt(btnImportInNew, true, true);
    btnImportInNew.setText("Import in New Layer");
    btnImportInNew.setBackground(null);

    txtNewLayerName = formToolkit.createText(container, "New Text", SWT.NONE);
    txtNewLayerName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtNewLayerName.setText("");
    txtNewLayerName.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        if (!StringUtils.isEmpty(txtNewLayerName.getText())) {
          btnImportInNew.setSelection(true);
        } else {
          btnImportInNew.setSelection(false);
        }

        validateAll();
      }
    });
    new Label(container, SWT.NONE);

    newLayerNameComposite = formToolkit.createComposite(container, SWT.NONE);
    newLayerNameComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    newLayerNameComposite.setEnabled(false);
    newLayerNameComposite.setBackground(null);
    formToolkit.paintBordersFor(newLayerNameComposite);
    newLayerNameComposite.setLayout(new GridLayout(1, false));

    try {
      loadScreens();
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return container;
  }

  /**
   * Load screens.
   * 
   * @throws CoreException
   *           the core exception
   */
  private void loadScreens() throws CoreException {
    resList = ScreenEditorUtil.getListOfScreensInCurrentProject(ScreenEditorUtil.getActiveEditorInput());
    for (IResource res : resList) {

      if (res.getName().equals(((IFileEditorInput) ScreenEditorUtil.getActiveEditorInput()).getFile().getName())) {
        continue;
      }

      list.add(res.getName());
    }

    list.select(0);

  }

  /**
   * Validate all.
   */
  private void validateAll() {
    validateNewLayerNameSelection();
    validateSuffix();
    validateScreens();

    if (StringUtils.isEmpty(txtNewLayerName.getText()) && btnImportInNew.getSelection()) {
      getButton(OK).setEnabled(false);
    }
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
    boolean isEmpty = StringUtils.isEmpty(txtSuffix.getText());

    if (isEmpty) {
      getButton(OK).setEnabled(false);
      setErrorMessage("Please provide a suffix");
      return;
    }

    // validate suffix
    // for the suffix to be valid
    if (!isAlphaNumeric || isEmpty) {
      getButton(OK).setEnabled(false);
      setErrorMessage("Suffix should be alphanumeric");
      return;
    }

    getButton(OK).setEnabled(true);
    setErrorMessage(null);
  }

  /**
   * Validate screens.
   */
  private void validateScreens() {
    // The resource list has at least one screen file i.e. itself, which is not
    // displayed on the list
    if (resList.size() <= 1) {
      setErrorMessage("You must have at least one other screen for import.");
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

    validateAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    // Before we proceed, we ask the user to save the file
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
        .doSave(new NullProgressMonitor());

    String resFile = list.getSelection()[0];

    for (IResource res : resList) {
      if (res.getName().equals(resFile)) {
        IFile file = (IFile) res;
        try {
          CGScreenModel model = CGScreenModel.parseFrom(file.getContents());

          GameModel gameModel = ScreenModelAdapter.asGameModel(model);

          GameModel thisScreenModel = ScreenEditorUtil.getScreenModel();

          CompoundCommand cc = new CompoundCommand();

          boolean createNewLayer = btnImportInNew.getSelection();

          ILayerManager layerMgr = ScreenEditorUtil.getScreenLayerManager();
          Layer layer = null;

          if (createNewLayer) {
            int newLayerId = layerMgr.getNewLayerId();
            layer = new Layer(newLayerId, txtNewLayerName.getText() + newLayerId, true, false);
            LayerAddCommand layerAddCmd = new LayerAddCommand(layer, thisScreenModel.getDiagram());
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

          /*
           * We need an id creator for model we're currently working on. not of
           * the file were trying to import from.
           */
          IDCreationStrategy idCreator = IDCreationStrategyFactory.getIDCreator(ScreenEditorUtil.getScreenModel());

          int index = 0;

          for (int i = 0; i < txtColumnsRepeat.getSelection(); i++) {

            for (Shape shape : gameModel.getDiagram().getChildren()) {
              // add suffix to id
              // TODO: Create a shape cloning mechanism
              ModelCopier shapeCopier = new ShapeCopier();

              Shape newShape = (Shape) shapeCopier.copy(shape);
              newShape.setParentLayer(layer);

              // make sure our items are really unique
              StringBuffer sb = new StringBuffer(shape.getId()).append(txtSuffix.getText()).append(index++);
              boolean isIdUsed = idCreator.isIdUsed(newShape.getEditorShapeType(), sb.toString());
              if (isIdUsed) {
                newShape.setId(idCreator.newId(newShape.getEditorShapeType()));
              } else {
                newShape.setId(sb.toString());
              }

              Rectangle r = new Rectangle(shape.getBounds()); // make sure to
                                                              // make a copy of
                                                              // the bounds and
                                                              // then modify its
                                                              // coords

              if (shape.getEditorShapeType().isBackground()) {
                r.x = i * shape.getBounds().getWidth() - 1;
              } else {
                r.x += i * thisScreenModel.getScreenPrefs().getCardPrefs().getCardWidth();
              }

              newShape.setBounds(r);

              ShapeCreateCommand scc = new ShapeCreateCommand(newShape, thisScreenModel.getDiagram());
              cc.add(scc);
            }

          }

          commandStack.execute(cc);

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
    return new Point(303, 450);
  }
}
