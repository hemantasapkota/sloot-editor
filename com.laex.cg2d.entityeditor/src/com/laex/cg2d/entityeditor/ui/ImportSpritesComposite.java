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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import swing2swt.layout.BorderLayout;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.JsonSprite;
import com.laex.cg2d.model.model.ResourceFile;
import com.laex.cg2d.model.resources.ResourceManager;

public class ImportSpritesComposite extends Composite {

  private ResourceFile spritesheetImageFile = ResourceFile.EMPTY;

  private ResourceFile spritesheetJsonMapperFile = ResourceFile.EMPTY;

  private Image selectedImage;

  private FreeformLayeredPane flp = new FreeformLayeredPane();

  private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

  private Spinner txtRows;

  private Spinner txtTileWidth;

  protected ArrayList<EntitySpritesheetItem> spritesheetItems = new ArrayList<EntitySpritesheetItem>();

  private Queue<Image> extractedImages = new LinkedList<Image>();

  private Spinner txtCols;

  private Spinner txtTileHeight;

  private FigureCanvas figureCanvas;

  private ImportSpriteCompositeDelegate delegate;

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

  private ExtractionDataModifyListener edm = new ExtractionDataModifyListener();

  private IResource resourceContainer;

  private Button btnEnableExtract;

  private Label lblCount;

  public void setDelegate(ImportSpriteCompositeDelegate delegate) {
    this.delegate = delegate;
  }

  private void setSelectedImageToCanvas() {
    flp.removeAll();
    selectedImage = ResourceManager.getImageOfRelativePath(spritesheetImageFile.getResourceFile());

    ImageFigure iff = new ImageFigure(selectedImage);
    iff.setBounds(new Rectangle(0, 0, selectedImage.getBounds().width, selectedImage.getBounds().height));
    flp.add(iff);

    figureCanvas.setBounds(0, 0, selectedImage.getBounds().width, selectedImage.getBounds().height);

  }

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

    txtTileWidth.setSelection(tileWidth);
    txtTileHeight.setSelection(tileHeight);

    extractedImages.clear();

    List<JsonSprite> lst = new ArrayList<JsonSprite>();

    for (int y = 0; y < imgHeight; y += tileHeight) {
      for (int x = 0; x < imgWidth; x += tileWidth) {
        RectangleFigure rf = createBlockFigure(x, y, tileWidth, tileHeight);
        flp.add(rf);

        JsonSprite js = new JsonSprite("", x, y, tileWidth, tileHeight, 0, 0);
        lst.add(js);
      }
    }

    /* Only extract if selected by user */
    if (btnEnableExtract.getSelection()) {
      extractFromJSON(lst);
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

  private void clearOutline() {
    flp.removeAll();
    figureCanvas.setContents(null);
  }

  protected List<JsonSprite> openJsonFileOpener() {
    List<JsonSprite> list = new ArrayList<JsonSprite>();

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

        String spriteName = (String) sprite.get("id");
        int x = (int) Float.parseFloat(sprite.get("x").toString());
        int y = (int) Float.parseFloat(sprite.get("y").toString());
        int width = (int) Float.parseFloat(sprite.get("width").toString());
        int height = (int) Float.parseFloat(sprite.get("height").toString());

        JsonSprite js = new JsonSprite(spriteName, x, y, width, height, 0, 0);
        list.add(js);
      }

    } catch (CoreException e) {
      e.printStackTrace();
    }

