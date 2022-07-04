package org.talend.repository.model.migration;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

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

public class MigrateWorkdayModeTask extends AbstractJobMigrationTask {

    @Override
    public ExecutionResult execute(final Item item) {
        final ProcessType processType = this.getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }

        final String componentName = "WorkdayInput";

        final IComponentConversion migrateMode = new IComponentConversion() {
            @Override
            public void transform(final NodeType nodeType) {
                final String modeNodeName = "configuration.dataSet.mode";
                final ElementParameterType mode = ComponentUtilities.getNodeProperty(nodeType, modeNodeName);

                if (mode == null) {
                    final String authName = "configuration.dataSet.datastore.authentication";
                    final ElementParameterType auth = ComponentUtilities.getNodeProperty(nodeType, authName);
                    if (auth == null || "CLIENT_ID".equals(auth.getValue())) {
                        ComponentUtilities.setNodeValue(nodeType, modeNodeName, "WQL");
                    } else if ("LOGIN".equals(auth.getValue())) {
                        ComponentUtilities.setNodeValue(nodeType, modeNodeName, "RAAS");
                    } else if ("WS_SECURITY".equals(auth.getValue())) {
                        ComponentUtilities.setNodeValue(nodeType, modeNodeName, "RAAS");
                    }
                    throw new RuntimeException("Unknown auth type");
                }
            }
        };

        final IComponentFilter filter = new NameComponentFilter(componentName);

        try {
            boolean modified = ModifyComponentsAction.searchAndModify(item, processType,
                    filter, Collections.singletonList(migrateMode));

            return modified ? ExecutionResult.SUCCESS_NO_ALERT : ExecutionResult.NOTHING_TO_DO;
        } catch (PersistenceException | RuntimeException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2022, Calendar.JULY, 4, 0, 0, 0);
        return gc.getTime();
    }
}
