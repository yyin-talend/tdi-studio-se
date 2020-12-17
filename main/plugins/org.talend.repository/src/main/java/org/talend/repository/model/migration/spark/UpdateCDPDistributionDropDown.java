package org.talend.repository.model.migration.spark;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractSparkJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

public class UpdateCDPDistributionDropDown extends AbstractSparkJobMigrationTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2020, 12, 15, 0, 0, 0);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(Item item) {
        try {
            ProcessType processType = getProcessType(item);
            if (processType != null) {
                final ParametersType parameters = processType.getParameters();
                if (parameters != null) {
                    boolean modified = false;
                    EList<ElementParameterType> elementParameters = parameters.getElementParameter();

                    ElementParameterType sparkVersion = null;
                    ElementParameterType distribution = null;
                    for (ElementParameterType elementParameter : elementParameters) {
                        if ("SPARK_VERSION".equals(elementParameter.getName())) {
                            sparkVersion = elementParameter;
                        }
                        if ("DISTRIBUTION".equals(elementParameter.getName())) {
                            distribution = elementParameter;
                        }
                    }

                    if (sparkVersion != null && distribution != null && sparkVersion.getValue().contains("CDP") && distribution.getValue().equals("CLOUDERA")) {
                        distribution.setValue("CDP");
                        modified = true;
                    }

                    if (modified) {
                        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        factory.save(item, true);
                        return ExecutionResult.SUCCESS_NO_ALERT;
                    }

                }
            }
        } catch (PersistenceException e) {
            return ExecutionResult.FAILURE;
        }
        return ExecutionResult.NOTHING_TO_DO;
    }
}
