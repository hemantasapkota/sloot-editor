package com.laex.cg2d.core.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import com.laex.cg2d.model.ICGCProject;

public class UploadToSpritesProjectProvider {

  public static class SlootProjectProvider {
    public String developerID = "";
    public String collectionTitle = "";

    public List<IResource> entities = new ArrayList<IResource>();
    public List<IResource> screens = new ArrayList<IResource>();
    public List<IResource> textures = new ArrayList<IResource>();
  }

  public static SlootProjectProvider slootProjectProvider(String developerID, IProject project) throws CoreException {
    final SlootProjectProvider labelProvider = new SlootProjectProvider();

    labelProvider.developerID = developerID;
    labelProvider.collectionTitle = project.getName();

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
