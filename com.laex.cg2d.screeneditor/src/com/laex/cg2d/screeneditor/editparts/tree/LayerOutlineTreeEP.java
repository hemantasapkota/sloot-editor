package com.laex.cg2d.screeneditor.editparts.tree;

import java.util.ArrayList;
import java.util.List;

import com.laex.cg2d.model.model.Layer;

public class LayerOutlineTreeEP extends LayerTreeEP {

  public LayerOutlineTreeEP(Layer layer) {
    super(layer);
  }
  
  @Override
  protected List getModelChildren() {
    //return empty list
    return new ArrayList();
  }

}
