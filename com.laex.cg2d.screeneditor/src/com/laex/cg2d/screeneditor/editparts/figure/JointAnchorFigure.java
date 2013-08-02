package com.laex.cg2d.screeneditor.editparts.figure;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.resources.ResourceManager;
import com.laex.cg2d.screeneditor.Activator;

public class JointAnchorFigure extends ImageFigure {

  private static final Image ANCHOR_IMG = ResourceManager.getPluginImage(Activator.PLUGIN_ID,
      "icons/plus-small-white.png");

  public JointAnchorFigure() {
    super(ANCHOR_IMG);
  }

}
