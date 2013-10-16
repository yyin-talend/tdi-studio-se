// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;


/**
 * This Dialog is similar to TraDialog both support helpAvailable feature, but this one override the UI of Help button
 * to a normal labeled button instead of a "<img src="http://www.eclipse.org/mylyn/images/icon-help.gif">" image button.
 * Help button is visible by default(can be set by {@link #setHelpAvailable(boolean)}),just need to override
 * {@link #helpPressed()} method.
 * 
 * @author GaoZone
 * @version 1.0
 */
public class HelpAvailableDialog extends Dialog {

    private boolean helpAvailable = true;

    public HelpAvailableDialog(IShellProvider parentShell) {
        super(parentShell);
    }

    public HelpAvailableDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        // Register help listener on the shell
        newShell.addHelpListener(new HelpListener() {
            public void helpRequested(HelpEvent event) {
                helpPressed();
            }
        });
    }

    /**
     * Perform help behavior. Override when necessary.</br>example open help part inner the dialog:</br>
     * {@code PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), HELP_CONTEXT_ID);}</br>or open independent
     * dialog:</br> {@code PlatformUI.getWorkbench().getHelpSystem().displayHelp(HELP_CONTEXT_ID);}
     */
    protected void helpPressed() {
        IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
        String helpContext = getHelpContextId();
        if (helpContext == null) {
            helpSystem.displayHelp();
        } else {
            helpSystem.displayHelp(helpContext);
        }
    }

    /**
     * Gets the help context id. Override when necessary.</br> Triggered in {@link #helpPressed()} by default, use to
     * open specify help context.
     * 
     * @return the help context id
     */
    protected String getHelpContextId() {
        return null;
    }

    /**
     * Add create Help Button if {@link #isHelpAvailable()} = true.
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        if (isHelpAvailable()) {
            createButton(parent, IDialogConstants.HELP_ID, IDialogConstants.HELP_LABEL, false);
        }
        super.createButtonsForButtonBar(parent);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.HELP_ID == buttonId) {
            helpPressed();
        } else {
            super.buttonPressed(buttonId);
        }
    }

    protected boolean isHelpAvailable() {
        return helpAvailable;
    }

    public void setHelpAvailable(boolean helpAvailable) {
        this.helpAvailable = helpAvailable;
    }
}
