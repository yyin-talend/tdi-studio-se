// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.ExternalCrossReferencer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.helper.ByteArrayResource;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.constants.FileConstants;
import org.talend.repository.ui.wizards.exportjob.IFileExporterFullPath;
import org.talend.repository.ui.wizards.exportjob.TarFileExporterFullPath;
import org.talend.repository.ui.wizards.exportjob.ZipFileExporterFullPath;

/***/
public class ExportItemUtil {

    private ResourceSet resourceSet;

    private Resource projectResource;

    private Resource propertyResource;

    private Resource itemResource;

    private File projectFile;

    private File propertyFile;

    private File itemFile;

    private IPath projectPath;

    private IPath propertyPath;

    private IPath itemPath;

    public void exportItems(File destination, Collection<Item> items) throws Exception {
        IFileExporterFullPath exporter = null;
        File tmpDirectory = null;
        Map<File, IPath> toExport;

        if (destination.getName().endsWith(".tar")) {
            createFolder(destination.getParentFile());
            exporter = new TarFileExporterFullPath(destination.getAbsolutePath(), false);
        } else if (destination.getName().endsWith(".tar.gz")) {
            createFolder(destination.getParentFile());
            exporter = new TarFileExporterFullPath(destination.getAbsolutePath(), true);
        } else if (destination.getName().endsWith(".zip")) {
            createFolder(destination.getParentFile());
            exporter = new ZipFileExporterFullPath(destination.getAbsolutePath(), true);
        } else {
            createFolder(destination);
        }

        try {
            if (exporter != null) {
                tmpDirectory = createTmpDirectory();
            }

            try {
                if (exporter != null) {
                    toExport = exportItems(items, tmpDirectory, true);

                    // in case of .tar.gz we remove extension twice
                    IPath rootPath = new Path(destination.getName()).removeFileExtension().removeFileExtension();
                    for (File file : toExport.keySet()) {
                        IPath path = toExport.get(file);
                        exporter.write(file.getAbsolutePath(), rootPath.append(path).toString());
                    }
                } else {
                    toExport = exportItems(items, destination, true);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (exporter != null) {
                    deleteTmpDirectory(tmpDirectory);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (exporter != null) {
                try {
                    exporter.finished();
                } catch (Exception e) {
                    // ignore me
                }
            }
        }
    }

    public Set<File> createLocalResources(File destinationDirectory, Item item) throws Exception {
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        Map<File, IPath> exportItems = exportItems(items, destinationDirectory, false);

        return exportItems.keySet();
    }

    private Map<File, IPath> exportItems(Collection<Item> items, File destinationDirectory,
            boolean projectFolderStructure) throws Exception {
        Map<File, IPath> toExport = new HashMap<File, IPath>();

        try {
            init();
            computeProjectFileAndPath(destinationDirectory);
            createProjectResource();
            for (Item item : items) {
                computeItemFilesAndPaths(destinationDirectory, item, projectFolderStructure);
                createItemResources(item);
                fixItemUserReferences();
                fixItemLockState();
                toExport.put(propertyFile, propertyPath);
                toExport.put(itemFile, itemPath);
            }
            toExport.put(projectFile, projectPath);
            dereferenceNotContainedObjects();
            saveResources();
        } catch (Exception e) {
            throw e;
        } finally {
            cleanResources();
        }

        return toExport;
    }

    private File createTmpDirectory() throws IOException {
        File tmpDirectory = null;
        int suffix = 0;
        while (tmpDirectory == null || tmpDirectory.exists()) {
            tmpDirectory = new File(SystemUtils.getJavaIoTmpDir(), "talendExportItems" + suffix);
            suffix++;
        }

        if (!tmpDirectory.mkdir()) {
            throw new IOException("cannot create " + tmpDirectory);
        }

        return tmpDirectory;
    }

    private void deleteTmpDirectory(File tmpDirectory) {
        if (tmpDirectory.isFile()) {
            tmpDirectory.delete();
        } else {
            for (File file : tmpDirectory.listFiles()) {
                deleteTmpDirectory(file);
            }
            tmpDirectory.delete();
        }
    }

    private void computeProjectFileAndPath(File destinationFile) {
        projectPath = new Path(FileConstants.LOCAL_PROJECT_FILENAME);
        projectFile = new File(destinationFile, projectPath.toOSString());
    }

    private void computeItemFilesAndPaths(File destinationFile, Item item, boolean projectFolderStructure) {
        IPath fileNamePath = new Path(ResourceFilenameHelper.getExpectedFileName(item.getProperty().getLabel(), item
                .getProperty().getVersion()));

        if (projectFolderStructure) {
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            IPath typeFolderPath = new Path(ERepositoryObjectType.getFolderName(itemType));
            IPath itemDestinationPath = typeFolderPath.append(item.getProperty().getItem().getState().getPath());
            fileNamePath = itemDestinationPath.append(fileNamePath);
        }

        propertyPath = fileNamePath.addFileExtension(FileConstants.PROPERTIES_EXTENSION);
        propertyFile = new File(destinationFile, propertyPath.toOSString());

        itemPath = fileNamePath.addFileExtension(FileConstants.ITEM_EXTENSION);
        itemFile = new File(destinationFile, itemPath.toOSString());
    }

    private void init() {
        resourceSet = new ResourceSetImpl();
    }

    private void createProjectResource() {
        Context ctx = CorePlugin.getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);

        EObject projectCopy = EcoreUtil.copy(repositoryContext.getProject().getEmfProject());

        User exportUser = PropertiesFactory.eINSTANCE.createUser();
        exportUser.setLogin("exportUser@talend.com");

        projectResource = createResource(projectFile, false);
        projectResource.getContents().add(projectCopy);
        projectResource.getContents().add(exportUser);
    }

    private void createItemResources(Item item) {
        Collection<EObject> copiedObjects = copyObjects(item);

        propertyResource = createResource(propertyFile, false);
        moveObjectsToResource(propertyResource, copiedObjects, PropertiesPackage.eINSTANCE.getProperty());
        moveObjectsToResource(propertyResource, copiedObjects, PropertiesPackage.eINSTANCE.getItemState());
        moveObjectsToResource(propertyResource, copiedObjects, PropertiesPackage.eINSTANCE.getItem());

        boolean isFileItem = PropertiesPackage.eINSTANCE.getFileItem().isSuperTypeOf(item.eClass());
        itemResource = createResource(itemFile, isFileItem);
        moveObjectsToResource(itemResource, copiedObjects, null);
    }

    private Resource createResource(File file, boolean byteArrayResource) {
        URI uri = URI.createFileURI(file.getAbsolutePath());
        if (byteArrayResource) {
            Resource resource = new ByteArrayResource(uri);
            resourceSet.getResources().add(resource);
            return resource;
        } else {
            return resourceSet.createResource(uri);
        }
    }

    private void saveResources() throws IOException {
        for (Resource resource : resourceSet.getResources()) {
            resource.save(Collections.EMPTY_MAP);
        }
    }

    private void cleanResources() {
        for (Resource resource : resourceSet.getResources()) {
            resource.unload();
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<EObject> copyObjects(Item item) {
        List<EObject> objects = new ArrayList<EObject>();

        objects.add(item);
        EList references = item.eClass().getEAllReferences();
        for (Iterator iter = references.iterator(); iter.hasNext();) {
            EReference reference = (EReference) iter.next();
            if (!reference.isTransient()) {
                if (reference.isMany()) {
                    EList referencedEList = (EList) item.eGet(reference);
                    for (Iterator iterator = referencedEList.iterator(); iterator.hasNext();) {
                        EObject referenceEObject = (EObject) iterator.next();
                        if (referenceEObject != null) {
                            objects.add(referenceEObject);
                        }
                    }
                } else {
                    EObject referenceEObject = (EObject) item.eGet(reference);
                    if (referenceEObject != null) {
                        objects.add(referenceEObject);
                    }
                }
            }
        }

        return EcoreUtil.copyAll(objects);
    }

    private void moveObjectsToResource(Resource resource, Collection<EObject> objects, EClass type) {
        Collection<EObject> objectsToTransfer;
        if (type != null) {
            objectsToTransfer = EcoreUtil.getObjectsByType(objects, type);
        } else {
            objectsToTransfer = objects;
        }
        resource.getContents().addAll(objectsToTransfer);
        objects.removeAll(objectsToTransfer);
    }

    private void fixItemUserReferences() {
        User exportUser = (User) EcoreUtil.getObjectByType(projectResource.getContents(), PropertiesPackage.eINSTANCE
                .getUser());
        Item item = (Item) EcoreUtil.getObjectByType(propertyResource.getContents(), PropertiesPackage.eINSTANCE
                .getItem());
        item.getProperty().setAuthor(exportUser);
    }

    private void fixItemLockState() {
        Item item = (Item) EcoreUtil.getObjectByType(propertyResource.getContents(), PropertiesPackage.eINSTANCE
                .getItem());
        item.getState().setLocker(null);
        item.getState().setLockDate(null);
        item.getState().setLocked(false);
    }

    @SuppressWarnings("unchecked")
    private void dereferenceNotContainedObjects() {
        Map<EObject, Collection<Setting>> externalObjects = ExternalCrossReferencer.find(resourceSet);

        for (EObject object : externalObjects.keySet()) {
            Collection<Setting> collection = externalObjects.get(object);
            for (Setting setting : collection) {
                if (setting.getEStructuralFeature().isMany()) {
                    EList referencedEList = (EList) setting.getEObject().eGet(setting.getEStructuralFeature());
                    referencedEList.clear();
                } else {
                    setting.getEObject().eSet(setting.getEStructuralFeature(), null);
                }
            }
        }
    }

    private void createFolder(File folder) throws IOException {
        folder.mkdirs();
        if (!folder.exists()) {
            throw new IOException("unable to create directory '" + folder + "'");
        }
    }
}
