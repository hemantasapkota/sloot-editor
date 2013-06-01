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
package com.laex.cg2d.render.impl;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.render.AbstractScreenScaffold;

/**
 * The Class BackgroundManager.
 */
public class BackgroundManager extends AbstractScreenScaffold {

  /** The batch. */
  private SpriteBatch batch;

  /** The background textures. */
  private Queue<Sprite> backgroundTextures;

  /**
   * Instantiates a new background manager.
   * 
   * @param model
   *          the model
   * @param world
   *          the world
   * @param cam
   *          the cam
   * @param batch
   *          the batch
   */
  public BackgroundManager(CGScreenModel model, World world, Camera cam, SpriteBatch batch) {
    super(model, world, cam);
    this.batch = batch;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#create()
   */
  @Override
  public void create() {
    backgroundTextures = new LinkedList<Sprite>();
    for (CGLayer layer : model().getLayersList()) {
      for (CGShape shape : layer.getShapeList()) {

        if (!(shape.getEditorShapeType() == CGEditorShapeType.BACKGROUND_SHAPE)) {
          continue;
        }

        FileHandle handle = Gdx.files.absolute(shape.getBackgroundResourceFile().getResourceFileAbsolute());

        Texture tex = new Texture(handle);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Sprite sprite = new Sprite(tex);

        float x = shape.getBounds().getX();
        float y = shape.getBounds().getY();
        float width = shape.getBounds().getWidth();
        float height = shape.getBounds().getHeight();

        Vector2 scrPos = new Vector2(x, y);
        Vector2 worldPos = screenToWorldFlipped(scrPos, height);
        sprite.setPosition(worldPos.x, worldPos.y);

        // Note: we cast width to float to make the division.
        // If we dont do that, then decimals from division would
        // be ignored. This will result in the size of sprite
        // being slightly off.
        float w = (width / ptmRatio());
        float h = (height / ptmRatio());

        sprite.setSize(w, h);

        backgroundTextures.add(sprite);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#render()
   */
  @Override
  public void render() {
    batch.begin();
    for (Sprite spr : backgroundTextures) {
      spr.draw(batch);
    }
    batch.end();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.render.IGameComponentManager#dispose()
   */
  @Override
  public void dispose() {
    for (Sprite spr : backgroundTextures) {
      spr.getTexture().dispose();
    }
  }

}
