package com.laex.cg2d.screeneditor.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.CGScreenModelAdapter;
import com.laex.cg2d.model.adapter.ScreenModelAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;

public class ShapeCopyHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    
    try {
      doCopy(gv);
    } catch (CoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void doCopy(GraphicalViewer gv) throws CoreException, IOException {
    List<Shape> toCopy = new ArrayList<Shape>();

    for (Object o : gv.getSelectedEditParts()) {
      if (o instanceof ShapeEditPart) {
        ShapeEditPart sep = (ShapeEditPart) o;
        Shape model = (Shape) sep.getModel();

        //How to copy a shape:
        //1. Convert shape model to CGShape
        //2. Create a new CGShape builder and modify properties
        //3. Convert back the CGShape to Shape
        //4. Create entities if the model is of type entity.
        CGShape cgModelPrototype = CGScreenModelAdapter.asCGShape(model);
        CGShape cgModelToCopy = CGShape.newBuilder(cgModelPrototype).build();

        Shape finalModelToCopy = ScreenModelAdapter.asShape(cgModelToCopy, model.getParentLayer());

        if (finalModelToCopy.getEditorShapeType().isEntity()) {
          Entity ent = Entity.createFromFile(finalModelToCopy.getEntityResourceFile().getResourceFile());
          finalModelToCopy.setEntity(ent);
        }

        toCopy.add(finalModelToCopy);
      }
    }

    Clipboard.getDefault().setContents(toCopy);
  }

}