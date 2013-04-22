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
package com.laex.cg2d.entityeditor.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.badlogic.gdx.math.Vector2;
import com.laex.cg2d.entityeditor.EntityFormEditor;
import com.laex.cg2d.entityeditor.ui.CollisionShapeSelectionDialog;
import com.laex.cg2d.shared.CGCProject;
import com.laex.cg2d.shared.ResourceManager;
import com.laex.cg2d.shared.SharedImages;
import com.laex.cg2d.shared.model.Entity;
import com.laex.cg2d.shared.model.EntityAnimation;
import com.laex.cg2d.shared.model.EntityCollisionType;
import com.laex.cg2d.shared.model.ResourceFile;
import com.laex.cg2d.shared.util.EntitiesUtil;

/**
 * The Class CollisionFormPage.
 */
public class CollisionFormPage extends FormPage {

  /** The frames composite. */
  private Composite framesComposite;

  /** The managed form. */
  private IManagedForm managedForm;

  /** The sctn frames. */
  private Section sctnFrames;

  /** The table. */
  private Table table;

  /** The table viewer. */
  private TableViewer tableViewer;

  /** The animation list items. */
  private java.util.List<AnimationListViewItem> animationListItems = new ArrayList<AnimationListViewItem>();

  /** The dirty. */
  private boolean dirty = false;

  /** The entity form editor. */
  private EntityFormEditor entityFormEditor;

  /** The txt x. */
  private Spinner txtX;

  /** The txt y. */
  private Spinner txtY;

  /** The txt width. */
  private Spinner txtWidth;

  /** The txt height. */
  private Spinner txtHeight;

  /** The collision shape hyper link. */
  private ImageHyperlink collisionShapeHyperLink;

  /** The sctn shapes. */
  private Section sctnShapes;

  /** The sctn shape properties. */
  private Section sctnShapeProperties;

  /** The sctn shapes composite. */
  private Composite sctnShapesComposite;

  /** The collision shape. */
  private EntityCollisionType collisionShape = EntityCollisionType.NONE;

  /** The mghprlnk add. */
  private ImageHyperlink mghprlnkAdd;

  /** The mghprlnk rem. */
  private ImageHyperlink mghprlnkRem;

  /** The phys ed fixture file. */
  private IFile physEdFixtureFile;

  /** The figure canvas. */
  private FigureCanvas figureCanvas;

  /** The freeform layered pane. */
  private FreeformLayeredPane freeformLayeredPane;

  /** The rectangle figure. */
  private RectangleFigure rectangleFigure;

  /** The ellipse figure. */
  private Ellipse ellipseFigure;

  /**
   * Create the form page.
   * 
   * @param id
   *          the id
   * @param title
   *          the title
   */
  public CollisionFormPage(String id, String title) {
    super(id, title);
  }

  /**
   * Create the form page.
   * 
   * @param editor
   *          the editor
   * @param id
   *          the id
   * @param title
   *          the title
   * @wbp.parser.constructor
   * @wbp.eval.method.parameter id "Some id"
   * @wbp.eval.method.parameter title "Some title"
   */
  public CollisionFormPage(EntityFormEditor editor, String id, String title) {
    super(editor, id, title);
    this.entityFormEditor = editor;

    // Update with the new model when page changes
    editor.addPageChangedListener(new IPageChangedListener() {
      @Override
      public void pageChanged(PageChangedEvent event) {
        if (event.getSelectedPage().equals(CollisionFormPage.this)) {
          resetAll();

          loadAnimationsFromModel();
          setShapePropertiesState(true);

          // Edge case. Disable add/remove collision shape link if there are no
          // list view items
          if (animationListItems.size() == 0) {
            mghprlnkAdd.setEnabled(false);
            mghprlnkRem.setEnabled(false);
            setShapePropertiesState(false);
          }
        }
      }
    });
  }

  /**
   * Reset all.
   */
  private void resetAll() {
    animationListItems.clear();
    tableViewer.refresh();
    freeformLayeredPane.removeAll();
    removeCollisionShape();
    resetFramesComposite();
    resetShapeProperties();
  }

