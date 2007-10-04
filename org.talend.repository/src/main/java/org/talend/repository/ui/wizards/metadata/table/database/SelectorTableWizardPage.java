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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class SelectorTableWizardPage extends WizardPage {

    private SelectorTableForm tableForm;

    private MetadataTable metadataTable;

    private ConnectionItem connectionItem;

    private boolean isRepositoryObjectEditable;

    /**
     * SelectorTableWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param ISelection
     */
    public SelectorTableWizardPage(ConnectionItem connectionItem, MetadataTable metadataTable, boolean isRepositoryObjectEditable) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize TableWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {

        tableForm = new SelectorTableForm(parent, connectionItem, this);
        tableForm.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    SelectorTableWizardPage.this.setPageComplete(false);
                }
                if (source.isStatusOnValid()) {
                    SelectorTableWizardPage.this.setPageComplete(true);
                }
            }
        };
        tableForm.setListener(listener);
        setControl(tableForm);
    }

    /**
     * DOC nrousseau Comment method "performCancel".
     */
    public void performCancel() {
        tableForm.performCancel();
    }

}
