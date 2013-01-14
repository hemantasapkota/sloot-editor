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
package com.laex.cg2d.entityeditor.pages;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class AnimationTableContentProvider.
 */
class AnimationTableContentProvider implements IStructuredContentProvider {

  /** The list items. */
  private List listItems;

  /**
   * Instantiates a new animation table content provider.
   * 
   * @param items
   *          the items
   */
  public AnimationTableContentProvider(List items) {
    this.listItems = items;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang
   * .Object)
   */
  public Object[] getElements(Object inputElement) {
    return listItems.toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.viewers.IContentProvider#dispose()
   */
  public void dispose() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
   * .viewers.Viewer, java.lang.Object, java.lang.Object)
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
