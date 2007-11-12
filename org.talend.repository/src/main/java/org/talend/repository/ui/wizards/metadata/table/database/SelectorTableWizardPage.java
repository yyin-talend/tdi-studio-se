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
package org.talend.repository.ui.wizards.metadata.table.database;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class SelectorTableWizardPage extends WizardPage {

    private SelectorTableForm tableForm;

    private final MetadataTable metadataTable;

    private final ConnectionItem connectionItem;

    private boolean isRepositoryObjectEditable;

    private final TableInfoParameters tableInfoParameters;

    /**
     * SelectorTableWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param tableInfoParameters
     * 
     * @param ISelection
     */
    public SelectorTableWizardPage(ConnectionItem connectionItem, MetadataTable metadataTable,
            boolean isRepositoryObjectEditable, TableInfoParameters tableInfoParameters) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.tableInfoParameters = tableInfoParameters;
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
        if (tableForm != null) {
            tableForm.performCancel();
        }
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

    public void initControlData() {
        tableForm.initControlData();
    }
}
