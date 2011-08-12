// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * WCHEN talend class global comment. Detailled comment
 */
public abstract class XmlTreeSchemaTableView extends AbstractExtendedTableViewer<TreeSchemaTableEntry> {

    public static final String ID_COLUMN_XPATH = "xpath";

    protected boolean isValidName = true;

    protected Composite parentComposite;

    private Composite mainComposite;

    private Label titleLabel;

    public XmlTreeSchemaTableView(ExtendedTableModel<TreeSchemaTableEntry> extendedTableModel, Composite parent,
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

        final String validateNameSpace = validateNameSpace(newValue);
        if (validateNameSpace != null) {
            return validateNameSpace;
        }

        TreeSchemaTableEntry bean = getExtendedTableModel().getBeansList().get(beanPosition);

        int xPathLength = XmlMapUtil.getXPathLength(bean.getXPath());

        if (getExtendedTableModel() != null) {
            for (int i = 0; i < getExtendedTableModel().getBeansList().size(); i++) {
                if (i == beanPosition) {
                    continue;
                }
                TreeSchemaTableEntry entry = getExtendedTableModel().getBeansList().get(i);
                int pathLength = XmlMapUtil.getXPathLength(entry.getXPath());
                if (entry.getName() != null && entry.getName().equals(newValue) && xPathLength == pathLength) {
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

    protected String validateNameSpace(String newValue) {
        if ((newValue.indexOf("(") != -1 || newValue.indexOf(")") != -1)
                && !newValue.equals(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX)) {
            isValidName = false;
            return "Namespace Prefix is invalid";
        }
        return null;

    }
}
