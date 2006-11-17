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
package org.talend.designer.core.model.process;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.temp.ECodeLanguage;

/**
 * Virtual node that will be used for the generated code.
 * 
 * $Id$
 * 
 */
public class DataNode extends AbstractNode {

    public DataNode(IComponent component, String uniqueName) {
        setElementParameters(component.createElementParameters(this));
        setComponentName(component.getName());
        setPluginFullName(component.getPluginFullName());
        List<IMetadataTable> metaList = new ArrayList<IMetadataTable>();
        IMetadataTable metaTable = new MetadataTable();
        metaTable.setTableName(uniqueName);
        metaList.add(metaTable);
        setMetadataList(metaList);
        setUniqueName(uniqueName);
        setComponent(component);

        ECodeLanguage currentLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage();
        setMultipleMethods(component.isMultipleMethods(currentLanguage));
    }

    public DataNode() {
        // nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }

    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }

    /**
     * Will return the first item of the subprocess. If "withCondition" is true, if there is links from type RunIf /
     * RunAfter / RunBefore, it will return the first element found. If "withCondition" is false, it will return the
     * first element with no active link from type Main/Ref/Iterate.<br>
     * <i><b>Note:</b></i> This function doesn't work if the node has several start points (will return a random
     * start node).
     * 
     * @param withCondition
     * @return Start Node found.
     */
    public DataNode getSubProcessStartNode(boolean withConditions) {
        if (!withConditions) {
            if ((getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) == 0)
                    && (getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) == 0)
                    && (getCurrentActiveLinksNbInput(EConnectionType.ITERATE) == 0)) {
                return this;
            }
        } else {
            int nb = 0;
            for (IConnection connection : getIncomingConnections()) {
                if (connection.isActivate()) {
                    nb++;
                }
            }
            if (nb == 0) {
                return this;
            }
        }
        DataConnection connec;

        for (int j = 0; j < getIncomingConnections().size(); j++) {
            connec = (DataConnection) getIncomingConnections().get(j);
            if (!connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                return ((DataNode) connec.getSource()).getSubProcessStartNode(withConditions);
            }
        }
        return null;
    }

    private int getCurrentActiveLinksNbInput(EConnectionType type) {
        int nb = 0;
        for (IConnection connection : getIncomingConnections()) {
            if (connection.isActivate() && connection.getLineStyle().equals(type)) {
                nb++;
            }
        }
        return nb;
    }
}
