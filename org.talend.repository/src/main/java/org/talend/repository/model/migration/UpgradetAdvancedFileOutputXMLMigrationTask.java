// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.model.migration;

import java.util.Arrays;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.UpdateMappingFortAdvancedXMLConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * DOC s class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2007-10-25 17:06:40 +0000 xzhang $
 */
public class UpgradetAdvancedFileOutputXMLMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        if (getProject().getLanguage() != ECodeLanguage.JAVA) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            IComponentFilter filter1 = new NameComponentFilter("tAdvancedFileOutputXML"); //$NON-NLS-1$

            IComponentConversion removeQuotes1 = new UpdateMappingFortAdvancedXMLConversion(); //$NON-NLS-1$
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(removeQuotes1));

            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}
