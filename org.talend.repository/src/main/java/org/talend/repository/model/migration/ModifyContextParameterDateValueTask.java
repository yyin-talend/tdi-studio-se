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

import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC zwang class global comment. Detailled comment
 */
public class ModifyContextParameterDateValueTask extends AbstractJobMigrationTask {

    private static final String DATE_TYPE = "id_Date";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        // TODO Auto-generated method stub
        try {
            List<ContextType> contextList = item.getProcess().getContext();
            modifyDateValue(contextList);

            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            List<ContextItem> contextItemList = factory.getContextItem();
            for (ContextItem contextItem : contextItemList) {
                contextList = contextItem.getContext();
                modifyDateValue(contextList);
            }

            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * DOC zwang Comment method "modifyDateValue".
     * 
     * @param item
     */
    private void modifyDateValue(List<ContextType> contextList) {
        // TODO Auto-generated method stub
        for (ContextType context : contextList) {
            for (ContextParameterType contextParameterType : (List<ContextParameterType>) context.getContextParameter()) {
                if (ModifyContextParameterDateValueTask.DATE_TYPE.equals(contextParameterType.getType())
                        && "null".equals(contextParameterType.getValue())) {
                    contextParameterType.setValue("");
                }
            }
        }
    }

}
