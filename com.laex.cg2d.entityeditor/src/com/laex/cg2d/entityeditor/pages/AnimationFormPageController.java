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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.laex.cg2d.model.ResourceManager;
import com.laex.cg2d.model.SharedImages;
import com.laex.cg2d.model.model.EntityAnimation;
import com.laex.cg2d.model.model.EntityCollisionType;
import com.laex.cg2d.model.util.FloatUtil;

// Seperates logic from UI code of AnimationFormPage
/**
 * The Class AnimationFormPageController.
 */
public class AnimationFormPageController {

  /** The animations. */
  private List<AnimationListViewItem> animations = new ArrayList<AnimationListViewItem>();

  /**
   * Provide new name.
   * 
   * @return the string
   */
  private String provideNewName() {
    return "Animation " + (animations.size() + 1);
  }

  /**
   * Adds the animation.
   * 
   * @param ea
   *          the ea
   * @return the animation list view item
   */
  public AnimationListViewItem addAnimation(EntityAnimation ea) {
    AnimationListViewItem alvi = new AnimationListViewItem();
    alvi.setName(ea.getAnimationName());
    alvi.setFirstFrame(SharedImages.BOX.createImage());
    alvi.setFrames(new LinkedList<Image>());
    alvi.setAnimation(ea);

    animations.add(alvi);

    return alvi;
  }

  /**
   * Creates the empty animation.
   * 
   * @return the animation list view item
   */
  public AnimationListViewItem createEmptyAnimation() {

    AnimationListViewItem alvi = new AnimationListViewItem();
    alvi.setName(provideNewName());
    alvi.setFirstFrame(SharedImages.BOX.createImage());
    alvi.setFrames(new LinkedList<Image>());
    alvi.setAnimation(new EntityAnimation());
    alvi.getAnimation().setAnimationName(alvi.getName());

    // alvi.getAnimation().setVertices(new ArrayList<Vector2>()); // set
    // empty
    // vertices

    alvi.getAnimation().setShapeType(EntityCollisionType.NONE); // NONE
                                                                // indicates no
                                                                // collision
                                                                // parameters

    if (animations.size() == 0) {
      alvi.getAnimation().setDefaultAnimation(true);
    }

    animations.add(alvi);

    return alvi;
  }

  /**
   * Removes the animation.
   * 
   * @param alvi
   *          the alvi
   * @return the int
   */
  public int removeAnimation(AnimationListViewItem alvi) {
    alvi.getFrames().clear();
    int index = animations.indexOf(alvi);
    animations.remove(index);
    return index;
  }

  /**
   * Preview animation external.
   * 
   * @param entAnim
   *          the ent anim
   * @param duration
   *          the duration
   */
  public void previewAnimationExternal(EntityAnimation entAnim, float duration) {
    String animStrip = entAnim.getAnimationResourceFile().getResourceFileAbsolute();

    if (animStrip == null)
      return;

    int rows = entAnim.getRows();
    int cols = entAnim.getCols();

    // Use JOGL Application
    // JoglApplicationConfiguration jac = new JoglApplicationConfiguration();
    // jac.width = 200;
    // jac.height = 200;
    // jac.title = entAnim.getAnimationName();
    //
    // ExternalAnimationPreview eap = new ExternalAnimationPreview(animStrip,
    // rows, cols, duration);
    // new JoglApplication(eap, jac);

    // LwjglApplicationConfiguration lac = new LwjglApplicationConfiguration();
    // lac.width = 200;
    // lac.height = 200;
    // lac.title = entAnim.getAnimationName();
    //
    //
    // ExternalAnimationPreview eap = new ExternalAnimationPreview(animStrip,
    // rows, cols, duration);
    // new LwjglApplication(eap, lac);
  }

  /**
   * Animation duration changed.
   * 
   * @param anim
   *          the anim
   * @param duration
   *          the duration
   */
  public void animationDurationChanged(EntityAnimation anim, float duration) {
    anim.setAnimationDelay(duration);
  }

  /**
   * Animation name change.
   * 
   * @param alvi
   *          the alvi
   * @param newName
   *          the new name
   * @param animDuration
   *          the anim duration
   */
  public void animationNameChange(AnimationListViewItem alvi, String newName, String animDuration) {

    alvi.setName(newName);

    if (alvi.getAnimation() != null) {
      alvi.getAnimation().setAnimationName(newName);
      alvi.getAnimation().setAnimationDelay(FloatUtil.toFloat(animDuration));
    }

  }

  /**
   * Export frames.
   *
   * @param alvi the alvi
   * @param destination the destination
   * @param monitor the monitor
   */
  public void exportFrames(AnimationListViewItem alvi, IPath destination, IProgressMonitor monitor) {
      int work = alvi.getFrames().size();
      int done = 0;
      monitor.beginTask("Export Frames", work);
      for (Image i : alvi.getFrames()) {
           ImageLoader loader = new ImageLoader();
           loader.data = new ImageData[] { i.getImageData() };
           int imgIndex = done + 1;
           String filename = destination.append("img" + imgIndex).addFileExtension("png").toOSString();
           loader.save(filename, SWT.IMAGE_PNG);
           monitor.worked(done++);
      }
      monitor.done();
  }

  /**
   * Animation default changed.
   * 
   * @param alvi
   *          the alvi
   * @param isDefaultAnimation
   *          the is default animation
   */
  public void defaultAnimationChanged(AnimationListViewItem alvi, boolean isDefaultAnimation) {
    alvi.getAnimation().setDefaultAnimation(isDefaultAnimation);

    // Undefault if any other items exist
    for (AnimationListViewItem ai : animations) {
      if (ai != alvi) {
        ai.getAnimation().setDefaultAnimation(false);
      }
    }

  }

  /**
   * Gets the animations.
   * 
   * @return the animations
   */
  public List<AnimationListViewItem> getAnimations() {
    return animations;
  }

  /**
   * Animations count.
   * 
   * @return the int
   */
  public int animationsCount() {
    return animations.size();
  }

  /**
   * Index of.
   * 
   * @param alvi
   *          the alvi
   * @return the int
   */
  public int indexOf(AnimationListViewItem alvi) {
    return animations.indexOf(alvi);
  }

}
