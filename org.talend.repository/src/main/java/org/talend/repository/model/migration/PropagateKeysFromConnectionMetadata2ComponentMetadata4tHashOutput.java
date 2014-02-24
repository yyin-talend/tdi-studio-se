// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.joblet.model.JobletNode;
import org.talend.designer.joblet.model.JobletProcess;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class PropagateKeysFromConnectionMetadata2ComponentMetadata4tHashOutput extends AbstractJobMigrationTask {

    @Override
    public ExecutionResult execute(Item item) {
        final ProcessType processType = getProcessType(item);
        String[] compNames = { "tHashOutput" }; //$NON-NLS-1$

        IComponentConversion action = new IComponentConversion() {

            public void transform(NodeType node) {
                if (node == null) {
                    return;
                }
                // get name
                String uname = ComponentUtilities.getNodeUniqueName(node);
                // get source component name
                String sourceName = getSourceName(processType, uname);
                // get source component
                NodeType sourceNode = ComponentUtilities.getNodeTypeFromUniqueName(processType, sourceName);
                if (sourceNode == null && processType instanceof JobletProcess) {
                    List<JobletNode> jobletNodes = ((JobletProcess) processType).getJobletNodes();
                    if (jobletNodes != null) {
                        for (JobletNode nodeType : jobletNodes) {
                            if (ComponentUtilities.getNodeUniqueName(nodeType).equals(sourceName)) {
                                sourceNode = nodeType;
                                break;
                            }
                        }
                    }
                }

                propagateKeys(node, sourceNode);
            }

        };

        for (String name : compNames) {
            IComponentFilter filter = new NameComponentFilter(name);

            try {
                ModifyComponentsAction.searchAndModify(item, processType, filter, Arrays.<IComponentConversion> asList(action));
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }

        return ExecutionResult.SUCCESS_NO_ALERT;

    }

    private void propagateKeys(NodeType node, NodeType sourceNode) {
        if (sourceNode == null) {
            return;
        }
        List<MetadataType> metadatas = node.getMetadata();
        List<MetadataType> sourceMetadatas = sourceNode.getMetadata();
        if (metadatas == null || sourceMetadatas == null || metadatas.size() != sourceMetadatas.size()) {
            return;
        }
        for (int i = 0; i < sourceMetadatas.size(); i++) {
            List<ColumnType> sourceMetadata = sourceMetadatas.get(i).getColumn();
            List<ColumnType> metadata = metadatas.get(i).getColumn();
            if (metadata == null || sourceMetadata == null || metadata.size() != sourceMetadata.size()) {
                return;
            }
            for (int j = 0; j < sourceMetadata.size(); j++) {
                ColumnType sourceColumn = sourceMetadata.get(j);
                ColumnType column = metadata.get(j);
                column.setKey(sourceColumn.isKey());
            }
        }
    }

    private String getSourceName(ProcessType processType, String uname) {
        if (uname == null) {
            return null;
        }
        String result = null;
        List<ConnectionType> conns = processType.getConnection();
        if (conns == null) {
            return null;
        }
        for (ConnectionType conn : conns) {
            if (conn == null) {
                continue;
            }
            String targetName = conn.getTarget();
            if (uname.equals(targetName)) {
                result = conn.getSource();
                break;
            }
        }
        return result;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2014, 1, 24, 12, 0, 0);
        return gc.getTime();
    }
}
