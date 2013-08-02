package com.laex.cg2d.entityeditor.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

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

import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.forms.widgets.Section;

public class EntitiesView extends ViewPart implements ISelectionListener, IEntityManager, EntityChangeListener {

  public static final String ID = EntityManager.ENTITIES_VIEW_ID; //$NON-NLS-1$

  private IProject selectedProject;

  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  private Map<String, Entity> entitiesMap = new HashMap<String, Entity>();

  private Map<String, Button> entitiesButtons = new HashMap<String, Button>();

  private EntityResourceChangeListener entitiesResourceListener = new EntityResourceChangeListener();

  int workToDO = 0;
  private Section sctnEntities;
  private ScrolledComposite scrolledComposite;
  private Composite entComposite;

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
      sctnEntities.setText("Entities");
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
        entComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
        
        entComposite.addListener(SWT.Resize, new Listener() {
          @Override
          public void handleEvent(Event event) {
            int height = entComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
            scrolledComposite.setMinHeight(height);
          }
        });
        
        scrolledComposite.setContent(entComposite);
        scrolledComposite.setMinSize(entComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
      }
    }

    createActions();
    initializeToolBar();
    initializeMenu();

    getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

    ResourcesPlugin.getWorkspace().addResourceChangeListener(entitiesResourceListener,
        IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);

    entitiesResourceListener.addEntityChangeListener(this);
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
    // Set the focus
  }

  private void loadNewEntity(final IResource resource) throws IOException, CoreException {
    String entityName = EntitiesUtil.getInternalName(resource.getName());
    final Entity entity = Entity.createFromFile((IFile) resource);

    if (!validateEntity(resource, entityName, entity)) {
      return;
    }

    entitiesMap.put(resource.getFullPath().toString(), entity);

    final Image i = EntitiesUtil.getDefaultFrame(entity, 1);
    String name = EntitiesUtil.getInternalName(entityName);

    entity.setDefaultFrame(i);
    entity.setInternalName(name);

    Button b = entitiesButtons.get(name);
    if (b == null || b.isDisposed()) {

      b = formToolkit.createButton(entComposite, "", SWT.None);

      DragSource dragSource = new DragSource(b, DND.DROP_COPY);
      dragSource.addDragListener(new DragSourceAdapter() {
        @Override
        public void dragSetData(DragSourceEvent event) {
          DNDFileTransfer.transferType = TransferType.ENTITY;
          DNDFileTransfer.entity = entity;
          DNDFileTransfer.entityResourceFile = (IFile) resource;
          event.data = "BOGUS";
        }
      });
      dragSource.setTransfer(new Transfer[]
        { TextTransfer.getInstance() });

      entitiesButtons.put(entityName, b);

    }

    b.setImage(ResourceManager.getImageDescriptor(i, 0.5f).createImage());
    b.setToolTipText(name);

    entComposite.layout(true);
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

    monitor.beginTask("Loading entities", workToDO);

    selectedProject.accept(new IResourceVisitor() {
      @Override
      public boolean visit(IResource resource) throws CoreException {

        if (resource.getName().endsWith(ICGCProject.ENTITIES_EXTENSION)) {

          try {
            monitor.subTask("Loading " + resource.getName());
            loadNewEntity(resource);
            monitor.worked(1);
          } catch (IOException e) {
            e.printStackTrace();
          }

        }

        return true;
      }
    });

    entComposite.layout(true);
    
    int height = 0;
    for (Button b : entitiesButtons.values()) {
      height += b.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
    }
    scrolledComposite.setMinHeight(height);
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    Object firstElement = strucSel.getFirstElement();

    if (!(firstElement instanceof IResource)) {
      return;
    }

    IProject prj = ((IResource) firstElement).getProject();

    if (prj == selectedProject) {
      /* do nothing */
      return;
    } else {
      selectedProject = prj;
    }

    clearEntities();

    try {
      // IProgressMonitor monitor =
      // getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
      ProgressMonitorDialog pmd = new ProgressMonitorDialog(getSite().getShell());
      pmd.run(false, false, new IRunnableWithProgress() {
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
      entitiesButtons.get(key).dispose();
    }

    entComposite.layout(true);
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

  @Override
  public void entityChanged(IResource resource) {

    try {
      loadNewEntity(resource);
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (CoreException e1) {
      e1.printStackTrace();
    }

  }

  void removeEntityButton(final String name) {

    getSite().getShell().getDisplay().asyncExec(new Runnable() {
      @Override
      public void run() {

        Button b = entitiesButtons.get(name);
        if (!b.isDisposed() && b.getToolTipText().equals(name)) {
          b.dispose();
        }

        entComposite.layout(true);
      }

    });

  }

  @Override
  public void entityRemoved(IResource resource) {
    Entity entity = entitiesMap.get(resource.getFullPath().toOSString());
    if (entity == null) {
      return;
    }

    final String name = EntitiesUtil.getInternalName(resource.getName());
    removeEntityButton(name);

  }
}
