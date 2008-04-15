// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.preferences.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractProjectMigrationTask;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;

/**
 * yzhang class global comment. Detailled comment
 */
public class AddQuotesForStatLogsPreference extends AbstractProjectMigrationTask {

    private String languagePrefix = LanguageManager.getCurrentLanguage() + "_";

    private String quoteType = LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA ? TalendTextUtils.QUOTATION_MARK
            : TalendTextUtils.SINGLE_QUOTE;

    private String[] ids = new String[] { EParameterName.FILE_PATH.getName(), EParameterName.FILENAME_STATS.getName(),
            EParameterName.FILENAME_LOGS.getName(), EParameterName.FILENAME_METTER.getName(), EParameterName.HOST.getName(),
            EParameterName.PORT.getName(), EParameterName.DBNAME.getName(), EParameterName.PROPERTIES.getName(),
            EParameterName.SCHEMA_DB.getName(), EParameterName.USER.getName(), EParameterName.PASS.getName(),
            EParameterName.DBFILE.getName(), EParameterName.TABLE_STATS.getName(), EParameterName.TABLE_LOGS.getName(),
            EParameterName.TABLE_METER.getName() };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult execute(Project project) {
        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();
        for (String id : ids) {
            String value = store.getString(languagePrefix + id);
            if (value != null && (!value.endsWith(quoteType) || !value.startsWith(quoteType))) {
                store.setValue(id, TalendTextUtils.addQuotes(value));
            }
        }
        return ExecutionResult.SUCCESS_NO_ALERT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
     */
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 4, 15, 15, 0, 0);
        return gc.getTime();
    }
}
