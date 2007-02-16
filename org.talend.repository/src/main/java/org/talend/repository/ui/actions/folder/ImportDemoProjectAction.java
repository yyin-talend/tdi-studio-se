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
package org.talend.repository.ui.actions.folder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.ERepositoryImages;
import org.talend.repository.ui.actions.importproject.ImportProjectsUtilities;

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
            ImportProjectsUtilities.importDemoProject(shell);
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

    public String getProjectName() {
        return ImportProjectsUtilities.TALENDDEMOS_NAME;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

}
