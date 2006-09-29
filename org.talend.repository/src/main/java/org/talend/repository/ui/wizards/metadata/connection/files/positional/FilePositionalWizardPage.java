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
package org.talend.repository.ui.wizards.metadata.connection.files.positional;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.swt.utils.AbstractPositionalFileStepForm;
import org.talend.repository.ui.swt.utils.AbstractForm.ICheckListener;

/**
 * Use to create a new connection to a File. Page allows setting a file.
 */
public class FilePositionalWizardPage extends WizardPage {

    private ConnectionItem connectionItem;
    
    private int step;

    private AbstractPositionalFileStepForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    /**
     * DOC ocarbone FilePositionalWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public FilePositionalWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames) {
        super("wizardPage");
        this.connectionItem = connectionItem;
        this.step = step;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize FileWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;

        if (step == 1) {
            currentComposite = new FileStep1Form(parent, connectionItem, existingNames);
        } else if (step == 2) {
            currentComposite = new FileStep2Form(parent, connectionItem);
        } else if (step == 3) {
            MetadataTable metadataTable = (MetadataTable) ((PositionalFileConnection)connectionItem.getConnection()).getTables().get(0);
            currentComposite = new FileStep3Form(parent, connectionItem, metadataTable, TableHelper.getTableNames( ((PositionalFileConnection)connectionItem.getConnection()), metadataTable
                    .getLabel()));
        }
        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    FilePositionalWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    FilePositionalWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }

}
