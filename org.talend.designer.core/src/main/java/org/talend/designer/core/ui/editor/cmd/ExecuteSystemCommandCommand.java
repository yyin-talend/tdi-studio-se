// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ExecuteSystemCommandCommand extends Command {

    private static final String RETURN = "\n"; //$NON-NLS-1$

    private List<String> commandsList;

    public ExecuteSystemCommandCommand(List<String> commandsList) {
        super();
        this.commandsList = commandsList;
    }

    @Override
    public void execute() {
        if (commandsList == null || commandsList.isEmpty()) {
            return;
        }
        StringBuffer errorMassage = new StringBuffer();

        CommandJob executeJob = new CommandJob(Messages.getString("ExecuteSystemCommandCommand.Command"), commandsList); //$NON-NLS-1$
        executeJob.schedule();

        try {
            executeJob.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            errorMassage.append(RETURN);
            errorMassage.append(e.getMessage());
        }

        ShowCommandMessage show = new ShowCommandMessage(PlatformUI.getWorkbench().getDisplay().getActiveShell(), executeJob
                .getCommands(), executeJob.getResultMessages(), executeJob.getErrorMessages() + errorMassage.toString());
        show.open();
    }

    /**
     * 
     * DOC ggu CommandJob class global comment. Detailled comment
     */
    class CommandJob extends Job {

        private List<String> commansList;

        private StringBuffer resultMassage = new StringBuffer();

        private StringBuffer errorMassage = new StringBuffer();

        private StringBuffer commandsMessage = new StringBuffer();

        public CommandJob(String name, List<String> commansList) {
            super(name);
            this.commansList = commansList;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            monitor.beginTask(getName(), commansList.size());

            Runtime runtime = Runtime.getRuntime();
            try {

                String line = ""; //$NON-NLS-1$
                for (String command : commansList) {
                    commandsMessage.append(command);
                    commandsMessage.append(RETURN);
                    if ("".equals(command.trim())) { //$NON-NLS-1$
                        continue;
                    }
                    final Process process = runtime.exec(command);

                    // return message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    try {
                        while ((line = reader.readLine()) != null) {
                            resultMassage.append(line);
                            resultMassage.append(RETURN);
                        }
                        resultMassage.append(RETURN);
                    } finally {
                        try {
                            reader.close();
                            reader = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // error message
                    reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    try {
                        while ((line = reader.readLine()) != null) {
                            errorMassage.append(line);
                            errorMassage.append(RETURN);
                        }
                        errorMassage.append(RETURN);
                    } finally {
                        try {
                            reader.close();
                            reader = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                errorMassage.append(RETURN);
                errorMassage.append(e.getMessage());
            }
            monitor.done();
            return Status.OK_STATUS;
        }

        public String getCommands() {
            return commandsMessage.toString();
        }

        public String getResultMessages() {
            return resultMassage.toString();
        }

        public String getErrorMessages() {
            return errorMassage.toString();
        }

    }

    /**
     * 
     * DOC ggu ShowCommandMessage class global comment. Detailled comment
     */
    class ShowCommandMessage extends Dialog {

        private static final int WIDTH = 480;

        private static final int HEIGHT = 370;

        private String commands;

        private String resultMessage;

        private String errorMessage;

        protected ShowCommandMessage(Shell parentShell, String commands, String resultMessage, String errorMessage) {
            super(parentShell);
            this.commands = commands;
            this.resultMessage = resultMessage;
            this.errorMessage = errorMessage;
            setDefaultImage(ImageProvider.getImageDesc(ECoreImage.PROCESS_ICON).createImage());
            setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
            setBlockOnOpen(true);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);
            composite.setFont(parent.getFont());
            GridData gridData;
            Composite inner = Form.startNewGridLayout(composite, 1);
            inner.setFont(composite.getFont());
            //
            Label label = new Label(inner, SWT.NONE);
            label.setText(Messages.getString("ExecuteSystemCommandCommand.Label")); //$NON-NLS-1$
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.heightHint = 20;
            label.setLayoutData(gridData);

            Text cmdText = new Text(inner, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
            cmdText.setText(commands);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.heightHint = 50;
            cmdText.setLayoutData(gridData);

            TabFolder messTabFolder = new TabFolder(inner, SWT.NONE);
            gridData = new GridData(GridData.FILL_BOTH);
            messTabFolder.setLayoutData(gridData);
            messTabFolder.setBackground(inner.getBackground());
            // message
            TabItem normalTabItem = new TabItem(messTabFolder, SWT.NONE);
            normalTabItem.setText(Messages.getString("ExecuteSystemCommandCommand.ResultMessages")); //$NON-NLS-1$

            final int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.BORDER;
            Text normalText = new Text(messTabFolder, style);
            normalText.setText(resultMessage);
            gridData = new GridData(GridData.FILL_BOTH);
            normalText.setLayoutData(gridData);
            normalText.setFont(inner.getFont());
            normalTabItem.setControl(normalText);

            if (errorMessage.length() > 0) {
                // message
                TabItem errorTabItem = new TabItem(messTabFolder, SWT.NONE);
                errorTabItem.setText(Messages.getString("ExecuteSystemCommandCommand.ErrorMessages")); //$NON-NLS-1$

                Text errorText = new Text(messTabFolder, style);
                errorText.setText(errorMessage);
                gridData = new GridData(GridData.FILL_BOTH);
                errorText.setLayoutData(gridData);
                errorText.setFont(inner.getFont());
                errorText.setForeground(new Color(null, 230, 0, 0));
                errorTabItem.setControl(errorText);
            }

            return composite;
        }

        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setSize(WIDTH, HEIGHT);
            newShell.setText(Messages.getString("ExecuteSystemCommandCommand.Title")); //$NON-NLS-1$

        }

        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        }

    }

}
