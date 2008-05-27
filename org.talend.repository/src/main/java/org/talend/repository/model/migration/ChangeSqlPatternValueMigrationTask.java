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
package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ChangeSqlPatternValueMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
     */
    public Date getOrder() {
        GregorianCalendar clendar = new GregorianCalendar(2008, 5, 27, 10, 0, 0);
        return clendar.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {

        boolean modified = false;

        for (Object objNode : item.getProcess().getNode()) {
            NodeType type = (NodeType) objNode;
            for (Object objElementParameter : type.getElementParameter()) {
                ElementParameterType elementParameterType = (ElementParameterType) objElementParameter;
                if (elementParameterType.getName().equals("SQLPATTERN_VALUE")) {
                    for (Object objElementValue : elementParameterType.getElementValue()) {
                        ElementValueType value = (ElementValueType) objElementValue;
                        String oldValue = (String) value.getValue();
                        if (!oldValue.contains("--")) {
                            String newValue = "tempid--" + oldValue;
                            value.setValue(newValue);
                            modified = true;
                        }
                    }
                }
            }
        }
        if (modified) {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.save(item, new boolean[] { true });
                return ExecutionResult.SUCCESS_NO_ALERT;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }

        return ExecutionResult.NOTHING_TO_DO;
    }
}
