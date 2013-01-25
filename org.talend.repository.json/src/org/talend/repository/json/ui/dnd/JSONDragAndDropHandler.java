// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.json.ui.dnd;

import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.IComponentName;
import org.talend.core.model.utils.IDragAndDropServiceHandler;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC wanghong class global comment. Detailled comment
 */
public class JSONDragAndDropHandler implements IDragAndDropServiceHandler {

    @Override
    public boolean canHandle(Connection connection) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IComponent> filterNeededComponents(Item item, RepositoryNode seletetedNode, ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setComponentValue(Connection connection, INode node, String repositoryValue) {
        // TODO Auto-generated method stub

    }

    @Override
    public ERepositoryObjectType getType(String repositoryType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleTableRelevantParameters(IElement ele, IMetadataTable metadataTable) {
        // TODO Auto-generated method stub

    }

}
