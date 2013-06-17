package com.laex.cg2d.model.model;

import com.laex.cg2d.model.model.impl.IDCreationStrategyImpl;

public class IDCreationStrategyFactory {
  
  public static IDCreationStrategy getIDCreator(GameModel model) {
    //We always return new instances. not singleton.
      return new IDCreationStrategyImpl(model);
  }

}
