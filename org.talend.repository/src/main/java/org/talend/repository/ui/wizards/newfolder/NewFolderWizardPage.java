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
package org.talend.repository.ui.wizards.newfolder;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;

/**
 * Page for new project details. <br/>
 * 
 * $Id$
 * 
 */
public class NewFolderWizardPage extends WizardPage {

    private static final String DESC = Messages.getString("NewFolderWizard.description"); //$NON-NLS-1$

    private Text nameText;

    private IStatus nameStatus;

    /**
     * Constructs a new NewProjectWizardPage.
     * 
     */
    public NewFolderWizardPage() {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(Messages.getString("NewFolderWizard.title")); //$NON-NLS-1$
        setDescription(DESC);

        nameStatus = createOkStatus();
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        // Name
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(Messages.getString("NewFolderWizard.label")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        setControl(container);
        addListeners();
        setPageComplete(false);
    }

    private void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });
    }

    /**
     * DOC ocarbone Comment method "checkField".
     */
    protected void checkFieldsValue() {
        // Field Name
        if (nameText.getText().length() == 0) {
            nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                    .getString("NewFolderWizard.nameEmpty"), null); //$NON-NLS-1$
        } else if (!Pattern.matches(RepositoryConstants.FOLDER_PATTERN, nameText.getText())) {
            nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                    .getString("NewFolderWizard.nameIncorrect"), null); //$NON-NLS-1$
        } else if (!((NewFolderWizard) getWizard()).isValid(nameText.getText())) {
            nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages.getString(
                    "NewFolderWizard.nameInvalid", nameText.getText()), null); //$NON-NLS-1$
        } else {
            nameStatus = createOkStatus();
        }
        updatePageStatus();
    }

    private void updatePageStatus() {
        setMessage(findMostSevere());
        setPageComplete(findMostSevere().getSeverity() != IStatus.ERROR);
    }

    private IStatus findMostSevere() {
        return nameStatus;
    }

    private void setMessage(IStatus status) {
        String message2 = status.getMessage();
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(message2);
            setMessage(""); //$NON-NLS-1$
        } else {
            if (message2.length() == 0) {
                message2 = DESC;
            }
            setMessage(message2);
            setErrorMessage(null);
        }
    }

    public String getName() {
        return nameText.getText();
    }

    private static IStatus createOkStatus() {
        return new Status(IStatus.OK, RepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }
}
