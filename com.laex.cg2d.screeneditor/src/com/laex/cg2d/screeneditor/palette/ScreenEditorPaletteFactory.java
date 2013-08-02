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

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.EditorShapeType;

/**
 * A factory for creating ScreenEditorPalette objects.
 */
public final class ScreenEditorPaletteFactory {

  /** The palette root. */
  private static PaletteRoot paletteRoot;

  /**
   * Instantiates a new screen editor palette factory.
   */
  private ScreenEditorPaletteFactory() {
  }


  /**
   * Creates a new ScreenEditorPalette object.
   * 
   * @return the palette container
   */
  private static PaletteContainer createShapesDrawer() {
    PaletteDrawer componentsDrawer = new PaletteDrawer("Shapes");

    {
      ShapeCreationInfo ci = new ShapeCreationInfo.Builder().setEditorShapeType(EditorShapeType.SIMPLE_SHAPE_CIRCLE)
          .build();
      ShapeCreationFactory scf = new ShapeCreationFactory(ci);

      CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("Circle", "Create an circle shape",
          ci.getEditorShapeType(), scf, SharedImages.CIRCLE, SharedImages.CIRCLE);
      componentsDrawer.add(component);
    }

    {
      ShapeCreationInfo ci = new ShapeCreationInfo.Builder().setEditorShapeType(EditorShapeType.SIMPLE_SHAPE_BOX)
          .build();
      ShapeCreationFactory scf = new ShapeCreationFactory(ci);

      CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("Box", "Create an box shape",
          ci.getEditorShapeType(), scf, SharedImages.BOX, SharedImages.BOX);
      componentsDrawer.add(component);
    }

    {

      ShapeCreationInfo ci = new ShapeCreationInfo.Builder().setEditorShapeType(EditorShapeType.SIMPLE_SHAPE_HEDGE)
          .build();
      ShapeCreationFactory scf = new ShapeCreationFactory(ci);

      CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("HEdge",
          "Create a horizonatl edge shape", ci.getEditorShapeType(), scf, SharedImages.BOX, SharedImages.BOX);
      componentsDrawer.add(component);
    }

    {

      ShapeCreationInfo ci = new ShapeCreationInfo.Builder().setEditorShapeType(EditorShapeType.SIMPLE_SHAPE_VEDGE)
          .build();
      ShapeCreationFactory scf = new ShapeCreationFactory(ci);

      CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("VEdge",
          "Create a vertical edge shape", ci.getEditorShapeType(), scf, SharedImages.BOX, SharedImages.BOX);
      componentsDrawer.add(component);
    }

    return componentsDrawer;
  }

  /**
   * Creates a new ScreenEditorPalette object.
   * 
   * @return the palette container
   */
  private static PaletteContainer createJointsDrawer() {
    PaletteDrawer componentsDrawer = new PaletteDrawer("Joints");
    componentsDrawer.add(getJointConnectionTool(JointType.DistanceJoint));
    componentsDrawer.add(getJointConnectionTool(JointType.RevoluteJoint));
    componentsDrawer.add(getJointConnectionTool(JointType.PrismaticJoint));
    componentsDrawer.add(getJointConnectionTool(JointType.PulleyJoint));
    componentsDrawer.add(getJointConnectionTool(JointType.FrictionJoint));
    componentsDrawer.add(getJointConnectionTool(JointType.WeldJoint));
    // componentsDrawer.add(getJointConnectionTool(JointType.GearJoint));
    // componentsDrawer.add(getJointConnectionTool(JointType.RopeJoint));
    // componentsDrawer.add(getJointConnectionTool(JointType.WheelJoint));
    return componentsDrawer;
  }

  /**
   * Creates the PaletteRoot and adds all palette elements. Use this factory
   * method to create a new palette for your graphical editor.
   * 
   * @return a new PaletteRoot
   */
  public static PaletteRoot createPalette() {
    paletteRoot = new PaletteRoot();
    paletteRoot.add(createToolsGroup(paletteRoot));
    paletteRoot.add(createShapesDrawer());
    paletteRoot.add(createJointsDrawer());
    return paletteRoot;
  }

  /**
   * Create the "Tools" group.
   * 
   * @param palette
   *          the palette
   * @return the palette container
   */
  private static PaletteContainer createToolsGroup(PaletteRoot palette) {
    PaletteToolbar toolbar = new PaletteToolbar("Tools");

    // Add a selection tool to the group
    ToolEntry tool = new PanningSelectionToolEntry();
    toolbar.add(tool);
    palette.setDefaultEntry(tool);

    // Add a marquee tool to the group
    toolbar.add(new MarqueeToolEntry());
    // toolbar.add(new CloneToolEntry());

    return toolbar;
  }

  /**
   * Gets the joint connection tool.
   * 
   * @param type
   *          the type
   * @return the joint connection tool
   */
  private static ConnectionCreationToolEntry getJointConnectionTool(final JointType type) {
    String name = "";
    String description = "";

    switch (type) {
    case DistanceJoint:
      name = "Distance";
      description = "Create a distance joint";
      break;
    case RevoluteJoint:
      name = "Revolute";
      description = "Create a revolute joint";
      break;
    case PrismaticJoint:
      name = "Prismatic";
      description = "Create a prismatic joint";
      break;
    case PulleyJoint:
      name = "Pulley";
      description = "Create a pulley joint";
      break;
    case FrictionJoint:
      name = "Friction";
      description = "Create a friction joint";
      break;
    case WeldJoint:
      name = "Weld";
      description = "Create a weld joint";
      break;
    case GearJoint:
      name = "Gear";
      description = "Create a gear joint";
      break;
    case RopeJoint:
      name = "Rope";
      description = "Create a rope joint";
      break;
    case WheelJoint:
      name = "Wheel";
      description = "Create a wheel joint";
    case Unknown:
    case MouseJoint:
      break;
    default:
      break;
    }

    ConnectionCreationToolEntry tool = new ConnectionCreationToolEntry(name, description, new CreationFactory() {

      @Override
      public Object getObjectType() {
        return type;
      }

      @Override
      public Object getNewObject() {
        return null;
      }
    }, SharedImages.JOINT, SharedImages.JOINT);

    return tool;

  }
}