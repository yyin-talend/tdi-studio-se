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
package org.talend.designer.runprocess.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.process.IProcess;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProcessDialog extends Dialog {

    /** The process to be run. */
    private IProcess process;

    /** Performance monitoring activated. */
    private boolean monitorPerf;
    
    /** Trace monitoring activated. */
    private boolean monitorTrace;

    private ProcessComposite processComposite;

    /**
     * DOC chuger ProcessDialog constructor comment.
     * 
     * @param parentShell
     */
    public ProcessDialog(Shell parentShell, IProcess process, boolean monitorPerf, boolean monitorTrace) {
        super(parentShell);

        this.process = process;
        this.monitorPerf = monitorPerf;
        this.monitorTrace = monitorTrace;
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.getString("ProcessDialog.title", process.getLabel())); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        processComposite = new ProcessComposite(composite, SWT.NONE);
        processComposite.setProcess(process, monitorPerf, monitorTrace);
        processComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        return composite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#open()
     */
    @Override
    public int open() {
        int returnCode = super.open();
        processComposite.dispose();
        return returnCode;
    }
}
