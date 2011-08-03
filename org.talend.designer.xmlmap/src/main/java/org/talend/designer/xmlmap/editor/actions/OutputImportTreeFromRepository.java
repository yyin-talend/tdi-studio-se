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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Attribute;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Element;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.FOXTreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.NameSpaceNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.TreeUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputImportTreeFromRepository extends ImportTreeFromRepository {

    private OutputTreeNode parentNode;

    private Shell shell;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.OutputImportTreeFromRepository";

    private Map<OutputTreeNode, XMLFileNode> nodeMap = new HashMap<OutputTreeNode, XMLFileNode>();

    public OutputImportTreeFromRepository(IWorkbenchPart part, Shell shell) {
        super(part);
        this.shell = shell;
        setId(ID);
        setText("Import From Repository");
    }

    @Override
    public void run() {
        RepositoryReviewDialog reviewDialog = new RepositoryReviewDialog(shell, ERepositoryObjectType.METADATA, "XML");
        if (reviewDialog.open() == Window.OK) {
            TreeNode treeNodeRoot = XmlMapUtil.getOutputTreeNodeRoot(parentNode);
            XmlMapUtil.detachNodeConnections(treeNodeRoot, mapperManager.getCopyOfMapData(), true);
            parentNode.getChildren().clear();
            RepositoryNode repositoryNode = reviewDialog.getResult();
            XmlFileConnectionItem item = (XmlFileConnectionItem) repositoryNode.getObject().getProperty().getItem();
            XmlFileConnection connection = (XmlFileConnection) item.getConnection();
            prepareEmfTreeNode(connection);
            nodeMap.clear();

            // if import fail , add back the root node
            if (parentNode.getChildren().isEmpty()) {
                OutputTreeNode rootNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                rootNode.setName("root");
                rootNode.setNodeType(NodeType.ELEMENT);
                rootNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                rootNode.setXpath(XmlMapUtil.getXPath(parentNode.getXpath(), "root", NodeType.ELEMENT));
                parentNode.getChildren().add(rootNode);
                showError();
            }

            if (mapperManager != null && parentNode.eContainer() instanceof OutputXmlTree) {
                mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) parentNode.eContainer());
            }
        }
    }

    /*
     * same as XmlFileOutputStep2Form.initXmlTreeData()
     */
    private void prepareEmfTreeNode(XmlFileConnection connection) {
        OutputTreeNode rootNode = null;
        OutputTreeNode current = parentNode;
        if (!connection.isInputModel()) {
            List<XMLFileNode> root = connection.getRoot();
            List<XMLFileNode> loop = connection.getLoop();
            List<XMLFileNode> group = connection.getGroup();

            OutputTreeNode temp = null;
            OutputTreeNode mainNode = null;
            String mainPath = null;
            String currentPath = null;

            // build root
            for (int i = 0; i < root.size(); i++) {
                XMLFileNode node = (XMLFileNode) root.get(i);
                String newPath = node.getXMLPath();
                String type = node.getType();

                if (node.getAttribute().equals("attri")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.ATTRIBUT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    current.getChildren().add(temp);
                } else if (node.getAttribute().equals("ns")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.NAME_SPACE);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));

                    current.getChildren().add(temp);
                } else {
                    temp = this.addElement(current, currentPath, node);
                    temp.setNodeType(NodeType.ELEMENT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));

                    if (rootNode == null) {
                        rootNode = temp;
                    }
                    if (node.getAttribute().equals("main")) {
                        temp.setMain(true);
                        mainNode = temp;
                        mainPath = newPath;
                    }
                    current = temp;
                    currentPath = newPath;
                }
                temp.setType(type);

            }

            // build group tree
            current = mainNode;
            currentPath = mainPath;
            boolean isFirst = true;
            for (int i = 0; i < group.size(); i++) {
                XMLFileNode node = (XMLFileNode) group.get(i);
                String newPath = node.getXMLPath();
                String type = node.getType();

                if (node.getAttribute().equals("attri")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.ATTRIBUT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    current.getChildren().add(temp);
                } else if (node.getAttribute().equals("ns")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.NAME_SPACE);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    current.getChildren().add(temp);
                } else {
                    temp = this.addElement(current, currentPath, node);
                    temp.setNodeType(NodeType.ELEMENT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    if (node.getAttribute().equals("main")) {
                        temp.setMain(true);
                        mainNode = temp;
                        mainPath = newPath;
                    }
                    if (isFirst) {
                        temp.setGroup(true);
                        isFirst = false;
                    }
                    current = temp;
                    currentPath = newPath;
                }

                temp.setType(type);

            }

            // build loop tree
            current = mainNode;
            currentPath = mainPath;
            isFirst = true;
            for (int i = 0; i < loop.size(); i++) {
                XMLFileNode node = (XMLFileNode) loop.get(i);
                String newPath = node.getXMLPath();
                String type = node.getType();

                if (node.getAttribute().equals("attri")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.ATTRIBUT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    current.getChildren().add(temp);
                } else if (node.getAttribute().equals("ns")) {
                    temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                    temp.setName(newPath);
                    temp.setDefaultValue(node.getDefaultValue());
                    temp.setNodeType(NodeType.NAME_SPACE);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    current.getChildren().add(temp);
                } else {
                    temp = this.addElement(current, currentPath, node);
                    temp.setNodeType(NodeType.ELEMENT);
                    temp.setXpath(XmlMapUtil.getXPath(current.getXpath(), temp.getName(), temp.getNodeType()));
                    if (node.getAttribute().equals("main")) {
                        temp.setMain(true);
                        mainNode = temp;
                        mainPath = newPath;
                    }
                    if (isFirst) {
                        temp.setLoop(true);
                        isFirst = false;
                    }
                    current = temp;
                    currentPath = newPath;
                }
                temp.setType(type);

            }
            if (rootNode == null) {
                rootNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                rootNode.setName("root");
                rootNode.setNodeType(NodeType.ELEMENT);
                rootNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                rootNode.setXpath(XmlMapUtil.getXPath(parentNode.getXpath(), "root", NodeType.ELEMENT));
            }
            parentNode.getChildren().add(rootNode);
        } else {
            String absoluteXPathQuery = "";
            List<SchemaTarget> schemaTargets = null;
            if (!connection.getSchema().isEmpty() && connection.getSchema().get(0) instanceof XmlXPathLoopDescriptorImpl) {
                absoluteXPathQuery = ((XmlXPathLoopDescriptorImpl) connection.getSchema().get(0)).getAbsoluteXPathQuery();
                schemaTargets = ((XmlXPathLoopDescriptorImpl) connection.getSchema().get(0)).getSchemaTargets();
            }

            // fix for TDI-8707 : only import mapped elements from connection to xml map
            if (schemaTargets == null || schemaTargets.isEmpty()) {
                return;
            }

            String file = connection.getXmlFilePath();
            List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
            File xmlFile = new File(file);
            if (xmlFile.exists()) {
                list = TreeUtil.getFoxTreeNodesForXmlMap(file, absoluteXPathQuery);
            } else if (connection.getFileContent() != null && connection.getFileContent().length > 0) {
                String xsdFile = initFileContent(connection);
                if (xsdFile != null && new File(xsdFile).exists()) {
                    list = TreeUtil.getFoxTreeNodesForXmlMap(xsdFile, absoluteXPathQuery);
                }
            }
            prepareEmfTreeNode(list, parentNode, null, absoluteXPathQuery, schemaTargets);
        }

    }

    private void prepareEmfTreeNode(List<FOXTreeNode> list, OutputTreeNode parent, String xmlPath, String absoluteXPathQuery,
            List<SchemaTarget> schemaTargets) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String xPath = parent.getXpath();
        for (FOXTreeNode foxNode : list) {
            OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
            createTreeNode.setName(foxNode.getLabel());
            if (foxNode instanceof Element) {
                createTreeNode.setNodeType(NodeType.ELEMENT);
            } else if (foxNode instanceof Attribute) {
                createTreeNode.setNodeType(NodeType.ATTRIBUT);
            } else if (foxNode instanceof NameSpaceNode) {
                createTreeNode.setNodeType(NodeType.NAME_SPACE);
                createTreeNode.setDefaultValue(foxNode.getDefaultValue());
                if (createTreeNode.getName() == null || createTreeNode.getName().equals("")) {
                    createTreeNode.setName(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX);
                }
            }
            createTreeNode.setXpath(XmlMapUtil.getXPath(xPath, createTreeNode.getName(), createTreeNode.getNodeType()));
            if (foxNode.getDataType() != null && "".equals(foxNode.getDataType())) {
                createTreeNode.setType(foxNode.getDataType());
            } else {
                createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
            }

            String tempXpath = null;
            if (xmlPath == null) {
                tempXpath = XmlMapUtil.XPATH_SEPARATOR + foxNode.getLabel();
            } else {
                tempXpath = xmlPath + XmlMapUtil.XPATH_SEPARATOR + foxNode.getLabel();
            }

            if (tempXpath.equals(absoluteXPathQuery)) {
                if (!(parent.eContainer() instanceof AbstractInOutTree)) {
                    createTreeNode.setLoop(true);
                }
            } else if (!isMappedChild(tempXpath, absoluteXPathQuery, schemaTargets)) {
                continue;
            }

            parent.getChildren().add(createTreeNode);
            if (foxNode.getChildren() != null && !foxNode.getChildren().isEmpty()) {
                prepareEmfTreeNode(foxNode.getChildren(), createTreeNode, tempXpath, absoluteXPathQuery, schemaTargets);
            }
        }

    }

    protected OutputTreeNode addElement(OutputTreeNode current, String currentPath, XMLFileNode node) {
        String newPath = node.getXMLPath();
        String defaultValue = node.getDefaultValue();
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        OutputTreeNode temp = XmlmapFactory.eINSTANCE.createOutputTreeNode();
        temp.setName(name);
        temp.setDefaultValue(defaultValue);

        if (current == parentNode) { // root node of a document
            return temp;
        }

        if (currentPath.equals(parentPath)) {
            EList<TreeNode> children = current.getChildren();
            int childrenSize = children.size();
            /*
             * keep the same order as in repository
             */
            int index = childrenSize;
            int order = node.getOrder();
            for (int i = 0; i < childrenSize; i++) {
                TreeNode treeNode = children.get(i);
                XMLFileNode xmlFileNode = nodeMap.get(treeNode);
                if (xmlFileNode != null) {
                    if (order < xmlFileNode.getOrder()) {
                        index = children.indexOf(treeNode);
                        break;
                    }
                }
            }
            if (index == childrenSize) {
                children.add(temp);
            } else {
                children.add(index, temp);
            }
            nodeMap.put(temp, node);
        } else {
            String[] nods = currentPath.split("/"); //$NON-NLS-1$
            String[] newNods = parentPath.split("/"); //$NON-NLS-1$
            int parentLevel = 0;
            int checkLength = nods.length < newNods.length ? nods.length : newNods.length;
            for (int i = 1; i < checkLength; i++) {
                if (nods[i].equals(newNods[i])) {
                    parentLevel = i;
                }
            }
            OutputTreeNode parent = current;
            for (int i = 0; i < nods.length - (parentLevel + 1); i++) {
                OutputTreeNode tmpParent = (OutputTreeNode) parent.eContainer();
                if (tmpParent == null) {
                    break;
                }
                parent = tmpParent;
            }

            if (parent != null) {

                EList<TreeNode> children = parent.getChildren();
                int childrenSize = children.size();

                int index = childrenSize - 1;
                int order = node.getOrder();
                for (int i = 0; i < childrenSize; i++) {
                    TreeNode treeNode = children.get(i);
                    XMLFileNode xmlFileNode = nodeMap.get(treeNode);
                    if (xmlFileNode != null) {
                        if (order < xmlFileNode.getOrder()) {
                            index = children.indexOf(treeNode);
                            break;
                        }
                    }
                }

                if (index == childrenSize - 1) {
                    children.add(temp);
                } else {
                    children.add(index, temp);
                }
                nodeMap.put(temp, node);
            }
        }

        return temp;
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            Object object = getSelectedObjects().get(0);
            if (object instanceof TreeNodeEditPart) {
                TreeNodeEditPart parentPart = (TreeNodeEditPart) object;
                parentNode = (OutputTreeNode) parentPart.getModel();
                if (parentNode.eContainer() instanceof OutputXmlTree && XmlMapUtil.DOCUMENT.equals(parentNode.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

}
