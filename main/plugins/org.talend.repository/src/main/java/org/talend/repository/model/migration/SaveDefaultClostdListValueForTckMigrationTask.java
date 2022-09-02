package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

public class SaveDefaultClostdListValueForTckMigrationTask extends AbstractJobMigrationTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDefaultClostdListValueForTckMigrationTask.class);

    protected ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
    
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2022, 7, 28, 12, 0, 0);
        return gc.getTime();
    }

    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        if (processType == null) {
            LOGGER.info("Not process item: " + item.getProperty().getDisplayName());
            return ExecutionResult.NOTHING_TO_DO;
        }

        try {
            boolean needModified = false;
            for (Object nodeObject : processType.getNode()) {
                NodeType nodeType = (NodeType) nodeObject;
                for (Object paramObjectType : nodeType.getElementParameter()) {
                    ElementParameterType param = (ElementParameterType) paramObjectType;
                    if (param.getField() != null) {
                        if (EParameterFieldType.TECHNICAL.getName().equals(param.getField()) && param.getName().equals("TACOKIT_COMPONENT_ID")) {
                            needModified = true;
                            break;
                        }
                    }
                }
            }
            if (needModified) {
                LOGGER.info("Need to update, item: " + item.getProperty().getDisplayName());
                IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                IProcess jobProcess = service.getProcessFromItem(item);

                if (jobProcess instanceof IProcess2) {
                    IProcess2 process = (IProcess2) jobProcess;
                    ProcessType pt = process.saveXmlFile();
                    ((ProcessItem) item).setProcess(pt);
                    factory.save(item, true);
                    LOGGER.info("updated, item: " + item.getProperty().getDisplayName());
                } else {
                    LOGGER.info("Not IProcess2, item: " + item.getProperty().getDisplayName());
                }
                return ExecutionResult.SUCCESS_NO_ALERT;
            } else {
                return ExecutionResult.NOTHING_TO_DO;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

}
