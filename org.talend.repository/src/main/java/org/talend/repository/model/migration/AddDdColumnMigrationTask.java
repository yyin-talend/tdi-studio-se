// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.model.migration;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * <p>
 * This Task check the originalDbColumnName="" in the *.item file, make sure the columnName and the originalDbColumnName
 * are the same.(just remove it, it is OK.)
 * </p>
 * <p>
 * Reason:
 * <li>1. Since TOS 2.2 M1, the DB component schema add the "DbColumn" column, but in the template jet file, it use the
 * columnName to create the SQL statement.</li>
 * <li>2. Since TOS 2.2 M2, the DB component template file use the DbColumn to create the SQL statement.</li>
 * <li>3. If in TOS 2.2 M1, the DbColumn and the column name are not same, the project import to TOS 2.2 M2, it will
 * create the wrong SQL statement.</li>
 */
public class AddDdColumnMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            removeDbColumn(item);
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    private void removeDbColumn(ProcessItem item) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        boolean modified = false;

        for (Object o : item.getProcess().getNode()) {

            NodeType node = (NodeType) o;

            for (Object o2 : node.getMetadata()) {

                MetadataType metadata = (MetadataType) o2;

                for (Object o3 : metadata.getColumn()) {
                    ColumnType column = (ColumnType) o3;

                    if (column.getOriginalDbColumnName() != null) {
                        column.setOriginalDbColumnName(null);
                        modified = true;
                    }

                }

            }
        }

        if (modified) {
            factory.save(item);
        }
    }
}
