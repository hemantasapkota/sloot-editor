package com.laex.cg2d.model.adapter;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.laex.cg2d.model.ScreenModel.CGColor;

public class ColorAdapter {
  
  public static CGColor cgColor(RGB rgb) {
    return CGColor.newBuilder().setR(rgb.red).setG(rgb.green).setB(rgb.blue).build();
  }
  
  public static Color swtColor(CGColor color) {
    return new Color(null, new RGB(color.getR(), color.getG(), color.getB()));
  }
  
  public static Color swtColor(RGB rgb) {
    return new Color(null, rgb);
  }

}
