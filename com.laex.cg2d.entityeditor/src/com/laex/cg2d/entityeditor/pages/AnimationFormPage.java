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
import java.util.List;
import java.util.Queue;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
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
import com.laex.cg2d.model.ResourceManager;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.EntityAnimation;
import com.laex.cg2d.model.model.ResourceFile;
import com.laex.cg2d.model.util.EntitiesUtil;
import com.laex.cg2d.model.util.FloatUtil;

/**
 * The Class AnimationFormPage.
 */
public class AnimationFormPage extends FormPage {

  /** The txt animation name. */
  private Text txtAnimationName;

  /** The managed form. */
  private IManagedForm managedForm;

  /** The sctn frames. */
  private Section sctnFrames;

  /** The table. */
  private Table table;

  /** The table viewer. */
  private TableViewer tableViewer;

  private List<Button> btnList = new ArrayList<Button>();

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
  private ImageHyperlink mghprlnkPreviewExternal;

  /** The export frames. */
  private ImageHyperlink mghprlnkExportFrames;

  /** The txt animation duration. */
  private Text txtAnimationDuration;

  /** The anim controller. */
  private AnimationFormPageController animController = new AnimationFormPageController();

  private ScrolledComposite scrolledComposite;

  private Composite framesComposite;

  enum UIState {
    None, LoadingFromModel, NewAnimation, AddFrames, SelectionChanged
  }

  private UIState pageState = UIState.None;

  private ImageHyperlink mghprlnkRemoveFrames;

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

        pageState = UIState.LoadingFromModel;

        updateFramesFromSpritesheet(frameImage, alvi);

