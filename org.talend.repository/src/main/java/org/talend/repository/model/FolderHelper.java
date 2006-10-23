// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.repository.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;

/**
 * This helper class contains a set of methods to perform basic operations on FolderItem objects.
 * 
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FolderHelper {

    private Project project;

    /**
     * DOC tguiu FolderHelper constructor comment.
     * 
     * @param project
     */
    private FolderHelper(Project project) {
        this.project = project;
    }

    public static FolderHelper createInstance(org.talend.core.model.general.Project project) {
        return createInstance(project.getEmfProject());
    }

    public static FolderHelper createInstance(Project project) {
        return new FolderHelper(project);
    }

    public void createSystemFolder(IPath path) {
        doCreateFolder(path, FolderType.SYSTEM_FOLDER_LITERAL);
    }

    public String createFolder(String path) {
        createFolder(new Path(path));
        return getFolder(path).getProperty().getId();
    }

    public void createFolder(IPath path) {
        doCreateFolder(path, FolderType.FOLDER_LITERAL);
    }

    private void doCreateFolder(IPath path, FolderType type) {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1) {
            return;
        }
        FolderItem folder = null;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                child = createFolder(project.eResource(), segment, type);
                if (folder == null) {
                    project.getFolders().add(child);
                } else {
                    folder.getChildren().add(child);
                }
            }
            folder = child;
        }
    }

    /**
     * DOC tguiu Comment method "createFolder".
     * 
     * @param resource
     * @param name
     * @param type
     * @return
     */
    private FolderItem createFolder(Resource resource, String name, FolderType type) {
        FolderItem folder;
        folder = PropertiesFactory.eINSTANCE.createFolderItem();
        folder.setType(type);
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        resource.getContents().add(property);
        property.setId(EcoreUtil.generateUUID());
        property.setLabel(name);
        folder.setProperty(property);
        resource.setModified(true);
        return folder;
    }

    public void deleteFolder(String completePath) {
        deleteFolder(new Path(completePath));
    }

    public void deleteFolder(IPath path) {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1) {
            return;
        }
        FolderItem folder = null;
        FolderItem previousFolder = null;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                return;
            }
            previousFolder = folder;
            folder = child;
        }
        if (previousFolder == null) {
            project.getFolders().remove(folder);
        } else {
            previousFolder.getChildren().remove(folder);
        }
        project.eResource().setModified(true);
        cleanResource(project.eResource(), folder);
    }

    /**
     * DOC tguiu Comment method "cleanResource".
     * 
     * @param resource
     * @param folder
     */
    private void cleanResource(Resource resource, FolderItem folder) {
        resource.getContents().remove(folder.getProperty());
        for (Object o : folder.getChildren()) {
            cleanResource(resource, (FolderItem) o);
        }
    }

    private static FolderItem find(List folders, String name) {
        for (Iterator it = folders.iterator(); it.hasNext();) {
            FolderItem folder = (FolderItem) it.next();
            if (folder.getProperty().getLabel().equals(name)) {
                return folder;
            }
        }
        return null;
    }

    public Set<IPath> listFolders() {
        HashSet<IPath> folders = new HashSet<IPath>();
        list(folders, project.getFolders(), new Path(""));
        return folders;
    }

    /**
     * DOC tguiu Comment method "list".
     * 
     * @param content
     * @param folders2
     */
    private void list(HashSet<IPath> collector, EList content, IPath currentPath) {
        for (Object o : content) {
            if (o instanceof FolderItem) {
                FolderItem folder = (FolderItem) o;
                IPath folderPath = currentPath.append(folder.getProperty().getLabel());
                collector.add(folderPath);
                list(collector, folder.getChildren(), folderPath);
            }
        }
    }

    public FolderItem getFolder(String pathStr) {
        return getFolder(new Path(pathStr));
    }

    public FolderItem getFolder(IPath path) {
        int segmentCount = path.segmentCount();
        FolderItem folder = null;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                return null;
            }
            folder = child;
        }
        return folder;
    }

    public void moveFolder(String completeOldPath, String completeNewPath) {
        FolderItem folder = getFolder(completeOldPath);
        if (folder == null) {
            return;
        }
        FolderItem destination = getFolder(new Path(completeNewPath).removeLastSegments(1));
        if (destination == null) {
            return;
        }
        destination.getChildren().add(folder);

    }

    public void renameFolder(String completePath, String label) {
        FolderItem folder = getFolder(completePath);
        if (folder != null) {
            folder.getProperty().setLabel(label);
        }
    }

    public FolderItem findChildFolder(FolderItem parent, String name) {
        EList children;
        if (parent != null) {
            children = parent.getChildren();
        } else {
            children = project.getFolders();
        }

        for (Iterator it = children.iterator(); it.hasNext();) {
            Item item = (Item) it.next();
            if (item instanceof FolderItem && item.getProperty().getLabel().equals(name)) {
                return (FolderItem) item;
            }
        }
        return null;
    }
}
