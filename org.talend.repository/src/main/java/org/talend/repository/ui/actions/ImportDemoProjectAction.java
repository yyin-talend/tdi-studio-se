// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.ERepositoryImages;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 824 2006-12-01 15:49:55 +0000 (ven., 01 déc. 2006) smallet $
 * 
 */
public final class ImportDemoProjectAction extends Action {

    private static final String ACTION_TITLE = Messages.getString("ImportDemoProjectAction.actionTitle"); //$NON-NLS-1$

    private static final String ACTION_TOOLTIP = Messages.getString("ImportDemoProjectAction.actionTooltip"); //$NON-NLS-1$

    private static ImportDemoProjectAction singleton;

    public static ImportDemoProjectAction getInstance() {
        if (singleton == null) {
            singleton = new ImportDemoProjectAction();
        }
        return singleton;
    }

    private Shell shell;

    private ImportDemoProjectAction() {
        super();
        this.setText(ACTION_TITLE);
        this.setToolTipText(ACTION_TOOLTIP);
        this.setImageDescriptor(ImageProvider.getImageDesc(ERepositoryImages.IMPORT_PROJECTS_ACTION));
    }

    public void run() {
        try {
            Bundle bundle = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
            URL url = FileLocator.resolve(bundle.getEntry("resources/TALENDDEMOS.zip")); //$NON-NLS-1$
            String archiveFilePath = new Path(url.getFile()).toOSString();

            ZipFile zipFile = new ZipFile(archiveFilePath);
            ZipLeveledStructureProvider provider = new ZipLeveledStructureProvider(zipFile);

            ArrayList fileSystemObjects = new ArrayList();
            getFilesForProject(fileSystemObjects, provider, provider.getRoot());

            ImportOperation operation = new ImportOperation(new Path("TALENDDEMOS"), provider.getRoot(), provider, //$NON-NLS-1$
                    new MyOverwriteQuery(), fileSystemObjects);
            operation.setContext(shell);
            operation.run(null);

            MessageDialog.openInformation(shell, Messages.getString("ImportDemoProjectAction.messageDialogTitle.demoProject"), //$NON-NLS-1$
                    Messages.getString("ImportDemoProjectAction.messageDialogContent.demoProjectImportedSuccessfully")); //$NON-NLS-1$
        } catch (IOException e) {
            MessageBoxExceptionHandler.process(e, shell);
        } catch (InvocationTargetException e) {
            MessageBoxExceptionHandler.process(e, shell);
        } catch (InterruptedException e) {
            MessageBoxExceptionHandler.process(e, shell);
        }
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    protected boolean getFilesForProject(Collection files, IImportStructureProvider provider, Object entry) {
        List children = provider.getChildren(entry);
        Iterator childrenEnum = children.iterator();

        while (childrenEnum.hasNext()) {
            Object child = childrenEnum.next();
            // Add the child, this way we get every files except the project
            // folder itself which we don't want
            files.add(child);
            // We don't have isDirectory for tar so must check for children
            // instead
            if (provider.isFolder(child)) {
                getFilesForProject(files, provider, child);
            }
        }
        return true;
    }

    /**
     * 
     * DOC smallet ImportDemoProjectAction class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private class MyOverwriteQuery implements IOverwriteQuery {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.dialogs.IOverwriteQuery#queryOverwrite(java.lang.String)
         */
        public String queryOverwrite(String pathString) {
            return pathString;
        }

    }
}
