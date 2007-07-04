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

import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Add quote marks to separator after feature 1192 added to trunk.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: AddQutoMigrationTask.java 下午01:23:38 2007-7-4 +0000 (2007-7-4) yzhang $
 * 
 */
public class AddQuoteMarkMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult execute(Project project) {

        try {
            addQuote();
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * Add quote marks to the separators.
     * 
     * yzhang Comment method "addQuote".
     * 
     * @throws PersistenceException
     */
    private void addQuote() throws PersistenceException {

        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.METADATA, true);

        for (IRepositoryObject mainObject : list) {
            List<IRepositoryObject> allVersion = factory.getAllVersion(mainObject.getId());
            for (IRepositoryObject object : allVersion) {
                boolean modified = false;

                ConnectionItem connItem = (ConnectionItem) object.getProperty().getItem();
                Connection conn = connItem.getConnection();
                if (conn instanceof FileConnection) {
                    FileConnection fileConnection = (FileConnection) conn;
                    String old = null;

                    old = fileConnection.getFieldSeparatorValue();
                    if (!isWithinQuote(old)) {
                        fileConnection.setFieldSeparatorValue(surroundDQuote(old));
                        modified = true;
                    }

                    old = fileConnection.getRowSeparatorValue();
                    if (!isWithinQuote(old)) {
                        fileConnection.setRowSeparatorValue(surroundDQuote(old));
                        modified = true;
                    }

                    old = fileConnection.getEscapeChar();
                    if (!isWithinQuote(old)) {
                        fileConnection.setEscapeChar(TalendTextUtils.addQuotes(old));
                        modified = true;
                    }

                    old = fileConnection.getTextEnclosure();
                    if (!isWithinQuote(old)) {
                        fileConnection.setTextEnclosure(TalendTextUtils.addQuotes(old));
                        modified = true;
                    }

                }

                if (modified) {
                    factory.save(connItem);
                }
            }
        }
    }

    /**
     * To see whehter the string is surrounded with double quote mark.
     * 
     * yzhang Comment method "isWithinQuot".
     * 
     * @param string
     * @return
     */
    private boolean isWithinQuote(String string) {
        if (string == null) {
            return true;
        }
        boolean isWithin = false;
        if (string.startsWith("\"") && string.endsWith("\"")) {
            isWithin = true;
        } else if (string.startsWith("\'") && string.endsWith("\'")) {
            isWithin = true;
        }
        return isWithin;
    }

    /**
     * Surround the old string with double quote.
     * 
     * yzhang Comment method "surroundQuote".
     * 
     * @param old
     * @return
     */
    private String surroundDQuote(String old) {
        return "\"" + old + "\"";
    }
}
