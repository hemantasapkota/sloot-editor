/*
 * Copyright (c) 2012, 2013 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package com.laex.cg2d.screeneditor;

import org.eclipse.core.expressions.PropertyTester;

/**
 * The Class ScreenEditorPropertyTester.
 */
public class ScreenEditorPropertyTester extends PropertyTester {

  /**
   * Instantiates a new screen editor property tester.
   */
  public ScreenEditorPropertyTester() {
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
   */
  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    System.err.println("Got some message from property tester");
    return false;
  }

}
