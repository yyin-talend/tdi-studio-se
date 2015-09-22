package org.talend.designer.dataprep.component.preview;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.designer.component.preview.shadow.ViewBlockProfileDataMain;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.runprocess.ui.ProcessManager;

public class ViewDataprepDataMain {

    private static Logger log = Logger.getLogger(ViewBlockProfileDataMain.class);

    public static final String PREVIEW = "ViewDataprep"; //$NON-NLS-1$

    private IContext previewContext = null;

    private final AbstractNode dataNode;

    public ViewDataprepDataMain(Node node, IProcess process) {
        // MOD by zhsen care for tMap Component which need more parameters when use.
        this.dataNode = new DataNode(cloneComponent(node.getComponent()), node.getUniqueName());
        copyConnections(dataNode, node);

        copyParamsAndMetadata(node);
        copyContextRefrence(node);
        dataNode.setProcess(process);
    }

    /**
     * DOC zshen Comment method "copyConnections".
     * 
     * @param dataNode2
     * @param node
     */
    private void copyConnections(AbstractNode targetNode, Node sourceNode) {
        // copy input connections
        List<IConnection> tempList = new ArrayList<IConnection>();
        tempList.addAll(sourceNode.getIncomingConnections());
        targetNode.setIncomingConnections(tempList);
        // copy output connections
        tempList = new ArrayList<IConnection>();
        tempList.addAll(sourceNode.getOutgoingConnections());
        targetNode.setOutgoingConnections(tempList);

    }

    public AbstractNode getDataNode() {
        return dataNode;
    }

    private IComponent cloneComponent(IComponent component) {
        IComponentsFactory compFac = CorePlugin.getDefault().getRepositoryService().getComponentsFactory();
        IComponent fakeComponent = compFac.get(component.getName(), ComponentCategory.CATEGORY_4_DI.getName());
        return fakeComponent;
    }

    private void copyParamsAndMetadata(Node component) {
        for (IElementParameter curParam : component.getElementParameters()) {
            String name = curParam.getName();
            IElementParameter dataNodeParam = dataNode.getElementParameter(name);
            if (dataNodeParam != null) {
                dataNodeParam.setValue(curParam.getValue());
                if (dataNodeParam.getFieldType() == EParameterFieldType.TABLE) {
                    dataNodeParam.setListItemsValue(curParam.getListItemsValue());
                }
                if ("JOIN_KEY".equals(name)) { //$NON-NLS-1$
                    dataNode.addElementParameter(dataNodeParam);
                } else if ("INTABLE".equals(name)) { //$NON-NLS-1$
                    // MOD yyi 2011-11-24 TDQ-3691 :ElementParameterParser needs DisplayCodeName for clone the inline
                    // table for tFixedFlowInput.
                    dataNodeParam.setListItemsDisplayCodeName(curParam.getListItemsDisplayCodeName());
                }
            } else {
                // add for feature 10280 server <==> host
                if ("SERVER".equals(name)) { //$NON-NLS-1$
                    IElementParameter elementParameter = dataNode.getElementParameter("HOST"); //$NON-NLS-1$
                    if (elementParameter != null) {
                        elementParameter.setValue(curParam.getValue());
                    }
                }
            }
        }

        statusCopy(component);

        dataNode.getMetadataList().clear();
        for (IMetadataTable metadataTable : component.getMetadataList()) {
            IMetadataTable orgtable = metadataTable.clone();
            orgtable.setAttachedConnector(metadataTable.getAttachedConnector());
            orgtable.setTableName(metadataTable.getTableName());
            dataNode.getMetadataList().add(orgtable);
        }
    }

    /**
     * DOC zshen Comment method "statusCopy".
     * 
     * @param component
     */
    private void statusCopy(Node component) {
        this.dataNode.setActivate(component.isActivate());
        this.dataNode.setStart(component.isStart());
        this.dataNode.setSubProcessStart(component.isSubProcessStart());

    }

    private void copyContextRefrence(Node component) {

        if (component.getProcess().getContextManager() != null) {
            if (component.getProcess().getContextManager().getListContext().size() > 1) {
                // get select context form ProcessContextComposite
                IContext selectContext = ProcessManager.getInstance().getSelectContext();
                if (selectContext == null) {
                    selectContext = component.getProcess().getContextManager().getDefaultContext();
                }
                setPreviewContext(selectContext);
            } else {
                setPreviewContext(component.getProcess().getContextManager().getDefaultContext());
            }

        }

    }

    /**
     * Getter for previewContext.
     * 
     * @return the previewContext
     */
    public IContext getPreviewContext() {
        return this.previewContext;
    }

    /**
     * Sets the previewContext.
     * 
     * @param previewContext the previewContext to set
     */
    public void setPreviewContext(IContext previewContext) {
        this.previewContext = previewContext;
    }

}
