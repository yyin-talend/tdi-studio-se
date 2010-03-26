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
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.LoadProjectSettingsCommand;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;
import org.talend.designer.core.ui.views.properties.WidgetFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.UpdateRepositoryUtils;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.preference.ProjectSettingPage;

/**
 * DOC aimingchen class global comment. Detailled comment
 */
public class ImplicitContextLoadProjectSettingPage extends ProjectSettingPage {

    private MultipleThreadDynamicComposite mComposite;

    private Element elem;

    WidgetFactory widgetFactory = new WidgetFactory();

    private List<RepositoryNode> implicitCheckedNodes = new ArrayList<RepositoryNode>();

    private List<IProcess> openedProcessList = new ArrayList<IProcess>();

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

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
        if (pro.getEmfProject().getImplicitContextSettings() == null) {
            // display one message box to tell the user the settings is get from old preference page.
            MessageDialog.openInformation(getShell(), getTitle(), Messages.getString("ImplicitContextLoad.LoadOldPreferences")); //$NON-NLS-1$
        }
        elem = ProjectSettingManager.createImplicitContextLoadElement(pro);
        ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
        ElementParameter2ParameterType.loadElementParameters(elem, implicit.getParameters(), JobSettingsConstants
                .getExtraParameterName(EParameterName.PROPERTY_TYPE.getName())
                + ":" + EParameterName.PROPERTY_TYPE.getName());
        // create implicitContextLoad Control base on the ImplicitContextLoadElement
        mComposite = new MultipleThreadDynamicComposite(composite, SWT.V_SCROLL | SWT.BORDER, EComponentCategory.EXTRA, elem,
                true);
        mComposite.setLayoutData(createFormData());
        return composite;
    }

    private FormData createFormData() {
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        return data;
    }

    @Override
    public void dispose() {
        if (widgetFactory != null)
            widgetFactory.dispose();
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {

        ImplicitContextSettings implicit = null;
        ParametersType parameters = null;
        if (mComposite != null) {
            // save the Element's parameters to EMF model
            Element elem = pro.getInitialContextLoad();
            implicit = pro.getEmfProject().getImplicitContextSettings();
            if (implicit != null) {
                parameters = implicit.getParameters();
                if (parameters != null && !"".equals(parameters)) {
                    // save to the memory
                    ElementParameter2ParameterType.saveElementParameters(elem, parameters);
                }
            }
            ProjectSettingManager.saveProject();
        }

        save();
        // if (parameters != null) {
        // ElementParameter2ParameterType.loadProjectsettingsParameters(parameters);
        // }
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        performOk();
        super.performApply();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        if (mComposite != null) {
            ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
            ElementParameter2ParameterType.loadElementParameters(elem, implicit.getParameters(), JobSettingsConstants
                    .getExtraParameterName(EParameterName.PROPERTY_TYPE.getName())
                    + ":" + EParameterName.PROPERTY_TYPE.getName());
            mComposite.refresh();
        }

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

    private boolean isStatUseProjectSetting(RepositoryNode node) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();

        String statB = ElementParameter2ParameterType.getParameterValue(pType,
                EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName());
        if (statB != null && "true".equals(statB)) { //$NON-NLS-1$
            return true;
        } else {
            return false;
        }
    }

    private void save() {
        List<String> implicitCheckedObjects = new ArrayList<String>();
        ProjectRepositoryNode root = (ProjectRepositoryNode) RepositoryManager.getRepositoryView().getRoot();
        RepositoryNode processNode = root.getProcessNode();
        List<RepositoryNode> objects = new ArrayList<RepositoryNode>();
        processItems(objects, processNode);
        for (RepositoryNode node : objects) {
            if (isStatUseProjectSetting(node)) {
                if (!implicitCheckedObjects.contains(node.getObject().getProperty().getId())) {
                    implicitCheckedObjects.add(node.getObject().getProperty().getId());
                    implicitCheckedNodes.add(node);
                }
            }
        }

        List<IProcess> allOpenedProcessList = CorePlugin.getDefault().getDesignerCoreService().getOpenedProcess(getEditors());
        if (allOpenedProcessList != null) {
            for (int i = 0; i < allOpenedProcessList.size(); i++) {
                if (implicitCheckedObjects.contains(allOpenedProcessList.get(i).getProperty().getId())) {
                    openedProcessList.add(allOpenedProcessList.get(i));
                }
            }
        }

        final IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor
                        .beginTask(
                                Messages.getString("StatLogsAndImplicitcontextTreeViewPage.SaveProjectSettings"), (implicitCheckedNodes.size()) * 100); //$NON-NLS-1$                
                saveChangedNode(EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName(), monitor);
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
        for (RepositoryNode node : implicitCheckedNodes) {
            saveProcess(node, paramName, monitor);
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

    // save project setting params to process which actived use project setting param
    private void saveProcess(RepositoryNode node, String paramName, IProgressMonitor monitor) {
        Property property = node.getObject().getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        ParametersType pType = pItem.getProcess().getParameters();
        if (isOpenProcess(node)) {
            Process process = getProcess(openedProcessList, node);
            LoadProjectSettingsCommand command = new LoadProjectSettingsCommand(process, paramName, Boolean.TRUE);
            exeCommand(process, command);

            String id = (String) process.getElementParameter(
                    EParameterName.PROPERTY_TYPE.getName() + ":" + EParameterName.REPOSITORY_PROPERTY_TYPE.getName()) //$NON-NLS-1$
                    .getValue();
            String propertyType = EParameterName.PROPERTY_TYPE.getName() + ":"
                    + EParameterName.REPOSITORY_PROPERTY_TYPE.getName();
            ConnectionItem connectionItem = UpdateRepositoryUtils.getConnectionItemByItemId(id);
            Connection connection = connectionItem.getConnection();
            ChangeValuesFromRepository cmd = new ChangeValuesFromRepository(process, connection, propertyType, id);
            cmd.ignoreContextMode(true);
            exeCommand(process, cmd);

            monitor.worked(100);
        } else {
            try {
                ProjectSettingManager.reloadImplicitValuesFromProjectSettings(pType, ProjectManager.getInstance()
                        .getCurrentProject());
                factory.save(pItem);
                monitor.worked(100);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

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

}
