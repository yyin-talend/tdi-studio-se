package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.migration.IMigrationTask.ExecutionResult;

public class RemoveErrorTagMigrationTask extends AbstractItemMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2013, 10, 17, 14, 0, 0);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        ConnectionItem connectionItem = (ConnectionItem) item;
        DatabaseConnection connection = (DatabaseConnection) connectionItem.getConnection();
        if (connection != null) {
            Iterator it = connectionItem.getProperty().getInformations().iterator();
            while (it.hasNext()) {
                org.talend.core.model.properties.Information Information = (org.talend.core.model.properties.Information) it
                        .next();
                if (Information.getLevel().equals(InformationLevel.ERROR_LITERAL)
                        || Information.getLevel().equals(InformationLevel.WARN_LITERAL)) {
                    it.remove();
                }
            }
            return ExecutionResult.SUCCESS_NO_ALERT;
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        return toReturn;
    }
}
