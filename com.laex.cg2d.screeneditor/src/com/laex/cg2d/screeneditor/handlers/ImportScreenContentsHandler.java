package com.laex.cg2d.screeneditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;


public class ImportScreenContentsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    
    ImportScreenContentsDialog screensDialog = new ImportScreenContentsDialog(ep.getSite().getShell(), gv.getEditDomain()
        .getCommandStack());
    int responseCode = screensDialog.open();
    if (responseCode == ImportScreenContentsDialog.CANCEL) {
      return null;
    }
    
    return null;
  }

}
