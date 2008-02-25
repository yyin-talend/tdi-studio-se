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
package org.talend.componentdesigner.ui.composite;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.ui.composite.xmltree.TreeNodeData;
import org.w3c.dom.Element;

/**
 * DOC rli class global comment. Detailled comment
 */
public class TreeNodeAttrCompsite extends Composite {

    private static final String MARK = ":";

    private final TreeNodeData nodeData;

    private static Map<String, String[]> attrValuesMap = new HashMap<String, String[]>();

    private static Map<String, String> attrToolTipsMap = new HashMap<String, String>();

    static {
        // attrValuesMap
        String[] booleanAttrNameArray = { "SHOW", "REQUIRED", "READONLY", "BASED_ON_SCHEMA", "KEY", "NULLABLE", "CUSTOM",
                "BUILTIN", "STARTABLE", "SCHEMA_AUTO_PROPAGATE", "DATA_AUTO_PROPAGATE", "HAS_CONDITIONAL_OUTPUTS",
                "IS_MULTIPLYING_OUTPUTS", "VISIBLE", "TSTATCATCHER_STATS", "USE_MERGE" };
        String[] booleanArray = { "true", "false" };

        for (String attrName : booleanAttrNameArray) {
            attrValuesMap.put(attrName, booleanArray);
        }

        // reference: org.talend.core.model.process.EConnectionType
        String[] connectionTypeArray = { "FLOW", "SUBJOB_OK", "AFTER", "COMPONENT_OK", "SUBJOB_ERROR", "COMPONENT_ERROR",
                "RUN_IF", "ITERATE", "LOOKUP", "TABLE", "MERGE", "ROWS_END" };
        attrValuesMap.put("CTYPE", connectionTypeArray);

        // reference: org.talend.core.model.process.EParameterFieldType
        String[] fieldTypeArray = { "TEXT", "MEMO_SQL", "MEMO_PERL", "MEMO_JAVA", "CLOSED_LIST", "CHECK", "MEMO", "SCHEMA_TYPE",
                "QUERYSTORE_TYPE", "PROPERTY_TYPE", "EXTERNAL", "FILE", "VERSION", "TABLE", "DIRECTORY", "PROCESS_TYPE", "IMAGE",
                "COLUMN_LIST", "CONNECTION_LIST", "PREV_COLUMN_LIST", "CONTEXT_PARAM_NAME_LIST", "LOOKUP_COLUMN_LIST",
                "TECHNICAL", "ENCODING_TYPE", "COMPONENT_LIST", "MAPPING_TYPE", "COLOR", "DBTABLE", "DATE", "DBTYPE_LIST",
                "LABEL", "AS400_CHECK", "MODULE_LIST", "COMMAND", "PALO_DIM_SELECTION" };
        attrValuesMap.put("FIELD", fieldTypeArray);

        attrValuesMap.put("AUTHOR", new String[] { "Talend" });
        attrValuesMap.put("AVAILABILITY", new String[] { "AFTER" });
        attrValuesMap.put("TYPE", new String[] { "id_Integer" });
        attrValuesMap.put("PLATEFORM", new String[] { "ALL" });
        attrValuesMap.put("COMPATIBILITY", new String[] { "ALL" });
        attrValuesMap.put("STATUS", new String[] { "ALPHA" });
        attrValuesMap.put("MAX_INPUT", new String[] { "0", "1" });
        attrValuesMap.put("MIN_INPUT", new String[] { "0", "1" });
        attrValuesMap.put("MAX_OUTPUT", new String[] { "0", "1" });
        attrValuesMap.put("MIN_OUTPUT", new String[] { "0", "1" });

        // attrToolTipsMap
        attrToolTipsMap.put("MAX_INPUT", "Integer value.");
        attrToolTipsMap.put("MIN_INPUT", "Integer value.");
        attrToolTipsMap.put("MAX_OUTPUT", "Integer value.");
        attrToolTipsMap.put("MIN_OUTPUT", "Integer value.");
        attrToolTipsMap.put("LINE_STYLE", "Integer value.");
        attrToolTipsMap.put("LENGTH", "Integer value.");
        attrToolTipsMap.put("PRECISION", "Integer value.");
        attrToolTipsMap.put("NB_LINES", "Integer value.");
        attrToolTipsMap.put("NUM_ROW", "Integer value.");
        attrToolTipsMap.put("VERSION", "A decimal value to indicate the version of your new component.");

        attrToolTipsMap.put("COLOR", "Hex value of RGB. For example: \"00FF00\"");

        for (String attrName : booleanAttrNameArray) {
            attrToolTipsMap.put(attrName, "Boolean value(true or false).");
        }

    }

