// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.CorePlugin;
import org.talend.core.PluginChecker;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC achen class global comment. Detailled comment
 */
public class MergeReferenceProjectAction extends AContextualAction {

    private boolean visible;

    private static MergeReferenceProjectAction singleton;

    private IPreferenceStore preferenceStore = RepositoryManager.getPreferenceStore();

    public MergeReferenceProjectAction() {
        super();
        // this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EDIT_ICON));
        //        this.setActionDefinitionId("deleteItem"); //$NON-NLS-1$
        setText("Merge reference project");
        singleton = this;

        setChecked(preferenceStore.getBoolean(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT));
    }

    /**
     * Getter for visible.
     * 
     * @return the visible
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visible.
     * 
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        if (PluginChecker.isRefProjectLoaded()) {
            visible = selection.isEmpty();
            if (selection.isEmpty()) {
                setChecked(preferenceStore.getBoolean(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT));
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
        super.run();
        preferenceStore.setValue(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT, isChecked());
        CorePlugin.getDefault().savePluginPreferences();
        IRepositoryView view = RepositoryManager.getRepositoryView();
        view.refresh();
    }
}
