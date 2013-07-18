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
package com.laex.cg2d.screeneditor.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.model.ScreenModel.CGBodyDef;
import com.laex.cg2d.model.ScreenModel.CGBodyType;
import com.laex.cg2d.model.ScreenModel.CGBounds;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGFixtureDef;
import com.laex.cg2d.model.ScreenModel.CGJoint;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.adapter.ResourceFileAdapter;
import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.GameModel;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Layer;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.screeneditor.Activator;

/**
 * The Class GameModelAdapter.
 */
public class ScreenModelAdapter {

  /**
   * As body type.
   * 
   * @param cbt
   *          the cbt
   * @return the body type
   */
  public static BodyType asBodyType(CGBodyType cbt) {
    switch (cbt) {
    case DYNAMIC:
      return BodyType.DynamicBody;

    case KINEMATIC:
      return BodyType.KinematicBody;

    case STATIC:
      return BodyType.StaticBody;

    default:
      break;
    }

    return null;
  }

  /**
   * As editor shape type.
   * 
   * @param type
   *          the type
   * @return the editor shape type
   */
  public static EditorShapeType asEditorShapeType(CGEditorShapeType type) {
    switch (type) {
    case BACKGROUND_SHAPE:
      return EditorShapeType.BACKGROUND_SHAPE;
    case ENTITY_SHAPE:
      return EditorShapeType.ENTITY_SHAPE;
    case SIMPLE_SHAPE_BOX:
      return EditorShapeType.SIMPLE_SHAPE_BOX;
    case SIMPLE_SHAPE_CIRCLE:
      return EditorShapeType.SIMPLE_SHAPE_CIRCLE;
    case SIMPLE_SHAPE_HEDGE:
      return EditorShapeType.SIMPLE_SHAPE_HEDGE;
    case SIMPLE_SHAPE_VEDGE:
      return EditorShapeType.SIMPLE_SHAPE_VEDGE;
    default:
      break;
    }
    return null;
  }

  /**
   * As rectangle.
   * 
   * @param b
   *          the b
   * @return the rectangle
   */
  public static Rectangle asRectangle(CGBounds b) {
    return new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
  }

  /**
   * As body def.
   * 
   * @param cgBodyDef
   *          the cg body def
   * @return the body def
   */
  public static BodyDef asBodyDef(CGBodyDef cgBodyDef) {
    BodyDef bdef = new BodyDef();
    bdef.active = cgBodyDef.getActive();
    bdef.allowSleep = cgBodyDef.getAllowSleep();
    bdef.angle = cgBodyDef.getAngle();
    bdef.angularDamping = cgBodyDef.getAngularDamping();
    bdef.angularVelocity = cgBodyDef.getAngularVelocity();
    bdef.awake = cgBodyDef.getAwake();
    bdef.bullet = cgBodyDef.getBullet();
    bdef.fixedRotation = cgBodyDef.getFixedRotation();
    bdef.gravityScale = cgBodyDef.getGravityScale();
    bdef.linearDamping = cgBodyDef.getLinearDamping();

    bdef.linearVelocity.x = cgBodyDef.getLinearVelocity().getX();
    bdef.linearVelocity.y = cgBodyDef.getLinearVelocity().getY();

    bdef.position.x = cgBodyDef.getPosition().getX();
    bdef.position.y = cgBodyDef.getPosition().getY();

    bdef.type = asBodyType(cgBodyDef.getType());

    return bdef;
  }

  /**
   * As fixture def.
   * 
   * @param fixDef
   *          the fix def
   * @return the fixture def
   */
  public static FixtureDef asFixtureDef(CGFixtureDef fixDef) {
    FixtureDef fdef = new FixtureDef();

    fdef.density = fixDef.getDensity();

    fdef.filter.categoryBits = (short) fixDef.getFilter().getCategoryBits();
    fdef.filter.groupIndex = (short) fixDef.getFilter().getGroupIndex();
    fdef.filter.maskBits = (short) fixDef.getFilter().getMaskBits();

    fdef.friction = fixDef.getFriction();
    fdef.isSensor = fixDef.getSensor();
    fdef.restitution = fixDef.getRestitution();

    return fdef;
  }

  /**
   * As shape.
   * 
   * @param cgShape
   *          the cg shape
   * @param layer
   *          the layer
   * @return the shape
   */
  public static Shape asShape(CGShape cgShape, Layer layer) {
    Shape shape = new Shape(asEditorShapeType(cgShape.getEditorShapeType()));
    shape.setId(cgShape.getId());
    shape.setLocked(cgShape.getLocked());
    shape.setBackground(cgShape.getBackground());
    shape.setBackgroundResourceFile(ResourceFileAdapter.asResourceFile(cgShape.getBackgroundResourceFile()));
    shape.setBounds(asRectangle(cgShape.getBounds()));
    shape.setEntityResourceFile(ResourceFileAdapter.asResourceFile(cgShape.getEntityRefFile()));
    shape.setParentLayer(layer);

    // set body def & fixture def
    shape.setPropertyValue(Shape.BODY_DEF_PROP, asBodyDef(cgShape.getBodyDef()));
    shape.setPropertyValue(Shape.FIXTURE_DEF_PROP, asFixtureDef(cgShape.getFixtureDef()));
    return shape;
  }

  /**
   * As game model.
   * 
   * @param cgModel
   *          the cg model
   * @return the game model
   */
  public static GameModel asGameModel(CGScreenModel cgModel) {
    GameModel model = new GameModel(cgModel.getScreenPrefs());
    Map<String, Shape> shapeMap = new HashMap<String, Shape>();

    // First step: make all the shapes
    for (CGLayer cgLayer : cgModel.getLayersList()) {
      Layer layer = new Layer(cgLayer.getId(), cgLayer.getName(), cgLayer.getVisible(), cgLayer.getLocked());

      model.getDiagram().getLayers().add(layer);

      for (CGShape cgShape : cgLayer.getShapeList()) {

        /*
         * Validate entities. If corresponding entity does not exist, do not
         * create the shape
         */
        if (cgShape.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE) {
          String resourceFile = cgShape.getEntityRefFile().getResourceFile();
          IPath entityPath = new Path(resourceFile);
          /* Entity does not exist */
          if (!ResourcesPlugin.getWorkspace().getRoot().getFile(entityPath).exists()) {
            /* Further if an entity exists with the same mapping, then remove it */
            Activator.getDefault().getEntitiesMap().remove(resourceFile);
            continue;
          }

          Entity e = Activator.getDefault().getEntitiesMap().get(resourceFile);
          if (e == null) {
            // Null means this entity does not exist. So we dont add this shape
            // as well.
            continue;
          }
        }

        Shape shape = ScreenModelAdapter.asShape(cgShape, layer);
        layer.add(shape);

        model.getDiagram().addChild(shape);

        shapeMap.put(cgShape.getId(), shape);

      }

    }

    // Second Step: make all the joints
    for (CGLayer cgLayer : cgModel.getLayersList()) {

      for (CGShape cgShape : cgLayer.getShapeList()) {

        for (CGJoint joint : cgShape.getJointsList()) {
          Shape source = shapeMap.get(joint.getSourceShapeId());
          Shape target = shapeMap.get(joint.getTargetShapeId());
          Joint jnt = JointAdapter.asJoint(joint, source, target);
          source.addJoint(jnt);
        }

      }
    }

    return model;
  }
}
