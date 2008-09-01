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
package org.talend.designer.core.ui.hierarchy;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: OpenRepositoryJobHierarchyAction.java 17216 2008-08-22 08:24:11Z yzhang $
 * 
 */
public class OpenRepositoryJobHierarchyAction extends AContextualAction {

    private static final String CREATE_LABEL = Messages.getString("OpenRepositoryJobHierarchyAction.createJob"); //$NON-NLS-1$

    public OpenRepositoryJobHierarchyAction() {
        super();
        setText("Open Job Hierarchy");
        setToolTipText("Open a Job Hierarchy on the Selected Element");
        setDescription("Open a job hierarchy on the selected element");
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();

        RepositoryNode node = (RepositoryNode) obj;
        Property property = (Property) node.getObject().getProperty();
        ProcessItem processItem = null;

        ItemCacheManager.clearCache();
        Assert.isTrue(property.getItem() instanceof ProcessItem);

        // TODO should use a fake Process here to replace the real Process.
        Process loadedProcess = new Process(property);
        loadedProcess.loadXmlFile();

        OpenJobHierarchyAction openAction = new OpenJobHierarchyAction(this.getViewPart());
        openAction.run(loadedProcess);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = selection.size() == 1;

        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;

            switch (node.getType()) {
            case REPOSITORY_ELEMENT:
                if (node.getObjectType() != ERepositoryObjectType.PROCESS) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

}
