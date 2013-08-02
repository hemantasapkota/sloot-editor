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
package com.laex.cg2d.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * The listener interface for receiving entityResourceChange events. The class
 * that is interested in processing a entityResourceChange event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addEntityResourceChangeListener<code> method. When
 * the entityResourceChange event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see EntityResourceChangeEvent
 */
public class EntityResourceChangeListener implements IResourceChangeListener {

  /**
   * The listener interface for receiving entityChange events. The class that is
   * interested in processing a entityChange event implements this interface,
   * and the object created with that class is registered with a component using
   * the component's <code>addEntityChangeListener<code> method. When
   * the entityChange event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see EntityChangeEvent
   */
  public interface EntityChangeListener {

    /**
     * Entity changed.
     * 
     * @param resource
     *          the resource
     */
    void entityChanged(IResource resource);

    /**
     * Entity removed.
     * 
     * @param resource
     *          the resource
     */
    void entityRemoved(IResource resource);
  }

  /**
   * The Class EntityVisitor.
   */
  class EntityVisitor implements IResourceDeltaVisitor {

    /**
     * Checks if is resource valid.
     * 
     * @param res
     *          the res
     * @return true, if is resource valid
     */
    private boolean isResourceValid(IResource res) {
      if (res == null)
        return false;

      String ext = res.getFileExtension();
      if (ext != null && ext.equals(ICGCProject.ENTITIES_EXTENSION)) {
        return true;

      }

      return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core
     * .resources.IResourceDelta)
     */
    @Override
    public boolean visit(IResourceDelta delta) throws CoreException {
      IResource res = delta.getResource();

      if (isResourceValid(res)) {

        switch (delta.getKind()) {

        case IResourceDelta.REMOVED:
          chgList.entityRemoved(res);
          break;

        case IResourceDelta.CHANGED:
          chgList.entityChanged(res);
          break;
        }

      }

      return true;
    }
  }

  /** The chg list. */
  private EntityChangeListener chgList;

  /**
   * Instantiates a new entity resource change listener.
   */
  public EntityResourceChangeListener() {
  }

  /**
   * Adds the entity change listener.
   * 
   * @param chgList
   *          the chg list
   */
  public void addEntityChangeListener(EntityChangeListener chgList) {
    this.chgList = chgList;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.
   * eclipse.core.resources.IResourceChangeEvent)
   */
  @Override
  public void resourceChanged(IResourceChangeEvent event) {
    switch (event.getType()) {
    case IResourceChangeEvent.PRE_DELETE:
      try {
        event.getDelta().accept(new EntityVisitor());
      } catch (CoreException e1) {
        e1.printStackTrace();
      }
      break;

    case IResourceChangeEvent.PRE_CLOSE:
      break;

    case IResourceChangeEvent.POST_CHANGE:
      try {
        event.getDelta().accept(new EntityVisitor());
      } catch (CoreException e) {
        e.printStackTrace();
      }
      break;

    }
  }

}
