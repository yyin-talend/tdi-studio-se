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
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Task replace run before and after with then run connection.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ChangeRunBeforeAfterToThenRunMigrationTask.java 下午04:41:56 2007-5-17 +0000 (2007-5-17) yzhang $
 * 
 */
public class RemoveSpaceInJobNameMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult executeOnProcess(ProcessItem item) {

        try {
            renameJobs(item);
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }

    }

    /**
     * Rename jobs if needed, remove spaces and replace them by "_". Note that this migration is only for 1.0 or 1.1 to
     * more recent version, after the 1.1 it was not possible to add some spaces.
     * 
     * @throws PersistenceException
     */
    private void renameJobs(ProcessItem item) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        if (item.getProperty().getLabel().contains(" ")) { // if the job contain some spaces
            item.getProperty().setLabel(item.getProperty().getLabel().replaceAll(" ", "_"));
            factory.save(item);
        }
    }
}
