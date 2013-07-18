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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.laex.cg2d.model.ILayerManager;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.editparts.tree.LayerTreeEP;
import com.laex.cg2d.screeneditor.model.ShapesDiagramAdapter;

/***
 * LayersViewPart.
 * 
 * @author hemantasapkota
 */
public class LayersViewPart extends ViewPart implements IAdaptable, ISelectionListener, IScreenDisposeListener {

  /** The Constant ID. */
  public static final String ID = "com.laex.cg2d.screeneditor.views.LayersViewPart"; //$NON-NLS-1$

  /** The Constant VISIBLE_PROP. */
  public static final String VISIBLE_PROP = "Visible";

  /** The Constant LOCKED_PROP. */
  public static final String LOCKED_PROP = "Locked";

  /** The Constant NAME_PROP. */
  public static final String NAME_PROP = "Name";

  /** The descriptors. */
  public static String[] descriptors;
  static {
    descriptors = new String[]
      { VISIBLE_PROP, LOCKED_PROP, NAME_PROP };
  }

  /**
   * The Class TableLabelProvider.
   */
  private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
     * .Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
      return ((LayerItem) element).getImage(columnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
     * .Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
      return ((LayerItem) element).getText(columnIndex);

    }
  }

  /**
   * The Class ContentProvider.
   */
  private static class ContentProvider implements IStructuredContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
     * .lang.Object)
     */
    public Object[] getElements(Object inputElement) {
      List<LayerItem> lis = (List<LayerItem>) inputElement;
      return lis.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
  }

  // Make LayerItem IAdaptable for handling ISelection.
  /**
   * The Class LayerItem.
   */
  class LayerItem implements IAdaptable {

    /** The id. */
    int id;

    /** The name. */
    String name;

    /** The visible. */
    boolean visible;

    /** The locked. */
    boolean locked;

    /** The reference layer. */
    Layer referenceLayer;

    /**
     * Gets the text.
     * 
     * @param index
     *          the index
     * @return the text
     */
    String getText(int index) {
      switch (index) {
      case 2:
        return name;
      }
      return StringUtils.EMPTY;
    }

    /**
     * Gets the image.
     * 
     * @param index
     *          the index
     * @return the image
     */
    Image getImage(int index) {
      switch (index) {
      case 0:
        return getEye(visible);
      case 1:
        return getLocked(locked);
      }
      return null;
    }

    /**
     * Gets the eye.
     * 
     * @param status
     *          the status
     * @return the eye
     */
    Image getEye(boolean status) {
      if (status) {
        return SharedImages.EYE_VISIBLE.createImage();
      }
      return SharedImages.EYE_INVISIBLE.createImage();
    }

    /**
     * Gets the locked.
     * 
     * @param status
     *          the status
     * @return the locked
     */
    Image getLocked(boolean status) {
      if (status) {
        return SharedImages.LOCKED.createImage();
      }
      return SharedImages.UNLOCKED.createImage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
      return null;
    }

    /**
     * To layer.
     * 
     * @return the layer
     */
    public Layer toLayer() {
      Layer l = new Layer(id, name, visible, locked);
      l.setChildren(referenceLayer.getChildren());
      return l;
    }
  }

  // private Queue<LayerItem> layerItems = new LinkedList<LayerItem>();
  /** The layer items. */
  private List<LayerItem> layerItems = new ArrayList<LayerItem>();

  /** The table. */
  private Table table;

  /** The table viewer. */
  private TableViewer tableViewer;

  /** The add action. */
  private Action addAction;

  /** The remove action. */
  private Action removeAction;

  /** The remove all action. */
  private Action removeAllAction;

  /** The lock action. */
  private Action lockAction;

  /** The visibility action. */
  private Action visibilityAction;

  /** The up action. */
  private Action upAction;

  /** The down action. */
  private Action downAction;

  /**
   * Instantiates a new layers view part.
   */
  public LayersViewPart() {
  }

  /**
   * Disable state.
   */
  private void disableState() {
    layerItems.clear();
    if (!tableViewer.getTable().isDisposed())
      tableViewer.refresh();

    addAction.setEnabled(false);
    lockAction.setEnabled(false);
    visibilityAction.setEnabled(false);
    removeAction.setEnabled(false);
    removeAllAction.setEnabled(false);
    upAction.setEnabled(false);
    downAction.setEnabled(false);
  }

  /**
   * Enable state.
   */
  private void enableState() {
    addAction.setEnabled(true);
    lockAction.setEnabled(true);
    visibilityAction.setEnabled(true);
    removeAction.setEnabled(true);
    removeAllAction.setEnabled(true);
    upAction.setEnabled(true);
    downAction.setEnabled(true);
  }

  /**
   * Create contents of the view part.
   * 
   * @param parent
   *          the parent
   */
  @Override
  public void createPartControl(Composite parent) {
    parent.setLayout(new FillLayout(SWT.HORIZONTAL));
    // NOTE: It is important to set SWT.SINGLE for this table viewer. With
    // SWT.MULTI enabled, the current
    // selection gets reset. This causes inconvinience for the user.
    tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
    table = tableViewer.getTable();
    table.setHeaderVisible(true);

    //
    tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        List<LayerItem> selected = selectedLayers();
        if (selected.isEmpty()) {
          return;
        }

        //
        for (Layer l : ScreenEditorUtil.getScreenModel().getDiagram().getLayers()) {
          if (l.isCurrent()) {
            l.setCurrent(false);
          }
        }

        // select default
        if (selected.size() == 1) {
          selected.get(0).referenceLayer.setCurrent(true);
        }

      }
    });

    //
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent e) {
        changeLayerName();
      }
    });
    addCustomSelectionRenderer(table);
    {
      TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn tblclmnVisible = tableViewerColumn.getColumn();
      tblclmnVisible.setMoveable(true);
      tblclmnVisible.setWidth(49);
      tblclmnVisible.setText("Visible");
    }
    {
      TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn tblclmnLock = tableViewerColumn.getColumn();
      tblclmnLock.setMoveable(true);
      tblclmnLock.setWidth(58);
      tblclmnLock.setText("Locked");
    }
    {
      TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn tblclmnName = tableViewerColumn.getColumn();
      tblclmnName.setMoveable(true);
      tblclmnName.setWidth(117);
      tblclmnName.setText("Name");
    }
    tableViewer.setLabelProvider(new TableLabelProvider());
    tableViewer.setContentProvider(new ContentProvider());
    tableViewer.setColumnProperties(descriptors);
    tableViewer.setInput(layerItems);

    getSite().setSelectionProvider(tableViewer);

    createActions();
    initializeToolBar();
    initializeMenu();

    getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
  }

  /**
   * Change layer name.
   */
  protected void changeLayerName() {
    List<LayerItem> selected = selectedLayers();
    if (selected.isEmpty()) {
      return;
    }

    LayerItem li = selected.get(0);
    SimpleTextChangeDialog stcd = new SimpleTextChangeDialog(getSite().getShell(), li.name, "Layer ID: " + li.id);
    int response = stcd.open();
    if (response == SimpleTextChangeDialog.CANCEL) {
      return;
    }

    li.name = stcd.getName();

    ScreenEditorUtil.getScreenLayerManager().changeLayerProperties(Layer.create(li.id, li.name, li.visible, li.locked));

    tableViewer.refresh();
  }

  /**
   * Needed for having better visibility of the icons when selected.
   * 
   * @param table2
   *          the table2
   */
  private void addCustomSelectionRenderer(Table table2) {
    table2.addListener(SWT.EraseItem, new Listener() {
      @Override
      public void handleEvent(Event event) {
        event.detail &= ~SWT.HOT;
        if ((event.detail & SWT.SELECTED) == 0)
          return; // / item not selected

        Table table = (Table) event.widget;
        TableItem item = (TableItem) event.item;
        int clientWidth = table.getClientArea().width;

        GC gc = event.gc;
        Color oldForeground = gc.getForeground();
        Color oldBackground = gc.getBackground();

        gc.setBackground(ColorConstants.lightGray);
        gc.setForeground(ColorConstants.white);
        gc.fillRectangle(0, event.y, clientWidth, event.height);

        gc.setForeground(oldForeground);
        gc.setBackground(oldBackground);
        event.detail &= ~SWT.SELECTED;
      }
    });
  }

  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
    {
      addAction = new Action("") {
        @Override
        public void run() {
          LayerItem li = new LayerItem();

          li.id = ScreenEditorUtil.getScreenLayerManager().getNewLayerId();
          li.name = "Layer" + li.id;
          li.locked = false;
          li.visible = true;
          int index = addLayer(li);
          Layer layer = new Layer(li.id, li.name, li.visible, li.locked);
          ScreenEditorUtil.getScreenLayerManager().addLayer(layer);
          li.referenceLayer = layer;

          getTableViewer().refresh();
          getTableViewer().getTable().select(index);
        }

        private int addLayer(LayerItem li) {
          if (layerItems.add(li)) {
            return layerItems.indexOf(li);
          }
          return -1;
        }

      };
      addAction.setDescription("Add a new Layer");
      addAction.setImageDescriptor(SharedImages.ADD_ITEM_SMALL);
    }

    {
      removeAction = new Action("") {
        @Override
        public void run() {
          List<LayerItem> selected = selectedLayers();
          if (selected.isEmpty()) {
            return;
          }

          MessageBox mb = new MessageBox(getSite().getShell(), SWT.YES | SWT.NO);
          mb.setMessage("Are you sure you want to remove the selected layers ?");
          int resp = mb.open();
          if (resp == SWT.NO) {
            return;
          }

          ILayerManager mgr = ScreenEditorUtil.getScreenLayerManager();
          for (LayerItem li : selected) {
            mgr.removeLayer(li.referenceLayer);
            layerItems.remove(li);
          }

          tableViewer.refresh();

          table.select(layerItems.size() - 1);

        }
      };

      removeAction.setDescription("Remove a selected layer");
      removeAction.setImageDescriptor(SharedImages.REMOVE_ITEM_SMALL);
    }

    {
      removeAllAction = new Action("") {
        @Override
        public void run() {
          MessageBox mb = new MessageBox(getSite().getShell(), SWT.YES | SWT.NO);
          mb.setMessage("Are you sure you want to remove all the layers ?");
          int resp = mb.open();
          if (resp == SWT.NO) {
            return;
          }

          ILayerManager mgr = ScreenEditorUtil.getScreenLayerManager();
          mgr.removeAllLayers();
          
          layerItems.clear();
          

          tableViewer.refresh();

          int size = layerItems.size();
          table.select(size >= 0 ? size - 1 : 0);

        }
      };

      removeAllAction.setDescription("Remove all layers");
      removeAllAction.setImageDescriptor(SharedImages.REMOVE_ALL_SMALL);
    }

    {
      lockAction = new Action("") {
        @Override
        public void run() {
          List<LayerItem> selected = selectedLayers();
          ILayerManager layerMgr = ScreenEditorUtil.getScreenLayerManager();
          for (LayerItem li : selected) {
            li.locked = !li.locked;
            layerMgr.changeLayerProperties(Layer.create(li.id, li.name, li.visible, li.locked));
          }

          tableViewer.refresh();
        }
      };
      lockAction.setImageDescriptor(SharedImages.LOCKED);
    }

    {
      visibilityAction = new Action("") {
        @Override
        public void run() {
          List<LayerItem> selected = selectedLayers();
          ILayerManager mgr = ScreenEditorUtil.getScreenLayerManager();
          for (LayerItem li : selected) {
            li.visible = !li.visible;
            mgr.changeLayerProperties(Layer.create(li.id, li.name, li.visible, li.locked));
          }

          tableViewer.refresh();
        }
      };
      visibilityAction.setImageDescriptor(SharedImages.EYE_VISIBLE);
    }

    {
      upAction = new Action("") {
        @Override
        public void run() {
          List<LayerItem> selLayers = selectedLayers();
          if (selLayers.isEmpty())
            return;

          LayerItem li = selLayers.get(0);
          int index = layerItems.indexOf(li);

          if (index == 0)
            return;

          // case:
          layerItems.remove(index);
          layerItems.add(index - 1, li);

          tableViewer.refresh();

          // update the changes
          changeLayerOrder();
        }
      };
      upAction.setImageDescriptor(SharedImages.BUTTON_UP);
    }

    {
      downAction = new Action("") {
        @Override
        public void run() {
          List<LayerItem> selLayers = selectedLayers();
          if (selLayers.isEmpty())
            return;

          LayerItem li = selLayers.get(0);
          int index = layerItems.indexOf(li);

          if (index == layerItems.size() - 1)
            return;

          layerItems.remove(index);
          layerItems.add(index + 1, li);

          tableViewer.refresh();

          // update the changes
          changeLayerOrder();
        }
      };
      downAction.setImageDescriptor(SharedImages.BUTTON_DOWN);
    }
  }

  /**
   * Change layer order.
   */
  private void changeLayerOrder() {
    Layer[] larr = new Layer[layerItems.size()];
    for (int i = 0; i < layerItems.size(); i++) {
      larr[i] = layerItems.get(i).toLayer();
    }

    //
    if (ScreenEditorUtil.isScreenEditorActive()) {
      ScreenEditorUtil.getScreenLayerManager().changeLayerOrder(larr);
    }
  }

  /**
   * Initialize the toolbar.
   */
  private void initializeToolBar() {
    IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
    toolbarManager.add(new Separator());
    toolbarManager.add(visibilityAction);
    toolbarManager.add(lockAction);
    toolbarManager.add(addAction);
    toolbarManager.add(removeAction);
    toolbarManager.add(removeAllAction);
    toolbarManager.add(upAction);
    toolbarManager.add(downAction);
    toolbarManager.update(true);
  }

  /**
   * Initialize the menu.
   */
  private void initializeMenu() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
   */
  @Override
  public Object getAdapter(Class adapter) {
    if (adapter == Layer.class) {
      ISelection isel = tableViewer.getSelection();
      if (isel.isEmpty() || isel instanceof ITextSelection) {
        return null;
      }

      IStructuredSelection structSel = (IStructuredSelection) isel;
      LayerItem li = (LayerItem) structSel.getFirstElement();
      return li.referenceLayer;
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
   */
  @Override
  public void setFocus() {
  }

  /**
   * Gets the table viewer.
   * 
   * @return the table viewer
   */
  private TableViewer getTableViewer() {
    return tableViewer;
  }

  /**
   * Selected layers.
   * 
   * @return the list
   */
  private List<LayerItem> selectedLayers() {
    IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
    if (selection.isEmpty()) {
      return new ArrayList<LayerItem>();
    }

    List<LayerItem> selected = selection.toList();
    return selected;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
   * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
   */
  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    // Shape editor gets deactivated when switching between editors
    if (!ScreenEditorUtil.isScreenEditorActive()) {
      disableState();
      return;
    }

    // Do nothing on empty selection or an incompatible tree selection.
    boolean notStructuredSelection = !(selection instanceof IStructuredSelection);
    if (selection.isEmpty() || notStructuredSelection) {
      enableState();
      return;
    }

    IStructuredSelection sel = (IStructuredSelection) selection;
    if (!(sel.getFirstElement() instanceof IAdaptable)) {
      enableState();
      return;
    }

    Object firstElement = sel.getFirstElement();
    if (firstElement instanceof LayerItem) {
      enableState();
      return; // do nothing
    }

    if (firstElement instanceof LayerTreeEP) {
      enableState();
      Layer l = (Layer) ((LayerTreeEP) firstElement).getModel();
      showSelectionForLayerEditPart(l);
    }

    enableState();
    /*
     * If the selection is ShapesDiagramEditPart, we get the model and populate
     * the layers. If the selection is not ShapesDiagramEditPart and is
     * ShapesEditPart, we get the diagram model, populate the layers, and show
     * selection for the selected shapes edit part.
     */
    IAdaptable element = (IAdaptable) firstElement;
    ShapesDiagram model = (ShapesDiagram) element.getAdapter(ShapesDiagram.class);
    if (model == null) {
      Shape shapeModel = (Shape) element.getAdapter(Shape.class);
      // shapeModel null means this selection is definitely not
      // ShapeEditPart but some other part in the system
      if (shapeModel == null) {
        return;
      }
      // if layers have not been populated yet, populate them and show
      // selection for edit part
      if (layerItems.isEmpty()) {
        model = (ShapesDiagram) element.getAdapter(ShapesDiagramAdapter.class);
        showSelectionForMainModel(model);
      }

      showSelectionForShapeEditPart(shapeModel);
      return;
    }

    showSelectionForMainModel(model);
  }

  /**
   * Show selection for shape edit part.
   * 
   * @param shapeModel
   *          the shape model
   */
  private void showSelectionForShapeEditPart(Shape shapeModel) {
    Layer parentLayer = shapeModel.getParentLayer();
    int index = 0;
    for (LayerItem li : layerItems) {
      if (li.referenceLayer == parentLayer) {
        table.setSelection(index);
        li.referenceLayer.setCurrent(true);
      }
      index++;
    }
  }

  /**
   * Show selection for layer edit part.
   * 
   * @param layerModel
   *          the layer model
   */
  private void showSelectionForLayerEditPart(Layer layerModel) {
    int index = 0;
    for (LayerItem li : layerItems) {
      if (li.referenceLayer == layerModel) {
        table.setSelection(index);
        li.referenceLayer.setCurrent(true);
      }
      index++;
    }
  }

  /**
   * Show selection for main model.
   * 
   * @param model
   *          the model
   */
  private void showSelectionForMainModel(ShapesDiagram model) {
    TableItem[] selections = table.getSelection();
    layerItems.clear();
    for (Layer l : model.getLayers()) {
      LayerItem li = new LayerItem();
      li.id = l.getId();
      li.name = l.getName();
      li.locked = l.isLocked();
      li.visible = l.isVisible();
      li.referenceLayer = l;
      layerItems.add(li);
    }
    getTableViewer().refresh();
    table.setSelection(selections);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.screeneditor.views.IScreenDisposeListener#screenDisposed()
   */
  @Override
  public void screenDisposed() {
    disableState();
  }

}
