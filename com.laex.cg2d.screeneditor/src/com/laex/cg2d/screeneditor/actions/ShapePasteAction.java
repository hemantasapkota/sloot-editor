package com.laex.cg2d.screeneditor.actions;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.laex.cg2d.model.ScreenModel.CGBounds;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;
import com.laex.cg2d.model.util.EntitiesUtil;
import com.laex.cg2d.screeneditor.commands.IDCreationStrategy;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;

public class ShapePasteAction extends Action {

  private EditPartViewer viewer;

  private ShapesDiagram diagram;

  public ShapePasteAction(EditPartViewer viewer) {
    this.viewer = viewer;
    this.diagram = (ShapesDiagram) viewer.getContents().getModel();

    setText("Paste");
    setId(ActionFactory.PASTE.getId());
    setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
  }

  @Override
  public boolean isEnabled() {
    return Clipboard.getDefault().getContents() != null;
  }

  @Override
  public void run() {
    try {
      this.redo();
    } catch (CoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void redo() throws CoreException, IOException {
    List<Shape> list = (List<Shape>) Clipboard.getDefault().getContents();

    CompoundCommand cc = new CompoundCommand();

    for (Shape s : list) {

      CGShape cgShape = CGScreenModelAdapter.asCGShape(s);
      CGShape.Builder shapeBuilder = CGShape.newBuilder(cgShape);
      CGBounds oldBounds = shapeBuilder.getBounds();

      // Set Bounds
      float newX = oldBounds.getX();
      float newY = oldBounds.getY() + 5;
      float newWidth = oldBounds.getWidth();
      float newHeight = oldBounds.getHeight();

      shapeBuilder.setBounds(CGBounds.newBuilder().setX(newX).setY(newY).setWidth(newWidth).setHeight(newHeight));

      CGShape newCGShape = shapeBuilder.build();

      Shape newShape = ScreenModelAdapter.asShape(newCGShape, s.getParentLayer());
      newShape.setId(IDCreationStrategy.create().newId(newShape.getEditorShapeType()));
      
      if (newShape.getEditorShapeType().isEntity()) {
        Entity ent = Entity.createFromFile(newShape.getEntityResourceFile().getResourceFile());
        newShape.setEntity(ent);
      }

      newShape.getParentLayer().add(newShape);
      ShapeCreateCommand scmd = new ShapeCreateCommand(newShape, diagram);
      cc.add(scmd);
    }

    viewer.getEditDomain().getCommandStack().execute(cc);
  }

}
