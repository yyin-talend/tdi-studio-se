package org.talend.designer.core.ui.editor.jobletcontainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.ui.IJobletProviderService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.JobletConnectionReconnectCommand;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

public class JobletContainer extends NodeContainer {

    public static final String UPDATE_JOBLET_CONTENT = "UPDATE_JOBLET_CONTENT"; //$NON-NLS-1$

    public static final String UPDATE_JOBLET_DATA = "UPDATE_JOBLET_DATA"; //$NON-NLS-1$

    public static final String UPDATE_JOBLET_CONNECTIONS = "UPDATE_JOBLET_CONNECTIONS"; //$NON-NLS-1$

    public static final String UPDATE_JOBLET_TITLE_COLOR = "UPDATE_JOBLET_TITLE_COLOR"; //$NON-NLS-1$

    public static final String UPDATE_JOBLET_DISPLAY = "UPDATE_JOBLET_DISPLAY"; //$NON-NLS-1$

    protected List<Node> nodes = new ArrayList<Node>();

    private List<NodeContainer> nodeContainers = new ArrayList<NodeContainer>();

    private IProcess2 process;

    private Node node;

    private Rectangle jobletRectangle;

    private int rightChangeWidth;

    private int downChangeHeight;

    private int leftChangeWidth;

    private int upChangeHeight;

    private boolean hasChange;

    private boolean update = false;

    private boolean needchangeLock = true;

    protected List<IElement> jobletElements = new ArrayList<IElement>();

    public static final int EXPEND_SIZE = 10;

    private String mrName = "";

    private Double percentMap = new Double(0);

    private Double percentReduce = new Double(0);

    public JobletContainer(Node node) {
        super(node);
        this.node = node;
    }

    /**
     * Getter for process.
     * 
     * @return the process
     */
    public IProcess2 getProcess() {
        if (process == null) {
            IProcess iPro = node.getComponent().getProcess();
            if (iPro instanceof IProcess2) {
                return (IProcess2) iPro;
            }
        }
        return this.process;
    }

    public Rectangle getJobletUnion(Rectangle jobletNodeRec, Rectangle rect) {
        // if (rect == null || rect.isEmpty())
        // return new Rectangle(this);
        Rectangle union = new Rectangle(jobletNodeRec.x, jobletNodeRec.y, 0, 0);
        union.width = Math.max(jobletNodeRec.x + jobletNodeRec.width, rect.x + rect.width) - union.x;
        union.height = Math.max(jobletNodeRec.y + jobletNodeRec.height, rect.y + rect.height) - union.y;
        return union;
    }

