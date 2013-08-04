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
package com.laex.cg2d.entityeditor.wizards;

import java.util.List;
import java.util.Queue;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.laex.cg2d.entityeditor.ui.ImportSpriteCompositeDelegate;
import com.laex.cg2d.entityeditor.ui.ImportSpritesComposite;
import com.laex.cg2d.model.model.EntitySpritesheetItem;
import com.laex.cg2d.model.model.ResourceFile;

/**
 * The Class NewEntityPage.
 */
public class NewEntitiesFromSpritesheetPage extends WizardPage implements ImportSpriteCompositeDelegate {

  /** The path to project. */
  private IPath pathToProject;
  
  /** The form toolkit. */
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  
  /** The spritesheet items. */
  private List<EntitySpritesheetItem> spritesheetItems;
  
  /** The extracted images. */
  private Queue<Image> extractedImages;
  
  /** The spritesheet file. */
  private ResourceFile spritesheetFile;
  
  /** The spritesheet json file. */
  private ResourceFile spritesheetJsonFile;
  
  /** The resource container. */
  private IResource resourceContainer;

  /**
   * Create the wizard.
   * 
   * @wbp.parser.constructor
   */
  public NewEntitiesFromSpritesheetPage() {
    super("wizardPage");
    setTitle("New Entities from Spritesheet");
    setPageComplete(false);
  }

  /**
   * Instantiates a new new entity page.
   *
   * @param resourceContainer the resource container
   */
  public NewEntitiesFromSpritesheetPage(IResource resourceContainer) {
    this();
    this.resourceContainer = resourceContainer;
  }

  /**
   * Create contents of the wizard.
   * 
   * @param parent
   *          the parent
   */
  public void createControl(Composite parent) {
    Composite container = new Composite(parent, SWT.NULL);

    setControl(container);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));

    ImportSpritesComposite importSpritesComposite = new ImportSpritesComposite(resourceContainer, container, SWT.NONE);
    importSpritesComposite.setDelegate(this);

    formToolkit.adapt(importSpritesComposite);
    formToolkit.paintBordersFor(importSpritesComposite);
    
    getShell().setMaximized(true);
  }

  /**
   * Open container resource dialog.
   */
  protected void openContainerResourceDialog() {
    ContainerSelectionDialog csd = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(),
        false, "Select a project");
    int response = csd.open();
    if (response == ContainerSelectionDialog.CANCEL) {
      return;
    }
    pathToProject = (IPath) csd.getResult()[0];
  }

  /**
   * Gets the path to project.
   * 
   * @return the path to project
   */
  public IPath getPathToProject() {
    return pathToProject;
  }


  /**
   * Gets the spritesheet items.
   *
   * @return the spritesheet items
   */
  public List<EntitySpritesheetItem> getSpritesheetItems() {
    return spritesheetItems;
  }

  /**
   * Gets the extracted images.
   *
   * @return the extracted images
   */
  public Queue<Image> getExtractedImages() {
    return extractedImages;
  }
  
  /**
   * Gets the spritesheet file.
   *
   * @return the spritesheet file
   */
  public ResourceFile getSpritesheetFile() {
    return spritesheetFile;
  }
  
  /**
   * Gets the spritesheet json file.
   *
   * @return the spritesheet json file
   */
  public ResourceFile getSpritesheetJsonFile() {
    return spritesheetJsonFile;
  }


  /* (non-Javadoc)
   * @see com.laex.cg2d.entityeditor.ui.ImportSpriteCompositeDelegate#spriteExtractionComplete(com.laex.cg2d.model.model.ResourceFile, com.laex.cg2d.model.model.ResourceFile, java.util.List, java.util.Queue)
   */
  @Override
  public void spriteExtractionComplete(ResourceFile spritesheetFile, ResourceFile spritesheetJsonFile,
      List<EntitySpritesheetItem> spritesheetItems, Queue<Image> extractedImages) {
    
    setPageComplete(true);
    
    this.spritesheetItems = spritesheetItems;
    this.extractedImages = extractedImages;
    this.spritesheetFile = spritesheetFile;
    this.spritesheetJsonFile = spritesheetJsonFile;
    
  }

}
