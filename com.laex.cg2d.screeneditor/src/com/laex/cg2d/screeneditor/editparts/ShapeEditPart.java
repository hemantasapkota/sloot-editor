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
package com.laex.cg2d.screeneditor.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.laex.cg2d.model.EntityManager;
import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.EditorShapeType;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.model.Joint;
import com.laex.cg2d.model.model.ModelElement;
import com.laex.cg2d.model.model.Shape;
import com.laex.cg2d.model.resources.ResourceManager;
import com.laex.cg2d.screeneditor.Activator;
import com.laex.cg2d.screeneditor.commands.ShapeChangeTextCommand;
import com.laex.cg2d.screeneditor.editparts.figure.BoxFigure;
import com.laex.cg2d.screeneditor.editparts.figure.CircleFigure;
import com.laex.cg2d.screeneditor.editparts.figure.HorizontalEdgeFigure;
import com.laex.cg2d.screeneditor.editparts.figure.TexturedBoxFigure;
import com.laex.cg2d.screeneditor.editparts.figure.VerticalEdgeFigure;
import com.laex.cg2d.screeneditor.editparts.policies.ShapeComponentEditPolicy;
import com.laex.cg2d.screeneditor.editparts.policies.ShapeGraphicalNodeEditPolicy;
import com.laex.cg2d.screeneditor.model.ShapesDiagramAdapter;
import com.laex.cg2d.screeneditor.views.SimpleTextChangeDialog;

/**
 * The Class ShapeEditPart.
 */