        pageState = UIState.None;
      }

      updateUIOnNewAnimationAdd(alvi, animController.indexOf(alvi));
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
    form.setText("Animation");

    Action ac = new Action("Add Animation") {
      @Override
      public void run() {
        super.run();

        onClickAddAnimation();

      }
    };
    ac.setImageDescriptor(SharedImages.ADD_ITEM_2_SMALL);

    form.getToolBarManager().add(ac);
    form.getToolBarManager().update(true);
    managedForm.getForm().getBody().setLayout(new FormLayout());

    Section sctnProperties = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnProperties = new FormData();
    fd_sctnProperties.top = new FormAttachment(0, 12);
    fd_sctnProperties.right = new FormAttachment(0, 648);
    fd_sctnProperties.left = new FormAttachment(0, 176);
    sctnProperties.setLayoutData(fd_sctnProperties);
    managedForm.getToolkit().paintBordersFor(sctnProperties);
    sctnProperties.setText("Properties");
    sctnProperties.setExpanded(true);

    sctnFrames = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    fd_sctnProperties.bottom = new FormAttachment(sctnFrames, -6);
    FormData fd_sctnFrames = new FormData();
    fd_sctnFrames.bottom = new FormAttachment(100, -10);
    fd_sctnFrames.top = new FormAttachment(100, -313);
    fd_sctnFrames.right = new FormAttachment(0, 648);
    fd_sctnFrames.left = new FormAttachment(0, 176);
    sctnFrames.setLayoutData(fd_sctnFrames);
    managedForm.getToolkit().paintBordersFor(sctnFrames);
    sctnFrames.setText("Frames");

    Composite comp = managedForm.getToolkit().createComposite(sctnFrames);
    comp.setBackground(null);
    sctnFrames.setTextClient(comp);

    Composite composite = managedForm.getToolkit().createComposite(sctnProperties, SWT.BORDER);
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
    mghprlnkChangeAnimationName.setToolTipText("Edit Properties");
    mghprlnkChangeAnimationName.setBackground(null);
    mghprlnkChangeAnimationName.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {

        onEditAnimationProperties();
      }
    });
    mghprlnkChangeAnimationName.setImage(SharedImages.CHANGE_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkChangeAnimationName);
    sctnProperties.setTextClient(mghprlnkChangeAnimationName);
    mghprlnkChangeAnimationName.setText("");
    toolkit.decorateFormHeading(form.getForm());
    comp.setLayout(new RowLayout(SWT.HORIZONTAL));

    mghprlnkAddFrames = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    mghprlnkAddFrames.setToolTipText("Add Frames");
    mghprlnkAddFrames.setBackground(null);
    mghprlnkAddFrames.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        onClickAddFrames();
      }
    });
    mghprlnkAddFrames.setImage(SharedImages.ADD_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkAddFrames);

    mghprlnkRemoveFrames = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    mghprlnkRemoveFrames.setToolTipText("Remove unmarked Frames");
    mghprlnkRemoveFrames.setBackground(null);
    mghprlnkRemoveFrames.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        onClickRemoveUnmarkedFrames();
      }
    });
    mghprlnkRemoveFrames.setImage(SharedImages.REMOVE_ITEM_SMALL.createImage());
    managedForm.getToolkit().paintBordersFor(mghprlnkRemoveFrames);
    mghprlnkChangeAnimationName.setEnabled(false); // default state

    mghprlnkPreviewExternal = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    mghprlnkPreviewExternal.setEnabled(false);
    mghprlnkPreviewExternal.setToolTipText("Preview Animation");
    managedForm.getToolkit().paintBordersFor(mghprlnkPreviewExternal);
    mghprlnkPreviewExternal.setBackground(null);
    mghprlnkPreviewExternal.setImage(SharedImages.RENDER.createImage());
    mghprlnkPreviewExternal.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        float duration = FloatUtil.toFloat(txtAnimationDuration.getText());
        animController.previewAnimationExternal(selectedAnimationListItem().getAnimation(), duration);
      }
    });

    mghprlnkExportFrames = managedForm.getToolkit().createImageHyperlink(comp, SWT.NONE);
    mghprlnkExportFrames.setEnabled(false);
    mghprlnkExportFrames.setToolTipText("Export Frames");
    managedForm.getToolkit().paintBordersFor(mghprlnkPreviewExternal);
    mghprlnkExportFrames.setBackground(null);
    mghprlnkExportFrames.setImage(SharedImages.PNG_EXPORT.createImage());
    mghprlnkExportFrames.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void linkActivated(HyperlinkEvent e) {
        DirectoryDialog dd = new DirectoryDialog(getSite().getShell());

        final String dirc = dd.open();
        if (dirc == null) {
          return;
        }

        final List<Image> imgList = new ArrayList<Image>();
        for (Button b : btnList) {
          imgList.add(b.getImage());
        }

        Job job = new Job("Export frames") {
          @Override
          protected IStatus run(IProgressMonitor monitor) {

            animController.exportFrames(imgList, new Path(dirc), monitor);

            return Status.OK_STATUS;
          }
        };
        job.setSystem(false);
        job.schedule();

      }
    });

    Section sctnAnimations = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TITLE_BAR);
    FormData fd_sctnAnimations = new FormData();
    fd_sctnAnimations.top = new FormAttachment(0, 12);
    fd_sctnAnimations.bottom = new FormAttachment(100, -10);
    fd_sctnAnimations.right = new FormAttachment(0, 170);

    Composite composite_2 = managedForm.getToolkit().createComposite(sctnFrames, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(composite_2);
    sctnFrames.setClient(composite_2);
    composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

    scrolledComposite = new ScrolledComposite(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    managedForm.getToolkit().adapt(scrolledComposite);
    managedForm.getToolkit().paintBordersFor(scrolledComposite);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);

    framesComposite = managedForm.getToolkit().createComposite(scrolledComposite, SWT.NONE);
    managedForm.getToolkit().paintBordersFor(framesComposite);

    RowLayout rl_framesComposite = new RowLayout(SWT.HORIZONTAL);
    framesComposite.setLayout(rl_framesComposite);
    scrolledComposite.setContent(framesComposite);
    scrolledComposite.setMinSize(framesComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

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
    mghprlnkRemove.setToolTipText("Remove Animation");
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

        onClickRemoveSelectedAnimation();
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

  private void onClickRemoveUnmarkedFrames() {

    int i = 0, size = btnList.size();

    /*
     * While removing buttons, the size of the btnList will change. So instead
     * of using For loop, we use this while loop to ensure we remove all the
     * unmarked frames.
     */

    while (i < size) {

      Button b = btnList.get(i);

      if (b.getData() == null) {
        removeSpriteButton(b);

        i = 0;
        size = btnList.size();
        continue;
      }

      size = btnList.size();
      i++;
    }

    System.err.println(btnList.size());
    toggleFramesCompositeState();
  }

  /**
   * Removes the selected animation.
   */
  private void onClickRemoveSelectedAnimation() {
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
      clearSpriteButtons();
      resetFramesComposite();
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
    btnDefaultAnimation.setSelection(false);
    btnDefaultAnimation.setEnabled(false);
    mghprlnkRemove.setEnabled(false);
    toggleFramesCompositeState();
    resetFramesComposite();
  }

  /**
   * Reset frames composite.
   */
  private void resetFramesComposite() {
    framesComposite.layout(true);
    setupFramesCompositeScrolling(SWT.DEFAULT);
  }

  private void clearSpriteButtons() {
    for (int i = 0; i < btnList.size(); i++) {
      btnList.get(i).dispose();
    }
    btnList.clear();
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
    clearSpriteButtons();
    resetFramesComposite();

    txtAnimationName.setText(ai.getName());
    txtAnimationDuration.setText(String.valueOf(ai.getAnimation().getAnimationDuration()));
    btnDefaultAnimation.setSelection(ai.getAnimation().isDefaultAnimation());

    if (ai.getAnimation() != null) {

      int frameIndexToCheck = 1;
      Iterator<Image> itr = ai.getFrames().iterator();

      while (itr.hasNext()) {
        Image frame = itr.next();

        boolean c = ai.getAnimation().getFrameIndices().contains(frameIndexToCheck);

        if (c) {
          addSpriteFrame(frame, frameIndexToCheck);
        }

        frameIndexToCheck++;

      }
    }

    toggleFramesCompositeState();
    setupFramesCompositeScrolling(computeHeightHint(ai));
  }

  private void toggleFramesCompositeState() {
    boolean state = btnList.isEmpty() ? false : true;

    mghprlnkRemoveFrames.setEnabled(state);
    mghprlnkExportFrames.setEnabled(state);
    mghprlnkPreviewExternal.setEnabled(state);
  }

  private int computeHeightHint(AnimationListViewItem ai) {
    int heightHint = 300;

    RowLayout rl = (RowLayout) framesComposite.getLayout();

    if (!ai.getFrames().isEmpty() && btnList.size() > 0) {
      Point btnSize = btnList.get(0).computeSize(SWT.DEFAULT, SWT.DEFAULT);

      /*
       * The width of sctnFrames is 472. To get accurate height and scrolling
       * parameters, we calculate the following: 1. No of rows of buttons on the
       * frame 2. No of cols of buttons on the frame 3. The size of the buttons
       * 4. heightHint = noCols * btnHeight + noCols * RowLayout.marginBottom 5.
       * Calculate the scroll bar increment for 1 Row of buttons
       */
      int noItemsInRow = 472 / btnSize.x;

      if (noItemsInRow <= 0)
        noItemsInRow = 1;

      int noCols = btnList.size() / noItemsInRow;

      heightHint = noCols * btnSize.y + noCols * rl.marginBottom;
    }

    scrolledComposite.getVerticalBar().setIncrement(70 + rl.marginBottom);

    return heightHint;
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
   * @param index
   *          the index
   */
  private void updateUIOnNewAnimationAdd(AnimationListViewItem alvi, int index) {
    clearSpriteButtons();
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
  private void updateFramesFromSpritesheet(Image selectedImage, AnimationListViewItem alvi) {
    resetFramesComposite();

    Queue<Image> strip = EntitiesUtil.createImageStrip(selectedImage, alvi.getAnimation().getCols(), alvi
        .getAnimation().getRows());

    int frameIndexToCheck = 1;
    List<Integer> frameIndices = alvi.getAnimation().getFrameIndices();

    for (Image i : strip) {

      boolean c = frameIndices.contains(frameIndexToCheck) && pageState == UIState.LoadingFromModel;

      if (c) {
        addSpriteFrame(i, frameIndexToCheck);
      }

      /* If its a user event, we add the frames, anyways */
      if (pageState == UIState.AddFrames) {
        addSpriteFrame(i, frameIndexToCheck);
      }

      frameIndexToCheck++;
    }

    alvi.setFrames(strip);

    // update the default frame
    entityFormEditor.getModel().setDefaultFrame(strip.peek());

    setupFramesCompositeScrolling(computeHeightHint(alvi));
    framesComposite.layout(true);

    tableViewer.refresh();
  }

  private void setupFramesCompositeScrolling(int heightHint) {
    framesComposite.layout(true);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setMinSize(SWT.DEFAULT, heightHint);
  }

  /**
   * Adds the group frame to frames composite.
   * 
   * @param extractImage
   *          the extract image
   */
  private void addSpriteFrame(Image extractImage, int frameIndex) {
    final Button btn = managedForm.getToolkit().createButton(framesComposite, "", SWT.None);

    btn.setToolTipText(String.valueOf(frameIndex));
    btn.setImage(extractImage);

    btn.addPaintListener(new PaintListener() {
      @Override
      public void paintControl(PaintEvent e) {
        if (btn.getData() != null) {
          e.gc.setBackground(ColorConstants.button);
          e.gc.drawText(btn.getToolTipText(), 5, 1);
        }
      }
    });

    btn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent e) {

        removeSpriteButton(btn);

      }

      @Override
      public void mouseUp(MouseEvent e) {
        Object customData = btn.getData();

        if (customData == null) {
          btn.setData(btn.getToolTipText());
        } else {
          btn.setData(null);
        }

        framesComposite.layout(true);
      }
    });

    btnList.add(btn);
  }

  /**
   * Adds the new frames to animation.
   */
  private void onClickAddFrames() {
    ImportSpriteDialog isd = new ImportSpriteDialog(getSite().getShell());
    int retval = isd.open();

    if (retval == ImportSpriteDialog.CANCEL || isd.getSelectedImage() == null) {
      return;
    }
    
    clearSpriteButtons();

    AnimationListViewItem alvi = selectedAnimationListItem();

    alvi.getAnimation().setAnimationResourceFile(
        ResourceFile.create(isd.getResourceFile(), isd.getResourceFileAbsolute()));

    alvi.getAnimation().setCols(isd.getCols());

    alvi.getAnimation().setRows(isd.getRows());

    pageState = UIState.AddFrames;

    updateFramesFromSpritesheet(isd.getSelectedImage(), alvi);

    pageState = UIState.None;

    animController.frameIndicesChanged(alvi, computeFrameIndices());

    toggleFramesCompositeState();

    //
    dirty = true;
    entityFormEditor.editorDirtyStateChanged();
  }

  private void onEditAnimationProperties() {
    AnimationPropertyChangeDialog stcd = new AnimationPropertyChangeDialog(getSite().getShell(),
        txtAnimationName.getText(), txtAnimationDuration.getText());
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

  private void onClickAddAnimation() {
    AnimationListViewItem alvi = animController.createEmptyAnimation();

    updateUIOnNewAnimationAdd(alvi, animController.indexOf(alvi));

    entityFormEditor.getModel().addEntityAnimation(alvi.getAnimation());
    dirty = true;
    entityFormEditor.editorDirtyStateChanged();
  }

  private List<Integer> computeFrameIndices() {
    /* Compute Frame Indices */
    List<Integer> fi = new ArrayList<Integer>();
    for (Button b : btnList) {
      fi.add(Integer.parseInt(b.getToolTipText()));
    }
    return fi;
  }

  private void removeSpriteButton(final Button btn) {
    btn.dispose();
    btnList.remove(btn);

    framesComposite.layout(true);

    AnimationListViewItem alvi = selectedAnimationListItem();

    setupFramesCompositeScrolling(computeHeightHint(alvi));

    List<Integer> fi = computeFrameIndices();

    animController.frameIndicesChanged(alvi, fi);
    dirty = true;
    entityFormEditor.editorDirtyStateChanged();
  }
}
