// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.sqlbuilder;




/**
 * Put Constants here
 * <br/>
 *
 * $Id: IConstants.java,v 1.5 2006/11/07 09:48:06 peiqin.hou Exp $
 *
 */
public interface IConstants {

    String AUTO_COMMIT = "SQLEditor.AutoCommit";

    String AUTO_OPEN_EDITOR = "SQLEditor.AutoOpenEditor";

    String CLIP_EXPORT_COLUMNS = "SQLEditor.ClipExportColumns";

    String CLIP_EXPORT_SEPARATOR = "SQLEditor.ClipExportSeparator";

    String COMMIT_ON_CLOSE = "SQLEditor.CommitOnClose";

    String DATASETRESULT_DATE_FORMAT = "DataSetResult.DateFormat";

    String DATASETRESULT_FORMAT_DATES = "DataSetResult.FormatDates";

    String DEFAULT_DRIVER = "Drivers.DefaultDriverName";

    String FONT = "SQLEditor.Font";

    String HISTORY_AUTOSAVE_AFTER = "SQLHistory.AutoSaveAfterXXStatements";

    String INTERACTIVE_QUERY_TIMEOUT = "InteractiveConnection.QueryTimeOutSeconds";

    //String MAX_SQL_ROWS = "SQLEditor.MaxSQLRows";
    
    String MAX_SQL_ROWS = "100";

    String PRE_ROW_COUNT = "SQLEditor.PreRowCount";

    String SQL_ALT_QRY_DELIMITER = "SQLEditor.AltQueryDelimiter";

    String SQL_ASSIST = "SQLEditor.Assist";

    /** The color key for database tables column names */
    String SQL_COLUMS = "SQLEditor.ColumnsColor";
    String SQL_COMMENT_DELIMITER = "SQLEditor.CommentDelimiter";

    /**
     * The color key for everthing in SQL code for which no other color is
     * specified.
     */
    String SQL_DEFAULT = "SQLEditor.DefaultColor";

    /** The color key for SQL keywords in Java code. */
    String SQL_KEYWORD = "SQLEditor.KeywordColor";

    /** The color key for multi-line comments in Java code. */
    String SQL_MULTILINE_COMMENT = "SQLEditor.MultiLineCommentColor";

    String SQL_QRY_DELIMITER = "SQLEditor.QueryDelimiter";

    /** The color key for single-line comments in Java code. */
    String SQL_SINGLE_LINE_COMMENT = "SQLEditor.SingleLineCommentColor";

    /** The color key for string and character literals in Java code. */
    String SQL_STRING = "SQLEditor.StringColor";

    /** The color key for database tables names */
    String SQL_TABLE = "SQLEditor.TableColor";

    String WARN_IF_LARGE_LIMIT = "SQLEditor.WarnIfLargeLimit";

    String WARN_LIMIT = "SQLEditor.WarnLimit";
    
    String WORD_WRAP = "SQLEditor.AutoWrap";
    
    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$
}
