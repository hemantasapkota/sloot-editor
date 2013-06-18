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
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.osgi.internal.resolver.ComputeNodeOrder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.laex.cg2d.model.ILayerManager;
import com.laex.cg2d.model.IScreenEditorState;
import com.laex.cg2d.model.IScreenPropertyManager;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.adapter.ShapeAdapter;
import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ModelValidatorFactory;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.util.EntitiesUtil;
import com.laex.cg2d.screeneditor.EntityResourceChangeListener.EntityChangeListener;
import com.laex.cg2d.screeneditor.commands.LayerAddCommand;
import com.laex.cg2d.screeneditor.commands.LayerChangeOrderCommand;
import com.laex.cg2d.screeneditor.commands.LayerChangePropertiesCommand;
import com.laex.cg2d.screeneditor.commands.LayerRemoveCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand.DeleteCommandType;
import com.laex.cg2d.screeneditor.editparts.CardEditPart;
import com.laex.cg2d.screeneditor.editparts.ScreenEditPartFactory;
import com.laex.cg2d.screeneditor.editparts.tree.LayerOutlineTreeEPFactory;
import com.laex.cg2d.screeneditor.editparts.tree.ScreenTreeEPFactory;
import com.laex.cg2d.screeneditor.palette.ScreenEditorPaletteFactory;
import com.laex.cg2d.screeneditor.palette.ShapeCreationFactory;
import com.laex.cg2d.screeneditor.palette.ShapeCreationInfo;
import com.laex.cg2d.screeneditor.prefs.PreferenceInitializer;
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
  
  private boolean gridState = false;

  /** The card height. */
  int x, y, cardWidthh, cardHeight;

  /**
   * ScreenOutineView. General outline view that shows all the editparts in the
   * screen viewer.
   * 
   * @author hemantasapkota
   * 
   */
  class ScreenOutlineView extends ContentOutlinePage {

    public ScreenOutlineView(EditPartViewer viewer) {
      super(viewer);
    }

    @Override
    public void createControl(Composite parent) {
      getViewer().createControl(parent);
      getViewer().setEditDomain(getEditDomain());
      getViewer().setEditPartFactory(new ScreenTreeEPFactory());
      getSelectionSynchronizer().addViewer(getViewer());
      getViewer().setContents(getModel());
      //Set context menu
      ScreenEditorContextMenuProvider scm = new ScreenEditorContextMenuProvider(getViewer(), getActionRegistry());
      getViewer().setContextMenu(scm);
      getSite().registerContextMenu("com.laex.cg2d.screeneditor.outline.contextmenu", scm,
          getSite().getSelectionProvider());
    }

    @Override
    public void dispose() {
      getSelectionSynchronizer().removeViewer(getViewer());
    }

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

    public LayerOutlineView(EditPartViewer viewer) {
      super(viewer);
    }

    @Override
    public void createControl(Composite parent) {
      getViewer().createControl(parent);
      getViewer().setEditDomain(getEditDomain());
      getViewer().setEditPartFactory(new LayerOutlineTreeEPFactory());
      getSelectionSynchronizer().addViewer(getViewer());
      getViewer().setContents(getModel());
    }

    @Override
    public void dispose() {
      getSelectionSynchronizer().removeViewer(getViewer());
    }

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

        updateCardLayer(x, y, cardWidthh, cardHeight);

        super.createLayers(layeredPane);
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
      ce.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Perform save.
   * 
   * @param file
   *          the file
   * @param monitor
   *          the monitor
   * @param screenPrefs
   *          the screen prefs
   * @throws CoreException
   *           the core exception
   * @throws IOException
   */
  private void performSave(IFile file, IProgressMonitor monitor) throws CoreException, IOException {
    // Use existing preferences
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
    if (PALETTE_MODEL == null)
      PALETTE_MODEL = ScreenEditorPaletteFactory.createPalette();
    return PALETTE_MODEL;
  }

  /**
   * Handle load exception.
   * 
   * @param e
   *          the e
   */
  private void handleLoadException(Exception e) {
    model = new GameModel(PreferenceInitializer.defaultScreenPrefs());
    Layer firstLayer = new Layer(0, "Layer1", true, false);
    model.getDiagram().getLayers().add(firstLayer);
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
    viewer.setContents(getModel().getDiagram()); // set the contents of this
                                                 // editor
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
          IFile file = DNDFileTransfer.file;
          creationInfo = new ShapeCreationInfo.Builder().setBackgroundResourceFile(file)
              .setEditorShapeType(EditorShapeType.BACKGROUND_SHAPE).build();
          break;
        case ENTITY:
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

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
   */
  protected void setInput(final IEditorInput input) {
    super.setInput(input);

    ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener,
        IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);
    // if there are any changes in the entities, load em up in the editor
    resourceListener.addEntityChangeListener(new EntityChangeListener() {
      @Override
      public void entityChanged(final IResource resource) {
        // reload the newly created entities. Refresh all entities
        /*
         * It is important to execute pallete creation via asyncExec because it
         * accesses GEF UI thread
         */
        getSite().getShell().getDisplay().asyncExec(new Runnable() {
          @Override
          public void run() {
            try {
              ScreenEditorPaletteFactory.createEntitesPaletteItems(input);
              // Check if the resource is valid entity or not. If not remove the
              // entity and log it

              Entity e = Entity.createFromFile((IFile) resource);
              boolean isValid = ModelValidatorFactory.getValidator(Entity.class, e).isValid();
              if (!isValid) {
                removeDeletedOrInvalidEntities(resource);
              }
            } catch (IOException e) {
              e.printStackTrace();
            } catch (CoreException e) {
              e.printStackTrace();
            }
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
            ScreenEditorPaletteFactory.removeEntity(resource);
            removeDeletedOrInvalidEntities(resource);
          }
        });
      }

      @Override
      public void entityCopied(IResource entityResource, IPath copiedPathFrom) {
        getSite().getShell().getDisplay().asyncExec(new Runnable() {

          @Override
          public void run() {
            MessageBox mb = new MessageBox(getSite().getShell());
            mb.setMessage("You are trying to copy an entity from one project to another. Do you want to copy the associated textures as well ?");
            mb.setText("Entity Copy Confirmation");
            mb.open();
          }
        });

      }
    });

    try {
      IFile file = ((IFileEditorInput) input).getFile();

      CGScreenModel cgGameModel = CGScreenModel.parseFrom(file.getContents());

      model = ScreenModelAdapter.asGameModel(cgGameModel);
      x = cgGameModel.getScreenPrefs().getCardPrefs().getCardNoX();
      y = cgGameModel.getScreenPrefs().getCardPrefs().getCardNoY();
      cardWidthh = cgGameModel.getScreenPrefs().getCardPrefs().getCardWidth();
      cardHeight = cgGameModel.getScreenPrefs().getCardPrefs().getCardHeight();

      // go ahead and init entities. if some entities are invalid or have been
      // deleted, remove them from this model as well.
      if (EntitiesUtil.visitModelAndInitEntities(model)) {
        getSite().getShell().getDisplay().syncExec(new Runnable() {
          @Override
          public void run() {
            MessageBox mb = new MessageBox(getSite().getShell(), SWT.OK);
            mb.setMessage("Some of the entities refereneced in this file are no longer valid. They will be removed.");
            mb.setText("Screen file inconsistent");
            mb.open();

            // remove inconsistent entities
            CompoundCommand cc = new CompoundCommand();
            for (int i = 0; i < model.getDiagram().getChildren().size(); i++) {
              Shape shape = model.getDiagram().getChildren().get(i);
              if (shape.getEditorShapeType().isEntity()) {
                try {
                  Entity e = Entity.createFromFile(shape.getEntityResourceFile().getResourceFile());
                  boolean isValid = ModelValidatorFactory.getValidator(Entity.class, e).isValid();

                  if (!isValid) {
                    ShapeDeleteCommand sdc = new ShapeDeleteCommand(model.getDiagram(), shape,
                        DeleteCommandType.NON_UNDOABLE);
                    cc.add(sdc);
                  }
                } catch (CoreException e) {
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
            if (!cc.isEmpty())
              getCommandStack().execute(cc);

          }
        });

      }

      setPartName(file.getProject().getName() + "/" + file.getName());
    } catch (CoreException e) {
      handleLoadException(e);
    } catch (IOException e) {
      handleLoadException(e);
    }

    // Load entities in the palette
    try {
      ScreenEditorPaletteFactory.createEntitesPaletteItems(input);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }
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
  
  @Override
  public void removeAll() {
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
  public void updateCardLayer(int noX, int noY, int cardWidth, int cardHeight) {
    cardLayer.removeAll();

    for (int i = 0; i < noY; i++) {
      for (int j = 0; j < noX; j++) {
        CardEditPart cep = new CardEditPart(j, i, cardWidth, cardHeight);
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
    String entityName = EntitiesUtil.getInternalName(resource.getName());
    // Remove the deleted entites from the screen
    // plain for loop to prevent concurrent list modification access
    CompoundCommand cc = new CompoundCommand();
    for (Shape s : model.getDiagram().getChildren()) {
      if (s.getEntity() != null) {
        if (s.getEntity().getInternalName().equals(entityName)) {
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
      e.printStackTrace();
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

  @Override
  public void toggleGrid() {
    gridState = !gridState;
    
    getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, gridState);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, gridState);

  }


}