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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.ResourceManager;
import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.ResourceFile;
import com.laex.cg2d.model.util.EntitiesUtil;

/**
 * The Class ImportSpriteDialog.
 */
public class ImportSpriteDialog extends Dialog {

  class JsonSprite {
    String spriteName;
    int x, y, width, height;
    int pivotX, pivotY;

    @Override
    public String toString() {
      return spriteName + "-" + x + "-" + y + "-" + width + "-" + height;
    }
  }

  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /** The txt cols. */
  private Spinner txtCols;

  /** The txt rows. */
  private Spinner txtRows;

  /** The sprite composite. */
  private ScrolledComposite spriteComposite;

  /** The selected image. */
  private Image selectedImage;

  private ResourceFile spritesheetImageFile = ResourceFile.EMPTY;

  private ResourceFile spritesheetJsonMapperFile = ResourceFile.EMPTY;

  /** The cols. */
  private int cols;

  /** The rows. */
  private int rows;

  /** The ok button. */
  private Button okButton;

  /** The edml. */
  private ExtractionDataModifyListener edml = new ExtractionDataModifyListener();

  /** The figure canvas. */
  private FigureCanvas figureCanvas;

  /** The flp. */
  private FreeformLayeredPane flp = new FreeformLayeredPane();

  /** The txt width. */
  private Spinner txtWidth;

  /** The txt height. */
  private Spinner txtHeight;

  private Queue<Image> extractedImages = new LinkedList<Image>();

  private Button btnJson;

  private Label lblCountSprites;

  protected ArrayList<EntitySpritesheetItem> spritesheetItems = new ArrayList<EntitySpritesheetItem>();

