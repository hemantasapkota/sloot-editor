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
package com.laex.cg2d.screeneditor.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.laex.cg2d.model.ResourceManager;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.IDCreationStrategy;
import com.laex.cg2d.model.model.IDCreationStrategyFactory;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;
import com.laex.cg2d.screeneditor.editparts.tree.ShapeTreeEP;

/**
 * The Class EditShapeIDDialog.
 */
public class EditShapeIDDialog extends TitleAreaDialog {

  /**
   * The Class Comparator.
   */
  private static class Comparator extends ViewerComparator {

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
      String item1 = ((InternalShapeId) e1).newId;
      String item2 = ((InternalShapeId) e2).newId;

      return item2.compareTo(item1);
    }

  }

  /**
   * The Class InternalShapeId.
   */
  class InternalShapeId {
    
    /** The shape. */
    Shape shape;
    
    /** The new id. */
    String newId;
  }

  /**
   * The Class TableLabelProvider.
   */
  private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
      if (columnIndex != 0)
        return null;

      InternalShapeId shp = (InternalShapeId) element;

      switch (shp.shape.getEditorShapeType()) {
      case BACKGROUND_SHAPE:
        return ResourceManager.getImage(shp.shape.getBackgroundResourceFile().getResourceFile());

      case ENTITY_SHAPE:
        Rectangle r = shp.shape.getEntity().getDefaultFrame().getBounds();
        Image i = shp.shape.getEntity().getDefaultFrame();
//        return ScreenEditorUtil.getImageDescriptor(i, r.width / 2, (r.width / r.height) * (r.height / 2)).createImage();
        return ScreenEditorUtil.getImageDescriptor(i, 0.5f).createImage();

      case SIMPLE_SHAPE_BOX:
        return SharedImages.BOX.createImage();

      case SIMPLE_SHAPE_CIRCLE:
        return SharedImages.CIRCLE.createImage();

      case SIMPLE_SHAPE_HEDGE:
      case SIMPLE_SHAPE_VEDGE:
        return SharedImages.BOX.createImage();
      }

      return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
      InternalShapeId shp = (InternalShapeId) element;

      if (columnIndex == 0) {
        return shp.shape.getId();
      }

      return shp.newId;
    }

  }

  /**
   * The Class ContentProvider.
   */
  private static class ContentProvider implements IStructuredContentProvider {

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
      return ((List<Shape>) inputElement).toArray();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
  }

  /** The shape id list. */
  private List<InternalShapeId> shapeIdList = new ArrayList<InternalShapeId>();
  
  /** The table. */
  private Table table;
  
  /** The table viewer. */
  private TableViewer tableViewer;
  
  /** The update id map. */
  private Map<Shape, String> updateIdMap = new HashMap<Shape, String>();

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell
   * @param selectedEditParts the selected edit parts
   */
  public EditShapeIDDialog(Shell parentShell, List selectedEditParts) {
    super(parentShell);

    for (Object o : selectedEditParts) {

      boolean isShapeEP = (o instanceof ShapeEditPart) || (o instanceof ShapeTreeEP);

      if (!isShapeEP) {
        continue;
      }

      EditPart shpEp = (EditPart) o;
      Shape shp = (Shape) shpEp.getModel();

      InternalShapeId sim = new InternalShapeId();
      sim.shape = shp;
      sim.newId = "";

      shapeIdList.add(sim);
    }
  }

  /**
   * Create contents of the dialog.
   *
   * @param parent the parent
   * @return the control
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setTitle("Change Shape ID");
    setMessage("Update the IDs of shapes");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
    table = tableViewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    // Tab Editing of Cells.
    TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer,
        new FocusCellOwnerDrawHighlighter(tableViewer));
    ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
      protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
        return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
            || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
            || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
            || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
      }
    };

    TableViewerEditor.create(tableViewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
        | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
        | ColumnViewerEditor.KEYBOARD_ACTIVATION);

    TableViewerColumn colOldID = new TableViewerColumn(tableViewer, SWT.NONE);
    final TableColumn tblclmnOldId = colOldID.getColumn();
    tblclmnOldId.setWidth(100);
    tblclmnOldId.setText("Old ID");
    tblclmnOldId.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        tableViewer.getTable().setSortColumn(tblclmnOldId);
        tableViewer.refresh();
      }
    });

    TableViewerColumn colNewID = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnNewId = colNewID.getColumn();
    tblclmnNewId.setWidth(100);
    tblclmnNewId.setText("New ID");
    colNewID.setEditingSupport(new EditingSupport(tableViewer) {

      IDCreationStrategy ics = IDCreationStrategyFactory.getIDCreator(ScreenEditorUtil.getScreenModel());

      private boolean validate(String idToCheck) {
        
        //Make a copy of list to check, otherwise we will perpetually be validating false
        for (InternalShapeId isi: shapeIdList) {
          
          boolean idUsed = ics.isIdUsed(isi.shape.getEditorShapeType(), isi.newId);
          
          if (idUsed) {
            setErrorMessage("ID already exists");
            getButton(OK).setEnabled(false);
            return false;
          }
          
        }

        setErrorMessage(null);
        getButton(OK).setEnabled(true);
        return true;
      }

      @Override
      protected void setValue(Object element, Object value) {
        ((InternalShapeId) element).newId = value.toString();
        tableViewer.refresh();
        
        validate(value.toString());
      }

      @Override
      protected Object getValue(Object element) {
        return ((InternalShapeId) element).newId;
      }

      @Override
      protected CellEditor getCellEditor(Object element) {
        TextCellEditor tce = new TextCellEditor(tableViewer.getTable());
        return tce;
      }

      @Override
      protected boolean canEdit(Object element) {
        return true;
      }
    });

    tableViewer.setLabelProvider(new TableLabelProvider());
    tableViewer.setContentProvider(new ContentProvider());
//    tableViewer.setComparator(new Comparator());

    tableViewer.setInput(shapeIdList);
    tableViewer.refresh();

    return area;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    updateIdMap.clear();

    for (InternalShapeId s : shapeIdList) {
      if (!StringUtils.isEmpty(s.newId)) {
        updateIdMap.put(s.shape, s.newId);
      }
    }

    super.okPressed();
  }

  /**
   * Gets the update id map.
   *
   * @return the update id map
   */
  public Map<Shape, String> getUpdateIdMap() {
    return updateIdMap;
  }

  /**
   * Create contents of the button bar.
   *
   * @param parent the parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

    getButton(OK).setEnabled(false);
  }

  /**
   * Return the initial size of the dialog.
   *
   * @return the initial size
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

}
