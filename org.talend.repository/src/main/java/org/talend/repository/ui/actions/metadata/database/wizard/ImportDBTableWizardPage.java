// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
