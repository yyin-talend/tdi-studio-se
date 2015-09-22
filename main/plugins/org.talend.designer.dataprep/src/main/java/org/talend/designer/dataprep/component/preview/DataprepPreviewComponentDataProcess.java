package org.talend.designer.dataprep.component.preview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.component.preview.shadow.JavaRowNode;
import org.talend.designer.component.preview.shadow.PreviewComponentDataProcess;
import org.talend.designer.component.preview.shadow.PreviewDataContextManager;
import org.talend.designer.component.preview.shadow.ShadowConnection;
import org.talend.designer.component.preview.shadow.SocketOutputNode;
import org.talend.designer.component.preview.shadow.SpecialShadowConnection;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * 
 * @author mzhao
 * 
 * view data process for configuration wizard (tGenKey, tMatchGroup etc.).
 */
public class DataprepPreviewComponentDataProcess extends PreviewComponentDataProcess {

    // a queue to save nodes which need to be back Trace
    private NodeQueue<AbstractNode> needToBackTraceNodeQueue = new NodeQueue<AbstractNode>();

    // a map to save the mapping between real node and Fake node the key is String because of old node is copy from real
    // node So a same node maybe have some differnt instance exist.
    private Map<String, AbstractNode> nodeAndFakeNodeMapping = new HashMap<String, AbstractNode>();

    public DataprepPreviewComponentDataProcess(DataNode node, IContextManager manager) {
        super(node, manager);
        needToBackTraceNodeQueue.put(node);
    }

    public DataprepPreviewComponentDataProcess(Property property, AbstractNode node, IContextManager manager) {
        super(property, node, manager);
        needToBackTraceNodeQueue.put(node);
    }

    public DataprepPreviewComponentDataProcess(Property property, AbstractNode node, IProcess process) {
        this(property, node, process.getContextManager());
        this.setElementParameters(process.getElementParameters());
    }

    @Override
    protected void setFlags(AbstractNode component) {
        super.setFlags(component);
        component.setStart(false);
        component.setSubProcessStart(false);
    }

    @Override
    protected void initNodesAndConnections(AbstractNode inputNode, IContextManager manager) {
        this.node = inputNode;

        if (manager == null) {
            contextManager = new PreviewDataContextManager();
        } else {
            contextManager = manager;
        }
    }

    public void initNodesAndConnections(int lmt) {
        createFakeNodes();
        // tlogrow will blocking the generate code process becasue of can not find a device to print.
        filterTLogRowNode();
        addCurrentAndSocketNode(lmt);
    }

