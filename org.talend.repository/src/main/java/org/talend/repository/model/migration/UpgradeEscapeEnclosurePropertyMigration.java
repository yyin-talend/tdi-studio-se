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

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * wliu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2009-04-07 17:06:40Z wliu $
 * 
 */
public class UpgradeEscapeEnclosurePropertyMigration extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.
     * ProcessItem)
     */
    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);

        if (getProject().getLanguage().equals(ECodeLanguage.JAVA) && processType != null) {

            final String name = "tFileInputDelimited"; //$NON-NLS-1$
            final String propertyCsv = "CSV_OPTION"; //$NON-NLS-1$
            final String propertyEscape = "ESCAPE_CHAR";//$NON-NLS-1$
            final String propertyEnclosure = "TEXT_ENCLOSURE";//$NON-NLS-1$

            IComponentFilter filert = new IComponentFilter() {

                public boolean accept(NodeType node) {

                    boolean toReturn = (name == null ? true : acceptS(node));
                    if (toReturn) {
                        String pValue = ComponentUtilities.getNodePropertyValue(node, propertyCsv);
                        toReturn = pValue.equals("true");
                    }
                    return toReturn;
                }

                public boolean acceptS(NodeType node) {
                    return node.getComponentName().equals(name);
                }
            };

            IComponentConversion conversion = new IComponentConversion() {

                public void transform(NodeType node) {
                    String pValueEscape = ComponentUtilities.getNodePropertyValue(node, propertyEscape); //$NON-NLS-1$ //$NON-NLS-2$
                    String pValueEnclosure = ComponentUtilities.getNodePropertyValue(node, propertyEnclosure);//$NON-NLS-1$ //$NON-NLS-2$

                    if (pValueEscape != null) {
                        String strTmp = pValueEscape.replaceAll("\"\"\"", "\"\\\\\"\"");
                        ComponentUtilities.setNodeValue(node, propertyEscape, strTmp);
                    }

                    if (pValueEnclosure != null) {
                        String strTmp = pValueEnclosure.replaceAll("\"\"\"", "\"\\\\\"\"");
                        ComponentUtilities.setNodeValue(node, propertyEnclosure, strTmp);
                    }
                }
            };
            List<IComponentConversion> cons = new ArrayList<IComponentConversion>();
            cons.add(conversion);

            try {
                ModifyComponentsAction.searchAndModify(item, processType, filert, cons);
                return ExecutionResult.SUCCESS_NO_ALERT;
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        } else {
            // do nothing
            return ExecutionResult.NOTHING_TO_DO;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2009, 3, 17, 12, 0, 0);
        return gc.getTime();
    }

}
