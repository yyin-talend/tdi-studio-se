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
package org.talend.repository.view.di.viewer.sorter;

import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.di.viewer.tester.JobTopNodesPropertyTester;
import org.talend.repository.view.di.viewer.tester.StandardJobNodePropertyTester;
import org.talend.repository.view.sorter.RepositoryNodeCompareSorter;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobDesignsWithStandardNodeChildrenSorter extends RepositoryNodeCompareSorter {

    protected StandardJobNodePropertyTester neededTester = new StandardJobNodePropertyTester();

    protected JobTopNodesPropertyTester topNodeTester = new JobTopNodesPropertyTester();

    protected void sortChildren(RepositoryNode parent, Object[] children) {
        if (topNodeTester.isJobDesignsNode(parent) && neededTester.needStandardNode(parent)) {
            sortChildren(children);
        }
    }

    @Override
    protected int compareNode(RepositoryNode n1, RepositoryNode n2) {
        return 0; // keep order
    }

    protected void swap(Object[] children, int first, int second) {
        if (first != second && first > -1 && second > -1 && first < children.length && second < children.length) {
            Object t = children[first];
            children[first] = children[second];
            children[second] = t;
        }
    }
}
