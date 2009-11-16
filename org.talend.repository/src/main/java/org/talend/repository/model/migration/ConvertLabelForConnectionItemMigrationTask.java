// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ConvertLabelForConnectionItemMigrationTask extends AbstractItemMigrationTask {

    private static final ProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    private boolean changed = false;

    private static final int MIN = 192;

    private static final int MAX = 255;

    @Override
    public ExecutionResult execute(Item item) {

        if (item instanceof ConnectionItem) {
            ConnectionItem conItem = (ConnectionItem) item;
            Connection connection = conItem.getConnection();
            EList tables = connection.getTables();
            for (Object tableObj : tables) {
                MetadataTable table = (MetadataTable) tableObj;
                String label = table.getLabel();
                if (label != null) {
                    String validateValue = MetadataTool.validateValue(label);
                    if (validateValue != null) {
                        table.setLabel(validateValue);
                        if (!table.getLabel().equals(label)) {
                            try {
                                FACTORY.save(conItem, true);
                            } catch (PersistenceException e) {
                                e.printStackTrace();
                            }
                            changed = true;
                        }
                    }
                }
            }
        }
        if (changed) {
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } else {
            return ExecutionResult.SUCCESS_NO_ALERT;
        }
    }

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        return toReturn;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 2, 17, 12, 0, 0);
        return gc.getTime();
    }
}
