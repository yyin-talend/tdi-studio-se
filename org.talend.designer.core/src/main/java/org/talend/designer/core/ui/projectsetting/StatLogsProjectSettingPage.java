// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.editor.cmd.LoadProjectSettingsCommand;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;
import org.talend.designer.core.ui.views.properties.WidgetFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.preference.ProjectSettingPage;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider;

/**
 * DOC aimingchen class global comment. Detailled comment
 */
public class StatLogsProjectSettingPage extends ProjectSettingPage {

    private MultipleThreadDynamicComposite mComposite;

    private Element elem;

    private List<IRepositoryObject> processList = new ArrayList<IRepositoryObject>();

    private List<IProcess> openedProcessList = new ArrayList<IProcess>();

    private List<RepositoryNode> statCheckedNode = new ArrayList<RepositoryNode>();

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    WidgetFactory widgetFactory = new WidgetFactory();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = widgetFactory.createComposite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        FormData data = createFormData();
        composite.setLayoutData(data);
        if (pro.getEmfProject().getStatAndLogsSettings() == null) {
            // display one message box to tell the user the settings is get from old preference page.
            MessageDialog.openInformation(getShell(), getTitle(), Messages.getString("StatLogs.LoadOldPreferences")); //$NON-NLS-1$
        }
        elem = ProjectSettingManager.createStatsAndLogsElement(pro);
        StatAndLogsSettings stats = pro.getEmfProject().getStatAndLogsSettings();
        ElementParameter2ParameterType.loadElementParameters(elem, stats.getParameters(), EParameterName.PROPERTY_TYPE.getName()
                + ":" + EParameterName.PROPERTY_TYPE.getName());
        // create StatsAndLogs control base on the statsAndLogsElement
        mComposite = new MultipleThreadDynamicComposite(composite, SWT.V_SCROLL | SWT.BORDER, EComponentCategory.STATSANDLOGS,
                elem, true);
        mComposite.setLayoutData(createFormData());
        return composite;
    }

    @Override
    public void dispose() {
        if (widgetFactory != null)
            widgetFactory.dispose();
        super.dispose();
    }

    private FormData createFormData() {
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        save();
        Shell activeShell = Display.getCurrent().getActiveShell();
        if (activeShell == null) {
            activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        }
        ProgressDialog progressDialog = new ProgressDialog(activeShell, 0) {

            private IProgressMonitor monitorWrap;

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                monitorWrap = new EventLoopProgressMonitor(monitor);
                monitorWrap.beginTask("Use Project Settings ...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$ 

                ParametersType parameters = null;
                if (mComposite != null) {
                    // save the Element's parameters to EMF model
                    Element elem = pro.getStatsAndLog();
                    StatAndLogsSettings stats = pro.getEmfProject().getStatAndLogsSettings();
                    if (stats != null) {
                        parameters = stats.getParameters();
                        if (parameters != null && !"".equals(parameters)) {
                            // save to the memory
                            ElementParameter2ParameterType.saveElementParameters(elem, parameters);
                        }
                    }
                    ProjectSettingManager.saveProject();

                }
                monitorWrap.worked(20);
            }

        };
        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
        }

        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        save();
        performOk();
        super.performApply();
    }

    private void save() {
        List<String> statCheckedObjects = new ArrayList<String>();
        IRepositoryView repositoryView;
        repositoryView = RepositoryManager.getRepositoryView();
        AllJobContentProvider statContentProvider = new AllJobContentProvider(repositoryView);
        RepositoryNode[] nodes = statContentProvider.getContents();
        List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
        for (RepositoryNode n : nodes) {
            processItems(objects, n);
            for (RepositoryNode node : objects) {
                if (isStatUseProjectSetting(node)) {
                    if (!statCheckedObjects.contains(node.getObject().getProperty().getId())) {
                        statCheckedObjects.add(node.getObject().getProperty().getId());
                        statCheckedNode.add(node);
                    }
                }
            }
        }
        if (statCheckedObjects == null) {
            return;
        }
        List<IProcess> allOpenedProcessList = CorePlugin.getDefault().getDesignerCoreService().getOpenedProcess(getEditors());
        if (allOpenedProcessList != null && statCheckedObjects != null) {
            for (int i = 0; i < allOpenedProcessList.size(); i++) {
                if (statCheckedObjects.contains(allOpenedProcessList.get(i).getProperty().getId())) {
                    openedProcessList.add(allOpenedProcessList.get(i));
                }
            }
        }
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
        List<IRepositoryObject> allProcessList;
        try {
            allProcessList = factory.getAll(ERepositoryObjectType.PROCESS, true);
            if (allProcessList == null) {
                allProcessList = new ArrayList<IRepositoryObject>();
            }
            if (statCheckedObjects != null) {
                for (IRepositoryObject repositoryObject : allProcessList) {
                    if (statCheckedObjects.contains(repositoryObject.getProperty().getId())) {
                        processList.add(repositoryObject);
                    }
                }
            }
        } catch (PersistenceException e1) {
            ExceptionHandler.process(e1);
        }

        final IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor
                        .beginTask(
                                Messages.getString("StatLogsAndImplicitcontextTreeViewPage.SaveProjectSettings"), (processList.size()) * 100); //$NON-NLS-1$                
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

    private void saveChangedNode(String paramName, IProgressMonitor monitor) {
        for (RepositoryNode node : statCheckedNode) {
            saveProcess(node, paramName, Boolean.TRUE, monitor);
        }
    }

    private boolean isOpenProcess(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        if (property.getItem() instanceof ProcessItem) {
            for (IProcess process : openedProcessList) {
                if (process.getId().equals(property.getId()) && process.getLabel().equals(property.getLabel())
                        && process.getVersion().equals(property.getVersion())) {
                    return true;
                }
            }
        }
        return false;
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

    private void saveProcess(RepositoryNode node, String paramName, Boolean isUseProjectSettings, IProgressMonitor monitor) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();
        if (isOpenProcess(node)) {
            Process process = getProcess(openedProcessList, node);

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

    public static IEditorReference[] getEditors() {
        final List<IEditorReference> list = new ArrayList<IEditorReference>();
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getEditorReferences();
                list.addAll(Arrays.asList(reference));
            }
        });
        return list.toArray(new IEditorReference[0]);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        if (mComposite != null) {
            StatAndLogsSettings sal = pro.getEmfProject().getStatAndLogsSettings();
            ElementParameter2ParameterType.loadElementParameters(elem, sal.getParameters(), EParameterName.PROPERTY_TYPE
                    .getName()
                    + ":" + EParameterName.PROPERTY_TYPE.getName());
            mComposite.refresh();
        }

    }
}
