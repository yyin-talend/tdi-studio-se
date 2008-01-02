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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.ui.ActiveProcessTracker;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.action.CreateProcess;
import org.talend.designer.core.ui.action.SaveJobBeforeRunAction;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.designer.core.ui.editor.palette.TalendPaletteViewerProvider;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessTemplateTransferDropTargetListener;
import org.talend.designer.core.ui.editor.process.TalendEditorDropTargetListener;
import org.talend.designer.core.ui.editor.properties.GefEditorLabelProvider;
import org.talend.designer.core.ui.editor.properties.RepositoryValueUtils;
import org.talend.designer.core.ui.views.contexts.Contexts;
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
            if (part.getSite().getId().equals(MultiPageTalendEditor.ID)) {
                MultiPageTalendEditor editor = (MultiPageTalendEditor) part;
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
        process.loadXmlFile(processItem.getProcess());

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
        if (!(editor instanceof MultiPageTalendEditor)) {
            return null;
        }
        IProcess process = ((MultiPageTalendEditor) editor).getProcess();
        return process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#refreshDesignerPalette()
     */
    public void refreshDesignerPalette() {

        for (IEditorPart editor : ProcessorUtilities.getOpenedEditors()) {
            ((AbstractTalendEditor) editor).updatePaletteContent();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getActiveProcessTracker()
     */
    public IPartListener getActiveProcessTracker() {

        return new ActiveProcessTracker();
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

    public PaletteRoot createPalette(IComponentsFactory factory, PaletteRoot paletteRoot) {
        return TalendEditorPaletteFactory.createPalette(factory, paletteRoot);
    }

    public IAction getCreateProcessAction(boolean isToolbar) {
        return new CreateProcess(isToolbar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#newProcess(org.talend.core.model.properties.Property)
     */
    public IProcess2 newProcess(Property property) {
        if (property == null) {
            return new Process();
        }
        return new Process(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#createPalettePreferences()
     */
    public FlyoutPreferences createPalettePreferences() {
        return TalendEditorPaletteFactory.createPalettePreferences();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#createPaletteViewerProvider()
     */
    public PaletteViewerProvider createPaletteViewerProvider(EditDomain domain) {
        return new TalendPaletteViewerProvider(domain);
    }

    public TransferDropTargetListener createTemplateTransferDropTargetListener(GraphicalViewer viewer) {
        return new ProcessTemplateTransferDropTargetListener(viewer);
    }

    public TransferDropTargetListener createEditorDropTargetListener(AbstractTalendEditor viewer) {
        return new TalendEditorDropTargetListener(viewer);
    }

}
