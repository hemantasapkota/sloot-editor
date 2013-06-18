/*
 * Copyright (c) 2012, 2013 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package com.laex.cg2d.screeneditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand.DeleteCommandType;
import com.laex.cg2d.screeneditor.editparts.ShapeEditPart;

/**
 * The Class ShapeDeleteHandler.
 */
public class ShapeDeleteHandler extends AbstractHandler {

  /* (non-Javadoc)
   * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    //Copy before deleting
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    GameModel model = (GameModel) ep.getAdapter(GameModel.class);
    
    CompoundCommand cc = new CompoundCommand();
    for (Object o : gv.getSelectedEditParts()) {
      ShapeEditPart sep = (ShapeEditPart) o;
      ShapeDeleteCommand sdc = new ShapeDeleteCommand(model.getDiagram(), (Shape) sep.getModel(), DeleteCommandType.UNDOABLE);
      cc.add(sdc);
    }
    gv.getEditDomain().getCommandStack().execute(cc);
    
    return null;
  }

}
