// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ChangeUpdate2BulkUpdate4tRedshiftOutput extends AbstractJobMigrationTask {

  @Override
  public ExecutionResult execute(Item item) {
    final ProcessType processType = getProcessType(item);
    String[] compNames = { "tRedshiftOutput" }; //$NON-NLS-1$

    IComponentConversion doMigration = new IComponentConversion() {

      public void transform(NodeType node) {
        if (node == null) {
          return;
        }

        ElementParameterType data_action = ComponentUtilities.getNodeProperty(node, "DATA_ACTION"); //$NON-NLS-1$

        if (data_action == null) {
          return;
        }

        String action = data_action.getValue();

        if (action == null || !"UPDATE".equals(action)) {//$NON-NLS-1$
          return;
        }

        if (isDynamicSchema(node)) {
          return;
        }

        if (isRejectLineExists(node)) {
          return;
        }

        data_action.setValue("BULK_UPDATE");//$NON-NLS-1$

        ElementParameterType nb_rows_per_insert = ComponentUtilities.getNodeProperty(node, "NB_ROWS_PER_INSERT"); //$NON-NLS-1$
        if (nb_rows_per_insert == null) {
          return;
        }

        nb_rows_per_insert.setValue("1000");
      }

      private boolean isRejectLineExists(NodeType node) {
        List<ConnectionType> list = ComponentUtilities.getNodeOutputConnections(node);
        for (ConnectionType connType : list) {
          EConnectionType eConnType = EConnectionType.getTypeFromId(connType.getLineStyle());
          if (eConnType == EConnectionType.FLOW_MAIN && connType.getConnectorName().equals("REJECT")) {
            return true;
          }
        }
        return false;
      }

      private boolean isDynamicSchema(NodeType node) {
        EList<EObject> list = node.eContents();
        for (EObject object : list) {
          if (object instanceof MetadataType) {
            MetadataType flow = (MetadataType) object;
            if ("FLOW".equalsIgnoreCase(flow.getConnector())) {
              Iterator<?> columns = flow.getColumn().iterator();
              while (columns.hasNext()) {
                Object outColumn = columns.next();
                if (outColumn instanceof ColumnType) {
                  ColumnType column = ((ColumnType) outColumn);
                  if ("id_Dynamic".equals(column.getType())) {
                    return true;
                  }
                }
              }
            }
          }
        }

        return false;
      }

    };

    for (String name : compNames) {
      IComponentFilter filter = new NameComponentFilter(name);

      try {
        ModifyComponentsAction.searchAndModify(item, processType, filter, Arrays.<IComponentConversion> asList(doMigration));
      } catch (PersistenceException e) {
        ExceptionHandler.process(e);
        return ExecutionResult.FAILURE;
      }
    }

    return ExecutionResult.SUCCESS_NO_ALERT;

  }

  public Date getOrder() {
    GregorianCalendar gc = new GregorianCalendar(2018, 0, 26, 16, 0, 0);
    return gc.getTime();
  }
}
