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
package org.talend.designer.mapper.language;

import org.talend.commons.utils.data.text.StringHelper;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractLanguage implements ILanguage {

    public static final String CARRIAGE_RETURN = "\n"; //$NON-NLS-1$

    private ECodeLanguage codeLanguage;

    protected ICodeProblemsChecker codeChecker;

    /**
     * DOC amaumont AbstractLanguage constructor comment.
     * 
     * @param perl
     */
    public AbstractLanguage(ECodeLanguage codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getLocation(org.talend.designer.mapper.model.tableentry.TableEntryLocation)
     */
    public String getLocation(String tableName, String columnName) {
        return StringHelper.replacePrms(getTemplateTableColumnVariable(), new Object[] { tableName, columnName });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getLocation(java.lang.String)
     */
    public String getLocation(String tableName) {
        return StringHelper.replacePrms(getTemplateTableVariable(), new Object[] { tableName });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getCodeLanguage()
     */
    public ECodeLanguage getCodeLanguage() {
        return codeLanguage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getCodeChecker()
     */
    public ICodeProblemsChecker getCodeChecker() {
        return codeChecker;
    }

}
