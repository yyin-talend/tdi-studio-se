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
package org.talend.repository.ui.wizards.license;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.repository.i18n.Messages;


/**
 * DOC mhirt  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public final class LicenseWizardDialog extends WizardDialog {
    /**
     * DOC mhirt LicenseWizardDialog constructor comment.
     * @param parentShell
     * @param newWizard
     */
    public LicenseWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        Button button = getButton(IDialogConstants.FINISH_ID);
        if (button != null) {
            button.setText(Messages.getString("LicenseWizard.accept"));
            GridData data = new GridData(300, 23);
            button.setLayoutData(data);
        }
    }
    
}
