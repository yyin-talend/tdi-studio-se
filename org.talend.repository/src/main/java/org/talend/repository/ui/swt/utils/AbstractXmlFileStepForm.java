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
package org.talend.repository.ui.swt.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractXmlFileStepForm extends AbstractForm {

    protected ConnectionItem connectionItem;

    protected XmlFileConnection connection;

    /**
     * DOC cantoine AbstractXmlFileStepForm constructor comment. Use to step1
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    /**
     * DOC cantoine AbstractXmlFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC cantoine AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    protected XmlFileConnection getConnection() {
        return (XmlFileConnection) connectionItem.getConnection();
    }
}
