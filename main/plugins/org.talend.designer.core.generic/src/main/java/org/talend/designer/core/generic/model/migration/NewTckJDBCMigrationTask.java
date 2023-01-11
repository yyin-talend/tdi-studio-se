// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;

public class NewTckJDBCMigrationTask extends ConvertTCompV0ToTckComponentMigrationTask {

    @Override
    public Date getOrder() {
        return new GregorianCalendar(2023, 0, 11, 18, 0, 0).getTime();
    }

    @Override
    protected String getMigrationFile() {
        return "NewTckJDBCMigrationTask.properties";
    }
    
}
