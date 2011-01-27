package org.talend.designer.core.ui.editor.jobletcontainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.ui.IJobletProviderService;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.NodeConnector;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.joblet.model.JobletNode;
import org.talend.designer.joblet.model.JobletProcess;

public class JobletUtil {

    public boolean isJobletInput(Node connNode, IProcess2 process) {
        if (process.getProperty().getItem() instanceof JobletProcessItem) {
            JobletProcess jobletProcess = ((JobletProcessItem) process.getProperty().getItem()).getJobletProcess();
            List<JobletNode> jobletNodes = jobletProcess.getJobletNodes();
            for (JobletNode jnode : jobletNodes) {
                List list = jnode.getElementParameter();
                for (Object obj : list) {
                    if (obj instanceof ElementParameterType) {
                        if (((ElementParameterType) obj).getValue().equals(connNode.getJoblet_unique_name())) {
                            return jnode.isInput();
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<INodeConnector> createConnectors(INode node, IProcess2 process) {
        List<INodeConnector> listConnector = new ArrayList<INodeConnector>();
        if (process == null) {
            return listConnector;
        }
        if (process.getProperty().getItem() instanceof JobletProcessItem) {
            JobletProcess jobletProcess = ((JobletProcessItem) process.getProperty().getItem()).getJobletProcess();
            EList<JobletNode> jobletLinks = jobletProcess.getJobletNodes();
            INodeConnector nodeConnector;
            int nbInput = 0;
            int nbOutput = 0;
            int jobletNbInput = 0;
            for (JobletNode jNode : jobletLinks) {
                if (jNode.isInput()) {
                    jobletNbInput++;
                }
            }
            boolean multipleInput = jobletNbInput > 1;
            for (JobletNode jNode : jobletLinks) {
                String name = ComponentUtilities.getNodeUniqueName(jNode);
                if (jNode.isTrigger()) {
                    // createTriggerConnector(listConnector, node);
                } else {
                    if (jNode.isInput()) {
                        if (multipleInput) {
                            nodeConnector = new NodeConnector(node);
                            nodeConnector.setName(name);
                            nodeConnector.setBaseSchema(EConnectionType.FLOW_MAIN.getName());
                            nodeConnector.setDefaultConnectionType(EConnectionType.FLOW_MAIN);
                            nodeConnector.setMaxLinkInput(1);
                            nodeConnector.setMinLinkInput(0);
                            nodeConnector.setMaxLinkOutput(0);
                            nodeConnector.setMinLinkOutput(0);
                            listConnector.add(nodeConnector);
                        }
                        nbInput++;
                    } else {
                        nodeConnector = new NodeConnector(node);
                        nodeConnector.setName(name);
                        nodeConnector.setBaseSchema(EConnectionType.FLOW_MAIN.getName());
                        nodeConnector.setBuiltIn(true);
                        nodeConnector.setMaxLinkOutput(1);
                        nodeConnector.setMinLinkOutput(1);
                        // achen modify to fix bug 6205

                        String displayName = getDisplayName(jNode);
                        nodeConnector.setLinkName("Joblet " + name);
                        nodeConnector.setMenuName(displayName);

                        nodeConnector.setDefaultConnectionType(EConnectionType.FLOW_MAIN);
                        nodeConnector.addConnectionProperty(EConnectionType.FLOW_MAIN, EConnectionType.FLOW_MAIN.getRGB(),
                                EConnectionType.FLOW_MAIN.getDefaultLineStyle());
                        nodeConnector.addConnectionProperty(EConnectionType.FLOW_REF, EConnectionType.FLOW_REF.getRGB(),
                                EConnectionType.FLOW_REF.getDefaultLineStyle());
                        nodeConnector.addConnectionProperty(EConnectionType.FLOW_MERGE, EConnectionType.FLOW_MERGE.getRGB(),
                                EConnectionType.FLOW_MERGE.getDefaultLineStyle());
                        listConnector.add(nodeConnector);
                        nbOutput++;
                    }
                }
            }
        }
        createTriggerConnector(listConnector, node);
        return listConnector;
    }

    private void createTriggerConnector(List<INodeConnector> listConnector, INode jobletNode) {
        IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                IJobletProviderService.class);
        if (service != null) {
            List<INode> triggerNodes = service.getTriggerNodes(jobletNode.getJobletNode());
            for (INode node : triggerNodes) {
                // INode node = jobletNode;
                EConnectionType triggerConnType = service.getTriggerNodeConnType(node);
                if (triggerConnType == null) {
                    continue;
                }
                boolean isInput = service.isTriggerInputNode(node);
                INodeConnector nodeConnector = null;
                String name = node.getUniqueName();

                nodeConnector = new NodeConnector(jobletNode);
                nodeConnector.setName(name);
                nodeConnector.setBaseSchema(triggerConnType.getName());
                nodeConnector.setBuiltIn(true);

                nodeConnector.setMaxLinkOutput(isInput ? 0 : 1);
                nodeConnector.setMinLinkOutput(0);
                nodeConnector.setMaxLinkInput(isInput ? 1 : 0);
                nodeConnector.setMinLinkInput(0);

                String displayName = conDisplayName(node.getLabel(), name);

                nodeConnector.setLinkName(triggerConnType.getDefaultLinkName() + " (" + displayName + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                nodeConnector.setMenuName(triggerConnType.getDefaultMenuName() + " (" + displayName + ")"); //$NON-NLS-1$ //$NON-NLS-2$

                nodeConnector.setDefaultConnectionType(triggerConnType);
                nodeConnector.addConnectionProperty(triggerConnType, triggerConnType.getRGB(),
                        triggerConnType.getDefaultLineStyle());

                listConnector.add(nodeConnector);

            }
        }
    }

    private String getDisplayName(NodeType node) {
        String uniqueName = ComponentUtilities.getNodeUniqueName(node);
        String label = ComponentUtilities.getNodePropertyValue(node, EParameterName.LABEL.getName());
        String displayName = uniqueName;
        if (label != null && uniqueName != null) {
            label = label.replaceAll("__" + EParameterName.UNIQUE_NAME.getName() + "__", uniqueName);//$NON-NLS-1$
            displayName = conDisplayName(label, uniqueName);
        }
        return "Joblet " + displayName;
    }

    private String conDisplayName(String label, String uniqueName) {
        String displayName = uniqueName;
        if (!label.equals(uniqueName)) {
            displayName = label + "(" + uniqueName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return displayName;
    }

    public NodeContainer cloneNodeContainer(NodeContainer nodeContainer, Node cloneNode) {
        NodeContainerPart nodeConPart = new NodeContainerPart();
        NodeContainer cloneNodeContainer = new NodeContainer(cloneNode);
        nodeConPart.setModel(cloneNodeContainer);
        cloneNodeContainer.setNodeError(cloneNode.getNodeError());
        cloneNodeContainer.setNodeLabel(cloneNode.getNodeLabel());
        cloneNodeContainer.setNodeProgressBar(cloneNode.getNodeProgressBar());
        cloneNodeContainer.setReadOnly(nodeContainer.isReadOnly());
        return cloneNodeContainer;
    }

    public Node cloneNode(Node node, IProcess process, Boolean lock) {
        NodePart nodePart = new NodePart();
        Node cloneNode = new Node(node.getComponent(), (IProcess2) process);
        nodePart.setModel(cloneNode);
        if (lock == null) {
            cloneNode.setReadOnly(true);
        } else {
            if (lock) {
                cloneNode.setReadOnly(false);
            } else {
                cloneNode.setReadOnly(true);
            }
        }

        List<? extends IElementParameter> elementParas = node.getElementParameters();
        for (IElementParameter elementPara : elementParas) {
            if (elementPara.getName() != null && !elementPara.getName().equals("UNIQUE_NAME")) {
                IElementParameter cloneElement = cloneNode.getElementParameter(elementPara.getName());
                cloneElement.setValue(elementPara.getValue());

                if (lock == null) {
                    cloneElement.setReadOnly(true);
                } else {
                    if (lock) {
                        cloneElement.setReadOnly(false);
                    } else {
                        cloneElement.setReadOnly(true);
                    }
                }

                if (elementPara.getChildParameters() != null) {
                    Map<String, IElementParameter> elementParaChild = elementPara.getChildParameters();
                    Map<String, IElementParameter> cloneElementChild = cloneElement.getChildParameters();
                    Iterator<Entry<String, IElementParameter>> ite = elementParaChild.entrySet().iterator();
                    while (ite.hasNext()) {
                        Entry<String, IElementParameter> entry = ite.next();
                        String key = entry.getKey();
                        IElementParameter c = entry.getValue();
                        if (key != null && c != null) {
                            IElementParameter cloneC = cloneElementChild.get(key);
                            if (cloneC != null) {
                                cloneC.setValue(c.getValue());
                                if (lock == null) {
                                    cloneC.setReadOnly(true);
                                } else {
                                    if (lock) {
                                        cloneC.setReadOnly(false);
                                    } else {
                                        cloneC.setReadOnly(true);
                                    }
                                }

                            }
                        }
                    }
                }
            }

        }

        for (IElementParameter param : cloneNode.getElementParameters()) {
            String repositoryValue = param.getRepositoryValue();
            if (param.isShow(cloneNode.getElementParameters()) && (repositoryValue != null)
                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))
                    && param.getFieldType() != EParameterFieldType.MEMO_SQL) {
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            }
        }

        cloneNode.setMetadataList(node.getMetadataList());
        cloneNode.setListConnector(node.getListConnector());
        cloneNode.setConnectionName(node.getConnectionName());
        cloneNode.setLocation(node.getLocation());
        return cloneNode;
    }

    public void updateNode(Node cloneNode, Node node) {
        List<? extends IElementParameter> elementParas = node.getElementParameters();
        for (IElementParameter elementPara : elementParas) {
            if (elementPara.getName() != null && !elementPara.getName().equals("UNIQUE_NAME")) {
                IElementParameter cloneElement = cloneNode.getElementParameter(elementPara.getName());
                cloneElement.setValue(elementPara.getValue());
            }

        }
        cloneNode.setLocation(node.getLocation());
    }

    public Connection cloneConnection(IConnection conn, INode source, INode target) {
        Connection cloneConn = new Connection(source, target, conn.getLineStyle(), conn.getConnectorName(), conn.getMetaName(),
                conn.getName(), conn.isMonitorConnection());
        return cloneConn;
    }

    public boolean canAdd(List<NodeContainer> nodeContainers, INode jobletNodes) {
        for (NodeContainer nodeCon : nodeContainers) {
            Node temNode = nodeCon.getNode();
            if (jobletNodes.getUniqueName().equals(temNode.getJoblet_unique_name())) {
                return false;
            }
        }
        return true;
    }

    public boolean canDelete(List<? extends INode> jobletNodes, Node temNode) {
        for (INode jCon : jobletNodes) {
            if (jCon.getUniqueName().equals(temNode.getJoblet_unique_name())) {
                return false;
            }
        }
        return true;
    }

    public Rectangle getExpandRectangle(Rectangle nodeRec) {
        Point location = nodeRec.getLocation();
        Dimension size = nodeRec.getSize();
        int width = location.x + size.width;
        int height = location.y + size.height;
        Rectangle rec = new Rectangle(0, 0, width, height);
        return rec;
    }

}