    /**
     * DOC hwang Comment method "getJobletContainerRectangle".
     * 
     * @return
     */
    public Rectangle getJobletContainerRectangle() {
        Rectangle totalRectangle = null;
        boolean collapsed = isCollapsed();
        boolean mapStart = this.getNode().isMapReduceStart();
        // List<INode> jobletNodes = new ArrayList<INode>();
        // if (this.getNode().isJoblet()) {
        // IProcess jobletProcess = this.getNode().getComponent().getProcess();
        // jobletNodes.addAll(jobletProcess.getGraphicalNodes());
        // } else if (mapStart) {
        // jobletNodes.addAll(this.getNode().getProcess().getGraphicalNodes());
        // }

        if ((!collapsed || mapStart) && nodeContainers.size() > 0) {
            Rectangle jobletNodeRec = this.node.getNodeContainer().getNodeContainerRectangle();
            for (NodeContainer container : nodeContainers) {
                Rectangle curRect = container.getNodeContainerRectangle();
                if (node.getNodeContainer() instanceof JobletContainer) {
                    String title = (String) node.getNodeContainer().getPropertyValue(EParameterName.SUBJOB_TITLE.getName());
                    SimpleHtmlFigure titleFigure = new SimpleHtmlFigure();
                    titleFigure.setText("<b> " + title + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
                    Dimension preferedSize = titleFigure.getPreferredSize();
                    if (preferedSize.width > curRect.width) {
                        curRect.setSize(preferedSize.width + 6, curRect.height);
                    }
                }

                if (this.getNode().isMapReduceStart()) {
                    Integer count = this.getNode().getMrJobInGroupCount();
                    if (count != null && count > 1) {
                        curRect.setSize(curRect.width, curRect.height + 20 * (count - 1));
                    }
                }

                // if (container.getNode().isDesignSubjobStartNode()) {
                // totalRectangle = curRect.getCopy();
                // } else {
                if (totalRectangle == null) {
                    totalRectangle = curRect.getCopy();
                } else {
                    totalRectangle = totalRectangle.getUnion(curRect);
                }
                // }

            }
            // totalRectangle.setLocation(jobletNodeRec.getLocation());
            // totalRectangle.x = totalRectangle.x - EXPEND_SIZE * 2;
            totalRectangle.y = totalRectangle.y - EXPEND_SIZE * 2;
            totalRectangle.height = totalRectangle.height + EXPEND_SIZE * 4;

        } else if (node != null) {
            NodeContainer container = node.getNodeContainer();
            Rectangle curRect = container.getNodeContainerRectangle();
            if (collapsed) {
                totalRectangle = curRect.getCopy();
            } else {
                if (totalRectangle == null) {
                    totalRectangle = curRect.getCopy();
                } else {
                    totalRectangle = totalRectangle.getUnion(curRect);
                }
            }
        }

        if (totalRectangle == null) {
            return null;
        }
        if (jobletRectangle != null) {
            if ((Math.abs(jobletRectangle.width - totalRectangle.width) != 0) || this.nodeContainers.size() == 1) {
                if (jobletRectangle.x > totalRectangle.x) {
                    leftChangeWidth = jobletRectangle.x - totalRectangle.x;
                }
                rightChangeWidth = Math.abs(jobletRectangle.width - totalRectangle.width);
                if (rightChangeWidth > leftChangeWidth) {
                    rightChangeWidth = rightChangeWidth - leftChangeWidth;
                }

            }
            if ((Math.abs(jobletRectangle.height - totalRectangle.height) != 0) || this.nodeContainers.size() == 1) {
                if (jobletRectangle.y > totalRectangle.y) {
                    upChangeHeight = jobletRectangle.y - totalRectangle.y;
                }
                downChangeHeight = Math.abs(jobletRectangle.height - totalRectangle.height);
                if (downChangeHeight > upChangeHeight) {
                    downChangeHeight = downChangeHeight - upChangeHeight;
                }
            }

        }

        changeWidth(totalRectangle);
        jobletRectangle = totalRectangle.getCopy();
        return totalRectangle;
    }

    public int getRightChangeWidth() {
        return this.rightChangeWidth;

    }

    public int getDownChangeHeight() {
        return this.downChangeHeight;
    }

    public int getLeftChangeWidth() {
        return this.leftChangeWidth;

    }

    public int getUpChangeHeight() {
        return this.upChangeHeight;
    }

    public boolean isReadonly() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public Node getJobletStartNode() {
        if (getProcess() != null) {
            if (getProcess().getSubjobContainers().size() > 0) {
                String subjobStartUniqueName = (String) getProcess().getSubjobContainers().get(0)
                        .getPropertyValue(EParameterName.UNIQUE_NAME.getName());
                if (getProcess() != null && (List<Node>) getProcess().getGraphicalNodes() != null) {
                    for (Node node : (List<Node>) getProcess().getGraphicalNodes()) {
                        if (node.getUniqueName() != null && node.getUniqueName().equals(subjobStartUniqueName)) {
                            return node;
                        }
                    }
                }
            }
        } else if (node != null) {
            return node;
        }

        return null;
    }

    @Override
    public void setPropertyValue(String id, Object value) {

        if (id.equals(EParameterName.COLLAPSED.getName())) {
            // outputs.clear();
            for (IConnection conn : node.getOutgoingConnections()) {
                outputs.add(conn);
            }
            // inputs.clear();
            for (IConnection conn : node.getIncomingConnections()) {
                inputs.add(conn);
            }
            if (needchangeLock) {
                if (!((Boolean) value)) {
                    if (!new JobletUtil().isRed(this)) {
                        IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                                IJobletProviderService.class);
                        if (service != null) {
                            service.lockJoblet(this.getNode());
                        }
                    }
                } else {
                    IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                            IJobletProviderService.class);
                    if (service != null) {
                        service.unlockJoblet(node, true);
                    }
                }
            }
            needchangeLock = true;
            refreshJobletNodes(false, (Boolean) value);
            if (!canCollapse()) {
                Shell shell = Display.getCurrent().getActiveShell();

                MessageDialog dlg = new MessageDialog(new Shell(shell), "ERROR", null, "Please attach connection!",
                        MessageDialog.QUESTION, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
                dlg.open();
                return;
            }
            super.setPropertyValue(id, value);
            transferLocation(false);
            updateSubjobContainer();

            if (node.isJoblet()) {
                refreshJobletConnections();
            }

            fireStructureChange(EParameterName.COLLAPSED.getName(), this);
        } else {
            super.setPropertyValue(id, value);
        }
    }

