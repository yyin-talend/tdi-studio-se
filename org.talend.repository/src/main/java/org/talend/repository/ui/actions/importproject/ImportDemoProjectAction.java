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
package org.talend.repository.ui.actions.importproject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.ERepositoryImages;
import org.talend.resources.ResourcesPlugin;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 824 2006-12-01 15:49:55 +0000 (ven., 01 déc. 2006) smallet $
 * 
 */
public final class ImportDemoProjectAction extends Action {

    private static final String ACTION_TITLE = Messages.getString("ImportDemoProjectAction.actionTitle"); //$NON-NLS-1$

    private static final String ACTION_TOOLTIP = Messages.getString("ImportDemoProjectAction.actionTooltip"); //$NON-NLS-1$

    private String lastImportedName;

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
        final boolean perl = !openJavaOrPerl(shell);

        ProgressDialog progressDialog = new ProgressDialog(shell, 1) {

            private IProgressMonitor monitorWrap;

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitorWrap = new EventLoopProgressMonitor(monitor);

                try {
                    String archivePath;
                    String techName;
                    if (perl) {
                        archivePath = "resources/TALENDDEMOSPERL.zip"; //$NON-NLS-1$
                        techName = "TALENDDEMOS"; //$NON-NLS-1$
                        lastImportedName = "TalendDemos"; //$NON-NLS-1$
                    } else {
                        archivePath = "resources/TALENDDEMOSJAVA.zip"; //$NON-NLS-1$
                        techName = "TALENDDEMOSJAVA"; //$NON-NLS-1$
                        lastImportedName = "TalendDemosJava"; //$NON-NLS-1$
                    }

                    Bundle bundle = Platform.getBundle(ResourcesPlugin.PLUGIN_ID);
                    URL url = FileLocator.resolve(bundle.getEntry(archivePath));
                    String archiveFilePath = new Path(url.getFile()).toOSString();

                    ImportProjectsUtilities.importArchiveProject(shell, techName, archiveFilePath, monitorWrap);
                } catch (IOException e) {
                    throw new InvocationTargetException(e);
                } catch (TarException e) {
                    throw new InvocationTargetException(e);
                }

                monitorWrap.done();
                MessageDialog.openInformation(shell,
                        Messages.getString("ImportDemoProjectAction.messageDialogTitle.demoProject"), //$NON-NLS-1$
                        Messages.getString("ImportDemoProjectAction.messageDialogContent.demoProjectImportedSuccessfully")); //$NON-NLS-1$
            }

        };

        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            MessageBoxExceptionHandler.process(e.getTargetException(), shell);
        } catch (InterruptedException e) {
            // Nothing to do
        }
    }

    public String getProjectName() {
        return lastImportedName;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    public static boolean openJavaOrPerl(Shell parent) {
        String title = Messages.getString("ImportDemoProjectAction.languageSelectionDialog.title"); //$NON-NLS-1$
        String message = Messages.getString("ImportDemoProjectAction.languageSelectionDialog.message"); //$NON-NLS-1$
        String javabutton = Messages.getString("ImportDemoProjectAction.languageSelectionDialog.java"); //$NON-NLS-1$
        String perlbutton = Messages.getString("ImportDemoProjectAction.languageSelectionDialog.perl"); //$NON-NLS-1$
        MessageDialog dialog = new MessageDialog(parent, title, null, message, MessageDialog.QUESTION, new String[] { javabutton,
                perlbutton }, 0);
        return dialog.open() == 0;
    }
}
