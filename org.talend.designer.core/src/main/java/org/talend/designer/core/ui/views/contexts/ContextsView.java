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
package org.talend.designer.core.ui.views.contexts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class ContextsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.ContextsView";

    private MultiPageTalendEditor part;

    private Process process;

    public ContextsView() {
    }

    private ContextViewComposite contextComposite;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.horizontalAlignment = SWT.FILL;
        parent.setLayoutData(gridData);
        getPart();

        contextComposite = new ContextViewComposite(parent, this);
        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.verticalAlignment = SWT.FILL;
        gd.horizontalAlignment = SWT.FILL;
        contextComposite.setLayoutData(gd);

        refresh();

    }

    public boolean updateContextFromRepository() {
        String repositoryId = process.getRepositoryId();
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ContextItem> contextItemList = null;
        try {
            contextItemList = factory.getContextItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (contextItemList != null) {
            for (ContextItem item : contextItemList) {
                if (factory.getStatus(item) != ERepositoryStatus.DELETED) {
                    String id = item.getProperty().getId();
                    if (id.equals(repositoryId)) {
                        IContextManager tmpManager = new JobContextManager(item.getContext(), item.getDefaultContext());
                        if (tmpManager.sameAs(getContextManager())) {
                            return false;
                        }
                        getContextManager().setListContext(tmpManager.getListContext());
                        getContextManager().setDefaultContext(tmpManager.getDefaultContext());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * bqian Comment method "getContextManager".
     * 
     * @return
     */
    private IContextManager getContextManager() {
        return part.getProcess().getContextManager();
    }

    public void updateContextView() {
        getPart();
        if (part != null) {
            boolean modified = updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        contextComposite.setReadOnly(false);
        contextComposite.refresh();
    }

    public void updateContextView(boolean isBuildIn) {
        getPart();
        if (part != null) {
            boolean modified = updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        contextComposite.setReadOnly(!isBuildIn);
        contextComposite.refresh();
    }

    public void updateContextView(boolean isBuildIn, boolean isDisposeAll) {
        getPart();
        if (part != null) {
            boolean modified = updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        contextComposite.setReadOnly(!isBuildIn);
        contextComposite.refresh();
    }

    public void refresh() {
        getPart();
        if (part != null) {
            boolean modified = updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        contextComposite.setPart(part);
    }

    @Override
    public void setFocus() {
        contextComposite.setFocus();
    }

    public void setPartName(String title) {
        String viewName = "Contexts"; //$NON-NLS-1$

        if (!title.equals("")) { //$NON-NLS-1$
            viewName = viewName + "(" + title + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        super.setPartName(viewName);
    }

    private void getPart() {
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) activeEditor;
            process = part.getTalendEditor().getProcess();
        } else {
            part = null;
        }
    }
}
