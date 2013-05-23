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
package com.laex.cg2d.screeneditor.factory;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

import com.laex.cg2d.screeneditor.commands.IDCreationStrategy;
import com.laex.cg2d.screeneditor.views.LayersViewPart;
import com.laex.cg2d.shared.model.EditorShapeType;
import com.laex.cg2d.shared.model.Entity;
import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.ResourceFile;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.util.EntitiesUtil;
import com.laex.cg2d.shared.util.PlatformUtil;

/**
 * A factory for creating ShapeCreation objects.
 */
public class ShapeCreationFactory implements CreationFactory {

  /** The creation info. */
  ShapeCreationInfo creationInfo = null;

  /** The id creator. */
  IDCreationStrategy idCreator = IDCreationStrategy.create();

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
    Layer layer = (Layer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(LayersViewPart.ID).getAdapter(Layer.class);
    return layer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
   */
  @Override
  public Object getNewObject() {
    Shape shape = createShape();

    shape.setParentLayer(getSelectedLayer());

    switch (shape.getEditorShapeType()) {
    case BACKGROUND_SHAPE:
      shape.setId(idCreator.newBackgroundId(shape.getEditorShapeType()));
      shape.setBackground(true);

      String bgResFile = PlatformUtil.resourceString(creationInfo.getBackgroundResourceFile());
      String bgResFileAbs = PlatformUtil.resourceStringAbsolute(creationInfo.getBackgroundResourceFile());
      shape.setBackgroundResourceFile(ResourceFile.create(bgResFile, bgResFileAbs));

      break;

    case ENTITY_SHAPE:
      shape.setId(idCreator.newEntityId(shape.getEditorShapeType()));
      try {
        Entity e = EntitiesUtil.createEntityModelFromFile(creationInfo.getEntityResourceFile());
        if (e != null) {
          Image defaultFrame = EntitiesUtil.getDefaultFrame(e);
          e.setDefaultFrame(defaultFrame);
          shape.setEntity(e);
        }
      } catch (CoreException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }

      String rs1 = PlatformUtil.resourceString(creationInfo.getEntityResourceFile());
      String rsAbs = PlatformUtil.resourceStringAbsolute(creationInfo.getEntityResourceFile());
      ResourceFile eRf = ResourceFile.create(rs1, rsAbs);
      shape.setEntityResourceFile(eRf);
      break;

    case SIMPLE_SHAPE_BOX:
      shape.setId(idCreator.newBoxId(shape.getEditorShapeType()));
      break;

    case SIMPLE_SHAPE_CIRCLE:
      shape.setId(idCreator.newCirlceId(shape.getEditorShapeType()));
      break;

    case SIMPLE_SHAPE_HEDGE:
      shape.setId(idCreator.newEdgeId(shape.getEditorShapeType()));
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
