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

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Migration task to rename the DB Macro name DBTABLE to TABLE
 * 
 */
public class RenameDbMacroMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        if (getProject().getLanguage() == ECodeLanguage.PERL) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            convertItem(item);
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    private boolean convertItem(ProcessItem item) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        boolean modified = false;

        EList node = item.getProcess().getNode();
        for (Object n : node) {
            NodeType type = (NodeType) n;

            EList elementParameterList = type.getElementParameter();
            for (Object elem : elementParameterList) {
                ElementParameterType elemType = (ElementParameterType) elem;
                if (elemType.getName().equals("DBTABLE")) {
                    elemType.setName("TABLE");
                    modified = true;

                }
            }

        }

        if (modified) {
            factory.save(item, true);
        }
        return modified;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 2, 17, 12, 0, 0);
        return gc.getTime();
    }

}
