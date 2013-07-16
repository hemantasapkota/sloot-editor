package com.laex.cg2d.screeneditor.handlers.joints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.IScreenEditorState;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.screeneditor.editparts.JointEditPart;

public class EditJointAnchorHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = (IEditorPart) HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);

    JointEditPart jep = (JointEditPart) gv.getSelectedEditParts().get(0);

    IScreenEditorState edState = (IScreenEditorState) ep;
    edState.toggleJointLayer((Joint) jep.getModel());

    return null;
  }

}