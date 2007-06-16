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
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * this Task for: add two attributes (product and mapping) in DatabaseConnection's model of Emf . qzhang class global
 * comment. Detailled comment <br/>
 * 
 */
public class AddProductAndMappingInDBConnectionEMFMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public boolean execute(Project project) {
        try {
            addProductAndMapping();
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    /**
     * qzhang Comment method "addProductAndMapping".
     */
    private boolean addProductAndMapping() throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.METADATA_CONNECTIONS, true);
        boolean modified = false;
        for (IRepositoryObject mainObject : list) {
            List<IRepositoryObject> allVersion = factory.getAllVersion(mainObject.getId());
            for (IRepositoryObject object : allVersion) {
                final ConnectionItem item = (ConnectionItem) object.getProperty().getItem();
                final DatabaseConnection connection = (DatabaseConnection) item.getConnection();
                final String product = EDatabaseTypeName.getTypeFromDisplayName(connection.getDatabaseType()).getProduct();
                connection.setProductId(product);
                connection.setDbmsId(MetadataTalendType.getDefaultDbmsFromProduct(product).getId());
                factory.save(item);
                modified = true;
            }
        }
        return modified;
    }

}
