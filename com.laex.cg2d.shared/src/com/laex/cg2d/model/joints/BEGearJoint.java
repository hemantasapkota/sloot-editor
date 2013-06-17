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
package com.laex.cg2d.model.joints;

import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.Shape;

/**
 * The Class BEGearJoint.
 */
public class BEGearJoint extends Joint {

  /**
   * Instantiates a new bE gear joint.
   * 
   * @param source
   *          the source
   * @param target
   *          the target
   */
  public BEGearJoint(Shape source, Shape target) {
    super(source, target);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.laex.cg2d.shared.model.Joint#getJointType()
   */
  @Override
  public JointType getJointType() {
    return JointType.GearJoint;
  }

}
