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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.perlmodule.IPerlModuleService;

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
        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();

        ERepositoryObjectType[] repositoryObjectTypeList = new ERepositoryObjectType[] { ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.PROCESS, ERepositoryObjectType.ROUTINES };
        for (ERepositoryObjectType repositoryObjectType : repositoryObjectTypeList) {
            Object folder = getFolder(project, repositoryObjectType);
            toReturn.addAll(getSerializableFromFolder(folder, id, repositoryObjectType, allVersion, true, true));
        }
        return toReturn;
    }

    protected abstract Object getFolder(Project project, ERepositoryObjectType repositoryObjectType) throws PersistenceException;

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
        List<IRepositoryObject> list = getAll(type, true);

        for (IRepositoryObject current : list) {
            if (name.equalsIgnoreCase(current.getProperty().getLabel()) && item.getProperty().getId() != current.getProperty().getId()) {
                return false;
            }
        }
        return true;
    }

    protected abstract List<IRepositoryObject> getSerializableFromFolder(Object folder, String id, ERepositoryObjectType type,
            boolean allVersion, boolean searchInChildren, boolean withDeleted) throws PersistenceException;

    protected abstract <K, T> RootContainer<K, T> getObjectFromFolder(ERepositoryObjectType type, boolean onlyLastVersion)
            throws PersistenceException;

    protected abstract <K, T> void addFolderMembers(ERepositoryObjectType type, Container<K, T> toReturn, Object objectFolder,
            boolean onlyLastVersion) throws PersistenceException;

    protected abstract FolderHelper getFolderHelper(org.talend.core.model.properties.Project emfProject);

    protected Item copyFromResource(Resource createResource) throws PersistenceException {
        Item newItem = (Item) EcoreUtil.getObjectByType(createResource.getContents(), PropertiesPackage.eINSTANCE.getItem());
        Property property = newItem.getProperty();
        property.setId(getNextId());
        property.setAuthor(getRepositoryContext().getUser());
        setPropNewName(property);
        EcoreUtil.resolveAll(createResource);
        return newItem;
    }

    /**
     * DOC smallet Comment method "getCopiedLabel".
     * 
     * @param copiedProperty
     * @return
     * @throws PersistenceException
     */
    private void setPropNewName(Property copiedProperty) throws PersistenceException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_";
        String initialTry = add1 + originalLabel;
        copiedProperty.setLabel(initialTry);
        if (isNameAvailable(copiedProperty.getItem(), null)) {
            return;
        } else {
            int i = 2;
            while (!isNameAvailable(copiedProperty.getItem(), null)) {
                String nextTry = initialTry + "_(" + (i++) + ")";
                copiedProperty.setLabel(nextTry);
            }
        }
    }

    protected void createSystemRoutines() throws PersistenceException {
        IPerlModuleService service = (IPerlModuleService) GlobalServiceRegister.getDefault().getService(IPerlModuleService.class);
        List<URL> routines = service.getBuiltInRoutines();
        Path path = new Path(RepositoryConstants.SYSTEM_DIRECTORY);
        for (URL url : routines) {
            createRoutine(url, path);
        }
    }

    /**
     * DOC smallet Comment method "createRoutine".
     * 
     * @param url
     * @throws PersistenceException
     */
    private void createRoutine(URL url, IPath path) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());
    
            String[] fragments = url.toString().split("/");
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\.");
            property.setLabel(tmp[0]);
    
            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);
    
            RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            routineItem.setProperty(property);
            routineItem.setContent(byteArray);
            routineItem.setBuiltIn(true);
            create(routineItem, path);
        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

}
