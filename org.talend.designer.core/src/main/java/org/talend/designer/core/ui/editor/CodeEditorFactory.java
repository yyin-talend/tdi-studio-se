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
package org.talend.designer.core.ui.editor;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.talend.core.language.ECodeLanguage;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public final class CodeEditorFactory {

    private static CodeEditorFactory instance = new CodeEditorFactory();

    private CodeEditorFactory() {
    }

    public static CodeEditorFactory getInstance() {
        return instance;
    }

    public AbstractDecoratedTextEditor getCodeEditor(ECodeLanguage language) {
        switch (language) {
        case PERL:
            return new TalendPerlEditor();
        case JAVA:
            return new TalendJavaEditor();
        default:
            return new TalendPerlEditor();
        }
    }
}
