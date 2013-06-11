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
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;
import com.laex.cg2d.screeneditor.editparts.tree.ShapeTreeEP;

public class EditShapeIDDialog extends TitleAreaDialog {

  private static class Comparator extends ViewerComparator {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
      String item1 = ((ShapeIdMgr) e1).newId;
      String item2 = ((ShapeIdMgr) e2).newId;

      return item2.compareTo(item1);
    }

  }

  class ShapeIdMgr {
    Shape shape;
    String newId;
  }

  private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
      if (columnIndex != 0)
        return null;

      ShapeIdMgr shp = (ShapeIdMgr) element;

      switch (shp.shape.getEditorShapeType()) {
      case BACKGROUND_SHAPE:
        return ResourceManager.getImage(shp.shape.getBackgroundResourceFile().getResourceFile());

      case ENTITY_SHAPE:
        Rectangle r = shp.shape.getEntity().getDefaultFrame().getBounds();
        Image i = shp.shape.getEntity().getDefaultFrame();
        return ScreenEditorUtil.getImageDescriptor(i, r.width / 2, (r.width / r.height) * (r.height / 2)).createImage();

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
  public EditShapeIDDialog(Shell parentShell, List selectedEditParts) {
    super(parentShell);

    for (Object o : selectedEditParts) {

      boolean isShapeEP = (o instanceof ShapeEditPart) || (o instanceof ShapeTreeEP);

      if (!isShapeEP) {
        continue;
      }

      EditPart shpEp = (EditPart) o;
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
    tableViewer.setComparator(new Comparator());

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
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
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
