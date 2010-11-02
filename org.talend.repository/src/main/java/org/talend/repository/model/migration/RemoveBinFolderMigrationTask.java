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
package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EmfHelper;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.ILocalRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.ResourceModelUtils;
import org.talend.repository.utils.URIHelper;
import org.talend.repository.utils.XmiResourceManager;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class RemoveBinFolderMigrationTask extends AbstractItemMigrationTask {

    private Map<String, ERepositoryObjectType> pathToCheckIfDeleted = new HashMap<String, ERepositoryObjectType>();

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.BUSINESS_PROCESS);
        toReturn.add(ERepositoryObjectType.SVG_BUSINESS_PROCESS);
        toReturn.add(ERepositoryObjectType.DOCUMENTATION);
        toReturn.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        toReturn.add(ERepositoryObjectType.METADATA_SAPCONNECTIONS);
        toReturn.add(ERepositoryObjectType.SQLPATTERNS);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_DELIMITED);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_REGEXP);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_XML);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_EXCEL);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_LDIF);
        toReturn.add(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
        toReturn.add(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
        toReturn.add(ERepositoryObjectType.METADATA_WSDL_SCHEMA);
        toReturn.add(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
        toReturn.add(ERepositoryObjectType.PROCESS);
        toReturn.add(ERepositoryObjectType.ROUTINES);
        toReturn.add(ERepositoryObjectType.SNIPPETS);
        toReturn.add(ERepositoryObjectType.JOBLET);
        toReturn.add(ERepositoryObjectType.CONTEXT);
        if (PluginChecker.isDocumentationPluginLoaded()) {
            toReturn.add(ERepositoryObjectType.JOBS);
        }
        if (PluginChecker.isJobLetPluginLoaded()) {
            toReturn.add(ERepositoryObjectType.JOBLETS);
        }
        if (PluginChecker.isEBCDICPluginLoaded()) {
            toReturn.add(ERepositoryObjectType.METADATA_FILE_EBCDIC);
        }
        if (PluginChecker.isHL7PluginLoaded()) {
            toReturn.add(ERepositoryObjectType.METADATA_FILE_HL7);
        }
        if (PluginChecker.isRulesPluginLoaded()) {
            toReturn.add(ERepositoryObjectType.METADATA_FILE_RULES);
        }
        if (PluginChecker.isMDMPluginLoaded()) {
            toReturn.add(ERepositoryObjectType.METADATA_MDMCONNECTION);
        }

        return toReturn;
    }

    @Override
    public ExecutionResult execute(Item item) {
        // this migration should be only on local and svn repository, so check if instance of local repository.
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
        IRepositoryViewObject object;
        try {
            object = factory.getLastVersion(item.getProperty().getId());
            if (object == null) {
                return ExecutionResult.FAILURE; // item not found !?
            }
            if (ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider() instanceof ILocalRepositoryFactory) {
                if (!object.getProperty().getItem().getState().isDeleted()) {
                    return ExecutionResult.SUCCESS_NO_ALERT;
                }
                item.getState().setDeleted(true);
                // ensure that this item got deleted status, some versions only set deleted status to last version.

                // item has been found, and is a deleted item, so move the item from bin folder to standard folder.
                IProject fsProject = ResourceModelUtils.getProject(getProject());

                ERepositoryObjectType repositoryType = ERepositoryObjectType.getItemType(item);
                EcoreUtil.resolveAll(item);

                Resource propertyResource = item.getProperty().eResource();
                pathToCheckIfDeleted.put(item.getState().getPath(), repositoryType);

                // check if imported item is in the bin folder or not, if not, no need to move anything.
                if (propertyResource.getURI().segmentCount() > 2) {
                    String parentFolder = propertyResource.getURI().segment(propertyResource.getURI().segmentCount() - 2);
                    if (!parentFolder.equals("bin")) {
                        return ExecutionResult.SUCCESS_NO_ALERT;
                    }
                } else {
                    return ExecutionResult.SUCCESS_NO_ALERT;
                }

                XmiResourceManager xrm = new XmiResourceManager();
                List<Resource> resources = xrm.getAffectedResources(item.getProperty());
                Collections.sort(resources, new Comparator<Resource>() {

                    public int compare(Resource o1, Resource o2) {
                        if (o1.getURI().fileExtension().equals(".properties")) {
                            return -1;
                        }
                        return 1;
                    }
                });
                // restore folder if doesn't exist anymore.
                String oldPath = item.getState().getPath();
                IPath path = new Path(oldPath);
                factory.createParentFoldersRecursively(getProject(), repositoryType, path);

                IFolder typeRootFolder = ResourceUtils.getFolder(fsProject, ERepositoryObjectType.getFolderName(repositoryType),
                        true);
                for (Resource resource : resources) {
                    IPath originalPath = URIHelper.convert(resource.getURI());
                    IPath finalPath = typeRootFolder.getFullPath().append(path).append(originalPath.lastSegment());
                    ResourceUtils.moveResource(URIHelper.getFile(resource.getURI()), finalPath);
                    resource.setURI(URIHelper.convert(finalPath));
                    EmfHelper.saveResource(resource);
                }
            }
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2010, 3, 23, 12, 0, 0);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(Project project) {
        ExecutionResult result = super.execute(project);
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();

        for (String curPath : pathToCheckIfDeleted.keySet()) {
            FolderItem folderItem = factory.getFolderItem(getProject(), pathToCheckIfDeleted.get(curPath), new Path(curPath));
            setPathToDeleteIfNeed(folderItem);
        }
        return result;
    }

    private boolean setPathToDeleteIfNeed(FolderItem folderItem) {
        if (folderItem.getType().getValue() != FolderType.FOLDER) {
            return false;
        }
        if (folderItem.getState().isDeleted()) {
            return true;
        }
        boolean allDeleted = true;
        for (Item item : (List<Item>) folderItem.getChildren()) {
            if (item instanceof FolderItem) {
                allDeleted = setPathToDeleteIfNeed((FolderItem) item);
                if (!allDeleted) {
                    break;
                }
            }
            if (!item.getState().isDeleted()) {
                allDeleted = false;
                break;
            }
        }
        if (allDeleted) {
            folderItem.getState().setDeleted(true);
            String fullPath = "";
            FolderItem curItem = folderItem;
            while (curItem.getParent() instanceof FolderItem && ((Item) curItem.getParent()).getParent() instanceof FolderItem
                    && ((FolderItem) ((Item) curItem.getParent()).getParent()).getType().getValue() == FolderType.FOLDER) {
                FolderItem parentFolder = (FolderItem) curItem.getParent();
                if ("".equals(fullPath)) {
                    fullPath = parentFolder.getProperty().getLabel() + fullPath;
                } else {
                    fullPath = parentFolder.getProperty().getLabel() + "/" + fullPath;
                }
                curItem = parentFolder;
            }
            folderItem.getState().setPath(fullPath);
        }
        return allDeleted;
    }
}
