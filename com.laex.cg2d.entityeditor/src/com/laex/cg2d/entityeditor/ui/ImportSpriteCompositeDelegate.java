package com.laex.cg2d.entityeditor.ui;

import java.util.List;
import java.util.Queue;

import org.eclipse.swt.graphics.Image;

import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.ResourceFile;

public interface ImportSpriteCompositeDelegate {

  void spriteExtractionComplete(ResourceFile spritesheetFile, ResourceFile spritesheetJsonFile,
      List<EntitySpritesheetItem> spritesheetItems, Queue<Image> extractedImages);

}
