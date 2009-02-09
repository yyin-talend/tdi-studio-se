// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ui.MetadataTypeLengthConstants;
import org.talend.core.utils.CsvArray;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.filemultischemas.MultiSchemasComponent;
import org.talend.designer.filemultischemas.data.ExternalMultiSchemasUIProperties;
import org.talend.designer.filemultischemas.data.IMultiSchemaConstant;
import org.talend.designer.filemultischemas.data.MultiMetadataColumn;
import org.talend.designer.filemultischemas.data.MultiSchemaRowData;
import org.talend.designer.filemultischemas.data.MultiSchemasMetadataColumn;
import org.talend.designer.filemultischemas.data.SchemasKeyData;
import org.talend.fileprocess.FileInputDelimited;
import org.talend.repository.preview.ProcessDescription;

/**
 * cLi class global comment. Detailled comment
 */
public class MultiSchemasManager {

    private final ECodeLanguage language = LanguageManager.getCurrentLanguage();

    private MultiSchemasComponent connector;

    private UIManager uiManager;

    public MultiSchemasManager(MultiSchemasComponent connector) {
        super();
        this.connector = connector;
    }

    public UIManager getUIManager() {
        if (this.uiManager == null) {
            uiManager = new UIManager(this);
        }
        return this.uiManager;
    }

    public MultiSchemasComponent getMultiSchemasComponent() {
        return this.connector;
    }

    public ECodeLanguage getLanguage() {
        return this.language;
    }

    public void executeCommand(Command cmd) {
        IProcess process = this.getMultiSchemasComponent().getProcess();
        if (process != null && process instanceof IProcess2) {
            CommandStack commandStack = ((IProcess2) process).getCommandStack();
            if (commandStack != null) {
                commandStack.execute(cmd);
                return;
            }
        }
        cmd.execute();
    }

    public void setValues(EParameterName paramName, Object value) {
        IElementParameter param = getMultiSchemasComponent().getElementParameter(paramName.getName());
        if (param != null) {
            param.setValue(value);
        }
    }

    public String getParameterValue(EParameterName paramName) {
        IElementParameter elementParameter = getMultiSchemasComponent().getElementParameter(paramName.getName());
        if (elementParameter != null) {
            Object value = elementParameter.getValue();
            if (value != null && value instanceof String) {
                return (String) value;
            }
        }
        return TalendTextUtils.addQuotes(""); //$NON-NLS-1$
    }

