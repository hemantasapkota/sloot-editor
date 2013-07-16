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

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.commands.ShapeChangeIDCommand;

/**
 * The Class EditShapeIDHandler.
 */
public class EditShapeIDHandler extends AbstractHandler {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
   * .ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = (IEditorPart) HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);

    if (gv.getSelectedEditParts().size() <= 0)
      return null;

    EditShapeIDDialog dlg = new EditShapeIDDialog(ep.getSite().getShell(), gv.getSelectedEditParts());
    int resp = dlg.open();
    if (resp == EditShapeIDDialog.CANCEL) {
      return null;
    }

    Map<Shape, String> updates = dlg.getUpdateIdMap();
    CompoundCommand cc = new CompoundCommand("Edit Shape ID");
    for (Shape shape : updates.keySet()) {
      ShapeChangeIDCommand cmd = new ShapeChangeIDCommand(shape, updates.get(shape));
      cc.add(cmd);
    }
    gv.getEditDomain().getCommandStack().execute(cc);
    return null;
  }

}
