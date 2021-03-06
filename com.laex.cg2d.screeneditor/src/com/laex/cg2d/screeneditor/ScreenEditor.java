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
package com.laex.cg2d.screeneditor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.badlogic.gdx.math.Rectangle;
import com.laex.cg2d.model.DNDFileTransfer;
import com.laex.cg2d.model.EntityManager;
import com.laex.cg2d.model.EntityResourceChangeListener;
import com.laex.cg2d.model.EntityResourceChangeListener.EntityChangeListener;
import com.laex.cg2d.model.ILayerManager;
import com.laex.cg2d.model.IScreenEditorState;
import com.laex.cg2d.model.IScreenPropertyManager;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.model.adapter.ColorAdapter;
import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ModelValidatorFactory;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.commands.LayerAddCommand;
import com.laex.cg2d.screeneditor.commands.LayerChangeOrderCommand;
import com.laex.cg2d.screeneditor.commands.LayerChangePropertiesCommand;
import com.laex.cg2d.screeneditor.commands.LayerRemoveCommand;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand.DeleteCommandType;
import com.laex.cg2d.screeneditor.editparts.CardEditPart;
import com.laex.cg2d.screeneditor.editparts.ScreenEditPartFactory;
import com.laex.cg2d.screeneditor.editparts.tree.LayerOutlineTreeEPFactory;
import com.laex.cg2d.screeneditor.editparts.tree.ScreenTreeEPFactory;
import com.laex.cg2d.screeneditor.model.CGScreenModelAdapter;
import com.laex.cg2d.screeneditor.model.ScreenModelAdapter;
import com.laex.cg2d.screeneditor.model.ShapeAdapter;
import com.laex.cg2d.screeneditor.model.ShapeCopier;
import com.laex.cg2d.screeneditor.palette.ScreenEditorPaletteFactory;
import com.laex.cg2d.screeneditor.palette.ShapeCreationFactory;
import com.laex.cg2d.screeneditor.palette.ShapeCreationInfo;
import com.laex.cg2d.screeneditor.views.IScreenDisposeListener;
import com.laex.cg2d.screeneditor.views.LayerOutlineViewPart;
import com.laex.cg2d.screeneditor.views.LayersViewPart;

/**
 * The Class ScreenEditor.
 */
