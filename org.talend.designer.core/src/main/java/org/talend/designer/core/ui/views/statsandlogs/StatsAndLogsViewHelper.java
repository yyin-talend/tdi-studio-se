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
package org.talend.designer.core.ui.views.statsandlogs;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;

/**
 * ftang class global comment. Detailed comment. <br/>
 * 
 */
public class StatsAndLogsViewHelper {

    private static final String LANGUAGE_PREFIX = LanguageManager.getCurrentLanguage().toString() + "_";

    private static final IPreferenceStore PREFERENCE_STORE = DesignerPlugin.getDefault().getPreferenceStore();

    public static final String FILE_NAME_REGEX = "[^\\>\\<\\\\\\/\\!\\:\\|\\?\\\"\\'\\s\\.]"
            + "+[\\.][^\\>\\<\\\\\\/\\!\\:\\|\\?\\\"\\'\\s\\.]+";

    public static final String OTHER_FILE_NAME_REGEX = "[^\\\"\\'\\s]*";

    /**
     * ftang Comment method "reloadValuesFromPreferencePage".
     * 
     * @param preferenceStore
     * @param element
     */
    public static void reloadValuesFromPreferencePage(Element element) {

        List<? extends IElementParameter> elementParameters = element.getElementParameters();
        for (IElementParameter elementParameter : elementParameters) {
            String name = elementParameter.getName();

            if (name.equals(EParameterName.ON_STATCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_STATCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_LOGCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_LOGCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_METERCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_METERCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_FILES_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX + EParameterName.ON_FILES_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.FILE_PATH.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILE_PATH.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_STATS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_STATS.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_LOGS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_LOGS.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_METTER.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_METTER.getName())));
                continue;
            }

            if (name.equals(EParameterName.ON_DATABASE_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE
                        .getBoolean(LANGUAGE_PREFIX + EParameterName.ON_DATABASE_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.DB_TYPE.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.DB_TYPE.getName())));
                continue;
            }

            if (name.equals(EParameterName.HOST.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.HOST.getName())));
                continue;
            }

            if (name.equals(EParameterName.PORT.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.PORT.getName())));
                continue;
            }

            if (name.equals(EParameterName.DBNAME.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.DBNAME.getName())));
                continue;
            }
            if (name.equals(EParameterName.PROPERTIES.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.PROPERTIES.getName())));
                continue;
            }

            if (name.equals(EParameterName.SCHEMA_DB.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.SCHEMA_DB.getName())));
                continue;
            }

            if (name.equals(EParameterName.USER.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.USER.getName())));
                continue;
            }

            if (name.equals(EParameterName.PASS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.PASS.getName())));
                continue;
            }

            if (name.equals(EParameterName.TABLE_STATS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.TABLE_STATS.getName())));
                continue;
            }

            if (name.equals(EParameterName.TABLE_LOGS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.TABLE_LOGS.getName())));
                continue;
            }

            if (name.equals(EParameterName.TABLE_METER.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.TABLE_METER.getName())));
                continue;
            }

            if (name.equals(EParameterName.CATCH_RUNTIME_ERRORS.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.CATCH_RUNTIME_ERRORS.getName()));
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_ERRORS.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.CATCH_USER_ERRORS.getName()));
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_WARNING.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.CATCH_USER_WARNING.getName()));
                continue;
            }

            if (name.equals(EParameterName.CATCH_REALTIME_STATS.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.CATCH_REALTIME_STATS.getName()));
                continue;
            }

        }
    }

    /**
     * ftang "saveValuesToPreferencePage".
     * 
     * @param preferenceStore
     * @param element
     */
    public static void saveValuesToPreferencePage(Element element) {

        List<? extends IElementParameter> elementParameters = element.getElementParameters();
        for (IElementParameter elementParameter : elementParameters) {
            String name = elementParameter.getName();
            Object elementValue = elementParameter.getValue();

            if (name.equals(EParameterName.ON_STATCATCHER_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_STATCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.ON_LOGCATCHER_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_LOGCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;

            }

            if (name.equals(EParameterName.ON_METERCATCHER_FLAG.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.ON_METERCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.ON_FILES_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_FILES_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.FILE_PATH.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILE_PATH.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_STATS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_STATS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_LOGS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_LOGS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_METTER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_METTER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.ON_DATABASE_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_DATABASE_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.DB_TYPE.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.DB_TYPE.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.HOST.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.HOST.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.PORT.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PORT.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.DBNAME.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.DBNAME.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }
            if (name.equals(EParameterName.PROPERTIES.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PROPERTIES.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }
            if (name.equals(EParameterName.SCHEMA_DB.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.SCHEMA_DB.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.USER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.USER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.PASS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PASS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_STATS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_STATS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_LOGS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_LOGS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_METER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_METER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.CATCH_RUNTIME_ERRORS.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.CATCH_RUNTIME_ERRORS.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_ERRORS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.CATCH_USER_ERRORS.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_WARNING.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.CATCH_USER_WARNING.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_REALTIME_STATS.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.CATCH_REALTIME_STATS.getName(), (Boolean) elementValue);
                continue;
            }
        }
    }

    /*
     * add quote.
     */
    private static String checkAndAddQuote(String value) {
        if (value == null) {
            return TalendTextUtils.addQuotes("");
        }
        value = value.trim();
        String mark = TalendTextUtils.QUOTATION_MARK;
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            mark = TalendTextUtils.SINGLE_QUOTE;
        }
        if (!value.startsWith(mark) && !value.endsWith(mark)) {
            value = TalendTextUtils.addQuotes(value, mark);
        }
        return value;
    }

    /*
     * remove quote.
     */
    private static String checkAndRemoveQuote(String value) {
        if (value == null) {
            return "";
        }
        value = value.trim();
        String mark = TalendTextUtils.QUOTATION_MARK;
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            mark = TalendTextUtils.SINGLE_QUOTE;
        }
        if (value.startsWith(mark) && value.endsWith(mark)) {
            value = TalendTextUtils.removeQuotes(value, mark);
        }
        return value;
    }
}
