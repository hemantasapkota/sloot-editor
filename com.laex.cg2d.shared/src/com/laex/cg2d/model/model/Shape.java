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
package com.laex.cg2d.model.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.descs.BodyDefPropertySource;
import com.laex.cg2d.model.descs.FixtureDefPropertySource;
import com.laex.cg2d.model.descs.RectanglePropertySource;

/**
 * The Class Shape.
 */
public class Shape extends ModelElement {

  /** The Constant SHAPE_ID. */
  public static final String SHAPE_ID = "Shape.shapeId";

  /** The Constant SHAPE_VISIBLE. */
  public static final String SHAPE_VISIBLE = "Shape.visible";

  /** The Constant SHAPE_LOCKED. */
  public static final String SHAPE_LOCKED = "Shape.locked";

  /** The Constant BOUNDS_PROP. */
  public static final String BOUNDS_PROP = "Shape.Bounds";

  /** The Constant BODY_DEF_PROP. */
  public static final String BODY_DEF_PROP = "Shape.BodyDef";

  /** The Constant FIXTURE_DEF_PROP. */
  public static final String FIXTURE_DEF_PROP = "Shape.FixtureDef";

  /** The Constant BACKGROUND_ENTITY. */
  public static final String BACKGROUND_ENTITY = "Shape.BackgroundEntity";
  // represents joints
  /** The Constant SOURCE_JOINT_PROP. */
  public static final String SOURCE_JOINT_PROP = "Shape.SourceJoint";

  /** The Constant TARGET_JOINT_PROP. */
  public static final String TARGET_JOINT_PROP = "Shape.TargetJoint";
  
  /** The Constant FIGURE_CHANGED. */
  public static final String FIGURE_CHANGED = "Shape.FigureChanged";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  /** The bg descriptor. */
  private static IPropertyDescriptor[] bgDescriptor;
  static {
    bgDescriptor = new IPropertyDescriptor[]
      {
          new TextPropertyDescriptor(SHAPE_ID, "#ID"),
          new PropertyDescriptor(BOUNDS_PROP, "Bounds"),
          new PropertyDescriptor(BACKGROUND_ENTITY, "Background") };
  }

  static {
    descriptors = new IPropertyDescriptor[]
      {
          new TextPropertyDescriptor(SHAPE_ID, "#ID"),
          new PropertyDescriptor(BOUNDS_PROP, "Bounds"),
          new PropertyDescriptor(BODY_DEF_PROP, "Body"),
          new PropertyDescriptor(FIXTURE_DEF_PROP, "Fixture") };
  }

  /** The id. */
  private String id;

  private String text;

  /** The visible. */
  private boolean visible = true;

  /** The locked. */
  private boolean locked = false;

  /** The background. */
  private Boolean background = false;

  /** The background resource file. */
  private ResourceFile backgroundResourceFile;

  /** The bounds. */
  private Rectangle bounds = new Rectangle(0, 0, 16, 16);

  /** The body def. */
  private BodyDef bodyDef = new BodyDef();

  /** The fixture def. */
  private FixtureDef fixtureDef = new FixtureDef();

  /** The parent layer. */
  private Layer parentLayer = null;

  /** The editor shape type. */
  private EditorShapeType editorShapeType;

  /** The entity resource file. */
  private ResourceFile entityResourceFile;

  /** The source joints. */
  private List<Joint> sourceJoints = new ArrayList<Joint>();

  /** The target joints. */
  private List<Joint> targetJoints = new ArrayList<Joint>();

