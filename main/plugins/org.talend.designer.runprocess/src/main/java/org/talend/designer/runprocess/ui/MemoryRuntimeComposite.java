// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.properties.tab.IDynamicProperty;
import org.talend.designer.core.IMultiPageTalendEditor;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.views.ProcessView;
import org.talend.designer.runprocess.ui.views.ProcessViewSelectionProvider;
import org.talend.designer.runtime.visualization.Activator;
import org.talend.designer.runtime.visualization.IActiveJvm;
import org.talend.designer.runtime.visualization.IHost;
import org.talend.designer.runtime.visualization.JvmCoreException;
import org.talend.designer.runtime.visualization.JvmModel;
import org.talend.designer.runtime.visualization.views.RuntimeGraphcsComposite;

/**
 * created by ldong on Apr 3, 2015 Detailled comment
 *
 */
public class MemoryRuntimeComposite extends ScrolledComposite implements IDynamicProperty {

    private Button runtimeButton;

    private Combo periodCombo;

    private RunProcessContext processContext;

    private ProcessManager processManager;

    private ProcessView viewPart;

    private IActiveJvm currentJvm;

    public MemoryRuntimeComposite(ProcessView viewPart, Composite parent, RunProcessContext processContext, int style) {
        super(parent, style);
        this.viewPart = viewPart;
        this.processContext = processContext;
        this.processManager = ProcessManager.getInstance();
        init(parent);
        // CSS
        CoreUIPlugin.setCSSClass(this, this.getClass().getSimpleName());
    }

    private void init(Composite parent) {
        if (processContext != null && processContext.isRunning()) {
            initCurrentActiveJobJvm();
            initMonitoringModel();
            initGraphicComponents(parent);
        } else {
            MessageDialog.openWarning(getShell(), "Warning", Messages.getString("ProcessView.noJobRunning")); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

    private IActiveJvm initCurrentActiveJobJvm() {
        JvmModel jvmModel = JvmModel.getInstance();
        String jobClassName = JavaResourcesHelper.getJobClassName(processContext.getProcess());
        List<IActiveJvm> activateJvms = jvmModel.getHost(IHost.LOCALHOST).getActiveJvms();
        for (IActiveJvm jvm : activateJvms) {
            if (jvm.getMainClass().equals(jobClassName)) {
                currentJvm = jvm;
                break;
            }
        }
        return currentJvm;
    }

    private void initMonitoringModel() {
        final List<IActiveJvm> contructSelections = new ArrayList<IActiveJvm>();
        contructSelections.add(currentJvm);
        if (currentJvm != null) {
            viewPart.getSite()
                    .setSelectionProvider(new ProcessViewSelectionProvider(new StructuredSelection(contructSelections)));
        }
    }

    private void initRuntimeGraphs(final Composite parent) {
        if (viewPart.getSite().getSelectionProvider() != null) {
            setExpandHorizontal(true);
            setExpandVertical(true);
            FormData layouData = new FormData();
            layouData.left = new FormAttachment(0, 0);
            layouData.right = new FormAttachment(100, 0);
            layouData.top = new FormAttachment(0, 0);
            layouData.bottom = new FormAttachment(100, 0);
            this.setBackground(parent.getBackground());
            this.setLayout(new FormLayout());
            setLayoutData(layouData);

            RuntimeGraphcsComposite rgc = new RuntimeGraphcsComposite(this, viewPart.getSelection(), SWT.NULL);
            setContent(rgc);

            FormLayout panelLayout = new FormLayout();
            panelLayout.marginWidth = 5;
            panelLayout.marginHeight = 5;
            panelLayout.spacing = 5;
            rgc.setLayout(panelLayout);
        } else {
            MessageDialog.openWarning(getShell(), "Warning", Messages.getString("ProcessView.monitorNotStart")); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

    private void connectToJvm() {
        if (currentJvm != null && !currentJvm.isConnected() && isEnabled()) {
            try {
                currentJvm.connect(1000);
            } catch (JvmCoreException e) {
                Activator.log(NLS.bind("Jvm failed", currentJvm.getPid()), e);
            }
        }
    }

    /**
     * DOC amaumont Comment method "initGraphicComponents".
     * 
     * @param parent
     */
    private void initGraphicComponents(Composite parent) {
        initRuntimeGraphs(parent);
        connectToJvm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getComposite()
     */
    @Override
    public Composite getComposite() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getCurRowSize()
     */
    @Override
    public int getCurRowSize() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getElement()
     */
    @Override
    public Element getElement() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getHashCurControls()
     */
    @Override
    public BidiMap getHashCurControls() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getPart()
     */
    @Override
    public IMultiPageTalendEditor getPart() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.properties.tab.IDynamicProperty#getRepositoryAliasName(org.talend.core.model.properties.
     * ConnectionItem)
     */
    @Override
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    @Override
    public EComponentCategory getSection() {
        return EComponentCategory.MEMORYRUN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbSchemaMap()
     */
    @Override
    public Map<String, String> getTableIdAndDbSchemaMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbTypeMap()
     */
    @Override
    public Map<String, String> getTableIdAndDbTypeMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#refresh()
     */
    @Override
    public void refresh() {
        if (!isDisposed()) {
            getParent().layout();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#setCurRowSize(int)
     */
    @Override
    public void setCurRowSize(int i) {

    }

}
