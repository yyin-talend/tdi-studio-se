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

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.PerlItemOldTypesConverter;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ConvertOldPerlTypesMigrationTask extends AbstractItemMigrationTask {

    private static final ProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    private boolean changed = false;

    @Override
    public ExecutionResult execute(Item item) {
        if (getProject().getLanguage() == ECodeLanguage.JAVA) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            convertItem(item);
            if (changed) {
                return ExecutionResult.SUCCESS_WITH_ALERT;
            } else {
                return ExecutionResult.SUCCESS_NO_ALERT;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        toReturn.add(ERepositoryObjectType.CONTEXT);
        toReturn.add(ERepositoryObjectType.ROUTINES);
        toReturn.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_DELIMITED);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_REGEXP);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_XML);
        toReturn.add(ERepositoryObjectType.METADATA_FILE_LDIF);
        toReturn.add(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
        return toReturn;
    }

    private void convertItem(Item item) throws PersistenceException {
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
        switch (itemType) {
        case PROCESS:
            convertJobs((ProcessItem) item);
            break;
        case CONTEXT:
            convertContext((ContextItem) item);
            break;
        case ROUTINES:
            convertRoutines((RoutineItem) item);
            break;
        case METADATA_CONNECTIONS:
        case METADATA_FILE_DELIMITED:
        case METADATA_FILE_POSITIONAL:
        case METADATA_FILE_REGEXP:
        case METADATA_FILE_XML:
        case METADATA_FILE_LDIF:
        case METADATA_GENERIC_SCHEMA:
            changeMetadataConnections((ConnectionItem) item);
        default:
            break;
        }
    }

    /**
     * 
     * DOC ggu Comment method "convertJobs".<br>
     * 
     * Convert the Process(Job)
     * 
     * @return
     */
    private void convertJobs(ProcessItem processItem) throws PersistenceException {
        List<IRepositoryObject> processList = FACTORY.getAll(ERepositoryObjectType.PROCESS, true);
        PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(processItem);

        if (converter.isModified()) {
            FACTORY.save(processItem);
            changed = true;
        }
    }

    /**
     * 
     * DOC ggu Comment method "convertContext".<br>
     * 
     * convert old Perl types in the Context.
     * 
     * @param contextItem
     * @return
     * @throws PersistenceException
     */
    private void convertContext(ContextItem contextItem) throws PersistenceException {
        PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(contextItem);

        if (converter.isModified()) {
            FACTORY.save(contextItem);
            changed = true;
        }
    }

    /**
     * 
     * DOC ggu Comment method "convertRoutines".<br>
     * 
     * convert the Routines
     * 
     * @return
     */
    private void convertRoutines(RoutineItem routineItem) throws PersistenceException {
        PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(routineItem);

        if (converter.isModified()) {
            FACTORY.save(routineItem);
            changed = true;
        }
    }

    private void changeMetadataConnections(ConnectionItem connectionItem) throws PersistenceException {
        PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(connectionItem);

        if (converter.isModified()) {
            FACTORY.save(connectionItem);
            changed = true;
        }
    }
}
