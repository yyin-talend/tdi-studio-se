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
package org.talend.repository.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.repository.i18n.Messages;

/**
 * This helper class contains a set of methods to perform basic operations on FolderItem objects.
 * 
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class FolderHelper {

    protected Project project;
    private User connectedUser;

    /**
     * DOC tguiu FolderHelper constructor comment.
     * 
     * @param project
     */
    protected FolderHelper(Project project, User connectedUser) {
        this.project = project;
        this.connectedUser = connectedUser;
    }

    public void createSystemFolder(IPath path) {
        doCreateFolder(path, FolderType.SYSTEM_FOLDER_LITERAL);
    }

    public FolderItem createFolder(String path) {
        createFolder(new Path(path));
        return getFolder(path);
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
                child = PropertiesFactory.eINSTANCE.createFolderItem();
                child.setType(type);

                Property property = PropertiesFactory.eINSTANCE.createProperty();
                property.setId(EcoreUtil.generateUUID());
                property.setLabel(segment);
                property.setVersion(VersionUtils.DEFAULT_VERSION);
                property.setCreationDate(new Date());
                property.setAuthor(connectedUser);

                child.setProperty(property);

                createItemState(child);
                doCreateFolder(child);
                
                if (folder == null) {
                    project.getFolders().add(child);
                } else {
                    folder.getChildren().add(child);
                }
            }
            folder = child;
        }
    }

    protected abstract void doCreateFolder(FolderItem folderItem);

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
        removeFromResource(folder);
    }

    protected abstract void removeFromResource(FolderItem folder);

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
        list(folders, project.getFolders(), new Path("")); //$NON-NLS-1$
        return folders;
    }

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

    /**
     * Returns <code>true</code> if a folder at the same level of <code>folder</code> with a different id and wich
     * label is <code>label</code> exists.
     * 
     * @param folder
     * @param label
     * @return
     */
    public boolean pathExists(FolderItem folder, String label) {
        for (Object child : ((FolderItem) folder.eContainer()).getChildren()) {
            if (child instanceof FolderItem) {
                FolderItem folderChild = (FolderItem) child;
                if (!folderChild.getProperty().getId().equals(folder.getProperty().getId())
                        && folderChild.getProperty().getLabel().equals(label)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createItemState(FolderItem folder) {
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        folder.setState(itemState);
        doCreateItemState(folder);
    }

    public abstract void doCreateItemState(FolderItem folder);

}
