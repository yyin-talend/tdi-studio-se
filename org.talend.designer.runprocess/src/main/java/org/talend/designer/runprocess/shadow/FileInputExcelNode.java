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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.repository.preview.ExcelSchemaBean;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class FileInputExcelNode extends FileInputNode {

    private List<IMetadataTable> metadatas = null;

    private final ExcelSchemaBean excelBean;

    /**
     * 
     * DOC YeXiaowei FileInputExcelNode constructor comment.
     * 
     * @param filename
     * @param metadatas
     * @param encoding
     * @param limitRows
     * @param header
     * @param footer
     * @param emptyEmptyRow
     * @param bean
     */
    public FileInputExcelNode(String filename, List<IMetadataTable> metadatas, String encoding, String limitRows, String header,
            String footer, String emptyEmptyRow, ExcelSchemaBean bean) {
        super("tFileInputExcel"); //$NON-NLS-1$
        excelBean = bean;

        // add base parameters
        String[] paramNames = new String[] {
                "FILENAME", "ENCODING", "LIMIT", "HEADER", "FOOTER", "REMOVE_EMPTY_ROW", "FIRST_COLUMN", "LAST_COLUMN" }; //$NON-NLS-1$
        String[] paramValues = new String[] { filename, encoding == null ? TalendTextUtils.addQuotes("ISO-8859-1") : encoding,
                limitRows.equals("0") ? "50" : limitRows, header, footer, emptyEmptyRow, bean.getFirstColumn(),
                bean.getLastColumn() };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null && !paramValues[i].equals("")) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }

        if (isPerlProject()) {
            addSheetParametersForPerlProject();
        } else {
            addSheetParameterForJavaProject();
        }

        setMetadataList(metadatas);
    }

    public void addSheetParameterForJavaProject() {
        TextElementParameter param = new TextElementParameter("SHEETNAME", TalendTextUtils.addQuotes(excelBean.getSheetName()));
        addParameter(param);
    }

    public void addSheetParametersForPerlProject() {
        TextElementParameter param = new TextElementParameter("ALL_SHEETS", Boolean.toString(excelBean.isSelectAllSheets()));
        addParameter(param);

        ArrayList x = excelBean.getSheetsList();
        if (x == null) {
            x = new ArrayList();
            x.add("Sheet1");
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object string : x) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SHEETNAME", TalendTextUtils.addQuotes(string.toString()));
            list.add(map);
        }

        ObjectElementParameter sheetsListParam = new ObjectElementParameter("SHEETLIST", list);
        sheetsListParam.setListItemsDisplayCodeName(new String[] { "SHEETNAME" });
        addParameter(sheetsListParam);

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

    public boolean isPerlProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.PERL);
    }
}
