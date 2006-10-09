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
import org.talend.core.model.properties.Container;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;


/**
 * This helper class contains a set of methods to perform basic operations on FolderItem objects.
 * 
 * DOC tguiu  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class FolderHelper {
    
    private Project project;
    
    
    /**
     * DOC tguiu FolderHelper constructor comment.
     * @param project
     */
    private FolderHelper(Project project) {
        this.project = project;
    }
    
    public static FolderHelper createInstance(org.talend.core.model.general.Project project) {
        return createInstance(project.getEmfProject());
    }

    public static FolderHelper createInstance(Project project)
    {
        return new FolderHelper(project);
    }
    
    public void createSystemFolder (IPath path)
    {
        doCreateFolder(path, FolderType.SYSTEM_FOLDER_LITERAL);
    }
    public void createFolder (String path)
    {
        createFolder(new Path(path));
    }
    public void createFolder (IPath path)
    {
        doCreateFolder(path, FolderType.FOLDER_LITERAL);
    }
    private void doCreateFolder (IPath path, FolderType type)
    {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1)
            return ;
        Container folder = project;
        for (int i = 0; i < segmentCount; i++)
        {
            String segment = path.segment(i);
            FolderItem child = folder.findChildFolder(segment);
            if (child == null)
            {
                child = createFolder(project.eResource(), segment, type);
                folder.getContent().add(child);
            }
            folder = child;
        }
    }

    /**
     * DOC tguiu Comment method "createFolder".
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
    
    public void deleteFolder (IPath path)
    {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1)
            return ;
        Container folder = project;
        Container previousFolder = null;
        for (int i = 1; i < segmentCount; i++)
        {
            String segment = path.segment(i);
            FolderItem child = folder.findChildFolder(segment);
            if (child == null)
            {
                return ;
            }
            previousFolder = folder;
            folder = child;
        }
        previousFolder.getContent().remove(folder);
        project.eResource().setModified(true);
        cleanResource(project.eResource(), folder);
    }
    
    /**
     * DOC tguiu Comment method "cleanResource".
     * @param resource
     * @param folder
     */
    private void cleanResource(Resource resource, Container folder) {
        resource.getContents().remove(((Item)folder).getProperty());
        for (Object o : folder.getContent())
            cleanResource(resource, (Container)o);
    }

    private static FolderItem find(List folders, String name)
    {
        for (Iterator it = folders.iterator(); it.hasNext();)
        {
            FolderItem folder = (FolderItem)it.next();
            if (folder.getProperty().getLabel().equals(name))
                return folder;
        }
        return null;
    }
    
    public Set<IPath> listFolders ()
    {
        HashSet<IPath> folders = new HashSet<IPath>();
        list(folders, project.getContent(), new Path(""));
        return folders;
    }

    /**
     * DOC tguiu Comment method "list".
     * @param content
     * @param folders2
     */
    private void list(HashSet<IPath> collector, EList content, IPath currentPath) {
        for (Object o : content)
        {
            if (o instanceof FolderItem) {
                FolderItem folder = (FolderItem)o;
                IPath folderPath = currentPath.append(folder.getProperty().getLabel());
                collector.add(folderPath);
                list(collector, folder.getChildren(), folderPath);
            }            
        }
    }

    public Container getFolder(String pathStr) {
         return getFolder(new Path(pathStr));
    }
    
    public Container getFolder(IPath path) {
        int segmentCount = path.segmentCount();
        Container folder = project;
        for (int i = 0; i < segmentCount; i++)
        {
            String segment = path.segment(i);
            FolderItem child = folder.findChildFolder(segment);
            if (child == null)
            {
                return null;
            }
            folder = child;
        }
        return folder;
    }
    
    public void moveFolder(String completeOldPath, String completeNewPath) {
        Container folder = getFolder(completeOldPath);
        if (folder == null)
            return ;
        Container destination = getFolder(new Path(completeNewPath).removeLastSegments(1));
        if (destination == null)
            return ;
        destination.getContent().add(folder);
        
    }

    /**
     * DOC tguiu Comment method "renameFolder".
     * @param completePath
     * @param label
     */
    public void renameFolder(String completePath, String label) {
        Container folder = getFolder(completePath);
        if (folder != null && folder instanceof FolderItem)
        {
            ((FolderItem)folder).getProperty().setLabel(label);
        }
        
    }

 
}
