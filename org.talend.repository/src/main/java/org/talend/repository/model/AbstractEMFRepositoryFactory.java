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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.CSVFileConnectionItem;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.properties.util.PropertiesSwitch;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractEMFRepositoryFactory extends AbstractRepositoryFactory implements IRepositoryFactory {

    /**
     * Generates the next id for serializable. If no serializable returns 0.
     * 
     * @param project the project to scan
     * 
     * @return the next id for the project
     * @throws PersistenceException
     * @throws PersistenceException if processes cannot be retrieved
     */
    public String getNextId() {
        return EcoreUtil.generateUUID();
    }

    private void collect(RootContainer<String, IRepositoryObject> rootContainer, List<ConnectionItem> result)
            throws PersistenceException {
        for (IRepositoryObject repositoryObject : rootContainer.getAbsoluteMembers().objects()) {
            ConnectionItem connectionItem = (ConnectionItem) repositoryObject.getProperty().getItem();
            if (getStatus(connectionItem) != ERepositoryStatus.DELETED) {
                result.add(connectionItem);
            }
        }
    }

    // gather all the metadata connections (file / db / etc ...)
    public List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException {
        List<ConnectionItem> result = new ArrayList<ConnectionItem>();

        collect(getMetadataFileDelimited(), result);
        collect(getMetadataFilePositional(), result);
        collect(getMetadataFileRegexp(), result);
        collect(getMetadataFileXml(), result);
        collect(getMetadataFileLdif(), result);
        collect(getMetadataConnection(), result);

        return result;
    }

    public RootContainer<String, IRepositoryObject> getDocumentation() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.DOCUMENTATION, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataConnection() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_CONNECTIONS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_FILE_DELIMITED, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFilePositional() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_FILE_POSITIONAL, true);
    }

    public RootContainer<String, IRepositoryObject> getProcess() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.PROCESS, true);
    }

    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.ROUTINES, true);
    }

    public RootContainer<String, IRepositoryObject> getBusinessProcess() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.BUSINESS_PROCESS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_FILE_REGEXP, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileXml() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_FILE_XML, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileLdif() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_FILE_LDIF, true);
    }

    /**
     * DOC smallet Comment method "convert".
     * 
     * @param toReturn
     * @param serializableAllVersion
     */
    protected List<IRepositoryObject> convert(List<IRepositoryObject> serializableAllVersion) {
        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();
        for (IRepositoryObject current : serializableAllVersion) {
            toReturn.add(current);
        }
        return toReturn;
    }

    protected List<IRepositoryObject> getSerializable(Project project, String id, boolean allVersion) throws PersistenceException {
        IProject fsProject = ResourceModelUtils.getProject(project);

        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();

        ERepositoryObjectType[] repositoryObjectTypeList = new ERepositoryObjectType[] { ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.PROCESS, ERepositoryObjectType.ROUTINES };
        for (ERepositoryObjectType repositoryObjectType : repositoryObjectTypeList) {
            IFolder folder = ResourceUtils.getFolder(fsProject, ERepositoryObjectType.getFolderName(repositoryObjectType), true);
            toReturn.addAll(getSerializableFromFolder(folder, id, repositoryObjectType, allVersion, true));
        }
        return toReturn;
    }

    public List<IRepositoryObject> getAllVersion(String id) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = getSerializable(getRepositoryContext().getProject(), id, true);
        return convert(serializableAllVersion);
    }

    public boolean isNameAvailable(Item item, String name) throws PersistenceException {
        if (name == null) {
            name = item.getProperty().getLabel();
        }

        if (item instanceof FolderItem) {
            FolderHelper folderHelper = getFolderHelper(getRepositoryContext().getProject().getEmfProject());
            return !folderHelper.pathExists((FolderItem) item, name);
        }

        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return false;
        }
        List<IRepositoryObject> list = getAll(type);

        for (IRepositoryObject current : list) {
            if (name.equals(current.getProperty().getLabel()) && item.getProperty().getId() != current.getProperty().getId()) {
                return false;
            }
        }
        return true;
    }

    

    protected abstract List<IRepositoryObject> getSerializableFromFolder(Object folder, String id, ERepositoryObjectType type,
            boolean allVersion, boolean searchInChildren) throws PersistenceException;

    protected abstract <K, T> RootContainer<K, T> getObjectFromFolder(ERepositoryObjectType type, boolean onlyLastVersion)
            throws PersistenceException;

    protected abstract <K, T> void addFolderMembers(ERepositoryObjectType type, Container<K, T> toReturn, Object objectFolder,
            boolean onlyLastVersion) throws PersistenceException;

    protected abstract FolderHelper getFolderHelper(org.talend.core.model.properties.Project emfProject);

}
