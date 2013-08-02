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

import org.eclipse.core.resources.IFile;

import com.laex.cg2d.model.model.Entity;

/**
 * The Class DNDFileTransfer.
 */
public final class DNDFileTransfer {

  /**
   * The Enum TransferType.
   */
  public enum TransferType {

    /** The none. */
    NONE,
    /** The texture. */
    TEXTURE,
    /** The entity. */
    ENTITY;
  }

  /** The transfer type. */
  public static TransferType transferType = TransferType.NONE;

  /** The file. */
  public static IFile file = null;
  
  /** The entity resource file. */
  public static IFile entityResourceFile = null;

  /** The entity. */
  public static Entity entity = null;

}
