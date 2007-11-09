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
package org.talend.repository.ui.actions.metadata.database.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportDBTableWizardPage extends WizardPage {

    private ImportDBTableForm importForm;

    public ImportDBTableWizardPage() {
        super(""); //$NON-NLS-1$

    }

    public void createControl(final Composite parent) {
        importForm = new ImportDBTableForm(parent);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    ImportDBTableWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    ImportDBTableWizardPage.this.setPageComplete(true);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        importForm.setListener(listener);
        setControl(importForm);

    }

    public ImportDBTableForm getFormSetting() {
        return importForm;
    }
}
