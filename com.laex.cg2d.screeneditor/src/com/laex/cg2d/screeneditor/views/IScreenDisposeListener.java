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
package com.laex.cg2d.screeneditor.views;

/**
 * The listener interface for receiving IScreenDispose events. The class that is
 * interested in processing a IScreenDispose event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addIScreenDisposeListener<code> method. When
 * the IScreenDispose event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see IScreenDisposeEvent
 */
public interface IScreenDisposeListener {

  /**
   * Screen disposed.
   */
  void screenDisposed();

}
