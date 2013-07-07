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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

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
import com.laex.cg2d.model.ResourceManager;

/**
 * The Class ExternalAnimationPreview.
 */
public class ExternalAnimationPreview extends ApplicationAdapter {

  /** The animation strip. */
  String animationStrip;

  /** The rows. */
  private int rows;

  /** The cols. */
  private int cols;

  /** The duration. */
  private float duration;

  /** The state time. */
  private float stateTime;

  /** The spr. */
  private Sprite spr;

  /** The batch. */
  private SpriteBatch batch;

  /** The cam. */
  private OrthographicCamera cam;

  /** The sprite animation. */
  private Animation spriteAnimation;

  /** The shape renderer. */
  private ShapeRenderer shapeRenderer;

  /** The h. */
  float w, h;

  private List<Integer> frameIndices;

  /**
   * Instantiates a new external animation preview.
   * 
   * @param animationStrip
   *          the animation strip
   * @param rows
   *          the rows
   * @param cols
   *          the cols
   * @param duration
   *          the duration
   */
  public ExternalAnimationPreview(String animationStrip, int rows, int cols, float duration, List<Integer> frameIndices) {
    this.animationStrip = animationStrip;
    this.rows = rows;
    this.cols = cols;
    this.duration = duration;
    this.frameIndices = frameIndices;
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

    FileHandle handle = Gdx.files.absolute(animationStrip);

    Texture tex = new Texture(handle);
    tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    if (cols / rows >= 1) {
      // create sprite sheet animation

      TextureRegion[][] tmp = TextureRegion.split(tex, tex.getWidth() / cols, tex.getHeight() / rows);
      TextureRegion[] walkFrames = new TextureRegion[cols * rows];
      int index = 0;
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          walkFrames[index++] = tmp[i][j];
        }
      }

      Array<TextureRegion> indexedFrames = new Array<TextureRegion>();
      for (int i : frameIndices) {
        indexedFrames.add(walkFrames[i - 1]);
      }

      spriteAnimation = new Animation(duration, indexedFrames);
    }

    spr = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.ApplicationAdapter#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();

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

    TextureRegion tr = spriteAnimation.getKeyFrame(stateTime, true);
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

  public static void main(String[] args) {

    String animationName = args[0];

    String spriteSheetFile = args[1];

    float duration = Float.parseFloat(args[2]);

    int cols = Integer.parseInt(args[3]);

    int rows = Integer.parseInt(args[4]);

    String[] tmpIndices = args[5].split(",");

    List<Integer> frameIndices = new ArrayList<Integer>();
    for (String index : tmpIndices) {
      frameIndices.add(Integer.parseInt(index));
    }
    
    LwjglApplicationConfiguration lac = new LwjglApplicationConfiguration();
    lac.width = 200;
    lac.height = 200;
    lac.title = animationName;

    ExternalAnimationPreview eap = new ExternalAnimationPreview(spriteSheetFile, rows, cols, duration, frameIndices);
    new LwjglApplication(eap, lac);

  }

}
