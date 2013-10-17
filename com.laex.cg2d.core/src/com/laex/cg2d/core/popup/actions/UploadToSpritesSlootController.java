package com.laex.cg2d.core.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Shell;

import com.googlecode.protobuf.format.JsonFormat;
import com.laex.cg2d.core.Activator;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGEntityCollisionType;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGResourceFile;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.SlootContent;
import com.laex.cg2d.model.SlootContent.SlootCollection;
import com.laex.cg2d.model.SlootContent.SlootCollectionList;
import com.laex.cg2d.model.SlootContent.SlootItem;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.util.EntitiesUtil;

public class UploadToSpritesSlootController {

  public static class SlootProjectProvider {
    public String developerID = "";
    public String collectionID = "";
    public String collectionTitle = "";

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

  private void exportSprites(IPath basePath, IResource resource) throws CoreException, IOException {
    /* Export Entities */
    if (isEntity(resource)) {
      IFile file = (IFile) resource;

      CGEntity cgEntity = CGEntity.parseFrom(file.getContents());

      /* Rewrite fixtures/spritesheet/spritesheetJsonFile path */
      CGEntity.Builder bldr = CGEntity.newBuilder(cgEntity);
      for (CGEntityAnimation.Builder animBldr : bldr.getAnimationsBuilderList()) {

        IPath trimmedBasePath = basePath.removeFirstSegments(basePath.segmentCount() - 2);

        if (!animBldr.getSpritesheetFileBuilder().getResourceFile().isEmpty()) {

          String lastSegmentSpritesheetFile = new Path(animBldr.getSpritesheetFileBuilder().getResourceFile())
              .lastSegment();
          String rewrittenPath = trimmedBasePath.append(lastSegmentSpritesheetFile).toOSString();

          animBldr.getSpritesheetFileBuilder().setResourceFile(rewrittenPath).setResourceFileAbsolute("");

        }

        if (!animBldr.getSpritesheetJsonFile().getResourceFile().isEmpty()) {

          String lastSegmentSpritesheetJsonFile = new Path(animBldr.getSpritesheetJsonFileBuilder().getResourceFile())
              .lastSegment();

          String rewrittenPath = trimmedBasePath.append(lastSegmentSpritesheetJsonFile).toOSString();

          animBldr.getSpritesheetJsonFileBuilder().setResourceFile(rewrittenPath).setResourceFileAbsolute("");

        }

        /* For custom collision type */
        if (animBldr.getCollisionType() == CGEntityCollisionType.CUSTOM) {

          String lastSegment = new Path(animBldr.getFixtureFileBuilder().getResourceFile()).lastSegment();
          String rewrittenPath = trimmedBasePath.append(lastSegment).toOSString();

          animBldr.getFixtureFileBuilder().setResourceFile(rewrittenPath).setResourceFileAbsolute("");

        }

      }

      CGEntity modifiedCGEntity = bldr.build();

      /* Step 1 : Generate JSON file and write it to file */
      String json = JsonFormat.printToString(modifiedCGEntity);
      File outputFile = basePath.append(resource.getName()).addFileExtension("json").toFile();
      FileUtils.writeByteArrayToFile(outputFile, json.getBytes());

      /* Step 2 : Generate PNG preview file and write it to file */
      Entity e = EntityAdapter.asEntity(cgEntity); // extract image from
                                                   // unmodified information
      Image i = EntitiesUtil.getDefaultFrame(e, 1f);
      String imgOutputFilename = basePath.append(resource.getName()).addFileExtension("png").toOSString();
      ImageLoader il = new ImageLoader();
      il.data = new ImageData[]
        { i.getImageData() };
      il.save(imgOutputFilename, SWT.IMAGE_PNG);

    }
  }

