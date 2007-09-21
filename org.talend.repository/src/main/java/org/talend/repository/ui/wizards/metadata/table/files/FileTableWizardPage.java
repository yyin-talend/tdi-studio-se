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
package org.talend.repository.ui.wizards.metadata.table.files;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.ldif.LdifFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.positional.FileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.regexp.RegexpFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaStep2Form;
import org.talend.repository.ui.wizards.metadata.connection.ldap.LDAPSchemaStep4Form;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class FileTableWizardPage extends WizardPage {

    private MetadataTable metadataTable;

    private ConnectionItem connectionItem;

    private boolean isRepositoryObjectEditable;

    /**
     * DatabaseWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param existingNames
     * 
     * @param ISelection
     */
    public FileTableWizardPage(ConnectionItem connectionItem, MetadataTable metadataTable,
            boolean isRepositoryObjectEditable) {
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

        final AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    FileTableWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    FileTableWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };

        Composite theForm = null;
        Connection connection = metadataTable.getConnection();
        theForm = (Composite) new ConnectionSwitch() {

            @Override
            public Object caseDelimitedFileConnection(final DelimitedFileConnection object) {
                DelimitedFileStep3Form delimitedFileStep3Form = new DelimitedFileStep3Form(parent, connectionItem,
                        metadataTable, TableHelper.getTableNames(object, metadataTable.getLabel()));
                delimitedFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                delimitedFileStep3Form.setListener(listener);
                return delimitedFileStep3Form;
            }

            @Override
            public Object casePositionalFileConnection(final PositionalFileConnection object) {
                FileStep3Form fileStep3Form = new FileStep3Form(parent, connectionItem, metadataTable, TableHelper
                        .getTableNames(object, metadataTable.getLabel()));
                fileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                fileStep3Form.setListener(listener);
                return fileStep3Form;
            }

            @Override
            public Object caseRegexpFileConnection(final RegexpFileConnection object) {
                RegexpFileStep3Form regexpFileStep3Form = new RegexpFileStep3Form(parent, connectionItem,
                        metadataTable, TableHelper.getTableNames(object, metadataTable.getLabel()));
                regexpFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                regexpFileStep3Form.setListener(listener);
                return regexpFileStep3Form;
            }

            public Object caseXmlFileConnection(final XmlFileConnection object) {
                XmlFileStep3Form xmlFileStep3Form = new XmlFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                xmlFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                xmlFileStep3Form.setListener(listener);
                return xmlFileStep3Form;
            }

            @Override
            public Object caseLdifFileConnection(final LdifFileConnection object) {
                LdifFileStep3Form ldifFileStep3Form = new LdifFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                ldifFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                ldifFileStep3Form.setListener(listener);
                return ldifFileStep3Form;
            }

            @Override
            public Object caseGenericSchemaConnection(final GenericSchemaConnection object) {
                GenericSchemaStep2Form genericSchemaStep2Form = new GenericSchemaStep2Form(parent, connectionItem,
                        metadataTable, TableHelper.getTableNames(object, metadataTable.getLabel()));
                genericSchemaStep2Form.setReadOnly(!isRepositoryObjectEditable);
                genericSchemaStep2Form.setListener(listener);
                return genericSchemaStep2Form;
            }
            
            @Override
            public Object caseLDAPSchemaConnection(final LDAPSchemaConnection object) {
                LDAPSchemaStep4Form ldapSchemaStep4Form = new LDAPSchemaStep4Form(parent, connectionItem);
                ldapSchemaStep4Form.setReadOnly(!isRepositoryObjectEditable);
                ldapSchemaStep4Form.setListener(listener);
                return ldapSchemaStep4Form;
            }

        }.doSwitch(connection);
        setControl(theForm);
    }
}
