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
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * This migration task did nothing but for importing job(s) from TOS 3.0.0. <br/>
 * 
 * 
 */
public class FileInputRegexContextVariableMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.
     * ProcessItem)
     */
    @Override
    public ExecutionResult execute(Item item) {
        // if (getProject().getLanguage() != ECodeLanguage.JAVA) {
        // return ExecutionResult.NOTHING_TO_DO;
        // }
        // try {
        // / addQuote(item);
        // return ExecutionResult.SUCCESS_NO_ALERT;
        // } catch (Exception e) {
        // return ExecutionResult.FAILURE;
        //
        // }
        return ExecutionResult.NOTHING_TO_DO;
    }

    private boolean addQuote(Item item) {
		ProcessType processType = getProcessType(item);
		if (processType == null) {
			return false;
		}
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        boolean modified = false;
        EList nodes = processType.getNode();
        for (Object n : nodes) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tFileInputRegex")) {
                EList elementParameterList = type.getElementParameter();
                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("HEADER") || elemType.getName().equals("FOOTER")
                            || elemType.getName().equals("LIMIT")) {
                        String oldV = elemType.getValue();
                        if (oldV == null || oldV.length() == 0) {
                            if (elemType.getName().equals("LIMIT")) {
                                oldV = "-1";
                            } else {
                                oldV = "0";
                            }
                        }
                        if (!oldV.contains("\"")) {
                            elemType.setValue("\"" + oldV + "\"");
                        }
                        modified = true;
                    }
                }
            }
        }
        return modified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
     */
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 8, 26, 12, 0, 0);
        return gc.getTime();
    }

}
