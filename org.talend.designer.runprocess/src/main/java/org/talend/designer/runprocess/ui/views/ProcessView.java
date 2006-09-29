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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
        Composite container = new Composite(parent, SWT.NONE);
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

        //fillActionBars();

        contextManagerListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (RunProcessContextManager.PROP_ACTIVE.equals(evt.getPropertyName())) {
                    runningProcessChanged();
                }
            }
        };
        RunProcessPlugin.getDefault().getRunProcessContextManager().addPropertyChangeListener(contextManagerListener);

        runningProcessChanged();
    }

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
            RunProcessPlugin.getDefault().getRunProcessContextManager().removePropertyChangeListener(
                    contextManagerListener);
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
    }

    private void runningProcessChanged() {
        refresh();
    }

    public void refresh() {
        RunProcessContext activeContext = RunProcessPlugin.getDefault().getRunProcessContextManager()
                .getActiveContext();
        //clearPerfAction.setProcess(activeContext != null ? activeContext.getProcess() : null);
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
}