    public void updateJobletNodes(boolean update) {
        // TDI-18915:no need "if(!isCollapsed()){}" here since it is only called in UpdateJobletNodeCommand and can not
        // update the joblet NodeContainer in job when modify the joblet
        refreshJobletNodes(update, isCollapsed());
        transferLocation(update);
        updateSubjobContainer();
        if (this.node.isJoblet()) {
            refreshJobletConnections();
        }
    }

    public void refreshJobletNodes(boolean update, boolean coll) {
        if (this.node.isJoblet()) {
            if (!coll || update) {
                JobletUtil util = new JobletUtil();
                IProcess jobletProcess = this.getNode().getComponent().getProcess();
                Set<IConnection> conns = new HashSet<IConnection>();
                List<? extends INode> jobletNodes = jobletProcess.getGraphicalNodes();
                boolean lockByOther = false;
                if (jobletProcess instanceof IProcess2) {
                    lockByOther = util.lockByOthers(((IProcess2) jobletProcess).getProperty().getItem());
                }

                Map<String, List<? extends IElementParameter>> paraMap = new HashMap<String, List<? extends IElementParameter>>();
                // List<NodeContainer> temList = new ArrayList<NodeContainer>(nodeContainers);
                for (NodeContainer nc : nodeContainers) {
                    if (this.node.getProcess() instanceof IProcess2) {
                        if (!update) {
                            paraMap.put(nc.getNode().getJoblet_unique_name(), nc.getNode().getElementParameters());
                        }
                        ((IProcess2) this.node.getProcess()).removeUniqueNodeName(nc.getNode().getUniqueName());
                    }
                }
                nodeContainers.clear();
                jobletElements.clear();

                // boolean canAdd = false;
                // boolean canRemove = false;
                for (INode inode : jobletNodes) {
                    // canAdd = util.canAdd(temList, inode);
                    if ((inode instanceof Node)) {
                        Node temNode = (Node) inode;
                        // if (canAdd) {
                        conns.addAll(temNode.getIncomingConnections());
                        conns.addAll(temNode.getOutgoingConnections());
                        Node jnode = util.cloneNode(temNode, this.node.getProcess(), paraMap, lockByOther);
                        if (!this.node.isActivate()) {
                            jnode.setActivate(this.node.isActivate());
                        }
                        NodeContainer nodeContainer = util.cloneNodeContainer(temNode.getNodeContainer(), jnode);
                        jnode.setJobletnode(this.node);
                        jnode.setJoblet_unique_name(temNode.getUniqueName());
                        this.nodeContainers.add(nodeContainer);
                        this.jobletElements.add(jnode);
                        this.jobletElements.add(jnode.getNodeLabel());
                        this.jobletElements.add(jnode.getNodeError());
                        this.jobletElements.add(jnode.getNodeProgressBar());
                        // } else if (update) {
                        // for (NodeContainer nodeC : nodeContainers) {
                        // if (nodeC.getNode().getJoblet_unique_name().equals(temNode.getUniqueName())) {
                        // util.updateNode(nodeC.getNode(), temNode);
                        // break;
                        // }
                        // }
                        // }

                    }
                }
                // temList = new ArrayList<NodeContainer>(nodeContainers);
                // for (NodeContainer nodeCon : temList) {
                // Node temNode = nodeCon.getNode();
                // canRemove = util.canDelete(jobletNodes, temNode);
                // if (canRemove) {
                // this.nodeContainers.remove(nodeCon);
                // this.jobletElements.remove(temNode);
                // this.jobletElements.remove(temNode.getNodeError());
                // this.jobletElements.remove(temNode.getNodeLabel());
                // this.jobletElements.remove(temNode.getNodeProgressBar());
                // List<? extends IConnection> inCons = new ArrayList<IConnection>(temNode.getIncomingConnections());
                // for (IConnection con : inCons) {
                // con.getTarget().removeInput(con);
                // }
                // List<? extends IConnection> outCons = new ArrayList<IConnection>(temNode.getOutgoingConnections());
                // for (IConnection con : outCons) {
                // con.getTarget().removeOutput(con);
                // }
                // }
                // }
                for (Iterator<IConnection> iter = conns.iterator(); iter.hasNext();) {
                    IConnection con = iter.next();
                    String sourceName = con.getSource().getUniqueName();
                    String targetName = con.getTarget().getUniqueName();
                    Node sourceNode = null;
                    Node targetNode = null;
                    for (NodeContainer nodeC : nodeContainers) {
                        Node connNode = nodeC.getNode();
                        if (connNode.getJoblet_unique_name().equals(sourceName)) {
                            sourceNode = connNode;
                        }
                        if (connNode.getJoblet_unique_name().equals(targetName)) {
                            targetNode = connNode;
                        }
                        if (sourceNode != null && targetNode != null) {
                            util.cloneConnection(con, sourceNode, targetNode);
                            break;
                        }
                    }
                }

            }
        } else if (this.node.isMapReduceStart()) {
            Integer mrGroupId = node.getMrGroupId();
            List<? extends INode> mapReduceNodes = this.node.getProcess().getGraphicalNodes();
            List<Node> nodeList = new ArrayList<Node>();
            if (node.getNodeContainer().getSubjobContainer().isCollapsed()) {
                nodeList.add(node);
            } else {
                for (INode inode : mapReduceNodes) {
                    if ((inode instanceof Node)) {
                        Node temNode = (Node) inode;
                        if (temNode.getMrGroupId() != null && mrGroupId != null && temNode.getMrGroupId().equals(mrGroupId)) {
                            nodeList.add(temNode);
                        }
                    }
                }
            }

            nodeContainers.clear();
            jobletElements.clear();

            for (Node inode : nodeList) {
                if (!inode.isActivate()) {
                    continue;
                }
                NodeContainer nodeContainer = inode.getNodeContainer();
                // inode.setJobletnode(this.node);
                // inode.setJoblet_unique_name(inode.getUniqueName());
                this.nodeContainers.add(nodeContainer);
                this.jobletElements.add(inode);
                this.jobletElements.add(inode.getNodeLabel());
                this.jobletElements.add(inode.getNodeError());
                this.jobletElements.add(inode.getNodeProgressBar());
            }
        }

    }

