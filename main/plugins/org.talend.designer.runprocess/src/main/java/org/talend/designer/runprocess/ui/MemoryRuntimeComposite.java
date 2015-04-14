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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.runtime.image.ImageProvider;
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
import org.talend.designer.runtime.visualization.views.AbstractRuntimeGraphcsComposite;
import org.talend.designer.runtime.visualization.views.RuntimeGraphcsComposite;

/**
 * created by ldong on Apr 3, 2015 Detailled comment
 *
 */
public class MemoryRuntimeComposite extends ScrolledComposite implements IDynamicProperty {

    private Button runtimeButton;

    private Combo periodCombo;

    private RunProcessContext processContext;

    private ProcessView viewPart;

    private IActiveJvm currentJvm;

    private Composite monitorComposite;

    private AbstractRuntimeGraphcsComposite chartComposite;

    private PropertyChangeListener propertyChangeListener;

    public MemoryRuntimeComposite(ProcessView viewPart, Composite parent, RunProcessContext processContext, int style) {
        super(parent, style);
        this.viewPart = viewPart;
        this.processContext = processContext;
        init(parent);
        // CSS
        CoreUIPlugin.setCSSClass(this, this.getClass().getSimpleName());
    }

    private void init(final Composite parent) {
        initGraphicComponents(parent);
        addListeners();
    }

    /**
     * DOC ldong Comment method "initGraphicComponents".
     * 
     * @param parent
     */
    private void initGraphicComponents(Composite parent) {
        initRuntimeGraphs(parent);
        connectToJvm();
    }

