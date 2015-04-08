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
package org.talend.designer.runtime.visualization.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.designer.runtime.visualization.Activator;
import org.talend.designer.runtime.visualization.IActiveJvm;
import org.talend.designer.runtime.visualization.JvmCoreException;
import org.talend.designer.runtime.visualization.JvmModelEvent;
import org.talend.designer.runtime.visualization.JvmModelEvent.State;
import org.talend.designer.runtime.visualization.MBean.IMBeanServer;
import org.talend.designer.runtime.visualization.MBean.IMBeanServerChangeListener;
import org.talend.designer.runtime.visualization.MBean.IMonitoredMXBeanAttribute;
import org.talend.designer.runtime.visualization.MBean.IMonitoredMXBeanGroup;
import org.talend.designer.runtime.visualization.MBean.MBeanServerEvent;
import org.talend.designer.runtime.visualization.internal.ui.IHelpContextIds;
import org.talend.designer.runtime.visualization.internal.ui.RefreshJob;
import org.talend.designer.runtime.visualization.internal.ui.properties.memory.GarbageAction;
import org.talend.designer.runtime.visualization.internal.ui.properties.timeline.ConfigChartAction;
import org.talend.designer.runtime.visualization.internal.ui.properties.timeline.LoadChartAction;
import org.talend.designer.runtime.visualization.internal.ui.properties.timeline.Messages;
import org.talend.designer.runtime.visualization.internal.ui.properties.timeline.MonitorAttributeName;
import org.talend.designer.runtime.visualization.internal.ui.properties.timeline.TimelineChart;

/**
 * created by ldong on Apr 7, 2015 Detailled comment
 *
 */
public class RuntimeGraphcsComposite extends AbstractRuntimeGraphcsComposite {

    /** The charts. */
    private List<TimelineChart> charts;

    /** The action to load chart set. */
    private LoadChartAction loadChartSetAction;

    /** The action to run garbage collector. */
    private GarbageAction garbageCollectorAction;

    /** The MBean server change listener. */
    private IMBeanServerChangeListener mBeanServerChangeListener;

    /** The charts page. */
    private Composite chartsPage;

    /** The message page. */
    private Composite messagePage;

    /** The report text filed. */
    private Text reportField;

    private Composite reportComposite;

    public RuntimeGraphcsComposite(Composite parent, ISelection selection, int style) {
        super(parent, selection, style);
        charts = new ArrayList<TimelineChart>();
        loadChartSetAction = new LoadChartAction(this);
        garbageCollectorAction = new GarbageAction(this);
        setInput(selection);
    }

    @Override
    protected void refresh() {
        refreshConnectionIndicator();
        refreshReportField();

        IActiveJvm jvm = getJvm();
        if (jvm == null || !jvm.isConnected() || isRefreshSuspended() || chartsPage.isDisposed()) {
            return;
        }

        for (TimelineChart chart : charts) {
            if (!chart.isDisposed() && chart.isVisible()) {
                chart.refresh();
            }
        }
    }

    private void refreshReportField() {
        IActiveJvm jvm = getJvm();
        List<IMonitoredMXBeanGroup> groups = jvm.getMBeanServer().getMonitoredAttributeGroups();

        for (IMonitoredMXBeanGroup group : groups) {
            if (group.getName().equals(MonitorAttributeName.HEAP_MEMORY)) {
                Number useHeapSize = 0;
                Number maxHeapSize = 0;
                Date lastDate = null;
                for (IMonitoredMXBeanAttribute attribute : group.getAttributes()) {
                    if (attribute.getAttributeName().equals(MonitorAttributeName.HEAP_MEMORY_USE)) {
                        List<Number> values = attribute.getValues();
                        List<Date> dates = attribute.getDates();
                        if (values.size() > 1) {
                            useHeapSize = values.get(values.size() - 1);
                            lastDate = dates.get(dates.size() - 1);
                        }
                    } else {
                        List<Number> values = attribute.getValues();
                        List<Date> dates = attribute.getDates();
                        if (values.size() > 1) {
                            maxHeapSize = values.get(values.size() - 1);
                            lastDate = dates.get(dates.size() - 1);
                        }
                    }
                }
                if (lastDate != null) {
                    if (isRightPercentage(maxHeapSize, useHeapSize)) {
                        setNormalReport(lastDate);
                    } else {
                        setWarningReport(lastDate);
                    }
                }
            }
        }
    }

    private boolean isRightPercentage(Number max, Number use) {
        double fixedPercent = 0.8;
        boolean isRightMemmory = false;
        if (max instanceof Integer) {
            isRightMemmory = (Integer) max * fixedPercent > (Integer) use;
        } else if (max instanceof Long) {
            isRightMemmory = (Long) max * fixedPercent > (Long) use;
        } else if (max instanceof Double) {
            isRightMemmory = (Double) max * fixedPercent > (Double) use;
        }
        return isRightMemmory;
    }