  /**
   * The listener interface for receiving extractionDataModify events. The class
   * that is interested in processing a extractionDataModify event implements
   * this interface, and the object created with that class is registered with a
   * component using the component's
   * <code>addExtractionDataModifyListener<code> method. When
   * the extractionDataModify event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see ExtractionDataModifyEvent
   */
  class ExtractionDataModifyListener implements ModifyListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
     * .ModifyEvent)
     */
    @Override
    public void modifyText(ModifyEvent e) {
      flp.removeAll();

      setSelectedImageToCanvas();
      updateOutline();
    }

  }

  /**
   * Create the dialog.
   * 
   * @param parentShell
   *          the parent shell
   * @wbp.parser.constructor
   */
  public ImportSpriteDialog(Shell parentShell) {
    super(parentShell);
    setShellStyle(SWT.BORDER | SWT.MAX | SWT.RESIZE);
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
    container.setLayout(new FillLayout(SWT.HORIZONTAL));

    Form frmNewForm = formToolkit.createForm(container);
    formToolkit.paintBordersFor(frmNewForm);
    frmNewForm.setText("Import Sprites from Strip");
    frmNewForm.getBody().setLayout(new FormLayout());


    Section sctnNewSection = formToolkit.createSection(frmNewForm.getBody(), Section.TITLE_BAR);
    FormData fd_sctnNewSection = new FormData();
    fd_sctnNewSection.bottom = new FormAttachment(100, -26);
    fd_sctnNewSection.top = new FormAttachment(0);
    fd_sctnNewSection.left = new FormAttachment(0, 10);
    sctnNewSection.setLayoutData(fd_sctnNewSection);
    formToolkit.paintBordersFor(sctnNewSection);
    sctnNewSection.setText("Extract");

    Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
    formToolkit.paintBordersFor(composite);
    sctnNewSection.setClient(composite);
    composite.setLayout(new GridLayout(1, false));

    Label lblColumns = formToolkit.createLabel(composite, "Columns", SWT.NONE);

    txtCols = new Spinner(composite, SWT.BORDER);
    txtCols.setSelection(8);
    txtCols.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtCols.addModifyListener(edml);

    Label lblRows = formToolkit.createLabel(composite, "Rows", SWT.NONE);

    txtRows = new Spinner(composite, SWT.BORDER);
    txtRows.setSelection(1);
    txtRows.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    Label lblWidth = new Label(composite, SWT.NONE);
    formToolkit.adapt(lblWidth, true, true);
    lblWidth.setText("Width");

    txtWidth = new Spinner(composite, SWT.BORDER);
    formToolkit.adapt(txtWidth);
    formToolkit.paintBordersFor(txtWidth);

    Label lblHeight = new Label(composite, SWT.NONE);
    formToolkit.adapt(lblHeight, true, true);
    lblHeight.setText("Height");

    txtHeight = new Spinner(composite, SWT.BORDER);
    formToolkit.adapt(txtHeight);
    formToolkit.paintBordersFor(txtHeight);

    Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
    formToolkit.adapt(label, true, true);

    btnJson = formToolkit.createButton(composite, "JSON", SWT.NONE);
    btnJson.setEnabled(false);
    btnJson.setGrayed(true);
    btnJson.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        final List<JsonSprite> lst = openJsonFileOpener();
        spritesheetItems = new ArrayList<EntitySpritesheetItem>();

        clearOutline();
        setSelectedImageToCanvas();

        ProgressMonitorDialog pmd = new ProgressMonitorDialog(getShell());
        try {

          pmd.run(false, false, new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

              int index = 0;

              for (JsonSprite js : lst) {
                Rectangle r = new Rectangle(js.x, js.y, js.width, js.height);

                EntitySpritesheetItem esi = new EntitySpritesheetItem();
                esi.setExtractBounds(RectAdapter.gdxRect(r));
                esi.setFrameIndex(index++);
                spritesheetItems.add(esi);

                final RectangleFigure rf = createBlockFigure(r.x, r.y, r.width, r.height);
                flp.add(rf);

                final ImageData id = EntitiesUtil.extractImageFromBounds(selectedImage.getImageData(), rf.getBounds());
                extractedImages.add(ResourceManager.getImage(id));
              }

              lblCountSprites.setText(String.valueOf(extractedImages.size()));
            }

          });

        } catch (InvocationTargetException e1) {
          e1.printStackTrace();
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }

        figureCanvas.setContents(flp);

      }
    });
    btnJson.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

    Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
    formToolkit.adapt(label_1, true, true);

    lblCountSprites = new Label(composite, SWT.NONE);
    formToolkit.adapt(lblCountSprites, true, true);
    lblCountSprites.setText("Count:");
    txtRows.addModifyListener(edml);

    spriteComposite = new ScrolledComposite(frmNewForm.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    FormData fd_spriteComposite = new FormData();
    fd_spriteComposite.bottom = new FormAttachment(100, -26);
    fd_spriteComposite.top = new FormAttachment(0);
    fd_spriteComposite.right = new FormAttachment(100, -10);
    fd_spriteComposite.left = new FormAttachment(sctnNewSection, 6);
    spriteComposite.setLayoutData(fd_spriteComposite);

    formToolkit.adapt(spriteComposite);
    formToolkit.paintBordersFor(spriteComposite);

    figureCanvas = new FigureCanvas(spriteComposite);
    flp.setLayoutManager(new FreeformLayout());

    formToolkit.adapt(figureCanvas);
    formToolkit.paintBordersFor(figureCanvas);
    spriteComposite.setContent(figureCanvas);
    spriteComposite.setMinSize(figureCanvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    Button btnChooseImage = formToolkit.createButton(frmNewForm.getHead(), "Choose Image", SWT.NONE);
    btnChooseImage.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        chooseImage();
        okButton.setEnabled(true);
      }
    });
    frmNewForm.setHeadClient(btnChooseImage);

    return container;
  }

  protected List<JsonSprite> openJsonFileOpener() {
    List<JsonSprite> list = new ArrayList<ImportSpriteDialog.JsonSprite>();

    FilteredResourcesSelectionDialog frsd = openResourceLoader("*.json");
    int resp = frsd.open();

    if (resp == FilteredResourcesSelectionDialog.CANCEL) {
      return list;
    }

    IFile selectedFile = (IFile) frsd.getFirstResult();

    String spritesheetJsonFile = selectedFile.getFullPath().toOSString();
    String spritesheetJsonAbs = selectedFile.getLocation().makeAbsolute().toOSString();

    spritesheetJsonMapperFile = ResourceFile.create(spritesheetJsonFile, spritesheetJsonAbs);

    JsonReader jr = new JsonReader();
    try {

      OrderedMap json = (OrderedMap) jr.parse(selectedFile.getContents());

      Entries entries = json.entries();

      while (entries.hasNext()) {
        Entry e = entries.next();

        if (e.key.equals("image") || e.key.equals("spriteCount")) {
          continue;
        }

        OrderedMap sprite = (OrderedMap) e.value;

        JsonSprite js = new JsonSprite();
        js.spriteName = (String) sprite.get("id");
        js.x = (int) Float.parseFloat(sprite.get("x").toString());
        js.y = (int) Float.parseFloat(sprite.get("y").toString());
        js.width = (int) Float.parseFloat(sprite.get("width").toString());
        js.height = (int) Float.parseFloat(sprite.get("height").toString());

        list.add(js);
      }

    } catch (CoreException e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * Choose image.
   */
  protected void chooseImage() {
    FilteredResourcesSelectionDialog frsd = openResourceLoader("*.png");
    int resp = frsd.open();

    if (resp == FilteredResourcesSelectionDialog.CANCEL) {
      btnJson.setEnabled(false);
      return;
    }

    IFile selectedFile = (IFile) frsd.getFirstResult();
    String resourceFile = selectedFile.getFullPath().toOSString();
    String resourceFileAbsolute = selectedFile.getLocation().makeAbsolute().toOSString();

    spritesheetImageFile = ResourceFile.create(resourceFile, resourceFileAbsolute);

    // set the image to figure canvas
    setSelectedImageToCanvas();
    updateOutline();

    btnJson.setEnabled(true);
  }

  private FilteredResourcesSelectionDialog openResourceLoader(String pattern) {
    IEditorInput edinp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
        .getEditorInput();

    IFolder texFlder = null;
    try {
      texFlder = CGCProject.getInstance().getTexturesFolder(edinp);
    } catch (CoreException e) {
      Activator.getDefault().getLog().log(e.getStatus());
    }

    FilteredResourcesSelectionDialog frsd = new FilteredResourcesSelectionDialog(getParentShell(), false, texFlder,
        IResource.FILE);
    frsd.setTitle("Select a image. All the images have been listed from the textures folder");
    frsd.setInitialPattern(pattern);
    return frsd;
  }

  /**
   * Sets the image to figure canvas.
   */
  private void setSelectedImageToCanvas() {
    flp.removeAll();
    selectedImage = ResourceManager.getImageOfRelativePath(spritesheetImageFile.getResourceFile());

    ImageFigure iff = new ImageFigure(selectedImage);
    iff.setBounds(new Rectangle(0, 0, selectedImage.getBounds().width, selectedImage.getBounds().height));
    flp.add(iff);

    figureCanvas.setBounds(0, 0, selectedImage.getBounds().width, selectedImage.getBounds().height);

  }

  private void clearOutline() {
    flp.removeAll();
    figureCanvas.setContents(null);
  }

  /**
   * Update outline.
   */
  private void updateOutline() {
    int cols = txtCols.getSelection();
    int rows = txtRows.getSelection();
    int imgWidth = selectedImage.getBounds().width;
    int imgHeight = selectedImage.getBounds().height;

    if (cols == 0)
      cols = 1;
    if (rows == 0)
      rows = 1;

    int tileWidth = imgWidth / cols;
    int tileHeight = imgHeight / rows;

    txtWidth.setSelection(tileWidth);
    txtHeight.setSelection(tileHeight);

    extractedImages.clear();

    for (int y = 0; y < imgHeight; y += tileHeight) {
      for (int x = 0; x < imgWidth; x += tileWidth) {
        RectangleFigure rf = createBlockFigure(x, y, tileWidth, tileHeight);
        flp.add(rf);

      }
    }

    figureCanvas.setContents(flp);
  }

  private RectangleFigure createBlockFigure(int x, int y, int tileWidth, int tileHeight) {
    RectangleFigure rf = new RectangleFigure();
    rf.setAlpha(120);
    rf.setBounds(new Rectangle(x, y, tileWidth, tileHeight));
    return rf;
  }

  public ArrayList<EntitySpritesheetItem> getSpritesheetItems() {
    return spritesheetItems;
  }

  /**
   * Gets the selected image.
   * 
   * @return the selected image
   */
  public Image getSelectedImage() {
    return selectedImage;
  }

  /**
   * Gets the cols.
   * 
   * @return the cols
   */
  public int getCols() {
    return cols;
  }

  /**
   * Gets the rows.
   * 
   * @return the rows
   */
  public int getRows() {
    return rows;
  }

  public ResourceFile getSpritesheetImageFile() {
    return spritesheetImageFile;
  }

  public ResourceFile getSpritesheetJsonMapperFile() {
    return spritesheetJsonMapperFile;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    cols = getTxtCols().getSelection();
    rows = getTxtRows().getSelection();

    /* extract images if spritesheet json file is not there */
    if (spritesheetJsonMapperFile.isEmpty()) {
      
      spritesheetItems.clear();
      extractedImages.clear();
      
      ProgressMonitorDialog pmd = new ProgressMonitorDialog(getParentShell());
      try {
        pmd.run(true, false, new IRunnableWithProgress() {
          @Override
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            int index=0;
            for (Object o : flp.getChildren()) {
              if (!(o instanceof RectangleFigure))
                continue;

              RectangleFigure rf = (RectangleFigure) o;
              ImageData id = EntitiesUtil.extractImageFromBounds(selectedImage.getImageData(), rf.getBounds());
              extractedImages.add(ResourceManager.getImage(id));
              
              EntitySpritesheetItem esi = new EntitySpritesheetItem();
              esi.setFrameIndex(index++);
              esi.setExtractBounds(RectAdapter.gdxRect(rf.getBounds()));
              spritesheetItems.add(esi);
            }
          }
        });
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    super.okPressed();
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   *          the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

    okButton.setEnabled(false);
  }

  /**
   * Return the initial size of the dialog.
   * 
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(628, 448);
  }

  /**
   * Gets the sprite composite.
   * 
   * @return the sprite composite
   */
  public ScrolledComposite getSpriteComposite() {
    return spriteComposite;
  }

  /**
   * Gets the txt cols.
   * 
   * @return the txt cols
   */
  private Spinner getTxtCols() {
    return txtCols;
  }

  /**
   * Gets the txt rows.
   * 
   * @return the txt rows
   */
  private Spinner getTxtRows() {
    return txtRows;
  }
  
  public Queue<Image> getExtractedImages() {
    return extractedImages;
  }

}
