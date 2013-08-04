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

import org.eclipse.core.resources.IFile;

import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.ScreenEditorUtil;

/**
 * The Class ShapeCreationInfo.
 */
public class ShapeCreationInfo {

  /** The editor shape type. */
  private EditorShapeType editorShapeType;

  /** The layer. */
  private Layer layer;

  /** The shape. */
  private Shape shape;

  /** The entity resource file. */
  private IFile entityResourceFile;

  /** The background resource file. */
  private IFile backgroundResourceFile;

  /** The background resource file absolute. */
  private String backgroundResourceFileAbsolute;

  /**
   * Instantiates a new shape creation info.
   */
  private ShapeCreationInfo() {
  }

  /**
   * Instantiates a new shape creation info.
   * 
   * @param builder
   *          the builder
   */
  private ShapeCreationInfo(Builder builder) {
    shape = builder.creationInfo.shape;
    entityResourceFile = builder.creationInfo.entityResourceFile;
    backgroundResourceFile = builder.creationInfo.backgroundResourceFile;
    backgroundResourceFileAbsolute = builder.creationInfo.backgroundResourceFileAbsolute;
    editorShapeType = builder.creationInfo.editorShapeType;
    layer = builder.creationInfo.layer;
  }

  /**
   * Gets the layer.
   * 
   * @return the layer
   */
  public Layer getLayer() {
    return layer;
  }

  /**
   * Gets the editor shape type.
   * 
   * @return the editor shape type
   */
  public EditorShapeType getEditorShapeType() {
    return editorShapeType;
  }

  /**
   * Gets the shape.
   * 
   * @return the shape
   */
  public Shape getShape() {
    return shape;
  }

  /**
   * Gets the entity resource file.
   * 
   * @return the entity resource file
   */
  public IFile getEntityResourceFile() {
    return entityResourceFile;
  }

  /**
   * Gets the background resource file.
   * 
   * @return the background resource file
   */
  public IFile getBackgroundResourceFile() {
    return backgroundResourceFile;
  }

  /**
   * Gets the background resource file absolute.
   * 
   * @return the background resource file absolute
   */
  public String getBackgroundResourceFileAbsolute() {
    return backgroundResourceFileAbsolute;
  }

  /**
   * Sets the shape.
   * 
   * @param shape
   *          the new shape
   */
  public void setShape(Shape shape) {
    this.shape = shape;
  }

  /**
   * The Class Builder.
   */
  public static class Builder {

    /** The creation info. */
    ShapeCreationInfo creationInfo = new ShapeCreationInfo();

    /**
     * Sets the editor shape type.
     * 
     * @param type
     *          the type
     * @return the builder
     */
    public Builder setEditorShapeType(EditorShapeType type) {
      creationInfo.editorShapeType = type;
      return this;
    }
    

    /**
     * Sets the entity resource file.
     * 
     * @param efile
     *          the efile
     * @return the builder
     */
    public Builder setEntityResourceFile(IFile efile) {
      creationInfo.entityResourceFile = efile;
      return this;
    }

    /**
     * Sets the background resource file.
     * 
     * @param bfile
     *          the bfile
     * @return the builder
     */
    public Builder setBackgroundResourceFile(IFile bfile) {
      creationInfo.backgroundResourceFile = bfile;
      creationInfo.backgroundResourceFileAbsolute = ScreenEditorUtil.resourceStringAbsolute(bfile);
      return this;
    }

    /**
     * Builds the.
     * 
     * @return the shape creation info
     */
    public ShapeCreationInfo build() {
      ShapeCreationInfo sci = new ShapeCreationInfo(this);
      return sci;
    }

  }

}