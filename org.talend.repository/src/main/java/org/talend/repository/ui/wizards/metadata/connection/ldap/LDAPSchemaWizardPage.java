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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private int step;

    private AbstractForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    private MetadataTable metadataTable;

    /**
     * DelimitedFileWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public LDAPSchemaWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;

        switch (step) {
        case 1:
            metadataTable = (MetadataTable) ((LDAPSchemaConnection) connectionItem.getConnection()).getTables().get(0);
            currentComposite = new LDAPSchemaStep1Form(parent, connectionItem, metadataTable, TableHelper
                    .getTableNames(((LDAPSchemaConnection) connectionItem.getConnection()), metadataTable.getLabel()));
            break;
        case 2:
            metadataTable = (MetadataTable) ((LDAPSchemaConnection) connectionItem.getConnection()).getTables().get(0);
            currentComposite = new LDAPSchemaStep2Form(parent, connectionItem, metadataTable, TableHelper
                    .getTableNames(((LDAPSchemaConnection) connectionItem.getConnection()), metadataTable.getLabel()));
            break;
        case 3:
            metadataTable = (MetadataTable) ((LDAPSchemaConnection) connectionItem.getConnection()).getTables().get(0);
            currentComposite = new LDAPSchemaStep3Form(parent, connectionItem);
            break;
        case 4:
            metadataTable = (MetadataTable) ((LDAPSchemaConnection) connectionItem.getConnection()).getTables().get(0);
            currentComposite = new LDAPSchemaStep4Form(parent, connectionItem);
            break;
        default:
            System.out.println("error...");
        }

        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    LDAPSchemaWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    LDAPSchemaWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }
}
