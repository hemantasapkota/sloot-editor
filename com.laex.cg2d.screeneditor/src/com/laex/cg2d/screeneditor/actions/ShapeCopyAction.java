package com.laex.cg2d.screeneditor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;

public class ShapeCopyAction extends Action {
  
  private EditPartViewer viewer;

  public ShapeCopyAction(EditPartViewer viewer) {
    this.viewer = viewer;
    setText("Copy");
    setId(ActionFactory.COPY.getId());
    setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
  }
  
  @Override
  public boolean isEnabled() {
    return viewer.getSelectedEditParts().size() != 0;
  }
  

  @Override
  public void run() {
    List<Shape> toCopy = new ArrayList<Shape>();
    for (Object o : viewer.getSelectedEditParts()) {
      if (o instanceof ShapeEditPart) {
        ShapeEditPart sep = (ShapeEditPart) o;
        Shape model = (Shape) sep.getModel();
        toCopy.add(model);
      }
    }
    
    Clipboard.getDefault().setContents(toCopy);
  }

}
