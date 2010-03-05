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
package org.talend.designer.runprocess.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessContextManager;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.ProcessComposite;
import org.talend.designer.runprocess.ui.actions.ClearPerformanceAction;

/**
 * View showing the execution of a process. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessView extends ViewPart {

    public static final String ID = RunProcessPlugin.PLUGIN_ID + ".ui.views.processview"; //$NON-NLS-1$

    public static final int EXEC_ID = 21;

    public static final int PAUSE_ID = 22;

    public static final int RESUME_ID = 23;

    public static final int DEBUG_ID = 24;

    private static Logger log = Logger.getLogger(ProcessView.class);

    private Label processNameLab;

    private ProcessComposite processComposite;

    private PropertyChangeListener contextManagerListener;

    private ClearPerformanceAction clearPerfAction;

    public RunAction runAction;

    private IProcessViewHelper processViewHelper;

    public static ProcessView findProcessView() {
        try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            return (ProcessView) page.findView(ID);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Constructs a new ProcessView.
     */
    public ProcessView() {
        super();
        IExtensionPointLimiter extensionPointLimiter = new ExtensionPointLimiterImpl(
                "org.talend.designer.runprocess.runprocess_view_helper", "runprocess_view_helper"); //$NON-NLS-1$ //$NON-NLS-2$

        IProcessViewHelper processViewHelperPrm = ExtensionImplementationProvider.getSingleInstance(extensionPointLimiter, null);
        if (processViewHelperPrm == null) {
            processViewHelperPrm = new DefaultProcessViewHelper();
        }

        setProcessViewHelper(processViewHelperPrm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 2;
        container.setLayout(layout);

        processNameLab = new Label(container, SWT.NULL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.verticalIndent = 2;
        // data.horizontalAlignment = SWT.CENTER;
        processNameLab.setLayoutData(data);
        processNameLab.setAlignment(SWT.CENTER);
        FontData[] fds = processNameLab.getFont().getFontData();

        for (FontData fd : fds) {
            fd.setHeight(fd.getHeight() + 2);
            fd.setStyle(fd.getStyle() | SWT.BOLD);
        }

        Font titleFont = new Font(processNameLab.getDisplay(), fds);
        processNameLab.setFont(titleFont);
        processNameLab.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));

        processComposite = this.processViewHelper.getProcessComposite(container);
        processComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        // fillActionBars();

        contextManagerListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (RunProcessContextManager.PROP_ACTIVE.equals(evt.getPropertyName())) {
                    runningProcessChanged();
                }
            }
        };
        RunProcessPlugin.getDefault().getRunProcessContextManager().addPropertyChangeListener(contextManagerListener);

        runningProcessChanged();

        IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);

        runAction = new RunAction();
        // IHandler handler2 = new ActionHandler(runAction);
        // handlerService.activateHandler(runAction.getActionDefinitionId(), handler2);

        IHandler handler1;
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (brandingService.getBrandingConfiguration().isAllowDebugMode()) {
            Action debugAction = new DebugAction();
            handler1 = new ActionHandler(debugAction);
            handlerService.activateHandler(debugAction.getActionDefinitionId(), handler1);
        }
        Action killAction = new KillAction();
        handler1 = new ActionHandler(killAction);
        handlerService.activateHandler(killAction.getActionDefinitionId(), handler1);

        FocusListener fl = new FocusListener() {

            public void focusGained(FocusEvent e) {
                log.trace(Messages.getString("ProcessView.gainFocusLog")); //$NON-NLS-1$
                IContextService contextService = (IContextService) RunProcessPlugin.getDefault().getWorkbench().getAdapter(
                        IContextService.class);
                ca = contextService.activateContext("talend.runProcess"); //$NON-NLS-1$
            }

            public void focusLost(FocusEvent e) {
                log.trace(Messages.getString("ProcessView.lostFocusLog")); //$NON-NLS-1$
                if (ca != null) {
                    IContextService contextService = (IContextService) RunProcessPlugin.getDefault().getWorkbench().getAdapter(
                            IContextService.class);
                    contextService.deactivateContext(ca);
                }
            }
        };

        addListenerOnChildren(container, fl);
    }

    public ProcessComposite getProcessComposite() {
        return processComposite;
    }

    private void addListenerOnChildren(Control parent, FocusListener focusListener) {
        parent.addFocusListener(focusListener);
        if (parent instanceof Composite) {
            for (Control child : ((Composite) parent).getChildren()) {
                addListenerOnChildren(child, focusListener);
            }
        }
    }

    private IContextActivation ca;

    private void fillActionBars() {
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();

        clearPerfAction = new ClearPerformanceAction();
        menuManager.add(clearPerfAction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        if (contextManagerListener != null) {
            RunProcessPlugin.getDefault().getRunProcessContextManager().removePropertyChangeListener(contextManagerListener);
            contextManagerListener = null;
        }
        processComposite.dispose();
        processComposite = null;
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        processComposite.setFocus();

        // IContextService contextService = (IContextService) RunProcessPlugin.getDefault().getWorkbench().getAdapter(
        // IContextService.class);
        // contextService.activateContext("talend.runProcess");
    }

    private void runningProcessChanged() {
        refresh();
    }

    public void refresh() {
        RunProcessContext activeContext = RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext();
        // clearPerfAction.setProcess(activeContext != null ? activeContext.getProcess() : null);
        processComposite.setProcessContext(activeContext);

        if (activeContext != null) {
            String jobName = Messages.getString("ProcessView.jobName"); //$NON-NLS-1$
            if (activeContext.getProcess().disableRunJobView()) { // ?? joblet
                jobName = "Joblet"; //$NON-NLS-1$
            }
            jobName = jobName + " " + activeContext.getProcess().getLabel(); //$NON-NLS-1$
            setTitleToolTip(jobName);
            setPartName(Messages.getString("ProcessView.title", jobName)); //$NON-NLS-1$
            processNameLab.setText(jobName);
        } else {
            setPartName(Messages.getString("ProcessView.titleEmpty")); //$NON-NLS-1$
            processNameLab.setText(Messages.getString("ProcessView.subtitleEmpty")); //$NON-NLS-1$
        }

        // processNameLab.getParent().layout(true, true);
    }

    /**
     * DOC smallet ProcessView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public class RunAction extends Action {

        /**
         * DOC smallet RunAction constructor comment.
         */
        public RunAction() {
            super();
            this.setActionDefinitionId("runProcess"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            IWorkbench workbench = PlatformUI.getWorkbench();
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            try {
                page.showView("org.talend.designer.runprocess.ui.views.processview"); //$NON-NLS-1$
            } catch (PartInitException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                ExceptionHandler.process(e);
            }

            if (processComposite.hasProcess() && !processComposite.getProcess().disableRunJobView()) {
                processComposite.errorMessMap.clear();
                processComposite.setCurRunMode(EXEC_ID);
                processComposite.exec();
            }
        }

    }

    /**
     * DOC smallet ProcessView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class DebugAction extends Action {

        /**
         * DOC smallet RunAction constructor comment.
         */
        public DebugAction() {
            super();
            this.setActionDefinitionId("debugProcess"); //$NON-NLS-1$
        }

        @Override
        public void run() {

            if (processComposite.hasProcess()) {
                processComposite.errorMessMap.clear();
                processComposite.debug();
            }
        }

    }

    /**
     * DOC smallet ProcessView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class KillAction extends Action {

        /**
         * DOC smallet RunAction constructor comment.
         */
        public KillAction() {
            super();
            this.setActionDefinitionId("killProcess"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            if (processComposite.hasProcess()) {
                processComposite.kill();
            }
        }

    }

    /**
     * Getter for processViewHelper.
     * 
     * @return the processViewHelper
     */
    public IProcessViewHelper getProcessViewHelper() {
        return this.processViewHelper;
    }

    /**
     * Sets the processViewHelper.
     * 
     * @param processViewHelper the processViewHelper to set
     */
    public void setProcessViewHelper(IProcessViewHelper processViewHelper) {
        this.processViewHelper = processViewHelper;
    }

}
