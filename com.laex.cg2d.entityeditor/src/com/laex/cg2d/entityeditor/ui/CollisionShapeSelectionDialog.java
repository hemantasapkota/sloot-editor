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
package com.laex.cg2d.entityeditor.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.EntityCollisionType;
import com.laex.cg2d.model.resources.SWTResourceManager;

/**
 * The Class CollisionShapeSelectionDialog.
 */
public class CollisionShapeSelectionDialog extends TitleAreaDialog {

  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /** The type selected. */
  private EntityCollisionType typeSelected = EntityCollisionType.NONE;

  /** The phs ed fixture file. */
  private IFile phsEdFixtureFile = null;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   */
  public CollisionShapeSelectionDialog(Shell parentShell) {
    super(parentShell);
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
    setTitle("Select a collision shape");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    container.setLayout(new RowLayout(SWT.VERTICAL));
    GridData gd_container = new GridData(GridData.FILL_BOTH);
    gd_container.heightHint = 85;
    container.setLayoutData(gd_container);

    ImageHyperlink mghprlnkBox = formToolkit.createImageHyperlink(container, SWT.NONE);
    mghprlnkBox.setImage(SharedImages.BOX.createImage());
    formToolkit.paintBordersFor(mghprlnkBox);
    mghprlnkBox.setText("Box");
    mghprlnkBox.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        typeSelected = EntityCollisionType.BOX;
        CollisionShapeSelectionDialog.this.close();
      }
    });

    ImageHyperlink mghprlnkCirlce = formToolkit.createImageHyperlink(container, SWT.NONE);
    mghprlnkCirlce.setImage(SharedImages.CIRCLE.createImage());
    formToolkit.paintBordersFor(mghprlnkCirlce);
    mghprlnkCirlce.setText("Cirlce");
    mghprlnkCirlce.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        typeSelected = EntityCollisionType.CIRCLE;
        CollisionShapeSelectionDialog.this.close();
      }
    });

    ImageHyperlink mghprlnkCustom = formToolkit.createImageHyperlink(container, SWT.NONE);
    mghprlnkCustom.setImage(SharedImages.PHYSICS_BODY_EDITOR_LOGO_SMALL.createImage());
    formToolkit.paintBordersFor(mghprlnkCustom);
    mghprlnkCustom.setText("Physics Editor");
    mghprlnkCustom.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        typeSelected = EntityCollisionType.CUSTOM;

        IEditorInput edinp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
            .getEditorInput();
        IFolder texFlder = null;

        try {
          texFlder = CGCProject.getInstance().getTexturesFolder(edinp);
        } catch (CoreException e1) {
          Activator.getDefault().getLog().log(e1.getStatus());
          return;
        }

        FilteredResourcesSelectionDialog frsd = new FilteredResourcesSelectionDialog(getParentShell(), false, texFlder,
            IResource.FILE);
        frsd.setTitle("Select a physics editor file. Usually, this is a JSON file");
        frsd.setInitialPattern("*.json*");
        int resp = frsd.open();
        if (resp == FilteredResourcesSelectionDialog.CANCEL) {
          return;
        }

        phsEdFixtureFile = (IFile) frsd.getFirstResult();

        CollisionShapeSelectionDialog.this.close();
      }
    });

    return area;
  }

  /**
   * Gets the type selected.
   * 
   * @return the type selected
   */
  public EntityCollisionType getTypeSelected() {
    return typeSelected;
  }

  /**
   * Gets the phs ed fixture file.
   * 
   * @return the phs ed fixture file
   */
  public IFile getPhsEdFixtureFile() {
    return phsEdFixtureFile;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   *          the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(230, 224);
  }

}
