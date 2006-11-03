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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.swt.utils.AbstractXmlFileStepForm;

/**
 * Use to create a new connection to a File. Page allows setting a file.
 */
public class XmlFileWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private int step;

    private AbstractXmlFileStepForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    /**
     * DOC ocarbone XmlFileWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public XmlFileWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames) {
        super("wizardPage");
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

        if (step == 1) {
            currentComposite = new XmlFileStep1Form(parent, connectionItem, existingNames);
        } else if (step == 2) {
            // MetadataSchema metadataSchema = (MetadataSchema)
            // ((XmlFileConnection)connectionItem.getConnection()).getSchema().get(0);
            // currentComposite = new XmlFileStep2Form(parent, connectionItem, metadataSchema);
            currentComposite = new XmlFileStep2Form(parent, connectionItem);

        } else if (step == 3) {
            MetadataTable metadataTable = (MetadataTable) ((XmlFileConnection) connectionItem.getConnection())
                    .getTables().get(0);
            currentComposite = new XmlFileStep3Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    ((XmlFileConnection) connectionItem.getConnection()), metadataTable.getLabel()));
        }

        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    // PTODO CAN : comment setPageComplete(true);
                    // XmlFileWizardPage.this.setPageComplete(false);
                    XmlFileWizardPage.this.setPageComplete(true);
                    setErrorMessage(source.getStatus());
                } else {
                    // PTODO CAN : comment setPageComplete(true);
                    // XmlFileWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    XmlFileWizardPage.this.setPageComplete(true);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }

}
