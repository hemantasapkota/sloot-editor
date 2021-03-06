package com.laex.cg2d.core.popup.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import com.laex.cg2d.core.popup.actions.UploadToSpritesProjectProvider.SlootProjectProvider;
import com.laex.cg2d.model.ICGCProject;
import com.laex.cg2d.model.ScreenModel.CGEditorShapeType;
import com.laex.cg2d.model.ScreenModel.CGEntity;
import com.laex.cg2d.model.ScreenModel.CGEntityAnimation;
import com.laex.cg2d.model.ScreenModel.CGEntityCollisionType;
import com.laex.cg2d.model.ScreenModel.CGLayer;
import com.laex.cg2d.model.ScreenModel.CGResourceFile;
import com.laex.cg2d.model.ScreenModel.CGScreenModel;
import com.laex.cg2d.model.ScreenModel.CGShape;
import com.laex.cg2d.model.SlootContent.SlootCollection;
import com.laex.cg2d.model.SlootContent.SlootItem;
import com.laex.cg2d.model.adapter.EntityAdapter;
import com.laex.cg2d.model.model.Entity;
import com.laex.cg2d.model.util.EntitiesUtil;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.multipart.FilePart;

public class UploadToSpritesSlootController {

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

        IPath trimmedBasePath = basePath.removeFirstSegments(basePath.segmentCount() - 1);

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
              String rewrittenPath = basePath.removeFirstSegments(basePath.segmentCount() - 1).append(lastSegment)
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

              /*
               * Background Resource File: has extension PNG, we dont append
               * JSON extend to this file
               */
              String rewrittenPath = basePath.removeFirstSegments(basePath.segmentCount() - 1).append(lastSegment)
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

  private String generateSlootContent(IPath basePath, String targetDir) throws IOException, CoreException {

    /*
     * We write sloot-content.json to a directory one step outside of the
     * collection
     */
    File fileJson = basePath.removeLastSegments(1).append("sloot-content.json").toFile();
    File fileProtobuf = basePath.removeLastSegments(1).append("sloot-content.cmf").toFile();
    IPath imgFileBasePath = basePath.removeFirstSegments(basePath.segmentCount() - 1); 

    final SlootCollection.Builder builder = SlootCollection.newBuilder().setCollectionTitle(slootProjectProvider.collectionTitle)
        .setDeveloperId(slootProjectProvider.developerID);

    /* Build sloot content */
    for (IResource resource : slootProjectProvider.entities) {
      String imgUrlPath = imgFileBasePath.append(resource.getName()).addFileExtension(ICGCProject.PNG_EXTENSION)
          .toOSString();
      String title = resource.getLocation().removeFileExtension().lastSegment();

      String id = toExport.getName() + "_" + resource.getName();
      SlootItem si = SlootItem.newBuilder().setId(id).setImgUrl(imgUrlPath).setTitle(title).setPrice(0).build();
      builder.addSlootItems(si);
    }

    SlootCollection collection = builder.build();
    String newJsonCollection = JsonFormat.printToString(collection);
    FileUtils.writeByteArrayToFile(fileJson, newJsonCollection.toString().getBytes());
    FileUtils.writeByteArrayToFile(fileProtobuf, collection.toByteArray());

    return newJsonCollection;
  }

  public void exportLocally(final String baseUrl, final Shell shell) throws CoreException, InvocationTargetException, InterruptedException,
      IOException {
    final IPath basePath = createDestinationFolderStructure(this.targetDirectory);

    /* Clear directory first */
    FileUtils.deleteDirectory(basePath.toFile());

    toExport.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

    Job job = new Job("Upload to SpritesLoot") {

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

          monitor.subTask("Generating Sloot Content");
          String slootContentJsonFile = generateSlootContent(basePath, targetDirectory);
          monitor.worked(1);

          monitor.subTask("Compressing folders");
          File zipFile = zipFolder(toExport.getLocation().append("tmp").append(slootProjectProvider.collectionTitle));

          monitor.subTask("Refreshing filesystem");
          toExport.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

          /* Finally upload */
          monitor.subTask("Uploading....");
          upload(baseUrl, zipFile, slootContentJsonFile);
          monitor.worked(1);

          monitor.done();

        } catch (Exception e) {
          return handleError(e);
        }

        return Status.OK_STATUS;
      }
    };

    job.setUser(true);
    job.schedule();
  }

  BoundRequestBuilder clientBuilder;

  private File zipFolder(IPath folderPath) throws CoreException {
    Project p = new Project();
    p.init();
    Zip zip = new Zip();
    zip.setProject(p);

    IPath zipFilePath = folderPath.addFileExtension("zip");

    zip.setBasedir(folderPath.toFile());
    zip.setDestFile(zipFilePath.toFile());
    zip.setIncludes("**");
    zip.perform();

    return zipFilePath.toFile();
  }

  private void upload(String baseUrl, File zipFile, String slootContentJson) throws IllegalArgumentException, IOException,
      CoreException {
    final AsyncHttpClient client = new AsyncHttpClient();

    /* Send the Sloot Content */
    AsyncCompletionHandler<String> handler = new AsyncCompletionHandler<String>() {
      @Override
      public String onCompleted(Response resp) throws Exception {
        System.err.println("Response: " + resp.getResponseBody());
        return null;
      }
    };

    String url = String.format("http://%s/api/slootContent", baseUrl); 
    client.preparePost(url).addHeader("Content-Type", "application/json")
        .setBody(slootContentJson).execute(handler);

    /* Send all the generated files */
    IPath genPath = toExport.getFullPath().append("tmp").append(slootProjectProvider.collectionTitle);
    IFolder tmpFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(genPath);
    final String putUrl = url + "/" + slootProjectProvider.collectionTitle;

    if (!tmpFolder.exists()) {
      return;
    }

    FilePart zipFilePart = new FilePart(zipFile.getName(), zipFile);

    client.preparePut(putUrl).addHeader("Content-Type", "multipart/form-data").addBodyPart(zipFilePart).execute();

  }
}