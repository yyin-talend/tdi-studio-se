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
package org.talend.designer.fileoutputxml.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.SchemaPopulationUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.xml.XmlUtil;
import org.talend.core.ui.metadata.dialog.RootNodeSelectDialog;
import org.talend.designer.fileoutputxml.ui.FOXUI;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Attribute;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Element;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.FOXTreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.NameSpaceNode;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: ImportTreeFromXMLAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class ImportTreeFromXMLAction extends SelectionProviderAction {

    // the xml viewer, see FOXUI.
    private TreeViewer xmlViewer;

    private FOXUI foxui;

    private boolean isXsd;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public ImportTreeFromXMLAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public ImportTreeFromXMLAction(TreeViewer xmlViewer, FOXUI foxui, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.foxui = foxui;
    }

    private List<FOXTreeNode> treeNodeAdapt() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        FileDialog f = new FileDialog(foxui.getFoxUIParent().getShell());
        String file = f.open();
        if (file == null) {
            return list;
        }
        isXsd = XmlUtil.isXSDFile(file);
        try {
            ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(file, true, 0);
            String schemaName = getSelectedSchema();
            String rootName = "";
            if (treeNode.getDataType() instanceof String) {
                rootName += "/" + treeNode.getValue() + "@" + (String) treeNode.getDataType();
            }
            FOXTreeNode root = cloneATreeNode(treeNode, schemaName, rootName);
            Element rootElement = (Element) root;
            if (rootElement.getElementChildren() != null && rootElement.getElementChildren().size() > 0) {
                for (FOXTreeNode foxTreeNode : rootElement.getElementChildren()) {
                    foxTreeNode.setParent(null);
                    list.add(foxTreeNode);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    private FOXTreeNode cloneATreeNode(ATreeNode treeNode, String schemaName, String currentPath) throws Exception {
        FOXTreeNode node = null;
        if (treeNode.getType() == ATreeNode.ATTRIBUTE_TYPE) {
            node = new Attribute();
        } else {
            node = new Element();
        }
        // zli fixed for bug 7414
        if (treeNode.getType() == ATreeNode.NAMESPACE_TYPE) {
            node = new NameSpaceNode();
            node.setLabel("");//$NON-NLS-1$
            node.setDefaultValue((String) treeNode.getValue());
        } else {
            node.setLabel((String) treeNode.getValue());
        }

        Object[] children = treeNode.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof ATreeNode) {
                    ATreeNode child = (ATreeNode) children[i];
                    String newPath = currentPath + "/";
                    if (child.getDataType() instanceof String) {
                        String elementName = (String) child.getDataType();
                        if (currentPath.contains("@" + elementName + "/")) {
                            ExceptionHandler.process(new Exception("XSD ERROR: loop found. Item: " + elementName
                                    + " is already in the currentPath (" + currentPath + ")."));
                            continue;
                        }
                        newPath += child.getValue() + "@" + elementName;
                    } else {
                        newPath += "unknownElement";
                    }

                    FOXTreeNode foxChild = cloneATreeNode(child, schemaName, newPath);
                    foxChild.setRow(schemaName);
                    node.addChild(foxChild);
                }
            }
        }
        return node;
    }

    /**
     * 
     * wzhang Comment method "getSelectedSchema".
     * 
     * @return
     */
    private String getSelectedSchema() {
        TreeItem[] selection = xmlViewer.getTree().getSelection();
        if (selection.length > 0) {
            Object data = selection[0].getData();
            if (data instanceof FOXTreeNode) {
                return ((FOXTreeNode) data).getRow();
            }
        }
        return foxui.getFoxManager().getCurrentSchema();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        List<FOXTreeNode> newInput = new ArrayList<FOXTreeNode>();
        List<FOXTreeNode> nodes = treeNodeAdapt();
        if (nodes.size() == 0) {
            return;
        }

        boolean changed = true;
        if (nodes.size() > 1 && isXsd) {
            RootNodeSelectDialog dialog = new RootNodeSelectDialog(xmlViewer.getControl().getShell(), nodes);
            if (dialog.open() == IDialogConstants.OK_ID) {
                FOXTreeNode selectedNode = dialog.getSelectedNode();
                newInput.add(selectedNode);
                changed = true;
            } else {
                changed = false;
            }
        } else {
            newInput.add(nodes.get(0));
        }

        if (changed) {
            List<FOXTreeNode> treeData = foxui.getFoxManager().getTreeData(getSelectedSchema());
            treeData.clear();
            treeData.addAll(newInput);
            xmlViewer.setInput(foxui.getFoxManager().getTreeData());
            // TreeUtil.guessAndSetLoopNode((FOXTreeNode) xmlViewer.getTree().getItem(0).getData());
            xmlViewer.refresh();
            xmlViewer.expandAll();
            foxui.updateStatus();
            foxui.redrawLinkers();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
        this.setEnabled(true);
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null) {
            foxui.setSelectedText(node.getLabel());
        }
    }
}
