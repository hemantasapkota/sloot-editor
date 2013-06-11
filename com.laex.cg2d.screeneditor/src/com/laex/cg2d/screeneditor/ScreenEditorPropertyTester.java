package com.laex.cg2d.screeneditor;

import org.eclipse.core.expressions.PropertyTester;

public class ScreenEditorPropertyTester extends PropertyTester {

  public ScreenEditorPropertyTester() {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    System.err.println("Got some message from property tester");
    return false;
  }

}
