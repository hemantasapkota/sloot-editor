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

package com.laex.cg2d.core.splashHandlers;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * The Class ExtensibleSplashHandler.
 * 
 * @since 3.3
 */
public class ExtensibleSplashHandler extends AbstractSplashHandler {

  /** The image list. */
  private ArrayList fImageList;

  /** The tooltip list. */
  private ArrayList fTooltipList;

  /** The Constant F_SPLASH_EXTENSION_ID. */
  private final static String F_SPLASH_EXTENSION_ID = "com.laex.cg2d.core.splashExtension"; // NON-NLS-1

  /** The Constant F_ELEMENT_ICON. */
  private final static String F_ELEMENT_ICON = "icon"; // NON-NLS-1

  /** The Constant F_ELEMENT_TOOLTIP. */
  private final static String F_ELEMENT_TOOLTIP = "tooltip"; // NON-NLS-1

  /** The Constant F_DEFAULT_TOOLTIP. */
  private final static String F_DEFAULT_TOOLTIP = "Image"; // NON-NLS-1

  /** The Constant F_IMAGE_WIDTH. */
  private final static int F_IMAGE_WIDTH = 50;

  /** The Constant F_IMAGE_HEIGHT. */
  private final static int F_IMAGE_HEIGHT = 50;

  /** The Constant F_SPLASH_SCREEN_BEVEL. */
  private final static int F_SPLASH_SCREEN_BEVEL = 5;

  /** The icon panel. */
  private Composite fIconPanel;

  /**
   * Instantiates a new extensible splash handler.
   */
  public ExtensibleSplashHandler() {
    fImageList = new ArrayList();
    fTooltipList = new ArrayList();
    fIconPanel = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets
   * .Shell)
   */
  public void init(Shell splash) {
    // Store the shell
    super.init(splash);
    // Configure the shell layout
    configureUISplash();
    // Load all splash extensions
    loadSplashExtensions();
    // If no splash extensions were loaded abort the splash handler
    if (hasSplashExtensions() == false) {
      return;
    }
    // Create UI
    createUI();
    // Configure the image panel bounds
    configureUICompositeIconPanelBounds();
    // Enter event loop and prevent the RCP application from
    // loading until all work is done
    doEventLoop();
  }