  /**
   * Reflect animations from model to screen.
   */
  private void loadAnimationsFromModel() {
    Entity model = this.entityFormEditor.getModel();

    for (EntityAnimation ea : model.getAnimationList()) {
      if (ea == null)
        continue;

      AnimationListViewItem alvi = new AnimationListViewItem();
      alvi.setName(ea.getAnimationName());
      alvi.setFirstFrame(SharedImages.BOX.createImage());
      alvi.setFrames(new LinkedList<Image>());
      alvi.setAnimation(ea);

      if (!ea.getAnimationResourceFile().isEmpty()) {
        Image frameImage = ResourceManager.getImageOfRelativePath(ea.getAnimationResourceFile().getResourceFile());
        createFramesFromStrip(frameImage, alvi);
      }

      addNewAnimation(alvi);
    }

    table.select(0);
    tableViewer.refresh();
    handleAnimationListSeletionListener();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
   */
  @Override
  public boolean isDirty() {
    return dirty;
  }

  /**
   * Sets the dirty.
   * 
   * @param dirty
   *          the new dirty
   */
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  /**
   * Create contents of the form.
   * 
   * @param managedForm
   *          the managed form
   */
  @Override
  protected void createFormContent(IManagedForm managedForm) {
    this.managedForm = managedForm;
    FormToolkit toolkit = managedForm.getToolkit();
    ScrolledForm form = managedForm.getForm();
    form.setText("Collision Shapes");
    form.getToolBarManager().update(true);
    managedForm.getForm().getBody().setLayout(new FormLayout());

    sctnFrames = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnFrames = new FormData();
    fd_sctnFrames.right = new FormAttachment(0, 605);
    fd_sctnFrames.bottom = new FormAttachment(100, -10);
    sctnFrames.setLayoutData(fd_sctnFrames);
    managedForm.getToolkit().paintBordersFor(sctnFrames);
    sctnFrames.setText("Set Collision Shape");
    sctnFrames.setExpanded(true);

    sctnShapes = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    fd_sctnFrames.left = new FormAttachment(0, 297);
    FormData fd_sctnShapes = new FormData();
    fd_sctnShapes.right = new FormAttachment(sctnFrames, -6);
    fd_sctnShapes.top = new FormAttachment(0, 12);
    sctnShapes.setLayoutData(fd_sctnShapes);
    managedForm.getToolkit().paintBordersFor(sctnShapes);
    sctnShapes.setText("Shapes");

    sctnShapesComposite = managedForm.getToolkit().createComposite(sctnShapes, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(sctnShapesComposite);
    sctnShapes.setClient(sctnShapesComposite);
    sctnShapesComposite.setLayout(new RowLayout(SWT.VERTICAL));

    framesComposite = managedForm.getToolkit().createComposite(sctnFrames, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(framesComposite);
    sctnFrames.setClient(framesComposite);
    framesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

    figureCanvas = new FigureCanvas(framesComposite);
    freeformLayeredPane = new FreeformLayeredPane();
    freeformLayeredPane.setLayoutManager(new FreeformLayout());
    figureCanvas.setContents(freeformLayeredPane);

    managedForm.getToolkit().adapt(figureCanvas);
    managedForm.getToolkit().paintBordersFor(figureCanvas);

    sctnShapeProperties = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    fd_sctnFrames.top = new FormAttachment(sctnShapeProperties, 6);
    FormData fd_sctnShapeProperties = new FormData();
    fd_sctnShapeProperties.right = new FormAttachment(0, 608);
    fd_sctnShapeProperties.top = new FormAttachment(0, 12);
    fd_sctnShapeProperties.left = new FormAttachment(0, 297);
    sctnShapeProperties.setLayoutData(fd_sctnShapeProperties);
    managedForm.getToolkit().paintBordersFor(sctnShapeProperties);
    sctnShapeProperties.setText("Properties");
    sctnShapeProperties.setExpanded(true);

    Composite composite = managedForm.getToolkit().createComposite(sctnShapeProperties, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(composite);
    sctnShapeProperties.setClient(composite);
    GridLayout gl_composite = new GridLayout(3, false);
    composite.setLayout(gl_composite);
    new Label(composite, SWT.NONE);

    Label lblX = managedForm.getToolkit().createLabel(composite, "X", SWT.NONE);

    txtX = new Spinner(composite, SWT.BORDER);
    txtX.setMinimum(-100);
    txtX.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateShapeDataToModel();
        handleAnimationListSeletionListener();
      }
    });
    txtX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(composite, SWT.NONE);

    Label lblY = managedForm.getToolkit().createLabel(composite, "Y", SWT.NONE);

    txtY = new Spinner(composite, SWT.BORDER);
    txtY.setMinimum(-100);
    txtY.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateShapeDataToModel();
        handleAnimationListSeletionListener();
      }
    });
    GridData gd_txtY = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txtY.widthHint = 49;
    txtY.setLayoutData(gd_txtY);
    managedForm.getToolkit().adapt(txtY);
    managedForm.getToolkit().paintBordersFor(txtY);
    new Label(composite, SWT.NONE);

    Label lblWidth = managedForm.getToolkit().createLabel(composite, "Width", SWT.NONE);

    txtWidth = new Spinner(composite, SWT.BORDER);
    txtWidth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtWidth.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateShapeDataToModel();
        handleAnimationListSeletionListener();

      }
    });
    managedForm.getToolkit().adapt(txtWidth);
    managedForm.getToolkit().paintBordersFor(txtWidth);
    new Label(composite, SWT.NONE);

    Label lblHeight = new Label(composite, SWT.NONE);
    managedForm.getToolkit().adapt(lblHeight, true, true);
    lblHeight.setText("Height");

    txtHeight = new Spinner(composite, SWT.BORDER);
    txtHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtHeight.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateShapeDataToModel();
        handleAnimationListSeletionListener();
      }
    });
    managedForm.getToolkit().adapt(txtHeight);
    managedForm.getToolkit().paintBordersFor(txtHeight);

    Composite shapesTopClientComposite = managedForm.getToolkit().createComposite(sctnShapes, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(shapesTopClientComposite);
    sctnShapes.setTextClient(shapesTopClientComposite);
    shapesTopClientComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
    shapesTopClientComposite.setBackground(null);

    mghprlnkAdd = managedForm.getToolkit().createImageHyperlink(shapesTopClientComposite, SWT.NONE);

    mghprlnkRem = managedForm.getToolkit().createImageHyperlink(shapesTopClientComposite, SWT.NONE);

    mghprlnkAdd.setBackground(null);
    mghprlnkAdd.addHyperlinkListener(new HyperlinkAdapter() {
      public void linkActivated(HyperlinkEvent e) {
        
        CollisionShapeSelectionDialog cssd = new CollisionShapeSelectionDialog(getSite().getShell());
        int resp = cssd.open();
        if (resp == CollisionShapeSelectionDialog.CANCEL) {
          return;
        }

        collisionShape = cssd.getTypeSelected();
        physEdFixtureFile = cssd.getPhsEdFixtureFile();

        ResourceFile fixtureResFile = null;
        if (collisionShape.isCustom()) {
          fixtureResFile = ResourceFile.create(physEdFixtureFile.getFullPath().toOSString(), physEdFixtureFile
              .getLocation().toOSString());
          
          selectedAnimationListItem().getAnimation().setFixtureResourceFile(fixtureResFile);
        }

        showCollisionShape(collisionShape, fixtureResFile);

        dirty = true;
        entityFormEditor.editorDirtyStateChanged();

        mghprlnkAdd.setEnabled(false);
        mghprlnkRem.setEnabled(true);
      }
    });
    managedForm.getToolkit().paintBordersFor(mghprlnkAdd);
    mghprlnkAdd.setImage(SharedImages.ADD_ITEM_SMALL.createImage());
    mghprlnkAdd.setText("");

    mghprlnkRem.addHyperlinkListener(new HyperlinkAdapter() {
      public void linkActivated(HyperlinkEvent e) {
        removeCollisionShape();

        dirty = true;
        entityFormEditor.editorDirtyStateChanged();

        mghprlnkAdd.setEnabled(true);
        mghprlnkRem.setEnabled(false);
      }
    });
    mghprlnkRem.setImage(SharedImages.REMOVE_ITEM_SMALL.createImage());
    mghprlnkRem.setBackground(null);
    managedForm.getToolkit().paintBordersFor(mghprlnkRem);
    mghprlnkRem.setText("");

    Section sctnAnimations = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    fd_sctnShapes.left = new FormAttachment(sctnAnimations, 6);
    fd_sctnShapes.bottom = new FormAttachment(sctnAnimations, 0, SWT.BOTTOM);
    FormData fd_sctnAnimations = new FormData();
    fd_sctnAnimations.top = new FormAttachment(0, 12);
    fd_sctnAnimations.bottom = new FormAttachment(100, -10);
    fd_sctnAnimations.right = new FormAttachment(0, 170);
    fd_sctnAnimations.left = new FormAttachment(0, 10);
    sctnAnimations.setLayoutData(fd_sctnAnimations);
    managedForm.getToolkit().paintBordersFor(sctnAnimations);
    sctnAnimations.setText("Animations");
    sctnAnimations.setExpanded(true);

    Composite composite_1 = managedForm.getToolkit().createComposite(sctnAnimations, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(composite_1);
    sctnAnimations.setClient(composite_1);
    composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

    tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
    table = tableViewer.getTable();
    managedForm.getToolkit().paintBordersFor(table);
    tableViewer.setLabelProvider(new AnimationTableLabelProvider());
    tableViewer.setContentProvider(new AnimationTableContentProvider(animationListItems));
    tableViewer.setInput(animationListItems);
    tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        handleAnimationListSeletionListener();
      }
    });
    toolkit.decorateFormHeading(form.getForm());
  }

  /**
   * Calculate vertices of shape.
   * 
   * @return the list
   */
  private List<Vector2> calculateVerticesOfShape() {
    switch (collisionShape) {
    case BOX:
    case CIRCLE:
      Rectangle r = shapeTypeBoundingBox();

      int widthVal = r.x + r.width;
      int heightVal = r.y + r.height;

      Vector2 v1 = new Vector2(r.x, r.y);
      Vector2 v2 = new Vector2(widthVal, r.y);
      Vector2 v3 = new Vector2(widthVal, heightVal);
      Vector2 v4 = new Vector2(r.x, heightVal);

      List<Vector2> vlist = new ArrayList<Vector2>();
      vlist.add(v1);
      vlist.add(v2);
      vlist.add(v3);
      vlist.add(v4);

      return vlist;
    case CUSTOM:
      break;
    case NONE:
      break;
    default:
      break;
    }

    return null;
  }

  /**
   * Shape type bounding box.
   * 
   * @return the rectangle
   */
  private Rectangle shapeTypeBoundingBox() {
    int x = txtX.getSelection();
    int y = txtY.getSelection();
    int width = txtWidth.getSelection();
    int height = txtHeight.getSelection();
    return new Rectangle(x, y, width, height);
  }

  /**
   * Update shape data to model.
   */
  protected void updateShapeDataToModel() {
    AnimationListViewItem alvi = selectedAnimationListItem();

    Rectangle r = shapeTypeBoundingBox();

    alvi.getAnimation().setShapeType(collisionShape);
    alvi.getAnimation().setShpX(r.x);
    alvi.getAnimation().setShpY(r.y);
    alvi.getAnimation().setShpWidth(r.width);
    alvi.getAnimation().setShpHeight(r.height);
    alvi.getAnimation().setVertices(calculateVerticesOfShape());

    //
    dirty = true;
    entityFormEditor.editorDirtyStateChanged();
  }

  /**
   * Reset frames composite.
   */
  private void resetFramesComposite() {
    framesComposite.layout(true);
  }

  /**
   * Handle animation list seletion listener.
   */
  private void handleAnimationListSeletionListener() {
    AnimationListViewItem ai = selectedAnimationListItem();
    if (ai == null) {
      mghprlnkAdd.setEnabled(false);
      mghprlnkRem.setEnabled(false);
      return;
    }

    // preview animation at the end
    doPreview(ai);

    // select appr. shape
    if (ai.getAnimation().getShapeType() != null) {
      collisionShape = ai.getAnimation().getShapeType();

      if (collisionShape.isNone()) {

        ai.getAnimation().setShpX(0);
        ai.getAnimation().setShpY(0);
        ai.getAnimation().setShpWidth(0);
        ai.getAnimation().setShpHeight(0);
        removeCollisionShape();
        resetShapeProperties();
        mghprlnkAdd.setEnabled(true);
        mghprlnkRem.setEnabled(false);
        return;

      }

      showCollisionShape(collisionShape, ai.getAnimation().getFixtureResourceFile());
      setShapeProperties(ai.getAnimation());
      mghprlnkAdd.setEnabled(false);
      mghprlnkRem.setEnabled(true);

      animationAndCollisionShapePreview(ai.getAnimation().getShapeType(), ai.getFrames());

    } else {
      removeCollisionShape();
      resetShapeProperties();
      mghprlnkAdd.setEnabled(true);
      mghprlnkRem.setEnabled(false);
    }

  }

  /**
   * Do preview.
   * 
   * @param ai
   *          the ai
   */
  private void doPreview(AnimationListViewItem ai) {
    if (ai.getAnimation() != null) {
      if (ai.getFrames() != null && !ai.getFrames().isEmpty()) {
        animationAndCollisionShapePreview(ai.getAnimation().getShapeType(), ai.getFrames());
      }
    }
  }

  /**
   * Sets the shape properties.
   * 
   * @param ea
   *          the new shape properties
   */
  private void setShapeProperties(EntityAnimation ea) {
    txtX.setSelection(ea.getShpX());
    txtY.setSelection(ea.getShpY());
    txtWidth.setSelection(ea.getShpWidth());
    txtHeight.setSelection(ea.getShpHeight());
  }

  /**
   * Reset shape properties.
   */
  private void resetShapeProperties() {
    txtX.setSelection(0);
    txtY.setSelection(0);
    txtWidth.setSelection(0);
    txtHeight.setSelection(0);
  }

  /**
   * Sets the shape properties state.
   * 
   * @param state
   *          the new shape properties state
   */
  private void setShapePropertiesState(boolean state) {
    txtX.setEnabled(state);
    txtY.setEnabled(state);
    txtWidth.setEnabled(state);
    txtHeight.setEnabled(state);
  }

  /**
   * Selected animation list item.
   * 
   * @return the animation list view item
   */
  private AnimationListViewItem selectedAnimationListItem() {
    if (tableViewer.getSelection().isEmpty()) {
      return null;
    }
    AnimationListViewItem ai = (AnimationListViewItem) ((IStructuredSelection) tableViewer.getSelection())
        .getFirstElement();
    return ai;
  }

  /**
   * Adds the new animation.
   * 
   * @param alvi
   *          the alvi
   */
  private void addNewAnimation(AnimationListViewItem alvi) {
    resetFramesComposite();
    animationListItems.add(alvi);
    tableViewer.refresh();

    // select the recently created object
    int index = animationListItems.indexOf(alvi);
    table.select(index);
    handleAnimationListSeletionListener();
  }

  /**
   * Creates the frames from strip.
   * 
   * @param selectedImage
   *          the selected image
   * @param alvi
   *          the alvi
   */
  private void createFramesFromStrip(Image selectedImage, AnimationListViewItem alvi) {
    Queue<Image> strip = EntitiesUtil.createImageStrip(selectedImage);
    alvi.setFrames(strip);
  }

  /**
   * Animation frame preview.
   * 
   * @param colType
   *          the col type
   * @param animation
   *          the animation
   */
  private void animationAndCollisionShapePreview(EntityCollisionType colType, final Queue<Image> animation) {
    freeformLayeredPane.removeAll();

    Image i = animation.peek();
    ImageFigure iff = new ImageFigure(i);
    iff.setBounds(new Rectangle(0, 0, i.getBounds().width, i.getBounds().height));

    freeformLayeredPane.add(iff);

    switch (colType) {
    case BOX:
      rectangleFigure = new RectangleFigure();
      rectangleFigure.setAlpha(120);
      rectangleFigure.setBounds(shapeTypeBoundingBox());

      freeformLayeredPane.add(rectangleFigure);
      break;
    case CIRCLE:
      ellipseFigure = new Ellipse();
      ellipseFigure.setAlpha(120);
      ellipseFigure.setBounds(shapeTypeBoundingBox());
      freeformLayeredPane.add(ellipseFigure);
      break;
    case CUSTOM:
      break;
    case NONE:
      break;
    default:
      break;
    }

  }

  /**
   * Clear collision figure.
   */
  private void clearCollisionFigure() {
    if (rectangleFigure != null) {
      if (freeformLayeredPane.getChildren().contains(rectangleFigure)) {
        freeformLayeredPane.remove(rectangleFigure);
        rectangleFigure = null;
      }
    }

    if (ellipseFigure != null) {
      if (freeformLayeredPane.getChildren().contains(ellipseFigure)) {
        freeformLayeredPane.remove(ellipseFigure);
        ellipseFigure = null;
      }
    }
  }

  /**
   * Removes the collision shape.
   */
  protected void removeCollisionShape() {
    if (collisionShapeHyperLink != null) {
      collisionShapeHyperLink.dispose();
      collisionShapeHyperLink = null;
    }

    collisionShape = EntityCollisionType.NONE;
    clearCollisionFigure();
    sctnShapesComposite.layout(true);
    resetShapeProperties();

    AnimationListViewItem alvi = selectedAnimationListItem();
    if (alvi != null) {
      alvi.getAnimation().setShapeType(collisionShape);
      alvi.getAnimation().setShpX(txtX.getSelection());
      alvi.getAnimation().setShpY(txtY.getSelection());
      alvi.getAnimation().setShpWidth(txtWidth.getSelection());
      alvi.getAnimation().setShpHeight(txtHeight.getSelection());
      alvi.getAnimation().setVertices(new ArrayList<Vector2>());
    }
  }

  /**
   * Adds the collision shape.
   */
  private void showCollisionShape(EntityCollisionType _collisionShape, ResourceFile physicsEditorFile) {
    if (collisionShapeHyperLink == null) {
      collisionShapeHyperLink = CollisionFormPage.this.managedForm.getToolkit().createImageHyperlink(
          sctnShapesComposite, SWT.NONE);
    }

    EntityAnimation entityAnim = selectedAnimationListItem().getAnimation();
    entityAnim.setShapeType(_collisionShape);

    collisionShapeHyperLink.setText(collisionShape.name());
    
    switch (_collisionShape) {
    
    case BOX:
      autoFillCollisionSize(entityAnim);
      collisionShapeHyperLink.setImage(SharedImages.BOX.createImage());
      break;

    case CIRCLE:
      autoFillCollisionSize(entityAnim);
      collisionShapeHyperLink.setImage(SharedImages.CIRCLE.createImage());
      break;

    case CUSTOM:
      collisionShapeHyperLink.setImage(SharedImages.PHYSICS_BODY_EDITOR_LOGO_SMALL.createImage());
      IPath resFilePath = new Path(physicsEditorFile.getResourceFile());
      boolean resourceFileExists = CGCProject.getInstance().exists(resFilePath, false);
      
      if (!resourceFileExists) {
        collisionShapeHyperLink.setText("File Missing");
      } else {
        collisionShapeHyperLink.setText(resFilePath.lastSegment());
      }
      break;
      
    case NONE:
      break;
      
    default:
      break;

    }

    doPreview(selectedAnimationListItem());

    sctnShapesComposite.layout(true);
  }

  /**
   * Auto fill collision size.
   */
  private void autoFillCollisionSize(EntityAnimation ea) {
    // If all of these are zero, then we auto fill
    if (ea.getShpX() == 0 && ea.getShpY() == 0 && ea.getShpWidth() == 0 && ea.getShpHeight() == 0) {
      Image img = entityFormEditor.getModel().getDefaultFrame();
      if (img == null) {
        return;
      }
      txtWidth.setSelection(img.getBounds().width);
      txtHeight.setSelection(img.getBounds().height);

      //
      updateShapeDataToModel();
    }

  }

}
