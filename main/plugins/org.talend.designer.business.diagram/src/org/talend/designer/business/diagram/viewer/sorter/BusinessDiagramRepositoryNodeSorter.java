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
package org.talend.designer.business.diagram.viewer.sorter;

import org.talend.designer.business.diagram.viewer.tester.BusinessDiagramNodeTester;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.di.viewer.sorter.DocumentationNodeChildrenSorter;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BusinessDiagramRepositoryNodeSorter extends DocumentationNodeChildrenSorter {

    private BusinessDiagramNodeTester bmTester = new BusinessDiagramNodeTester();

    /**
     * always on top
     */
    @Override
    protected int compareNode(RepositoryNode n1, RepositoryNode n2) {
        if (bmTester.isBusinessDiagramTopNode(n1)) {
            return -1;
        }
        if (bmTester.isBusinessDiagramTopNode(n2)) {
            return 1;
        }
        return 0;
    }

}
