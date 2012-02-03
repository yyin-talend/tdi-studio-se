// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.dnd;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.DefaultRepositoryComponentDndFilter;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TAmazonOracleDndFilter extends DefaultRepositoryComponentDndFilter {

    /**
     * DOC ggu TAmazonOracleDndFilter constructor comment.
     */
    public TAmazonOracleDndFilter() {
        super();
    }

    @Override
    public boolean except(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (component != null && (component.getName().equals("tAmazonOracleInput") //$NON-NLS-1$
                || component.getName().equals("tAmazonOracleOutput"))) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    @Override
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        return false; // no valid component
    }

}
