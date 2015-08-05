// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.processor;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.metadata.MetadataColumnRepositoryObject;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryTypeProcessor;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.ICDCProviderService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.utils.RecombineRepositoryNodeUtil;

/**
 * ggu class global comment. Detailled comment
 */
public abstract class MultiTypesProcessor implements IRepositoryTypeProcessor {

    protected String[] repositoryTypes;

    public MultiTypesProcessor(String[] repositoryTypes) {
        super();
        this.repositoryTypes = repositoryTypes;
    }

    protected String[] getRepositoryTypes() {
        return repositoryTypes;
    }

    protected abstract List<ERepositoryObjectType> getTypes();

    @Override
    public IRepositoryNode getInputRoot(IProjectRepositoryNode projectRepoNode) {
        return RecombineRepositoryNodeUtil.getFixingTypesInputRoot(projectRepoNode, getTypes());
    }

    @Override
    public boolean isSelectionValid(RepositoryNode node) {
        Object nodeType = node.getProperties(EProperties.CONTENT_TYPE);
        List<ERepositoryObjectType> types = getTypes();
        if (types != null) {
            for (ERepositoryObjectType type : types) {
                if (nodeType == type) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                return selectRepositoryNode(viewer, (RepositoryNode) parentElement, (RepositoryNode) element);
            }
        };
    }

    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode parentNode, RepositoryNode node) {
        if (node == null) {
            return false;
        }
        IRepositoryViewObject object = node.getObject();
        if (object != null) {
            // column
            if (object instanceof MetadataColumnRepositoryObject) {
                return false;
            }
        }
        // hide the column folder
        if (object == null && node.getParent() != null && node.getParent().getObject() != null
                && node.getParent().getObjectType() == ERepositoryObjectType.METADATA_CON_TABLE) {
            return false;
        }
        // cdc
        ICDCProviderService cdcService = null;
        if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_CDC) {
            return false;
        }
        if (isCDCConnection(node)) {
            return false;
        }
        if (PluginChecker.isCDCPluginLoaded()) {
            cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(ICDCProviderService.class);
            if (cdcService != null && cdcService.isSubscriberTableNode(node)) {
                return false;
            }
        }
        return true;
    }

    protected final boolean isCDCConnection(RepositoryNode node) {
        ICDCProviderService service = null;
        if (PluginChecker.isCDCPluginLoaded()) {
            service = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(ICDCProviderService.class);
        }
        if (node != null && node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
            List<IRepositoryNode> children = node.getChildren();
            if (children != null) {
                for (IRepositoryNode child : children) {
                    if (service != null && service.isSystemSubscriberTable((RepositoryNode) child)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    @Override
    public String getDialogTitle() {
        return null;
    }

}