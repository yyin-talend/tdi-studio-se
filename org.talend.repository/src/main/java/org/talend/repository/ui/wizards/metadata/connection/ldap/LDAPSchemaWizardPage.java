// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
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

    private IMetadataContextModeManager contextModeManager;

    /**
     * DelimitedFileWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public LDAPSchemaWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames, IMetadataContextModeManager contextModeManager) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.contextModeManager = contextModeManager;
    }

    /**
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;
        Connection connection = connectionItem.getConnection();
        LDAPSchemaConnection ldapConnection = (LDAPSchemaConnection) connection;
        EList tables = ldapConnection.getTables();
        switch (step) {
        case 1:
            // add for bug 7914
            if (tables.size() == 0) {
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                metadataTable.setId(factory.getNextId());
                connection.getTables().add(metadataTable);
            } else {
                metadataTable = (MetadataTable) tables.get(0);
            }

            currentComposite = new LDAPSchemaStep1Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    (ldapConnection), metadataTable.getLabel()), contextModeManager);
            break;

        case 2:
            metadataTable = (MetadataTable) tables.get(0);
            currentComposite = new LDAPSchemaStep2Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    (ldapConnection), metadataTable.getLabel()), contextModeManager);
            break;
        case 3:
            metadataTable = (MetadataTable) tables.get(0);
            currentComposite = new LDAPSchemaStep3Form(parent, connectionItem, contextModeManager);
            break;
        case 4:
            metadataTable = (MetadataTable) tables.get(0);
            currentComposite = new LDAPSchemaStep4Form(parent, connectionItem, contextModeManager);
            break;
        default:
            System.out.println("error..."); //$NON-NLS-1$
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
