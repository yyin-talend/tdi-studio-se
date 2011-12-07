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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.commons.xml.XmlUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.ui.IMDMProviderService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.dialog.RepositoryXmlSelectionDialog;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Attribute;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.Element;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.FOXTreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.treeNode.NameSpaceNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.StringUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.util.TreeUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ImportTreeFromRepository extends SelectionAction {

    private MapperManager mapperManager;

    private TreeNode parentNode;

    private Shell shell;

    private Map<TreeNode, XMLFileNode> nodeMap = new HashMap<TreeNode, XMLFileNode>();

    private boolean input;

    private List schemaTargets;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.ImportTreeFromRepository";

    /**
     * DOC talend ImportTreeFromRepository constructor comment.
     * 
     * @param part
     */
    public ImportTreeFromRepository(IWorkbenchPart part, Shell shell) {
        super(part);
        this.shell = shell;
        setId(ID);
        setText("Import From Repository");
    }

    @Override
    public void run() {
        RepositoryXmlSelectionDialog reviewDialog = new RepositoryXmlSelectionDialog(shell, new String[] { "XML", "MDM" });
        if (reviewDialog.open() == Window.OK) {
            TreeNode treeNodeRoot = XmlMapUtil.getTreeNodeRoot(parentNode);

            XmlMapUtil.detachNodeConnections(treeNodeRoot, mapperManager.getCopyOfMapData(), true);
            parentNode.getChildren().clear();
            RepositoryNode repositoryNode = reviewDialog.getResult();

            Item item = repositoryNode.getObject().getProperty().getItem();
            try {
                if (item instanceof XmlFileConnectionItem) {
                    XmlFileConnectionItem xmlitem = (XmlFileConnectionItem) item;
                    XmlFileConnection connection = (XmlFileConnection) xmlitem.getConnection();
                    prepareEmfTreeFromXml(connection);
                    nodeMap.clear();
                } else if (item instanceof MDMConnectionItem) {
                    String selectedSchema = (String) repositoryNode.getProperties(EProperties.LABEL);
                    MDMConnection connection = (MDMConnection) ((MDMConnectionItem) item).getConnection();
                    prepareEmfTreeFromMdm(connection, selectedSchema);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                if (parentNode.getChildren().isEmpty()) {
                    TreeNode rootNode = createModel();
                    rootNode.setName("root");
                    rootNode.setNodeType(NodeType.ELEMENT);
                    rootNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                    rootNode.setXpath(XmlMapUtil.getXPath(parentNode.getXpath(), "root", NodeType.ELEMENT));
                    parentNode.getChildren().add(rootNode);
                    showError();
                }
            }

            AbstractInOutTree tree = null;
            if (parentNode.eContainer() instanceof InputXmlTree) {
                mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) parentNode.eContainer());
                tree = (InputXmlTree) parentNode.eContainer();
            } else if (parentNode.eContainer() instanceof OutputXmlTree) {
                mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) parentNode.eContainer());
                tree = (OutputXmlTree) parentNode.eContainer();
            }
            if (tree != null) {
                mapperManager.getProblemsAnalyser().checkLoopProblems(tree);
                mapperManager.getMapperUI().updateStatusBar();
            }
        }
    }

    private void prepareEmfTreeFromXml(XmlFileConnection connection) {
        if (!connection.isInputModel()) {
            prepareModelFromOutput(connection.getRoot(), connection.getLoop(), connection.getGroup());
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

            this.schemaTargets = schemaTargets;
            String file = connection.getXmlFilePath();
            List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
            File xmlFile = new File(file);
            if (xmlFile.exists()) {
                list = TreeUtil.getFoxTreeNodesForXmlMap(xmlFile.getAbsolutePath(), absoluteXPathQuery);
            } else if (connection.getFileContent() != null && connection.getFileContent().length > 0) {
                String xsdFile = initFileContent(connection);
                if (xsdFile != null && new File(xsdFile).exists()) {
                    list = TreeUtil.getFoxTreeNodesForXmlMap(xsdFile, absoluteXPathQuery);
                }
            }
            prepareEmfTree(list, parentNode, null, absoluteXPathQuery);
        }

    }

    private void prepareEmfTree(List<FOXTreeNode> list, TreeNode parent, String xmlPath, String absoluteXPathQuery) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String xPath = parent.getXpath();
        for (FOXTreeNode foxNode : list) {
            TreeNode createTreeNode = createModel();
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
                if (foxNode instanceof Attribute) {
                    tempXpath = XmlMapUtil.XPATH_SEPARATOR + XmlMapUtil.XPATH_ATTRIBUTE + foxNode.getLabel();
                } else {
                    tempXpath = XmlMapUtil.XPATH_SEPARATOR + foxNode.getLabel();
                }
            } else {
                if (foxNode instanceof Attribute) {
                    tempXpath = xmlPath + XmlMapUtil.XPATH_SEPARATOR + XmlMapUtil.XPATH_ATTRIBUTE + foxNode.getLabel();
                } else {
                    tempXpath = xmlPath + XmlMapUtil.XPATH_SEPARATOR + foxNode.getLabel();
                }
            }

            if (tempXpath.equals(absoluteXPathQuery)) {
                if (!(parent.eContainer() instanceof AbstractInOutTree)) {
                    createTreeNode.setLoop(true);
                }
            } else if (!isMappedChild(tempXpath, absoluteXPathQuery)) {
                continue;
            }

            parent.getChildren().add(createTreeNode);
            if (createTreeNode.isLoop()) {
                XmlMapUtil.upsetMainNode(createTreeNode);
            }
            if (foxNode.getChildren() != null && !foxNode.getChildren().isEmpty()) {
                prepareEmfTree(foxNode.getChildren(), createTreeNode, tempXpath, absoluteXPathQuery);
            }
        }

    }

    private void prepareEmfTreeFromMdm(MDMConnection connection, String selectedConcept) {
        if (connection == null || selectedConcept == null) {
            return;
        }
        EList<Concept> schemas = connection.getSchemas();
        Concept selected = null;
        for (int i = 0; i < schemas.size(); i++) {
            Concept concept = schemas.get(i);
            if (selectedConcept.equals(concept.getLabel())) {
                selected = concept;
                break;
            }
        }

        if (selected != null) {
            if (!MdmConceptType.OUTPUT.equals(selected.getConceptType())) {
                IMDMProviderService service = (IMDMProviderService) GlobalServiceRegister.getDefault().getService(
                        IMDMProviderService.class);
                if (service != null) {
                    boolean initConcepts = service.initConcepts(connection);
                    if (initConcepts) {
                        String loopExpression = selected.getLoopExpression();
                        EList<ConceptTarget> conceptTargets = selected.getConceptTargets();
                        if (conceptTargets == null || loopExpression == null) {
                            return;
                        }
                        this.schemaTargets = conceptTargets;
                        List<FOXTreeNode> list = TreeUtil.getFoxTreeNodesForXmlMap(getTempTemplateXSDFile().getAbsolutePath(),
                                loopExpression);
                        prepareEmfTree(list, parentNode, null, loopExpression);
                    }
                }
            } else {
                prepareModelFromOutput(selected.getRoot(), selected.getLoop(), selected.getGroup());
            }
        }
    }

    private boolean isMappedChild(String tempXpath, String absoluteXPathQuery) {
        for (Object obj : schemaTargets) {
            String relativeXPathQuery = "";
            if (obj instanceof SchemaTarget) {
                relativeXPathQuery = ((SchemaTarget) obj).getRelativeXPathQuery();
            } else if (obj instanceof ConceptTarget) {
                relativeXPathQuery = ((ConceptTarget) obj).getRelativeLoopExpression();
            }
            final String toAbsolute = absoluteXPathQuery + XmlMapUtil.XPATH_SEPARATOR + relativeXPathQuery;
            if (toAbsolute.startsWith(tempXpath)) {
                return true;
            }
        }

        return false;
    }

    /*
     * same as XmlFileOutputStep2Form.initXmlTreeData()
     */
    private String initFileContent(XmlFileConnection connection) {
        byte[] bytes = connection.getFileContent();
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project.getTechnicalLabel());
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return null;
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp";
        String fileName = "";

        String pathStr = connection.getXmlFilePath();
        if (connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection, true);
            pathStr = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
        }
        if (pathStr != null && XmlUtil.isXMLFile(pathStr)) {
            fileName = StringUtil.TMP_XML_FILE;
        } else if (pathStr != null && XmlUtil.isXSDFile(pathStr)) {
            fileName = StringUtil.TMP_XSD_FILE;
        }
        File temfile = new File(temPath + File.separator + fileName);
        if (!temfile.exists()) {
            try {
                temfile.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }

        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(temfile);
            outStream.write(bytes);
            outStream.close();
        } catch (FileNotFoundException e1) {
            ExceptionHandler.process(e1);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        return temfile.getPath();

    }

    private File getTempTemplateXSDFile() {
        IPath tempPath = new Path(System.getProperty("user.dir")).append("temp"); //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-2$
        File tempFile = tempPath.toFile();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(tempFile, "template.xsd"); //$NON-NLS-1$
        return file;
    }

    private void prepareModelFromOutput(List<XMLFileNode> root, List<XMLFileNode> loop, List<XMLFileNode> group) {
        TreeNode rootNode = null;
        TreeNode lastTreeNode = parentNode;

        TreeNode temp = null;
        TreeNode mainNode = parentNode;
        String mainPath = null;
        String lastXmlPath = null;

        // build root
        for (int i = 0; i < root.size(); i++) {
            XMLFileNode node = (XMLFileNode) root.get(i);
            String newPath = node.getXMLPath();
            String type = node.getType();
            if (node.getAttribute().equals("attri")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.ATTRIBUT);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
                lastTreeNode.getChildren().add(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.NAME_SPACE);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));

                lastTreeNode.getChildren().add(temp);
            } else {
                temp = this.addElement(lastTreeNode, lastXmlPath, node);
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                lastTreeNode = temp;
                lastXmlPath = newPath;
            }
            if (type == null) {
                type = JavaTypesManager.getDefaultJavaType().getId();
            }
            temp.setType(type);

        }

        // build group tree
        lastTreeNode = mainNode;
        lastXmlPath = mainPath;
        List<TreeNode> groupElements = new ArrayList<TreeNode>();
        boolean isFirst = true;
        for (int i = 0; i < group.size(); i++) {
            XMLFileNode node = (XMLFileNode) group.get(i);
            String newPath = node.getXMLPath();
            String type = node.getType();

            if (node.getAttribute().equals("attri")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.ATTRIBUT);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
                lastTreeNode.getChildren().add(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.NAME_SPACE);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
                lastTreeNode.getChildren().add(temp);
            } else {
                temp = this.addElement(lastTreeNode, lastXmlPath, node);
                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setGroup(true);
                    isFirst = false;
                }
                lastTreeNode = temp;
                lastXmlPath = newPath;
                groupElements.add(temp);
            }
            if (type == null) {
                type = JavaTypesManager.getDefaultJavaType().getId();
            }
            temp.setType(type);

        }

        // build loop tree
        lastTreeNode = mainNode;
        lastXmlPath = mainPath;
        isFirst = true;
        TreeNode loopElement = null;
        for (int i = 0; i < loop.size(); i++) {
            XMLFileNode node = (XMLFileNode) loop.get(i);
            String newPath = node.getXMLPath();
            String type = node.getType();

            if (node.getAttribute().equals("attri")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.ATTRIBUT);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
                lastTreeNode.getChildren().add(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = createModel();
                temp.setName(newPath);
                temp.setDefaultValue(node.getDefaultValue());
                temp.setNodeType(NodeType.NAME_SPACE);
                temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
                lastTreeNode.getChildren().add(temp);
            } else {
                temp = this.addElement(lastTreeNode, lastXmlPath, node);
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setLoop(true);
                    loopElement = temp;
                    isFirst = false;
                }
                lastTreeNode = temp;
                lastXmlPath = newPath;
            }
            if (type == null) {
                type = JavaTypesManager.getDefaultJavaType().getId();
            }
            temp.setType(type);

        }

        if (loopElement != null && !groupElements.isEmpty()) {
            fillGroup(loopElement, groupElements);
        }

        if (rootNode != null) {
            parentNode.getChildren().add(rootNode);
        }

    }

    private void fillGroup(TreeNode loopElement, List<TreeNode> groupElements) {
        final EObject parent = loopElement.eContainer();
        if (parent != null) {
            if (groupElements.contains(parent)) {
                ((TreeNode) parent).setGroup(true);
            }
            if (parent.eContainer() != null && !(parent.eContainer().eContainer() instanceof AbstractInOutTree)) {
                fillGroup((TreeNode) parent, groupElements);
            }
        }

    }

    protected TreeNode addElement(TreeNode lastTreeNode, String lastXmlPath, XMLFileNode xmlFilenode) {
        String newPath = xmlFilenode.getXMLPath();
        String defaultValue = xmlFilenode.getDefaultValue();
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        TreeNode temp = createModel();
        temp.setName(name);
        temp.setDefaultValue(defaultValue);
        temp.setNodeType(NodeType.ELEMENT);
        if (lastTreeNode == parentNode) { // root node of a document
            temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
            return temp;
        }

        // in the same branch as lastTreeNode
        if (lastXmlPath.equals(parentPath)) {
            temp.setXpath(XmlMapUtil.getXPath(lastTreeNode.getXpath(), temp.getName(), temp.getNodeType()));
            EList<TreeNode> children = lastTreeNode.getChildren();
            int childrenSize = children.size();
            /*
             * keep the same order as in repository
             */
            int index = childrenSize;
            int order = xmlFilenode.getOrder();
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
            nodeMap.put(temp, xmlFilenode);
        }
        // in different branches as lastTreeNode
        else {
            String[] nods = lastXmlPath.split("/"); //$NON-NLS-1$
            String[] newNods = parentPath.split("/"); //$NON-NLS-1$
            int parentLevel = 0;
            int checkLength = nods.length < newNods.length ? nods.length : newNods.length;
            for (int i = 1; i < checkLength; i++) {
                if (nods[i].equals(newNods[i])) {
                    parentLevel = i;
                }
            }
            TreeNode parent = lastTreeNode;
            for (int i = 0; i < nods.length - (parentLevel + 1); i++) {
                TreeNode tmpParent = (TreeNode) parent.eContainer();
                if (tmpParent == null) {
                    break;
                }
                parent = tmpParent;
            }

            if (parent != null) {
                temp.setXpath(XmlMapUtil.getXPath(parent.getXpath(), temp.getName(), temp.getNodeType()));
                EList<TreeNode> children = parent.getChildren();
                int childrenSize = children.size();

                int index = childrenSize - 1;
                int order = xmlFilenode.getOrder();
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
                nodeMap.put(temp, xmlFilenode);
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
                parentNode = (TreeNode) parentPart.getModel();
                if (parentNode.eContainer() instanceof AbstractInOutTree && XmlMapUtil.DOCUMENT.equals(parentNode.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    private void showError() {
        MessageDialog.openError(null, "Error", "Import from repository fail, please check your repository connection!");
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    private TreeNode createModel() {
        if (input) {
            return XmlmapFactory.eINSTANCE.createTreeNode();
        } else {
            return XmlmapFactory.eINSTANCE.createOutputTreeNode();
        }
    }

}
