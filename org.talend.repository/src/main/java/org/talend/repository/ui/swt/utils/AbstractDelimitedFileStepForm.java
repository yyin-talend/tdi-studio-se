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
package org.talend.repository.ui.swt.utils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractDelimitedFileStepForm extends AbstractForm {

    protected int maximumRowsToPreview = CorePlugin.getDefault().getPreferenceStore().getInt(
            ITalendCorePrefConstants.PREVIEW_LIMIT);

    protected ConnectionItem connectionItem;

    protected DelimitedFileConnection connection;

    private WizardPage page = null;

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    /**
     * DOC ocarbone AbstractDelimitedFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    protected DelimitedFileConnection getConnection() {
        return (DelimitedFileConnection) connectionItem.getConnection();
    }

    /**
     * Getter for page.
     * 
     * @return the page
     */
    public WizardPage getWizardPage() {
        return this.page;
    }

    /**
     * Sets the page.
     * 
     * @param page the page to set
     */
    public void setWizardPage(WizardPage page) {
        this.page = page;
    }

}
