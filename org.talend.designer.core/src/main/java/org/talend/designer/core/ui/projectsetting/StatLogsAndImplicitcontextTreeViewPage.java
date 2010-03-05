// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.projectsetting;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.editor.cmd.LoadProjectSettingsCommand;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.update.UpdateManagerUtils;
import org.talend.designer.core.ui.views.properties.WidgetFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.preference.ProjectSettingPage;
import org.talend.repository.ui.views.CheckboxRepositoryTreeViewer;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryLabelProvider;
import org.talend.repository.ui.views.RepositoryNameSorter;

/**
 * DOC achen class global comment. Detailled comment
 */
public class StatLogsAndImplicitcontextTreeViewPage extends ProjectSettingPage {

    private IRepositoryView repositoryView;

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    final List<IProcess> opendProcess = UpdateManagerUtils.getOpenedProcess();

    // implicit context tree
    private CheckboxRepositoryTreeViewer viewer;

    private AllJobContentProvider contentProvider;

    private List<RepositoryNode> checkedObjects = new ArrayList<RepositoryNode>();

    private List<RepositoryNode> uncheckedObjects = new ArrayList<RepositoryNode>();

    // stats and log tree
    private CheckboxRepositoryTreeViewer statViewer;

    private AllJobContentProvider statContentProvider;

    private List<RepositoryNode> statCheckedObjects = new ArrayList<RepositoryNode>();

    private List<RepositoryNode> statUncheckedObjects = new ArrayList<RepositoryNode>();

    private WidgetFactory widgetFactory = new WidgetFactory();

    private IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .getEditorReferences();

    // private IDesignerCoreService coreService = CorePlugin.getDefault().getDesignerCoreService();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = widgetFactory.createComposite(parent, SWT.NONE);
        // composite.setLayout(new FillLayout());
        GridLayout layout = new GridLayout();
        layout.marginLeft = 5;
        layout.marginRight = 5;
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        composite.setLayout(layout);