    public Object getParameterObjectValue(EParameterName paramName) {
        IElementParameter elementParameter = getMultiSchemasComponent().getElementParameter(paramName.getName());
        if (elementParameter != null) {
            Object value = elementParameter.getValue();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 
     * cLi Comment method "createSchemasTree".
     * 
     * @param csvArray
     * @return tree of data.
     */
    public SchemasKeyData createSchemasTree(CsvArray csvArray) {
        if (csvArray == null) {
            return null;
        }
        final int columnIndex = 2;
        final SchemasKeyData rootSchemasKeyData = new SchemasKeyData(""); //$NON-NLS-1$

        for (String[] line : csvArray.getRows()) {
            if (line.length < columnIndex) { // must contain 2 columns
                continue;
            }
            final String key = precessString(line[0]);
            if (key == null) {
                continue;
            }

            //
            SchemasKeyData schemaData = findSchemasKeyData(rootSchemasKeyData, key);
            if (schemaData == null) {
                schemaData = new SchemasKeyData(key);
                rootSchemasKeyData.addChild(schemaData);
            }
            //
            MultiSchemaRowData rowData = createRowData(line);
            if (rowData != null) {
                schemaData.addRowsData(rowData);
            }

        }
        for (SchemasKeyData schemaData : rootSchemasKeyData.getChildren()) {
            createPropertiesColumns(schemaData);
        }
        return rootSchemasKeyData;
    }

    private String precessString(String str) {
        if (str != null) {
            str = str.trim();
            if (!"".equals(str)) { //$NON-NLS-1$
                return str;
            }
        }
        return null;
    }

    private SchemasKeyData findSchemasKeyData(SchemasKeyData schemaKeyData, String key) {
        key = key.trim();
        if (schemaKeyData != null || !"".equals(key)) { //$NON-NLS-1$
            if (schemaKeyData.getKeyName().equals(key)) {
                return schemaKeyData;
            }
            for (SchemasKeyData child : schemaKeyData.getChildren()) {
                if (child.getKeyName().equals(key)) {
                    return child;
                }
                SchemasKeyData foundData = findSchemasKeyData(child, key);
                if (foundData != null) {
                    return foundData;
                }
            }
        }

        return null;
    }

    /**
     * 
     * cLi Comment method "createRowData".
     * 
     * @param line
     * @return
     */
    private MultiSchemaRowData createRowData(String[] line) {
        if (line == null || line.length < 1) {
            return null;
        }

        boolean isEndEmpty = true;

        List<String> newDatas = new ArrayList<String>();

        for (int i = line.length - 1; i > -1; i--) {
            String data = line[i];
            // find the last un-empty data.
            if (!"".equals(data) && isEndEmpty) { //$NON-NLS-1$
                isEndEmpty = false;
            }
            if (!isEndEmpty) {
                newDatas.add(data);
            }
        }

        MultiSchemaRowData rowData = new MultiSchemaRowData(precessString(line[0]));

        for (int i = newDatas.size() - 1; i > -1; i--) {
            final String data = newDatas.get(i);
            MultiSchemasMetadataColumn metadataColumn = guessDataProperties(data);
            metadataColumn.setData(data);

            UniqueStringGenerator<MultiSchemasMetadataColumn> generator = getUniqueStringGenerator(rowData.getColumnsData());
            metadataColumn.setLabel(generator.getUniqueString());

            rowData.addColumnData(metadataColumn);
        }
        return rowData;
    }

    private UniqueStringGenerator<MultiSchemasMetadataColumn> getUniqueStringGenerator(List<MultiSchemasMetadataColumn> columns) {
        UniqueStringGenerator<MultiSchemasMetadataColumn> generator = new UniqueStringGenerator<MultiSchemasMetadataColumn>(
                ExternalMultiSchemasUIProperties.DEFAULT_COLUMN_NAME, columns) {

            @Override
            protected String getBeanString(MultiSchemasMetadataColumn bean) {
                return bean.getLabel();
            }

        };
        return generator;
    }

    private MultiSchemasMetadataColumn guessDataProperties(String data) {
        if (data == null) {
            data = ""; //$NON-NLS-1$
        }
        String talendType = null;
        int lengthValue = 0;
        int precisionValue = 0;
        // type
        if (getLanguage() == ECodeLanguage.JAVA) {
            talendType = JavaDataTypeHelper.getTalendTypeOfValue(data);
        } else { // perl
            talendType = PerlDataTypeHelper.getNewTalendTypeOfValue(data);
        }
        // length
        if (lengthValue < data.length()) {
            lengthValue = data.length();
        }
        // precision
        if (!data.equals("")) { //$NON-NLS-1$
            int positionDecimal = 0;
            if (data.indexOf(',') > -1) {
                positionDecimal = data.lastIndexOf(',');
                precisionValue = lengthValue - positionDecimal;
            } else if (data.indexOf('.') > -1) {
                positionDecimal = data.lastIndexOf('.');
                precisionValue = lengthValue - positionDecimal;
            }
        } else {
            talendType = getValueDefaultType();
            lengthValue = getValueDefaultLength();
        }

        //
        MultiSchemasMetadataColumn metadataColumn = new MultiSchemasMetadataColumn();

        if (getLanguage() == ECodeLanguage.JAVA) {
            if (talendType.equals(JavaTypesManager.FLOAT.getId()) || talendType.equals(JavaTypesManager.DOUBLE.getId())) {
                metadataColumn.setPrecision(precisionValue);
            } else {
                metadataColumn.setPrecision(0);
            }
        } else {
            if (talendType.equals(PerlTypesManager.DECIMAL)) {
                metadataColumn.setPrecision(precisionValue);
            } else {
                metadataColumn.setPrecision(0);
            }
        }
        metadataColumn.setTalendType(talendType);
        metadataColumn.setLength(lengthValue);

        return metadataColumn;
    }

    private String getValueDefaultType() {
        String valueDefaultType = MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE;
        if (getLanguage() == ECodeLanguage.PERL) {
            valueDefaultType = MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_TYPE;
        }
        String defaultType = CorePlugin.getDefault().getPreferenceStore().getString(valueDefaultType);
        if (defaultType != null && !"".equals(defaultType.trim())) { //$NON-NLS-1$
            return defaultType;
        }
        return null;
    }

    private int getValueDefaultLength() {
        String valueDefaultLength = MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH;
        if (getLanguage() == ECodeLanguage.PERL) {
            valueDefaultLength = MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_LENGTH;
        }
        String defaultLength = CorePlugin.getDefault().getPreferenceStore().getString(valueDefaultLength);
        if (defaultLength != null && !"".equals(defaultLength.trim())) { //$NON-NLS-1$
            return Integer.parseInt(defaultLength);
        }
        return 0;
    }

    private List<MultiMetadataColumn> createPropertiesColumns(SchemasKeyData keyData) {
        List<MultiMetadataColumn> columns = new ArrayList<MultiMetadataColumn>();
        if (keyData != null) {
            final int tagLevel = keyData.getTagLevel();
            //
            final List<MultiSchemaRowData> rowsData = keyData.getRowsData();

            for (MultiSchemaRowData row : rowsData) {
                final List<MultiSchemasMetadataColumn> columnsData = row.getColumnsData();
                for (int i = 0; i < columnsData.size(); i++) {
                    final MultiSchemasMetadataColumn metadataColumn = columnsData.get(i);

                    MultiMetadataColumn column = null;
                    if (columns.size() - 1 < i) { // not contain
                        column = new MultiMetadataColumn(row.getKeyName());
                        column.setLabel(metadataColumn.getLabel());

                        column.setContainer(keyData);
                        columns.add(column);
                    } else {
                        column = columns.get(i);
                    }
                    column.addDataColumns(metadataColumn);
                }
            }
            keyData.setMetadataColumns(columns);
        }
        return columns;
    }

    /**
     * 
     * cLi Comment method "saveProperties".
     * 
     */
    public void savePropertiesToComponent(SchemasKeyData data, String filePath, String rowSeperator, String fieldSeperator) {
        if (data != null) {
            ChangeMultiSchemasCommand cmd = new ChangeMultiSchemasCommand(getMultiSchemasComponent(), data, filePath,
                    fieldSeperator, rowSeperator);
            executeCommand(cmd);
        }
    }

    /**
     * 
     * cLi Comment method "retrievePropertiesFromNode".
     * 
     */
    @SuppressWarnings("unchecked")
    public SchemasKeyData retrievePropertiesFromNode() {
        SchemasKeyData rootData = new SchemasKeyData(""); //$NON-NLS-1$

        IElementParameter elementParameter = getMultiSchemasComponent().getElementParameter(EParameterName.SCHEMAS.getName());
        final Object value = elementParameter.getValue();
        if (value == null) {
            return null;
        }
        List<Map<String, String>> schemasValues = (List<Map<String, String>>) value;
        createSimpleDatas(rootData, schemasValues);
        retrieveRelativeParent(rootData, schemasValues);
        return rootData;
    }

    private void retrieveRelativeParent(SchemasKeyData rootData, List<Map<String, String>> schemasValues) {
        for (Map<String, String> line : schemasValues) {
            String code = line.get(IMultiSchemaConstant.CODE);
            if (code != null) {
                code = TalendTextUtils.removeQuotes(code);
                SchemasKeyData currentData = findKeyData(rootData, code);
                if (currentData != null) {
                    String codeParent = line.get(IMultiSchemaConstant.CODE_PARENT);
                    if (codeParent != null) {
                        codeParent = TalendTextUtils.removeQuotes(codeParent);
                    }
                    if (codeParent == null || "".equals(codeParent.trim())) { // root //$NON-NLS-1$
                        rootData.addChild(currentData);
                    } else {
                        SchemasKeyData parentData = findKeyData(rootData, codeParent.trim());
                        if (parentData != null) {
                            parentData.addChild(currentData);
                        }
                    }
                }
            }
        }
    }

    private SchemasKeyData findKeyData(SchemasKeyData parent, String key) {
        if (parent == null || key == null) {
            return null;
        }
        for (SchemasKeyData child : parent.getChildren()) {
            if (key.equals(child.getKeyName())) {
                return child;
            }
            SchemasKeyData keydata = findKeyData(child, key);
            if (keydata != null) {
                return keydata;
            }
        }
        return null;
    }

    private void createSimpleDatas(SchemasKeyData rootData, List<Map<String, String>> schemasValues) {

        for (Map<String, String> line : schemasValues) {
            String code = line.get(IMultiSchemaConstant.CODE);
            if (code != null) {
                code = TalendTextUtils.removeQuotes(code);
                SchemasKeyData data = new SchemasKeyData(code);
                final String schemaName = line.get(IMultiSchemaConstant.SCHEMA);
                if (schemaName != null) {
                    final IMetadataTable metadataTable = MetadataTool.getMetadataTableFromNode(getMultiSchemasComponent(),
                            schemaName);
                    if (metadataTable != null) {
                        createMultiSchemasColumns(data, metadataTable.clone(true));
                    }
                    rootData.addChild(data);
                }
            }
        }
    }

    private void createMultiSchemasColumns(SchemasKeyData data, final IMetadataTable metadataTable) {
        //
        for (IMetadataColumn column : metadataTable.getListColumns()) {

            MultiMetadataColumn multiColumn = new MultiMetadataColumn(data.getKeyName());
            multiColumn.setContainer(data);
            data.getMetadataColumns().add(multiColumn);

            multiColumn.setComment(column.getComment());
            multiColumn.setCustom(column.isCustom());
            multiColumn.setCustomId(column.getCustomId());
            multiColumn.setDefault(column.getDefault());
            multiColumn.setId(column.getId());
            multiColumn.setKey(column.isKey());
            multiColumn.setLabel(column.getLabel());
            multiColumn.setLength(column.getLength());
            multiColumn.setNullable(column.isNullable());
            multiColumn.setOriginalDbColumnName(column.getOriginalDbColumnName());
            multiColumn.setPattern(column.getPattern());
            multiColumn.setPrecision(column.getPrecision());
            multiColumn.setReadOnly(column.isReadOnly());
            multiColumn.setTalendType(column.getTalendType());
            multiColumn.setType(column.getType());

        }

    }

    /**
     * 
     * cLi Comment method "getOriginalValue".
     * 
     * check the context or not.
     */
    public String getOriginalValue(String value) {
        if (value != null) {
            IProcess process = this.getMultiSchemasComponent().getProcess();
            if (process != null) {
                String varName = ContextParameterUtils.getVariableFromCode(value);
                if (varName != null) {
                    IContextParameter contextParameter = process.getContextManager().getDefaultContext().getContextParameter(
                            varName);
                    if (contextParameter != null) {
                        String value2 = contextParameter.getValue();
                        if (value2 != null) {
                            if (value2.startsWith(TalendTextUtils.getQuoteChar())
                                    && value2.endsWith(TalendTextUtils.getQuoteChar())) {

                                return value2;
                            } else { // no quote
                                return TalendTextUtils.addQuotes(value2);
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    public CsvArray retrieveCsvArrayInUniqueModel(ProcessDescription processDesc, boolean checked) {
        if (processDesc == null || !checked) {
            return null;
        }
        CsvArray csvArray = new CsvArray();

        final String filePath = TalendTextUtils.removeQuotes(processDesc.getFilepath());
        final String encoding = TalendTextUtils.removeQuotes(processDesc.getEncoding());

        final String fieldSeparator = TalendTextUtils.trimParameter(processDesc.getFieldSeparator());
        final String rowSeparator = TalendTextUtils.trimParameter(processDesc.getRowSeparator());

        final boolean needSkpipEmptyRecord = true;
        final boolean splitRecord = false;
        FileInputDelimited fileInputDelimited = null;
        try {
            fileInputDelimited = new FileInputDelimited(filePath, encoding, fieldSeparator, rowSeparator, needSkpipEmptyRecord,
                    0, 0, -1, -1, splitRecord);
            final int maxColumnCount = FileInputDelimited.getMaxColumnCount(filePath, encoding, fieldSeparator, rowSeparator,
                    needSkpipEmptyRecord, splitRecord, 0, Integer.MAX_VALUE);
            if (maxColumnCount < 1) {
                return null;
            }
            Set<String> uniqueKey = new HashSet<String>();
            String currentRowKey = ""; //$NON-NLS-1$

            while (fileInputDelimited.nextRecord()) {
                final String first = fileInputDelimited.get(0);
                if ("".equals(first.trim())) { // must be contain first //$NON-NLS-1$
                    continue;
                }
                currentRowKey = first.toLowerCase();
                if (uniqueKey.contains(currentRowKey)) { // existed.
                    continue;
                }
                uniqueKey.add(currentRowKey);

                String[] lineDatas = new String[maxColumnCount];
                lineDatas[0] = first;

                for (int i = 1; i < maxColumnCount; i++) {
                    final String value = fileInputDelimited.get(i);
                    lineDatas[i] = value;
                }
                csvArray.add(lineDatas);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (fileInputDelimited != null) {
                fileInputDelimited.close();
            }
        }

        //
        return csvArray;
    }
}
