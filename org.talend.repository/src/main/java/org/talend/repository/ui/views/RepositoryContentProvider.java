// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * Content provider for the repository view.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    private final IRepositoryView view;

    private TreeViewer viewer;

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    public RepositoryContentProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        viewer = (TreeViewer) v;
    }

    public void dispose() {
    }

    protected IRepositoryView getView() {
        return view;
    }

    public Object[] getElements(Object parent) {
        if (parent.equals(getView().getViewSite())) {
            IRepositoryNode systemFolders = getView().getRoot();
            if (systemFolders.getChildren().isEmpty()) {
                initialize();
            }

            return systemFolders.getChildren().toArray();
        }
        return getChildren(parent);
    }

    public Object getParent(Object child) {

        return ((RepositoryNode) child).getParent();
    }

    public Object[] getChildren(Object parent) {
        if (parent == null) {
            return new RepositoryNode[0];
        }
        RepositoryNode repositoryNode = ((RepositoryNode) parent);
        if (!repositoryNode.isInitialized()) {
            if (repositoryNode.getParent() instanceof ProjectRepositoryNode) {
                // initialize repository from main project
                ((ProjectRepositoryNode) repositoryNode.getParent()).initializeChildren(parent);
            }
            repositoryNode.setInitialized(true);
        }

        return repositoryNode.getChildren().toArray();
    }

    public boolean hasChildren(Object parent) {
        Boolean boolean1 = factory.hasChildren(parent);
        if (boolean1 != null) {
            return boolean1;
        } else {
            if (parent instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) parent;
                if (repositoryNode.isInitialized()) {
                    return repositoryNode.getChildren().size() > 0;
                } else {
                    return getChildren(parent).length > 0;
                }
            }
            return true;
        }
    }

    private void initialize() {
        ProjectRepositoryNode root = getRoot();

        String currentPerspective = IBrandingConfiguration.PERSPECTIVE_DI_ID;

        try {
            currentPerspective = getView().getSite().getPage().getPerspective().getId();
        } catch (Exception e) {
            // do nothing
            // this exception is just in case, since for some specific cases, page can be null (shouldn't happen but..)
        }
        root.initialize(currentPerspective);
    }

    /**
     * Getter for processNode.
     * 
     * @return the processNode
     */
    public RepositoryNode getProcessNode() {
        return getRoot().getProcessNode();
    }

    public RepositoryNode getJobletNode() {
        return getRoot().getJobletNode();
    }

    public RepositoryNode getReferenceProjectNode() {
        return getRoot().getReferenceProjectNode();
    }

    /**
     * Getter for metadataConNode.
     * 
     * @return the metadataConNode
     */
    public RepositoryNode getMetadataConNode() {
        if (getRoot().getMetadataConNode() == null) {
            getChildren(getRoot().getMetadataConNode());
        }
        return getRoot().getMetadataConNode();
    }

    /**
     * Getter for metadataConNode.
     * 
     * @return the metadataConNode
     */
    public RepositoryNode getMetadataServicesNode() {
        if (getRoot().getMetadataConNode() == null) {
            getChildren(getRoot().getMetadataConNode());
        }
        return getRoot().getMetadataConNode();
    }

    /**
     * Getter for codeNode.
     * 
     * @return the codeNode
     */
    public RepositoryNode getCodeNode() {
        return getRoot().getCodeNode();
    }

    /**
     * Getter for metadataFileNode.
     * 
     * @return the metadataFileNode
     */
    public RepositoryNode getMetadataFileNode() {
        return getRoot().getMetadataFileNode();
    }

    /**
     * Getter for metadataFilePositionalNode.
     * 
     * @return the metadataFilePositionalNode
     */
    public RepositoryNode getMetadataFilePositionalNode() {
        return getRoot().getMetadataFilePositionalNode();
    }

    /**
     * Getter for metadataFileRegexpNode.
     * 
     * @return the metadataFileRegexpNode
     */
    public RepositoryNode getMetadataFileRegexpNode() {
        return getRoot().getMetadataFileRegexpNode();
    }

    /**
     * Getter for metadataFileXmlNode.
     * 
     * @return the metadataFileXmlNode
     */
    public RepositoryNode getMetadataFileXmlNode() {
        return getRoot().getMetadataFileXmlNode();
    }

    public RepositoryNode getMetadataValidationRulesNode() {
        return getRoot().getMetadataValidationRulesNode();
    }

    public RepositoryNode getMetadataEDIFACTNode() {
        return getRoot().getMetadataEdifactNode();
    }

    /**
     * Getter for metadataFileLdifNode.
     * 
     * @return the metadataFileLdifNode
     */
    public RepositoryNode getMetadataFileLdifNode() {
        return getRoot().getMetadataFileLdifNode();
    }

    /**
     * Getter for metadataGenericSchemaNode.
     * 
     * @return the metadataGenericSchemaNode
     */
    public RepositoryNode getMetadataGenericSchemaNode() {
        return getRoot().getMetadataGenericSchemaNode();
    }

    /**
     * Getter for metadataLDAPSchemaNode.
     * 
     * @return the metadataLDAPSchemaNode
     */
    public RepositoryNode getMetadataLDAPSchemaNode() {
        return getRoot().getMetadataLDAPSchemaNode();
    }

    /**
     * Getter for metadataWSDLSchemaNode.
     * 
     * @return the metadataWSDLSchemaNode
     */
    public RepositoryNode getMetadataWSDLSchemaNode() {
        return getRoot().getMetadataWSDLSchemaNode();
    }

    /**
     * Getter for metadataNode.
     * 
     * @return the metadataNode
     */
    public RepositoryNode getMetadataNode() {
        return getRoot().getMetadataNode();
    }

    /**
     * Getter for metadataFileExcelNode.
     * 
     * @return the metadataFileExcelNode
     */
    public RepositoryNode getMetadataFileExcelNode() {
        return getRoot().getMetadataFileExcelNode();
    }

    /**
     * Getter for metadataSalesforceSchemaNode.
     * 
     * @return the metadataSalesforceSchemaNode
     */
    public RepositoryNode getMetadataSalesforceSchemaNode() {
        return getRoot().getMetadataSalesforceSchemaNode();
    }

    public RepositoryNode getMetadataBRMSConnectionNode() {
        return getRoot().getMetadataBRMSConnectionNode();
    }

    public String[] gatherMetdataChildrens() {
        if (getMetadataNode() == null) {
            return null;
        }

        List<IRepositoryNode> nodes = getMetadataNode().getChildren();
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }

        String[] res = new String[nodes.size()];
        for (int i = 0, n = nodes.size(); i < n; i++) {
            res[i] = nodes.get(i).getLabel();
        }

        return res;
    }

    public RepositoryNode getMetadataSAPConnectionNode() {
        return getRoot().getMetadataSAPConnectionNode();
    }

    public RepositoryNode getMetadataHeaderFooterConnectionNode() {
        return getRoot().getMetadataHeaderFooterConnectionNode();
    }

    public RepositoryNode getMetadataMdmConnectionNode() {
        return getRoot().getMetadataMDMConnectionNode();
    }

    public RepositoryNode getRootRepositoryNode(ERepositoryObjectType type) {
        RepositoryNode rootRepositoryNode = getRoot().getRootRepositoryNode(type);
        if (rootRepositoryNode == null) {
            initialize();
            // re-retrieve
            rootRepositoryNode = getRoot().getRootRepositoryNode(type);
        }
        if (rootRepositoryNode != null && rootRepositoryNode.getChildren().isEmpty()) {
            // retrieve child
            getChildren(rootRepositoryNode);

        }
        return rootRepositoryNode;
    }

    public ProjectRepositoryNode getRoot() {
        return (ProjectRepositoryNode) getView().getRoot();
    }
}
