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
package org.talend.sqlbuilder.ui.proposal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mode;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Modes;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.IConstants;
import org.talend.sqlbuilder.util.QueryTokenizer;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLEditorProposalUtil.java,v 1.19 2006/11/08 10:03:04 qiang.zhang Exp $
 * 
 */
public class SQLEditorProposalUtil {

    private RepositoryNode session;

    private List<IContentProposal> proposals;

    private List<String> allString;

    private String languageMode;

    private int position;

    private String[] contents;

    public SQLEditorProposalUtil(RepositoryNode session, String language) {
        super();
        contents = new String[2];
        this.session = session;
        this.languageMode = language;
        allString = new ArrayList<String>();
        if (allString != null && !allString.isEmpty()) {
            allString.clear();
        }
        getAllProposalString();
    }

    /**
     * DOC dev Comment method "getSQLEditorContentProposals".
     * 
     * @param content editor has input all String
     * @param position edit current position
     * @return ContentProposal array.
     */
    public IContentProposal[] getSQLEditorContentProposals(String content, int position) {
        this.position = position;
        contents[0] = content.substring(0, position);
        contents[1] = content.substring(position);
        proposals = new ArrayList<IContentProposal>();
        List<String> queryStrings = getAllSqlQuery(content);

        try {
            String[] curSql = getCurrentSqlQuery(content);

            if (queryStrings.isEmpty() || (curSql[1].length() == 0 && curSql[0].length() == 0)) {
                for (String string : allString) {
                    createAllSQLEditorProposal("", string);
                }
            } else {
                while (!queryStrings.isEmpty()) {
                    hasSQLQueryProposal(queryStrings, curSql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IContentProposal[] res = new IContentProposal[proposals.size()];
        res = proposals.toArray(res);
        return res;

    }

    /**
     * DOC dev Comment method "getAllQqlQuery".
     * 
     * @param content editor has input all String
     * @return list of all SQL Query.
     */
    private List<String> getAllSqlQuery(String content) {
        QueryTokenizer qt = new QueryTokenizer(content, IConstants.QUERY_DELIMITER, IConstants.ALTERNATE_DELIMITER,
                IConstants.COMMENT_DELIMITER);
        List<String> queryStringsTmp = new ArrayList<String>();
        while (qt.hasQuery()) {
            String querySql = qt.nextQuery();
            // ignore commented lines.
            if (!querySql.startsWith("--")) {
                queryStringsTmp.add(querySql);
            }
        }
        List<String> queryStrings = new ArrayList<String>();
        while (!queryStringsTmp.isEmpty()) {
            String temp = (String) queryStringsTmp.remove(0);
            String querySql = temp.replaceAll("\n", "");
            queryStrings.add(querySql);
        }
        return queryStrings;
    }

    /**
     * Get Current SQL Query.
     * 
     * @param content editor content.
     * @return current SQL Query String
     */
    private String[] getCurrentSqlQuery(String content) {
        String[] curSql = new String[2];
        String before = content.substring(0, position);
        String after = content.substring(position);
        if (before != null && before.trim().length() != 0) {
            if (before.lastIndexOf(";") == -1) {
                curSql[0] = before.replaceAll("\n", "");
            } else {
                curSql[0] = before.substring(before.lastIndexOf(";") + 1, position).replaceAll("\n", "");
            }
        } else {
            curSql[0] = "";
        }
        if (after != null && after.trim().length() != 0) {
            if (after.lastIndexOf(";") == -1) {
                curSql[1] = after.replaceAll("\n", "");
            } else {
                curSql[1] = after.substring(0, after.indexOf(";")).replaceAll("\n", "");
            }
        } else {
            curSql[1] = "";
        }

        return curSql;
    }

    // /**
    // * DOC dev Comment method "createFullSQLEditorProposal".
    // * @param tmp editor has input String
    // * @param tablename need to proposal Table QualifiedName.
    // */
    // private void createFullSQLEditorProposal(String tmp, String tablename) {
    // proposals.add(new SQLEditorFullNameProposal(tmp, tablename));
    // }

    /**
     * DOC dev Comment method "createAllSQLEditorProposal".
     * 
     * @param tmp editor has input String
     * @param string need to proposal String
     */
    private void createAllSQLEditorProposal(String tmp, String string) {
        proposals.add(new SQLEditorAllProposal(tmp, string, position, contents));
    }

    /**
     * DOC dev Comment method "getAllKeywords".
     * 
     * @return all Key words of SQL.
     */
    private String[] getAllKeywords() {
        Mode mode = Modes.getMode(languageMode + ".xml");
        String[] keywords = mode.getDefaultRuleSet().getKeywords().get("KEYWORD1");
        return keywords;
    }

    /**
     * DOC dev Comment method "getAllProposalString".
     */
    private void getAllProposalString() {
        List<String> tablenames = SQLBuilderRepositoryNodeManager.getTableNamesByRepositoryNode(session);
        String[] allTablename = new String[tablenames.size()];
        allTablename = (String[]) tablenames.toArray(allTablename);
        Arrays.sort(allTablename);
        for (String tablename : allTablename) {
            allString.add(tablename);
        }
        String[] allKeywords = getAllKeywords();
        Arrays.sort(allKeywords);
        for (String keyword : allKeywords) {
            allString.add(keyword.toLowerCase());
        }
    }

    /**
     * DOC dev Comment method "hasSQLQueryProposal".
     * 
     * @param queryStrings all Sql Query String.
     * @param curSql current Sql Query String.
     */
    private void hasSQLQueryProposal(List<String> queryStrings, String[] curSql) {
        String querySql = (String) queryStrings.remove(0);
        if ((curSql[0] + curSql[1]).startsWith(querySql)) {
            String hasInput = "";
            int seqIndex = curSql[0].lastIndexOf(" ");
            if (seqIndex != -1) {
                hasInput = curSql[0].substring(seqIndex + 1);
            } else {
                hasInput = curSql[0];
            }
            if (allString != null && !allString.isEmpty()) {
                for (String string : allString) {
                    int index = string.indexOf(".");
                    String tmp2 = "";
                    if (index != -1) {
                        tmp2 = string.substring(index + 2, string.length() - 1);
                    } else {
                        tmp2 = string;
                    }
                    if (tmp2.toLowerCase().startsWith(hasInput.toLowerCase())) {
                        createAllSQLEditorProposal(hasInput, string);
                    }
                }
            }
        }
    }
}