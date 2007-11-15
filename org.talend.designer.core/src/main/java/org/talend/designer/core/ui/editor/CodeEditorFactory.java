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
