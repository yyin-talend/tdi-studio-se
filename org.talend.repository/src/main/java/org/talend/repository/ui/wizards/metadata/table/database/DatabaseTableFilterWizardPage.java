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
package org.talend.repository.ui.wizards.metadata.table.database;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class DatabaseTableFilterWizardPage extends WizardPage {

    private DatabaseTableFilterForm tableForm;

    private final TableInfoParameters tableInfoParameters;

    /**
     * DatabaseWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param tableInfoParameters
     * 
     * @param managerConnection
     * 
     * @param ISelection
     */
    public DatabaseTableFilterWizardPage(TableInfoParameters tableInfoParameters) {
        super("wizardPage"); //$NON-NLS-1$
        this.tableInfoParameters = tableInfoParameters;
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize TableWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {

        tableForm = new DatabaseTableFilterForm(parent, this);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    DatabaseTableFilterWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseTableFilterWizardPage.this.setPageComplete(false);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        tableForm.setListener(listener);
        setControl(tableForm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
     */
    @Override
    public IWizardPage getNextPage() {
        CorePlugin.getDefault().getPreferenceStore().setValue(DatabaseTableFilterForm.PREFS_NAMEFILTER,
                tableForm.getNameFilter());
        getTableInfoParameters().setNameFilters(tableForm.getFilters());
        IWizardPage nextPage = super.getNextPage();
        if (nextPage instanceof SelectorTableWizardPage) {
            ((SelectorTableWizardPage) nextPage).initControlData();
        }
        return nextPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        return true;
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

}