    public TreeNodeAttrCompsite(Composite parent, int style, TreeNodeData nodeData) {
        super(parent, style);
        this.nodeData = nodeData;
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
    }

    public void creatPart() {
        GridData gd = null;
        if ((!nodeData.isHasTreeAttr()) && (!nodeData.isHasChildTreeNode())) {
            Label label = new Label(this, SWT.NONE);
            label.setText(nodeData.getXMLNode().getNodeName() + MARK);
            gd = new GridData();
            gd.horizontalSpan = 2;
            label.setLayoutData(gd);
            final Text nodeText = new Text(this, SWT.BORDER | SWT.MULTI | SWT.WRAP);
            gd = new GridData(GridData.FILL_BOTH);
            gd.horizontalSpan = 2;
            nodeText.setLayoutData(gd);
            nodeText.setText(nodeData.getBodayText());

            nodeText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    nodeData.getXMLNode().setTextContent(nodeText.getText());
                    nodeData.setBodayText(nodeText.getText());
                }
            });
        } else {
            for (Object attrName : nodeData.getTreeAttrNames()) {
                final Label attrLabel = new Label(this, SWT.NONE);
                attrLabel.setText(attrName + MARK);
                attrLabel.setLayoutData(new GridData());
                String[] valuesArray = attrValuesMap.get(attrName);
                String tooTipsText = attrToolTipsMap.get(attrName);

                if (valuesArray == null) {
                    final Text attrText = new Text(this, SWT.BORDER);
                    gd = new GridData(GridData.FILL_HORIZONTAL);
                    attrText.setLayoutData(gd);
                    if (nodeData.getAttrValue((String) attrName) != null) {
                        attrText.setText(nodeData.getAttrValue((String) attrName));
                    }

                    if (tooTipsText != null) {
                        attrText.setToolTipText(tooTipsText);
                    }

                    attrText.addModifyListener(new ModifyListener() {

                        public void modifyText(ModifyEvent e) {
                            String attrName = attrLabel.getText().split(MARK)[0];
                            if (attrText.getText().equals(PluginConstant.EMPTY_STRING)) {
                                ((Element) nodeData.getXMLNode()).removeAttribute(attrName);
                                nodeData.removeAttrValue(attrName);
                            } else {
                                ((Element) nodeData.getXMLNode()).setAttribute(attrName, attrText.getText());
                                nodeData.putAttrValue(attrName, attrText.getText());
                            }
                        }

                    });
                } else {
                    final Combo attrCombo = new Combo(this, SWT.BORDER);
                    gd = new GridData(GridData.FILL_HORIZONTAL);
                    attrCombo.setLayoutData(gd);
                    attrCombo.setItems(valuesArray);
                    String attrValue = nodeData.getAttrValue((String) attrName);
                    if (attrValue != null) {
                        int index = -1;
                        for (int i = 0; i < valuesArray.length; i++) {
                            if (valuesArray[i].equals(attrValue)) {
                                index = i;
                                break;
                            }
                        }

                        if (index == -1) {
                            attrCombo.setText(attrValue);
                        } else {
                            attrCombo.select(index);

                        }
                    }

                    if (tooTipsText != null) {
                        attrCombo.setToolTipText(tooTipsText);
                    }

                    attrCombo.addModifyListener(new ModifyListener() {

                        public void modifyText(ModifyEvent e) {
                            String attrName = attrLabel.getText().split(MARK)[0];
                            if (attrCombo.getText().equals(PluginConstant.EMPTY_STRING)) {
                                ((Element) nodeData.getXMLNode()).removeAttribute(attrName);
                                nodeData.removeAttrValue(attrName);
                            } else {
                                ((Element) nodeData.getXMLNode()).setAttribute(attrName, attrCombo.getText());
                                nodeData.putAttrValue(attrName, attrCombo.getText());
                            }
                        }

                    });
                }

            }
        }
    }
}
