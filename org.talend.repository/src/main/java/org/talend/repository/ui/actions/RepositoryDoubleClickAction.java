// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.QueryEMFRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryDoubleClickAction extends Action {

    private List<ITreeContextualAction> contextualsActions = new ArrayList<ITreeContextualAction>();

    private IRepositoryView view;

    public RepositoryDoubleClickAction(IRepositoryView view, List<ITreeContextualAction> contextualsActionsList) {
        super();
        this.view = view;
        for (ITreeContextualAction current : contextualsActionsList) {
            if (current.isDoubleClickAction()) {
                contextualsActions.add(current);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ISelection selection = getSelection();
        if (selection == null) {
            return;
        }

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj == null) {
            return;
        }

        RepositoryNode node = (RepositoryNode) obj;

        if (node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.STABLE_SYSTEM_FOLDER
                || node.getType() == ENodeType.SYSTEM_FOLDER) {
            view.expand(node);
            view.refresh();
        } else {
            ITreeContextualAction actionToRun = getAction(node);
            if (!(actionToRun == null)) {
                actionToRun.init((TreeViewer) getViewPart().getViewer(), (IStructuredSelection) selection);
                actionToRun.run();
                // showView();
            }
        }
    }

    private ITreeContextualAction getAction(RepositoryNode obj) {
        for (ITreeContextualAction current : contextualsActions) {

            ERepositoryObjectType nodeType = (ERepositoryObjectType) obj.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_CON_TABLE)) {
                if (current.getClassForDoubleClick().equals(IMetadataTable.class)) {
                    return current;
                }
                // Added for v2.0.0
            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_CON_QUERY)) {
                if (current.getClassForDoubleClick().equals(QueryEMFRepositoryNode.class)) {
                    return current;
                }
                // end
            } else if (obj.getObject() != null
                    && current.getClassForDoubleClick().isInstance(obj.getObject().getProperty().getItem())) {
                return current;
            }
        }
        return null;
    }

    protected ISelection getSelection() {
        return getViewPart().getViewer().getSelection();
    }

    protected IRepositoryView getViewPart() {
        showView();

        IViewPart viewPart = (IViewPart) getActivePage().getActivePart();
        return (IRepositoryView) viewPart;
    }

    /**
     * DOC smallet Comment method "showView".
     */
    private void showView() {
        // Added to fix a newly appeared bug : ClassCastException on IViewPart
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        try {
            page.showView("org.talend.repository.views.repository"); //$NON-NLS-1$
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        // ----------------
    }

    protected IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }
}
