// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.runprocess.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessContextManager;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.ProcessComposite2;
import org.talend.designer.runprocess.ui.actions.ClearPerformanceAction;

/**
 * View showing the execution of a process. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessView extends ViewPart {

    public static final String ID = RunProcessPlugin.PLUGIN_ID + ".ui.view.processview"; //$NON-NLS-1$

    private Label processNameLab;

    private ProcessComposite2 processComposite2;

    private PropertyChangeListener contextManagerListener;

    private ClearPerformanceAction clearPerfAction;

    /**
     * Constructs a new ProcessView.
     */
    public ProcessView() {
        super();
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

        processNameLab = new Label(container, SWT.NONE);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        data.verticalIndent = 2;
        processNameLab.setLayoutData(data);
        FontData[] fds = processNameLab.getFont().getFontData();
        for (FontData fd : fds) {
            fd.setHeight(fd.getHeight() + 2);
            fd.setStyle(fd.getStyle() | SWT.BOLD);
        }
        Font titleFont = new Font(processNameLab.getDisplay(), fds);
        processNameLab.setFont(titleFont);
        processNameLab.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));

        processComposite2 = new ProcessComposite2(container, SWT.NONE);
        processComposite2.setLayoutData(new GridData(GridData.FILL_BOTH));

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

        Action runAction = new RunAction();
        getViewSite().getKeyBindingService().registerAction(runAction);
        Action debugAction = new DebugAction();
        getViewSite().getKeyBindingService().registerAction(debugAction);
        Action killAction = new KillAction();
        getViewSite().getKeyBindingService().registerAction(killAction);

        FocusListener fl = new FocusListener() {

            public void focusGained(FocusEvent e) {
                System.out.println("Run process gain focus");
                IContextService contextService = (IContextService) RunProcessPlugin.getDefault().getWorkbench().getAdapter(
                        IContextService.class);
                ca = contextService.activateContext("talend.runProcess");
            }

            public void focusLost(FocusEvent e) {
                System.out.println("Run process lost focus");
                if (ca != null) {
                    IContextService contextService = (IContextService) RunProcessPlugin.getDefault().getWorkbench().getAdapter(
                            IContextService.class);
                    contextService.deactivateContext(ca);
                }
            }
        };

        addListenerOnChildren(container, fl);
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
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        processComposite2.setFocus();

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
        processComposite2.setProcessContext(activeContext);

        if (activeContext != null) {
            setPartName(Messages.getString("ProcessView.title", activeContext.getProcess().getLabel())); //$NON-NLS-1$
            processNameLab.setText(Messages.getString("ProcessView.subtitle", activeContext.getProcess().getLabel())); //$NON-NLS-1$
        } else {
            setPartName(Messages.getString("ProcessView.titleEmpty")); //$NON-NLS-1$
            processNameLab.setText(Messages.getString("ProcessView.subtitleEmpty")); //$NON-NLS-1$
        }
        processNameLab.getParent().layout(true, true);
    }

    /**
     * DOC smallet ProcessView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class RunAction extends Action {

        /**
         * DOC smallet RunAction constructor comment.
         */
        public RunAction() {
            super();
            this.setActionDefinitionId("runProcess");
        }

        @Override
        public void run() {
            if (processComposite2.hasProcess()) {
                processComposite2.exec();
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
            this.setActionDefinitionId("debugProcess");
        }

        @Override
        public void run() {
            if (processComposite2.hasProcess()) {
                processComposite2.debug();
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
            this.setActionDefinitionId("killProcess");
        }

        @Override
        public void run() {
            if (processComposite2.hasProcess()) {
                processComposite2.kill();
            }
        }

    }
}