public class ScreenEditor extends GraphicalEditorWithFlyoutPalette implements ILayerManager, IScreenPropertyManager,
    IScreenEditorState {

  /** The Constant ID. */
  public static final String ID = "com.laex.cg2d.screeneditor.ScreenEditor";

  /** The palette model. */
  private static PaletteRoot PALETTE_MODEL;

  /** The Constant CARD_LAYER. */
  private static final String CARD_LAYER = "Card Layer";

  /** The card layer. */
  private ScalableFreeformLayeredPane cardLayer;

  /** The scalable root edit part. */
  private ScalableFreeformRootEditPart scalableRootEditPart;

  /** The resource listener. */
  private EntityResourceChangeListener resourceListener = new EntityResourceChangeListener();

  /** The model. */
  private GameModel model;

  /** The grid state. */
  private boolean gridState = true;

  /** The card height. */
  private int x, y, cardWidthh, cardHeight;

  /** The card bg color. */
  private Color cardBgColor;

  /**
   * ScreenOutineView. General outline view that shows all the editparts in the
   * screen viewer.
   * 
   * @author hemantasapkota
   * 
   */
  class ScreenOutlineView extends ContentOutlinePage {

    /**
     * Instantiates a new screen outline view.
     * 
     * @param viewer
     *          the viewer
     */
    public ScreenOutlineView(EditPartViewer viewer) {
      super(viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.gef.ui.parts.ContentOutlinePage#createControl(org.eclipse
     * .swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
      getViewer().createControl(parent);
      getViewer().setEditDomain(getEditDomain());
      getViewer().setEditPartFactory(new ScreenTreeEPFactory());
      getSelectionSynchronizer().addViewer(getViewer());
      getViewer().setContents(getModel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.Page#dispose()
     */
    @Override
    public void dispose() {
      getSelectionSynchronizer().removeViewer(getViewer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.ContentOutlinePage#getControl()
     */
    @Override
    public Control getControl() {
      return getViewer().getControl();
    }

  }

  /**
   * LayerOutlineView. Shows the list of layers.Auto selects the appropriate
   * layer when an edit part is selected.
   * 
   * @author hemantasapkota
   * 
   */
  public class LayerOutlineView extends ScreenOutlineView {

    /**
     * Instantiates a new layer outline view.
     * 
     * @param viewer
     *          the viewer
     */
    public LayerOutlineView(EditPartViewer viewer) {
      super(viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.laex.cg2d.screeneditor.ScreenEditor.ScreenOutlineView#createControl
     * (org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
      getViewer().createControl(parent);
      getViewer().setEditDomain(getEditDomain());
      getViewer().setEditPartFactory(new LayerOutlineTreeEPFactory());
      getSelectionSynchronizer().addViewer(getViewer());
      getViewer().setContents(getModel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.laex.cg2d.screeneditor.ScreenEditor.ScreenOutlineView#dispose()
     */
    @Override
    public void dispose() {
      getSelectionSynchronizer().removeViewer(getViewer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.laex.cg2d.screeneditor.ScreenEditor.ScreenOutlineView#getControl()
     */
    @Override
    public Control getControl() {
      return getViewer().getControl();
    }

  }

  /**
   * Instantiates a new screen editor.
   */
  public ScreenEditor() {
    setEditDomain(new DefaultEditDomain(this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
   */
  @Override
  protected void createActions() {
    super.createActions();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
   */
  protected void configureGraphicalViewer() {
    GraphicalViewer viewer = getGraphicalViewer();
    viewer.setEditPartFactory(new ScreenEditPartFactory());
    viewer.setEditDomain(getEditDomain());

    scalableRootEditPart = getScalableFreeFormRootEditPart();
    getGraphicalViewer().setRootEditPart(scalableRootEditPart);

    toggleGrid();
    setGridDimension(16, 16);

    // Zoom manager for now
    setupZoomManager();

    // configure the context menu provider
    ContextMenuProvider cmProvider = new ScreenEditorContextMenuProvider(viewer, getActionRegistry());
    viewer.setContextMenu(cmProvider);
    getSite().registerContextMenu(cmProvider, viewer);

    GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer);
    viewer.setKeyHandler(keyHandler);

    // Notify other views of this viewer's disposal
    getGraphicalViewer().getControl().addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent e) {
        IViewPart vp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LayersViewPart.ID);
        if (vp != null) {
          IScreenDisposeListener isdp = (IScreenDisposeListener) vp;
          isdp.screenDisposed();
        }
      }
    });

    super.configureGraphicalViewer();
  }

  /**
   * Setup zoom manager.
   */
  private void setupZoomManager() {
    ZoomManager manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString());
    manager.addZoomListener(new ZoomListener() {
      @Override
      public void zoomChanged(double zoom) {
        updateCardLayerZoom(zoom);
      }
    });
    manager.setZoom(1);

    // Scroll-wheel Zoom
    getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
  }

  /**
   * Gets the scalable free form root edit part.
   * 
   * @return the scalable free form root edit part
   */
  private ScalableFreeformRootEditPart getScalableFreeFormRootEditPart() {
    ScalableFreeformRootEditPart rep = new ScalableFreeformRootEditPart() {
      @Override
      protected void createLayers(LayeredPane layeredPane) {

        cardLayer = new ScalableFreeformLayeredPane();
        layeredPane.addLayerBefore(cardLayer, CARD_LAYER, LayerConstants.GRID_LAYER);
        updateCardLayer(x, y, cardWidthh, cardHeight, cardBgColor);

        super.createLayers(layeredPane);

        /* Create Joint Anchor Layer after other layers are created */
      }

    };

    super.configureGraphicalViewer();

    return rep;
  }

  /**
   * Sets the grid dimension.
   * 
   * @param width
   *          the width
   * @param height
   *          the height
   */
  public void setGridDimension(int width, int height) {
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(width, height));
  }

  /**
   * Gets the grid state.
   * 
   * @return the grid state
   */
  public boolean getGridState() {
    return (Boolean) getGraphicalViewer().getProperty(SnapToGrid.PROPERTY_GRID_VISIBLE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util.
   * EventObject)
   */
  public void commandStackChanged(EventObject event) {
    firePropertyChange(IEditorPart.PROP_DIRTY);
    super.commandStackChanged(event);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
   * createPaletteViewerProvider()
   */
  protected PaletteViewerProvider createPaletteViewerProvider() {
    return new PaletteViewerProvider(getEditDomain()) {
      protected void configurePaletteViewer(PaletteViewer viewer) {
        super.configurePaletteViewer(viewer);
        viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
      }
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor
   * )
   */
  public void doSave(IProgressMonitor monitor) {
    try {

      IFile file = ((FileEditorInput) getEditorInput()).getFile();
      performSave(file, monitor);

    } catch (CoreException ce) {
      Activator.log(ce);
    } catch (IOException e) {
      Activator.log(e);
    }
  }

  /**
   * Perform save.
   * 
   * @param file
   *          the file
   * @param monitor
   *          the monitor
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void performSave(IFile file, IProgressMonitor monitor) throws CoreException, IOException {
    // Use existing preferences
    file.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

    CGScreenPreferences screenPrefsExisting = CGScreenModel.parseFrom(file.getContents()).getScreenPrefs();
    CGScreenModel cgGameModel = new CGScreenModelAdapter(model, screenPrefsExisting).asCGGameModel();
    file.setContents(new ByteArrayInputStream(cgGameModel.toByteArray()), true, false, monitor);
    getCommandStack().markSaveLocation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#dispose()
   */
  @Override
  public void dispose() {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceListener);
    super.dispose();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(java
   * .lang.Class)
   */
  public Object getAdapter(Class type) {
    if (type == ShapeAdapter.class) {
      return Boolean.TRUE;
    }

    if (type == GameModel.class) {
      return model;
    }

    if (type == ZoomManager.class) {
      return getGraphicalViewer().getProperty(ZoomManager.class.toString());
    }

    if (type == IContentOutlinePage.class) {
      // Screen Outline View
      ScreenOutlineView ov = new ScreenOutlineView(new TreeViewer());
      return ov;
    }

    if (type == LayerOutlineViewPart.class) {
      return new LayerOutlineView(new TreeViewer());
    }

    return super.getAdapter(type);
  }

  /**
   * Gets the model.
   * 
   * @return the model
   */
  public GameModel getModel() {
    return model;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
   */
  protected PaletteRoot getPaletteRoot() {
    if (PALETTE_MODEL == null) {
      PALETTE_MODEL = ScreenEditorPaletteFactory.createPalette();

    }
    return PALETTE_MODEL;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
   * initializeGraphicalViewer()
   */
  protected void initializeGraphicalViewer() {
    super.initializeGraphicalViewer();
    GraphicalViewer viewer = getGraphicalViewer();
    viewer.setContents(getModel().getDiagram());

    // listen for dropped parts
    viewer.addDropTargetListener(new AbstractTransferDropTargetListener(viewer) {

      private ShapeCreationInfo creationInfo;

      @Override
      public boolean isEnabled(DropTargetEvent event) {
        boolean isCurrentLayerLocked = getModel().getDiagram().isCurrentLayerLocked();
        return !isCurrentLayerLocked;
      }

      @Override
      protected Request createTargetRequest() {
        CreateRequest cr = new CreateRequest();
        cr.setFactory(new ShapeCreationFactory(creationInfo));
        return cr;
      }

      @Override
      protected void updateTargetRequest() {
        ((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
      }

      @Override
      protected void handleDragOver() {
        getCurrentEvent().detail = DND.DROP_COPY;
        super.handleDragOver();
      }

      @Override
      protected void handleDrop() {
        switch (DNDFileTransfer.transferType) {

        case TEXTURE:
          creationInfo = new ShapeCreationInfo.Builder().setBackgroundResourceFile(DNDFileTransfer.file)
              .setEditorShapeType(EditorShapeType.BACKGROUND_SHAPE).build();
          break;

        case ENTITY:
          creationInfo = new ShapeCreationInfo.Builder().setEditorShapeType(EditorShapeType.ENTITY_SHAPE)
              .setEntityResourceFile(DNDFileTransfer.entityResourceFile).build();
          break;

        case NONE:
          break;

        default:
          break;
        }

        super.handleDrop();
      }

      @Override
      public Transfer getTransfer() {
        return TextTransfer.getInstance();
      }

    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#isSaveAsAllowed()
   */
  public boolean isSaveAsAllowed() {
    return false;
  }

  /**
   * Close editor.
   */
  private void closeEditor() {
    getSite().getShell().getDisplay().asyncExec(new Runnable() {
      @Override
      public void run() {
        getSite().getPage().closeEditor(ScreenEditor.this, false);
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
   */
  protected void setInput(final IEditorInput input) {
    setupResChangeListenerForDelete(input.getName());

    try {

      setupEntityResourceListener(input);
      loadScreenModel(input);

    } catch (CoreException e) {
      Activator.log(e);
    } catch (IOException e) {
      Activator.log(e);
    } catch (InvocationTargetException e) {
      Activator.log(e);
    } catch (InterruptedException e) {
      Activator.log(e);
    }

    super.setInput(input);
  }

  /**
   * Load screen model.
   *
   * @param input the input
   * @throws CoreException the core exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws InvocationTargetException the invocation target exception
   * @throws InterruptedException the interrupted exception
   */
  private void loadScreenModel(final IEditorInput input) throws CoreException, IOException, InvocationTargetException,
      InterruptedException {
    final IFile file = ((IFileEditorInput) input).getFile();

    ProgressMonitorDialog pmd = new ProgressMonitorDialog(getSite().getShell());
    pmd.run(false, false, new IRunnableWithProgress() {
      @Override
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

        try {

          monitor.beginTask("Parse screen model", 1);

          CGScreenModel cgGameModel = CGScreenModel.parseFrom(file.getContents());
          model = ScreenModelAdapter.asGameModel(cgGameModel);
          monitor.worked(1);

          x = cgGameModel.getScreenPrefs().getCardPrefs().getCardNoX();
          y = cgGameModel.getScreenPrefs().getCardPrefs().getCardNoY();
          cardWidthh = cgGameModel.getScreenPrefs().getCardPrefs().getCardWidth();
          cardHeight = cgGameModel.getScreenPrefs().getCardPrefs().getCardHeight();
          cardBgColor = ColorAdapter.swtColor(cgGameModel.getScreenPrefs().getBackgroundColor());

          monitor.done();
        } catch (IOException e) {
          Activator.log(e);
        } catch (CoreException e) {
          Activator.log(e);
        }

      }
    });

    /* Go Ahead and activate the edit parts. */

    setPartName(file.getProject().getName() + "/" + file.getName());
  }

  /**
   * Sets the up res change listener for delete.
   *
   * @param resName the new up res change listener for delete
   */
  private void setupResChangeListenerForDelete(final String resName) {

    ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
      @Override
      public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
          try {
            event.getDelta().accept(new IResourceDeltaVisitor() {
              @Override
              public boolean visit(IResourceDelta delta) throws CoreException {
                if (delta.getKind() == IResourceDelta.REMOVED) {
                  if (resName.equals(delta.getResource().getName())) {
                    closeEditor();
                  }
                }
                return true;
              }
            });
          } catch (CoreException e) {
            Activator.log(e);
          }
        }
      }
    }, IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);
  }

  /**
   * Sets the up entity resource listener.
   *
   * @param input the new up entity resource listener
   * @throws CoreException the core exception
   */
  private void setupEntityResourceListener(final IEditorInput input) throws CoreException {
    ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener,
        IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);
    // if there are any changes in the entities, load em up in the editor

    resourceListener.addEntityChangeListener(new EntityChangeListener() {
      @Override
      public void entityChanged(final IResource resource) {
        /*
         * It is important to execute pallete creation safely because it
         * accesses GEF UI thread
         */
        SafeRunnable.run(new ISafeRunnable() {

          @Override
          public void run() throws Exception {
            // Check if the resource is valid entity or not.
            String entityPath = resource.getFullPath().toOSString();
            Entity e = EntityManager.entityManager().findEntity(entityPath);

            boolean isValid = ModelValidatorFactory.getValidator(Entity.class, e).isValid();

            if (!isValid) {
              removeDeletedOrInvalidEntities(resource);
              return;
            }

            /*
             * Recompute the frame for entities that might have changed
             * Essentially, were re-creating a new shape by copying the old one.
             * Following are the considerations for this: 1. The id for the new
             * shape should be same as the old one 2. The rectagle bounds for
             * the new shape should reflect the new image
             */
            CompoundCommand cc = new CompoundCommand();
            for (Shape sh : model.getDiagram().getChildren()) {

              if (!sh.getEditorShapeType().isEntity()) {
                continue;
              }

              boolean isEntityUsed = entityPath.equals(sh.getEntityResourceFile().getResourceFile());
              if (isEntityUsed) {
                ShapeCopier copier = new ShapeCopier();
                Shape newShape = (Shape) copier.copy(sh);
                newShape.setId(sh.getId()); /* We use the same id */

                /* update bounds */
                Rectangle r = newShape.getBounds();
                r.setWidth(e.getDefaultFrame().getBounds().width);
                r.setHeight(e.getDefaultFrame().getBounds().height);
                newShape.setBounds(r);
                /* end update bounds */

                ShapeDeleteCommand sdc = new ShapeDeleteCommand(model.getDiagram(), sh, DeleteCommandType.NON_UNDOABLE);
                ShapeCreateCommand scc = new ShapeCreateCommand(newShape, model.getDiagram());
                cc.add(sdc);
                cc.add(scc);
              }
            }
            getCommandStack().execute(cc);

          }

          @Override
          public void handleException(Throwable exception) {
            exception.printStackTrace();
          }

        });
      }

      @Override
      public void entityRemoved(final IResource resource) {
        /*
         * Important: entity Removed is called in resource listener. Resource
         * listener might be run in whatever thread it was triggered in. So in
         * order to remove the entities from the pallette, we need to asyncExec
         * deletion. This will prevent invalid thread access error.
         */
        getSite().getShell().getDisplay().asyncExec(new Runnable() {
          @Override
          public void run() {
            removeDeletedOrInvalidEntities(resource);
            /* Also remove this entity from the map */
            EntityManager.entityManager().removeEntity(resource.getFullPath().toOSString());
          }
        });
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ILayerManager#addLayer(com.laex.cg2d.shared.model.
   * Layer)
   */
  @Override
  public void addLayer(Layer newLayer) {
    getEditDomain().getCommandStack().execute(new LayerAddCommand(newLayer, getModel().getDiagram()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ILayerManager#changeLayerProperties(com.laex.cg2d.
   * shared.model.Layer)
   */
  @Override
  public void changeLayerProperties(Layer newLayer) {
    getEditDomain().getCommandStack().execute(new LayerChangePropertiesCommand(newLayer, getModel().getDiagram()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ILayerManager#changeLayerOrder(com.laex.cg2d.shared
   * .model.Layer[])
   */
  @Override
  public void changeLayerOrder(Layer[] orderedLayers) {
    getEditDomain().getCommandStack().execute(new LayerChangeOrderCommand(getModel().getDiagram(), orderedLayers));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.ILayerManager#removeLayer(com.laex.cg2d.shared.model
   * .Layer)
   */
  @Override
  public void removeLayer(Layer layer) {
    getEditDomain().getCommandStack().execute(new LayerRemoveCommand(layer, getModel().getDiagram()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.model.ILayerManager#removeAll()
   */
  @Override
  public void removeAllLayers() {
    CompoundCommand cc = new CompoundCommand();
    for (Layer l : getModel().getDiagram().getLayers()) {
      LayerRemoveCommand lrc = new LayerRemoveCommand(l, getModel().getDiagram());
      cc.add(lrc);
    }
    getEditDomain().getCommandStack().execute(cc);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#updateCardLayer(int, int, int, int)
   */
  @Override
  public void updateCardLayer(int noX, int noY, int cardWidth, int cardHeight, Color bgColor) {
    cardLayer.removeAll();

    for (int i = 0; i < noY; i++) {
      for (int j = 0; j < noX; j++) {
        CardEditPart cep = new CardEditPart(j, i, cardWidth, cardHeight, bgColor);
        cardLayer.add(cep.getFigure());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#updateCardLayerZoom(double)
   */
  @Override
  public void updateCardLayerZoom(double zoom) {
    cardLayer.setScale(zoom);
  }

  /**
   * Use the command stack and delete commands to remove the entities.
   * 
   * @param resource
   *          the resource
   */
  private void removeDeletedOrInvalidEntities(final IResource resource) {
    // Remove the deleted entites from the screen
    CompoundCommand cc = new CompoundCommand();
    for (Shape s : model.getDiagram().getChildren()) {

      if (s.getEditorShapeType().isEntity()) {

        String entResFile = s.getEntityResourceFile().getResourceFile();
        String resFile = resource.getFullPath().toOSString();

        if (entResFile.equals(resFile)) {
          
          ShapeDeleteCommand sdc = new ShapeDeleteCommand(model.getDiagram(), s, DeleteCommandType.NON_UNDOABLE);
          cc.add(sdc);
          
        }

      }
    }

    if (!cc.isEmpty())
      getCommandStack().execute(cc);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.IScreenPropertyManager#updateScreenProperties(java
   * .util.Map)
   */
  @Override
  public void updateScreenProperties(CGScreenPreferences screenPrefs) {
    IFile file = ((FileEditorInput) getEditorInput()).getFile();

    try {

      CGScreenModel cgGameModel = new CGScreenModelAdapter(model, screenPrefs).asCGGameModel();
      file.setContents(new ByteArrayInputStream(cgGameModel.toByteArray()), true, false, null);
      getCommandStack().markSaveLocation();

    } catch (CoreException e) {
      Activator.log(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#getNewLayerId()
   */
  @Override
  public int getNewLayerId() {
    if (model.getDiagram().getLayers().isEmpty()) {
      return 1;
    }
    int id = model.getDiagram().getLayers().size() + 1;
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#getCurrentLayer()
   */
  @Override
  public Layer getCurrentLayer() {
    for (Layer layer : model.getDiagram().getLayers()) {
      if (layer.isCurrent()) {
        return layer;
      }
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#layerCount()
   */
  @Override
  public int layerCount() {
    return getModel().getDiagram().getLayers().size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.ILayerManager#getLayerAt(int)
   */
  @Override
  public Layer getLayerAt(int index) {
    return (Layer) getModel().getDiagram().getLayers().toArray()[index];
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.model.IScreenEditorState#toggleGrid()
   */
  @Override
  public void toggleGrid() {
    gridState = !gridState;

    getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, gridState);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, gridState);

  }

  /* (non-Javadoc)
   * @see com.laex.cg2d.model.IScreenEditorState#toggleJointLayer(com.laex.cg2d.model.model.Joint)
   */
  @Override
  public void toggleJointLayer(Joint joint) {
  }

}