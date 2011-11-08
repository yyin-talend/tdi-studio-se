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
package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.StringUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class CreateNameSpaceAction extends SelectionAction {

    private TreeNode parent;

    private MapperManager mapperManager;

    private boolean input;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.CreateNameSapceInput";

    public CreateNameSpaceAction(IWorkbenchPart part) {
        super(part);
        setText("Set A Namespace");
        setId(ID);
    }

    @Override
    public void run() {
        String prefix = null;
        String defaultValue = null;
        while (!StringUtil.validateLabelForNameSpace(prefix) || !StringUtil.validateLabelForFixedValue(defaultValue)) {
            NameSpaceDialog nsDialog = new NameSpaceDialog(null);
            int status = nsDialog.open();
            if (status == nsDialog.OK) {
                defaultValue = nsDialog.getNSValue();
                if (defaultValue != null) {
                    defaultValue = defaultValue.trim();
                }
                prefix = nsDialog.getPrefix().trim();
            }
            if (status == nsDialog.CANCEL) {
                return;
            }
        }

        TreeNode createdNode = null;
        if (input) {
            createdNode = XmlmapFactory.eINSTANCE.createTreeNode();
        } else {
            createdNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
        }
        createdNode.setName(prefix);
        createdNode.setNodeType(NodeType.NAME_SPACE);
        createdNode.setDefaultValue(defaultValue);
        createdNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
        String label = createdNode.getName();
        if (prefix == null || "".equals(prefix)) {
            label = XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX;
            createdNode.setName(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX);
        }

        createdNode.setXpath(XmlMapUtil.getXPath(parent.getXpath(), label, NodeType.NAME_SPACE));

        final EList<TreeNode> children = parent.getChildren();
        int index = 0;
        for (int i = 0; i < children.size(); i++) {
            final TreeNode treeNode = children.get(i);
            if (treeNode.getNodeType() == NodeType.NAME_SPACE) {
                if (i == children.size() - 1) {
                    index = children.size();
                }
                continue;
            } else {
                index = i;
                break;
            }
        }
        children.add(index, createdNode);

        // children.add(createdNode);

        TreeNode docRoot = XmlMapUtil.getTreeNodeRoot(parent);
        if (docRoot != null && docRoot.eContainer() instanceof OutputXmlTree) {
            mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) docRoot.eContainer());
        } else if (docRoot != null && docRoot.eContainer() instanceof InputXmlTree) {
            mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) docRoot.eContainer());
        }
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            Object object = getSelectedObjects().get(0);
            if (object instanceof TreeNodeEditPart) {
                TreeNodeEditPart nodePart = (TreeNodeEditPart) object;
                this.parent = (TreeNode) nodePart.getModel();
                boolean isElement = NodeType.ELEMENT.equals(parent.getNodeType());
                if (isElement && !XmlMapUtil.DOCUMENT.equals(parent.getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    class NameSpaceDialog extends TitleAreaDialog {

        private Text prefixText;

        private LabelledText nsValueLabel;

        private Button prefixBtn;

        private static final String DEFAULT = "Input the prefix and value of the namespace";

        private String prefixValue = "";

        private String nsValue;

        /**
         * wzhang NameSpaceDialog constructor comment.
         * 
         * @param parentShell
         */
        public NameSpaceDialog(Shell parentShell) {
            super(parentShell);
        }

        @Override
        protected Control createContents(Composite parent) {
            super.createContents(parent);
            this.getShell().setText("Namespace dialog");
            this.setTitle("Input new namespace");
            this.setMessage("Input the prefix and value of the namespace");
            return parent;
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            super.createDialogArea(parent);
            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new GridLayout(6, true));

            nsValueLabel = new LabelledText(composite, "Namespace", 5); //$NON-NLS-1$
            nsValueLabel.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    nsValue = nsValueLabel.getText();
                    validateField();
                }

            });

            prefixBtn = new Button(composite, SWT.CHECK | SWT.RIGHT);
            prefixBtn.setText("Prefix");

            prefixBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (prefixText != null) {
                        if (prefixBtn.getSelection()) {
                            prefixText.setVisible(true);
                        } else {
                            prefixText.setVisible(false);
                            prefixText.setText("");
                        }
                    }
                }
            });

            prefixText = new Text(composite, SWT.BORDER); //$NON-NLS-1$
            GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            gridData.horizontalSpan = 5;
            prefixText.setLayoutData(gridData);
            prefixText.setVisible(false);
            prefixText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    prefixValue = prefixText.getText();
                    validateField();
                }
            });

            return parent;

        }

        private void validateField() {
            if (!StringUtil.validateLabelForNameSpace(prefixValue)) {
                setMessage("Prefix value is invalid!", IMessageProvider.ERROR); //$NON-NLS-1$
                return;
            } else {
                setMessage(DEFAULT);
            }
            if (!StringUtil.validateLabelForFixedValue(nsValue)) {
                setMessage("NameSpace value is invalid!", IMessageProvider.ERROR); //$NON-NLS-1$
                return;
            } else {
                setMessage(DEFAULT);
            }
        }

        @Override
        public void setMessage(String newMessage, int newType) {
            super.setMessage(newMessage, newType);
            Button button = getButton(IDialogConstants.OK_ID);
            if (button != null && !button.isDisposed()) {
                if (!DEFAULT.equals(newMessage)) { // error
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        }

        public String getPrefix() {
            return this.prefixValue;
        }

        public String getNSValue() {
            return this.nsValue;
        }

    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

}
