package com.laex.cg2d.entityeditor.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import com.laex.cg2d.model.CGCProject;
import com.laex.cg2d.model.DNDFileTransfer;
import com.laex.cg2d.model.DNDFileTransfer.TransferType;
import com.laex.cg2d.model.EntityManager;
import com.laex.cg2d.model.EntityResourceChangeListener;
import com.laex.cg2d.model.EntityResourceChangeListener.EntityChangeListener;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.IEntityManager;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.ModelValidator;
import com.laex.cg2d.model.model.ModelValidatorFactory;
import com.laex.cg2d.model.resources.ResourceManager;
import com.laex.cg2d.model.util.EntitiesUtil;
import org.eclipse.swt.widgets.Combo;

public class EntitiesView extends ViewPart implements ISelectionListener, IEntityManager, EntityChangeListener,
    ControlListener {

  public static final String ID = EntityManager.ENTITIES_VIEW_ID; //$NON-NLS-1$

  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  private IProject selectedProject;

  private Map<String, Entity> entitiesMap = new HashMap<String, Entity>();

  private Map<String, Button> entitiesButtons = new HashMap<String, Button>();

  Map<String, Button> tempRemovedList = new HashMap<String, Button>();

  private EntityResourceChangeListener entitiesResourceListener = new EntityResourceChangeListener();

  int workToDO = 0;

  private Section sctnEntities;

  private ScrolledComposite scrolledComposite;

  private Composite entComposite;

  private Composite composite;
  private Text txtFilter;
  private Label label;

  public EntitiesView() {
  }

  /**
   * Create contents of the view part.
   * 
   * @param parent
   */
  @Override
  public void createPartControl(Composite parent) {
    {
      sctnEntities = formToolkit.createSection(parent, Section.TITLE_BAR);
      formToolkit.paintBordersFor(sctnEntities);
      sctnEntities.setExpanded(true);
      {
        scrolledComposite = new ScrolledComposite(sctnEntities, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        formToolkit.adapt(scrolledComposite);
        formToolkit.paintBordersFor(scrolledComposite);
        sctnEntities.setClient(scrolledComposite);

        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        entComposite = formToolkit.createComposite(scrolledComposite, SWT.NONE);
        formToolkit.paintBordersFor(entComposite);
      }
      {
        composite = formToolkit.createComposite(sctnEntities, SWT.NONE);
        composite.setBackground(null);
        formToolkit.paintBordersFor(composite);
        sctnEntities.setTextClient(composite);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));

        ImageHyperlink mghprlnkNewImagehyperlink = formToolkit.createImageHyperlink(composite, SWT.NONE);
        mghprlnkNewImagehyperlink.addHyperlinkListener(new IHyperlinkListener() {
          public void linkActivated(HyperlinkEvent e) {
            loadAllEntitiesWithProgress();
          }

          public void linkEntered(HyperlinkEvent e) {
          }

          public void linkExited(HyperlinkEvent e) {
          }
        });
        mghprlnkNewImagehyperlink.setBackground(null);
        formToolkit.paintBordersFor(mghprlnkNewImagehyperlink);
        mghprlnkNewImagehyperlink.setText("Reload");
      }

      txtFilter = formToolkit.createText(sctnEntities, "", SWT.NONE);
      txtFilter.setText("");
      txtFilter.setMessage("Filter entities");
      txtFilter.addModifyListener(new ModifyListener() {
        @Override
        public void modifyText(ModifyEvent e) {
          doFilter();
        }
      });

      sctnEntities.setDescriptionControl(txtFilter);

      label = formToolkit.createSeparator(sctnEntities, SWT.HORIZONTAL);
      sctnEntities.setSeparatorControl(label);
    }

    createActions();
    initializeToolBar();
    initializeMenu();

    getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

    ResourcesPlugin.getWorkspace().addResourceChangeListener(entitiesResourceListener,
        IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);

    entitiesResourceListener.addEntityChangeListener(this);
  }

  protected void doFilter() {
    String text = txtFilter.getText();
    boolean empty = StringUtils.isEmpty(text);

    for (String key : entitiesButtons.keySet()) {
      Button b = entitiesButtons.get(key);

      String entityName = EntitiesUtil.getDisplayName(new Path(key)).toLowerCase();

      RowData rd = new RowData();

      if (entityName.contains(text)) {
        b.setVisible(true);
        rd.exclude = false;
      } else {
        b.setVisible(false);
        rd.exclude = true;
      }

      if (empty) {
        b.setVisible(true);
        rd.exclude = false;
      }

      b.setLayoutData(rd);

    }

    setRowLayout();

  }

  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
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

  @Override
  public void setFocus() {
  }

  private void onEntityButtonClick(Button b) {

    Image newImage = ResourceManager.getImage(ResourceManager.rotate(b.getImage().getImageData(), SWT.RIGHT));

    b.setImage(newImage);
    b.setSize(newImage.getBounds().width, newImage.getBounds().height);

    entComposite.layout(true);

  }

  private void loadNewEntity(final IResource resource) throws IOException, CoreException {
    final Entity entity = Entity.createFromFile((IFile) resource);
    final String entityName = EntitiesUtil.getInternalName(resource.getFullPath());

    if (!validateEntity(resource, entityName, entity)) {
      return;
    }

    entitiesMap.put(entityName, entity);

    final Image imgScaled = ResourceManager.getImageDescriptor(entity.getDefaultFrame(), 0.5f).createImage();

    Button b = entitiesButtons.get(entityName);

    if (b == null || b.isDisposed()) {
      b = formToolkit.createButton(entComposite, "", SWT.None);

      b.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
          onEntityButtonClick((Button) e.getSource());
        };
      });

      DragSource dragSource = new DragSource(b, DND.DROP_COPY);
      dragSource.addDragListener(new DragSourceAdapter() {
        @Override
        public void dragSetData(DragSourceEvent event) {
          DNDFileTransfer.transferType = TransferType.ENTITY;
          DNDFileTransfer.entityResourceFile = (IFile) resource;
          event.data = "BOGUS";
        }
      });
      dragSource.setTransfer(new Transfer[]
        { TextTransfer.getInstance() });
      entitiesButtons.put(entityName, b);
    }

    b.setImage(imgScaled);
    b.setToolTipText(entityName);

    /* Compute height hint */
    updateCount();
    setRowLayout();
  }

  private boolean validateEntity(final IResource resource, String entityName, final Entity entity) {
    ModelValidator entityValidator = ModelValidatorFactory.getValidator(Entity.class, entity);
    boolean isValid = entityValidator.isValid();
    if (!isValid) {
      entitiesMap.remove(resource.getFullPath().toOSString());
      removeEntityButton(entityName);
    }
    return isValid;
  }

  private void calculateNoOfEntitiesToLoad() {
    workToDO = 0;
    try {
      selectedProject.accept(new IResourceVisitor() {
        @Override
        public boolean visit(IResource resource) throws CoreException {
          if (resource.getName().endsWith(ICGCProject.ENTITIES_EXTENSION)) {
            workToDO++;
          }
          return true;
        }
      });
    } catch (CoreException e) {
      e.printStackTrace();
    }

  }

  private void loadEntities(final IProgressMonitor monitor) throws CoreException {
    /* pass 1: Calculate the amount to work */
    calculateNoOfEntitiesToLoad();
    clearEntities();

    if (workToDO <= 0) {
      return;
    }

    monitor.beginTask("Loading entities", workToDO);

    EntitiesUtil.startCacheSpritesheets();

    selectedProject.accept(new IResourceVisitor() {
      @Override
      public boolean visit(final IResource resource) throws CoreException {

        /* Cancel the task */
        if (monitor.isCanceled()) {
          return false;
        }

        if (!resource.getName().endsWith(ICGCProject.ENTITIES_EXTENSION)) {
          return true;
        }

        monitor.subTask("Loading " + resource.getName());
        try {
          loadNewEntity(resource);
        } catch (IOException e) {
          e.printStackTrace();
        }
        monitor.worked(1);

        return true;
      }
    });

    EntitiesUtil.stopCacheSpritesheet();

    setRowLayout();
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    Object firstElement = strucSel.getFirstElement();

    if (!(firstElement instanceof IResource)) {
      return;
    }

    final IResource res = (IResource) firstElement;

    boolean isScreen = res.getFileExtension() != null && res.getFileExtension().equals(ICGCProject.SCREEN_EXTENSION);
    if (!isScreen) {
      return;
    }

    final IProject prj = res.getProject();

    if (selectedProject != prj) {
      /* Start our entities loader */
      selectedProject = prj;
      loadAllEntitiesWithProgress();
    }

  }

  private void loadAllEntitiesWithProgress() {

    if (selectedProject == null) {
      IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
      if (activeEditor != null) {
        selectedProject = CGCProject.getInstance().getCurrentProject(activeEditor.getEditorInput());
      } else {
        MessageDialog.openInformation(getSite().getShell(), "Select a resoure",
            "Select a screen file from the project you want to load the entities or open an entity file.");
        return;
      }
    }

    ProgressMonitorDialog pmd = new ProgressMonitorDialog(getSite().getShell());
    try {
      pmd.run(false, true, new IRunnableWithProgress() {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

          try {
            loadEntities(monitor);
          } catch (CoreException e) {
            e.printStackTrace();
          }
        }
      });
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void clearEntities() {

    for (String key : entitiesButtons.keySet()) {
      Button b = entitiesButtons.get(key);
      b.dispose();
      b = null;
    }
    entitiesButtons.clear();

    updateCount();
    setRowLayout();
  }

  @Override
  public void dispose() {
    getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(entitiesResourceListener);

    super.dispose();
  }

  @Override
  public Entity findEntity(String key) {
    return entitiesMap.get(key);
  }

  @Override
  public void removeEntity(String key) {
    entitiesMap.remove(key);
  }

  private void runInMainThread(Runnable r) {
    getSite().getShell().getDisplay().asyncExec(r);
  }

  @Override
  public void entityChanged(final IResource resource) {

    runInMainThread(new Runnable() {
      @Override
      public void run() {
        try {

          loadNewEntity(resource);

        } catch (IOException e) {
          e.printStackTrace();
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }
    });

  }

  void removeEntityButton(final String name) {

    Button b = entitiesButtons.get(name);
    if (b != null && !b.isDisposed() && b.getToolTipText().equals(name)) {
      b.dispose();
      b = null;
    }

    entitiesButtons.remove(name);
    entitiesMap.get(name).dispose();
    entitiesMap.remove(name);

    updateCount();
    setRowLayout();
  }

  @Override
  public void entityRemoved(final IResource resource) {
    runInMainThread(new Runnable() {

      @Override
      public void run() {

        Entity entity = entitiesMap.get(resource.getFullPath().toOSString());
        if (entity == null) {
          return;
        }

        final String name = EntitiesUtil.getInternalName(resource.getFullPath());
        removeEntityButton(name);
      }
    });

  }

  private void updateCount() {
    sctnEntities.setText("Count: " + entitiesButtons.values().size());
    sctnEntities.redraw();
  }

  private void setRowLayout() {
    /*
     * Row Layout on EntComposite should be set once all the components have
     * been created
     */
    RowLayout rl = new RowLayout(SWT.HORIZONTAL);
    rl.wrap = true;
    entComposite.setLayout(rl);
    entComposite.layout(true);

    scrolledComposite.setContent(entComposite);

    Rectangle r = scrolledComposite.getClientArea();
    scrolledComposite.setMinSize(entComposite.computeSize(r.width, SWT.DEFAULT));

    scrolledComposite.removeControlListener(this);
    scrolledComposite.addControlListener(this);
  }

  @Override
  public void controlMoved(ControlEvent e) {
  }

  @Override
  public void controlResized(ControlEvent e) {
    Rectangle r = scrolledComposite.getClientArea();
    scrolledComposite.setMinSize(entComposite.computeSize(r.width, SWT.DEFAULT));
  }
}
