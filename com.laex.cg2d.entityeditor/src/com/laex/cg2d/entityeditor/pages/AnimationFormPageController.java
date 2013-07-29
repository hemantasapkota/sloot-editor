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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.laex.cg2d.entityeditor.Activator;
import com.laex.cg2d.entityeditor.preferences.PreferenceConstants;
import com.laex.cg2d.model.model.EntityAnimation;
import com.laex.cg2d.model.model.EntityCollisionType;
import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.ResourceFile;
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
    alvi.setAnimation(new EntityAnimation());
    alvi.getAnimation().setAnimationName(alvi.getName());

    alvi.getAnimation().setShapeType(EntityCollisionType.NONE);

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
  public void previewAnimationExternal(final EntityAnimation entAnim, final String thisEntityFile) {

    Job job = new Job("Preview Animation") {

      @Override
      protected IStatus run(IProgressMonitor monitor) {
        /*
         * Structure of animation preview arguments "Animation1" -- animation
         * "entity-filename"
         */

        String pathToPreviewer = Activator.getDefault().getPreferenceStore()
            .getString(PreferenceConstants.ANIMATION_PREVIEW);

        String[] commands =
          { "java", "-jar", pathToPreviewer, entAnim.getAnimationName(), thisEntityFile };

        StringBuilder printCmd = new StringBuilder("java -jar ").append(pathToPreviewer).append(" ")
            .append(entAnim.getAnimationName()).append(" ").append(thisEntityFile);

        Activator.getDefault().getLog().log(new Status(Status.OK, Activator.PLUGIN_ID, printCmd.toString()));

        ProcessBuilder pb = new ProcessBuilder(commands);
        try {

          Process p = pb.start();

          Scanner scnr = new Scanner(p.getErrorStream());
          while (scnr.hasNext()) {
            Activator.getDefault().getLog().log(new Status(Status.INFO, Activator.PLUGIN_ID, scnr.nextLine()));
          }

        } catch (IOException e) {
          e.printStackTrace();
        }

        return Status.OK_STATUS;
      }
    };

    job.setUser(true);
    job.schedule();

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
    anim.setAnimationDuration(duration);
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
      alvi.getAnimation().setAnimationDuration(FloatUtil.toFloat(animDuration));
    }

  }

  public void spritesheetImageFileChanged(AnimationListViewItem alvi, ResourceFile spritesheetFile) {
    alvi.getAnimation().setSpritesheetFile(spritesheetFile);
  }

  public void spritesheetItemsChanged(AnimationListViewItem alvi, ResourceFile spritesheetMapperFile,
      List<EntitySpritesheetItem> esiList) {
    alvi.getAnimation().setSpritesheetMapperFile(spritesheetMapperFile);
    alvi.getAnimation().setSpritesheetItems(esiList);
  }

  public void removeSpritesheetItem(AnimationListViewItem alvi, EntitySpritesheetItem esi) {
    alvi.getAnimation().getSpritesheetItems().remove(esi);
  }

  /**
   * Export frames.
   * 
   * @param alvi
   *          the alvi
   * @param destination
   *          the destination
   * @param monitor
   *          the monitor
   */
  public void exportFrames(List<Image> images, IPath destination, IProgressMonitor monitor) {
    int work = images.size();
    int done = 0;
    monitor.beginTask("Export Frames", work);
    for (Image i : images) {
      ImageLoader loader = new ImageLoader();
      loader.data = new ImageData[]
        { i.getImageData() };
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
