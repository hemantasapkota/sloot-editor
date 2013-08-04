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
package com.laex.cg2d.screeneditor.handlers.joints;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.Shape;

/**
 * The Class LiveJointEditingDialog.
 */
public class LiveJointEditingDialog extends TitleAreaDialog {

  /**
   * The Class JointRenderer.
   */
  class JointRenderer {

    /** The figure canvas. */
    private FigureCanvas figureCanvas;
    
    /** The layered pane. */
    private FreeformLayeredPane layeredPane;

    /**
     * Instantiates a new joint renderer.
     *
     * @param composite the composite
     */
    public JointRenderer(Composite composite) {

      initRenderer(composite);

      for (Shape shp : models) {

        RectangleFigure rf = new RectangleFigure();
        rf.setBounds(RectAdapter.d2dRect(shp.getBounds()));
        rf.setBackgroundColor(ColorConstants.green);

        layeredPane.add(rf);

      }

      figureCanvas.setBounds(0, 0, getInitialSize().x, getInitialSize().y);
      figureCanvas.setContents(layeredPane);

    }

    /**
     * Inits the renderer.
     *
     * @param composite the composite
     */
    private void initRenderer(Composite composite) {
      figureCanvas = new FigureCanvas(composite);
      layeredPane = new FreeformLayeredPane();
      layeredPane.setLayoutManager(new FreeformLayout());
      figureCanvas.setContents(layeredPane);
    }

  }

  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /** The models. */
  private List<Shape> models;

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell
   * @wbp.parser.constructor
   */
  public LiveJointEditingDialog(Shell parentShell) {
    super(parentShell);
    setShellStyle(SWT.MAX);
  }

  /**
   * Instantiates a new live joint editing dialog.
   *
   * @param parentShell the parent shell
   * @param models the models
   */
  public LiveJointEditingDialog(Shell parentShell, List<Shape> models) {
    super(parentShell);

    this.models = models;
  }

  /**
   * Create contents of the dialog.
   *
   * @param parent the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setTitle("Edit joint anchors.");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    setupJointsRenderer();

    return area;
  }

  /**
   * Setup joints renderer.
   */
  private void setupJointsRenderer() {
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
  }

  /**
   * Return the initial size of the dialog.
   *
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(388, 346);
  }
}
