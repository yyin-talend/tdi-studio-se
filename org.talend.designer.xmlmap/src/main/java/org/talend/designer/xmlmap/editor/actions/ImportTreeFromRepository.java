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
import org.talend.core.model.utils.TalendTextUtils;
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

    private TreeNode schemaNode;

    private Shell shell;

    private Map<TreeNode, XMLFileNode> nodeMap = new HashMap<TreeNode, XMLFileNode>();

    private boolean input;

    private List schemaTargets;

    private String absoluteXPathQuery = "";

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.ImportTreeFromRepository";

    private Map<String, TreeNode> xpathAndSubs = new HashMap<String, TreeNode>();

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
            TreeNode treeNodeRoot = XmlMapUtil.getTreeNodeRoot(schemaNode);

            XmlMapUtil.detachNodeConnections(treeNodeRoot, mapperManager.getCopyOfMapData(), true);
            schemaNode.getChildren().clear();
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
                if (schemaNode.getChildren().isEmpty()) {
                    TreeNode rootNode = createModel();
                    rootNode.setName("root");
                    rootNode.setNodeType(NodeType.ELEMENT);
                    rootNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                    rootNode.setXpath(XmlMapUtil.getXPath(schemaNode.getXpath(), "root", NodeType.ELEMENT));
                    schemaNode.getChildren().add(rootNode);
                    showError();
                }
            }

            AbstractInOutTree tree = null;
            if (schemaNode.eContainer() instanceof InputXmlTree) {
                mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) schemaNode.eContainer());
                tree = (InputXmlTree) schemaNode.eContainer();
            } else if (schemaNode.eContainer() instanceof OutputXmlTree) {
                mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) schemaNode.eContainer());
                tree = (OutputXmlTree) schemaNode.eContainer();
            }
            if (tree != null) {
                mapperManager.getProblemsAnalyser().checkLoopProblems(tree);
                mapperManager.getMapperUI().updateStatusBar();
            }
        }
    }

    private void prepareEmfTreeFromXml(XmlFileConnection connection) {
        if (!connection.isInputModel()) {
            String filePath = connection.getXmlFilePath();
            if (filePath != null && filePath.toLowerCase().endsWith(".xsd")) {
                File xsdFile = new File(filePath);
                if (!xsdFile.exists()) {
                    filePath = initFileContent(connection);
                }
            }
            File xsdFile = null;
            if (filePath != null) {
                xsdFile = new File(filePath);
            }
            prepareModelFromOutput(connection.getRoot(), connection.getLoop(), connection.getGroup(), xsdFile);
        } else {
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
            prepareEmfTree(list, schemaNode);
        }

    }

    private void prepareEmfTree(List<FOXTreeNode> list, TreeNode parent) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String xPath = parent.getXpath();
        TreeNode realParentNode = parent;
        if (parent.isSubstitution() || parent.isChoice()) {
            realParentNode = XmlMapUtil.getRealParentNode(parent);
            if (realParentNode != null) {
                xPath = realParentNode.getXpath();
            }
        }
        for (FOXTreeNode foxNode : list) {
            TreeNode createTreeNode = createModel();
            String label = foxNode.getLabel();
            createTreeNode.setName(label);
            if (foxNode instanceof Element) {
                createTreeNode.setNodeType(NodeType.ELEMENT);
                if (foxNode.isChoice()) {
                    createTreeNode.setChoice(foxNode.isChoice());
                } else if (foxNode.isSubstitution()) {
                    createTreeNode.setSubstitution(foxNode.isSubstitution());
                }
            } else if (foxNode instanceof Attribute) {
                createTreeNode.setNodeType(NodeType.ATTRIBUT);
            } else if (foxNode instanceof NameSpaceNode) {
                createTreeNode.setNodeType(NodeType.NAME_SPACE);
                createTreeNode.setDefaultValue(foxNode.getDefaultValue());
                if (createTreeNode.getName() == null || createTreeNode.getName().equals("")) {
                    createTreeNode.setName(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX);
                }
            }
            createTreeNode.setXpath(XmlMapUtil.getXPath(xPath, label, createTreeNode.getNodeType()));
            if (foxNode.getDataType() != null && "".equals(foxNode.getDataType())) {
                createTreeNode.setType(foxNode.getDataType());
            } else {
                createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
            }

            // tempXpath is current xpath remove schema node xpath like: row1:newColumn1
            String tempXpath = createTreeNode.getXpath().substring(schemaNode.getXpath().length() + 1);
            if (createTreeNode.isChoice() || createTreeNode.isSubstitution()) {
                if (!isMappedChoiceSubs(foxNode, xPath)) {
                    continue;
                }

            } else if (tempXpath.equals(absoluteXPathQuery)) {
                createTreeNode.setLoop(true);
            } else if (!isMappedChild(tempXpath)) {
                continue;
            }

            parent.getChildren().add(createTreeNode);
            if (createTreeNode.isLoop()) {
                XmlMapUtil.upsetMainNode(createTreeNode);
            }
            if (foxNode.getChildren() != null && !foxNode.getChildren().isEmpty()) {
                prepareEmfTree(foxNode.getChildren(), createTreeNode);
            }
        }

    }

    private boolean isMappedChoiceSubs(FOXTreeNode choiceSubs, String parentXpath) {
        // if any child of the choice or subs is mapped to target schema , it should be created in xmlmap
        for (FOXTreeNode child : choiceSubs.getChildren()) {
            NodeType nodeType = null;
            if (child instanceof Element) {
                nodeType = NodeType.ELEMENT;
            } else if (child instanceof Attribute) {
                nodeType = NodeType.ATTRIBUT;
            } else if (child instanceof NameSpaceNode) {
                nodeType = NodeType.NAME_SPACE;
            }
            String tempPath = XmlMapUtil.getXPath(parentXpath, child.getLabel(), nodeType);
            tempPath = tempPath.substring(schemaNode.getXpath().length() + 1);
            if (isMappedChild(tempPath)) {
                return true;
            }
        }
        return false;
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
            IMDMProviderService service = (IMDMProviderService) GlobalServiceRegister.getDefault().getService(
                    IMDMProviderService.class);
            if (service != null) {
                boolean initConcepts = service.initConcepts(connection);
                if (initConcepts) {
                    if (!MdmConceptType.OUTPUT.equals(selected.getConceptType())) {
                        String prefix = service.getXPathPrefixValue(selected);
                        prefix = TalendTextUtils.removeQuotes(prefix);
                        absoluteXPathQuery = selected.getLoopExpression();
                        EList<ConceptTarget> conceptTargets = selected.getConceptTargets();
                        if (conceptTargets == null || absoluteXPathQuery == null) {
                            return;
                        }
                        this.schemaTargets = conceptTargets;
                        List<FOXTreeNode> list = TreeUtil.getFoxTreeNodesForXmlMap(getTempTemplateXSDFile().getAbsolutePath(),
                                absoluteXPathQuery);
                        TreeNode pNode = schemaNode;
                        if (MdmConceptType.RECEIVE.equals(selected.getConceptType())) {
                            if (prefix != null && !"".equals(prefix)) {
                                String[] preValues = prefix.split(XmlMapUtil.XPATH_SEPARATOR);
                                for (String value : preValues) {
                                    if (!"".equals(value)) {
                                        TreeNode createTreeNode = createModel();
                                        createTreeNode.setName(value);
                                        createTreeNode.setNodeType(NodeType.ELEMENT);
                                        createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                        createTreeNode.setXpath(XmlMapUtil.getXPath(pNode.getXpath(), createTreeNode.getName(),
                                                createTreeNode.getNodeType()));
                                        pNode.getChildren().add(createTreeNode);
                                        pNode = createTreeNode;
                                    }
                                }
                                absoluteXPathQuery = prefix + absoluteXPathQuery;
                            }

                        }

                        prepareEmfTree(list, pNode);

                    } else {
                        File file = new File(getTempTemplateXSDFile().getAbsolutePath());
                        prepareModelFromOutput(selected.getRoot(), selected.getLoop(), selected.getGroup(), file);
                    }
                }
            }
        }
    }

    private boolean isMappedChild(String tempXpath) {
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

    private void prepareXpathAndFoxNodeMap(Map<String, FOXTreeNode> xpathAndFoxNodeMap, List<FOXTreeNode> foxTreeNodes,
            String parentXpath) {
        for (int i = 0; i < foxTreeNodes.size(); i++) {
            FOXTreeNode foxNode = foxTreeNodes.get(i);
            String label = foxNode.getLabel();
            NodeType type = null;
            if (foxNode instanceof Element) {
                type = NodeType.ELEMENT;
            } else if (foxNode instanceof Attribute) {
                type = NodeType.ATTRIBUT;
            } else if (foxNode instanceof NameSpaceNode) {
                type = NodeType.NAME_SPACE;
            }
            String xpath = XmlMapUtil.getXPath(parentXpath, label, type);
            xpathAndFoxNodeMap.put(xpath, foxNode);
            String pXpath = xpath;
            if (foxNode.isSubstitution() || foxNode.isChoice()) {
                pXpath = parentXpath;
            }
            if (!foxNode.getChildren().isEmpty()) {
                prepareXpathAndFoxNodeMap(xpathAndFoxNodeMap, foxNode.getChildren(), pXpath);
            }
        }
    }

    private void prepareModelFromOutput(List<XMLFileNode> root, List<XMLFileNode> loop, List<XMLFileNode> group, File xsdFile) {
        xpathAndSubs.clear();
        Map<String, FOXTreeNode> xpathAndFoxNodeMap = new HashMap<String, FOXTreeNode>();
        if (xsdFile != null) {
            if (root.size() > 0) {
                List<FOXTreeNode> foxTreeNodes = TreeUtil.getFoxTreeNodesForXmlMap(xsdFile.getAbsolutePath(), root.get(0)
                        .getXMLPath());
                prepareXpathAndFoxNodeMap(xpathAndFoxNodeMap, foxTreeNodes, schemaNode.getXpath());
            }
        }

        TreeNode rootNode = null;
        TreeNode lastTreeNode = schemaNode;

        TreeNode temp = null;
        TreeNode mainNode = schemaNode;
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
                temp = this.addElement(lastTreeNode, lastXmlPath, node, xpathAndFoxNodeMap);
                if (temp == null) {
                    continue;
                }
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (node.getAttribute().equals("main")) {
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
                temp = this.addElement(lastTreeNode, lastXmlPath, node, xpathAndFoxNodeMap);
                if (temp == null) {
                    continue;
                }
                if (node.getAttribute().equals("main")) {
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
                temp = this.addElement(lastTreeNode, lastXmlPath, node, xpathAndFoxNodeMap);
                if (temp == null) {
                    continue;
                }
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (node.getAttribute().equals("main")) {
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setLoop(true);
                    XmlMapUtil.upsetMainNode(temp);
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
            schemaNode.getChildren().add(rootNode);
        }

    }

    private TreeNode createParentChoiceOrSubs(TreeNode parentNode, TreeNode currentNode,
            Map<String, FOXTreeNode> xpathAndFoxNodeMap) {
        FOXTreeNode treeNode = xpathAndFoxNodeMap.get(currentNode.getXpath());
        if (treeNode != null) {
            FOXTreeNode parent = treeNode.getParent();
            if (parent != null) {
                if (parent.isChoice()) {
                    String xPath = XmlMapUtil.getXPath(parentNode.getXpath(), parent.getLabel(), NodeType.ELEMENT);
                    TreeNode choice = xpathAndSubs.get(xPath);
                    if (choice == null) {
                        choice = createModel();
                        choice.setChoice(true);
                        choice.setName(parent.getLabel());
                        choice.setNodeType(NodeType.ELEMENT);
                        choice.setXpath(xPath);
                        xpathAndSubs.put(xPath, choice);
                    }
                    choice.getChildren().add(currentNode);
                    return choice;
                } else if (parent.isSubstitution()) {
                    FOXTreeNode clone = cloneSubsFOXTreeNode(parent);
                    FOXTreeNode subRoot = getSubsRoot(parent, clone);
                    String xPath = XmlMapUtil.getXPath(parentNode.getXpath(), subRoot.getLabel(), NodeType.ELEMENT);
                    TreeNode subNode = xpathAndSubs.get(xPath);
                    if (subNode == null) {
                        subNode = createModel();
                        subNode.setSubstitution(true);
                        subNode.setName(subRoot.getLabel());
                        subNode.setNodeType(NodeType.ELEMENT);
                        subNode.setXpath(xPath);
                        xpathAndSubs.put(xPath, subNode);
                    }
                    TreeNode lastSubs = createSubChild(subNode, subRoot);
                    lastSubs.getChildren().add(currentNode);
                    return subNode;
                }
            }
            // if no subs or choice created ,return current node
            return currentNode;
        } else {
            // return null for abstract sub nodes , no need to create
            return null;
        }
    }

    private TreeNode createSubChild(TreeNode subParent, FOXTreeNode foxSubParent) {
        if (foxSubParent.getChildren().isEmpty()) {
            return subParent;
        } else {
            FOXTreeNode foxChild = foxSubParent.getChildren().get(0);
            String xPath = XmlMapUtil.getXPath(subParent.getXpath(), foxChild.getLabel(), NodeType.ELEMENT);
            TreeNode subNode = xpathAndSubs.get(xPath);
            if (subNode == null) {
                subNode = createModel();
                subNode.setSubstitution(true);
                subNode.setName(foxChild.getLabel());
                subNode.setNodeType(NodeType.ELEMENT);
                subNode.setXpath(xPath);
                subParent.getChildren().add(subNode);
                xpathAndSubs.put(xPath, subNode);
            }
            return createSubChild(subNode, foxChild);
        }
    }

    /*
     * find up for parent subs ,make a clone to make sure the subs tree only have one branch
     */
    private FOXTreeNode getSubsRoot(FOXTreeNode subNode, FOXTreeNode clone) {
        FOXTreeNode parent = subNode.getParent();
        if (!parent.isSubstitution()) {
            return clone;
        } else {
            FOXTreeNode cloneP = cloneSubsFOXTreeNode(parent);
            clone.setParent(cloneP);
            clone.getChildren().add(clone);
            return getSubsRoot(parent, cloneP);
        }
    }

    private FOXTreeNode cloneSubsFOXTreeNode(FOXTreeNode node) {
        Element element = new Element();
        element.setLabel(node.getLabel());
        element.setSubstitution(node.isSubstitution());
        return element;
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

    protected TreeNode addElement(TreeNode lastTreeNode, String lastXmlPath, XMLFileNode xmlFilenode,
            Map<String, FOXTreeNode> xpathAndFoxNodeMap) {
        String newPath = xmlFilenode.getXMLPath();
        String defaultValue = xmlFilenode.getDefaultValue();
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        TreeNode temp = createModel();
        temp.setName(name);
        temp.setDefaultValue(defaultValue);
        temp.setNodeType(NodeType.ELEMENT);
        if (lastTreeNode == schemaNode) { // root node of a document
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
                TreeNode child = createParentChoiceOrSubs(lastTreeNode, temp, xpathAndFoxNodeMap);
                if (child != null) {
                    children.add(child);
                } else {
                    temp = null;
                }
            } else {
                TreeNode child = createParentChoiceOrSubs(lastTreeNode, temp, xpathAndFoxNodeMap);
                if (child != null) {
                    children.add(index, child);
                } else {
                    temp = null;
                }
            }
            if (temp != null) {
                nodeMap.put(temp, xmlFilenode);
            }
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
                if (tmpParent.isSubstitution() || tmpParent.isChoice()) {
                    parent = tmpParent;
                    i--;
                    continue;
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
                    TreeNode child = createParentChoiceOrSubs(parent, temp, xpathAndFoxNodeMap);
                    if (child != null) {
                        children.add(child);
                    } else {
                        temp = null;
                    }
                } else {
                    TreeNode child = createParentChoiceOrSubs(parent, temp, xpathAndFoxNodeMap);
                    if (child != null) {
                        children.add(index, child);
                    } else {
                        temp = null;
                    }
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
                schemaNode = (TreeNode) parentPart.getModel();
                if (schemaNode.eContainer() instanceof AbstractInOutTree && XmlMapUtil.DOCUMENT.equals(schemaNode.getType())) {
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
