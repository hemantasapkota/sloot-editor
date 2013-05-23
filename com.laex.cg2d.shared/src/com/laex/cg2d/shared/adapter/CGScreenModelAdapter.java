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
package com.laex.cg2d.shared.adapter;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.laex.cg2d.protobuf.ScreenModel.CGBodyDef;
import com.laex.cg2d.protobuf.ScreenModel.CGBodyType;
import com.laex.cg2d.protobuf.ScreenModel.CGBounds;
import com.laex.cg2d.protobuf.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.protobuf.ScreenModel.CGFilter;
import com.laex.cg2d.protobuf.ScreenModel.CGFixtureDef;
import com.laex.cg2d.protobuf.ScreenModel.CGJoint;
import com.laex.cg2d.protobuf.ScreenModel.CGLayer;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenModel;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenPreferences;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenPreferences.CardPreferences;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenPreferences.DebugDrawPreferences;
import com.laex.cg2d.protobuf.ScreenModel.CGScreenPreferences.WorldPreferences;
import com.laex.cg2d.protobuf.ScreenModel.CGShape;
import com.laex.cg2d.protobuf.ScreenModel.CGVector2;
import com.laex.cg2d.shared.model.EditorShapeType;
import com.laex.cg2d.shared.model.GameModel;
import com.laex.cg2d.shared.model.Joint;
import com.laex.cg2d.shared.model.Layer;
import com.laex.cg2d.shared.model.Shape;
import com.laex.cg2d.shared.prefs.PreferenceConstants;
import com.laex.cg2d.shared.util.BooleanUtil;
import com.laex.cg2d.shared.util.FloatUtil;
import com.laex.cg2d.shared.util.IntegerUtil;

/**
 * The Class CGGameModelAdapter.
 */
public class CGScreenModelAdapter {

  /** The model. */
  GameModel model;

  /** The prefs. */
  private Map<String, String> prefs;

  /**
   * Instantiates a new cG game model adapter.
   * 
   * @param model
   *          the model
   * @param prefs
   *          the prefs
   */
  public CGScreenModelAdapter(GameModel model, Map<String, String> prefs) {
    this.model = model;
    this.prefs = prefs;
  }

  /**
   * As cg body type.
   * 
   * @param bt
   *          the bt
   * @return the cG body type
   */
  private static CGBodyType asCGBodyType(BodyType bt) {
    switch (bt) {
    case StaticBody:
      return CGBodyType.STATIC;
    case DynamicBody:
      return CGBodyType.DYNAMIC;
    case KinematicBody:
      return CGBodyType.KINEMATIC;
    }
    return null;
  }

  /**
   * As cg editor type.
   * 
   * @param st
   *          the st
   * @return the cG editor shape type
   */
  private static CGEditorShapeType asCGEditorType(EditorShapeType st) {
    switch (st) {
    case BACKGROUND_SHAPE:
      return CGEditorShapeType.BACKGROUND_SHAPE;
    case ENTITY_SHAPE:
      return CGEditorShapeType.ENTITY_SHAPE;
    case SIMPLE_SHAPE_BOX:
      return CGEditorShapeType.SIMPLE_SHAPE_BOX;
    case SIMPLE_SHAPE_CIRCLE:
      return CGEditorShapeType.SIMPLE_SHAPE_CIRCLE;
    case SIMPLE_SHAPE_HEDGE:
      return CGEditorShapeType.SIMPLE_SHAPE_HEDGE;
    case SIMPLE_SHAPE_VEDGE:
      return CGEditorShapeType.SIMPLE_SHAPE_VEDGE;
    default:
      break;
    }
    return null;
  }

  /**
   * Make cg body def.
   * 
   * @param bdef
   *          the bdef
   * @return the cG body def. builder
   */
  public static CGBodyDef.Builder makeCGBodyDef(BodyDef bdef) {
    return CGBodyDef.newBuilder().setActive(bdef.active).setAllowSleep(bdef.allowSleep).setAngle(bdef.angle)
        .setAngularDamping(bdef.angularDamping).setAngularVelocity(bdef.angularVelocity).setAwake(bdef.awake)
        .setBullet(bdef.bullet).setFixedRotation(bdef.fixedRotation).setLinearDamping(bdef.linearDamping)
        .setGravityScale(bdef.gravityScale)
        .setLinearVelocity(CGVector2.newBuilder().setX(bdef.linearVelocity.x).setY(bdef.linearVelocity.y).build())
        .setType(asCGBodyType(bdef.type))
        .setPosition(CGVector2.newBuilder().setX(bdef.position.x).setY(bdef.position.y));
  }

  /**
   * Make cg fixture def.
   * 
   * @param fdef
   *          the fdef
   * @return the cG fixture def. builder
   */
  public static CGFixtureDef.Builder makeCGFixtureDef(FixtureDef fdef) {
    return CGFixtureDef
        .newBuilder()
        .setDensity(fdef.density)
        .setFriction(fdef.friction)
        .setRestitution(fdef.restitution)
        .setSensor(fdef.isSensor)
        .setFilter(
            CGFilter.newBuilder().setCategoryBits(fdef.filter.categoryBits).setGroupIndex(fdef.filter.groupIndex)
                .setMaskBits(fdef.filter.maskBits));
  }

