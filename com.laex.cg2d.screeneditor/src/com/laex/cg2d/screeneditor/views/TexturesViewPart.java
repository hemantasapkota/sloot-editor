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
package com.laex.cg2d.screeneditor.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.laex.cg2d.model.DNDFileTransfer;
import com.laex.cg2d.model.DNDFileTransfer.TransferType;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.resources.ResourceManager;

/**
 * The Class TexturesViewPart.
 */
public class TexturesViewPart extends ViewPart {

  /** The Constant ID. */
  public static final String ID = "com.laex.cg2d.screeneditor.TexturesView"; //$NON-NLS-1$

  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /** The img canvas. */
  private Canvas imgCanvas;

  /**
   * The listener interface for receiving texturesSelection events. The class
   * that is interested in processing a texturesSelection event implements this
   * interface, and the object created with that class is registered with a
   * component using the component's
   * <code>addTexturesSelectionListener<code> method. When
   * the texturesSelection event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see TexturesSelectionEvent
   */
  private final class TexturesSelectionListener implements ISelectionListener {

    /**
     * Fallback.
     */
    private void fallback() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
     * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
      if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
        fallback();
        return;
      }

      Object firstElement = ((IStructuredSelection) selection).getFirstElement();
      if (firstElement instanceof IFile) {
        IFile transferIFile = (IFile) firstElement;

        if (isPNGExtension(transferIFile)) {
          DNDFileTransfer.file = transferIFile;

          Image image = ResourceManager.getImage(transferIFile);

          imgCanvas = new Canvas(imageComposite, SWT.None);
          imgCanvas.setBackgroundImage(image);
          imgCanvas.setData(transferIFile.getFullPath().toOSString());
          imgCanvas.setSize(image.getBounds().width, image.getBounds().height);
          imageComposite.setContent(imgCanvas);

          DragSource dragSource = new DragSource(imgCanvas, DND.DROP_COPY);
          dragSource.addDragListener(new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
              DNDFileTransfer.transferType = TransferType.TEXTURE;
              event.data = "BOGUS";
            }
          });
          dragSource.setTransfer(new Transfer[]
            { TextTransfer.getInstance() });
        }

      } else {
        fallback();
      }

    }

    /**
     * Checks if is pNG extension.
     * 
     * @param ifile
     *          the ifile
     * @return true, if is pNG extension
     */
    private boolean isPNGExtension(IFile ifile) {
      return ifile != null && ifile.getFileExtension().equals(ICGCProject.PNG_EXTENSION);
    }
  }

  /** The tsl. */
  private TexturesSelectionListener tsl = new TexturesSelectionListener();

  /** The image composite. */
  private ScrolledComposite imageComposite;
  private Action toggleBGAction;

  /**
   * Instantiates a new textures view part.
   */
  public TexturesViewPart() {
  }

  /**
   * Adds the workbench selection listener.
   */
  private void addWorkbenchSelectionListener() {
    getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(tsl);
  }

  /**
   * Create contents of the view part.
   * 
   * @param parent
   *          the parent
   */
  @Override
  public void createPartControl(Composite parent) {

    createActions();
    initializeToolBar();
    initializeMenu();
    addWorkbenchSelectionListener();
    {
      imageComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
      formToolkit.adapt(imageComposite);
      formToolkit.paintBordersFor(imageComposite);
      imageComposite.setContent(imgCanvas);
    }
  }

  /**
   * Create the actions.
   */
  private void createActions() {

    // DragSource dragSource = new DragSource(mghprlnkNewImagehyperlink,
    // DND.DROP_COPY);
    // dragSource.addDragListener(new DragSourceAdapter() {
    // @Override
    // public void dragSetData(DragSourceEvent event) {
    // DNDFileTransfer.transferType = TransferType.TEXTURE;
    // event.data = "BOGUS";
    // }
    // });
    // dragSource.setTransfer(new Transfer[]
    // { TextTransfer.getInstance() });
    // }
    //

    {
      toggleBGAction = new Action("Toggle BG") {
        boolean toggled = false;

        @Override
        public void run() {
        }
      };
    }
  }

  /**
   * Initialize the toolbar.
   */
  private void initializeToolBar() {
    IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
  }

  /**
   * Initialize the menu.
   */
  private void initializeMenu() {
    IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
   */
  @Override
  public void setFocus() {
    // Set the focus
  }
}
