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
package com.laex.cg2d.screeneditor.palette;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.requests.CreationFactory;

import com.badlogic.gdx.math.Rectangle;
import com.laex.cg2d.model.EntityManager;
import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.IDCreationStrategy;
import com.laex.cg2d.model.model.IDCreationStrategyFactory;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.ResourceFile;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.resources.ResourceManager;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;

/**
 * A factory for creating ShapeCreation objects.
 */
public class ShapeCreationFactory implements CreationFactory {

  /** The creation info. */
  ShapeCreationInfo creationInfo = null;

  /**
   * Instantiates a new shape creation factory.
   * 
   * @param creationInfo
   *          the creation info
   */
  public ShapeCreationFactory(ShapeCreationInfo creationInfo) {
    this.creationInfo = creationInfo;
  }

  /**
   * Gets the selected layer.
   * 
   * @return the selected layer
   */
  private Layer getSelectedLayer() {
    return ScreenEditorUtil.getScreenLayerManager().getCurrentLayer();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
   */
  @Override
  public Object getNewObject() {
    Layer layer = getSelectedLayer();

    IDCreationStrategy creator = IDCreationStrategyFactory.getIDCreator(ScreenEditorUtil.getScreenModel());

    Shape shape = createShape();
    shape.setParentLayer(layer);
    shape.setId(creator.newId(shape.getEditorShapeType()));

    if (layer != null) {
      layer.add(shape);
    }

    switch (shape.getEditorShapeType()) {
    case BACKGROUND_SHAPE:
      shape.setBackground(true);

      String bgResFile = ScreenEditorUtil.resourceString(creationInfo.getBackgroundResourceFile());
      String bgResFileAbs = ScreenEditorUtil.resourceStringAbsolute(creationInfo.getBackgroundResourceFile());
      shape.setBackgroundResourceFile(ResourceFile.create(bgResFile, bgResFileAbs));

      Rectangle bounds = RectAdapter.gdxRect(ResourceManager.getImageOfRelativePath(bgResFile).getBounds());

      shape.setBounds(bounds);
      break;

    case ENTITY_SHAPE:
      Entity e = EntityManager.entityManager().findEntity(
          creationInfo.getEntityResourceFile().getFullPath().toOSString());

      shape.setBounds(RectAdapter.gdxRect(e.getDefaultFrame().getBounds()));

      String rs1 = ScreenEditorUtil.resourceString(creationInfo.getEntityResourceFile());
      String rsAbs = ScreenEditorUtil.resourceStringAbsolute(creationInfo.getEntityResourceFile());
      ResourceFile eRf = ResourceFile.create(rs1, rsAbs);
      shape.setEntityResourceFile(eRf);
      break;

    case SIMPLE_SHAPE_BOX:
      shape.setBounds(new Rectangle(0, 0, 32, 32));
      break;

    case SIMPLE_SHAPE_CIRCLE:
      shape.setBounds(new Rectangle(0, 0, 32, 32));
      break;

    case SIMPLE_SHAPE_HEDGE:
      break;

    default:
      break;
    }

    creationInfo.setShape(shape);
    return creationInfo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
   */
  @Override
  public Object getObjectType() {
    return creationInfo.getEditorShapeType();
  }

  /**
   * Creates a new ShapeCreation object.
   * 
   * @return the shape
   */
  private Shape createShape() {
    EditorShapeType t = creationInfo.getEditorShapeType();
    Shape shape = new Shape(t);
    return shape;
  }
}
