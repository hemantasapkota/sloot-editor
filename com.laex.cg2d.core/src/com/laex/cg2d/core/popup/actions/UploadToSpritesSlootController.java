package com.laex.cg2d.core.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Shell;

import com.googlecode.protobuf.format.JsonFormat;
import com.laex.cg2d.core.Activator;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.SlootContent;
import com.laex.cg2d.model.SlootContent.SlootCollection;
import com.laex.cg2d.model.SlootContent.SlootCollectionList;
import com.laex.cg2d.model.SlootContent.SlootItem;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.util.EntitiesUtil;

public class UploadToSpritesSlootController {

  public static class SlootProjectProvider {
    public List<IResource> entities = new ArrayList<IResource>();
    public List<IResource> screens = new ArrayList<IResource>();
    public List<IResource> textures = new ArrayList<IResource>();
  }

  public IResource toExport;
  public String targetDirectory;
  private SlootProjectProvider slootProjectProvider;

  public UploadToSpritesSlootController(IResource toExport, String targetDirectory,
      SlootProjectProvider slootProjectProvider) {
    this.toExport = toExport;
    this.targetDirectory = targetDirectory;
    this.slootProjectProvider = slootProjectProvider;
  }

  private IPath createDestinationFolderStructure(String destination) throws CoreException, IOException {
    IPath destinationPath = new Path(destination).append(toExport.getName());
    FileUtils.forceMkdir(destinationPath.toFile());
    return destinationPath;
  }

  private void exportSprites(IPath basePath, IResource resource) throws CoreException {
    /* Export Entities */
    if (isEntity(resource)) {
      IFile file = (IFile) resource;
      try {
        CGEntity cgEntity = CGEntity.parseFrom(file.getContents());

        /* Step 1 : Generate JSON file and write it to file */
        String json = JsonFormat.printToString(cgEntity);
        File outputFile = basePath.append(resource.getName()).addFileExtension("json").toFile();
        FileUtils.writeByteArrayToFile(outputFile, json.getBytes());

        /* Step 2 : Generate PNG preview file and write it to file */
        Entity e = EntityAdapter.asEntity(cgEntity);
        Image i = EntitiesUtil.getDefaultFrame(e, 1f);
        String imgOutputFilename = basePath.append(resource.getName()).addFileExtension("png").toOSString();
        ImageLoader il = new ImageLoader();
        il.data = new ImageData[]
          { i.getImageData() };
        il.save(imgOutputFilename, SWT.IMAGE_PNG);

      } catch (IOException e) {
        Activator.log(e);
      }
    }
  }

  private void exportScreens(IPath basePath, IResource resource) throws CoreException {
    if (isScreen(resource)) {
      IFile file = (IFile) resource;
      try {
        CGScreenModel screen = CGScreenModel.parseFrom(file.getContents());
        String json = JsonFormat.printToString(screen);
        File outputFile = basePath.append(resource.getName()).addFileExtension("json").toFile();
        FileUtils.writeByteArrayToFile(outputFile, json.getBytes());
      } catch (IOException e) {
        Activator.log(e);
      }
    }
  }

  public static boolean isScreen(IResource resource) {
    return resource.getName().endsWith(ICGCProject.SCREEN_EXTENSION);
  }

  public static boolean isEntity(IResource resource) {
    return resource.getName().endsWith(ICGCProject.ENTITIES_EXTENSION);
  }

