// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.wizards.metadata.table.database;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.ManagerConnection;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class DatabaseTableWizardPage extends WizardPage {

    private DatabaseTableForm tableForm;

    private MetadataTable metadataTable;

    private ConnectionItem connectionItem;

    private boolean isRepositoryObjectEditable;

    private ManagerConnection managerConnection;

    /**
     * DatabaseWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param managerConnection
     * 
     * @param ISelection
     */
    public DatabaseTableWizardPage(ManagerConnection managerConnection, ConnectionItem connectionItem,
            MetadataTable metadataTable, boolean isRepositoryObjectEditable) {
        super("wizardPage"); //$NON-NLS-1$
        this.managerConnection = managerConnection;
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

        tableForm = new DatabaseTableForm(parent, connectionItem, metadataTable, managerConnection,this);
        tableForm.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    DatabaseTableWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseTableWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        tableForm.setListener(listener);
        setControl(tableForm);
    }

}
