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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.laex.cg2d.model.IScreenEditorState;

/**
 * The Class ToggleGridHandler.
 */
public class ToggleGridHandler extends AbstractHandler {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
   * .ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    final IEditorPart editorPart = window.getActivePage().getActiveEditor();

    IScreenEditorState editorState = (IScreenEditorState) editorPart;
    editorState.toggleGrid();

    return null;
  }

}
