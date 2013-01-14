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
package com.laex.cg2d.render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.laex.cg2d.protobuf.GameObject.CGGameModel;

/**
 * The Class MyGdxGameDesktop.
 * 
 * @author hemantasapkota
 */
public class MyGdxGameDesktop {

  /**
   * Construct.
   * 
   * @return the options
   */
  public static Options construct() {
    final Options gnuOptions = new Options();

    Option cardWidth = new Option("cw", true, "Card Width");
    Option cardHeight = new Option("ch", true, "Card Height");
    Option cardNoX = new Option("cx", true, "Card No X");
    Option cardNoY = new Option("cy", true, "Card No Y");

    Option drawBodies = new Option("body", "Draw Bodies");
    Option drawJoint = new Option("joint", "Draw Joints");
    Option drawAABB = new Option("aabb", "Draw AABB");
    Option drawInactiveBodies = new Option("inactive", "Draw Inactive Bodieds");
    Option drawDebugData = new Option("debugData", "Draw Debug Data");
    Option drawEntities = new Option("entities", "Draw Entities");
    Option mouseJoint = new Option("mouseJoint", "Install Mouse Joint");

    Option ptmRatio = new Option("ptmRatio", true, "PIXELS TO METERS (PTM) Ratio");
    Option gravityX = new Option("gravityX", true, "Gravity X. Can be negative.");
    Option gravityY = new Option("gravityY", true, "Gravity Y. Can be negative.");
    Option timestep = new Option("timeStep", true, "TimeStep");
    Option velocityItr = new Option("velItr", true, "Velocity Iterations");
    Option positionItr = new Option("posItr", true, "Position Iterations");

    Option screenFile = new Option("screenFile", true, "CG2D Screen File");

    gnuOptions.addOption(cardWidth);
    gnuOptions.addOption(cardHeight);
    gnuOptions.addOption(cardNoX);
    gnuOptions.addOption(cardNoY);
    gnuOptions.addOption(drawBodies);
    gnuOptions.addOption(drawJoint);
    gnuOptions.addOption(drawAABB);
    gnuOptions.addOption(drawInactiveBodies);
    gnuOptions.addOption(drawDebugData);
    gnuOptions.addOption(drawEntities);
    gnuOptions.addOption(mouseJoint);

    gnuOptions.addOption(ptmRatio);
    gnuOptions.addOption(gravityX);
    gnuOptions.addOption(gravityY);
    gnuOptions.addOption(timestep);
    gnuOptions.addOption(velocityItr);
    gnuOptions.addOption(positionItr);

    gnuOptions.addOption(screenFile);

    return gnuOptions;
  }

