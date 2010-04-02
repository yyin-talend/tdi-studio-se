// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.routines.RoutineLibraryMananger;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
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

    public RootContainer<String, IRepositoryObject> getDocumentation(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.DOCUMENTATION, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataConnection(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_CONNECTIONS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSAPConnection(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_SAPCONNECTIONS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataEBCDIC(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_EBCDIC, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataMDM(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_MDMCONNECTION, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataRules(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_RULES, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_DELIMITED, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFilePositional(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_POSITIONAL, true);
    }

    public RootContainer<String, IRepositoryObject> getProcess(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.PROCESS, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getJoblets()
     */
    public RootContainer<String, IRepositoryObject> getJoblets(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.JOBLET, true);
    }

    public RootContainer<String, IRepositoryObject> getContext(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.CONTEXT, true);
    }

    public RootContainer<String, IRepositoryObject> getRoutine(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.ROUTINES, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSQLPattern(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.SQLPATTERNS, true);
    }

    public RootContainer<String, IRepositoryObject> getSnippets(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.SNIPPETS, true);
    }

    public RootContainer<String, IRepositoryObject> getBusinessProcess(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.BUSINESS_PROCESS, true);
    }

    public RootContainer<String, IRepositoryObject> getSVGBusinessProcess(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.SVG_BUSINESS_PROCESS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_REGEXP, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileXml(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_XML, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileLdif(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_LDIF, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileExcel(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_EXCEL, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSalesforceSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_LDAP_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_GENERIC_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataWSDLSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_WSDL_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataHL7(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_HL7, true);
    }

    // MOD sgandon 31/03/2010 : moved from local variable to static variable for optimisation purpose.
    static final ERepositoryObjectType[] REPOSITORY_OBJECT_TYPE_LIST = new ERepositoryObjectType[] {
            ERepositoryObjectType.PROCESS, ERepositoryObjectType.JOBLET, ERepositoryObjectType.METADATA_CONNECTIONS,
            ERepositoryObjectType.METADATA_SAPCONNECTIONS, ERepositoryObjectType.SQLPATTERNS,
            ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
            ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
            ERepositoryObjectType.METADATA_FILE_EXCEL, ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.ROUTINES,
            ERepositoryObjectType.CONTEXT, ERepositoryObjectType.METADATA_LDAP_SCHEMA,
            ERepositoryObjectType.METADATA_GENERIC_SCHEMA, ERepositoryObjectType.METADATA_WSDL_SCHEMA,
            ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA, ERepositoryObjectType.METADATA_FILE_EBCDIC,
            ERepositoryObjectType.METADATA_FILE_RULES, ERepositoryObjectType.METADATA_MDMCONNECTION,
            ERepositoryObjectType.BUSINESS_PROCESS, ERepositoryObjectType.SVG_BUSINESS_PROCESS,
            ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.SNIPPETS, ERepositoryObjectType.METADATA_FILE_HL7 };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getRecycleBinItems()
     */
    public List<IRepositoryObject> getRecycleBinItems(Project project) throws PersistenceException {
        ERepositoryObjectType types[] = { ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_SAPCONNECTIONS, ERepositoryObjectType.SQLPATTERNS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.PROCESS, ERepositoryObjectType.CONTEXT, ERepositoryObjectType.SNIPPETS,
                ERepositoryObjectType.ROUTINES, ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.METADATA_FILE_EXCEL,
                ERepositoryObjectType.METADATA_LDAP_SCHEMA, ERepositoryObjectType.METADATA_GENERIC_SCHEMA,
                ERepositoryObjectType.METADATA_WSDL_SCHEMA, ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA,
                ERepositoryObjectType.JOBLET, ERepositoryObjectType.METADATA_FILE_EBCDIC,
                ERepositoryObjectType.METADATA_FILE_RULES, ERepositoryObjectType.METADATA_FILE_HL7,
                ERepositoryObjectType.METADATA_MDMCONNECTION };

        List<IRepositoryObject> deletedItems = new ArrayList<IRepositoryObject>();
        for (int i = 0; i < types.length; i++) {
            RootContainer<String, IRepositoryObject> container = getObjectFromFolder(project, types[i], true);
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

    Map<String, String> lastFolderForItemMap = new HashMap<String, String>();

    Map<String, ERepositoryObjectType> lastRepositoryTypeForItemMap = new HashMap<String, ERepositoryObjectType>();

    protected void addToHistory(String id, ERepositoryObjectType itemType, String path) {
        lastFolderForItemMap.put(id, path);
        lastRepositoryTypeForItemMap.put(id, itemType);
    }

    protected List<IRepositoryObject> getSerializable(Project project, String id, boolean allVersion) throws PersistenceException {
        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();

        if (lastFolderForItemMap.containsKey(id)) {
            ERepositoryObjectType itemType = lastRepositoryTypeForItemMap.get(id);
            String currentPath = lastFolderForItemMap.get(id);
            Object fullFolder = getFullFolder(project, itemType, currentPath);

            try {
                if (fullFolder != null) {
                    List<IRepositoryObject> itemsFound = getSerializableFromFolder(project, fullFolder, id, itemType, allVersion,
                            false, true);
                    if (!itemsFound.isEmpty()) { // add for items in recycle-bin
                        toReturn.addAll(itemsFound);
                        return toReturn;
                    }
                }
            } catch (PersistenceException e) {
                // do nothing.
                // if any exception happen or can't find the item, just try to look for it everywhere.
            }
        }

        // added
        for (ERepositoryObjectType repositoryObjectType : REPOSITORY_OBJECT_TYPE_LIST) {
            Object folder = getFolder(project, repositoryObjectType);
            if (folder != null) {
                List<IRepositoryObject> itemsFound = getSerializableFromFolder(project, folder, id, repositoryObjectType,
                        allVersion, true, true);
                if (!itemsFound.isEmpty()) {
                    addToHistory(id, repositoryObjectType, itemsFound.get(0).getProperty().getItem().getState().getPath());
                    toReturn.addAll(itemsFound);
                    // all items from the same id are always in the same folder
                    // as we shouldn't find any other item with the same id in another folder.
                    return toReturn;
                }
            }
        }
        return toReturn;
    }

    protected abstract Object getFolder(Project project, ERepositoryObjectType repositoryObjectType) throws PersistenceException;

    public List<IRepositoryObject> getAllVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = null;
        serializableAllVersion = getSerializable(project, id, true);
        return convert(serializableAllVersion);
    }

    public List<IRepositoryObject> getAllVersion(Project project, String id, String relativeFolder, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = null;

        Object fullFolder = getFullFolder(project, type, relativeFolder);
        if (fullFolder != null) {
            serializableAllVersion = getSerializableFromFolder(project, fullFolder, id, type, true, false, true);
            if (serializableAllVersion.isEmpty()) {
                // look in all folders
                serializableAllVersion = getSerializable(project, id, true);
            }
            return convert(serializableAllVersion);
        }
        serializableAllVersion = new ArrayList<IRepositoryObject>();
        return serializableAllVersion;
    }

    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryObject>... givenList)
            throws PersistenceException {
        if (name == null) {
            name = item.getProperty().getLabel();
        }

        if (item instanceof FolderItem) {
            FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
            return !folderHelper.pathExists((FolderItem) item, name);
        }

        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return false;
        }
        boolean isSqlPattern = (type == ERepositoryObjectType.SQLPATTERNS);
        String path = null;
        if (item.getState() != null) {
            path = item.getState().getPath();
        }

        List<IRepositoryObject> list;

        if (givenList.length == 0) {
            list = getAll(project, type, true, false);
        } else {
            list = givenList[0];
        }

        for (IRepositoryObject current : list) {
            if (name.equalsIgnoreCase(current.getProperty().getLabel())
                    && item.getProperty().getId() != current.getProperty().getId()) {
                // To check SQLPattern in same path. see bug 0005038: unable to add a SQLPattern into repository.
                if (!isSqlPattern || current.getProperty().getItem().getState().getPath().equals(path)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected abstract List<IRepositoryObject> getSerializableFromFolder(Project project, Object folder, String id,
            ERepositoryObjectType type, boolean allVersion, boolean searchInChildren, boolean withDeleted,
            boolean... recursiveCall) throws PersistenceException;

    protected abstract <K, T> RootContainer<K, T> getObjectFromFolder(Project project, ERepositoryObjectType type,
            boolean onlyLastVersion) throws PersistenceException;

    protected abstract <K, T> void addFolderMembers(Project project, ERepositoryObjectType type, Container<K, T> toReturn,
            Object objectFolder, boolean onlyLastVersion) throws PersistenceException;

    protected abstract FolderHelper getFolderHelper(org.talend.core.model.properties.Project emfProject);

    protected Item copyFromResource(Resource createResource) throws PersistenceException, BusinessException {
        return copyFromResource(createResource, true);
    }

    protected Item copyFromResource(Resource createResource, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException {
        Item newItem = (Item) EcoreUtil.getObjectByType(createResource.getContents(), PropertiesPackage.eINSTANCE.getItem());
        Property property = newItem.getProperty();
        property.setId(getNextId());
        property.setAuthor(getRepositoryContext().getUser());

        if (changeLabelWithCopyPrefix) {
            setPropNewName(property);
        }

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
        if (isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
            return;
        } else {
            char j = 'a';
            while (!isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
                if (j > 'z') {
                    throw new BusinessException(Messages.getString("AbstractEMFRepositoryFactory.cannotGenerateItem")); //$NON-NLS-1$
                }
                String nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                copiedProperty.setLabel(nextTry);
            }
        }
    }

    protected void createSystemRoutines() throws PersistenceException {
        ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());

        List<URL> routines = service.getSystemRoutines();
        Path path = new Path(RepositoryConstants.SYSTEM_DIRECTORY);
        // will automatically set the children folders
        folderHelper.createFolder("code/routines/system"); //$NON-NLS-1$

        List<IRepositoryObject> repositoryObjects = getAll(project, ERepositoryObjectType.ROUTINES, false, false);
        Map<String, List<URI>> routineAndJars = RoutineLibraryMananger.getInstance().getRoutineAndJars();
        for (URL url : routines) {
            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$
            String routineLabel = tmp[0];

            if (routineLabel.equals(ITalendSynchronizer.TEMPLATE)) {
                continue;
            }

            RoutineItem existingItem = null;
            for (IRepositoryObject object : repositoryObjects) {
                if (object.getLabel().equals(routineLabel) && object.getProperty().getItem() instanceof RoutineItem) {
                    existingItem = (RoutineItem) object.getProperty().getItem();
                    break;
                }
            }
            if (existingItem == null) {
                createRoutine(url, path, routineLabel, routineAndJars.get(routineLabel));
            } else {
                updateRoutine(url, existingItem);
            }
        }
    }

    protected void createSystemSQLPatterns() throws PersistenceException {
        ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
        // will automatically set the children folders
        folderHelper.createFolder("sqlPatterns/system"); //$NON-NLS-1$

        List<URL> routines = service.getSystemSQLPatterns();

        List<IRepositoryObject> repositoryObjects = getAll(project, ERepositoryObjectType.SQLPATTERNS, false, false);

        for (URL url : routines) {
            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$

            Path relativePath = new Path(url.getFile());

            // for instance: categoryName is Teradata; fileName is
            // Loadfile.sqlpattern
            String fileName = relativePath.segment(relativePath.segmentCount() - 1);
            String categoryName = relativePath.segment(relativePath.segmentCount() - 2);

            tmp = fileName.split("\\."); //$NON-NLS-1$

            String sqlPatternLabel = tmp[0];

            SQLPatternItem existingItem = null;
            for (IRepositoryObject object : repositoryObjects) {
                if (object.getLabel().equals(sqlPatternLabel) && object.getProperty().getItem() instanceof SQLPatternItem
                        && ((SQLPatternItem) object.getProperty().getItem()).getEltName().equals(categoryName)) {
                    existingItem = (SQLPatternItem) object.getProperty().getItem();
                    break;
                }
            }
            if (existingItem == null) {
                createSQLPattern(url, sqlPatternLabel, categoryName);
            } else {
                updateSQLPattern(url, existingItem);
            }
        }
    }

    /**
     * DOC smallet Comment method "createRoutine".
     * 
     * @param url
     * @throws PersistenceException
     */
    private void createRoutine(URL url, IPath path, String label, List<URI> neededJars) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());
            property.setLabel(label);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            // String basePath = System.getProperty("user.dir") + File.separator + "plugins";

            RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            routineItem.setProperty(property);
            routineItem.setContent(byteArray);
            routineItem.setBuiltIn(true);
            if (neededJars != null) {
                for (URI jar : neededJars) {
                    IMPORTType type = ComponentFactory.eINSTANCE.createIMPORTType();
                    type.setMESSAGE("");
                    type.setNAME(label);
                    type.setREQUIRED(true);
                    type.setMODULE(new Path(jar.getPath()).lastSegment());
                    type.setUrlPath(jar.getPath());
                    routineItem.getImports().add(type);
                }
            }

            if (!routineItem.getProperty().getLabel().equals(ITalendSynchronizer.TEMPLATE)) {
                create(getRepositoryContext().getProject(), routineItem, path);
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

    private void updateRoutine(URL url, RoutineItem item) throws PersistenceException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();

            byte[] currentContent = item.getContent().getInnerContent();

            if (!Arrays.equals(innerContent, currentContent)) {
                item.getContent().setInnerContent(innerContent);
                Project project = getRepositoryContext().getProject();
                save(project, item);
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

    private void createSQLPattern(URL url, String sqlPatternLabel, String categoryName) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());

            property.setLabel(sqlPatternLabel);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            SQLPatternItem sqlpatternItem = PropertiesFactory.eINSTANCE.createSQLPatternItem();
            sqlpatternItem.setProperty(property);
            sqlpatternItem.setEltName(categoryName);
            sqlpatternItem.setContent(byteArray);
            sqlpatternItem.setSystem(true);

            // set the item's relative path in the repository view
            IPath categoryPath = new Path(categoryName);
            IPath systemPath = categoryPath.append(RepositoryConstants.SYSTEM_DIRECTORY);
            IPath userPath = categoryPath.append(RepositoryConstants.USER_DEFINED);

            FolderHelper folderHelper = getFolderHelper(getRepositoryContext().getProject().getEmfProject());
            IPath parentPath = new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.SQLPATTERNS));
            if (folderHelper.getFolder(parentPath.append(categoryPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, new Path(""), categoryPath //$NON-NLS-1$
                        .lastSegment());
            }
            if (folderHelper.getFolder(parentPath.append(systemPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, categoryPath, systemPath
                        .lastSegment());
            }
            if (folderHelper.getFolder(parentPath.append(userPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, categoryPath, userPath
                        .lastSegment());
            }
            create(getRepositoryContext().getProject(), sqlpatternItem, systemPath);

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

    private void updateSQLPattern(URL url, SQLPatternItem item) throws PersistenceException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();

            byte[] currentContent = item.getContent().getInnerContent();

            if (!Arrays.equals(innerContent, currentContent)) {
                item.getContent().setInnerContent(innerContent);
                Project project = getRepositoryContext().getProject();
                save(project, item);
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
        setLoggedOnProject(false);

        new StatusPreferenceInitializer().initializeDefaultPreferences();
        String productVersion = RepositoryPlugin.getDefault().getBundle().getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION).toString();
        IBrandingService brandingService = null;
        try {
            brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
        } catch (Exception e) {
            // nothing to do.
        }
        if (brandingService != null) {
            String productBranding = brandingService.getFullProductName();
            project.getEmfProject().setProductVersion(productBranding + "-" + productVersion); //$NON-NLS-1$
        }
        saveProject();

        setLoggedOnProject(true);
    }

    protected abstract void saveProject() throws PersistenceException;

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        List<IRepositoryObject> jobs = repositoryFactory.getAll(ERepositoryObjectType.PROCESS, true);
        for (IRepositoryObject cur : jobs) {
            if (repositoryFactory.getStatus(cur) != ERepositoryStatus.DELETED) {
                ProcessItem item = (ProcessItem) cur.getProperty().getItem();
                if (item == null || item.getProcess() == null) {
                    continue;
                }
                List<NodeType> nodes = item.getProcess().getNode();
                for (NodeType node : nodes) {
                    List<ElementParameterType> elementParameter = node.getElementParameter();
                    for (ElementParameterType elementParam : elementParameter) {
                        if (elementParam.getField().equals(EParameterFieldType.MODULE_LIST.getName())) {
                            String uniquename = ElementParameterParser.getUNIQUENAME(node);
                            ModuleNeeded toAdd = new ModuleNeeded(
                                    Messages.getString("AbstractEMFRepositoryFactory.job") + item.getProperty().getLabel(), //$NON-NLS-1$
                                    elementParam.getValue(),
                                    Messages.getString("AbstractEMFRepositoryFactory.requiredComponent") + uniquename + ".", true); //$NON-NLS-1$ //$NON-NLS-2$
                            importNeedsList.add(toAdd);
                        }
                    }
                }
            }
        }

        return importNeedsList;
    }

    public RootContainer<String, IRepositoryObject> getRoutineFromProject(Project project) throws PersistenceException {
        RootContainer<String, IRepositoryObject> toReturn = new RootContainer<String, IRepositoryObject>();
        ERepositoryObjectType type = ERepositoryObjectType.ROUTINES;

        IProject fsProject = ResourceModelUtils.getProject(project);

        IFolder objectFolder = ResourceUtils.getFolder(fsProject, ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.ROUTINES), true);

        addFolderMembers(project, type, toReturn, objectFolder, true);
        return toReturn;
    }

    public synchronized IRepositoryObject getLastVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = null;
        serializableAllVersion = getSerializable(project, id, false);
        int size = serializableAllVersion.size();
        if (size > 1) {
            String message = getItemsMessages(serializableAllVersion, size);

            throw new PersistenceException(Messages.getString(
                    "AbstractEMFRepositoryFactory.presistenceException.OnlyOneOccurenceMustbeFound", message)); //$NON-NLS-1$
        } else if (size == 1) {
            return serializableAllVersion.get(0);
        } else {
            return null;
        }
    }

    /**
     * DOC zli Comment method "getItemsMessages".
     * 
     * @param serializableAllVersion
     * @param size
     * @return
     */
    // for bug 9265
    private String getItemsMessages(List<IRepositoryObject> serializableAllVersion, int size) {
        String message = Messages.getString("AbstractEMFRepositoryFactory.presistenceException.whoCauseProblems");//$NON-NLS-1$
        int k = 0;
        for (IRepositoryObject object : serializableAllVersion) {
            message += object.getProperty().getLabel();
            k++;
            if (k < size) {
                message += ", ";//$NON-NLS-1$
            }
        }
        return message;
    }

    public synchronized IRepositoryObject getLastVersion(Project project, String id, String relativeFolder,
            ERepositoryObjectType type) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = null;
        Object fullFolder = getFullFolder(project, type, relativeFolder);
        serializableAllVersion = getSerializableFromFolder(project, fullFolder, id, type, false, false, true);
        if (serializableAllVersion.isEmpty()) {
            // look in all folders
            serializableAllVersion = getSerializable(project, id, true);
        }
        int size = serializableAllVersion.size();

        if (size > 1) {
            String message = getItemsMessages(serializableAllVersion, size);

            throw new PersistenceException(Messages.getString(
                    "AbstractEMFRepositoryFactory.presistenceException.OnlyOneOccurenceMustbeFound", message)); //$NON-NLS-1$
        } else if (size == 1) {
            return serializableAllVersion.get(0);
        } else {
            return null;
        }
    }

    protected void computePropertyMaxInformationLevel(Property property) {
        EList<Information> informations = property.getInformations();
        InformationLevel maxLevel = null;
        for (Information information : informations) {
            int value = information.getLevel().getValue();
            if (maxLevel == null || value > maxLevel.getValue()) {
                maxLevel = information.getLevel();
            }
        }
        property.setMaxInformationLevel(maxLevel);
    }

    private Object getFullFolder(Project project, ERepositoryObjectType itemType, String path) throws PersistenceException {
        Object folder = getFolder(project, itemType);
        if (folder == null) {
            return null;
        }
        Object fullFolder;
        if (folder instanceof IFolder) {
            fullFolder = (IFolder) getFolder(project, itemType);
            if (path != null && !"".equals(path)) { //$NON-NLS-1$
                fullFolder = ((IFolder) fullFolder).getFolder(new Path(path));
            }
        } else {
            // FolderItem
            if (path != null && !"".equals(path)) { //$NON-NLS-1$
                // MOD mzhao feature 9207
                if (folder == null) {
                    fullFolder = ResourceModelUtils.getProject(project).getFolder(new Path(path));
                } else {
                    fullFolder = this.getFolderHelper(project.getEmfProject()).getFolder(
                            ((FolderItem) folder).getProperty().getLabel() + "/" + path); //$NON-NLS-1$
                }
            } else {
                fullFolder = folder;
            }
        }
        return fullFolder;
    }

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException {

        List<IRepositoryObject> allVersion = new ArrayList<IRepositoryObject>();
        getAllVersions(project, property, allVersion);
        for (IRepositoryObject repositoryObject : allVersion) {
            Property uptodateProperty = repositoryObject.getProperty();
            if (uptodateProperty.getVersion().equals(property.getVersion())) {
                return uptodateProperty;
            }
        }
        return null;
    }

    private void getAllVersions(Project project, Property property, List<IRepositoryObject> allVersion)
            throws PersistenceException {
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());

        Object fullFolder = getFullFolder(project, itemType, property.getItem().getState().getPath());
        if (fullFolder != null) {
            allVersion.addAll(getSerializableFromFolder(project, fullFolder, property.getId(), itemType, true, false, true));
            if (allVersion.size() == 0) {
                // if no item found in current directory, look for all directory
                allVersion.addAll(getAllVersion(project, property.getId()));
            }
        } else {
            allVersion.addAll(getAllVersion(project, property.getId()));
        }
        if (allVersion.size() == 0 && project.getEmfProject().getReferencedProjects().size() > 0) {
            for (ProjectReference refProject : (List<ProjectReference>) (List<ProjectReference>) project.getEmfProject()
                    .getReferencedProjects()) {
                org.talend.core.model.properties.Project emfProject = refProject.getReferencedProject();
                getAllVersions(new Project(emfProject), property, allVersion);
                if (allVersion.size() > 0) {
                    break;
                }
            }
        }
        // MOD mzhao Temporary return original object. In this case, the object hasn't been updated from svn server.
        if (allVersion.size() == 0) {
            allVersion.add(new RepositoryObject(property));
        }

    }

    public void deleteObjectPhysical(Project project, IRepositoryObject objToDelete) throws PersistenceException {
        if (objToDelete.getType() == ERepositoryObjectType.PROCESS || objToDelete.getType() == ERepositoryObjectType.JOBLET) {
            RelationshipItemBuilder builder = RelationshipItemBuilder.getInstance();
            if (builder.isAlreadyBuilt(project)) {
                builder.removeItemRelations(objToDelete.getProperty().getItem());
            }
        }
    }
}
