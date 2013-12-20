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
package com.laex.cg2d.entityeditor.pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.google.common.base.Optional;
import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.model.ScreenModel.CGBounds;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGEntitySpritesheetItem;

/**
 * The Class ExternalAnimationPreview.
 */
public class ExternalAnimationPreview extends ApplicationAdapter {

  /** The state time. */
  private float stateTime;

  /** The spr. */
  private Sprite spr;

  /** The batch. */
  private SpriteBatch batch;

  /** The cam. */
  private OrthographicCamera cam;

  /** The sprite animation. */
  private Optional<Animation> spriteAnimation = Optional.absent();

  /** The shape renderer. */
  private ShapeRenderer shapeRenderer;

  /** The h. */
  float w, h;

  /** The animation name. */
  private String animationName;

  /** The cge file. */
  private String cgeFile;

  /**
   * Instantiates a new external animation preview.
   * 
   * @param animationName
   *          the animation name
   * @param cgeFile
   *          the cge file
   */
  public ExternalAnimationPreview(String animationName, String cgeFile) {
    this.animationName = animationName;
    this.cgeFile = cgeFile;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.ApplicationAdapter#create()
   */
  @Override
  public void create() {
    super.create();

    this.shapeRenderer = new ShapeRenderer();

    Texture.setEnforcePotImages(false);

    batch = new SpriteBatch();
    w = Gdx.graphics.getWidth();
    h = Gdx.graphics.getHeight();

    cam = new OrthographicCamera(w, h);
    cam.position.set(0, 0, 0);

    CGEntityAnimation anim = null;

    FileInputStream fis;
    try {
      fis = new FileInputStream(cgeFile);
      CGEntity entity = CGEntity.parseFrom(fis);
      for (CGEntityAnimation ea : entity.getAnimationsList()) {
        if (ea.getAnimationName().equals(animationName)) {
          anim = ea;
        }
      }
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }

    if (anim == null) {
      return;
    }

    FileHandle handle = Gdx.files.absolute(anim.getSpritesheetFile().getResourceFileAbsolute());
    if (!handle.exists()) {
      return;
    }

    Texture tex = new Texture(handle);
    tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    Array<TextureRegion> indexedFrames = new Array<TextureRegion>();
    for (CGEntitySpritesheetItem esi : anim.getSpritesheetItemsList()) {
      CGBounds b = esi.getExtractBounds();
      TextureRegion tr = new TextureRegion(tex, (int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
      indexedFrames.add(tr);
    }

    Animation sa = new Animation(anim.getAnimationDuration(), indexedFrames);
    spriteAnimation = Optional.fromNullable(sa);

    spr = new Sprite(sa.getKeyFrame(stateTime, true));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.ApplicationAdapter#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();

    if (spr != null)
      spr.getTexture().dispose();

    batch.dispose();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.ApplicationAdapter#render()
   */
  @Override
  public void render() {
    GL10 gl = Gdx.graphics.getGL10();
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    stateTime += Gdx.graphics.getDeltaTime();
    batch.setProjectionMatrix(cam.combined);

    if (!spriteAnimation.isPresent()) {
      return;
    }

    TextureRegion tr = spriteAnimation.get().getKeyFrame(stateTime, true);
    spr.setRegion(tr);

    batch.begin();
    spr.draw(batch);
    batch.end();

    shapeRenderer.begin(ShapeType.Line);
    shapeRenderer.setColor(Color.ORANGE);
    shapeRenderer.line(0, h / 2, w, h / 2);
    shapeRenderer.line(w / 2, 0, w / 2, h);
    shapeRenderer.end();
  }

  /**
   * The main method.
   * 
   * @param args
   *          the arguments
   */
  public static void main(String[] args) {

    String animationName = args[0];
    String spriteSheetFile = args[1];

    LwjglApplicationConfiguration lac = new LwjglApplicationConfiguration();
    lac.width = 200;
    lac.height = 200;
    lac.title = animationName;

    try {
      ExternalAnimationPreview eap = new ExternalAnimationPreview(animationName, spriteSheetFile);
      new LwjglApplication(eap, lac);
    } catch (Exception ge) {
      System.exit(0);
    }

  }

}
