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
package org.talend.componentdesigner.ui.wizard.creatcomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.componentdesigner.model.componentpref.ComponentPref;
import org.talend.componentdesigner.ui.composite.TreeNodeAttrCompsite;
import org.talend.componentdesigner.ui.composite.xmltree.ATreeNodeUtil;
import org.talend.componentdesigner.ui.composite.xmltree.TreeNodeData;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.w3c.dom.Node;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractXMLConfigPage extends AbstractComponentPage {

    protected static final String IMPORTS = "IMPORTS";

    protected static final String IMPORT = "IMPORT";

    protected static final String REQUIRED = "REQUIRED";

    protected static final String MODULE = "MODULE";

    protected static final String NAME = "NAME";

    protected static final String CODEGENERATION = "CODEGENERATION";

    protected static final String IMPORTITEMPATH = "COMPONENT/CODEGENERATION/IMPORTS/IMPORT";

    protected static final String IMPORTSITEMPATH = "COMPONENT/CODEGENERATION/IMPORTS";

    protected transient Tree availableXmlTree;

    // protected TreePopulator treePopulator;

    protected Composite rightComposite;

    protected TreeNodeAttrCompsite nodeAttrCompsite;

    protected Map<String, String[]> attrsAvailableValuesMap = null;

    protected Map<String, String> attrsToolTipsMap = null;

    /**
     * DOC rli WizardXMLConfigPage constructor comment.
     * 
     * @param pageName
     * @param componentPref
     */
    public AbstractXMLConfigPage(String pageName, ComponentPref componentPref) {
        super(pageName, componentPref);
        loadAttrsInfo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#createPageContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createPageContent(Composite parent) {
        Composite topComposite = new Composite(parent, SWT.NONE);
        topComposite.setLayout(new GridLayout(2, false));
        GridData data = new GridData(GridData.FILL_BOTH);
        topComposite.setLayoutData(data);
        availableXmlTree = new Tree(topComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData treeGD = new GridData(GridData.FILL_BOTH);
        treeGD.widthHint = 110;
        treeGD.grabExcessHorizontalSpace = true;
        treeGD.grabExcessVerticalSpace = true;
        availableXmlTree.setLayoutData(treeGD);

        rightComposite = new Composite(topComposite, SWT.NONE);
        GridData rightPanelGd = new GridData(GridData.FILL_BOTH);
        rightPanelGd.widthHint = 180;
        rightComposite.setLayoutData(rightPanelGd);
        rightComposite.setLayout(new GridLayout());

        Label label = new Label(rightComposite, SWT.NONE);
        label.setText("Set the properties for selected element.");
        GridData labelGd = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(labelGd);

        this.setControl(topComposite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#initialize()
     */
    protected void initialize() {
        availableXmlTree.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (availableXmlTree.getMenu() != null) {
                    availableXmlTree.getMenu().dispose();
                }

                final TreeItem[] treeItem = ((Tree) event.getSource()).getSelection();

                Menu popMenu = new Menu(availableXmlTree);
                MenuItem itemNew = new MenuItem(popMenu, SWT.CASCADE);
                itemNew.setText("New");
                MenuItem itemDel = new MenuItem(popMenu, SWT.CASCADE);
                itemDel.setText("Delete");
                itemDel.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        TreeNodeData currentNodeData = (TreeNodeData) treeItem[0].getParentItem().getData();
                        currentNodeData.getXMLNode().removeChild(((TreeNodeData) treeItem[0].getData()).getXMLNode());
                        treeItem[0].dispose();
                    }
                });

                final TreeNodeData treeNodeData = ((TreeNodeData) treeItem[0].getData());
                if (treeNodeData.isHasChildTreeNode()) {
                    Menu subNewMenu = new Menu(itemNew);
                    itemNew.setMenu(subNewMenu);

                    Object[] subNodeNames = treeNodeData.getChildNodeNames();
                    for (Object nodeName : subNodeNames) {
                        final MenuItem nodeMenuItem = new MenuItem(subNewMenu, SWT.CASCADE);
                        final String itemName = (String) nodeName;
                        nodeMenuItem.setText(itemName);
                        nodeMenuItem.addSelectionListener(new SelectionAdapter() {

                            public void widgetSelected(SelectionEvent e) {
                                TreeItem newItem = new TreeItem(treeItem[0], 0);
                                newItem.setText(itemName);
                                TreeNodeData newNodeData = null;
                                newNodeData = new TreeNodeData();
                                Node parentNode = treeNodeData.getXMLNode();
                                newNodeData.setXMLNode(parentNode.appendChild(parentNode.getOwnerDocument().createElement(
                                        itemName)));
                                newNodeData.setTreePath(treeNodeData.getTreePath() + "/" + itemName);
                                newNodeData.setTreeNode(ATreeNodeUtil.getTreeNodeByPath(newNodeData.getTreePath()));
                                newItem.setData(newNodeData);
                            }
                        });
                    }
                } else {
                    itemNew.setEnabled(false);
                }

                availableXmlTree.setMenu(popMenu);

                if (nodeAttrCompsite != null) {
                    nodeAttrCompsite.dispose();
                }
                rebuildAttrComposite(treeNodeData);
                rightComposite.layout();
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#validatePage()
     */
    protected boolean validatePage() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        if (componentPref.getLibEntries() != null && componentPref.getLibEntries().length != 0) {
            fillImportItems();
        }
        super.setVisible(visible);
    }

    protected abstract void fillImportItems();

    private void rebuildAttrComposite(TreeNodeData nodeData) {
        nodeAttrCompsite = new TreeNodeAttrCompsite(rightComposite, SWT.NONE, nodeData);
        nodeAttrCompsite.setLayoutData(new GridData(GridData.FILL_BOTH));
        nodeAttrCompsite.creatPart(attrsAvailableValuesMap, attrsToolTipsMap);
    }

    protected void loadAttrsInfo(){

        attrsAvailableValuesMap = new HashMap<String, String[]>();

        String[] valuesArray = null;

        // CTYPE
        EConnectionType[] eConnectionTypes = EConnectionType.values();
        valuesArray = new String[eConnectionTypes.length];
        for (int i = 0; i < eConnectionTypes.length; i++) {
            valuesArray[i] = eConnectionTypes[i].getName();
        }
        attrsAvailableValuesMap.put("CTYPE", valuesArray);

        // FIELD
        EParameterFieldType[] eParameterFieldType = EParameterFieldType.values();
        valuesArray = new String[eParameterFieldType.length];
        for (int i = 0; i < eParameterFieldType.length; i++) {
            valuesArray[i] = eParameterFieldType[i].getName();
        }
        attrsAvailableValuesMap.put("FIELD", valuesArray);

        // PLATFORM
        attrsAvailableValuesMap.put("PLATEFORM", new String[] { "ALL", "WINDOWS", "LINUX" });

        // COMPATIBILITY
        attrsAvailableValuesMap.put("COMPATIBILITY", new String[] { "ALL", "WINDOWS", "LINUX" });

        // STATUS
        attrsAvailableValuesMap.put("STATUS", new String[] { "ALPHA", "BETA", "RELEASE" });

        // LINE_STYLE
        attrsAvailableValuesMap.put("LINE_STYLE", new String[] { "1", "2", "3", "4" });

        // NB_LINES
        attrsAvailableValuesMap.put("NB_LINES", new String[] { "1", "2", "3", "4", "5" });

        // MAX_INPUT MIN_INPUT MAX_OUTPUT MIN_OUTPUT
        valuesArray = new String[] { "0", "1", "2", "3", "4", "5" };
        attrsAvailableValuesMap.put("MAX_INPUT", valuesArray);
        attrsAvailableValuesMap.put("MIN_INPUT", valuesArray);
        attrsAvailableValuesMap.put("MAX_OUTPUT", valuesArray);
        attrsAvailableValuesMap.put("MIN_OUTPUT", valuesArray);

        // PROPERTY_TYPE
        List<String> valuesList = new ArrayList<String>();
        valuesList.add("DELIMITED");
        valuesList.add("POSITIONAL");
        valuesList.add("REGEX");
        valuesList.add("XML");
        valuesList.add("LDAP");
        valuesList.add("WSDL");
        valuesList.add("GENERIC");
        valuesList.add("DATABASE");
        for (EDatabaseTypeName eDatabaseTypeName : EDatabaseTypeName.values()) {
            valuesList.add("DATABASE:" + eDatabaseTypeName.getProduct());
        }
        attrsAvailableValuesMap.put("REPOSITORY_VALUE", valuesList.toArray(new String[0]));

        // attributes with boolean value
        String[] booleanAttrNameArray = { "SHOW", "REQUIRED", "READONLY", "BASED_ON_SCHEMA", "KEY", "NULLABLE", "CUSTOM",
                "BUILTIN", "STARTABLE", "SCHEMA_AUTO_PROPAGATE", "DATA_AUTO_PROPAGATE", "HAS_CONDITIONAL_OUTPUTS",
                "IS_MULTIPLYING_OUTPUTS", "VISIBLE", "TSTATCATCHER_STATS", "USE_MERGE" };
        String[] booleanArray = { "true", "false" };
        for (String attrName : booleanAttrNameArray) {
            attrsAvailableValuesMap.put(attrName, booleanArray);
        }

        // init maps for TooTipsText
        attrsToolTipsMap = new HashMap<String, String>();
        attrsToolTipsMap.put("MAX_INPUT", "Integer value. Maximum input connection for this connection");
        attrsToolTipsMap.put("MIN_INPUT", "Integer value. Minimum input connection for this connection");
        attrsToolTipsMap.put("MAX_OUTPUT", "Integer value. Maximum output connection for this connection");
        attrsToolTipsMap.put("MIN_OUTPUT", "Integer value. Minimum output connection for this connection");
        attrsToolTipsMap.put("LINE_STYLE",
                "Integer value. Line style of the connetion.(1: Solid Line, 2: Dash Line, 3: Dot Line, 4: Dashdot Line)");
        attrsToolTipsMap.put("LENGTH", "Integer value.");
        attrsToolTipsMap.put("PRECISION", "Integer value.");
        attrsToolTipsMap.put("NB_LINES", "Integer value. Lines spans of this parameter in the component's properties view");
        attrsToolTipsMap.put("NUM_ROW", "Integer value. Row position of this parameter in the component's properties view");
        attrsToolTipsMap.put("VERSION", "A decimal value to indicate the version of your new component.");
        attrsToolTipsMap.put("REPOSITORY_VALUE", "Metadata repository to select.");
        attrsToolTipsMap.put("EXTENSION", "String value of plugin name. This option is for external componet.");
        attrsToolTipsMap.put("CTYPE", "Connector type.");
        attrsToolTipsMap.put("TYPE", "Data type.");

        attrsToolTipsMap.put("COLOR", "Hex value of RGB. For example: \"00FF00\"");

        for (String attrName : booleanAttrNameArray) {
            attrsToolTipsMap.put(attrName, "Boolean value(true or false).");
        }

    
    }

}