    /**
     * DOC zshen Comment method "addCurrentAndSocketNode".
     */
    private void addCurrentAndSocketNode(int lmt) {
        AbstractNode previousNode = (AbstractNode) nodes.get(0);
        AbstractNode currentNode = node; // noOutputVisualNode;
        if (currentNode != null) {
            String MetadataTableName = currentNode.getIncomingConnections().get(0).getMetaName();
            // MOD yyi 2011-10-13 add java node before the current node for limit
            MetadataTable sourceTablePrv = (MetadataTable) previousNode.getMetadataTable(MetadataTableName);
            MetadataTable targetTablePrv = new MetadataTable();
            MetadataToolHelper.copyTable(sourceTablePrv, targetTablePrv);
            targetTablePrv.setAttachedConnector(EConnectionType.FLOW_MAIN.getName());
            targetTablePrv.setTableName(sourceTablePrv.getTableName());

            MetadataTable sourceTableCur = (MetadataTable) previousNode.getMetadataTable(MetadataTableName);
            MetadataTable targetTableCur = new MetadataTable();
            MetadataToolHelper.copyTable(sourceTableCur, targetTableCur);
            targetTableCur.setAttachedConnector(sourceTableCur.getAttachedConnector());
            targetTableCur.setTableName(sourceTableCur.getTableName());

            // previous node->javanode
            jrn = new JavaRowNode(lmt, targetTablePrv);
            IConnection currentConnection = getCurrentConnection(currentNode, previousNode);
            ShadowConnection cnxPrv2Java = new SpecialShadowConnection(previousNode, jrn, currentConnection.getName(),
                    currentConnection.getLineStyle());
            if (previousNode.getOutgoingConnections().size() > 0) {
                cnxPrv2Java.setConnectorName(currentConnection.getConnectorName());
            } else {
                cnxPrv2Java.setConnectorName(sourceTablePrv.getAttachedConnector());
            }
            cnxPrv2Java.setMetaName(currentConnection.getMetaName());
            List<IConnection> cnxs = new ArrayList<IConnection>();
            cnxs.add(cnxPrv2Java);
            previousNode.getOutgoingConnections().remove(getCurrentConnection(currentNode, previousNode));

            cnxs.addAll(previousNode.getOutgoingConnections());
            previousNode.setOutgoingConnections(cnxs);
            cnxs = new ArrayList<IConnection>();
            cnxs.add(cnxPrv2Java);
            // replaceShadowConnection(previousNode, cnxPrv2Java, true);
            // replaceShadowConnection(jrn, cnxPrv2Java, false);
            jrn.setIncomingConnections(cnxs);

            nodes.add(jrn);

            // java node->current node
            /*
             * ShadowConnection cnxJava2Cur = new ShadowConnection(jrn, currentNode);
             * cnxJava2Cur.setConnectorName(EConnectionType.FLOW_MAIN.getName());
             * cnxJava2Cur.setMetaName(jrn.getUniqueName()); cnxs = new ArrayList<IConnection>(); cnxs.add(cnxJava2Cur);
             * jrn.setOutgoingConnections(cnxs); currentNode.setIncomingConnections(cnxs); nodes.add(currentNode);
             */
            // current node ->log node
            logRowNode = new SocketOutputNode(targetTableCur);
            ShadowConnection cnxCur2Log = new ShadowConnection(jrn, logRowNode);
            cnxCur2Log.setConnectorName(EConnectionType.FLOW_MAIN.getName());
            cnxCur2Log.setMetaName(jrn.getUniqueName());
            cnxs = new ArrayList<IConnection>();
            cnxs.add(cnxCur2Log);
            jrn.setOutgoingConnections(cnxs);
            logRowNode.setIncomingConnections(cnxs);
            logRowNode.setActivate(true);
            nodes.add(logRowNode);

            // set process
            String jvmEncoding = System.getProperty("file.encoding"); //$NON-NLS-1$
            if (jvmEncoding != null) {
                logRowNode.addEncodingParameters(jvmEncoding);
            } else {
                // Using UTF-8 as a default encoding.
                logRowNode.addEncodingParameters("UTF-8"); //$NON-NLS-1$
            }
            jrn.setProcess(this);
            // currentNode.setProcess(this);
            logRowNode.setProcess(this);

        }

    }

    /**
     * Filter out the tlogRow node from the virtual job. (Fix a problem of match wizard when printing on console)
     */
    private void filterTLogRowNode() {
        List<INode> nodeList = new ArrayList<INode>();
        nodeList.addAll(nodes);
        for (INode theNode : nodeList) {
            if (theNode.getComponent().getName().equals("tLogRow")) { //$NON-NLS-1$
                IConnection iConnection = theNode.getIncomingConnections().get(0);
                if (theNode.getOutgoingConnections().size() == 0) {
                    // the node is leaf node case we remove it.
                    INode preNode = iConnection.getSource();
                    preNode.getOutgoingConnections().remove(iConnection);
                } else {
                    // the node is previous node of tmatchGroup. Remove the tLogRow node and re-establish the
                    // connection.
                    AbstractNode preNode = (AbstractNode) iConnection.getSource();
                    IConnection oConnection = theNode.getOutgoingConnections().get(0);
                    AbstractNode NextNode = (AbstractNode) oConnection.getTarget();
                    // Set the connection
                    ShadowConnection cnx = new SpecialShadowConnection(preNode, NextNode, iConnection.getName(),
                            iConnection.getLineStyle());
                    cnx.setElementParameters(iConnection.getElementParameters());
                    cnx.setConnectorName(iConnection.getConnectorName());
                    cnx.setMetaName(iConnection.getMetaName());
                    NextNode.getIncomingConnections().remove(oConnection);
                    replaceShadowConnection(preNode, cnx, true);
                    replaceShadowConnection(NextNode, cnx, false);
                }
                nodes.remove(theNode);
                continue;
            }
            // TDQ-9711,after remove the tLogRow node,if the last node which must connect a node(like as
            // tSortRow),remove it.so that avoid compile error.
            if (theNode.getOutgoingConnections().size() == 0) {
                INodeConnector connectorFromType = theNode.getConnectorFromName("FLOW"); //$NON-NLS-1$
                if (connectorFromType != null && connectorFromType.getMinLinkOutput() > 0) {
                    IConnection iConnection = theNode.getIncomingConnections().get(0);
                    INode preNode = iConnection.getSource();
                    preNode.getOutgoingConnections().remove(iConnection);
                    nodes.remove(theNode);
                }
            }
        }
    }

