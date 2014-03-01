package com.laex.cg2d.core.popup.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.laex.cg2d.core.Activator;
import com.laex.cg2d.core.popup.actions.UploadToSpritesProjectProvider.SlootProjectProvider;

public class UploadToSpritesSlootDialog extends TitleAreaDialog {

  private class TreeContentProvider implements ITreeContentProvider {
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

    public Object[] getElements(Object inputElement) {
      return new Object[]
        { "Entities", "Screens", "Textures" };
    }

    public Object[] getChildren(Object parentElement) {

      if (parentElement.equals("Entities")) {
        return slootProjectProvider.entities.toArray();
      }

      if (parentElement.equals("Screens")) {
        return slootProjectProvider.screens.toArray();
      }

      if (parentElement.equals("Textures")) {
        return slootProjectProvider.textures.toArray();
      }

      return new Object[] {};

    }

    public Object getParent(Object element) {
      return null;
    }

    public boolean hasChildren(Object element) {
      return getChildren(element).length > 0;
    }
  }

  private static class ViewerLabelProvider extends LabelProvider {
    public Image getImage(Object element) {
      return super.getImage(element);
    }

    public String getText(Object element) {

      if (element instanceof IResource) {
        return ((IResource) element).getName();
      }

      return element.toString();
    }
  }

  private Text txtTargetLocalExport;
  private IResource toExport;
  private UploadToSpritesSlootController controller;
  private SlootProjectProvider slootProjectProvider;
  private Text txtDeveloperID;
  private Text txtCollectionTitle;
  private Text txtServer;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   * @wbp.parser.constructor
   */
  public UploadToSpritesSlootDialog(Shell parentShell) {
    super(parentShell);
  }

  public UploadToSpritesSlootDialog(Shell parentShell, IResource toExport) {
    super(parentShell);

    this.toExport = toExport;


  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setMessage("This dialog allows you to upload the selected project to SpritesSloot. To export the project locally, just select the \"Export Locally\" tab.");
    setTitle("Upload project to SpritesSloot [Experimental]");
    Composite area = (Composite) super.createDialogArea(parent);

    Composite composite_2 = new Composite(area, SWT.NONE);
    composite_2.setLayout(new GridLayout(2, false));
    composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    
    Label lblServerAddress = new Label(composite_2, SWT.NONE);
    lblServerAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblServerAddress.setText("Server Address");
    
    txtServer = new Text(composite_2, SWT.BORDER);
    txtServer.setText("localhost:3000");
    txtServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    Label lblNewLabel = new Label(composite_2, SWT.NONE);
    lblNewLabel.setText("Developer ID");

    txtDeveloperID = new Text(composite_2, SWT.BORDER);
    txtDeveloperID.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        validate();
      }
    });
    txtDeveloperID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    Label lblCollectionTitle = new Label(composite_2, SWT.NONE);
    lblCollectionTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblCollectionTitle.setText("Collection Title");

    txtCollectionTitle = new Text(composite_2, SWT.BORDER);
    txtCollectionTitle.setEditable(false);
    txtCollectionTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    TabFolder tabFolder = new TabFolder(container, SWT.NONE);

    TabItem tbtmExportLocally = new TabItem(tabFolder, SWT.NONE);
    tbtmExportLocally.setText("Upload");

    Composite composite = new Composite(tabFolder, SWT.NONE);
    tbtmExportLocally.setControl(composite);
    composite.setLayout(new GridLayout(3, false));

    Label lblTargetDirectory = new Label(composite, SWT.NONE);
    lblTargetDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblTargetDirectory.setText("Local Target Directory");

    txtTargetLocalExport = new Text(composite, SWT.BORDER);
    txtTargetLocalExport.setEditable(false);
    txtTargetLocalExport.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        validate();
      }
    });
    txtTargetLocalExport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    Button btnBrowse = new Button(composite, SWT.NONE);
    btnBrowse.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        DirectoryDialog dd = new DirectoryDialog(getParentShell());
        String targetDirectory = dd.open();
        if (targetDirectory != null) {
          txtTargetLocalExport.setText(targetDirectory);
        }
      }
    });
    btnBrowse.setText("Browse");

    Composite composite_1 = new Composite(composite, SWT.NONE);
    composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
    gd_composite_1.heightHint = 253;
    gd_composite_1.widthHint = 66;
    composite_1.setLayoutData(gd_composite_1);

    TreeViewer treeViewer = new TreeViewer(composite_1, SWT.BORDER);
    Tree tree = treeViewer.getTree();
    tree.setHeaderVisible(true);

    TreeColumn trclmnResources = new TreeColumn(tree, SWT.NONE);
    trclmnResources.setWidth(464);
    trclmnResources.setText("Resources");
    treeViewer.setContentProvider(new TreeContentProvider());
    treeViewer.setLabelProvider(new ViewerLabelProvider());

    String exportPath = this.toExport.getLocation().append("tmp").toOSString();
    txtTargetLocalExport.setText(exportPath);

    /* generate sloot project provider */
    try {

      slootProjectProvider = UploadToSpritesProjectProvider.slootProjectProvider("laex.pearl@gmail.com", (IProject) toExport);
      txtCollectionTitle.setText(slootProjectProvider.collectionTitle);
      txtDeveloperID.setText(slootProjectProvider.developerID);

      treeViewer.setInput(slootProjectProvider);
      treeViewer.expandAll();

    } catch (CoreException e1) {
      Activator.log(e1);
    }

    return area;
  }

  private void validate() {

    setOKButtonStatus(false);

    if (StringUtils.isEmpty(txtDeveloperID.getText())) {
      setErrorMessage("Developer ID cannot be empty");
    } else if (StringUtils.isEmpty(txtTargetLocalExport.getText())) {
      setErrorMessage("Target directory cannot be empty");
    } else {

      setErrorMessage(null);
      setOKButtonStatus(true);

    }

  }

  private void setOKButtonStatus(boolean status) {
    if (getButton(OK) != null) {
      getButton(OK).setEnabled(status);
    }
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
    return new Point(571, 473);
  }

  @Override
  protected void okPressed() {

    try {

      controller = new UploadToSpritesSlootController(toExport, txtTargetLocalExport.getText(), slootProjectProvider);

      controller.exportLocally(txtServer.getText().trim(), getParentShell());

    } catch (CoreException e) {
      Activator.log(e);
    } catch (InvocationTargetException e) {
      Activator.log(e);
    } catch (InterruptedException e) {
      Activator.log(e);
    } catch (IOException e) {
      Activator.log(e);
    }

    super.okPressed();

  }
}
