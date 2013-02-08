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
import java.util.Iterator;
import java.util.Queue;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.laex.cg2d.entityeditor.EntityFormEditor;
import com.laex.cg2d.entityeditor.ui.AnimationPropertyChangeDialog;
import com.laex.cg2d.entityeditor.ui.ImportSpriteDialog;
import com.laex.cg2d.shared.ResourceManager;
import com.laex.cg2d.shared.SharedImages;
import com.laex.cg2d.shared.model.Entity;
import com.laex.cg2d.shared.model.EntityAnimation;
import com.laex.cg2d.shared.model.ResourceFile;
import com.laex.cg2d.shared.util.EntitiesUtil;
import com.laex.cg2d.shared.util.FloatUtil;

/**
 * The Class AnimationFormPage.
 */
public class AnimationFormPage extends FormPage {

  /** The txt animation name. */
  private Text txtAnimationName;

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

  /** The btn list. */
  private java.util.List<Button> btnList = new ArrayList<Button>();

  /** The dirty. */
  private boolean dirty = false;

  /** The entity form editor. */
  private EntityFormEditor entityFormEditor;

  /** The btn default animation. */
  private Button btnDefaultAnimation;

  /** The mghprlnk add frames. */
  private ImageHyperlink mghprlnkAddFrames;

  /** The mghprlnk remove. */
  private ImageHyperlink mghprlnkRemove;

  /** The mghprlnk change animation name. */
  private ImageHyperlink mghprlnkChangeAnimationName;

  /** The lbl duration. */
  private Label lblDuration;

  /** The preview external. */
  private ImageHyperlink previewExternal;

  /** The txt animation duration. */
  private Text txtAnimationDuration;

  private AnimationFormPageController animController = new AnimationFormPageController();

