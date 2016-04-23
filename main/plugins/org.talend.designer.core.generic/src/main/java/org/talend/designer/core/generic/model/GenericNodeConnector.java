// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.Connector.ConnectorType;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.model.components.NodeConnector;


/**
 * created by nrousseau on Apr 22, 2016
 * Detailled comment
 *
 */
public class GenericNodeConnector extends NodeConnector implements INodeConnector {

    private ConnectorType genericConnectorType;


    /**
     * DOC nrousseau GenericNodeConnector constructor comment.
     * @param parentNode
     */
    public GenericNodeConnector(INode parentNode) {
        super(parentNode);
    }


    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.NodeConnector#getMaxLinkInput()
     */
    @Override
    public int getMaxLinkInput() {
        int nb = 0;
        Set<Connector> connectors = getParentNode().getComponentProperties().getAvailableConnectors(null, false);
        for (Connector connector : connectors) {
            if (connector.getType() == genericConnectorType) {
                nb++;
            }
        }
        return nb;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.NodeConnector#getMaxLinkOutput()
     */
    @Override
    public int getMaxLinkOutput() {
        int nb = 0;
        Set<Connector> connectors = getParentNode().getComponentProperties().getAvailableConnectors(null, true);
        for (Connector connector : connectors) {
            if (connector.getType() == genericConnectorType) {
                nb++;
            }
        }
        return nb;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.NodeConnector#getMinLinkInput()
     */
    @Override
    public int getMinLinkInput() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.NodeConnector#getMinLinkOutput()
     */
    @Override
    public int getMinLinkOutput() {
        return 0;
    }


    /**
     * DOC nrousseau Comment method "setGenericConnectorType".
     * @param genericConnectorType
     */
    public void setGenericConnectorType(ConnectorType genericConnectorType) {
        this.genericConnectorType = genericConnectorType;
    }

    
}
