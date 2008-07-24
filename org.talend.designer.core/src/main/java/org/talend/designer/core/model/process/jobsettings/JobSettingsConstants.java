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
package org.talend.designer.core.model.process.jobsettings;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class JobSettingsConstants {

    /**
     * 
     */
    public enum ContextLoadInfo {
        ERROR("Error"), //$NON-NLS-1$
        WARNING("Warning"), //$NON-NLS-1$
        INFO("Info"); //$NON-NLS-1$

        private String display;

        private ContextLoadInfo(String display) {
            this.display = display;
        }

        public String getDisplayName() {
            return this.display;
        }

        public String getName() {
            return this.toString();
        }

    }

    private static final String[] PERL_DB_INPUT_COMPONENTS = new String[] { "tDBInput", "tMysqlInput", "tDBInput",
            "tOracleInput_sid", "tOracleInput_service", "tPostgresqlInput", "tDB2Input", "tSybaseInput", "tIngresInput",
            "tInterbaseInput", "tSQLiteInput", "tFirebirdInput", "tInformixInput", "tAccessInput", "tTeradataInput" };

    private static final String[] JAVA_DB_INPUT_COMPONENTS = new String[] { "tDBInput", "tMysqlInput", "tMSSqlInput",
            "tOracleInput_sid", "tOracleInput_service", "tPostgresqlInput", "tDB2Input", "tSybaseInput", "tIngresInput",
            "tInterbaseInput", "tSQLiteInput", "tFirebirdInput", "tInformixInput", "tAccessInput", "tTeradataInput" };

    public static final String[][] DB_INPUT_COMPONENTS = new String[][] { PERL_DB_INPUT_COMPONENTS, JAVA_DB_INPUT_COMPONENTS };

    private static final String EXTRA = "_IMPLICIT_CONTEXT"; //$NON-NLS-1$

    public static final String QUERY = "QUERY"; //$NON-NLS-1$

    public static String getExtraParameterName(final String name) {
        if (name == null) {
            return "";
        }
        return name + EXTRA;
        // return name;
    }

    public static boolean isExtraParameter(final String name) {
        if (name == null) {
            return false;
        }
        return name.endsWith(EXTRA);
    }

    public static String addBrackets(final String condition) {
        if (condition == null || "".equals(condition.trim())) { //$NON-NLS-1$
            return ""; //$NON-NLS-1$
        }
        return "(" + condition + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