  /**
   * Create the form page.
   * 
   * @param id
   *          the id
   * @param title
   *          the title
   */
  public AnimationFormPage(String id, String title) {
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
  public AnimationFormPage(EntityFormEditor editor, String id, String title) {
    super(editor, id, title);
    this.entityFormEditor = editor;
  }

  /**
   * Update animations.
   */
  private void loadAnimationsFromModel() {
    Entity model = this.entityFormEditor.getModel();

    for (EntityAnimation ea : model.getAnimationList()) {
      if (ea == null)
        continue;
      
      AnimationListViewItem alvi = animController.addAnimation(ea);

      if (!ea.getAnimationResourceFile().isEmpty()) {
        Image frameImage = ResourceManager.getImageOfRelativePath(ea.getAnimationResourceFile().getResourceFile());
        createAnimationFromStrip(frameImage, alvi);
        previewExternal.setEnabled(true);
      } else {
        previewExternal.setEnabled(false);
      }
      
      updateUIOnNewAnimationAdd(alvi, animController.indexOf(alvi));
    }

    table.select(0);
    tableViewer.refresh();
    framesComposite.layout(true);
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
    form.setText("Animation");

    Action ac = new Action("Add Animation") {
      @Override
      public void run() {
        super.run();

        AnimationListViewItem alvi = animController.createEmptyAnimation();

        updateUIOnNewAnimationAdd(alvi, animController.indexOf(alvi));

        entityFormEditor.getModel().addEntityAnimation(alvi.getAnimation());
        dirty = true;
        entityFormEditor.editorDirtyStateChanged();

      }
    };
    ac.setImageDescriptor(SharedImages.ADD_ITEM_2_SMALL);

    form.getToolBarManager().add(ac);
    form.getToolBarManager().update(true);
    managedForm.getForm().getBody().setLayout(new FormLayout());

    Section sctnProperties = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnProperties = new FormData();
    fd_sctnProperties.right = new FormAttachment(0, 648);
    fd_sctnProperties.top = new FormAttachment(0, 12);
    fd_sctnProperties.left = new FormAttachment(0, 176);
    sctnProperties.setLayoutData(fd_sctnProperties);
    managedForm.getToolkit().paintBordersFor(sctnProperties);
    sctnProperties.setText("Properties");
    sctnProperties.setExpanded(true);

    sctnFrames = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnFrames = new FormData();
    fd_sctnFrames.bottom = new FormAttachment(100, -10);
    fd_sctnFrames.top = new FormAttachment(sctnProperties, 6);
    fd_sctnFrames.right = new FormAttachment(0, 648);
    fd_sctnFrames.left = new FormAttachment(0, 176);
    sctnFrames.setLayoutData(fd_sctnFrames);
    managedForm.getToolkit().paintBordersFor(sctnFrames);
    sctnFrames.setText("Frames");
    sctnFrames.setExpanded(true);

    framesComposite = managedForm.getToolkit().createComposite(sctnFrames, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(framesComposite);
    sctnFrames.setClient(framesComposite);
    framesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

    Composite comp = managedForm.getToolkit().createComposite(sctnFrames);
    comp.setBackground(null);
    sctnFrames.setTextClient(comp);

    Composite composite = managedForm.getToolkit().createComposite(sctnProperties, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(composite);
    sctnProperties.setClient(composite);
    composite.setLayout(new GridLayout(2, false));

    Label lblName = managedForm.getToolkit().createLabel(composite, "Name", SWT.NONE);

    txtAnimationName = managedForm.getToolkit().createText(composite, "New Text", SWT.NONE);
    txtAnimationName.setEditable(false);
    txtAnimationName.setText("");
    txtAnimationName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    lblDuration = new Label(composite, SWT.NONE);
    lblDuration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    managedForm.getToolkit().adapt(lblDuration, true, true);
    lblDuration.setText("Duration");

    txtAnimationDuration = new Text(composite, SWT.BORDER);
    txtAnimationDuration.setEditable(false);
    txtAnimationDuration.setText("0.05");
    txtAnimationDuration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    managedForm.getToolkit().adapt(txtAnimationDuration, true, true);

    Label lblDefault = managedForm.getToolkit().createLabel(composite, "Default", SWT.NONE);

    btnDefaultAnimation = new Button(composite, SWT.CHECK);
    btnDefaultAnimation.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        AnimationListViewItem alvi = selectedAnimationListItem();

        if (alvi != null) {

          animController.defaultAnimationChanged(alvi, btnDefaultAnimation.getSelection());
          dirty = true;
          entityFormEditor.editorDirtyStateChanged();

        }
      }
    });
    btnDefaultAnimation.setText("Is this the default animation for this entity ?");
    managedForm.getToolkit().adapt(btnDefaultAnimation, true, true);

    mghprlnkChangeAnimationName = managedForm.getToolkit().createImageHyperlink(sctnProperties, SWT.NONE);
    mghprlnkChangeAnimationName.setBackground(null);
    mghprlnkChangeAnimationName.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {

        AnimationPropertyChangeDialog stcd = new AnimationPropertyChangeDialog(getSite().getShell(), txtAnimationName
            .getText(), txtAnimationDuration.getText());
        int resp = stcd.open();
        if (resp == AnimationPropertyChangeDialog.CANCEL) {
          return;
        }

        txtAnimationName.setText(stcd.getName());
        txtAnimationDuration.setText(stcd.getAnimationDuration());

        animController.animationNameChange(selectedAnimationListItem(), stcd.getName(), stcd.getAnimationDuration());

        dirty = true;
        entityFormEditor.editorDirtyStateChanged();
        tableViewer.refresh();
      }
    });
    mghprlnkChangeAnimationName.setImage(SharedImages.CHANGE_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkChangeAnimationName);
    sctnProperties.setTextClient(mghprlnkChangeAnimationName);
    mghprlnkChangeAnimationName.setText("");
    toolkit.decorateFormHeading(form.getForm());
    comp.setLayout(new RowLayout(SWT.HORIZONTAL));

    mghprlnkAddFrames = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    mghprlnkAddFrames.setBackground(null);
    mghprlnkAddFrames.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        addNewFramesToAnimation();
      }
    });
    mghprlnkAddFrames.setImage(SharedImages.ADD_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkAddFrames);

    mghprlnkAddFrames.setEnabled(false); // default state
    mghprlnkChangeAnimationName.setEnabled(false); // default state

    previewExternal = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(previewExternal);
    previewExternal.setBackground(null);
    previewExternal.setImage(SharedImages.RENDER.createImage());
    previewExternal.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        float duration = FloatUtil.toFloat(txtAnimationDuration.getText());
        animController.previewAnimationExternal(selectedAnimationListItem().getAnimation(), duration);
      }
    });

    Section sctnAnimations = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnAnimations = new FormData();
    fd_sctnAnimations.right = new FormAttachment(0, 170);
    fd_sctnAnimations.bottom = new FormAttachment(sctnFrames, 0, SWT.BOTTOM);
    fd_sctnAnimations.top = new FormAttachment(sctnProperties, 0, SWT.TOP);
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

    mghprlnkRemove = managedForm.getToolkit().createImageHyperlink(sctnAnimations, SWT.NONE);
    mghprlnkRemove.setBackground(null);
    mghprlnkRemove.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        MessageBox mb = new MessageBox(getSite().getShell(), SWT.YES | SWT.NO);
        mb.setMessage("Are you sure you want to remove the selected animation? This will affect all the places this animation is used.");
        int resp = mb.open();
        if (resp == SWT.NO) {
          return;
        }

        removeSelectedAnimation();
      }
    });
    mghprlnkRemove.setImage(SharedImages.REMOVE_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkRemove);
    sctnAnimations.setTextClient(mghprlnkRemove);

    tableViewer.setLabelProvider(new AnimationTableLabelProvider());
    tableViewer.setContentProvider(new AnimationTableContentProvider(animController.getAnimations()));
    tableViewer.setInput(animController.getAnimations());
    tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        handleAnimationListSeletionListener();
      }
    });

    // load animations from mode
    loadAnimationsFromModel();

    if (animController.animationsCount() == 0) {
      disableUIState();
    }
  }

  /**
   * Removes the selected animation.
   */
  private void removeSelectedAnimation() {
    AnimationListViewItem alvi = selectedAnimationListItem();

    animController.removeAnimation(alvi);

    // sync with the model
    entityFormEditor.getModel().getAnimationList().remove(alvi.getAnimation());

    dirty = true;
    entityFormEditor.editorDirtyStateChanged();

    tableViewer.refresh();

    // select
    table.select(animController.getAnimations().size() - 1);
    handleAnimationListSeletionListener();

    if (animController.animationsCount() == 0) {
      disableUIState();
    }
  }

  /**
   * Reset ui.
   */
  private void disableUIState() {
    txtAnimationName.setText("");
    txtAnimationDuration.setEnabled(false);
    mghprlnkAddFrames.setEnabled(false);
    mghprlnkChangeAnimationName.setEnabled(false);
    mghprlnkRemove.setEnabled(false);
    previewExternal.setEnabled(false);
    btnDefaultAnimation.setSelection(false);
    btnDefaultAnimation.setEnabled(false);
    resetFramesComposite();
  }

  /**
   * Reset frames composite.
   */
  private void resetFramesComposite() {
    for (int i = 0; i < btnList.size(); i++) {
      btnList.get(i).dispose();
    }

    framesComposite.layout(true);
  }

  /**
   * Handle animation list seletion listener.
   */
  private void handleAnimationListSeletionListener() {
    AnimationListViewItem ai = selectedAnimationListItem();

    if (ai == null) {
      return;
    }

    mghprlnkAddFrames.setEnabled(true);
    mghprlnkChangeAnimationName.setEnabled(true);
    resetFramesComposite();

    txtAnimationName.setText(ai.getName());
    txtAnimationDuration.setText(String.valueOf(ai.getAnimation().getAnimationDuration()));
    btnDefaultAnimation.setSelection(ai.getAnimation().isDefaultAnimation());

    if (ai.getAnimation() != null) {
      Iterator<Image> itr = ai.getFrames().iterator();
      while (itr.hasNext()) {
        Image frame = itr.next();
        addGroupFrameToFramesComposite(frame);
      }
    }
    framesComposite.layout(true);
  }

  /**
   * Selected animation list item.
   * 
   * @return the animation list view item
   */
  private AnimationListViewItem selectedAnimationListItem() {
    if (tableViewer.getSelection().isEmpty())
      return null;

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
  private void updateUIOnNewAnimationAdd(AnimationListViewItem alvi, int index) {
    resetFramesComposite();
    tableViewer.refresh();

    // select the recently created object
    table.select(index);

    handleAnimationListSeletionListener();

    mghprlnkAddFrames.setEnabled(true);
    mghprlnkChangeAnimationName.setEnabled(true);
    txtAnimationDuration.setEnabled(true);
    mghprlnkRemove.setEnabled(true);
    btnDefaultAnimation.setEnabled(true);
  }

  /**
   * Creates the animation from strip.
   * 
   * @param selectedImage
   *          the selected image
   * @param alvi
   *          the alvi
   */
  private void createAnimationFromStrip(Image selectedImage, AnimationListViewItem alvi) {
    resetFramesComposite();

    Queue<Image> strip = EntitiesUtil.createImageStrip(selectedImage);
    for (Image i : strip) {
      addGroupFrameToFramesComposite(i);
    }

    alvi.setFrames(strip);

    // update the default frame
    entityFormEditor.getModel().setDefaultFrame(strip.peek());

    framesComposite.layout(true);
    tableViewer.refresh();

  }

  /**
   * Adds the group frame to frames composite.
   * 
   * @param extractImage
   *          the extract image
   */
  private void addGroupFrameToFramesComposite(Image extractImage) {
    Button btn = managedForm.getToolkit().createButton(framesComposite, "", SWT.NONE);
    btn.setImage(extractImage);
    btnList.add(btn);
  }

  /**
   * Adds the new frames to animation.
   */
  private void addNewFramesToAnimation() {
    ImportSpriteDialog isd = new ImportSpriteDialog(getSite().getShell());
    int retval = isd.open();

    if (retval == ImportSpriteDialog.CANCEL || isd.getSelectedImage() == null) {
      return;
    }

    AnimationListViewItem alvi = selectedAnimationListItem();
    alvi.getAnimation().setAnimationResourceFile(
        ResourceFile.create(isd.getResourceFile(), isd.getResourceFileAbsolute()));
    alvi.getAnimation().setCols(isd.getCols());
    alvi.getAnimation().setRows(isd.getRows());

    createAnimationFromStrip(isd.getSelectedImage(), alvi);

    previewExternal.setEnabled(true);
    //
    dirty = true;
    entityFormEditor.editorDirtyStateChanged();
  }
}
