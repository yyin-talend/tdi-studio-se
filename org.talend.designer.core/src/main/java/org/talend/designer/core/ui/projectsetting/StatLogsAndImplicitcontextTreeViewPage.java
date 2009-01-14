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
package org.talend.designer.core.ui.projectsetting;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.update.UpdateManagerUtils;
import org.talend.designer.core.ui.views.properties.WidgetFactory;
import org.talend.designer.joblet.model.JobletProcess;
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

    private CheckboxRepositoryTreeViewer viewer;

    private AllJobContentProvider contentProvider;

    private IRepositoryView repositoryView;

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private List<RepositoryNode> checkedObjects = new ArrayList<RepositoryNode>();

    private List<RepositoryNode> initcheckedObjects = new ArrayList<RepositoryNode>();

    private List<RepositoryNode> inituncheckedObjects = new ArrayList<RepositoryNode>();

    private List<RepositoryNode> uncheckedObjects = new ArrayList<RepositoryNode>();

    private IDesignerCoreService coreService = CorePlugin.getDefault().getDesignerCoreService();

    final List<IProcess> opendProcess = UpdateManagerUtils.getOpenedProcess();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new WidgetFactory().createComposite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        viewer = new CheckboxRepositoryTreeViewer(composite, SWT.MULTI | SWT.V_SCROLL);

        repositoryView = RepositoryManager.getRepositoryView();
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
        initcheckedObjects.addAll(checkedObjects);
        inituncheckedObjects.addAll(uncheckedObjects);

        viewer.setCheckedElements(checkedObjects.toArray());
        return composite;
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

    private boolean isUseProjectSetting(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();

        String implictB = ElementParameter2ParameterType.getParameterValue(pType,
                EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName());
        String statB = ElementParameter2ParameterType.getParameterValue(pType, EParameterName.STATANDLOG_USE_PROJECT_SETTINGS
                .getName());
        if (implictB != null && statB != null && "true".equals(statB) && "true".equals(implictB)) {
            return true;
        } else {
            return false;
        }
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
            return (RepositoryNode[]) systemFolders.getRootRepositoryNode(ERepositoryObjectType.PROCESS).getChildren().toArray(
                    new RepositoryNode[0]);
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

    private void saveProcess(RepositoryNode node, Boolean value, IProgressMonitor monitor) {
        Property property = node.getObject().getProperty();
        org.talend.designer.core.ui.editor.process.Process process = (org.talend.designer.core.ui.editor.process.Process) coreService
                .getProcessFromItem(property.getItem());

        if (opendProcess.contains(process)) {
            process = getProcess(opendProcess, process);
            PropertyChangeCommand cmd = new PropertyChangeCommand(process, EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS
                    .getName(), value);
            PropertyChangeCommand cmd1 = new PropertyChangeCommand(process, EParameterName.STATANDLOG_USE_PROJECT_SETTINGS
                    .getName(), value);
            exeCommand(cmd, cmd1);
        } else {
            process.loadXmlFile();
            ElementParameter2ParameterType.setParameterValue(process, EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS
                    .getName(), value);
            ElementParameter2ParameterType.setParameterValue(process, EParameterName.STATANDLOG_USE_PROJECT_SETTINGS.getName(),
                    value);
            try {
                ProcessType processType = process.saveXmlFile();
                Item item = process.getProperty().getItem();
                if (item instanceof JobletProcessItem) {
                    ((JobletProcessItem) item).setJobletProcess((JobletProcess) processType);
                } else {
                    ((ProcessItem) item).setProcess(processType);
                }
                factory.save(item);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        RepositoryManager.refreshSavedNode(node);
        monitor.worked(100);
    }

    private void saveChangedNode(IProgressMonitor monitor) {
        for (RepositoryNode node : initcheckedObjects) {
            if (uncheckedObjects.contains(node)) {
                saveProcess(node, Boolean.FALSE, monitor);
            }
        }
        for (RepositoryNode node : inituncheckedObjects) {
            if (checkedObjects.contains(node)) {
                saveProcess(node, Boolean.TRUE, monitor);
            }
        }
    }

    private void save() {
        if (viewer == null) {
            return;
        }
        final IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("", checkedObjects.size() * 100); //$NON-NLS-1$                
                saveChangedNode(monitor);
                monitor.done();
            }
        };

        final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(null);
        try {
            dialog.run(false, false, runnable);
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
        }

    }

    private org.talend.designer.core.ui.editor.process.Process getProcess(List<IProcess> list,
            org.talend.designer.core.ui.editor.process.Process p) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(p)) {
                return (org.talend.designer.core.ui.editor.process.Process) list.get(i);
            }
        }
        return null;
    }

    private void exeCommand(Command... cmd) {
        IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        for (IEditorReference activeEditor : editors) {
            if (activeEditor != null) {
                IEditorPart editp = activeEditor.getEditor(false);
                if (editp instanceof AbstractMultiPageTalendEditor) {
                    AbstractTalendEditor workbenchPart = ((AbstractMultiPageTalendEditor) editp).getTalendEditor();
                    for (Command c : cmd) {
                        workbenchPart.getCommandStack().execute(c);
                    }
                }
            }
        }
    }

}
