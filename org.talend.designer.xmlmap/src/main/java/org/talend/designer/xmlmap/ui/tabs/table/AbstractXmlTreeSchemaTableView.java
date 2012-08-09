// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.ui.tabs.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlModel;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.StringUtil;

/**
 * WCHEN talend class global comment. Detailled comment
 */
public abstract class AbstractXmlTreeSchemaTableView extends AbstractExtendedTableViewer<TreeSchemaTableEntry> {

    public static final String ID_COLUMN_XPATH = "xpath";

    protected boolean isValidName = true;

    protected Composite parentComposite;

    private Composite mainComposite;

    private Label titleLabel;

    public AbstractXmlTreeSchemaTableView(ExtendedTableModel<TreeSchemaTableEntry> extendedTableModel, Composite parent,
            boolean readOnly, boolean initTable) {
        super(extendedTableModel, parent, readOnly, initTable);
        this.parentComposite = parent;
    }

    public void initGraphicComponents() {
        mainComposite = new Composite(parentComposite, SWT.NONE);
        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(mainComposite.getBackground())) {
            mainComposite.setBackground(parentComposite.getBackground());
        }
        GridLayout layout = new GridLayout();
        mainComposite.setLayout(layout);
        titleLabel = new Label(mainComposite, SWT.NONE);
        titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(titleLabel.getBackground())) {
            titleLabel.setBackground(parentComposite.getBackground());
        }
        if (getExtendedTableModel() != null) {
            titleLabel.setText(getExtendedTableModel().getName());
        }

        initTable();
        if (getExtendedControlModel() != null) {
            initModelListeners();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer#setExtendedControlModel(org.talend.commons
     * .ui.swt.extended.table.AbstractExtendedControlModel)
     */
    @Override
    public void setExtendedControlModel(AbstractExtendedControlModel model) {
        super.setExtendedControlModel(model);
        if (titleLabel != null) {
            titleLabel.setText(model.getName());
        }
    }

    protected String validateXPath(String newValue, int beanPosition) {
        if (beanPosition == -1) {
            return null;
        }
        isValidName = true;
        if (newValue == null || "".equals(newValue)) {
            isValidName = false;
            return "Name can't be null";
        }
        TreeSchemaTableEntry bean = getExtendedTableModel().getBeansList().get(beanPosition);

        if (NodeType.NAME_SPACE == bean.getTreeNode().getNodeType()) {
            final String validateNameSpace = validateNameSpace(newValue, beanPosition);
            if (validateNameSpace != null) {
                return validateNameSpace;
            }
        }

        if (!StringUtil.validateLabelForXML(newValue)) {
            if (NodeType.ELEMENT == bean.getTreeNode().getNodeType()) {
                isValidName = false;
                return "Element name is invalid";
            } else if (NodeType.ATTRIBUT == bean.getTreeNode().getNodeType()) {
                isValidName = false;
                return "Attribute name is invalid";
            }
        }

        return validateEntry(newValue, bean, beanPosition);
    }

    protected String validateEntry(String newValue, TreeSchemaTableEntry bean, int beanPosition) {
        String newXPath = bean.getXPath();
        newXPath = newXPath.substring(0, newXPath.lastIndexOf(bean.getName()));
        newXPath = newXPath + newValue;
        if (getExtendedTableModel() != null) {
            for (int i = 0; i < getExtendedTableModel().getBeansList().size(); i++) {
                if (i == beanPosition) {
                    continue;
                }
                TreeSchemaTableEntry entry = getExtendedTableModel().getBeansList().get(i);
                if (newXPath.equals(entry.getXPath())) {
                    isValidName = false;
                    return "Name alrady existed";
                }
            }
        }

        return null;
    }

    public void refresh() {
        this.getTableViewerCreator().getTableViewer().refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer#getParentComposite()
     */
    @Override
    public Composite getParentComposite() {
        return mainComposite;
    }

    protected String validateNameSpace(String newValue, int beanPosition) {
        if ((newValue.indexOf("(") != -1 || newValue.indexOf(")") != -1)
                && !newValue.equals(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX)) {
            isValidName = false;
            return "Namespace Prefix is invalid";
        }

        if (getExtendedTableModel() != null) {
            for (int i = 0; i < getExtendedTableModel().getBeansList().size(); i++) {
                if (i == beanPosition) {
                    continue;
                }
                TreeSchemaTableEntry entry = getExtendedTableModel().getBeansList().get(i);
                if (NodeType.NAME_SPACE == entry.getTreeNode().getNodeType() && newValue.equals(entry.getName())) {
                    isValidName = false;
                    return "Namespace Prefix already exist";
                }

            }
        }

        return null;

    }
}
