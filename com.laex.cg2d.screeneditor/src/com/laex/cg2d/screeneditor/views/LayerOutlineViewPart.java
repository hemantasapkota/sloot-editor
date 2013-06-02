package com.laex.cg2d.screeneditor.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.laex.cg2d.screeneditor.ScreenEditor.LayerOutlineView;
import com.laex.cg2d.screeneditor.editparts.ScreenEditPart;

public class LayerOutlineViewPart extends ViewPart implements ISelectionListener {

  public static final String ID = "com.laex.cg2d.screeneditor.views.LayerOutlineViewPart"; //$NON-NLS-1$
  private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
  private Composite treeComposite;
  private LayerOutlineView lov;
  
  public LayerOutlineViewPart() {
  }

  /**
   * Create contents of the view part.
   * 
   * @param parent
   */
  @Override
  public void createPartControl(Composite parent) {
    Composite container = toolkit.createComposite(parent, SWT.NONE);
    toolkit.paintBordersFor(container);

    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    {
      treeComposite = new Composite(container, SWT.NONE);
      toolkit.adapt(treeComposite);
      toolkit.paintBordersFor(treeComposite);
      treeComposite.setLayout(new TreeColumnLayout());
      {
        //Dont remove this block of code. For layouting purpose
        // we need it to add the LayerViewOutline to this view
     }
    }

    createActions();
    initializeToolBar();
    initializeMenu();

    getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
  }

  public void dispose() {
    toolkit.dispose();
    super.dispose();
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
    IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
  }

  /**
   * Initialize the menu.
   */
  private void initializeMenu() {
    IMenuManager manager = getViewSite().getActionBars().getMenuManager();
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    IStructuredSelection ss = (IStructuredSelection) selection;
    Object firstEl = ss.getFirstElement();

    if (firstEl instanceof ScreenEditPart) {
      IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

      if (lov == null) {
        lov = (LayerOutlineView) editorPart.getAdapter(LayerOutlineViewPart.class);
        lov.createControl(treeComposite);
      }
    }
  }

}
