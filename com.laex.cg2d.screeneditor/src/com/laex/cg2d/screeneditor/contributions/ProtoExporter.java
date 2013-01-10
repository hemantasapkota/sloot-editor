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
package com.laex.cg2d.screeneditor.contributions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.laex.cg2d.shared.model.GameModel;
import com.laex.cg2d.shared.util.PlatformUtil;

/**
 * The Class ProtoExporter.
 */
class ProtoExporter {

  /**
   * Export.
   *
   * @param destinationPath the destination path
   * @param model the model
   * @param monitor the monitor
   * @return the input stream
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static InputStream export(String destinationPath, GameModel model, IProgressMonitor monitor)
      throws IOException {
    // Toggle state as per the preferences
    // ProtoPreferences prf = model.getProtoPrefs();
    // ProtoPreferences prf = null;
    // IPath destPath = new Path(destinationPath);

    // Level Settings
    // CardSettings cs = model.getCardSettings();

    // PCardSettings pCardSettings =
    // PGameObjectProto.PGameObject.PCardSettings.newBuilder().setCardHeight(0)
    // .setCardWidth(0).setCardNoX(0).setCardNoY(0).build();

    // Box2D Settings
    // Box2dSettings b2d = model.getBox2dSettings();

    // PBox2dSettings pBox2dSettings =
    // PGameObjectProto.PGameObject.PBox2dSettings.newBuilder().setAabbBit(false)
    // .setJointBit(false).setShapeBit(false).setPositionIterations(10)
    // .setVelocityIterations(10).setDt(60)
    // .setGravity(PVec2.newBuilder().setX(0).setY(9).build()).build();

    // com.laex.cg2d.proto.PGameObjectProto.PGameObject.Builder
    // gameObjectBuilder = PGameObjectProto.PGameObject
    // .newBuilder().setBox2DSettings(pBox2dSettings).setCardSettings(pCardSettings);

    // Layer and Shapes
    // for (Layer layer : model.getDiagram().getLayers()) {
    // Builder layerBuilder =
    // PGameObjectProto.PGameObject.PLayer.newBuilder().setId(layer.getId())
    // .setLocked(layer.isLocked()).setVisible(layer.isVisible()).setName(layer.getName());

    // PLayer pLayer = layerBuilder.build();
    // make sure we preserve the index
    // gameObjectBuilder.addLayers(pLayer.getId() - 1, pLayer);

    // for (Shape s : layer.getChildren()) {
    // BodyDef bdef = s.getBodyDef();
    // PBodyDef pBodyDef =
    // PBodyDef.newBuilder().setActive(bdef.active).setAllowSleep(bdef.allowSleep)
    // .setAngle(bdef.angle).setAngularDamping(bdef.angularDamping).setAngularVelocity(bdef.angularVelocity)
    // .setAwake(bdef.awake).setBullet(bdef.bullet).setFixedRotation(bdef.fixedRotation)
    // .setLinearDampitng(bdef.linearDamping)
    // .setLinearVelocity(PVec2.newBuilder().setX(bdef.linearVelocity.x).setY(bdef.linearVelocity.y).build())
    // .setType(toPBodyType(bdef.type)).build();

    // FixtureDef fdef = s.getFixtureDef();
    // PFixtureDef pFixtureDef = PFixtureDef
    // .newBuilder()
    // .setDensity(fdef.density)
    // .setFriction(fdef.friction)
    // .setRestitution(fdef.restitution)
    // .setSensor(fdef.isSensor)
    // .setFilter(
    // PFilter.newBuilder().setCategoryBits(fdef.filter.categoryBits).setGroupIndex(fdef.filter.groupIndex)
    // .setMaskBits(fdef.filter.maskBits)).build();

    // PBounds pBounds = PBounds.newBuilder().setX(0).setY(0)
    // .setWidth(0).setHeight(0).build();

    // String dstGlTexFile = toRelativeFilepath(s.getGlTextureFilename());

    // com.laex.cg2d.proto.PGameObjectProto.PGameObject.PShape.Builder
    // shapeBuilder = PShape.newBuilder()
    // .setId(s.getShapeId()).setVisible(s.isVisible()).setLocked(s.isLocked())
    // .setBackgroundEntity(s.isBackground()).setBounds(pBounds).setBodyDef(pBodyDef).setFixtureDef(pFixtureDef)
    // .setParentLayer(pLayer);

    // Also export images
    // exportImages(s.getGlTextureFilename(),
    // destPath.append(dstGlTexFile).toOSString(), monitor);

    // layerBuilder.addShape(shapeBuilder.build());
    // }
    // }

    // PGameObject gameObject = gameObjectBuilder.build();
    // return new ByteArrayInputStream(gameObject.toByteArray());
    return null;
  }

//  private static PBodyType toPBodyType(BodyType bt) {
//    switch (bt) {
//    case StaticBody:
//      return PBodyType.STATIC;
//    case DynamicBody:
//      return PBodyType.DYNAMIC;
//    case KinematicBody:
//      return PBodyType.KINEMATIC;
//    }
//    return null;
//  }

  /**
 * To relative filepath.
 *
 * @param workspaceFilePath the workspace file path
 * @return the string
 */
private static String toRelativeFilepath(String workspaceFilePath) {
    if (workspaceFilePath == null || StringUtils.isEmpty(workspaceFilePath)) {
      return PlatformUtil.STRING_EMPTY;
    }
    IPath path = new Path(workspaceFilePath);
    path = path.removeFirstSegments(path.segmentCount() - 1);
    String pp = path.toOSString();
    return pp;
  }

  /**
   * Export images.
   *
   * @param sourcePath the source path
   * @param destPath the dest path
   * @param monitor the monitor
   */
  private static void exportImages(String sourcePath, String destPath, IProgressMonitor monitor) {
    IPath srcPath = new Path(sourcePath);
    IPath dstPath = new Path(destPath);

    if (StringUtils.isEmpty(sourcePath)) {
      return;
    }

    IFile dstFile = ResourcesPlugin.getWorkspace().getRoot().getFile(dstPath);
    if (dstFile.exists()) {
      // no need to copy
      return;
    }

    IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(srcPath);
    if (file.exists())
      try {
        file.copy(dstPath, true, monitor);
      } catch (CoreException e) {
        e.printStackTrace();
      }
  }
}
