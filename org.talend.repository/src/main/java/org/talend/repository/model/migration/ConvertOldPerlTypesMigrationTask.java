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
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
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
public class ConvertOldPerlTypesMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    private static final ProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    private boolean changed = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult execute(Project project) {
        try {

            switch (LanguageManager.getCurrentLanguage()) {
            case JAVA:
                changed = false;
                break;
            case PERL:
            default:

                convertJobs();
                convertContext();
                convertRoutines();
                convertMetadata();
            }

            if (changed) {
                return ExecutionResult.SUCCESS_WITH_ALERT;
            } else {
                return ExecutionResult.SUCCESS_NO_ALERT;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
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
    private void convertJobs() throws PersistenceException {

        List<IRepositoryObject> processList = FACTORY.getAll(ERepositoryObjectType.PROCESS, true);
        for (IRepositoryObject processObject : processList) {
            List<IRepositoryObject> allVersion = FACTORY.getAllVersion(processObject.getId());
            for (IRepositoryObject object : allVersion) {
                ProcessItem processItem = (ProcessItem) object.getProperty().getItem();

                PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(processItem);

                if (converter.isModified()) {
                    FACTORY.save(processItem);
                    changed = true;
                }
            }
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
    private void convertContext() throws PersistenceException {

        List<IRepositoryObject> contextList = FACTORY.getAll(ERepositoryObjectType.CONTEXT, true);
        for (IRepositoryObject contextObject : contextList) {
            List<IRepositoryObject> allVersion = FACTORY.getAllVersion(contextObject.getId());
            for (IRepositoryObject object : allVersion) {
                ContextItem contextItem = (ContextItem) object.getProperty().getItem();

                PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(contextItem);

                if (converter.isModified()) {
                    FACTORY.save(contextItem);
                    changed = true;
                }

            }
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
    private void convertRoutines() throws PersistenceException {

        List<IRepositoryObject> routinesList = FACTORY.getAll(ERepositoryObjectType.ROUTINES, true);
        for (IRepositoryObject routinesObject : routinesList) {
            List<IRepositoryObject> allVersion = FACTORY.getAllVersion(routinesObject.getId());
            for (IRepositoryObject object : allVersion) {
                RoutineItem routineItem = (RoutineItem) object.getProperty().getItem();

                PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(routineItem);

                if (converter.isModified()) {
                    FACTORY.save(routineItem);
                    changed = true;
                }

            }
        }

    }

    /**
     * 
     * DOC ggu Comment method "convertMetadata".<br>
     * 
     * convert the Schemas under the Metadata Repository
     * 
     * @return
     * @throws PersistenceException
     */
    private void convertMetadata() throws PersistenceException {

        // DB
        changeMetadataConnections(ERepositoryObjectType.METADATA_CONNECTIONS);
        // FileDelimited
        changeMetadataConnections(ERepositoryObjectType.METADATA_FILE_DELIMITED);
        // File Positional
        changeMetadataConnections(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
        // File RegExp
        changeMetadataConnections(ERepositoryObjectType.METADATA_FILE_REGEXP);
        // File XML
        changeMetadataConnections(ERepositoryObjectType.METADATA_FILE_XML);
        // File ldif
        changeMetadataConnections(ERepositoryObjectType.METADATA_FILE_LDIF);
        // GenericSchema
        changeMetadataConnections(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);

        // // BD view schema
        // try {
        // changeMetadataConnections(ERepositoryObjectType.METADATA_CON_VIEW);// error?
        // } catch (PersistenceException e) {
        // e.printStackTrace();
        // }
        // // DB synonym schema
        // try {
        // changeMetadataConnections(ERepositoryObjectType.METADATA_CON_SYNONYM);// error?
        // } catch (PersistenceException e) {
        // e.printStackTrace();
        // }
        // // meta table
        // try {
        // changeMetadataConnections(ERepositoryObjectType.METADATA_CON_TABLE);// error?
        // } catch (PersistenceException e) {
        // e.printStackTrace();
        // }

    }

    private void changeMetadataConnections(ERepositoryObjectType repositoryObjectType) throws PersistenceException {

        List<IRepositoryObject> connList = FACTORY.getAll(repositoryObjectType, true);
        for (IRepositoryObject connObject : connList) {
            List<IRepositoryObject> allVersion = FACTORY.getAllVersion(connObject.getId());
            for (IRepositoryObject object : allVersion) {
                ConnectionItem connectionItem = (ConnectionItem) object.getProperty().getItem();
                PerlItemOldTypesConverter converter = new PerlItemOldTypesConverter(connectionItem);

                if (converter.isModified()) {
                    FACTORY.save(connectionItem);
                    changed = true;
                }
            }
        }

    }
}