  private void generateSlootContent(IPath basePath, String targetDir) throws IOException, CoreException {
    /*
     * We write sloot-content.json to a directory one step outside of the
     * collection
     */
    File fileJson = basePath.removeLastSegments(1).append("sloot-content.json").toFile();
    File fileProtobuf = basePath.removeLastSegments(1).append("sloot-content.cmf").toFile();
    IPath imgFileBasePath = basePath.removeFirstSegments(basePath.segmentCount() - 2); // our
                                                                                       // url
                                                                                       // should
                                                                                       // be:
                                                                                       // sloot-content/<folder_name>/...

    final SlootCollection.Builder builder = SlootCollection.newBuilder().setTitle(toExport.getName());

    /* TODO: Build list of screens as well. */
    for (IResource resource : slootProjectProvider.entities) {
      String imgUrlPath = imgFileBasePath.append(resource.getName()).addFileExtension(ICGCProject.PNG_EXTENSION)
          .toOSString();
      String title = resource.getLocation().removeFileExtension().lastSegment();

      String id = toExport.getName() + "_" + resource.getName();
      SlootItem si = SlootItem.newBuilder().setId(id).setImgUrl(imgUrlPath).setTitle(title).setPrice(0.99f).build();
      builder.addSlootItems(si);
    }

    SlootCollectionList collectionList = SlootCollectionList.newBuilder().addSlootCollection(builder.build()).build();
    String newJsonCollection = JsonFormat.printToString(collectionList);
    try {
      /* Combine existing Json with new one and write it to file. */
      SlootCollectionList existinglistOfCollections = SlootContent.SlootCollectionList.parseFrom(new FileInputStream(
          fileProtobuf));

      existinglistOfCollections = SlootCollectionList.newBuilder()
          .addAllSlootCollection(existinglistOfCollections.getSlootCollectionList())
          .addSlootCollection(collectionList.getSlootCollection(0)).build();

      String existingCollectionJson = JsonFormat.printToString(existinglistOfCollections);
      FileUtils.writeByteArrayToFile(fileJson, existingCollectionJson.toString().getBytes());
      FileUtils.writeByteArrayToFile(fileProtobuf, existinglistOfCollections.toByteArray());

    } catch (FileNotFoundException e) {
      /* Let's do something here */
      /* Just write the new json */
      FileUtils.writeByteArrayToFile(fileJson, newJsonCollection.toString().getBytes());
      FileUtils.writeByteArrayToFile(fileProtobuf, collectionList.toByteArray());
    }

  }

  public void exportLocally(Shell shell) throws CoreException, InvocationTargetException, InterruptedException,
      IOException {
    final IPath basePath = createDestinationFolderStructure(this.targetDirectory);

    Job job = new Job("Export locally") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {

        monitor.beginTask("Exporting", slootProjectProvider.entities.size() + slootProjectProvider.screens.size() + slootProjectProvider.textures.size());

        monitor.subTask("Exporting Sprites");
        for (IResource cgEntityRes : slootProjectProvider.entities) {

          try {
            monitor.setTaskName(cgEntityRes.getName());
            exportSprites(basePath, cgEntityRes);
            monitor.worked(1);
          } catch (CoreException e) {
            Activator.log(e);
          }

        }

        monitor.subTask("Exporting Screens");
        for (IResource cgScreenRes : slootProjectProvider.screens) {
          try {
            monitor.setTaskName(cgScreenRes.getName());
            exportScreens(basePath, cgScreenRes);
            monitor.worked(1);
          } catch (CoreException e) {
            Activator.log(e);
          }

        }

        monitor.subTask("Exportng Textures");
        for (IResource cgTextureRes : slootProjectProvider.textures) {
          File outputFile = basePath.append(cgTextureRes.getName()).toFile();
          try {
            monitor.setTaskName(cgTextureRes.getName());
            FileUtils.writeByteArrayToFile(outputFile, IOUtils.toByteArray(((IFile) cgTextureRes).getContents()));
            monitor.worked(1);
          } catch (IOException e) {
            Activator.log(e);
          } catch (CoreException e) {
            Activator.log(e);
          }
        }

        monitor.done();
        return Status.OK_STATUS;
      }
    };

    job.setUser(true);
    job.schedule();


    /* */
    generateSlootContent(basePath, targetDirectory);
  }

  public static SlootProjectProvider slootProjectProvider(IProject project) throws CoreException {
    final SlootProjectProvider labelProvider = new SlootProjectProvider();

    project.accept(new IResourceVisitor() {
      @Override
      public boolean visit(IResource resource) throws CoreException {
        if (UploadToSpritesSlootController.isEntity(resource)) {
          labelProvider.entities.add(resource);
        }
        if (UploadToSpritesSlootController.isScreen(resource)) {
          labelProvider.screens.add(resource);
        }
        return true;
      }
    });

    IFolder texturesFolder = project.getFolder(ICGCProject.TEXTURES_FOLDER);
    texturesFolder.accept(new IResourceVisitor() {
      @Override
      public boolean visit(IResource resource) throws CoreException {
        if (resource.getType() == IResource.FILE) {
          labelProvider.textures.add(resource);
        }
        return true;
      }
    });

    return labelProvider;
  }
}