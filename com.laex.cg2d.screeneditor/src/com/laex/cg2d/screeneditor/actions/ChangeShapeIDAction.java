package com.laex.cg2d.screeneditor.actions;

import java.util.Map;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;

import com.laex.cg2d.screeneditor.commands.ShapeChangeIDCommand;
import com.laex.cg2d.shared.model.Shape;

public class ChangeShapeIDAction extends Action {
  
  private EditPartViewer editPartViewer;

  public ChangeShapeIDAction(EditPartViewer viewer) {
    setText("Change Shape ID");
    this.editPartViewer = viewer;
  }
  
  @Override
  public boolean isEnabled() {
    return editPartViewer.getSelectedEditParts().size() != 0;
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