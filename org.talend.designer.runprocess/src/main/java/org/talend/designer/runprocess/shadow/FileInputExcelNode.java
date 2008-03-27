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
package org.talend.designer.runprocess.shadow;

import java.util.List;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.utils.TalendTextUtils;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class FileInputExcelNode extends FileInputNode {

    private List<IMetadataTable> metadatas = null;

    /**
     * 
     * DOC YeXiaowei FileInputExcelNode constructor comment.
     * 
     * @param filename
     * @param metadatas
     * @param encoding
     * @param sheetName
     * @param limitRows
     * @param header
     * @param footer
     * @param emptyEmptyRow
     */
    public FileInputExcelNode(String filename, List<IMetadataTable> metadatas, String encoding, String sheetName,
            String limitRows, String header, String footer, String emptyEmptyRow) {
        super("tFileInputExcel"); //$NON-NLS-1$

        String[] paramNames = new String[] { "FILENAME", "ENCODING", "SHEETNAME", "LIMIT", "HEADER", "FOOTER", "REMOVE_EMPTY_ROW" }; //$NON-NLS-1$
        String[] paramValues = new String[] { filename, encoding == null ? TalendTextUtils.addQuotes("ISO-8859-1") : encoding,
                (sheetName != null) ? TalendTextUtils.addQuotes(sheetName) : TalendTextUtils.addQuotes("Sheet1"),
                limitRows.equals("0") ? "50" : limitRows, header, footer, emptyEmptyRow };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null && !paramValues[i].equals("")) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }

        setMetadataList(metadatas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#getMetadataList()
     */
    @Override
    public List<IMetadataTable> getMetadataList() {
        return metadatas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#setMetadataList(java.util.List)
     */
    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
        this.metadatas = metadataList;
    }
}
