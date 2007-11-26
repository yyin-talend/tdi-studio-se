// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.IRoutineSynchronizer;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.preference.StatusPreferenceInitializer;

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
    @Override
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

    public RootContainer<String, IRepositoryObject> getContext() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.CONTEXT, true);
    }

    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.ROUTINES, true);
    }

    public RootContainer<String, IRepositoryObject> getSnippets() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.SNIPPETS, true);
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

    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_LDAP_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema() throws PersistenceException {
        return getObjectFromFolder(ERepositoryObjectType.METADATA_GENERIC_SCHEMA, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getRecycleBinItems()
     */
    public List<IRepositoryObject> getRecycleBinItems() throws PersistenceException {
        ERepositoryObjectType types[] = { ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.PROCESS, ERepositoryObjectType.CONTEXT, ERepositoryObjectType.SNIPPETS,
                ERepositoryObjectType.ROUTINES, ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.METADATA_LDAP_SCHEMA,
                ERepositoryObjectType.METADATA_GENERIC_SCHEMA, ERepositoryObjectType.JOBS };

        List<IRepositoryObject> deletedItems = new ArrayList<IRepositoryObject>();
        for (int i = 0; i < types.length; i++) {
            RootContainer<String, IRepositoryObject> container = getObjectFromFolder(types[i], true);
            List<IRepositoryObject> repositoryObjects = container.getAbsoluteMembers().objects();
            for (IRepositoryObject object : repositoryObjects) {
                if (object.getProperty().getItem().getState().isDeleted()) {
                    deletedItems.add(object);
                }
            }
        }
        return deletedItems;
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
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.PROCESS, ERepositoryObjectType.ROUTINES,
                ERepositoryObjectType.CONTEXT, ERepositoryObjectType.SNIPPETS, ERepositoryObjectType.METADATA_LDAP_SCHEMA,
                ERepositoryObjectType.METADATA_GENERIC_SCHEMA };
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
            if (name.equalsIgnoreCase(current.getProperty().getLabel())
                    && item.getProperty().getId() != current.getProperty().getId()) {
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

    protected Item copyFromResource(Resource createResource) throws PersistenceException, BusinessException {
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
     * @throws BusinessException
     */
    private void setPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_"; //$NON-NLS-1$
        String initialTry = add1 + originalLabel;
        copiedProperty.setLabel(initialTry);
        if (isNameAvailable(copiedProperty.getItem(), null)) {
            return;
        } else {
            char j = 'a';
            while (!isNameAvailable(copiedProperty.getItem(), null)) {
                if (j > 'z') {
                    throw new BusinessException("Cannot generate pasted item label.");
                }
                String nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                copiedProperty.setLabel(nextTry);
            }
        }
    }

    protected void createSystemRoutines() throws PersistenceException {
        ILibrariesService service = CorePlugin.getDefault().getLibrariesService();

        List<URL> routines = service.getSystemRoutines();
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

            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$
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
            if (!routineItem.getProperty().getLabel().equals(IRoutineSynchronizer.TEMPLATE)) {
                create(routineItem, path);
            }
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

    public void logOnProject(Project project) throws LoginException, PersistenceException {
        new StatusPreferenceInitializer().initializeDefaultPreferences();
    }

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        List<IRepositoryObject> jobs = repositoryFactory.getAll(ERepositoryObjectType.PROCESS, true);
        for (IRepositoryObject cur : jobs) {
            if (repositoryFactory.getStatus(cur) != ERepositoryStatus.DELETED) {
                ProcessItem item = (ProcessItem) cur.getProperty().getItem();
                List<NodeType> nodes = item.getProcess().getNode();
                for (NodeType node : nodes) {
                    List<ElementParameterType> elementParameter = node.getElementParameter();
                    for (ElementParameterType elementParam : elementParameter) {
                        if (elementParam.getField().equals(EParameterFieldType.MODULE_LIST.getName())) {
                            String uniquename = ElementParameterParser.getUNIQUENAME(node);
                            ModuleNeeded toAdd = new ModuleNeeded("Job " + item.getProperty().getLabel(),
                                    elementParam.getValue(), "Required for using component : " + uniquename + ".", true);
                            importNeedsList.add(toAdd);
                        }
                    }
                }
            }
        }

        return importNeedsList;
    }
}
