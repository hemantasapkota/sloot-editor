package com.laex.cg2d.screeneditor.actions;

import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.commands.ShapeChangeIDCommand;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;
import com.laex.cg2d.screeneditor.editparts.tree.ShapeTreeEP;

public class ChangeShapeIDAction extends Action {
  
  private EditPartViewer editPartViewer;

  public ChangeShapeIDAction(EditPartViewer viewer) {
    setText("Change Shape ID");
    this.editPartViewer = viewer;
  }
  
  @Override
  public boolean isEnabled() {
    boolean moreThanOneSelection = editPartViewer.getSelectedEditParts().size() != 0;
    Object firstEl = ((IStructuredSelection) editPartViewer.getSelection()).getFirstElement();
    return moreThanOneSelection && (firstEl instanceof ShapeEditPart || firstEl instanceof ShapeTreeEP);
  }
  
  @Override
  public void run() {
    ChangeShapeIDDialog dlg = new ChangeShapeIDDialog(editPartViewer.getControl().getShell(), editPartViewer.getSelectedEditParts());
    int resp = dlg.open(); 
    if (resp == ChangeShapeIDDialog.CANCEL) {
      return;
    }
    
    Map<Shape, String> updates = dlg.getUpdateIdMap();
    CompoundCommand cc = new CompoundCommand("Change Shape ID");
    for (Shape shape : updates.keySet()) {
      ShapeChangeIDCommand cmd = new ShapeChangeIDCommand(shape, updates.get(shape));
      cc.add(cmd);
    }
    editPartViewer.getEditDomain().getCommandStack().execute(cc);
  }
  
}