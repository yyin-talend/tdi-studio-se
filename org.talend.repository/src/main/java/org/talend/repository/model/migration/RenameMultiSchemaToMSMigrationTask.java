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

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.RenameComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC wliu class global comment. Detailled comment <br/> Rename tFileInputXMLMultiSchema to tFileInputMSXML Rename<br/>
 * tFileOutputXMLMultiSchema to tFileOutputMSXML <br/> $Id: talend.epf 1 2009-02-31 17:06:40Z wliu $
 * 
 */
public class RenameMultiSchemaToMSMigrationTask extends AbstractJobMigrationTask {

    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);

        if (getProject().getLanguage() == ECodeLanguage.JAVA && processType != null) {
            try {
                IComponentFilter filter1 = new NameComponentFilter("tFileInputXMLMultiSchema"); //$NON-NLS-1$
                IComponentFilter filter2 = new NameComponentFilter("tFileOutputXMLMultiSchema");//$NON-NLS-2$
                IComponentConversion changeNodeNameConversion = new IComponentConversion() {

                    public void transform(NodeType node) {

                        ProcessType item = (ProcessType) node.eContainer();
                        for (Object o : item.getNode()) {
                            NodeType nt = (NodeType) o;
                            for (Object o1 : nt.getElementParameter()) {
                                ElementParameterType t = (ElementParameterType) o1;
                                String value = t.getValue();
                                if (value != null) {
                                    if (value.contains("tFileInputXMLMultiSchema")) { //$NON-NLS-1$
                                        String replaceAll = value.replaceAll("tFileInputXMLMultiSchema", "tFileInputMSXML"); //$NON-NLS-1$ //$NON-NLS-2$
                                        t.setValue(replaceAll);
                                    }
                                    if (value.contains("tOXMLMS")) {
                                        String replaceAll = value.replaceAll("tOXMLMS", "tFileOutputMSXML"); //$NON-NLS-1$ //$NON-NLS-2$
                                        t.setValue(replaceAll);
                                    }
                                }
                            }
                        }
                    }
                };
                IComponentConversion renameComponentConversion1 = new RenameComponentConversion("tFileInputMSXML"); //$NON-NLS-1$

                IComponentConversion renameComponentConversion2 = new RenameComponentConversion("tFileOutputMSXML"); //$NON-NLS-1$

                ModifyComponentsAction.searchAndModify(item, processType, filter1, Arrays.<IComponentConversion> asList(
                        renameComponentConversion1, changeNodeNameConversion));

                ModifyComponentsAction.searchAndModify(item, processType, filter2, Arrays.<IComponentConversion> asList(
                        renameComponentConversion2, changeNodeNameConversion));

                return ExecutionResult.SUCCESS_WITH_ALERT;
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        } else {
            return ExecutionResult.NOTHING_TO_DO;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2009, 2, 17, 12, 0, 0);
        return gc.getTime();
    }

}
