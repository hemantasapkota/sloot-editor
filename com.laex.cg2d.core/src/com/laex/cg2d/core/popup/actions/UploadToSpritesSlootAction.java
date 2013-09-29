package com.laex.cg2d.core.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class UploadToSpritesSlootAction implements IObjectActionDelegate {

  private Shell shell;

  private List<IResource> selectedResources;

  /**
   * Constructor for Action1.
   */
  public UploadToSpritesSlootAction() {
    super();
  }

  /**
   * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    shell = targetPart.getSite().getShell();
  }

  /**
   * @see IActionDelegate#run(IAction)
   */
  public void run(IAction action) {
    if (selectedResources.size() <= 0)
      return;

	  UploadToSpritesSlootDialog uploadDialog = new UploadToSpritesSlootDialog(shell, selectedResources.get(0));
	  uploadDialog.open();
	}

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    selectedResources = (List<IResource>) strucSel.toList();
  }

}
