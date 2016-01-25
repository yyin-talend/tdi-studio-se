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
package org.talend.componentdesigner.ui.action.provider;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.componentdesigner.i18n.internal.Messages;
import org.talend.componentdesigner.ui.dialog.ImportComponentDialog;
import org.talend.core.GlobalServiceRegister;
import org.talend.designer.core.ILocalProviderService;

/**
 * DOC slanglois class global comment. Detailled comment
 */
public class CopyFromPaletteActionProvider extends CommonActionProvider {

    private IAction copyProjectAction;

    private IProject selectedProject;

    public void init(ICommonActionExtensionSite anExtensionSite) {

        if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            copyProjectAction = new CopyFromPaletteAction();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void fillContextMenu(IMenuManager menu) {
        menu.insertBefore("group.edit", copyProjectAction); //$NON-NLS-1$
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof IProject) {
            selectedProject = ((IProject) obj);
        }
    }

    /**
     * DOC slanglois PushToPaletteActionProvider class global comment. Detailled comment
     */
    class CopyFromPaletteAction extends Action {

        public CopyFromPaletteAction() {
            super(Messages.getString("CopyFromPaletteActionProvider.Label")); //$NON-NLS-1$
            // setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.COPYCOMPONENT_ACTION2));
        }

        /*
         * (non-Javadoc) Method declared on IAction.
         */
        public void run() {
            ILocalProviderService service = (ILocalProviderService) GlobalServiceRegister.getDefault().getService(
                    ILocalProviderService.class);

            URL url = service.getPlugin().getBundle().getResource("/components"); //$NON-NLS-1$
            try {
                url = FileLocator.toFileURL(url);
            } catch (IOException e) {
                // e.printStackTrace();
                org.talend.componentdesigner.exception.ExceptionHandler.process(e);
            }
            String path = url.getFile();
            // path = "";

            ImportComponentDialog dialog = new ImportComponentDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell(), Messages.getString("CopyFromPaletteActionProvider.Label2"), null, path, selectedProject); //$NON-NLS-1$
            dialog.open();
        }
    }
}
