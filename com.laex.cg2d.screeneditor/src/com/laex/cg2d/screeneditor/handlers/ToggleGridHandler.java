package com.laex.cg2d.screeneditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.IScreenEditorState;

public class ToggleGridHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    final IEditorPart editorPart = window.getActivePage().getActiveEditor();
    
    IScreenEditorState editorState = (IScreenEditorState) editorPart;
    editorState.toggleGrid();

    return null;
  }

}
