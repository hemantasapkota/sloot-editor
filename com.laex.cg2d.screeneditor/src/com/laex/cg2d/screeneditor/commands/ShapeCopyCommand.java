package com.laex.cg2d.screeneditor.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.laex.cg2d.model.model.Shape;

public class ShapeCopyCommand extends Command {
  
  private List<Shape> shapes;

  public ShapeCopyCommand(List<Shape> shapes) {
    this.shapes = shapes;
  }
  
  @Override
  public boolean canExecute() {
    return super.canExecute();
  }
  
  @Override
  public boolean canUndo() {
    return false;
  }
  
  @Override
  public void execute() {
    if (canExecute()) {
      Clipboard.getDefault().setContents(this.shapes);
    }
  }
  

}
