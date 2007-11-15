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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.XmlArray;
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

        if (step == 1) {
            currentComposite = new XmlFileStep1Form(parent, connectionItem, existingNames);
        } else if (step == 2) {
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
                    XmlFileWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    XmlFileWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#dispose()
     */
    @Override
    public void dispose() {
        XmlArray.setLimitToDefault();
        super.dispose();
    }

}
