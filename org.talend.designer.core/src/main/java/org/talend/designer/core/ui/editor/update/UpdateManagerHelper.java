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
package org.talend.designer.core.ui.editor.update;

import java.util.List;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;

/**
 * ggu class global comment. Detailled comment
 */
public final class UpdateManagerHelper {

    // public static String checkQuotes(String query) {
    // if (query == null) {
    // return TalendTextUtils.addQuotes(UpdatesConstants.EMPTY);
    // }
    //
    // ECodeLanguage language = LanguageManager.getCurrentLanguage();
    // String quote = null;
    // switch (language) {
    // case JAVA:
    // quote = TalendTextUtils.QUOTATION_MARK;
    // break;
    // case PERL:
    // default: // PERL
    // quote = TalendTextUtils.SINGLE_QUOTE;
    // break;
    // }
    // String tmpQuery = query;
    // tmpQuery = tmpQuery.replaceAll("\r", UpdatesConstants.SPACE);
    // tmpQuery = tmpQuery.replaceAll("\n", UpdatesConstants.SPACE);
    // tmpQuery = tmpQuery.trim();
    // if (tmpQuery.startsWith(quote) && tmpQuery.endsWith(quote)) {
    // return query;
    // }
    // return TalendTextUtils.addQuotes(query);
    // }

    /**
     * qzhang Comment method "getNewOutputTableForConnection".
     * 
     * @param newOutputTableList
     * @param attachedConnector
     * @return
     */
    public static IMetadataTable getNewOutputTableForConnection(List<IMetadataTable> newOutputTableList, String tableName) {
        for (IMetadataTable metadataTable : newOutputTableList) {
            if (tableName != null && tableName.equals(metadataTable.getTableName())
                    || tableName.equals(metadataTable.getTableName())) {
                return metadataTable;
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "getNewInputTableForConnection".
     * 
     * @param newInputTableList
     * @param connector
     * 
     * @return
     */
    public static IMetadataTable getNewInputTableForConnection(List<IMetadataTable> newInputTableList, String connector) {
        for (IMetadataTable metadataTable : newInputTableList) {
            if (connector != null
                    && (connector.equals(metadataTable.getAttachedConnector()) || connector.equals(metadataTable.getTableName()))) {
                return metadataTable;
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "getElemParam".
     * 
     * @param elemParams
     * @param string
     * 
     * @return
     */
    public static IElementParameter getElemParam(List<IElementParameter> elemParams, String string) {
        for (IElementParameter elementParameter : elemParams) {
            if (string != null && string.equals(elementParameter.getContext())) {
                return elementParameter;
            }
        }
        return null;
    }

}
