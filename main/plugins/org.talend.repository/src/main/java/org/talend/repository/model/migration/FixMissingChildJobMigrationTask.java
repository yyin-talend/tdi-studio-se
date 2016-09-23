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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class FixMissingChildJobMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2016, 7, 19, 12, 00, 00);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        try {
            final ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            final ProcessType processType = getProcessType(item);

            List<IRepositoryViewObject> all = null;

            boolean modified = false;
            for (Object n : processType.getNode()) {
                if (n instanceof NodeType && (((NodeType) n).getComponentName().equals("tRunJob"))) {
                    NodeType node = (NodeType) n;
                    final ElementParameterType processIdParam = ComponentUtilities.getNodeProperty(node,
                            "PROCESS:PROCESS_TYPE_PROCESS");
                    if (processIdParam != null) { // found
                        final String processIdValue = processIdParam.getValue();
                        if (processIdValue == null || processIdValue.length() == 0) { // is empty
                            String processLabel = ComponentUtilities.getNodePropertyValue(node, "PROCESS");
                            // have label, try to find back the job id.
                            if (processLabel != null && processLabel.length() > 0) {
                                if (all == null) {
                                    all = factory.getAll(ERepositoryObjectType.PROCESS, true);
                                    all.addAll(factory.getAll(ERepositoryObjectType.PROCESS, true));
                                }
                                for (IRepositoryViewObject obj : all) {
                                    if (obj.getLabel().equals(processLabel)) {
                                        processIdParam.setValue(obj.getId());
                                        modified = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (modified) {
                factory.save(item, true);
                return ExecutionResult.SUCCESS_NO_ALERT;
            }

        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
        return ExecutionResult.NOTHING_TO_DO;
    }
}