  /**
   * Instantiates a new shape.
   * 
   * @param shapeType
   *          the shape type
   */
  public Shape(EditorShapeType shapeType) {
    id = StringUtils.EMPTY;
    text = StringUtils.EMPTY;
    backgroundResourceFile = ResourceFile.create(StringUtils.EMPTY, StringUtils.EMPTY);
    this.editorShapeType = shapeType;
    this.visible = true;
    this.locked = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.ModelElement#getPropertyDescriptors()
   */
  public IPropertyDescriptor[] getPropertyDescriptors() {
    if (editorShapeType.isBackground()) {
      return bgDescriptor;
    }
    return descriptors;
  }

  /**
   * Gets the id.
   * 
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the background resource file.
   * 
   * @return the background resource file
   */
  public ResourceFile getBackgroundResourceFile() {
    return backgroundResourceFile;
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
   * Gets the parent layer.
   * 
   * @return the parent layer
   */
  public Layer getParentLayer() {
    return parentLayer;
  }

  /**
   * Gets the body def.
   * 
   * @return the body def
   */
  public BodyDef getBodyDef() {
    return bodyDef;
  }

  /**
   * Gets the fixture def.
   * 
   * @return the fixture def
   */
  public FixtureDef getFixtureDef() {
    return fixtureDef;
  }

  
  /**
   * Gets the entity resource file.
   * 
   * @return the entity resource file
   */
  public ResourceFile getEntityResourceFile() {
    return entityResourceFile;
  }

  /**
   * Gets the bounds.
   * 
   * @return the bounds
   */
  public Rectangle getBounds() {
    return bounds;
  }

  /**
   * Checks if is background.
   * 
   * @return the boolean
   */
  public Boolean isBackground() {
    return background;
  }

  /**
   * Checks if is visible.
   * 
   * @return true, if is visible
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Checks if is locked.
   * 
   * @return true, if is locked
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * Gets the source joints.
   * 
   * @return the source joints
   */
  public List<Joint> getSourceJoints() {
    return sourceJoints;
  }

  /**
   * Gets the target joints.
   * 
   * @return the target joints
   */
  public List<Joint> getTargetJoints() {
    return targetJoints;
  }

  /**
   * Sets the background resource file.
   * 
   * @param backgroundResourceFile
   *          the new background resource file
   */
  public void setBackgroundResourceFile(ResourceFile backgroundResourceFile) {
    this.backgroundResourceFile = backgroundResourceFile;
  }


  /**
   * Sets the entity resource file.
   * 
   * @param entityResourceFile
   *          the new entity resource file
   */
  public void setEntityResourceFile(ResourceFile entityResourceFile) {
    this.entityResourceFile = entityResourceFile;
  }

  /**
   * Sets the parent layer.
   * 
   * @param parentLayer
   *          the new parent layer
   */
  public void setParentLayer(Layer parentLayer) {
    this.parentLayer = parentLayer;
  }

  /**
   * Sets the visible.
   * 
   * @param visible
   *          the new visible
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
    firePropertyChange(SHAPE_VISIBLE, null, this.visible);
  }

  /**
   * Sets the locked.
   * 
   * @param locked
   *          the new locked
   */
  public void setLocked(boolean locked) {
    this.locked = locked;
    firePropertyChange(SHAPE_LOCKED, null, this.locked);
  }

  /**
   * Sets the background.
   * 
   * @param background
   *          the new background
   */
  public void setBackground(Boolean background) {
    this.background = background;
  }

  /**
   * Adds the joint.
   * 
   * @param jon
   *          the jon
   */
  public void addJoint(Joint jon) {
    boolean isSourceEqualTarget = jon.getSource().getId().equals(jon.getTarget().getId());
    boolean isSourceEqualThis = jon.getSource().getId().equals(this.id);
    boolean isTargetEqualThis = jon.getTarget().getId().equals(this.id);

    if (jon == null || isSourceEqualTarget) {
      throw new IllegalArgumentException();
    }

    if (isSourceEqualThis && !sourceJoints.contains(jon)) {
      sourceJoints.add(jon);
      firePropertyChange(SOURCE_JOINT_PROP, null, jon);
    } else if (isTargetEqualThis && !targetJoints.contains(jon)) {
      targetJoints.add(jon);
      firePropertyChange(TARGET_JOINT_PROP, null, jon);
    }

  }

  /**
   * Removes the joint.
   * 
   * @param jon
   *          the jon
   */
  void removeJoint(Joint jon) {
    if (jon == null) {
      throw new IllegalArgumentException();
    }

    boolean isSourceEqualThis = jon.getSource().getId().equals(this.id);
    boolean isTargetEqualThis = jon.getTarget().getId().equals(this.id);

    if (isSourceEqualThis) {
      sourceJoints.remove(jon);
      firePropertyChange(SOURCE_JOINT_PROP, null, jon);
    } else if (isTargetEqualThis) {
      targetJoints.remove(jon);
      firePropertyChange(TARGET_JOINT_PROP, null, jon);
    }
  }

  /**
   * Sets the id.
   * 
   * @param id
   *          the new id
   */
  public void setId(String id) {
    if (id != null) {
      this.id = id;
      firePropertyChange(SHAPE_ID, null, this.id);
    }
  }

  /**
   * Sets the body def.
   * 
   * @param value
   *          the new body def
   */
  private void setBodyDef(BodyDef value) {
    if (value != null) {
      this.bodyDef = value;
      firePropertyChange(BODY_DEF_PROP, null, this.bodyDef);
    }
  }

  /**
   * Sets the fixture def.
   * 
   * @param value
   *          the new fixture def
   */
  private void setFixtureDef(FixtureDef value) {
    if (value != null) {
      this.fixtureDef = value;
      firePropertyChange(FIXTURE_DEF_PROP, null, this.bodyDef);
    }
  }
  
  /**
   * Notify figure changed.
   */
  public void notifyFigureChanged() {
    firePropertyChange(FIGURE_CHANGED, null, null);
  }

  /**
   * Sets the bounds.
   * 
   * @param value
   *          the new bounds
   */
  public void setBounds(Rectangle value) {
    if (value != null) {
      bounds = value;
      firePropertyChange(BOUNDS_PROP, null, this.bounds);
    }
  }

  /**
   * Checks if is background entity prop.
   * 
   * @param propertyId
   *          the property id
   * @return true, if is background entity prop
   */
  private boolean isBackgroundEntityProp(Object propertyId) {
    return BACKGROUND_ENTITY.equals(propertyId);
  }

  /**
   * Checks if is shape id prop.
   * 
   * @param propertyId
   *          the property id
   * @return true, if is shape id prop
   */
  private boolean isShapeIdProp(Object propertyId) {
    return SHAPE_ID.equals(propertyId);
  }

  /**
   * Checks if is bounds prop.
   * 
   * @param propertyId
   *          the property id
   * @return true, if is bounds prop
   */
  private boolean isBoundsProp(Object propertyId) {
    return BOUNDS_PROP.equals(propertyId);
  }

  /**
   * Checks if is body def prop.
   * 
   * @param propertyId
   *          the property id
   * @return true, if is body def prop
   */
  private boolean isBodyDefProp(Object propertyId) {
    return BODY_DEF_PROP.equals(propertyId);
  }

  /**
   * Checks if is fixture def prop.
   * 
   * @param propertyId
   *          the property id
   * @return true, if is fixture def prop
   */
  private boolean isFixtureDefProp(Object propertyId) {
    return FIXTURE_DEF_PROP.equals(propertyId);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  public void setPropertyValue(Object propertyId, Object value) {
    if (isShapeIdProp(propertyId)) {
      // we dont set shape id here.
    } else if (isBodyDefProp(propertyId)) {
      setBodyDef((BodyDef) value);
    } else if (isFixtureDefProp(propertyId)) {
      setFixtureDef((FixtureDef) value);
    } else if (isBoundsProp(propertyId)) {
      setBounds((Rectangle) value);
    } else {
      super.setPropertyValue(propertyId, value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#getPropertyValue(java.lang.Object)
   */
  public Object getPropertyValue(Object propertyId) {
    if (isShapeIdProp(propertyId)) {
      return id;
    }

    if (isBodyDefProp(propertyId)) {
      return new BodyDefPropertySource(this.bodyDef);
    }
    if (isFixtureDefProp(propertyId)) {
      return new FixtureDefPropertySource(this.fixtureDef);
    }
    if (isBoundsProp(propertyId)) {
      boolean sizeImmutable = editorShapeType.isBackground() || editorShapeType.isEntity();
      return new RectanglePropertySource(this.bounds, sizeImmutable);
    }

    if (isBackgroundEntityProp(propertyId)) {
      return background.toString();
    }
    return super.getPropertyValue(propertyId);
  }

}