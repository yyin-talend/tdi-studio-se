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
package org.talend.designer.mapper.language;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.language.java.JavaLanguage;
import org.talend.designer.mapper.language.perl.PerlLanguage;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LanguageProvider {

    public static ILanguage getCurrentLanguage() {
        ECodeLanguage codeLanguage = null;
        if (!MapperMain.isStandAloneMode()) {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY);
            codeLanguage = repositoryContext.getProject().getLanguage();
        } else {
            codeLanguage = ECodeLanguage.PERL;
        }
        switch (codeLanguage) {
        case PERL:
            return new PerlLanguage();
        case JAVA:
            return new JavaLanguage();
        default:
            return new PerlLanguage();
        }
    }

}