  /**
   * Parses the.
   * 
   * @param options
   *          the options
   * @param args
   *          the args
   * @return the map
   * @throws ParseException
   *           the parse exception
   */
  private static Map<String, Object> parse(Options options, String args[]) throws ParseException {
    Map<String, Object> prefs = new HashMap<String, Object>();

    CommandLineParser parse = new GnuParser();
    CommandLine cmd = parse.parse(options, args);

    prefs.put(PreferenceConstants.DRAW_AABB, false);
    prefs.put(PreferenceConstants.DRAW_BODIES, false);
    prefs.put(PreferenceConstants.DRAW_JOINT, false);
    prefs.put(PreferenceConstants.DRAW_DEBUG_DATA, false);
    prefs.put(PreferenceConstants.DRAW_ENTITIES, false);
    prefs.put(PreferenceConstants.INSTALL_MOUSE_JOINT, false);
    prefs.put(PreferenceConstants.DRAW_INACTIVE_BODIES, false);

    // Card Prefernces
    if (cmd.hasOption("cw")) {
      prefs.put(PreferenceConstants.CARD_WIDTH, RunnerUtil.toInt(cmd.getOptionValue("cw")));
    }

    if (cmd.hasOption("ch")) {
      prefs.put(PreferenceConstants.CARD_HEIGHT, RunnerUtil.toInt(cmd.getOptionValue("ch")));
    }

    if (cmd.hasOption("cx")) {
      prefs.put(PreferenceConstants.CARD_NO_X, RunnerUtil.toInt(cmd.getOptionValue("cx")));
    }

    if (cmd.hasOption("cy")) {
      prefs.put(PreferenceConstants.CARD_NO_Y, RunnerUtil.toInt(cmd.getOptionValue("cy")));
    }

    // Debug Draw Flags
    if (cmd.hasOption("body")) {
      prefs.put(PreferenceConstants.DRAW_BODIES, true);
    }

    if (cmd.hasOption("joint")) {
      prefs.put(PreferenceConstants.DRAW_JOINT, true);
    }

    if (cmd.hasOption("aabb")) {
      prefs.put(PreferenceConstants.DRAW_AABB, true);
    }

    if (cmd.hasOption("inactive")) {
      prefs.put(PreferenceConstants.DRAW_INACTIVE_BODIES, true);
    }

    if (cmd.hasOption("debugData")) {
      prefs.put(PreferenceConstants.DRAW_DEBUG_DATA, true);
    }

    // Entites & Mouse Joint
    if (cmd.hasOption("entities")) {
      prefs.put(PreferenceConstants.DRAW_ENTITIES, true);
    }

    if (cmd.hasOption("mouseJoint")) {
      prefs.put(PreferenceConstants.INSTALL_MOUSE_JOINT, true);
    }

    // Box2d Prefs
    if (cmd.hasOption("ptmRatio")) {
      prefs.put(PreferenceConstants.PTM_RATIO, RunnerUtil.toInt(cmd.getOptionValue("ptmRatio")));
    }

    if (cmd.hasOption("gravityX")) {
      prefs.put(PreferenceConstants.GRAVITY_X, RunnerUtil.toFloat(cmd.getOptionValue("gravityX")));
    }

    if (cmd.hasOption("gravityY")) {
      prefs.put(PreferenceConstants.GRAVITY_Y, RunnerUtil.toFloat(cmd.getOptionValue("gravityY")));
    }

    if (cmd.hasOption("timeStep")) {
      prefs.put(PreferenceConstants.TIMESTEP, RunnerUtil.toFloat(cmd.getOptionValue("timeStep")));
    }

    if (cmd.hasOption("velItr")) {
      prefs.put(PreferenceConstants.VELOCITY_ITERATIONS, RunnerUtil.toInt(cmd.getOptionValue("velItr")));
    }

    if (cmd.hasOption("posItr")) {
      prefs.put(PreferenceConstants.POSITION_ITERATIONS, RunnerUtil.toInt(cmd.getOptionValue("posItr")));
    }

    // CG2D File
    if (cmd.hasOption("screenFile")) {
      prefs.put(PreferenceConstants.SCREEN_FILE, cmd.getOptionValue("screenFile"));
    }

    return prefs;
  }

  /**
   * The main method.
   * 
   * @param args
   *          the arguments
   * @throws ParseException
   *           the parse exception
   */
  public static void main(String[] args) throws ParseException {
    Options options = construct();

    Map<String, Object> prefs = parse(options, args);

    String cg2dFile = (String) prefs.get(PreferenceConstants.SCREEN_FILE);

    InputStream is;
    CGGameModel model = null;

    try {
      is = new FileInputStream(cg2dFile);
      model = CGGameModel.parseFrom(is);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return;
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    int cardWidth = model.getScreenPrefs().getCardPrefs().getCardWidth();
    int cardHeight = model.getScreenPrefs().getCardPrefs().getCardHeight();

    JoglApplicationConfiguration jac = new JoglApplicationConfiguration();
    jac.width = cardWidth;
    jac.height = cardHeight;
    jac.title = cg2dFile;

    final CGGameModel modelMain = model;
    MyGdxGame mgd = new MyGdxGame() {
      @Override
      public CGGameModel loadGameModel() {
        return modelMain;
      }
    };

    new JoglApplication(mgd, jac);
  }
}
