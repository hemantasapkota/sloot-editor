package com.laex.cg2d.screeneditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class RedoHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    
    
    if (!gv.getEditDomain().getCommandStack().canRedo()) {
      return null;
    }
    
    
    RedoAction redo = new RedoAction(HandlerUtil.getActiveEditor(event));
    redo.run();
    return null;
  }
  
}