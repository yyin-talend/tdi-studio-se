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
package org.talend.componentdesigner.model.enumtype;

import org.talend.componentdesigner.PluginConstant;

/**
 * All the type of language type. <br/>
 * 
 */
public enum LanguageType {
    PERLLANGUAGETYPE(PluginConstant.PERL_LANG, "_perl", ".preljet"),
    JAVALANGUAGETYPE(PluginConstant.JAVA_LANG, "_java", ".javajet"),
    BOTHLANGUAGETYPE(PluginConstant.BOTH_LANG, "_perl;_java", ".perljet;.javajet");

    private final String lang;

    private final String fileSuffix;

    private final String nameSuffix;

    LanguageType(String lang, String nameSuffix, String fileSuffix) {
        this.lang = lang;
        this.nameSuffix = nameSuffix;
        this.fileSuffix = fileSuffix;
    }

    public String getLang() {
        return this.lang;
    }

    public String getNameSuffix() {
        return this.nameSuffix;
    }

    public String getFileSuffix() {
        return this.fileSuffix;
    }

    /**
     * Get the corresponding LanguageType enum by the specified lang string.
     * 
     * @param priority
     * @return
     */
    public static final LanguageType find(String lang) {
        LanguageType type = null;
        if (lang.equals(PluginConstant.JAVA_LANG)) {
            type = JAVALANGUAGETYPE;
        } else if (lang.equals(PluginConstant.PERL_LANG)) {
            type = PERLLANGUAGETYPE;
        } else {
            type = BOTHLANGUAGETYPE;
        }
        return type;
    }

    public static final LanguageType find(boolean javaType, boolean perlType) {
        if (javaType && perlType) {
            return BOTHLANGUAGETYPE;
        } else if (javaType) {
            return JAVALANGUAGETYPE;
        } else if (perlType) {
            return PERLLANGUAGETYPE;
        } else {
            return null;
        }
    }
}
