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

/**
 * The Class ShapeCopyHandler.
 */
public class ShapeCopyHandler extends AbstractHandler {

  /** The copier. */
  ModelCopier copier;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
   * .ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart ep = HandlerUtil.getActiveEditor(event);
    GraphicalViewer gv = (GraphicalViewer) ep.getAdapter(GraphicalViewer.class);
    GameModel gameModel = ScreenEditorUtil.getScreenModel();

    copier = ModelCopierFactory.getModelCopier(Shape.class);

    try {
      doCopy(gv, gameModel);
    } catch (CoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Do copy.
   * 
   * @param gv
   *          the gv
   * @param model
   *          the model
   * @throws CoreException
   *           the core exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
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