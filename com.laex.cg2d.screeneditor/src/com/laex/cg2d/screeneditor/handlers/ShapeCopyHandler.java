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

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.ModelCopier;
import com.laex.cg2d.model.model.ModelCopierFactory;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;

public class ShapeCopyHandler extends AbstractHandler {
  
  ModelCopier copier;

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    GameModel gameModel = ScreenEditorUtil.getScreenModel();
    
    copier = ModelCopierFactory.getModelCopier(Shape.class, gameModel);
    
    try {
      doCopy(gv, gameModel);
    } catch (CoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void doCopy(GraphicalViewer gv, GameModel model) throws CoreException, IOException {
    List<Shape> toCopy = new ArrayList<Shape>();
    
    for (Object o : gv.getSelectedEditParts()) {
      
      if (o instanceof ShapeEditPart) {
    
        Shape modelToCopy = (Shape) ((ShapeEditPart) o).getModel();
        
        toCopy.add((Shape) copier.copy(modelToCopy));
      }
    }

    Clipboard.getDefault().setContents(toCopy);
  }

}