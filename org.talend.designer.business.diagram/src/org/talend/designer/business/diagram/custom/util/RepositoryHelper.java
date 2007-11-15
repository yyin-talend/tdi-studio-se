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
package org.talend.designer.business.diagram.custom.util;

import java.util.Iterator;

import org.eclipse.emf.ecore.EClass;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessFactory;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.Repository;
import org.talend.designer.business.model.business.TalendItem;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryHelper {

    public TalendItem getTalendItem(Repository repository, Object object) {
        if (object instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) object;
            if (repositoryNode.getType() == RepositoryNode.ENodeType.REPOSITORY_ELEMENT) {
                IRepositoryObject repositoryObject = repositoryNode.getObject();
                for (Iterator iter = repository.getTalenditems().iterator(); iter.hasNext();) {
                    TalendItem talendItem = (TalendItem) iter.next();
                    if (talendItem.getId().equals(repositoryObject.getId())) {
                        return talendItem;
                    }
                }
            }
        }
        return null;
    }

    public TalendItem createTalendItem(Repository repository, Object object) {
        TalendItem result = null;

        if (object instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) object;
            if (repositoryNode.getType() == RepositoryNode.ENodeType.REPOSITORY_ELEMENT) {
                IRepositoryObject repositoryObject = repositoryNode.getObject();
                ERepositoryObjectType nodeType = (ERepositoryObjectType) repositoryNode
                        .getProperties(EProperties.CONTENT_TYPE);

                result = (TalendItem) BusinessFactory.eINSTANCE.create(getEClass(nodeType));

                result.setRepository(repository);
                result.setId(repositoryObject.getId());
                result.setLabel(repositoryObject.getLabel());
            }
        }

        return result;
    }

    public void unassignTalendItemsFromBusinessAssignment(BusinessAssignment businessAssignment) {
        TalendItem talendItem = businessAssignment.getTalendItem();
        talendItem.getAssignments().remove(businessAssignment);
        if (talendItem.getAssignments().size() == 0) {
            talendItem.getRepository().getTalenditems().remove(talendItem);
        }
    }

    /**
     * DOC mhelleboid Comment method "getEClass".
     * 
     * @param type
     * @return
     */
    private EClass getEClass(ERepositoryObjectType type) {
        if (type == ERepositoryObjectType.BUSINESS_PROCESS) {
            return BusinessPackage.eINSTANCE.getBusinessProcess();
        } else if (type == ERepositoryObjectType.PROCESS) {
            return BusinessPackage.eINSTANCE.getProcess();
        } else if (type == ERepositoryObjectType.ROUTINES) {
            return BusinessPackage.eINSTANCE.getRoutine();
        } else if (type == ERepositoryObjectType.DOCUMENTATION) {
            return BusinessPackage.eINSTANCE.getDocumentation();
        } else if (type == ERepositoryObjectType.METADATA_CONNECTIONS) {
            return BusinessPackage.eINSTANCE.getDatabaseMetadata();
        } else if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return BusinessPackage.eINSTANCE.getTableMetadata();
        } else if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            return BusinessPackage.eINSTANCE.getFileDelimitedMetadata();
        } else if (type == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
            return BusinessPackage.eINSTANCE.getFilePositionalMetadata();
        } else if (type == ERepositoryObjectType.METADATA_FILE_REGEXP) {
            return BusinessPackage.eINSTANCE.getFileRegexpMetadata();
        } else if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            return BusinessPackage.eINSTANCE.getFileXmlMetadata();
        } else if (type == ERepositoryObjectType.METADATA_FILE_LDIF) {
            return BusinessPackage.eINSTANCE.getFileLdifMetadata();
        } else if (type == ERepositoryObjectType.CONTEXT) {
            return BusinessPackage.eINSTANCE.getContext();
        } else if (type == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            return BusinessPackage.eINSTANCE.getGenericSchemaMetadata();
        }
        return null;
    }

    /**
     * DOC mhelleboid Comment method "createLocalRepositoryCopy".
     * 
     * @param businessProcess
     * @return
     */
    public Repository createLocalRepositoryCopy(BusinessProcess businessProcess) {
        Repository repository = businessProcess.getLocalRepositoryCopy();
        if (repository == null) {
            repository = BusinessFactory.eINSTANCE.createRepository();
            businessProcess.setLocalRepositoryCopy(repository);
        }
        return repository;
    }
}