    private void transferLocation(boolean update) {
        this.update = update;
        if (update) {
            // do nothing
        }
        if ((this.isCollapsed() == true)) {// && !this.getNode().isMapReduceStart()
            return;
        }
        if (this.getNode().isMapReduce()) {
            return;
        }
        if (this.nodeContainers.size() <= 0) {
            return;
        }
        Point oragPoint = this.getNode().getLocation();
        Node startNode = getJobletStartNode();
        if (startNode == null) {
            return;
        }
        Point stratPoint = startNode.getLocation();
        int withe_x = oragPoint.x - stratPoint.x;
        int hight_y = oragPoint.y - stratPoint.y;
        for (NodeContainer nodeCon : this.nodeContainers) {
            Node jobNode = nodeCon.getNode();
            if (jobNode.getJoblet_unique_name() != null && jobNode.getJoblet_unique_name().equals(startNode.getUniqueName())) {
                jobNode.setLocation(oragPoint);
            } else {
                Point nodePoint = jobNode.getLocation();
                jobNode.setLocation(new Point(nodePoint.x + withe_x, nodePoint.y + hight_y));
                hasChange = true;
            }
        }

    }

    public void transferLocation(Point oldPos) {
        this.update = false;
        if ((this.isCollapsed() == true)) {// && !this.getNode().isMapReduceStart()
            return;
        }
        if (this.getNode().isMapReduce()) {
            return;
        }
        if (this.nodeContainers.size() <= 0) {
            return;
        }
        Point oragPoint = this.getNode().getLocation();
        Node startNode = getJobletStartNode();
        if (startNode == null) {
            return;
        }
        // Point stratPoint = startNode.getLocation();
        int withe_x = oragPoint.x - oldPos.x;
        int hight_y = oragPoint.y - oldPos.y;
        for (NodeContainer nodeCon : this.nodeContainers) {
            Node jobNode = nodeCon.getNode();
            if (jobNode.getJoblet_unique_name().equals(startNode.getUniqueName())) {
                jobNode.setLocation(oragPoint);
            } else {
                Point nodePoint = jobNode.getLocation();
                jobNode.setLocation(new Point(nodePoint.x + withe_x, nodePoint.y + hight_y));
            }
        }
    }

