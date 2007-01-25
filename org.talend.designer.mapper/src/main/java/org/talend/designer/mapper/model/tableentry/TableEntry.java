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
package org.talend.designer.mapper.model.tableentry;

import java.util.Iterator;
import java.util.List;

import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.JavaGenerationManager;
import org.talend.designer.mapper.language.generation.JavaGenerationManager.PROBLEM_KEY_FIELD;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class TableEntry implements ITableEntry {

    private static final String EMPTY_STRING = "";

    private String expression;

    private AbstractDataMapTable parent;

    private String name;

    private List<Problem> problems;

    public TableEntry(AbstractDataMapTable abstractDataMapTable, String expression) {
        super();
        this.parent = abstractDataMapTable;
        this.expression = expression;
    }

    public TableEntry(AbstractDataMapTable abstractDataMapTable) {
        this(abstractDataMapTable, null);
    }

    /**
     * DOC amaumont ITableEntry constructor comment.
     * 
     * @param abstractDataMapTable
     * @param name, can't be null
     */
    public TableEntry(AbstractDataMapTable abstractDataMapTable, String name, String expression) {
        super();
        this.parent = abstractDataMapTable;
        this.name = name;
        if (this.name == null) {
            throw new IllegalArgumentException("Name of the TableEntry must not be null !");
        }
        this.expression = expression;
        // TimeMeasure.start("checkErrors");
        checkErrors();
        // TimeMeasure.end("checkErrors");
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        // TimeMeasure.start("checkErrors2");
        checkErrors();
        // TimeMeasure.end("checkErrors2");
    }

    public AbstractDataMapTable getParent() {
        return this.parent;
    }

    public void setParent(AbstractDataMapTable parent) {
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        if (parent != null) {
            return parent.getName();
        } else {
            throw new IllegalStateException("parent null");
        }
    }

    public List<Problem> getProblems() {
        return this.problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    /**
     * DOC amaumont Comment method "checkErrors".
     * 
     * @param columnEntries
     */
    private void checkErrors() {

        if (expression == null || EMPTY_STRING.equals(expression.trim())) {
            this.problems = null;
        } else {
            ILanguage currentLanguage = LanguageProvider.getCurrentLanguage();
            ECodeLanguage codeLanguage = currentLanguage.getCodeLanguage();
            ICodeProblemsChecker codeChecker = currentLanguage.getCodeChecker();
            // System.out.println("check=" + expression);
            List<Problem> problems = null;
            if (codeLanguage == ECodeLanguage.PERL) {
                problems = codeChecker.checkProblems(expression);
            } else if (codeLanguage == ECodeLanguage.JAVA) {
                PROBLEM_KEY_FIELD problemKeyField = JavaGenerationManager.PROBLEM_KEY_FIELD.METADATA_COLUMN;
                String entryName = getName();
                if (this instanceof FilterTableEntry) {
                    problemKeyField = JavaGenerationManager.PROBLEM_KEY_FIELD.FILTER;
                    entryName = null;
                }
                String key = parent.getMapperManager().buildProblemKey(problemKeyField, getParent().getName(), entryName);
                problems = codeChecker.checkProblemsFromKey(key);
            }
            if (problems != null) {
                for (Iterator iter = problems.iterator(); iter.hasNext();) {
                    Problem problem = (Problem) iter.next();
                    ProblemStatus status = problem.getStatus();
                    if (status != ProblemStatus.ERROR) {
                        iter.remove();
                    }

                }
                this.problems = problems;

            } else {
                this.problems = null;
            }
        }

    }

}
