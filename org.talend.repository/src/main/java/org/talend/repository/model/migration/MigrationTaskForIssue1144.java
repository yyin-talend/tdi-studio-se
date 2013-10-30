// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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

import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;

public class MigrationTaskForIssue1144 extends AbstractJobMigrationTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2013, 10, 24, 15, 53, 0);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(final Item item) {
        return ExecutionResult.NOTHING_TO_DO;
    }

}
