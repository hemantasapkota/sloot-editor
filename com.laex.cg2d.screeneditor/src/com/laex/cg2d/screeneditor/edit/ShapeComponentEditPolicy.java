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
package com.laex.cg2d.screeneditor.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand;
import com.laex.cg2d.screeneditor.commands.ShapeDeleteCommand.DeleteCommandType;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.model.ShapesDiagram;

/**
 * The Class ShapeComponentEditPolicy.
 */
public class ShapeComponentEditPolicy extends ComponentEditPolicy {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org
   * .eclipse.gef.requests.GroupRequest)
   */
  protected Command createDeleteCommand(GroupRequest deleteRequest) {
    Object parent = getHost().getParent().getModel();
    Object child = getHost().getModel();
    if (parent instanceof ShapesDiagram && child instanceof Shape) {
      return new ShapeDeleteCommand((ShapesDiagram) parent, (Shape) child, DeleteCommandType.UNDOABLE);
    }
    return super.createDeleteCommand(deleteRequest);
  }
}