    private void refreshJobletConnections() {
        Iterator<IConnection> inIterator = inputs.iterator();
        while (inIterator.hasNext()) {
            IConnection conn = inIterator.next();
            if (isCollapsed()) {
                ((Connection) conn).reconnect(conn.getSource(), this.node, conn.getLineStyle());
                IConnection[] jobletConns = this.node.getComponent().getProcess().getAllConnections(null);
                if (jobletConns != null) {
                    for (int i = 0; i < jobletConns.length; i++) {
                        this.node.getProcess().removeUniqueConnectionName(jobletConns[i].getUniqueName());
                    }
                }

            } else {
                for (NodeContainer nodeContainer : this.nodeContainers) {
                    Node connNode = nodeContainer.getNode();
                    IElementParameter elePa = this.node.getElementParameter(connNode.getJoblet_unique_name());

                    if (elePa != null) {
                        IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                                IJobletProviderService.class);
                        IElementParameter elechild = null;
                        String uniqueName = null;
                        boolean isTri = service.isTriggerNode(connNode);
                        if (service != null && isTri) {
                            elechild = elePa.getChildParameters().get("COMPONENT_LIST");
                            uniqueName = conn.getMetaName();
                        } else {
                            elechild = elePa.getChildParameters().get("CONNECTION");
                            uniqueName = conn.getUniqueName();
                        }

                        if (elechild != null && elechild.getValue().equals(uniqueName)) {
                            List<? extends INodeConnector> connList = new JobletUtil().createConnectors(connNode,
                                    this.getProcess());
                            // modify///////////////////////////////////////////////////////////////////////////////////////////////////////
                            List<INodeConnector> inodeConnList = new ArrayList<INodeConnector>();
                            inodeConnList.addAll(connList);
                            inodeConnList.addAll(connNode.getListConnector());
                            connNode.setListConnector(inodeConnList);
                            // connNode.getListConnector().addAll(connList);
                            IMetadataTable iTable = this.node.getMetadataTable(connNode.getUniqueName());
                            if (iTable != null && !connNode.getMetadataList().contains(iTable)) {
                                // connNode.getMetadataList().add(iTable);
                            }
                            JobletConnectionReconnectCommand reConnectCommand = new JobletConnectionReconnectCommand(conn);
                            reConnectCommand.setNewTarget(connNode);
                            reConnectCommand.execute();
                            break;
                        } else if (getFlowInput(inputs).size() == 1 && !isTri
                                && new JobletUtil().isJobletInput(connNode, this.getProcess())) {
                            JobletConnectionReconnectCommand reConnectCommand = new JobletConnectionReconnectCommand(conn);
                            reConnectCommand.setNewTarget(connNode);
                            reConnectCommand.execute();
                            break;
                        }
                    }
                }
            }
        }

        Iterator<IConnection> outIterator = outputs.iterator();
        while (outIterator.hasNext()) {
            IConnection conn = outIterator.next();
            if (isCollapsed()) {
                ((Connection) conn).reconnect(this.node, conn.getTarget(), conn.getLineStyle());
            } else {
                for (NodeContainer nodeContainer : this.nodeContainers) {
                    Node connNode = nodeContainer.getNode();
                    if (conn.getConnectorName().equals(connNode.getJoblet_unique_name())) {
                        List<? extends INodeConnector> connList = new JobletUtil().createConnectors(connNode, this.getProcess());
                        List<INodeConnector> inodeConnList = new ArrayList<INodeConnector>();
                        inodeConnList.addAll(connList);
                        inodeConnList.addAll(connNode.getListConnector());
                        connNode.setListConnector(inodeConnList);
                        // connNode.setListConnector(connList);
                        IMetadataTable iTable = this.node.getMetadataTable(connNode.getUniqueName());
                        if (iTable != null && !connNode.getMetadataList().contains(iTable)) {
                            // connNode.getMetadataList().add(iTable);
                        }

                        JobletConnectionReconnectCommand reConnectCommand = new JobletConnectionReconnectCommand(conn);
                        reConnectCommand.setNewSource(connNode);
                        reConnectCommand.execute();
                        // return;
                    }
                }
            }
        }

