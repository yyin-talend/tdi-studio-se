// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

// import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ChangeDefaultValue4tRedShiftOutput extends AbstractJobMigrationTask {

    public ChangeDefaultValue4tRedShiftOutput() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }

        String[] componentsName = new String[] { "tRedshiftOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        IComponentConversion changeJDBCDriverJarType = new IComponentConversion() {

            public void transform(NodeType node) {
                ElementParameterType dataAction = ComponentUtilities.getNodeProperty(node, "DATA_ACTION");
                ElementParameterType isExtendInsert = ComponentUtilities.getNodeProperty(node, "EXTENDINSERT");
                if ("INSERT".equals(dataAction.getValue()) && "true".equalsIgnoreCase(isExtendInsert.getValue())) {
                    if (ComponentUtilities.getNodeProperty(node, "TABLE_WITH_SCHEMA") == null) {
                        ComponentUtilities.addNodeProperty(node, "TABLE_WITH_SCHEMA", "CHECK");
                        ComponentUtilities.getNodeProperty(node, "TABLE_WITH_SCHEMA").setValue("true");
                    }
                }
            }
        };

        for (String name : componentsName) {
            IComponentFilter filter = new NameComponentFilter(name); //$NON-NLS-1$

            try {
                ModifyComponentsAction.searchAndModify(item, processType, filter,
                        Arrays.<IComponentConversion> asList(changeJDBCDriverJarType));
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }

        return ExecutionResult.SUCCESS_WITH_ALERT;

    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2016, 4, 31, 12, 0, 0);
        return gc.getTime();
    }

}
