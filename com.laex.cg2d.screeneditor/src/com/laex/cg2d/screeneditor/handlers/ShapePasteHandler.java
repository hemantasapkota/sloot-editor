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

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.IDCreationStrategy;
import com.laex.cg2d.model.model.IDCreationStrategyFactory;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.model.ShapesDiagram;
import com.laex.cg2d.screeneditor.commands.ShapeCreateCommand;

/**
 * The Class ShapePasteHandler.
 */
public class ShapePasteHandler extends AbstractHandler {
  
  /** The id creator. */
  IDCreationStrategy idCreator;
  
  /**
   * Instantiates a new shape paste handler.
   */
  public ShapePasteHandler() {
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    GameModel model = (GameModel) ep.getAdapter(GameModel.class);
    
    if (Clipboard.getDefault().getContents() == null) {
      return null;
    }
    
    idCreator = IDCreationStrategyFactory.getIDCreator(model);

    Object contents = Clipboard.getDefault().getContents();
    if (!(contents instanceof List<?>)) {
      return null;
    }
    
    List<Shape> list = (List<Shape>) contents;
    for (Shape s : list) {
      ShapesDiagram diagram = model.getDiagram();
      
      s.setId(idCreator.newId(s.getEditorShapeType()));

      ShapeCreateCommand scmd = new ShapeCreateCommand(s, diagram);
      gv.getEditDomain().getCommandStack().execute(scmd);
    }

    
    Clipboard.getDefault().setContents(new Object());

    return null;
  }

}