    private void initRuntimeGraphs(final Composite parent) {
        ISelection selection = viewPart.getSite().getSelectionProvider() == null ? null : viewPart.getSelection();
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

        monitorComposite = new Composite(this, SWT.NULL);
        FormData baseData = new FormData();
        baseData.left = new FormAttachment(0, 0);
        baseData.right = new FormAttachment(100, 0);
        baseData.top = new FormAttachment(0, 0);
        baseData.bottom = new FormAttachment(100, 0);
        monitorComposite.setLayout(new FormLayout());
        monitorComposite.setLayoutData(layouData);
        setContent(monitorComposite);

        Group topGroup = createTopGroup(monitorComposite);
        runtimeButton = new Button(topGroup, SWT.PUSH);
        runtimeButton.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
        runtimeButton.setText("run"); //$NON-NLS-1$
        FormData runButtonData = new FormData();
        Point execSize = null;
        runButtonData.left = new FormAttachment(0, 10);

        execSize = computeSize(runtimeButton.getText());
        runButtonData.right = new FormAttachment(0, execSize.x + 70);
        runButtonData.height = 25;
        runtimeButton.setLayoutData(runButtonData);

        Label periodLabel = new Label(topGroup, SWT.NULL);
        periodLabel.setText(Messages.getString("ProcessView.moniorPeriod")); //$NON-NLS-1$
        periodLabel.setBackground(getBackground());
        FormData periodLabelData = new FormData();
        periodLabelData.left = new FormAttachment(runtimeButton, 20, SWT.RIGHT);
        periodLabelData.right = new FormAttachment(runtimeButton, 220, SWT.RIGHT);
        periodLabelData.top = new FormAttachment(0, 5);
        periodLabelData.bottom = new FormAttachment(100, 0);
        periodLabelData.height = 30;
        periodLabel.setLayoutData(periodLabelData);

        periodCombo = new Combo(topGroup, SWT.READ_ONLY);
        FormData periodData = new FormData();
        periodData.left = new FormAttachment(periodLabel, 10, SWT.RIGHT);
        periodData.right = new FormAttachment(periodLabel, 100, SWT.RIGHT);
        periodData.height = 25;
        periodCombo.setLayoutData(periodData);
        periodCombo.setItems(new String[] { "30 seconds", "60 seconds", "120 seconds" });
        periodCombo.select(0);

        chartComposite = new RuntimeGraphcsComposite(monitorComposite, selection, SWT.NULL);
        FormLayout rgcLayout = new FormLayout();
        FormData charLayData = new FormData();
        charLayData.left = new FormAttachment(0, 10);
        charLayData.right = new FormAttachment(100, 0);
        charLayData.top = new FormAttachment(20, 5);
        charLayData.bottom = new FormAttachment(100, 0);
        chartComposite.setLayout(rgcLayout);
        chartComposite.setLayoutData(charLayData);
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

    private Group createTopGroup(Composite parent) {
        Composite topComposite = new Composite(parent, SWT.NULL);
        FormLayout topLayout = new FormLayout();
        FormData topData = new FormData();
        topData.left = new FormAttachment(0, 10);
        topData.right = new FormAttachment(100, 0);
        topData.top = new FormAttachment(0, 0);
        topComposite.setLayout(topLayout);
        topComposite.setLayoutData(topData);

        Group topGroup = new Group(topComposite, SWT.NULL);
        topGroup.setText("Monitor Control"); //$NON-NLS-1$
        FormLayout groupLayout = new FormLayout();
        groupLayout.marginHeight = 2;
        groupLayout.marginWidth = 2;
        groupLayout.spacing = 7;
        FormData groupData = new FormData();
        groupData.left = new FormAttachment(0, 0);
        groupData.right = new FormAttachment(100, 0);
        groupData.top = new FormAttachment(0, 0);
        groupData.bottom = new FormAttachment(100, 0);
        topGroup.setLayout(groupLayout);
        topGroup.setLayoutData(groupData);

        return topGroup;
    }

    private Point computeSize(String text) {
        GC gc = new GC(runtimeButton.getDisplay());
        final Point p = gc.textExtent(text);
        gc.dispose();
        return p;
    }

    private void addListeners() {
        runtimeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (processContext != null && processContext.isRunning()) {
                    initCurrentActiveJobJvm();
                    initMonitoringModel();
                    refreshMonitorComposite();
                    processContext.setMonitoring(true);
                    runtimeButton.setEnabled(!processContext.isMonitoring());
                } else {
                    MessageDialog.openWarning(getShell(), "Warning", Messages.getString("ProcessView.noJobRunning")); //$NON-NLS-1$//$NON-NLS-2$
                }

            }
        });

        propertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                runProcessContextChanged(evt);
            }
        };

    }

    private void initCurrentActiveJobJvm() {
        JvmModel jvmModel = JvmModel.getInstance();
        String jobClassName = JavaResourcesHelper.getJobClassName(processContext.getProcess());
        List<IActiveJvm> activateJvms = jvmModel.getHost(IHost.LOCALHOST).getActiveJvms();
        for (IActiveJvm jvm : activateJvms) {
            if (jvm.getMainClass().equals(jobClassName)) {
                currentJvm = jvm;
                break;
            }
        }
    }

    private void initMonitoringModel() {
        final List<IActiveJvm> contructSelections = new ArrayList<IActiveJvm>();
        contructSelections.add(currentJvm);
        if (currentJvm != null) {
            viewPart.getSite()
                    .setSelectionProvider(new ProcessViewSelectionProvider(new StructuredSelection(contructSelections)));
        }
    }

    private void refreshMonitorComposite() {
        if (monitorComposite != null && !monitorComposite.isDisposed()) {
            monitorComposite.dispose();
        }
        initGraphicComponents(this);
        monitorComposite.layout();
    }

    private void runProcessContextChanged(final PropertyChangeEvent evt) {
        if (isDisposed()) {
            return;
        }
        String propName = evt.getPropertyName();

        if (RunProcessContext.MEMORY_MONITOR.equals(propName)) {
            getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    if (isDisposed()) {
                        return;
                    }
                    boolean monitoring = ((Boolean) evt.getNewValue()).booleanValue();
                    if (runtimeButton != null && !runtimeButton.isDisposed()) {
                        runtimeButton.setEnabled(!monitoring);
                    }
                    if (!monitoring && chartComposite != null && chartComposite.isDisposed()) {
                        viewPart.setSelection(null);
                        refreshMonitorComposite();
                    }
                }
            });
        }

    }

    public void setProcessContext(RunProcessContext processContext) {

        if (this.processContext != null) {
            this.processContext.removePropertyChangeListener(propertyChangeListener);
        }
        this.processContext = processContext;
        if (processContext != null) {
            processContext.addPropertyChangeListener(propertyChangeListener);
        }
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
