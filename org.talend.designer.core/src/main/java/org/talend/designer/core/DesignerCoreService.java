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

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.action.CreateProcess;
import org.talend.designer.core.ui.action.SaveJobBeforeRunAction;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.GefEditorLabelProvider;
import org.talend.designer.core.ui.editor.properties.RepositoryValueUtils;
import org.talend.designer.core.ui.views.contexts.Contexts;
import org.talend.designer.core.ui.views.jobsettings.JobSettings;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.core.ui.views.properties.ComponentSettings;
import org.talend.designer.core.utils.UpgradeParameterHelper;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.model.ProxyRepositoryFactory;

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
     * @see org.talend.designer.core.IDesignerCoreService#getProcessFromProcessItem(org.talend.core.model.properties.ProcessItem)
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
     * @see org.talend.designer.core.IDesignerCoreService#getProcessFromJobletProcessItem(org.talend.core.model.properties.JobletProcessItem)
     */
    public IProcess getProcessFromJobletProcessItem(JobletProcessItem jobletProcessItem) {
        Process process = null;
        process = new Process(jobletProcessItem.getProperty());
        process.loadXmlFile();

        return process;
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
     * @see org.talend.designer.core.IDesignerCoreService#getRepositoryAliasName(org.talend.core.model.properties.ConnectionItem)
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
        List<String> openJobs = new ArrayList<String>();
        for (IEditorPart editor : ProcessorUtilities.getOpenedEditors()) {
            AbstractTalendEditor abstractTalendEditor = ((AbstractTalendEditor) editor);
            abstractTalendEditor.updateGraphicalNodes(evt);
            IProcess2 process = abstractTalendEditor.getProcess();
            openJobs.add(process.getName() + process.getVersion());
        }
        if (ComponentUtilities.JOBLET_SCHEMA_CHANGED.equals(evt.getPropertyName())) {
            try {
                String oldName = ((IProcess) evt.getSource()).getName();

                Object[] oldMetadataTables = (Object[]) evt.getOldValue();
                Object[] newMetadataTables = (Object[]) evt.getNewValue();
                List<IMetadataTable> oldInputTableList = (List<IMetadataTable>) oldMetadataTables[0];
                List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];

                List<IMetadataTable> oldOutputTableList = (List<IMetadataTable>) oldMetadataTables[1];
                List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];
                IMetadataTable newInputMetadataTable = null;
                IMetadataTable newOutputMetadataTable = null;

                ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                List<IRepositoryObject> allJobs = factory.getAll(ERepositoryObjectType.PROCESS, true);
                for (IRepositoryObject repositoryObject : allJobs) {
                    String keyId = repositoryObject.getProperty().getLabel() + repositoryObject.getProperty().getVersion();
                    if (!openJobs.contains(keyId)) {
                        ProcessItem item = (ProcessItem) repositoryObject.getProperty().getItem();
                        boolean isModify = false;
                        for (Object o : item.getProcess().getNode()) {
                            NodeType currentNode = (NodeType) o;
                            if (currentNode.getComponentName().equals(oldName)) {
                                EList metadata = currentNode.getMetadata();
                                for (Object object : metadata) {
                                    MetadataType metadataTable = (MetadataType) object;
                                    newInputMetadataTable = getNewInputTableForConnection(newInputTableList, metadataTable
                                            .getConnector());
                                    newOutputMetadataTable = getNewOutputTableForConnection(newOutputTableList, metadataTable
                                            .getName());
                                    if (newInputMetadataTable != null) {
                                        MetadataTool.copyTable(newInputMetadataTable, metadataTable);
                                    } else if (newOutputMetadataTable != null) {
                                        MetadataTool.copyTable(newOutputMetadataTable, metadataTable);
                                    }
                                    isModify = true;
                                }
                            }
                        }
                        if (isModify) {
                            factory.save(item, true);
                        }
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    /**
     * DOC qzhang Comment method "getNewInputTableForConnection".
     * 
     * @param newInputTableList
     * @param connector
     * 
     * @return
     */
    private IMetadataTable getNewInputTableForConnection(List<IMetadataTable> newInputTableList, String connector) {
        for (IMetadataTable metadataTable : newInputTableList) {
            if (connector != null && connector.equals(metadataTable.getAttachedConnector())) {
                return metadataTable;
            }
        }
        return null;
    }

    /**
     * DOC qzhang Comment method "getNewOutputTableForConnection".
     * 
     * @param newOutputTableList
     * @param attachedConnector
     * @return
     */
    private IMetadataTable getNewOutputTableForConnection(List<IMetadataTable> newOutputTableList, String tableName) {
        for (IMetadataTable metadataTable : newOutputTableList) {
            if (tableName != null && tableName.equals(metadataTable.getTableName())) {
                return metadataTable;
            }
        }
        return null;
    }

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
     * @see org.talend.designer.core.IDesignerCoreService#createPalette(org.talend.core.model.components.IComponentsFactory)
     */
    public PaletteRoot createPalette(IComponentsFactory factory) {
        return TalendEditorPaletteFactory.createPalette(factory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#createPalette(org.talend.core.model.components.IComponentsFactory,
     * org.eclipse.gef.palette.PaletteRoot)
     */
    public PaletteRoot createPalette(IComponentsFactory compFac, PaletteRoot root) {
        return TalendEditorPaletteFactory.createPalette(compFac, root);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#isRoutineCompilePass(java.lang.String)
     */
    public Boolean isRoutineCompilePass(String routineName) {
        Boolean compilePass = Problems.isRoutineCompilePass(routineName);
        return compilePass;
    }

    public boolean isTalendEditor(IEditorPart activeEditor) {
        if (activeEditor == null) {
            return false;
        }
        return activeEditor.getSite().getId().equals(MultiPageTalendEditor.ID);

    }

    /**
     * ggu upgrade the element parameters(feature 3310).
     */
    public boolean upgradeItemParameters(Item item) {
        return UpgradeParameterHelper.upgradeItem(item);
    }
}
