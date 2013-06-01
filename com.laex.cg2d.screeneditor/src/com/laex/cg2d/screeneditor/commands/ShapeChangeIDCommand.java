package com.laex.cg2d.screeneditor.commands;

import org.eclipse.gef.commands.Command;

import com.laex.cg2d.model.model.Shape;

public class ShapeChangeIDCommand extends Command {
  
  private Shape shape;
  private String newId;
  private String oldId;

  public ShapeChangeIDCommand(Shape shape, String newId) {
    this.shape = shape;
    this.newId = newId;
    this.oldId = shape.getId();
    setLabel("Change ID");
  }
  
  @Override
  public void execute() {
    redo();
  }
  
  @Override
  public void redo() {
    shape.setId(newId);
  }
  
  @Override
  public void undo() {
    shape.setId(oldId);
  }

}
