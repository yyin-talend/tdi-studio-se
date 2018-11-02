package org.talend.repository.model.migration;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
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

public class ChangeDefaultDateCheckStrategyInSchemaComplianceCheck extends AbstractJobMigrationTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2018, 11, 2, 14, 0, 0);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            String componentsName = new String("tSchemaComplianceCheck");

            IComponentFilter filter = new NameComponentFilter(componentsName);
            IComponentConversion addOption = new IComponentConversion() {

                @Override
                public void transform(NodeType node) {

                    String propertyType = "CHECK";
                    String fastDateCheckPropertyName = "FAST_DATE_CHECK";
                    String strictDateCheckPropertyName = "STRICT_DATE_CHECK";

                    if (ComponentUtilities.getNodeProperty(node, strictDateCheckPropertyName) == null) {
                        ComponentUtilities.addNodeProperty(node, strictDateCheckPropertyName, propertyType);
                    }
                    ElementParameterType fastDateCheck = ComponentUtilities.getNodeProperty(node, fastDateCheckPropertyName);
                    if ((fastDateCheck != null) && ("true".equals(fastDateCheck.getValue()))) {
                        ComponentUtilities.setNodeValue(node, fastDateCheckPropertyName, "false");
                        return;
                    }

                    ComponentUtilities.setNodeValue(node, strictDateCheckPropertyName, "true");

                }

            };
            ModifyComponentsAction.searchAndModify(item, processType, filter, Arrays.<IComponentConversion>asList(addOption));
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }

    }
}
