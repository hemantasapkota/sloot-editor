package com.laex.cg2d.core.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class BaseObjectActionDelegate implements IObjectActionDelegate {

  private List<IResource> selectedResources;

  @Override
  public void run(IAction action) {
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    IStructuredSelection strucSel = (IStructuredSelection) selection;
    selectedResources = (List<IResource>) strucSel.toList();
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {

  }

}
