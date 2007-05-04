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
package org.talend.designer.core.ui.preferences;

import org.talend.designer.core.model.components.EParameterName;

/**
 * This class is used for storing the same variable name of JAVA and Perl.
 * 
 * @author ftang <br/>
 * 
 */
public class StatsAndLogsConstants {

    public static final EParameterName[] ON_FILE_FLAG = new EParameterName[] { EParameterName.PERL_ON_FILES_FLAG,
            EParameterName.JAVA_ON_FILES_FLAG };

    public static final EParameterName[] FILE_PATH = new EParameterName[] { EParameterName.PERL_FILE_PATH,
            EParameterName.JAVA_FILE_PATH };

    public static final EParameterName[] FILENAME_STATS = new EParameterName[] { EParameterName.PERL_FILENAME_STATS,
            EParameterName.JAVA_FILENAME_STATS };

    public static final EParameterName[] FILENAME_LOGS = new EParameterName[] { EParameterName.PERL_FILENAME_LOGS,
            EParameterName.JAVA_FILENAME_LOGS };

    public static final EParameterName[] ON_DATABASE_FLAG = new EParameterName[] {
            EParameterName.PERL_ON_DATABASE_FLAG, EParameterName.JAVA_ON_DATABASE_FLAG };

    public static final EParameterName[] DB_TYPE = new EParameterName[] { EParameterName.PERL_DB_TYPE,
            EParameterName.JAVA_DB_TYPE };

    public static final EParameterName[] PROPERTY_TYPE = new EParameterName[] { EParameterName.PERL_PROPERTY_TYPE,
            EParameterName.JAVA_PROPERTY_TYPE, };

    public static final EParameterName[] REPOSITORY_PROPERTY_TYPE = new EParameterName[] {
            EParameterName.PERL_REPOSITORY_PROPERTY_TYPE, EParameterName.JAVA_REPOSITORY_PROPERTY_TYPE };

    public static final EParameterName[] HOST = new EParameterName[] { EParameterName.PERL_HOST,
            EParameterName.JAVA_HOST };

    public static final EParameterName[] PORT = new EParameterName[] { EParameterName.PERL_PORT,
            EParameterName.JAVA_PORT };

    public static final EParameterName[] DBNAME = new EParameterName[] { EParameterName.PERL_DBNAME,
            EParameterName.JAVA_DBNAME };

    public static final EParameterName[] SCHEMA_DB = new EParameterName[] { EParameterName.PERL_SCHEMA_DB,
            EParameterName.JAVA_SCHEMA_DB };

    public static final EParameterName[] USER = new EParameterName[] { EParameterName.PERL_USER,
            EParameterName.JAVA_USER };

    public static final EParameterName[] PASS = new EParameterName[] { EParameterName.PERL_PASS,
            EParameterName.JAVA_PASS };

    public static final EParameterName[] TABLE_STATS = new EParameterName[] { EParameterName.PERL_TABLE_STATS,
            EParameterName.JAVA_TABLE_STATS };

    public static final EParameterName[] TABLE_LOGS = new EParameterName[] { EParameterName.PERL_TABLE_LOGS,
            EParameterName.JAVA_TABLE_LOGS };

    public static final EParameterName[] CATCH_RUNTIME_ERRORS = new EParameterName[] {
            EParameterName.PERL_CATCH_RUNTIME_ERRORS, EParameterName.JAVA_CATCH_RUNTIME_ERRORS };

    public static final EParameterName[] CATCH_USER_ERRORS = new EParameterName[] {
            EParameterName.PERL_CATCH_USER_ERRORS, EParameterName.JAVA_CATCH_USER_ERRORS };

    public static final EParameterName[] CATCH_USER_WARNING = new EParameterName[] {
            EParameterName.PERL_CATCH_USER_WARNING, EParameterName.JAVA_CATCH_USER_WARNING };

    public static final EParameterName[] CATCH_REALTIME_STATS = new EParameterName[] {
            EParameterName.PERL_CATCH_REALTIME_STATS, EParameterName.JAVA_CATCH_REALTIME_STATS };

}
