package com.laex.cg2d.screeneditor.handlers.joints;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.laex.cg2d.model.adapter.RectAdapter;
import com.laex.cg2d.model.model.Shape;

public class LiveJointEditingDialog extends TitleAreaDialog {

  class JointRenderer {

    private FigureCanvas figureCanvas;
    private FreeformLayeredPane layeredPane;

    public JointRenderer(Composite composite) {

      initRenderer(composite);

      for (Shape shp : models) {

        RectangleFigure rf = new RectangleFigure();
        rf.setBounds(RectAdapter.d2dRect(shp.getBounds()));
        rf.setBackgroundColor(ColorConstants.green);

        layeredPane.add(rf);

      }

      figureCanvas.setBounds(0, 0, getInitialSize().x, getInitialSize().y);
      figureCanvas.setContents(layeredPane);

    }

    private void initRenderer(Composite composite) {
      figureCanvas = new FigureCanvas(composite);
      layeredPane = new FreeformLayeredPane();
      layeredPane.setLayoutManager(new FreeformLayout());
      figureCanvas.setContents(layeredPane);
    }

  }

  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  private List<Shape> models;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   * @wbp.parser.constructor
   */
  public LiveJointEditingDialog(Shell parentShell) {
    super(parentShell);
    setShellStyle(SWT.MAX);
  }

  public LiveJointEditingDialog(Shell parentShell, List<Shape> models) {
    super(parentShell);

    this.models = models;
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setTitle("Edit joint anchors.");
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    setupJointsRenderer();

    return area;
  }

  private void setupJointsRenderer() {
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(388, 346);
  }
}