    return list;
  }

  private void extractFromJSON(final List<JsonSprite> lst) {
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
            Rectangle r = new Rectangle(js.getX(), js.getY(), js.getWidth(), js.getHeight());

            EntitySpritesheetItem esi = new EntitySpritesheetItem();
            esi.setExtractBounds(RectAdapter.gdxRect(r));
            esi.setFrameIndex(index++);
            spritesheetItems.add(esi);

            final RectangleFigure rf = createBlockFigure(r.x, r.y, r.width, r.height);
            flp.add(rf);

            final ImageData id = ResourceManager.extractImageFromBounds(selectedImage.getImageData(),
                RectAdapter.swtRect(rf.getBounds()));
            extractedImages.add(ResourceManager.getImage(id));
          }

          lblCount.setText("#" + String.valueOf(extractedImages.size()));

          if (delegate != null) {
            delegate.spriteExtractionComplete(spritesheetImageFile, spritesheetJsonMapperFile, spritesheetItems,
                extractedImages);
          }
        }

      });

    } catch (InvocationTargetException e1) {
      e1.printStackTrace();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    figureCanvas.setContents(flp);
  }

  private FilteredResourcesSelectionDialog openResourceLoader(String pattern) {
    IFolder texFlder = resourceContainer.getProject().getFolder(ICGCProject.TEXTURES_FOLDER);

    FilteredResourcesSelectionDialog frsd = new FilteredResourcesSelectionDialog(getShell(), false, texFlder,
        IResource.FILE);
    frsd.setTitle("Select a image. All the images have been listed from the textures folder");
    frsd.setInitialPattern(pattern);
    return frsd;
  }

  protected void chooseImage() {
    FilteredResourcesSelectionDialog frsd = openResourceLoader("*.png");
    int resp = frsd.open();

    if (resp == FilteredResourcesSelectionDialog.CANCEL) {
      return;
    }

    IFile selectedFile = (IFile) frsd.getFirstResult();
    String resourceFile = selectedFile.getFullPath().toOSString();
    String resourceFileAbsolute = selectedFile.getLocation().makeAbsolute().toOSString();

    spritesheetImageFile = ResourceFile.create(resourceFile, resourceFileAbsolute);

    // set the image to figure canvas
    setSelectedImageToCanvas();
    updateOutline();
  }

  private void toggleExtractItems() {
    if (!btnEnableExtract.getSelection()) {
      txtRows.setEnabled(false);
      txtCols.setEnabled(false);
    } else {
      txtRows.setEnabled(true);
      txtCols.setEnabled(true);
    }
  }

  /**
   * Create the composite.
   * 
   * @param parent
   * @param style
   */
  public ImportSpritesComposite(Composite parent, int style) {
    this(null, parent, style);
  }

  public ImportSpritesComposite(IResource container, Composite parent, int style) {
    super(parent, style);

    this.resourceContainer = container;

    addDisposeListener(new DisposeListener() {
      public void widgetDisposed(DisposeEvent e) {
        toolkit.dispose();
      }
    });
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new FillLayout(SWT.HORIZONTAL));

    Composite composite = toolkit.createComposite(this, SWT.NONE);
    toolkit.paintBordersFor(composite);
    composite.setLayout(new BorderLayout(0, 0));

    Button btnChooseImage = toolkit.createButton(composite, "Choose Spritesheet", SWT.NONE);
    btnChooseImage.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        chooseImage();
      }
    });
    btnChooseImage.setLayoutData(BorderLayout.NORTH);

    Section sctnExtract = toolkit.createSection(composite, Section.EXPANDED | Section.TITLE_BAR);
    sctnExtract.setLayoutData(BorderLayout.WEST);
    toolkit.paintBordersFor(sctnExtract);
    sctnExtract.setText("Extract");
    sctnExtract.setExpanded(true);

    Composite composite_1 = toolkit.createComposite(sctnExtract, SWT.NONE);
    toolkit.paintBordersFor(composite_1);
    sctnExtract.setClient(composite_1);
    composite_1.setLayout(new RowLayout(SWT.VERTICAL));

    Label lblColumns = toolkit.createLabel(composite_1, "Columns", SWT.NONE);

    txtCols = new Spinner(composite_1, SWT.BORDER);
    txtCols.setSelection(8);
    txtCols.addModifyListener(edm);
    toolkit.adapt(txtCols);
    toolkit.paintBordersFor(txtCols);

    Label lblRows = toolkit.createLabel(composite_1, "Rows", SWT.NONE);

    txtRows = new Spinner(composite_1, SWT.BORDER);
    txtRows.setSelection(1);
    txtRows.addModifyListener(edm);

    Label lblWidth = new Label(composite_1, SWT.NONE);
    toolkit.adapt(lblWidth, true, true);
    lblWidth.setText("Tile Width");

    txtTileWidth = new Spinner(composite_1, SWT.BORDER);
    txtTileWidth.setEnabled(false);

    Label lblTileHeight = toolkit.createLabel(composite_1, "Tile Height", SWT.NONE);

    txtTileHeight = new Spinner(composite_1, SWT.BORDER);
    txtTileHeight.setEnabled(false);
    toolkit.adapt(txtTileHeight);
    toolkit.paintBordersFor(txtTileHeight);

    Label label = toolkit.createSeparator(composite_1, SWT.HORIZONTAL);

    Button btnJSON = toolkit.createButton(composite_1, "JSON", SWT.NONE);

    Label label_1 = toolkit.createSeparator(composite_1, SWT.HORIZONTAL);

    lblCount = toolkit.createLabel(composite_1, "#", SWT.NONE);

    btnEnableExtract = new Button(sctnExtract, SWT.CHECK);
    btnEnableExtract.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        toggleExtractItems();
      }
    });
    toolkit.adapt(btnEnableExtract, true, true);
    btnEnableExtract.setBackground(null);
    sctnExtract.setTextClient(btnEnableExtract);
    btnJSON.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        final List<JsonSprite> lst = openJsonFileOpener();
        extractFromJSON(lst);
      }
    });

    ScrolledComposite spriteComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    spriteComposite.setLayoutData(BorderLayout.CENTER);
    toolkit.adapt(spriteComposite);
    toolkit.paintBordersFor(spriteComposite);
    spriteComposite.setExpandHorizontal(true);
    spriteComposite.setExpandVertical(true);
    flp.setLayoutManager(new FreeformLayout());

    toolkit.adapt(spriteComposite);
    toolkit.paintBordersFor(spriteComposite);

    figureCanvas = new FigureCanvas(spriteComposite);
    flp.setLayoutManager(new FreeformLayout());

    toolkit.adapt(figureCanvas);
    toolkit.paintBordersFor(figureCanvas);
    spriteComposite.setContent(figureCanvas);
    spriteComposite.setMinSize(figureCanvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    toggleExtractItems();

  }
}