    /**
     * Create the fake nodes (except current node in selection).
     * 
     */
    private void createFakeNodes() {
        // Back trace the node along with all of lines, create the fake node and the connections.
        checkAndAddAllLeavesNodes();
        while (!needToBackTraceNodeQueue.empty()) {
            backTraceAlongwithIncomingLine(traceNodes, needToBackTraceNodeQueue.get(), this);
            nodes.addAll(traceNodes);
        }

    }

    /**
     * DOC zshen Comment method "getCurrentConnectionName".
     * 
     * @param currentNode
     * @param previousNode
     * @return
     */
    private IConnection getCurrentConnection(AbstractNode currentNode, AbstractNode previousNode) {
        List<? extends IConnection> incomingConnections = currentNode.getIncomingConnections();
        for (IConnection inComingConn : incomingConnections) {
            INode source = inComingConn.getSource();
            if (source != null && source.getElementName().equalsIgnoreCase(previousNode.getElementName())) {
                source.removeInput(inComingConn);
                currentNode.removeOutput(inComingConn);
                return inComingConn;
            }
        }
        if (incomingConnections.size() > 0) {
            return currentNode.getIncomingConnections().get(0);
        } else {
            return null;
        }
    }

    /**
     * Get all of leaveNode from the Process the leaveNode should be Satisfy the condition as below: 1.the node should
     * be Active. 2.the node should not be behind of current selection one.
     */
    private void checkAndAddAllLeavesNodes() {
        List<? extends INode> dependencyNode = this.node.getProcess().getGraphicalNodes();
        for (INode currentNode : dependencyNode) {
            if (!currentNode.isActivate()) {
                continue;
            }
            if (currentNode.getOutgoingConnections().size() == 0) {
                if (isAfterUISelectedNode(currentNode)) {
                    continue;
                }
                AbstractNode convertAbstractNode = convertAbstractNode(currentNode);
                if (convertAbstractNode != null && !needToBackTraceNodeQueue.isContain(convertAbstractNode)) {
                    needToBackTraceNodeQueue.put(convertAbstractNode);
                }
            }
        }

    }