  private void exportScreens(IPath basePath, IResource resource) throws CoreException, IOException {
    if (isScreen(resource)) {
      IFile file = (IFile) resource;

      CGScreenModel screen = CGScreenModel.parseFrom(file.getContents());

      /* Based on the prototype, we create a new builder */
      CGScreenModel.Builder bldr = CGScreenModel.newBuilder(screen);

      /* Rewrite entities path */
      for (CGLayer.Builder layerBuilder : bldr.getLayersBuilderList()) {
        for (CGShape.Builder shpBuilder : layerBuilder.getShapeBuilderList()) {

          boolean isEntity = shpBuilder.getEditorShapeType() == CGEditorShapeType.ENTITY_SHAPE;
          boolean isBg = shpBuilder.getEditorShapeType() == CGEditorShapeType.BACKGROUND_SHAPE;

          /* For entity */
          if (isEntity) {
            CGResourceFile entityRefFile = shpBuilder.getEntityRefFile();
            if (!entityRefFile.getResourceFile().isEmpty()) {
              String lastSegment = new Path(entityRefFile.getResourceFile()).lastSegment();
              String rewrittenPath = basePath.removeFirstSegments(basePath.segmentCount() - 2).append(lastSegment)
                  .addFileExtension("json").toOSString();

              entityRefFile = CGResourceFile.newBuilder(entityRefFile).setResourceFile(rewrittenPath)
                  .setResourceFileAbsolute("").build();

              shpBuilder.setEntityRefFile(entityRefFile);
            }
          }

          if (isBg) {
            CGResourceFile bgResourceFile = shpBuilder.getBackgroundResourceFile();
            if (!bgResourceFile.getResourceFile().isEmpty()) {
              String lastSegment = new Path(bgResourceFile.getResourceFile()).lastSegment();

              /* Background Resource File: has extension PNG, we dont append JSON extend to this file */
              String rewrittenPath = basePath.removeFirstSegments(basePath.segmentCount() - 2).append(lastSegment)
                  .toOSString();

              bgResourceFile = CGResourceFile.newBuilder(bgResourceFile).setResourceFile(rewrittenPath)
                  .setResourceFileAbsolute("").build();

              shpBuilder.setBackgroundResourceFile(bgResourceFile);

            }

          }

        }
      }

      screen = bldr.build();

      String json = JsonFormat.printToString(screen);
      File outputFile = basePath.append(resource.getName()).addFileExtension("json").toFile();
      FileUtils.writeByteArrayToFile(outputFile, json.getBytes());

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

    final SlootCollection.Builder builder = SlootCollection.newBuilder().setTitle(slootProjectProvider.collectionTitle)
        .setDeveloperId(slootProjectProvider.developerID).setSlootCollectionId(slootProjectProvider.collectionID);

    for (IResource resource : slootProjectProvider.entities) {
      String imgUrlPath = imgFileBasePath.append(resource.getName()).addFileExtension(ICGCProject.PNG_EXTENSION)
          .toOSString();
      String title = resource.getLocation().removeFileExtension().lastSegment();

      String id = toExport.getName() + "_" + resource.getName();
      SlootItem si = SlootItem.newBuilder().setId(id).setImgUrl(imgUrlPath).setTitle(title).setPrice(0.99f).build();
      builder.addSlootItems(si);
    }

    SlootCollectionList newCollectionList = SlootCollectionList.newBuilder().addSlootCollection(builder.build())
        .build();
    String newJsonCollection = JsonFormat.printToString(newCollectionList);
    try {
      /* Combine existing Json with new one and write it to file. */
      SlootCollectionList existinglistOfCollections = SlootContent.SlootCollectionList.parseFrom(new FileInputStream(
          fileProtobuf));

      /*
       * We might have to update information on one of the existng collections
       * already present on the list
       */
      boolean shouldAppend = true;
      SlootCollectionList.Builder bldr = SlootCollectionList.newBuilder(existinglistOfCollections);

      for (SlootCollection.Builder colBldr : bldr.getSlootCollectionBuilderList()) {

        SlootCollection c = newCollectionList.getSlootCollection(0);

        /* Ok definitely, we have to update the list instead of appending it */
        if (colBldr.getDeveloperId().equals(c.getDeveloperId()) && colBldr.getTitle().equals(c.getTitle())) {

          // colBldr.setDeveloperId(c.getDeveloperId()).setTitle(c.getTitle()).setSlootCollectionId(c.getSlootCollectionId());

          for (int i = 0; i < c.getSlootItemsCount(); i++) {
            SlootItem si = c.getSlootItems(i);
            colBldr.setSlootItems(i, si);
          }

          shouldAppend = false;

        }
      }

      /* update */
      existinglistOfCollections = bldr.build();

      if (shouldAppend) {

        existinglistOfCollections = SlootCollectionList.newBuilder()
            .addAllSlootCollection(existinglistOfCollections.getSlootCollectionList())
            .addSlootCollection(newCollectionList.getSlootCollection(0)).build();

      }

      String existingCollectionJson = JsonFormat.printToString(existinglistOfCollections);
      FileUtils.writeByteArrayToFile(fileJson, existingCollectionJson.toString().getBytes());
      FileUtils.writeByteArrayToFile(fileProtobuf, existinglistOfCollections.toByteArray());

    } catch (FileNotFoundException e) {
      /* Let's do something here */
      /* Just write the new json */
      FileUtils.writeByteArrayToFile(fileJson, newJsonCollection.toString().getBytes());
      FileUtils.writeByteArrayToFile(fileProtobuf, newCollectionList.toByteArray());
    }

  }

  public void exportLocally(final Shell shell) throws CoreException, InvocationTargetException, InterruptedException,
      IOException {
    final IPath basePath = createDestinationFolderStructure(this.targetDirectory);

    /* Clear directory first */
    FileUtils.deleteDirectory(basePath.toFile());

    Job job = new Job("Export locally") {

      private IStatus handleError(final Exception e) {

        Activator.log(e);

        shell.getDisplay().asyncExec(new Runnable() {
          @Override
          public void run() {
            MessageDialog.openError(shell, "Error Exporting",
                "An error occurred while exporting files. Please check Log view.");
          }
        });

        return Status.CANCEL_STATUS;
      }

      @Override
      protected IStatus run(IProgressMonitor monitor) {

        monitor.beginTask("Exporting", slootProjectProvider.entities.size() + slootProjectProvider.screens.size()
            + slootProjectProvider.textures.size());

        monitor.subTask("Exporting Sprites");

        try {

          for (IResource cgEntityRes : slootProjectProvider.entities) {
            monitor.setTaskName(cgEntityRes.getName());
            exportSprites(basePath, cgEntityRes);
            monitor.worked(1);
          }

          monitor.subTask("Exporting Screens");
          for (IResource cgScreenRes : slootProjectProvider.screens) {
            monitor.setTaskName(cgScreenRes.getName());
            exportScreens(basePath, cgScreenRes);
            monitor.worked(1);
          }

          monitor.subTask("Exportng Textures");
          for (IResource cgTextureRes : slootProjectProvider.textures) {
            File outputFile = basePath.append(cgTextureRes.getName()).toFile();
            monitor.setTaskName(cgTextureRes.getName());
            FileUtils.writeByteArrayToFile(outputFile, IOUtils.toByteArray(((IFile) cgTextureRes).getContents()));
            monitor.worked(1);
          }

          monitor.done();

        } catch (Exception e) {

          return handleError(e);

        }

        return Status.OK_STATUS;
      }
    };

    job.setUser(true);
    job.schedule();

    /* */
    generateSlootContent(basePath, targetDirectory);
  }

  public static SlootProjectProvider slootProjectProvider(String developerID, IProject project) throws CoreException {
    final SlootProjectProvider labelProvider = new SlootProjectProvider();

    labelProvider.developerID = developerID;
    labelProvider.collectionTitle = project.getName();
    labelProvider.collectionID = UUID.randomUUID().toString();

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