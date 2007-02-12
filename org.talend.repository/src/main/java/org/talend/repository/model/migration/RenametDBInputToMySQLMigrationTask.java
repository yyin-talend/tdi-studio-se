// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.repository.model.migration.conversions.IComponentConversion;
import org.talend.repository.model.migration.conversions.RemovePropertyComponentConversion;
import org.talend.repository.model.migration.filters.IComponentFilter;
import org.talend.repository.model.migration.filters.PropertyComponentFilter;

/**
 * Use to rename tDB(Input|Output|SQLRow) into tMysql(Input|Output|Row). Related bug 540.
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenametDBInputToMySQLMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    public boolean execute(Project project) {
        try {
            IComponentConversion conversion = new RemovePropertyComponentConversion("TYPE");

            IComponentFilter filter1 = new PropertyComponentFilter("tDBInput", "TYPE", "mysql;mysql");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            RenameComponentAction.rename("tDBInput", "tMysqlInput", filter1, conversion); //$NON-NLS-1$ //$NON-NLS-2$

            IComponentFilter filter2 = new PropertyComponentFilter("tDBOutput", "TYPE", "mysql;mysql;MYSQL");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            RenameComponentAction.rename("tDBOutput", "tMysqlOutput", filter2, conversion); //$NON-NLS-1$ //$NON-NLS-2$

            IComponentFilter filter3 = new PropertyComponentFilter("tDBSQLRow", "TYPE", "mysql;mysql");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            RenameComponentAction.rename("tDBSQLRow", "tMysqlRow", filter3, conversion); //$NON-NLS-1$ //$NON-NLS-2$

            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        }
    }
}
