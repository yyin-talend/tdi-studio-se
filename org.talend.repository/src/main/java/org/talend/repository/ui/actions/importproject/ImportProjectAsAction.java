// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.ERepositoryImages;
import org.talend.repository.ui.wizards.newproject.ImportProjectAsWizard;
import org.talend.repository.ui.wizards.newproject.ImportProjectWizardDialog;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 824 2006-12-01 15:49:55 +0000 (ven., 01 déc. 2006) smallet $
 * 
 */
public final class ImportProjectAsAction extends Action {

    private static final String ACTION_TITLE = Messages.getString("ImportProjectAsAction.actionTitle"); //$NON-NLS-1$

    private static final String ACTION_TOOLTIP = Messages.getString("ImportProjectAsAction.actionTooltip",
            ((IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class)).getShortProductName()); //$NON-NLS-1$

    private static ImportProjectAsAction singleton;

    public static ImportProjectAsAction getInstance() {
        if (singleton == null) {
            singleton = new ImportProjectAsAction();
        }
        return singleton;
    }

    private String project;

    private ImportProjectAsAction() {
        super();
        this.setText(ACTION_TITLE);
        this.setToolTipText(ACTION_TOOLTIP);
        this.setImageDescriptor(ImageProvider.getImageDesc(ERepositoryImages.IMPORT_PROJECTS_ACTION));
    }

    public void run() {
        ImportProjectAsWizard docWizard = new ImportProjectAsWizard();
        WizardDialog dlg = new ImportProjectWizardDialog(Display.getCurrent().getActiveShell(), docWizard);
        
        
        if (dlg.open() == IDialogConstants.OK_ID) {
        	
            project = docWizard.getProjectName();
            
        } else {
            project = null;
        }

    }

    public String getProjectName() {
        return project;
    }
}
