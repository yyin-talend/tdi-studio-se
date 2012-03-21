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
package org.talend.repository.view.di.viewer.content.rules;

import org.talend.repository.model.RepositoryNode;
import org.talend.repository.tester.NodeTester;
import org.talend.repository.view.di.viewer.content.AbstractChildTopNodeContentProvider;

public abstract class AbstractRulesContentProvider extends AbstractChildTopNodeContentProvider {

    NodeTester nodeTester = new NodeTester();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.SingleTopLevelContentProvider#isRootNodeType(java.lang.Object)
     */
    @Override
    protected boolean isRootNodeType(Object element) {
        if (element instanceof RepositoryNode) {
            return nodeTester.isRulesTopNode((RepositoryNode) element);
        } else {
            return false;
        }

    }

}
