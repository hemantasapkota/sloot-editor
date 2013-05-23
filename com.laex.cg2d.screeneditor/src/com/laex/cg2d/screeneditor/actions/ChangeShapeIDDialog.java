package com.laex.cg2d.screeneditor.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.laex.cg2d.screeneditor.edit.ShapeEditPart;
import com.laex.cg2d.shared.model.Shape;
import org.eclipse.jface.viewers.ViewerSorter;

public class ChangeShapeIDDialog extends TitleAreaDialog {

  private static class Sorter extends ViewerSorter {

    public int compare(Viewer viewer, Object e1, Object e2) {
      String item1 = ((ShapeIdMgr) e1).newId;
      String item2 = ((ShapeIdMgr) e2).newId;

      return item1.compareTo(item2);
    }
  }

  class ShapeIdMgr {
    Shape shape;
    String newId;
  }

  private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
      return null;
    }

    public String getColumnText(Object element, int columnIndex) {
      ShapeIdMgr shp = (ShapeIdMgr) element;

      if (columnIndex == 0) {
        return shp.shape.getId();
      }

      return shp.newId;
    }

  }

  private static class ContentProvider implements IStructuredContentProvider {

    public Object[] getElements(Object inputElement) {
      return ((List<Shape>) inputElement).toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
  }

  private List<ShapeIdMgr> shapeIdList = new ArrayList<ShapeIdMgr>();
  private Table table;
  private TableViewer tableViewer;
  private Map<Shape, String> updateIdMap = new HashMap<Shape, String>();

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public ChangeShapeIDDialog(Shell parentShell, List selectedEditParts) {
    super(parentShell);

    for (Object o : selectedEditParts) {

      if (!(o instanceof ShapeEditPart)) {
        continue;
      }

      ShapeEditPart shpEp = (ShapeEditPart) o;
      Shape shp = (Shape) shpEp.getModel();

      ShapeIdMgr sim = new ShapeIdMgr();
      sim.shape = shp;
      sim.newId = "";

      shapeIdList.add(sim);
    }
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
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

    TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnOldId = tableViewerColumn_1.getColumn();
    tblclmnOldId.setWidth(100);
    tblclmnOldId.setText("Old ID");
    tblclmnOldId.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        tableViewer.setSorter(new Sorter());
      }
    });

    TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnNewId = tableViewerColumn_2.getColumn();
    tblclmnNewId.setWidth(100);
    tblclmnNewId.setText("New ID");
    tableViewerColumn_2.setEditingSupport(new EditingSupport(tableViewer) {

      @Override
      protected void setValue(Object element, Object value) {
        ((ShapeIdMgr) element).newId = value.toString();
        tableViewer.refresh();
      }

      @Override
      protected Object getValue(Object element) {
        return ((ShapeIdMgr) element).newId;
      }

      @Override
      protected CellEditor getCellEditor(Object element) {
        return new TextCellEditor(tableViewer.getTable());
      }

      @Override
      protected boolean canEdit(Object element) {
        return true;
      }
    });

    tableViewer.setLabelProvider(new TableLabelProvider());
    tableViewer.setContentProvider(new ContentProvider());
    tableViewer.setSorter(new Sorter());

    tableViewer.setInput(shapeIdList);
    tableViewer.refresh();

    return area;
  }

  @Override
  protected void okPressed() {
    updateIdMap.clear();

    for (ShapeIdMgr s : shapeIdList) {
      if (!StringUtils.isEmpty(s.newId)) {
          updateIdMap.put(s.shape, s.newId);
      }
    }

    super.okPressed();
  }
  
  public Map<Shape, String> getUpdateIdMap() {
    return updateIdMap;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

}
