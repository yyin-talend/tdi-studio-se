// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.ui.provider;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.designer.filemultischemas.data.EPropertyName;
import org.talend.designer.filemultischemas.managers.UIManager;

/**
 * cLi class global comment. Detailled comment
 */
public abstract class SchemaDetailsCellModifier implements ICellModifier {

    private TreeViewer schemaDetailsViewer;

    public SchemaDetailsCellModifier(TreeViewer schemaDetailsViewer) {
        super();
        this.schemaDetailsViewer = schemaDetailsViewer;
    }

    protected TreeViewer getSchemaDetailsViewer() {
        return this.schemaDetailsViewer;
    }

    protected String validateValue(String value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

    protected int getTalendTypeIndex(String talendType) {
        if (talendType == null || "".equals(talendType)) {
            return 0;
        } else {
            List<String> types = null;
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                talendType = JavaTypesManager.getJavaTypeFromId(talendType).getLabel();
                types = Arrays.asList(JavaTypesManager.getJavaTypesLabels());
            } else { // perl
                types = Arrays.asList(PerlTypesManager.getPerlTypes());
            }
            return types.indexOf(talendType);
        }
    }

    protected String getAndCheckIntgerValue(Integer value) {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }

    protected String getTalendTypeByIndex(Integer index) {
        if (index > -1) {
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                final JavaType javaType = JavaTypesManager.getJavaTypes()[index];
                return javaType.getId();
            } else {
                final String perlType = PerlTypesManager.getPerlTypes()[index];
                return perlType; // perl
            }
        }
        return null;
    }

    protected boolean validateColumnName(EPropertyName pName, Object value) {
        return UIManager.validSchemaDetailsColumns(this.getSchemaDetailsViewer(), EPropertyName.NAME, value) == null;
    }
}
