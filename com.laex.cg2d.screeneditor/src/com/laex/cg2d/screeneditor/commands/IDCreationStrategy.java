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
package com.laex.cg2d.screeneditor.commands;

import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;

/**
 * The Class IDCreationStrategy.
 */
public class IDCreationStrategy {

  /** The box format. */
  static String BOX_FORMAT = "box%d";

  /** The circle format. */
  static String CIRCLE_FORMAT = "circle%d";

  /** The entity format. */
  static String ENTITY_FORMAT = "entity%d";

  /** The edge format. */
  static String EDGE_FORMAT = "edge%d";

  /** The background format. */
  static String BACKGROUND_FORMAT = "bg%d";

  /**
   * Creates the.
   * 
   * @return the iD creation strategy
   */
  public static IDCreationStrategy create() {
    IDCreationStrategy ics = new IDCreationStrategy();
    return ics;
  }

  public String newId(EditorShapeType type) {
    
    switch (type) {
    
    case BACKGROUND_SHAPE:
      return newBackgroundId(type);

    case ENTITY_SHAPE:
      return newEntityId(type);

    case SIMPLE_SHAPE_BOX:
      return newBoxId(type);

    case SIMPLE_SHAPE_CIRCLE:
      return newCirlceId(type);

    case SIMPLE_SHAPE_HEDGE:
      return newEdgeId(type);

    case SIMPLE_SHAPE_VEDGE:
      return newEdgeId(type);

    default:

    }

    return "otherId";
  }

  /**
   * New box id.
   * 
   * @param type
   *          the type
   * @return the string
   */
  private String newBoxId(EditorShapeType type) {
    return String.format(BOX_FORMAT, getAvailableId(type));
  }

  /**
   * New cirlce id.
   * 
   * @param type
   *          the type
   * @return the string
   */
  private String newCirlceId(EditorShapeType type) {
    return String.format(CIRCLE_FORMAT, getAvailableId(type));
  }

  /**
   * New entity id.
   * 
   * @param type
   *          the type
   * @return the string
   */
  private String newEntityId(EditorShapeType type) {
    return String.format(ENTITY_FORMAT, getAvailableId(type));
  }

  /**
   * New edge id.
   * 
   * @param type
   *          the type
   * @return the string
   */
  private String newEdgeId(EditorShapeType type) {
    return String.format(EDGE_FORMAT, getAvailableId(type));
  }

  /**
   * New background id.
   * 
   * @param type
   *          the type
   * @return the string
   */
  private String newBackgroundId(EditorShapeType type) {
    return String.format(BACKGROUND_FORMAT, getAvailableId(type));
  }

  /**
   * Checks if is id used.
   * 
   * @param model
   *          the model
   * @param type
   *          the type
   * @param id
   *          the id
   * @return true, if is id used
   */
  private synchronized boolean isIdUsed(GameModel model, EditorShapeType type, String id) {
    boolean used = false;
    for (Shape shp : model.getDiagram().getChildren()) {
      if (shp.getEditorShapeType() == type) {
        if (shp.getId().endsWith(id)) {
          used = true;
          break;
        }
      }
    }
    return used;
  }

  /**
   * Gets the available id.
   * 
   * @param type
   *          the type
   * @return the available id
   */
  private synchronized int getAvailableId(EditorShapeType type) {
    GameModel model = ScreenEditorUtil.getScreenModel();
    int id = 0;
    for (Shape shp : model.getDiagram().getChildren()) {
      if (shp.getEditorShapeType() == type) {
        String idStr = String.valueOf(id);
        if (!isIdUsed(model, shp.getEditorShapeType(), idStr)) {
          return id;
        }
        id++;
      }
    }

    return id;
  }

}
