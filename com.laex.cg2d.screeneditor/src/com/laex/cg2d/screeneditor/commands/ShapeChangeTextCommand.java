package com.laex.cg2d.screeneditor.commands;

import org.eclipse.gef.commands.Command;

import com.laex.cg2d.model.model.Shape;

public class ShapeChangeTextCommand extends Command {

  private Shape shape;
  private String newTxt;
  private String oldTxt;

  public ShapeChangeTextCommand(Shape shape, String newText) {
    this.shape = shape;
    this.newTxt = newText;
    this.oldTxt = shape.getText();
  }

  @Override
  public void execute() {
    redo();
  }

  @Override
  public void redo() {
    this.shape.setText(newTxt);
  }

  @Override
  public void undo() {
    this.shape.setText(oldTxt);
  }

}
