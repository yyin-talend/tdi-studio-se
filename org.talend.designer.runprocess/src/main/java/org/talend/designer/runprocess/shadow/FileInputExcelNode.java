// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.repository.preview.ExcelSchemaBean;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class FileInputExcelNode extends FileInputNode {

    private List<IMetadataTable> metadatas = null;

    private final ExcelSchemaBean excelBean;

    private String versionCheck; // hywang add for excel2007 wizard

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
            String footer, String emptyEmptyRow, ExcelSchemaBean bean, String versionCheck) {
        super("tFileInputExcel"); //$NON-NLS-1$
        excelBean = bean;

        // add base parameters
        String[] paramNames = new String[] {
                "FILENAME", "ENCODING", "LIMIT", "HEADER", "FOOTER", "REMOVE_EMPTY_ROW", "FIRST_COLUMN", "LAST_COLUMN", "WITH_FORMAT" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

        String[] paramValues;

        // // hywang add for excel 2007
        // ElementParameter elem = new ElementParameter(inNode);
        // elem.setName("VERSION_2007");
        // elem.setValue(true);

        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            paramValues = new String[] { filename, encoding == null ? TalendTextUtils.addQuotes("ISO-8859-1") : encoding, //$NON-NLS-1$
                    limitRows.equals("0") ? "50" : limitRows, header, footer, emptyEmptyRow, bean.getFirstColumn(), //$NON-NLS-1$ //$NON-NLS-2$
                    bean.getLastColumn(), "true" }; //$NON-NLS-1$
            break;
        default: // PERL
            paramValues = new String[] { filename, encoding == null ? TalendTextUtils.addQuotes("ISO-8859-1") : encoding, //$NON-NLS-1$
                    limitRows.equals("0") ? "50" : limitRows, header, footer, emptyEmptyRow, //$NON-NLS-1$ //$NON-NLS-2$
                    bean.getFirstColumn() == null ? null : "'" + bean.getFirstColumn() + "'", //$NON-NLS-1$ //$NON-NLS-2$
                    bean.getLastColumn() == null ? null : "'" + bean.getLastColumn() + "'", "true" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null && !paramValues[i].equals("")) { //$NON-NLS-1$
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }

        // hywang for excel2007 wizard
        ElementParameter checkVesionFor2007 = new ElementParameter(this);
        checkVesionFor2007.setName("VERSION_2007"); //$NON-NLS-N$
        checkVesionFor2007.setValue(versionCheck);
        addParameter(checkVesionFor2007);

        addSheetsParameters();

        if (!isPerlProject()) {
            addJavaSpecificParameters();
        }

        setMetadataList(metadatas);
        int nbColumns = 0;
        for (IMetadataTable metadata : metadatas) {
            nbColumns += metadata.getListColumns().size();
        }
        setColumnNumber(nbColumns);
    }

    public void addJavaSpecificParameters() {
        String[] paramNames = new String[] { "ADVANCED_SEPARATOR", "THOUSANDS_SEPARATOR", "DECIMAL_SEPARATOR" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String[] paramValues = new String[] { Boolean.toString(excelBean.isAdvancedSeparator()),
                TalendTextUtils.addQuotes(excelBean.getThousandSeparator()),
                TalendTextUtils.addQuotes(excelBean.getDecimalSeparator()) };
        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null && !paramValues[i].equals("")) { //$NON-NLS-1$
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }

    public void addSheetsParameters() {
        TextElementParameter param = new TextElementParameter("ALL_SHEETS", Boolean.toString(excelBean.isSelectAllSheets())); //$NON-NLS-1$
        addParameter(param);

        ArrayList x = excelBean.getSheetsList();
        if (x == null) {
            x = new ArrayList();
            x.add("Sheet1"); //$NON-NLS-1$
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object string : x) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SHEETNAME", TalendTextUtils.addQuotes(string.toString())); //$NON-NLS-1$
            list.add(map);
        }

        ObjectElementParameter sheetsListParam = new ObjectElementParameter("SHEETLIST", list); //$NON-NLS-1$
        sheetsListParam.setListItemsDisplayCodeName(new String[] { "SHEETNAME" }); //$NON-NLS-1$
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