        if (!isCollapsed()) {
            for (NodeContainer nodeContainer : this.nodeContainers) {
                Node connNode = nodeContainer.getNode();
                IProcess jobletProcess = this.getNode().getComponent().getProcess();
                List<? extends INode> jobletNodes = jobletProcess.getGraphicalNodes();
                for (INode n : jobletNodes) {
                    if (connNode.getJoblet_unique_name().equals(n.getUniqueName())) {
                        connNode.setDummy(n.isDummy());
                        // connNode.setActivate(n.isActivate());
                    }
                }
            }
        }

    }

    public void updateSubjobContainer() {
        fireStructureChange(UPDATE_JOBLET_CONTENT, this);
    }

    public void updateSubjobDisplay() {
        if (!isDisplayed() && isCollapsed()) {
            // if the subjob hidden and collapsed, remove the collapse status first.
            setCollapsed(false);
        }
        fireStructureChange(UPDATE_JOBLET_DISPLAY, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.ISubjobContainer#isDisplayed()
     */
    public boolean isDisplayed() {
        if (!DesignerPlugin.getDefault().getPreferenceStore().getBoolean(TalendDesignerPrefConstants.DISPLAY_SUBJOBS)) {
            return false;
        }
        return true;// (Boolean) getPropertyValue(EParameterName.SUBJOB_DISPLAYED.getName());
    }

    public boolean isUpdate() {
        return this.update;
    }

    /**
     * Sets the collapsed.
     * 
     * @param collapsed the collapsed to set
     */
    public void setCollapsed(boolean collapsed) {
        setPropertyValue(EParameterName.COLLAPSED.getName(), new Boolean(collapsed));
    }

    @Override
    public List getElements() {
        if (isCollapsed() || this.jobletElements.size() <= 0) {
            return super.getElements();
        } else {
            return this.jobletElements;
        }

    }

    public List<NodeContainer> getNodeContainers() {
        return this.nodeContainers;
    }

    public boolean canCollapse() {
        if (node.getIncomingConnections().size() > 1) {
            for (NodeContainer nodeContainer : this.nodeContainers) {
                Node connNode = nodeContainer.getNode();
                IElementParameter elePa = this.node.getElementParameter(connNode.getJoblet_unique_name());
                if (elePa != null) {
                    IElementParameter elechild = elePa.getChildParameters().get("CONNECTION");
                    if (elechild != null && (elechild.getValue() == null || "".equals(elechild.getValue()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Set<IConnection> getFlowInput(Set<IConnection> inputs) {
        Set<IConnection> finputs = new HashSet<IConnection>();
        Iterator<IConnection> ite = inputs.iterator();
        while (ite.hasNext()) {
            IConnection conn = ite.next();
            if (!conn.getConnectorName().equals("SUBJOB_OK") && !conn.getConnectorName().equals("SUBJOB_ERROR")
                    && !conn.getConnectorName().equals("COMPONENT_OK") && !conn.getConnectorName().equals("COMPONENT_ERROR")) {
                finputs.add(conn);
            }
        }
        return finputs;
    }

    // public Boolean isNeedLock() {
    // return needLock;
    // }

    public void setNeedchangeLock(boolean needchangeLock) {
        this.needchangeLock = needchangeLock;
    }

    public void updateState(final String id, Object value, Double percentMap, Double percentReduce) {
        if (id.equals("UPDATE_STATUS")) { //$NON-NLS-1$
            this.percentMap = percentMap;
            this.percentReduce = percentReduce;
            firePropertyChange("UPDATE_STATUS", null, value); //$NON-NLS-1$
        }
    }

    public Double getPercentMap() {
        return this.percentMap;
    }

    public Double getPercentReduce() {
        return this.percentReduce;
    }

    public String getMrName() {
        return mrName;
    }

    public void setMrName(String mrName) {
        this.mrName = mrName;
    }

    private void changeWidth(Rectangle totalRectangle) {
        int temWidth = 114;
        for (NodeContainer nc : nodeContainers) {
            if (nc.getNode().isMrContainsReduce()) {
                temWidth = 240;
            }
        }
        if (this.getMrName() != null && !"".equals(this.getMrName())) {
            if (totalRectangle.width < 240 && this.getNode().isMapReduceStart()) {
                Integer distance = null;
                for (NodeContainer nc : this.getSubjobContainer().getNodeContainers()) {
                    if (nc.getNode().getUniqueName().equals(this.getNode().getUniqueName())) {
                        continue;
                    }
                    if (this.getNode().getMrGroupId() != null && nc.getNode().getMrGroupId() != null
                            && this.getNode().getMrGroupId().equals(nc.getNode().getMrGroupId())) {
                        continue;
                    }
                    int w = nc.getNodeContainerRectangle().x - totalRectangle.x;
                    if (w <= 0) {
                        continue;
                    }
                    if (distance == null) {
                        distance = w;
                    } else if (w < distance) {
                        distance = w;
                    }
                }
                if (distance == null) {
                    totalRectangle.width = temWidth;
                } else if (distance >= temWidth) {
                    totalRectangle.width = temWidth;
                } else if (distance < totalRectangle.width) {
                    return;
                } else {
                    totalRectangle.width = distance;
                }
            }
        }
    }
}