public class ShapeEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

  /** The anchor. */
  private ConnectionAnchor anchor;

  /** The selectable. */
  private boolean selectable = true;;

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
   */
  public void activate() {
    if (!isActive()) {
      super.activate();
      ((ModelElement) getModel()).addPropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
   */
  public void deactivate() {
    if (isActive()) {
      super.deactivate();
      ((ModelElement) getModel()).removePropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang
   * .Class)
   */
  @Override
  public Object getAdapter(Class key) {
    if (key == Shape.class) {
      return getModel();
    }

    if (key == ShapesDiagramAdapter.class) {
      return getParent().getModel();
    }

    return super.getAdapter(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef
   * .Request)
   */
  @Override
  public void performRequest(Request req) {
    if (req.getType() == RequestConstants.REQ_OPEN) {
      requestOpen();
    }
    super.performRequest(req);
  }

  private void requestOpen() {
    /* Request open: Open entity editor */
    Shape shp = getCastedModel();

    if (shp.getEditorShapeType().isEntity()) {
      String pathFile = shp.getEntityResourceFile().getResourceFile();
      FileEditorInput fei = new FileEditorInput(ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(pathFile)));
      try {
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .openEditor(fei, "com.laex.cg3d.entityeditor.EntityEditor");
      } catch (PartInitException e) {
        Activator.log(e);
      }
    }

    if (shp.getEditorShapeType().isBox()) {
      SimpleTextChangeDialog stcd = new SimpleTextChangeDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
          .getShell(), getCastedModel().getText(), "Change the text of the figure");
      int resp = stcd.open();
      if (resp == SimpleTextChangeDialog.CANCEL) {
        return;
      }

      ShapeChangeTextCommand sctc = new ShapeChangeTextCommand(getCastedModel(), stcd.getName());
      getViewer().getEditDomain().getCommandStack().execute(sctc);
      ((BoxFigure) getFigure()).updateLabel(stcd.getName());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies() {
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ShapeGraphicalNodeEditPolicy());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure() {
    IFigure f = createFigureForModel();
    return f;
  }

  /**
   * Creates the figure for model.
   * 
   * @return the i figure
   */
  private IFigure createFigureForModel() {
    Shape model = (Shape) getModel();
    EditorShapeType type = model.getEditorShapeType();
    IFigure figure = null;

    switch (type) {
    case SIMPLE_SHAPE_BOX:
      figure = new BoxFigure(model.getText());
      break;
    case SIMPLE_SHAPE_CIRCLE:
      figure = new CircleFigure();
      break;
    case SIMPLE_SHAPE_HEDGE:
      figure = new HorizontalEdgeFigure();
      break;
    case SIMPLE_SHAPE_VEDGE:
      figure = new VerticalEdgeFigure();
      break;
    case BACKGROUND_SHAPE:
      figure = new TexturedBoxFigure(ResourceManager.getImageOfRelativePath(model.getBackgroundResourceFile()
          .getResourceFile()));
      break;
    case ENTITY_SHAPE:
      figure = updateEntityFigure(model);
      break;

    default:
      throw new IllegalArgumentException();

    }

    figure.setVisible(model.isVisible());
    this.selectable = !model.isLocked();

    return figure;
  }

  /**
   * Update entity figure.
   * 
   * @param model
   *          the model
   * @return the i figure
   */
  private IFigure updateEntityFigure(Shape model) {
    IFigure figure;
    Entity e = EntityManager.entityManager().findEntity(model.getEntityResourceFile().getResourceFile());
    figure = new TexturedBoxFigure(e.getDefaultFrame());
    return figure;
  }

  /**
   * Gets the casted model.
   * 
   * @return the casted model
   */
  private Shape getCastedModel() {
    return (Shape) getModel();
  }

  /**
   * Gets the connection anchor.
   * 
   * @return the connection anchor
   */
  protected ConnectionAnchor getConnectionAnchor() {
    if (anchor == null) {
      EditorShapeType type = ((Shape) getModel()).getEditorShapeType();

      if (type.isCircle()) {
        anchor = new EllipseAnchor(getFigure());
      } else if (isValidForAnchor(type)) {
        anchor = new ChopboxAnchor(getFigure());
      }

    }
    return anchor;
  }

  /**
   * Checks if is valid for anchor.
   * 
   * @param type
   *          the type
   * @return true, if is valid for anchor
   */
  private boolean isValidForAnchor(EditorShapeType type) {
    return type.isBox() || type.isEntity();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
   * ()
   */
  protected List<Joint> getModelSourceConnections() {
    return getCastedModel().getSourceJoints();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
   * ()
   */
  protected List<Joint> getModelTargetConnections() {
    return getCastedModel().getTargetJoints();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.
   * ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
    return getConnectionAnchor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.
   * Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request) {
    return getConnectionAnchor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.
   * ConnectionEditPart)
   */
  public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
    return getConnectionAnchor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.
   * Request)
   */
  public ConnectionAnchor getTargetConnectionAnchor(Request request) {
    return getConnectionAnchor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#isSelectable()
   */
  @Override
  public boolean isSelectable() {
    return selectable;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  public void propertyChange(PropertyChangeEvent evt) {
    String prop = evt.getPropertyName();
    if (Shape.BOUNDS_PROP.equals(prop)) {
      refreshVisuals();
    } else if (Shape.SOURCE_JOINT_PROP.equals(prop)) {
      refreshSourceConnections();
    } else if (Shape.TARGET_JOINT_PROP.equals(prop)) {
      refreshTargetConnections();
    } else if (Shape.SHAPE_VISIBLE.equals(prop)) {
      getFigure().setVisible((Boolean) evt.getNewValue());
      refreshVisuals();
    } else if (Shape.SHAPE_LOCKED.equals(prop)) {
      boolean locked = (Boolean) evt.getNewValue();
      this.selectable = !locked;
    } else {
      refreshVisuals();
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
   */
  protected void refreshVisuals() {
    // notify parent container of changed position & location
    // if this line is removed, the XYLayoutManager used by the parent
    // container
    // (the Figure of the ShapesDiagramEditPart), will not know the bounds
    // of this figure
    // and will not draw it correctly.
    // Rectangle bounds = new Rectangle(getCastedModel().getLocation(),
    // getCastedModel().getSize());
    Rectangle bounds = RectAdapter.d2dRect(getCastedModel().getBounds());
    ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
  }

}