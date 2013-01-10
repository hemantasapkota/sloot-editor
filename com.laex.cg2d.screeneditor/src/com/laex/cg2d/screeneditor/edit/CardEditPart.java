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
package com.laex.cg2d.screeneditor.edit;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * The Class CardEditPart.
 */
public class CardEditPart extends AbstractGraphicalEditPart {

  /** The card number x. */
  private int cardNumberX;
  
  /** The card number y. */
  private int cardNumberY;
  
  /** The card height. */
  private int cardHeight;
  
  /** The card width. */
  private int cardWidth;

  /**
   * Instantiates a new card edit part.
   *
   * @param cardNumberX the card number x
   * @param cardNumberY the card number y
   * @param cardWidth the card width
   * @param cardHeight the card height
   */
  public CardEditPart(int cardNumberX, int cardNumberY, int cardWidth, int cardHeight) {
    this.cardNumberX = cardNumberX;
    this.cardNumberY = cardNumberY;
    this.cardWidth = cardWidth;
    this.cardHeight = cardHeight;
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  @Override
  protected IFigure createFigure() {
    RectangleFigure rf = new RectangleFigure();
    rf.setAlpha(30);
    rf.setBorder(new MarginBorder(0));
    rf.setAntialias(120);
    rf.setBackgroundColor(ColorConstants.lightGray);
    rf.setOpaque(true);

    int nx = cardWidth * cardNumberX;
    int ny = cardHeight * cardNumberY;

    rf.setBounds(new Rectangle(nx, ny, cardWidth, cardHeight));
    return rf;
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  @Override
  protected void createEditPolicies() {
  }

}
