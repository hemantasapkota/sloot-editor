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
package com.laex.cg2d.entityeditor;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.entityeditor.pages.AnimationFormPage;
import com.laex.cg2d.entityeditor.pages.CollisionFormPage;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;

/**
 * The Class EntityFormEditor.
 */
public class EntityFormEditor extends FormEditor {

  /** The Constant ID. */
  public static final String ID = "com.laex.cg3d.entityeditor.EntityEditor"; //$NON-NLS-1$

  /** The entity model. */
  private Entity entityModel;

  /** The animation form page. */
  AnimationFormPage animationFormPage = new AnimationFormPage(this, "animPage", "Animation");

  /** The collision form page. */
  CollisionFormPage collisionFormPage = new CollisionFormPage(this, "collisionPage", "Collision");

  /**
   * Instantiates a new entity form editor.
   */
  public EntityFormEditor() {
  }

  /**
   * Gets the model.
   * 
   * @return the model
   */
  public Entity getModel() {
    return entityModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
   */
  @Override
  protected void addPages() {
    try {
      addPage(animationFormPage);
      addPage(collisionFormPage);
    } catch (PartInitException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
   */
  @Override
  protected void setInput(IEditorInput input) {
    super.setInput(input);

    // entityModel = new Entity();
    // entityModel.setInternalName(EntitiesUtil.getInternalName(input.getName()));

    // parseWorkspaceFile(input);
    try {
      parseEntityFile(input);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }

    setPartName(input.getName());
  }

  /**
   * Parses the entity file.
   * 
   * @param input
   *          the input
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws CoreException
   *           the core exception
   */
  private void parseEntityFile(IEditorInput input) throws IOException, CoreException {
    IFile file = ((IFileEditorInput) input).getFile();
    CGEntity cgEntityModel = CGEntity.parseFrom(file.getContents());
    entityModel = EntityAdapter.asEntity(cgEntityModel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
   */
  @Override
  public boolean isSaveAsAllowed() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor
   * )
   */
  @Override
  public void doSave(IProgressMonitor monitor) {
    try {
      doSaveProtoBase(monitor);

      animationFormPage.setDirty(false);
      collisionFormPage.setDirty(false);
      this.editorDirtyStateChanged();
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Do save proto base.
   * 
   * @param monitor
   *          the monitor
   * @throws CoreException
   *           the core exception
   */
  private void doSaveProtoBase(IProgressMonitor monitor) throws CoreException {
    IFile file = ((FileEditorInput) getEditorInput()).getFile();
    CGEntity e = EntityAdapter.asCGEntity(entityModel);
    ByteArrayInputStream bais = new ByteArrayInputStream(e.toByteArray());
    file.setContents(bais, true, false, monitor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.EditorPart#doSaveAs()
   */
  @Override
  public void doSaveAs() {
  }

}
