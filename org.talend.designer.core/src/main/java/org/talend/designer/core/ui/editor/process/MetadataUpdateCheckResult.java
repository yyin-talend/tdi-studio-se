// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.process;

import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * This class hold the check result when the metadata updated. <br/>
 * 
 */
public class MetadataUpdateCheckResult {

    /**
     * The type of the metadata update. <br/>
     * 
     */
    public static enum ResultType {
        delete(Messages.getString("MetadataUpdateCheckResult.changeToBuildinMode")), //$NON-NLS-1$
        change(Messages.getString("MetadataUpdateCheckResult.UpdateFromRepository")); //$NON-NLS-1$

        private String displayName;

        ResultType(String displayName) {
            this.displayName = displayName;
        }

        public String getName() {
            return this.toString();
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    /**
     * The type of the repository. <br/>
     * 
     */
    public static enum RepositoryType {
        property,
        schema,
        query
    }

    private boolean checked = true;

    private Node node = null;

    private ResultType resultType = null;

    private RepositoryType repositoryType = null;

    private Object parameter = null;

    /**
     * Create one node 's check result when the metadata updated.<br/>
     * 
     * @param node
     */
    public MetadataUpdateCheckResult(Node node) {
        super();
        this.node = node;
    }

    public void setResult(RepositoryType repositoryType, ResultType resultType, Object parameter) {
        this.repositoryType = repositoryType;
        this.resultType = resultType;
        this.parameter = parameter;
        // the ResultType.delete must initialize the checked==true, so it will show in the dialog
        if (resultType.equals(ResultType.delete)) {
            setChecked(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // the sequence is very important, it will sort with it
        StringBuffer sb = new StringBuffer();
        sb.append(resultType);
        sb.append(node.getUniqueName());
        sb.append(repositoryType);
        return sb.toString();
    }

    /**
     * Getter for checked.
     * 
     * @return the checked
     */
    public boolean isChecked() {
        return this.checked;
    }

    /**
     * Sets the checked.
     * 
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Getter for node.
     * 
     * @return the node
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * Sets the node.
     * 
     * @param node the node to set
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Getter for parameter.
     * 
     * @return the parameter
     */
    public Object getParameter() {
        return this.parameter;
    }

    /**
     * Sets the parameter.
     * 
     * @param parameter the parameter to set
     */
    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    /**
     * Getter for repositoryType.
     * 
     * @return the repositoryType
     */
    public RepositoryType getRepositoryType() {
        return this.repositoryType;
    }

    /**
     * Sets the repositoryType.
     * 
     * @param repositoryType the repositoryType to set
     */
    public void setRepositoryType(RepositoryType repositoryType) {
        this.repositoryType = repositoryType;
    }

    /**
     * Getter for resultType.
     * 
     * @return the resultType
     */
    public ResultType getResultType() {
        return this.resultType;
    }

    /**
     * Sets the resultType.
     * 
     * @param resultType the resultType to set
     */
    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }
}