    /**
     * 
     * Check if the current node is linked after tMatchGroup/tGenKey component.
     * 
     * @param currentNode
     * @return true if linked after tMatchgroup/tGenKey component.
     */
    private boolean isAfterUISelectedNode(INode currentNode) {
        if (currentNode == null) {
            return false;
        }
        if (currentNode.getElementName().equalsIgnoreCase(this.node.getElementName())) {
            return true;
        }
        for (IConnection inputConn : currentNode.getIncomingConnections()) {
            INode source = inputConn.getSource();
            if (isAfterUISelectedNode(source)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * back trace each node by the main line (incoming line).
     * 
     * @param incomingConnections Should be real connection, So can get real node from the source.
     * @return
     */
    private void backTraceAlongwithIncomingLine(List<INode> traceNodeList, AbstractNode currFakeNode, IProcess proc) {

        // Find the previous node.
        List<? extends IConnection> incomingConnections = currFakeNode.getIncomingConnections();
        if (incomingConnections == null || incomingConnections.size() == 0 || incomingConnections.get(0).getSource() == null) {
            // the case for some node which is along then filter it
            if (currFakeNode.getOutgoingConnections() == null || currFakeNode.getOutgoingConnections().size() == 0) {
                return;
            }
            if (!hasBeenConsider(currFakeNode.getElementName())) {
                currFakeNode.setStart(Boolean.TRUE);
                currFakeNode.setSubProcessStart(Boolean.TRUE);
                activeAndAddNode(traceNodeList, currFakeNode);

            }
            return;
        }
        List<IConnection> loopIncomingConnections = new ArrayList<IConnection>();
        loopIncomingConnections.addAll(incomingConnections);

        for (IConnection currConn : loopIncomingConnections) {
            if (checkInComingConnecitonWhetherHasBeenCreate(currConn)) {
                continue;
            }
            // remove old InComingConnection
            currFakeNode.removeInput(currConn);
            Node prevNode = (Node) currConn.getSource();
            if (isNeedActiveAndAdd(traceNodeList, currFakeNode)) {
                activeAndAddNode(traceNodeList, currFakeNode);
            }
            AbstractNode prevFakeNode = GetPrevFakeNode(prevNode);

            // Set the connection
            ShadowConnection cnx = new SpecialShadowConnection(prevFakeNode, currFakeNode, currConn.getName(),
                    currConn.getLineStyle());
            cnx.setElementParameters(currConn.getElementParameters());
            cnx.setConnectorName(currConn.getConnectorName());
            cnx.setMetaName(currConn.getMetaName());
            replaceShadowConnection(prevFakeNode, cnx, true);
            replaceShadowConnection(currFakeNode, cnx, false);

            // Get the previous node
            backTraceAlongwithIncomingLine(traceNodeList, prevFakeNode, this);

        }
    }

    /**
     * DOC talend Comment method "isNeedActionAndAdd".
     * 
     * @param traceNodeList
     * @param currFakeNode
     * @return
     */
    private boolean isNeedActiveAndAdd(List<INode> traceNodeList, AbstractNode currFakeNode) {
        return !this.node.getElementName().equals(currFakeNode.getElementName()) && !traceNodeList.contains(currFakeNode);
    }

    /**
     * DOC zshen Comment method "activeAndAddNode".
     * 
     * @param traceNodeList
     * @param currFakeNode
     */
    private void activeAndAddNode(List<INode> traceNodeList, AbstractNode currFakeNode) {
        currFakeNode.setActivate(true);
        traceNodeList.add(currFakeNode);
        if (isContainMoreThanOneOutputConnection(currFakeNode)) {
            nodeAndFakeNodeMapping.put(currFakeNode.getElementName(), currFakeNode);
        }
    }

    /**
     * DOC zshen Comment method "GetPrevFakeNode".
     * 
     * @param prevNode
     * @return
     */
    private AbstractNode GetPrevFakeNode(Node prevNode) {
        AbstractNode prevFakeNode = null;
        if (!hasBeenConsider(prevNode.getElementName())) {
            ViewDataprepDataMain dataMain = new ViewDataprepDataMain(prevNode, this);

            prevFakeNode = dataMain.getDataNode();
            // clearOutConnection(prevFakeNode);
        } else {
            prevFakeNode = nodeAndFakeNodeMapping.get(prevNode.getElementName());
        }
        return prevFakeNode;
    }

    /**
     * DOC zshen Comment method "checkInComingConnecitonWhetherHasBeenCreate".
     * 
     * @param currConn
     * @return
     */
    private boolean checkInComingConnecitonWhetherHasBeenCreate(IConnection currConn) {
        if (currConn instanceof ShadowConnection) {
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isBeenConsider".
     * 
     * Judge whether the prevNode has been create before that
     * 
     * @param prevNode
     * @return
     */
    private boolean hasBeenConsider(String nodeName) {
        if (nodeName == null) {
            return false;
        }
        for (String keyNodeName : nodeAndFakeNodeMapping.keySet()) {
            if (keyNodeName.equalsIgnoreCase(nodeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC zshen Comment method "getAbstractNode". return an AbstractNode If parameter is Node the fake one.
     * 
     * @param currentNode
     */
    private AbstractNode convertAbstractNode(INode currentNode) {
        if (currentNode instanceof Node) {
            ViewDataprepDataMain dataMain = new ViewDataprepDataMain((Node) currentNode, this);
            return dataMain.getDataNode();
        } else if (currentNode instanceof AbstractNode) {
            return (AbstractNode) currentNode;
        }
        return null;

    }

    /**
     * DOC zshen Comment method "isContainMoreThanOneOutputConnection".
     * 
     * Judge whether the output Connection is more than one.
     * 
     * @param prevNode
     * @return
     */
    private boolean isContainMoreThanOneOutputConnection(AbstractNode prevNode) {
        List<? extends IConnection> outgoingConnections = prevNode.getOutgoingConnections();
        return outgoingConnections.size() > 1;
    }

    /**
     * remove old one and add new shadowConnection
     * 
     * @param currFakeNode
     * @param cnxs
     */
    private void replaceShadowConnection(AbstractNode currFakeNode, IConnection cnx, boolean isOutput) {
        if (cnx == null) {
            return;
        }
        List<IConnection> connections = new ArrayList<IConnection>();
        if (isOutput) {
            connections = (List<IConnection>) currFakeNode.getOutgoingConnections();
        } else {
            connections = (List<IConnection>) currFakeNode.getIncomingConnections();
        }
        IConnection removeConn = null;
        for (IConnection inConn : connections) {
            if (cnx.getName().equalsIgnoreCase(inConn.getName())) {
                removeConn = inConn;
                break;
            }
        }

        if (removeConn != null) {
            connections.remove(removeConn);
        }
        connections.add(cnx);
    }

    private class NodeQueue<E> {

        private LinkedList<E> ll = new LinkedList<E>();

        public void put(E o) {
            ll.addLast(o);
        }

        public E get() {
            return ll.removeFirst();
        }

        public boolean empty() {
            return ll.isEmpty();
        }

        public boolean isContain(E o) {
            return ll.indexOf(o) != -1;
        }
    }

}
