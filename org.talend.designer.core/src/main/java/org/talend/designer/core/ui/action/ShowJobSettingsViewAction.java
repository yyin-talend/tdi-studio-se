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
package org.talend.designer.core.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.model.business.BusinessType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.jobsettings.JobSettingsView;

/**
 * wchen class global comment. Detailled comment
 */
public class ShowJobSettingsViewAction extends Action {

    public static final String ID = "org.talend.designer.core.ui.editor.action.ShowJobSettingsViewAction"; //$NON-NLS-1$

    /**
     * DOC Administrator ShowJobSettingsViewAction constructor comment.
     */
    public ShowJobSettingsViewAction() {
        setId(ID);
        setImageDescriptor(ImageDescriptor.createFromFile(ECoreImage.class, ECoreImage.PROCESS_ICON.getPath()));
        setText(Messages.getString("ShowJobSettingsViewAction.showView")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        try {
            page.showView(getViewId());
            JobSettingsView view = (JobSettingsView) page.findView(getViewId());
            ISelection selection = CorePlugin.getDefault().getDiagramModelService().getBusinessEditorSelection(
                    page.getActiveEditor());
            if (selection instanceof IStructuredSelection) {
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                if (obj != null) {
                    if (BusinessType.PROCESS.equals(CorePlugin.getDefault().getDiagramModelService().getBusinessModelType(obj))) {
                        view.refresh();
                    } else {
                        view.refresh(false, obj);
                    }
                }
            }

        } catch (PartInitException e) {
            e.printStackTrace();
        }

    }

    public String getViewId() {
        return JobSettingsView.ID;
    }

    @Override
    public boolean isEnabled() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        ISelection selection = CorePlugin.getDefault().getDiagramModelService()
                .getBusinessEditorSelection(page.getActiveEditor());
        if (selection instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection) selection).getFirstElement();
            if (obj != null) {
                return true;
            }
        }
        return false;
    }
}
