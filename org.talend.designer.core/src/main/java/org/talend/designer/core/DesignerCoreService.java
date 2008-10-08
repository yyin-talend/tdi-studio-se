// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.genhtml.IJobSettingConstants;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.action.CreateProcess;
import org.talend.designer.core.ui.action.SaveJobBeforeRunAction;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.JobTemplateViewsAndProcessUtil;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.GefEditorLabelProvider;
import org.talend.designer.core.ui.editor.properties.RepositoryValueUtils;
import org.talend.designer.core.ui.editor.update.UpdateManagerUtils;
import org.talend.designer.core.ui.views.contexts.Contexts;
import org.talend.designer.core.ui.views.jobsettings.JobSettings;
import org.talend.designer.core.ui.views.properties.ComponentSettings;
import org.talend.designer.runprocess.ProcessorUtilities;

/**
 * Detailled comment <br/>.
 * 
 * $Id: DesignerCoreService.java 1 2006-12-19 上午10:25:42 bqian
 * 
 */
public class DesignerCoreService implements IDesignerCoreService {

    public List<IProcess> getOpenedProcess(IEditorReference[] reference) {
        List<IProcess> list = new ArrayList<IProcess>();
        // IEditorReference[] reference =
        // PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        for (IEditorReference er : reference) {
            IEditorPart part = er.getEditor(false);
            if (part instanceof AbstractMultiPageTalendEditor) {
                AbstractMultiPageTalendEditor editor = (AbstractMultiPageTalendEditor) part;
                list.add(editor.getProcess());
            }
        }
        return list;
    }

    public Item getProcessItem(MultiPageEditorPart talendEditor) {
        ProcessEditorInput processEditorInput = (ProcessEditorInput) talendEditor.getEditorInput();
        Item item = processEditorInput.getItem();
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#getProcessFromProcessItem(org.talend.core.model.properties.ProcessItem
     * )
     */
    public IProcess getProcessFromProcessItem(ProcessItem processItem) {
        Process process = null;
        process = new Process(processItem.getProperty());
        process.loadXmlFile();

        return process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#getProcessFromJobletProcessItem(org.talend.core.model.properties
     * .JobletProcessItem)
     */
    public IProcess getProcessFromJobletProcessItem(JobletProcessItem jobletProcessItem) {
        AbstractProcessProvider processProvider = AbstractProcessProvider.findProcessProviderFromPID(IComponent.JOBLET_PID);
        if (processProvider != null) {
            return processProvider.getProcessFromJobletProcessItem(jobletProcessItem);
        }

        return null;
    }

    public ILabelProvider getGEFEditorNodeLabelProvider() {
        return new GefEditorLabelProvider();
    }

    // used for generating HTML only
    /**
     * Constructs a new instance.
     */
    private RepositoryValueUtils repositoryValueUtils = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getQueriesMap()
     */
    public List<Map> getMaps() {
        if (repositoryValueUtils == null) {
            repositoryValueUtils = new RepositoryValueUtils();
        }
        List<Map> list = new ArrayList<Map>();
        list.add(repositoryValueUtils.getRepositoryConnectionItemMap());
        list.add(repositoryValueUtils.getRepositoryDBIdAndNameMap());
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#getRepositoryAliasName(org.talend.core.model.properties.ConnectionItem
     * )
     */
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        return repositoryValueUtils.getRepositoryAliasName(connectionItem);
    }

    public void switchToCurContextsView() {
        Contexts.switchToCurContextsView();
    }

    public void switchToCurComponentSettingsView() {
        ComponentSettings.switchToCurComponentSettingsView();
    }

    public void switchToCurJobSettingsView() {
        JobSettings.switchToCurJobSettingsView();
    }

    public void saveJobBeforeRun(IProcess activeProcess) {
        new SaveJobBeforeRunAction(activeProcess).run();
    }

    // ends.

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getCurrentProcess()
     */
    public IProcess getCurrentProcess() {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (!(editor instanceof AbstractMultiPageTalendEditor)) {
            return null;
        }
        IProcess process = ((AbstractMultiPageTalendEditor) editor).getProcess();
        return process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#refreshDesignerPalette()
     */
    public void synchronizeDesignerUI(PropertyChangeEvent evt) {
        ComponentUtilities.updatePalette();
        // List<String> openJobs = new ArrayList<String>();
        for (IEditorPart editor : ProcessorUtilities.getOpenedEditors()) {
            AbstractTalendEditor abstractTalendEditor = ((AbstractTalendEditor) editor);
            // abstractTalendEditor.updateGraphicalNodes(evt);
            IProcess2 process = abstractTalendEditor.getProcess();
            process.getUpdateManager().addNodesPropertyChanger(evt);
            // openJobs.add(process.getName() + process.getVersion());
        }
        // removed by (featrue 3232)

        // if (ComponentUtilities.JOBLET_SCHEMA_CHANGED.equals(evt.getPropertyName())) {
        // try {
        // String oldName = ((IProcess) evt.getSource()).getName();
        //
        // Object[] oldMetadataTables = (Object[]) evt.getOldValue();
        // Object[] newMetadataTables = (Object[]) evt.getNewValue();
        // List<IMetadataTable> oldInputTableList = (List<IMetadataTable>) oldMetadataTables[0];
        // List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];
        //
        // List<IMetadataTable> oldOutputTableList = (List<IMetadataTable>) oldMetadataTables[1];
        // List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];
        // IMetadataTable newInputMetadataTable = null;
        // IMetadataTable newOutputMetadataTable = null;
        //
        // ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        // List<IRepositoryObject> allJobsAndJoblets = new ArrayList<IRepositoryObject>();
        // allJobsAndJoblets.addAll(factory.getAll(ERepositoryObjectType.PROCESS, true));
        // allJobsAndJoblets.addAll(factory.getAll(ERepositoryObjectType.JOBLET, true));
        // for (IRepositoryObject repositoryObject : allJobsAndJoblets) {
        // String keyId = repositoryObject.getProperty().getLabel() + repositoryObject.getProperty().getVersion();
        // // if (!openJobs.contains(keyId)) {
        // Item item2 = repositoryObject.getProperty().getItem();
        // EList node = null;
        // if (item2 instanceof ProcessItem) {
        // ProcessItem item = (ProcessItem) item2;
        // node = item.getProcess().getNode();
        // } else if (item2 instanceof JobletProcessItem) {
        // JobletProcessItem item = (JobletProcessItem) item2;
        // node = item.getJobletProcess().getNode();
        // }
        // boolean isModify = false;
        // if (node != null) {
        // for (Object o : node) {
        // NodeType currentNode = (NodeType) o;
        // if (currentNode.getComponentName().equals(oldName)) {
        // EList metadata = currentNode.getMetadata();
        // for (Object object : metadata) {
        // MetadataType metadataTable = (MetadataType) object;
        // newInputMetadataTable = getNewInputTableForConnection(newInputTableList, metadataTable
        // .getConnector());
        // newOutputMetadataTable = getNewOutputTableForConnection(newOutputTableList, metadataTable
        // .getName());
        // if (newInputMetadataTable != null) {
        // MetadataTool.copyTable(newInputMetadataTable, metadataTable);
        // } else if (newOutputMetadataTable != null) {
        // MetadataTool.copyTable(newOutputMetadataTable, metadataTable);
        // }
        // isModify = true;
        // }
        // }
        // }
        // }
        // if (isModify) {
        // factory.save(item2, true);
        // }
        // // }
        // }
        // } catch (PersistenceException e) {
        // ExceptionHandler.process(e);
        // }
        // }
    }

    /**
     * DOC qzhang Comment method "getNewInputTableForConnection".
     * 
     * @param newInputTableList
     * @param connector
     * 
     * @return
     */
    // private IMetadataTable getNewInputTableForConnection(List<IMetadataTable> newInputTableList, String connector) {
    // for (IMetadataTable metadataTable : newInputTableList) {
    // if (connector != null && connector.equals(metadataTable.getAttachedConnector())) {
    // return metadataTable;
    // }
    // }
    // return null;
    // }
    /**
     * DOC qzhang Comment method "getNewOutputTableForConnection".
     * 
     * @param newOutputTableList
     * @param attachedConnector
     * @return
     */
    // private IMetadataTable getNewOutputTableForConnection(List<IMetadataTable> newOutputTableList, String tableName)
    // {
    // for (IMetadataTable metadataTable : newOutputTableList) {
    // if (tableName != null && tableName.equals(metadataTable.getTableName())) {
    // return metadataTable;
    // }
    // }
    // return null;
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getPreferenceStore(java.lang.String)
     */
    public String getPreferenceStore(String key) {
        return DesignerPlugin.getDefault().getPreferenceStore().getString(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#createPalette(org.talend.core.model.components.IComponentsFactory)
     */
    public PaletteRoot createPalette(IComponentsFactory factory) {
        return TalendEditorPaletteFactory.createPalette(factory);
    }

    public PaletteRoot getAllNodeStructure(IComponentsFactory factory) {
        return TalendEditorPaletteFactory.getAllNodeStructure(factory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#createPalette(org.talend.core.model.components.IComponentsFactory,
     * org.eclipse.gef.palette.PaletteRoot)
     */
    public PaletteRoot createPalette(IComponentsFactory compFac, PaletteRoot root) {
        return TalendEditorPaletteFactory.createPalette(compFac, root);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#setPaletteFilter(java.lang.String)
     */
    public void setPaletteFilter(String filter) {
        TalendEditorPaletteFactory.setFilter(filter);
    }

    public IAction getCreateProcessAction(boolean isToolbar) {
        return new CreateProcess(isToolbar);
    }

    public List<PaletteEntry> createJobletEtnry() {
        List<PaletteEntry> list = new ArrayList<PaletteEntry>();
        for (AbstractProcessProvider provider : AbstractProcessProvider.findAllProcessProviders()) {
            list.addAll(provider.addJobletEntry());
        }
        return list;
    }

    public boolean isTalendEditor(IEditorPart activeEditor) {
        if (activeEditor == null) {
            return false;
        }
        return activeEditor.getSite().getId().equals(MultiPageTalendEditor.ID);

    }

    public INode getRefrenceNode(String componentName) {

        if (componentName == null) {
            return null;
        }

        IComponentsFactory compFac = CorePlugin.getDefault().getRepositoryService().getComponentsFactory();
        IComponent salesforceComponent = compFac.get(componentName);

        return new DataNode(salesforceComponent, componentName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#executeUpdatesManager(java.util.List)
     */
    public boolean executeUpdatesManager(List<UpdateResult> results) {
        return UpdateManagerUtils.executeUpdates(results);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getProcessForJobTemplate()
     */
    public List<IProcess> getProcessForJobTemplate() {
        if (JobTemplateViewsAndProcessUtil.getInstance().getHelpProcess() != null) {
            // Everytime return a new list
            List<IProcess> result = new ArrayList<IProcess>();
            result.add(JobTemplateViewsAndProcessUtil.getInstance().getHelpProcess());
            return result;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.IDesignerCoreService#getJobModificationDateMap(org.talend.core.model.process.IProcess)
     */
    public Map<String, java.util.Date> getJobModificationDateMap(IProcess process) {
        if (process instanceof Process) {
            return ((Process) process).getJobModificationDateMap();
        }
        return null;
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getDisplayForProcessParameterFromName".
     * 
     * @param name
     * @return
     */
    public String getDisplayForProcessParameterFromName(final String name) {

        for (EParameterName param : EParameterName.values()) {
            String keyName = name;
            String suffix = "";
            if (name.endsWith("_IMPLICIT_CONTEXT")) {
                keyName = name.substring(0, name.indexOf("_IMPLICIT_CONTEXT"));
                suffix = " (implict context)";
            }
            if (param.name().equals(keyName)) {
                return param.getDisplayName() + suffix;
            }
        }

        if (name.equals(IJobSettingConstants.PROPERTY_TYPE_IMPLICIT_CONTEXT_PROPERTY_TYPE)) {
            return "Property";
        } else if (name.equals(IJobSettingConstants.PROPERTY_TYPE_IMPLICIT_CONTEXT_REPOSITORY_PROPERTY_TYPE)) {
            return "Property:source";
        } else if (name.equals(IJobSettingConstants.PROPERTY_TYPE_PROPERTY_TYPE)) {
            return "Property";
        } else if (name.equals(IJobSettingConstants.PROPERTY_TYPE_REPOSITORY_PROPERTY_TYPE)) {
            return "Property:source";
        }

        return name;
    }

    public void refreshComponentView(Item item) {
        try {
            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            IEditorReference[] editors = activePage.getEditorReferences();
            for (IEditorReference er : editors) {
                IEditorPart part = er.getEditor(false);
                if (part instanceof AbstractMultiPageTalendEditor) {
                    AbstractMultiPageTalendEditor editor = (AbstractMultiPageTalendEditor) part;
                    CommandStack stack = (CommandStack) editor.getTalendEditor().getAdapter(CommandStack.class);
                    if (stack != null) {
                        IProcess process = editor.getProcess();
                        for (final INode processNode : process.getGraphicalNodes()) {
                            if (processNode instanceof Node) {
                                checkRepository((Node) processNode, item, stack);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * DOC hcw Comment method "checkRepository".
     * 
     * @param node
     * @param item
     * @param stack
     */
    private void checkRepository(final Node node, Item item, CommandStack stack) {
        final String updataComponentParamName = EParameterName.UPDATE_COMPONENTS.getName();
        final List<IElementParameter> repositoryParam = new ArrayList<IElementParameter>();

        for (IElementParameter param : node.getElementParameters()) {
            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                String value = (String) param.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName()).getValue();

                if (value.equals(EmfComponent.REPOSITORY)) {
                    IElementParameter schema = param.getChildParameters().get(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                    if (schema != null && schema.getValue() != null) {
                        String[] names = ((String) schema.getValue()).split(" - ");
                        if (names.length > 0) {
                            if (names[0].equals(item.getProperty().getId())) {
                                repositoryParam.add(schema);
                            }
                        }
                    }
                }

            } else if (param.getField().equals(EParameterFieldType.PROPERTY_TYPE)) {
                Object value = param.getChildParameters().get(EParameterName.PROPERTY_TYPE.getName()).getValue();
                if (value.equals(EmfComponent.REPOSITORY)) {
                    IElementParameter property = param.getChildParameters()
                            .get(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                    if (property != null && property.getValue() != null) {

                        if (property.getValue().equals(item.getProperty().getId())) {
                            repositoryParam.add(property);
                        }

                    }
                }
            }
        }

        if (repositoryParam.isEmpty()) {
            return;
        }

        stack.execute(new Command() {

            @Override
            public void execute() {

                node.setPropertyValue(updataComponentParamName, new Boolean(true));
                for (IElementParameter param : repositoryParam) {
                    // force to reload label
                    param.setListItemsDisplayName(new String[0]);
                    param.setListItemsValue(new String[0]);
                }
            }

        });
    }
}