  /**
   * Checks for splash extensions.
   * 
   * @return true, if successful
   */
  private boolean hasSplashExtensions() {
    if (fImageList.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Creates the ui.
   */
  private void createUI() {
    // Create the icon panel
    createUICompositeIconPanel();
    // Create the images
    createUIImages();
  }

  /**
   * Creates the ui images.
   */
  private void createUIImages() {
    Iterator imageIterator = fImageList.iterator();
    Iterator tooltipIterator = fTooltipList.iterator();
    int i = 1;
    int columnCount = ((GridLayout) fIconPanel.getLayout()).numColumns;
    // Create all the images
    // Abort if we run out of columns (left-over images will not fit within
    // the usable splash screen width)
    while (imageIterator.hasNext() && (i <= columnCount)) {
      Image image = (Image) imageIterator.next();
      String tooltip = (String) tooltipIterator.next();
      // Create the image using a label widget
      createUILabel(image, tooltip);
      i++;
    }
  }

  /**
   * Creates the ui label.
   * 
   * @param image
   *          the image
   * @param tooltip
   *          the tooltip
   */
  private void createUILabel(Image image, String tooltip) {
    // Create the label (no text)
    Label label = new Label(fIconPanel, SWT.NONE);
    label.setImage(image);
    label.setToolTipText(tooltip);
  }

  /**
   * Creates the ui composite icon panel.
   */
  private void createUICompositeIconPanel() {
    Shell splash = getSplash();
    // Create the composite
    fIconPanel = new Composite(splash, SWT.NONE);
    // Determine the maximum number of columns that can fit on the splash
    // screen. One 50x50 image per column.
    int maxColumnCount = getUsableSplashScreenWidth() / F_IMAGE_WIDTH;
    // Limit size to the maximum number of columns if the number of images
    // exceed this amount; otherwise, use the exact number of columns
    // required.
    int actualColumnCount = Math.min(fImageList.size(), maxColumnCount);
    // Configure the layout
    GridLayout layout = new GridLayout(actualColumnCount, true);
    layout.horizontalSpacing = 0;
    layout.verticalSpacing = 0;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    fIconPanel.setLayout(layout);
  }

  /**
   * Configure ui composite icon panel bounds.
   */
  private void configureUICompositeIconPanelBounds() {
    // Determine the size of the panel and position it at the bottom-right
    // of the splash screen.
    Point panelSize = fIconPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

    int x_coord = getSplash().getSize().x - F_SPLASH_SCREEN_BEVEL - panelSize.x;
    int y_coord = getSplash().getSize().y - F_SPLASH_SCREEN_BEVEL - panelSize.y;
    int x_width = panelSize.x;
    int y_width = panelSize.y;

    fIconPanel.setBounds(x_coord, y_coord, x_width, y_width);
  }

  /**
   * Gets the usable splash screen width.
   * 
   * @return the usable splash screen width
   */
  private int getUsableSplashScreenWidth() {
    // Splash screen width minus two graphic border bevel widths
    return getSplash().getSize().x - (F_SPLASH_SCREEN_BEVEL * 2);
  }

  /**
   * Load splash extensions.
   */
  private void loadSplashExtensions() {
    // Get all splash handler extensions
    IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(F_SPLASH_EXTENSION_ID).getExtensions();
    // Process all splash handler extensions
    for (int i = 0; i < extensions.length; i++) {
      processSplashExtension(extensions[i]);
    }
  }

  /**
   * Process splash extension.
   * 
   * @param extension
   *          the extension
   */
  private void processSplashExtension(IExtension extension) {
    // Get all splash handler configuration elements
    IConfigurationElement[] elements = extension.getConfigurationElements();
    // Process all splash handler configuration elements
    for (int j = 0; j < elements.length; j++) {
      processSplashElements(elements[j]);
    }
  }

  /**
   * Process splash elements.
   * 
   * @param configurationElement
   *          the configuration element
   */
  private void processSplashElements(IConfigurationElement configurationElement) {
    // Attribute: icon
    processSplashElementIcon(configurationElement);
    // Attribute: tooltip
    processSplashElementTooltip(configurationElement);
  }

  /**
   * Process splash element tooltip.
   * 
   * @param configurationElement
   *          the configuration element
   */
  private void processSplashElementTooltip(IConfigurationElement configurationElement) {
    // Get attribute tooltip
    String tooltip = configurationElement.getAttribute(F_ELEMENT_TOOLTIP);
    // If a tooltip is not defined, give it a default
    if ((tooltip == null) || (tooltip.length() == 0)) {
      fTooltipList.add(F_DEFAULT_TOOLTIP);
    } else {
      fTooltipList.add(tooltip);
    }
  }

  /**
   * Process splash element icon.
   * 
   * @param configurationElement
   *          the configuration element
   */
  private void processSplashElementIcon(IConfigurationElement configurationElement) {
    // Get attribute icon
    String iconImageFilePath = configurationElement.getAttribute(F_ELEMENT_ICON);
    // Abort if an icon attribute was not specified
    if ((iconImageFilePath == null) || (iconImageFilePath.length() == 0)) {
      return;
    }
    // Create a corresponding image descriptor
    ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
        configurationElement.getNamespaceIdentifier(), iconImageFilePath);
    // Abort if no corresponding image was found
    if (descriptor == null) {
      return;
    }
    // Create the image
    Image image = descriptor.createImage();
    // Abort if image creation failed
    if (image == null) {
      return;
    }
    // Abort if the image does not have dimensions of 50x50
    if ((image.getBounds().width != F_IMAGE_WIDTH) || (image.getBounds().height != F_IMAGE_HEIGHT)) {
      // Dipose of the image
      image.dispose();
      return;
    }
    // Store the image and tooltip
    fImageList.add(image);
  }

  /**
   * Configure ui splash.
   */
  private void configureUISplash() {
    // Configure layout
    GridLayout layout = new GridLayout(1, true);
    getSplash().setLayout(layout);
    // Force shell to inherit the splash background
    getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
  }

  /**
   * Do event loop.
   */
  private void doEventLoop() {
    Shell splash = getSplash();
    if (splash.getDisplay().readAndDispatch() == false) {
      splash.getDisplay().sleep();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.splash.AbstractSplashHandler#dispose()
   */
  public void dispose() {
    super.dispose();
    // Check to see if any images were defined
    if ((fImageList == null) || fImageList.isEmpty()) {
      return;
    }
    // Dispose of all the images
    Iterator iterator = fImageList.iterator();
    while (iterator.hasNext()) {
      Image image = (Image) iterator.next();
      image.dispose();
    }
  }
}