    private void setWarningReport(Date lastDate) {
        StringBuilder finalContent = new StringBuilder();
        finalContent.append("Warning:the heap memory is nearly max size at ");
        finalContent.append(lastDate.toString());
        reportField.setText(finalContent.toString());
        reportField.setForeground(new Color(Display.getDefault(), 255, 0, 0));
        reportField.setTopIndex(Integer.MAX_VALUE);
    }

    private void setNormalReport(Date lastDate) {
        if (reportField != null && !reportField.isDisposed()) {
            StringBuilder finalContent = new StringBuilder();
            finalContent.append("Info:Heap memory is normal at ");
            finalContent.append(lastDate.toString());
            reportField.setText(finalContent.toString());
            reportField.setForeground(new Color(Display.getDefault(), 0, 0, 0));
            reportField.setTopIndex(Integer.MAX_VALUE);
        }
    }

    @Override
    public void jvmModelChanged(JvmModelEvent event) {
        super.jvmModelChanged(event);

        if (event.state == State.JvmConnected && !chartsPage.isDisposed()) {
            final IActiveJvm newJvm = (IActiveJvm) event.jvm;
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    reconstructCharts(newJvm, true);
                }
            });
            newJvm.getMBeanServer().addServerChangeListener(mBeanServerChangeListener);
        }
    }

    @Override
    protected void setInput(ISelection selection, IActiveJvm newJvm, IActiveJvm oldJvm) {
        if (newJvm == null || newJvm.equals(oldJvm)) {
            return;
        }

        reconstructCharts(newJvm, false);

        if (oldJvm != null && mBeanServerChangeListener != null) {
            IMBeanServer server = oldJvm.getMBeanServer();
            if (server != null) {
                server.removeServerChangeListener(mBeanServerChangeListener);
            }
        }

        newJvm.getMBeanServer().addServerChangeListener(mBeanServerChangeListener);
    }

    void reconstructCharts(IActiveJvm activeJvm, boolean connected) {
        if (chartsPage.isDisposed()) {
            return;
        }

        if (connected) {
            try {
                loadChartSetAction.loadDefaultChartSet();
            } catch (JvmCoreException e) {
                Activator.log(Messages.configureMonitoredAttributesFailedMsg, e);
            }
        }

        List<IMonitoredMXBeanGroup> groups = activeJvm.getMBeanServer().getMonitoredAttributeGroups();
        if (groups.size() == 0) {
            return;
        }

        // chartsPage.setVisible(false);
        for (TimelineChart chart : charts) {
            chart.dispose();
        }
        charts.clear();

        if (reportComposite != null && !reportComposite.isDisposed()) {
            reportComposite.dispose();
        }

        for (IMonitoredMXBeanGroup group : groups) {
            createSection(chartsPage, group);
        }

        createReportField();

        chartsPage.layout();
        chartsPage.setVisible(true);
        refresh();
    }

    private void createSection(Composite parent, IMonitoredMXBeanGroup group) {
        if (parent.isDisposed()) {
            return;
        }
        FormToolkit tookit = new FormToolkit(Display.getDefault());

        ExpandableComposite section = tookit.createSection(parent, ExpandableComposite.TITLE_BAR);
        section.setText(group.getName());
        FormLayout sectionLayout = new FormLayout();
        sectionLayout.marginWidth = 0;
        sectionLayout.marginHeight = 0;
        section.setLayout(sectionLayout);
        FormData sectionData = new FormData();
        if (group.getName().equals(MonitorAttributeName.HEAP_MEMORY)) {
            sectionData.left = new FormAttachment(0, 0);
            sectionData.right = new FormAttachment(50, 0);
            sectionData.top = new FormAttachment(0, 0);
            sectionData.bottom = new FormAttachment(50, -5);
        } else if (group.getName().equals(MonitorAttributeName.THREAD_COUNT)) {
            sectionData.left = new FormAttachment(50, 5);
            sectionData.right = new FormAttachment(100, -5);
            sectionData.top = new FormAttachment(50, 2);
            sectionData.bottom = new FormAttachment(100, 0);
        } else if (group.getName().equals(MonitorAttributeName.CPU_USE)) {
            sectionData.left = new FormAttachment(0, 0);
            sectionData.right = new FormAttachment(50, 0);
            sectionData.top = new FormAttachment(50, 2);
            sectionData.bottom = new FormAttachment(100, 0);
        }
        section.setLayoutData(sectionData);

        Composite flatFormComposite = createFlatFormComposite(section, tookit);
        FormLayout formLayout = new FormLayout();
        formLayout.marginHeight = 0;
        formLayout.marginWidth = 0;
        flatFormComposite.setLayout(formLayout);
        TimelineChart chart = new TimelineChart(flatFormComposite, section, group, SWT.NONE, toString() + getJvm().getPid());
        charts.add(chart);

        FormData data;
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        chart.setLayoutData(data);

        section.setClient(flatFormComposite);
        List<Action> actions = new ArrayList<Action>();
        for (IMonitoredMXBeanAttribute attribute : group.getAttributes()) {
            if (attribute.getAttributeName().startsWith("HeapMemoryUsage")) { //$NON-NLS-1$
                actions.add(garbageCollectorAction);
                break;
            }
        }
        actions.add(new ConfigChartAction(chart, this));
        addSectionActions(section, actions);
    }

    private Composite createFlatFormComposite(Composite section, FormToolkit tookit) {
        Composite flatFormComposite = createComposite(section, tookit);
        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        flatFormComposite.setLayout(layout);
        return flatFormComposite;
    }

    public Composite createComposite(Composite parent, FormToolkit tookit) {
        Composite c = tookit.createComposite(parent, SWT.NONE);
        tookit.paintBordersFor(c);
        return c;
    }

    @Override
    protected void createControls(Composite parent) {
        chartsPage = new Composite(parent, SWT.NULL);
        // messagePage = createMessagePage(parent);

        parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 0;
        formLayout.marginHeight = 0;
        chartsPage.setLayout(formLayout);

        FormData pageData = new FormData();
        pageData.top = new FormAttachment(0, 0);
        pageData.bottom = new FormAttachment(100, 0);
        pageData.left = new FormAttachment(0, 0);
        pageData.right = new FormAttachment(100, 0);

        chartsPage.setLayoutData(pageData);
        mBeanServerChangeListener = new IMBeanServerChangeListener() {

            @Override
            public void serverChanged(MBeanServerEvent event) {
                new RefreshJob(Messages.reconstructChartJobLabel, toString()) {

                    @Override
                    protected void refreshUI() {
                        IActiveJvm jvm = getJvm();
                        if (jvm != null) {
                            reconstructCharts(jvm, false);
                        }
                    }
                }.schedule();
            }
        };

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.TIMELINE_PAGE);
    }

    private Composite createMessagePage(Composite parent) {
        messagePage = new Composite(parent, SWT.NONE);
        messagePage.setLayout(new GridLayout(3, false));
        messagePage.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        FormToolkit toolkit = new FormToolkit(Display.getDefault());
        Hyperlink hyperlink = toolkit.createHyperlink(messagePage, Messages.loadDefaultChartSetLabel, SWT.NONE);
        hyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent event) {
                try {
                    new LoadChartAction(RuntimeGraphcsComposite.this).loadDefaultChartSet();
                } catch (JvmCoreException e) {
                    Activator.log(Messages.loadChartSetFailedMsg, e);
                }
            }
        });
        return messagePage;
    }

    private void createReportField() {
        reportComposite = createFlatFormComposite(chartsPage, new FormToolkit(Display.getDefault()));
        FormLayout reportLayout = new FormLayout();
        reportLayout.marginWidth = 0;
        reportLayout.marginHeight = 0;
        reportComposite.setLayout(reportLayout);
        FormData reportData = new FormData();
        reportData.left = new FormAttachment(50, 5);
        reportData.right = new FormAttachment(100, -5);
        reportData.top = new FormAttachment(0, 0);
        reportData.bottom = new FormAttachment(50, -5);
        reportComposite.setLayoutData(reportData);

        Group group = new Group(reportComposite, SWT.NULL);
        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 0;
        groupLayout.marginHeight = 0;
        group.setLayout(groupLayout);
        FormData groupData = new FormData();
        groupData.left = new FormAttachment(0, 0);
        groupData.right = new FormAttachment(100, 0);
        groupData.top = new FormAttachment(0, 0);
        groupData.bottom = new FormAttachment(100, 0);
        group.setLayoutData(groupData);
        group.setText("Runtime statics");

        reportField = new Text(group, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
        reportField.setEditable(false);
        reportField.setBackground(group.getBackground());

        FormData data;
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        reportField.setLayoutData(data);
    }

    private void refreshConnectionIndicator() {
        IActiveJvm jvm = getJvm();
        boolean isConnected = jvm != null && jvm.isConnected();
        refreshBackground(chartsPage, isConnected);
        for (TimelineChart chart : charts) {
            refreshBackground(chart, isConnected);
            refreshBackground(chart.getPlotArea(), isConnected);
            refreshBackground(chart.getSection(), isConnected);
        }
    }

    private static void addSectionActions(ExpandableComposite expandableComposite, List<Action> actions) {
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(expandableComposite);

        // set cursor
        final Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        toolbar.setCursor(cursor);
        toolbar.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (!cursor.isDisposed()) {
                    cursor.dispose();
                }
            }
        });

        // add menus
        for (Action action : actions) {
            toolBarManager.add(action);
        }
        toolBarManager.update(true);

        expandableComposite.setTextClient(toolbar);
    }

}
