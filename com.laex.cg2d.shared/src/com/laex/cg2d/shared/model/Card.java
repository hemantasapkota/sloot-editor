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
package com.laex.cg2d.shared.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * The Class Card.
 */
public class Card extends ModelElement {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1170672262125613001L;

  /** The Constant BACKGROUND_PROP. */
  public static final String BACKGROUND_PROP = "Card.Background";

  /** The Constant CARD_NO_X_PROP. */
  public static final String CARD_NO_X_PROP = "Card.NoX";

  /** The Constant CARD_NO_Y_PROP. */
  public static final String CARD_NO_Y_PROP = "Card.NoY";

  /** The descriptors. */
  private static IPropertyDescriptor[] descriptors;

  static {
    PropertyDescriptor cardNoXProp = new TextPropertyDescriptor(CARD_NO_X_PROP, "No(x)");
    PropertyDescriptor cardNoYProp = new TextPropertyDescriptor(CARD_NO_Y_PROP, "No(y)");

    descriptors = new IPropertyDescriptor[]
      { cardNoXProp, cardNoYProp };

  }

  /** The card number x. */
  private int cardNumberX;

  /** The card number y. */
  private int cardNumberY;

  /** The card w. */
  private int cardW;

  /** The card h. */
  private int cardH;

  /**
   * Instantiates a new card.
   */
  public Card() {
  }

  /**
   * Instantiates a new card.
   * 
   * @param cNoX
   *          the c no x
   * @param cNoY
   *          the c no y
   * @param cardW
   *          the card w
   * @param cardH
   *          the card h
   */
  public Card(int cNoX, int cNoY, int cardW, int cardH) {
    this.cardNumberX = cNoX;
    this.cardNumberY = cNoY;
    this.cardW = cardW;
    this.cardH = cardH;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.ModelElement#getEditableValue()
   */
  @Override
  public Object getEditableValue() {
    return this;
  }

  /**
   * Gets the descriptors.
   * 
   * @return the descriptors
   */
  public static IPropertyDescriptor[] getDescriptors() {
    return descriptors;
  }

  /**
   * Gets the card number x.
   * 
   * @return the card number x
   */
  public int getCardNumberX() {
    return cardNumberX;
  }

  /**
   * Gets the card number y.
   * 
   * @return the card number y
   */
  public int getCardNumberY() {
    return cardNumberY;
  }

  /**
   * Gets the card h.
   * 
   * @return the card h
   */
  public int getCardH() {
    return cardH;
  }

  /**
   * Gets the card w.
   * 
   * @return the card w
   */
  public int getCardW() {
    return cardW;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#getPropertyValue(java.lang.Object)
   */
  @Override
  public Object getPropertyValue(Object id) {
    if (CARD_NO_X_PROP.equals(id)) {
      return Integer.toString(cardNumberX);
    }
    if (CARD_NO_Y_PROP.equals(id)) {
      return Integer.toString(cardNumberY);
    }
    return super.getPropertyValue(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.laex.cg2d.shared.model.ModelElement#setPropertyValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void setPropertyValue(Object id, Object value) {
    super.setPropertyValue(id, value);
  }

}