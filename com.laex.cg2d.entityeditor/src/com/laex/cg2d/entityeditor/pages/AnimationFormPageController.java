package com.laex.cg2d.entityeditor.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.shared.SharedImages;
import com.laex.cg2d.shared.model.EntityAnimation;
import com.laex.cg2d.shared.model.EntityCollisionType;
import com.laex.cg2d.shared.util.FloatUtil;

// Seperates logic from UI code of AnimationFormPage
public class AnimationFormPageController {

  private List<AnimationListViewItem> animations = new ArrayList<AnimationListViewItem>();

  private String provideNewName() {
    return "Animation " + (animations.size() + 1);
  }

  public AnimationListViewItem addAnimation(EntityAnimation ea) {
    AnimationListViewItem alvi = new AnimationListViewItem();
    alvi.setName(ea.getAnimationName());
    alvi.setFirstFrame(SharedImages.BOX.createImage());
    alvi.setFrames(new LinkedList<Image>());
    alvi.setAnimation(ea);

    animations.add(alvi);
    
    return alvi;
  }

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

    alvi.getAnimation().setShapeType(EntityCollisionType.NONE); // NONE indicates no collision parameters
    
    if (animations.size() == 0) {
      alvi.getAnimation().setDefaultAnimation(true);
    }

    animations.add(alvi);

    return alvi;
  }

  public int removeAnimation(AnimationListViewItem alvi) {
    alvi.getFrames().clear();
    int index = animations.indexOf(alvi);
    animations.remove(index);
    return index;
  }

  public void previewAnimationExternal(EntityAnimation entAnim, float duration) {
    String animStrip = entAnim.getAnimationResourceFile().getResourceFileAbsolute();

    if (animStrip == null)
      return;

    int rows = entAnim.getRows();
    int cols = entAnim.getCols();

    // Use JOGL Application
//    JoglApplicationConfiguration jac = new JoglApplicationConfiguration();
//    jac.width = 200;
//    jac.height = 200;
//    jac.title = entAnim.getAnimationName();
//
//    ExternalAnimationPreview eap = new ExternalAnimationPreview(animStrip, rows, cols, duration);
//    new JoglApplication(eap, jac);
    
//    LwjglApplicationConfiguration lac = new LwjglApplicationConfiguration();
//    lac.width = 200;
//    lac.height = 200;
//    lac.title = entAnim.getAnimationName();
//    
//    
//    ExternalAnimationPreview eap = new ExternalAnimationPreview(animStrip, rows, cols, duration);
//    new LwjglApplication(eap, lac);
  }

  public void animationDurationChanged(EntityAnimation anim, float duration) {
    anim.setAnimationDelay(duration);
  }

  public void animationNameChange(AnimationListViewItem alvi, String newName, String animDuration) {

    alvi.setName(newName);

    if (alvi.getAnimation() != null) {
      alvi.getAnimation().setAnimationName(newName);
      alvi.getAnimation().setAnimationDelay(FloatUtil.toFloat(animDuration));
    }

  }

  /**
   * Animation default changed.
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

  public List<AnimationListViewItem> getAnimations() {
    return animations;
  }

  public int animationsCount() {
    return animations.size();
  }

  public int indexOf(AnimationListViewItem alvi) {
    return animations.indexOf(alvi);
  }

}
