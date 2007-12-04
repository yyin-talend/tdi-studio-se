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
package org.talend.repository.ui.actions.metadata.importing;

import java.io.File;

import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class DelimitedConnectionTablesHelper extends AbstractImportingTablesHelper {

    /**
     * DOC ggu DelimitedConnectionTablesHelper constructor comment.
     * 
     * @param file
     */
    public DelimitedConnectionTablesHelper(File file) {
        super(ERepositoryObjectType.METADATA_FILE_DELIMITED, file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.importing.delimited.AbstractImportingTablesHelper#createConnectionItem(org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean)
     */
    @Override
    protected ConnectionItem createConnectionItem(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getFile())) {
            processRecords.addRejectedRecords(RejectedType.FILE, bean.getFile(), bean.getName());
            return null;
        }
        DelimitedFileConnection conn = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        conn.setFilePath(bean.getFile());

        DelimitedFileConnectionItem connItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
        createProperty(connItem, bean);
        connItem.setConnection(conn);

        setConnectionCreated(true);

        return connItem;

    }

}
