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
package org.talend.designer.hl7.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.hl7.HL7InputComponent;
import org.talend.designer.hl7.ui.header.TalendHL7Reader;

/**
 * DOC hywang class global comment. Detailled comment <br/>
 * 
 */
public class HL7Manager {

    protected HL7InputComponent hl7Component;

    protected UIManager uiManager;

    protected String messageContent;

    protected Map<String, List<MetadataColumn>> schemaRelationMap;

    private boolean hasFile;

    protected String filePath;

    private String startChar;

    private String endChar;

    /**
     * constructor.
     */
    public HL7Manager(HL7InputComponent hl7Component) {
        this.hl7Component = hl7Component;
        this.uiManager = new UIManager(this);
        this.schemaRelationMap = new HashMap<String, List<MetadataColumn>>();
        readMessageContent();
    }

    private void readMessageContent() {
        filePath = hl7Component.getElementParameter(EParameterName.FILENAME.getName()).getValue().toString();
        String filePathNoQuotes = TalendTextUtils.removeQuotes(filePath);
        File file = Path.fromOSString(filePathNoQuotes).toFile();
        startChar = hl7Component.getElementParameter("START_MSG").getValue().toString();
        endChar = hl7Component.getElementParameter("END_MSG").getValue().toString();
        if (file.exists()) {
            hasFile = true;
            ByteArray array = PropertiesFactory.eINSTANCE.createByteArray();
            try {
                array.setInnerContentFromFile(file);

                TalendHL7Reader talendHL7Reader = new TalendHL7Reader(new java.io.FileInputStream(file), "ISO-8859-15");
                String HL7InputTem = null;
                String messageText = "";
                // talendHL7Reader.setStartMsgChar(startChar.getText().toCharArray()[0]);
                // talendHL7Reader.setEndMsgChar(endChar.getText().toCharArray()[0]);

                while ((HL7InputTem = talendHL7Reader.getMessage()) != null) {
                    messageText = messageText + HL7InputTem + "\r";
                }
                if (messageText == null || "".equals(messageText)) {
                    messageText = new String(array.getInnerContent());
                }
                this.messageContent = messageText;
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        } else {
            String content = hl7Component.getElementParameter("MESSAGE").getValue().toString();
            if (content != null && !"".equals(content)) {
                this.messageContent = content;
                hasFile = true;
            } else {
                hasFile = false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void updateRelationMapping(String key, MetadataColumn modifiedColumn, boolean isAdd) {
        if (schemaRelationMap != null) {
            List<MetadataColumn> schema = schemaRelationMap.get(key);
            if (schema != null) {
                if (!isAdd) {
                    for (MetadataColumn column : schema) {
                        if (column.getLabel().equals(modifiedColumn.getLabel())) {
                            schema.remove(column);
                            break;
                        }
                    }
                } else {
                    schema.add(modifiedColumn);
                }
            }
        } else {
            schemaRelationMap = new HashMap<String, List<MetadataColumn>>();
        }
    }

    public boolean saveDataToComponent() {
        boolean result = false;
        List<Map<String, String>> schemas = convertMetadataColumns2Propertis();
        result = hl7Component.setTableElementParameter(schemas, "SCHEMAS"); //$NON-NLS-N$
        String messageContent = this.getUiManager().getHl7UI().getHeader().getMessageContent();
        String startNsg = this.getUiManager().getHl7UI().getHeader().getStartCharValue();
        String endNsg = this.getUiManager().getHl7UI().getHeader().getEndCharValue();
        hl7Component.setValueToParameter("MESSAGE", messageContent);
        hl7Component.setValueToParameter("START_MSG", startNsg);
        hl7Component.setValueToParameter("END_MSG", endNsg);
        String filePath = this.getUiManager().getHl7UI().getHeader().getFilePath();
        hl7Component.setValueToParameter(EParameterName.FILENAME.getName(), filePath);
        return result;
    }

    public List<Map<String, String>> convertMetadataColumns2Propertis() {
        List<Map<String, String>> schemas = new ArrayList<Map<String, String>>();
        Iterator<String> it = schemaRelationMap.keySet().iterator();
        while (it.hasNext()) {
            MetadataTable metatable = ConnectionFactory.eINSTANCE.createMetadataTable();
            String schemaKey = it.next().toString();
            List<MetadataColumn> columns = new ArrayList<MetadataColumn>();
            columns.addAll(schemaRelationMap.get(schemaKey));
            Map<String, String> oneSchema = new HashMap<String, String>();
            String displayName = ""; //$NON-NLS-N$
            for (int i = 0; i < columns.size(); i++) {
                MetadataColumn column = columns.get(i);
                String original = columns.get(i).getOriginalField();
                if (original != null && !"".equals(original)) {
                    original = original.substring(0, original.indexOf(TalendTextUtils.LBRACKET));
                }
                if (i != columns.size() - 1) {
                    displayName = displayName + TalendTextUtils.QUOTATION_MARK + original + TalendTextUtils.QUOTATION_MARK + ",";
                } else {
                    displayName = displayName + TalendTextUtils.QUOTATION_MARK + original + TalendTextUtils.QUOTATION_MARK;
                }
                if (column.getSourceType() == null) {
                    column.setSourceType("id_String");
                }
                if (column.getTalendType() == null) {
                    column.setTalendType("id_String");
                }
                // column.setLabel(column.getOriginalField()); // display user defined column name
                metatable.getColumns().add(column);
            }
            metatable.setLabel(schemaKey);
            IMetadataTable table = ConvertionHelper.convert(metatable);
            if (displayName != null && !"".equals(displayName)) {
                oneSchema.put("SCHEMA", schemaKey); //$NON-NLS-N$
                oneSchema.put("MAPPING", displayName); //$NON-NLS-N$
                schemas.add(oneSchema);
            }
            if (table != null && !table.getListColumns().isEmpty()) {
                boolean find = false;
                int index = 0;
                for (int i = 0; i < hl7Component.getMetadataList().size(); i++) {
                    if (table.getLabel().equals(hl7Component.getMetadataList().get(i).getLabel())) {
                        find = true;
                        index = i;
                        break;
                    }
                }
                if (find) {
                    hl7Component.getMetadataList().remove(index);
                }
                hl7Component.getMetadataList().add(table);
            }
        }
        return schemas;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public UIManager getUiManager() {
        return this.uiManager;
    }

    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public HL7InputComponent getHl7Component() {
        return this.hl7Component;
    }

    public Map<String, List<MetadataColumn>> getSchemaRelationMap() {
        return this.schemaRelationMap;
    }

    public boolean isHasFile() {
        return this.hasFile;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getStartChar() {
        return this.startChar;
    }

    public String getEndChar() {
        return this.endChar;
    }

}