  /**
   * Make shape.
   * 
   * @param fdef
   *          the fdef
   * @param bdef
   *          the bdef
   * @param s
   *          the s
   * @return the cG shape. builder
   */
  public static CGShape.Builder makeShape(CGFixtureDef fdef, CGBodyDef bdef, Shape s) {
    Rectangle b = s.getBounds();
    CGBounds pBounds = CGBounds.newBuilder().setX(b.x).setY(b.y).setWidth(b.width).setHeight(b.height).build();

    return CGShape.newBuilder().setId(s.getId()).setVisible(s.isVisible()).setLocked(s.isLocked())
        .setBackground(s.isBackground())
        .setBackgroundResourceFile(ResourceFileAdapter.asCGResourceFile(s.getBackgroundResourceFile()))
        .setEditorShapeType(asCGEditorType(s.getEditorShapeType())).setBounds(pBounds).setBodyDef(bdef)
        .setFixtureDef(fdef);

  }

  /**
   * As cg game model.
   * 
   * @return the cG game model
   */
  public CGScreenModel asCGGameModel() {
    CGScreenModel.Builder cgModelBuilder = CGScreenModel.newBuilder();

    Map<Shape, CGShape> shapeToCGShapeMap = new HashMap<Shape, CGShape>();

    // Shapes
    for (Layer l : model.getDiagram().getLayers()) {
      for (Shape s : l.getChildren()) {
        BodyDef bdef = s.getBodyDef();
        FixtureDef fdef = s.getFixtureDef();
        CGBodyDef pBodyDef = makeCGBodyDef(bdef).build();
        CGFixtureDef pFixtureDef = makeCGFixtureDef(fdef).build();
        CGShape.Builder shapeBuilder = makeShape(pFixtureDef, pBodyDef, s);

        // create entity
        if (s.getEditorShapeType().isEntity()) {
          shapeBuilder.setEntityRefFile(ResourceFileAdapter.asCGResourceFile(s.getEntityResourceFile()));
        }

        CGShape cgShape = shapeBuilder.build();
        shapeToCGShapeMap.put(s, cgShape);
      }
    }

    // Build joints: For building joints all the shapes must have been created.
    for (Layer l : model.getDiagram().getLayers()) {

      CGLayer.Builder layerBuilder = CGLayer.newBuilder().setId(l.getId()).setLocked(l.isLocked())
          .setVisible(l.isVisible()).setName(l.getName());

      for (Shape s : l.getChildren()) {

        // buid source joints. There's no need to build for s.getTargetJoints().
        for (Joint j : s.getSourceJoints()) {
          buildJoints(shapeToCGShapeMap, s, j);
        }

        layerBuilder = layerBuilder.addShape(shapeToCGShapeMap.get(s));
      }

      cgModelBuilder.addLayers(layerBuilder.build());
    }

    // add preferences
    CardPreferences cf = CardPreferences.newBuilder()
        .setCardHeight(IntegerUtil.toInt((prefs.get(PreferenceConstants.CARD_HEIGHT))))
        .setCardWidth(IntegerUtil.toInt(prefs.get(PreferenceConstants.CARD_WIDTH)))
        .setCardNoX(IntegerUtil.toInt(prefs.get(PreferenceConstants.CARD_NO_X)))
        .setCardNoY(IntegerUtil.toInt(prefs.get(PreferenceConstants.CARD_NO_Y))).build();

    DebugDrawPreferences ddp = DebugDrawPreferences.newBuilder()
        .setDrawAABB(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_AABB)))
        .setDrawBodies(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_BODIES)))
        .setDrawDebugData(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_DEBUG_DATA)))
        .setDrawEntities(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_ENTITIES)))
        .setInstallMouseJoint(BooleanUtil.toBool(prefs.get(PreferenceConstants.INSTALL_MOUSE_JOINT)))
        .setDrawJoints(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_JOINT)))
        .setDrawInactiveBodies(BooleanUtil.toBool(prefs.get(PreferenceConstants.DRAW_INACTIVE_BODIES))).build();

    WorldPreferences wp = WorldPreferences.newBuilder()
        .setGravityX(FloatUtil.toFloat(prefs.get(PreferenceConstants.GRAVITY_X)))
        .setGravityY(FloatUtil.toFloat(prefs.get(PreferenceConstants.GRAVITY_Y)))
        .setPositionIterations(IntegerUtil.toInt(prefs.get(PreferenceConstants.POSITION_ITERATIONS)))
        .setVelocityIterations(IntegerUtil.toInt(prefs.get(PreferenceConstants.VELOCITY_ITERATIONS)))
        .setPtmRatio(IntegerUtil.toInt(prefs.get(PreferenceConstants.PTM_RATIO)))
        .setTimeStep(FloatUtil.toFloat(prefs.get(PreferenceConstants.TIMESTEP))).build();

    CGScreenPreferences screenPrefs = CGScreenPreferences.newBuilder().setCardPrefs(cf).setDebugDrawPrefs(ddp)
        .setWorldPrefs(wp).build();

    return cgModelBuilder.setScreenPrefs(screenPrefs).build();
  }

  /**
   * When we are building joints, we only take the source joints from Shape.
   * There's no need to make joints for target.
   * 
   * @param shapeToCGShapeMap
   *          the shape to cg shape map
   * @param s
   *          the s
   * @param j
   *          the j
   */
  private void buildJoints(Map<Shape, CGShape> shapeToCGShapeMap, Shape s, Joint j) {
    CGShape source = shapeToCGShapeMap.get(j.getSource());
    CGShape target = shapeToCGShapeMap.get(j.getTarget());

    CGJoint cgJoint = JointAdapter.asCGJoint(j, s, source, target);

    source = CGShape.newBuilder(source).addJoints(cgJoint).build();

    // update the map
    shapeToCGShapeMap.put(s, source);
  }

}
