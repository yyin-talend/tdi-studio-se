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
package org.talend.repository.ui.actions.metadata;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractCreateAction extends AContextualAction {

    private String[] existingNames;

    /**
     * DOC tguiu AbstractCreateAction constructor comment.
     */
    public AbstractCreateAction() {
        super();
    }

    public void init(final TreeViewer viewer, final IStructuredSelection selection) {
        setEnabled(false);
        Object o = selection.getFirstElement();
        if (selection.isEmpty() || selection.size() != 1 || !(o instanceof RepositoryNode)) {
            return;
        }
        init((RepositoryNode) o);
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
          //  setEnabled(false);
        }
    }

    protected abstract void init(RepositoryNode node);

    /**
     * DOC tguiu Comment method "getExistingNames".
     * 
     * @return
     */
    protected String[] getExistingNames() {
        if (existingNames == null) {
            init((RepositoryNode) ((IStructuredSelection) getSelection()).getFirstElement());
        }
        return existingNames;
    }

    protected void collectChildNames(final RepositoryNode node) {
        List<String> names = doCollectChildNames(node);
        existingNames = names.toArray(new String[names.size()]);
    }

    private List<String> doCollectChildNames(final RepositoryNode node) {
        List<String> names = new ArrayList<String>();
        for (RepositoryNode sibling : node.getChildren()) {
            names.add((String) sibling.getProperties(EProperties.LABEL));
        }
        return names;
    }

    protected void collectSiblingNames(final RepositoryNode node) {
        List<String> names = doCollectChildNames(node.getParent());
        names.remove((String) node.getProperties(EProperties.LABEL));
        existingNames = names.toArray(new String[names.size()]);
    }

}
