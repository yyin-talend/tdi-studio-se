// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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

import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.Item;

/**
 * created by talend2 on 2013-8-14 Detailled comment
 * 
 */
public class FixItemNameAndVersionMigrationTask extends AbstractItemMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2000, 6, 30, 10, 20, 0);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        // check filename expected. (LABEL)

        // get expected name: last version name.
        // for example: TESTCASE

        // check current filename if good or not :
        // ResourceFilenameHelper.FileName fileNameTest = ResourceFilenameHelper.create(resourceProperty.eResource(),
        // resourceProperty, lastVersionProperty);
        // boolean mustChangeLabel = ResourceFilenameHelper.mustChangeLabel(fileNameTest);

        // if not good, rename the file.
        // if (!ResourceFilenameHelper.hasSameNameButDifferentCase(fileName)) {
        // moveResource(resource, ResourceFilenameHelper.getExpectedFilePath(fileName, false));
        // }
        // else {
        // moveResource(resource, ResourceFilenameHelper.getExpectedFilePath(fileName, false) +.bak);
        // moveResource(resource +.bak, ResourceFilenameHelper.getExpectedFilePath(fileName, false));
        // }

        // check filename expected. (VERSION)
        // ResourceFilenameHelper.FileName fileNameTest = ResourceFilenameHelper.create(item.getProperty().eResource(),
        // item.getProperty(), null);
        // if (ResourceFilenameHelper.mustChangeVersion(fileNameTest)) {
        // XmiResourceManager xmiResourceManager =
        // ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
        // .getResourceManager();
        // try {
        // xmiResourceManager.propagateFileName(item.getProperty(), item.getProperty());
        //
        // } catch (PersistenceException e) {
        // ExceptionHandler.process(e);
        // return ExecutionResult.FAILURE;
        // }
        // }
        return ExecutionResult.NOTHING_TO_DO;
    }

}
