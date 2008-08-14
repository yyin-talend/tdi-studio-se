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

import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class MigrationTaskForIssue4449 extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        if (getProject().getLanguage() == ECodeLanguage.JAVA) {
            boolean isModified = false;
            ProcessType processType = item.getProcess();
            for (Object nodeType : processType.getNode()) {
                NodeType tmpNodeType = (NodeType) nodeType;
                ElementParameterType property = ComponentUtilities.getNodeProperty(tmpNodeType, "TABLE_ACTION");
                if (property != null && property.getValue().equals("CLEAR")) {
                    property.setValue("TRUNCATE");
                    isModified = true;
                }
            }
            if (isModified) {
                ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                try {
                    factory.save(item, true);
                } catch (PersistenceException e) {
                    throw new RuntimeException(e);
                }
            }
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } else {
            return ExecutionResult.NOTHING_TO_DO;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 8, 5, 16, 0, 0);
        return gc.getTime();
    }
}