        repositoryView = RepositoryManager.getRepositoryView();
        createImplicitcontextTree(composite);
        createStatTree(composite);
        return composite;
    }

    @Override
    public void dispose() {
        if (widgetFactory != null)
            widgetFactory.dispose();
        super.dispose();
    }

    private void createImplicitcontextTree(Composite composite) {
        Group g = widgetFactory.createGroup(composite, Messages.getString("ExtraComposite.ImplicitContextSettings")); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        g.setLayoutData(gd);
        g.setLayout(new FillLayout());
        viewer = new CheckboxRepositoryTreeViewer(g, SWT.MULTI | SWT.V_SCROLL);
        contentProvider = new AllJobContentProvider(repositoryView);
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new RepositoryLabelProvider(repositoryView));
        viewer.setSorter(new RepositoryNameSorter());
        IViewSite viewSite = repositoryView.getViewSite();
        viewer.setInput(viewSite);

        // This only tree listener aim is to change open/close icons on folders :
        viewer.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_CLOSE_ICON));
                    }
                }
            }

            public void treeExpanded(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_OPEN_ICON));
                    }
                }
            }
        });

        viewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
                processItems(objects, node);
                if (event.getChecked()) {
                    checkedObjects.addAll(objects);
                    uncheckedObjects.removeAll(objects);
                } else {
                    checkedObjects.removeAll(objects);
                    uncheckedObjects.addAll(objects);
                }
                // set checked
                viewer.setCheckedElements(checkedObjects.toArray());
                // viewer.refresh();
            }
        });

        init();

        viewer.setCheckedElements(checkedObjects.toArray());
        viewer.setExpandedElements(contentProvider.getContents());

    }

    private void init() {
        RepositoryNode[] nodes = contentProvider.getContents();
        List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
        for (RepositoryNode n : nodes) {
            processItems(objects, n);
            for (RepositoryNode node : objects) {
                if (isUseProjectSetting(node)) {
                    if (!checkedObjects.contains(node)) {
                        checkedObjects.add(node);
                    }
                    uncheckedObjects.remove(node);
                } else {
                    checkedObjects.remove(node);
                    if (!uncheckedObjects.contains(node)) {
                        uncheckedObjects.add(node);
                    }
                }
            }
        }
    }

    private void createStatTree(Composite composite) {
        Group g = widgetFactory.createGroup(composite, Messages.getString("StatsAndLogsComposite.StatsLogsSettings")); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        g.setLayoutData(gd);
        g.setLayout(new FillLayout());
        statViewer = new CheckboxRepositoryTreeViewer(g, SWT.MULTI | SWT.V_SCROLL);
        statContentProvider = new AllJobContentProvider(repositoryView);
        statViewer.setContentProvider(statContentProvider);
        statViewer.setLabelProvider(new RepositoryLabelProvider(repositoryView));
        statViewer.setSorter(new RepositoryNameSorter());
        IViewSite viewSite = repositoryView.getViewSite();
        statViewer.setInput(viewSite);

        // This only tree listener aim is to change open/close icons on folders :
        statViewer.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(statViewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_CLOSE_ICON));
                    }
                }
            }

            public void treeExpanded(TreeExpansionEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                    TreeItem item = getObject(statViewer.getTree(), event.getElement());
                    if (item != null) {
                        item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_OPEN_ICON));
                    }
                }
            }
        });

        statViewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
                processItems(objects, node);
                if (event.getChecked()) {
                    statCheckedObjects.addAll(objects);
                    statUncheckedObjects.removeAll(objects);
                } else {
                    statCheckedObjects.removeAll(objects);
                    statUncheckedObjects.addAll(objects);
                }
                // set checked
                statViewer.setCheckedElements(statCheckedObjects.toArray());
                // viewer.refresh();
            }
        });

        initstat();

        statViewer.setCheckedElements(statCheckedObjects.toArray());
        statViewer.setExpandedElements(statContentProvider.getContents());
    }

    private void initstat() {
        RepositoryNode[] nodes = statContentProvider.getContents();
        List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
        for (RepositoryNode n : nodes) {
            processItems(objects, n);
            for (RepositoryNode node : objects) {
                if (isStatUseProjectSetting(node)) {
                    if (!statCheckedObjects.contains(node)) {
                        statCheckedObjects.add(node);
                    }
                    statUncheckedObjects.remove(node);
                } else {
                    statCheckedObjects.remove(node);
                    if (!statUncheckedObjects.contains(node)) {
                        statUncheckedObjects.add(node);
                    }
                }
            }
        }
    }

    private boolean isUseProjectSetting(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();

        String implictB = ElementParameter2ParameterType.getParameterValue(pType,
                EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName());
        if (implictB != null && "true".equals(implictB)) { //$NON-NLS-1$
            return true;
        } else {
            return false;
        }
    }

    private boolean isStatUseProjectSetting(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();

        String statB = ElementParameter2ParameterType.getParameterValue(pType, EParameterName.STATANDLOG_USE_PROJECT_SETTINGS
                .getName());
        if (statB != null && "true".equals(statB)) { //$NON-NLS-1$
            return true;
        } else {
            return false;
        }
    }

    private boolean isOpenProcess(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        if (property.getItem() instanceof ProcessItem) {
            for (IProcess process : opendProcess) {
                if (process.getId().equals(property.getId()) && process.getLabel().equals(property.getLabel())
                        && process.getVersion().equals(property.getVersion())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void processItems(List<RepositoryNode> objects, RepositoryNode node) {
        if (node == null) {
            return;
        }
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            if (node.getObject() != null) {
                objects.add(node);
            }
        } else {
            for (RepositoryNode child : node.getChildren()) {
                processItems(objects, child);
            }
        }
    }

    private TreeItem getObject(Tree tree, Object objectToFind) {
        for (TreeItem item : tree.getItems()) {
            TreeItem toReturn = getObject(item, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    private TreeItem getObject(TreeItem parent, Object objectToFind) {
        for (TreeItem currentChild : parent.getItems()) {
            if (objectToFind.equals(currentChild.getData())) {
                return currentChild;
            }
            TreeItem toReturn = getObject(currentChild, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    class AllJobContentProvider extends RepositoryContentProvider {

        private IRepositoryView view = null;

        public AllJobContentProvider(IRepositoryView v) {
            super(v);
            view = v;
        }

        public Object[] getElements(Object parent) {
            if (parent.equals(view.getViewSite())) {
                return getContents();
            }
            return getChildren(parent);
        }

        RepositoryNode[] getContents() {
            ProjectRepositoryNode systemFolders = (ProjectRepositoryNode) view.getRoot();
            return new RepositoryNode[] { systemFolders.getRootRepositoryNode(ERepositoryObjectType.PROCESS) };
            // return (RepositoryNode[])
            // systemFolders.getRootRepositoryNode(ERepositoryObjectType.PROCESS).getChildren().toArray(
            // new RepositoryNode[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        save();
        super.performApply();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        save();
        return super.performOk();
    }

    private void saveProcess(RepositoryNode node, String paramName, Boolean isUseProjectSettings, IProgressMonitor monitor) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();

        if (isOpenProcess(node)) {
            Process process = getProcess(opendProcess, node);

            ElementParameter2ParameterType.setParameterValue(process, paramName, isUseProjectSettings);
            if (isUseProjectSettings) {
                LoadProjectSettingsCommand command = new LoadProjectSettingsCommand(process, paramName, isUseProjectSettings);
                exeCommand(process, command);
            }
            monitor.worked(100);
        } else {

            ElementParameter2ParameterType.setParameterValue(pType, paramName, isUseProjectSettings);
            if (isUseProjectSettings) {
                try {
                    Process process = (Process) CorePlugin.getDefault().getDesignerCoreService().getProcessFromProcessItem(pItem);
                    LoadProjectSettingsCommand command = new LoadProjectSettingsCommand(process, paramName, isUseProjectSettings);
                    exeCommand(process, command);
                    ProcessType processType = process.saveXmlFile();
                    pItem.setProcess(processType);
                    factory.save(pItem);
                    monitor.worked(100);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

    }

    private void saveChangedNode(String paramName, IProgressMonitor monitor) {
        List<RepositoryNode> checked = new ArrayList<RepositoryNode>();
        List<RepositoryNode> unChecked = new ArrayList<RepositoryNode>();
        if (EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName().equals(paramName)) {
            checked = checkedObjects;
            unChecked = uncheckedObjects;
        } else if (EParameterName.STATANDLOG_USE_PROJECT_SETTINGS.getName().equals(paramName)) {
            checked = statCheckedObjects;
            unChecked = statUncheckedObjects;
        }
        for (RepositoryNode node : checked) {
            saveProcess(node, paramName, Boolean.TRUE, monitor);
        }
        for (RepositoryNode node : unChecked) {
            saveProcess(node, paramName, Boolean.FALSE, monitor);
        }
    }

    private void save() {
        if (viewer == null) {
            return;
        }

        final IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor
                        .beginTask(
                                Messages.getString("StatLogsAndImplicitcontextTreeViewPage.SaveProjectSettings"), (checkedObjects.size() + statCheckedObjects.size()) * 100); //$NON-NLS-1$                

                saveChangedNode(EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName(), monitor);
                saveChangedNode(EParameterName.STATANDLOG_USE_PROJECT_SETTINGS.getName(), monitor);

                monitor.done();
            }
        };

        final ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
        try {
            dialog.run(true, true, runnable);
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
        }

    }

    private org.talend.designer.core.ui.editor.process.Process getProcess(List<IProcess> list, RepositoryNode p) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(p.getId())) {
                return (org.talend.designer.core.ui.editor.process.Process) list.get(i);
            }
        }
        return null;
    }

    private void exeCommand(final Process process, final Command cmd) {
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        if (display != null) {
            display.asyncExec(new Runnable() {

                public void run() {
                    process.getCommandStack().execute(cmd);
                }
            });
        } else {
            cmd.execute();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
