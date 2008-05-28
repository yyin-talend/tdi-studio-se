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
package org.talend.componentdesigner.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.componentdesigner.i18n.internal.Messages;
import org.talend.componentdesigner.util.file.FileCopy;

/**
 * DOC slanglois class global comment. Detailled comment
 */
public class ImportComponentDialog extends Dialog {

    /**
     * The title of the dialog.
     */
    private String title;

    /**
     * The message to display, or <code>null</code> if none.
     */
    private String message;

    /**
     * The input value; the empty string by default.
     */
    private String value = ""; //$NON-NLS-1$

    /**
     * Input text widget.
     */
    private Text text;

    /**
     * Filter text widget.
     */
    private Text filterText;

    /**
     * Error message label widget.
     */
    private Text errorMessageText;

    /**
     * Components List too choose.
     */
    private List componentList;

    /**
     * components name list
     */
    private java.util.List<String> components;

    /**
     * selected project in component designer workspace
     */
    private IProject selectedProject;

    /**
     * DOC slanglois ImportComponentDialog constructor comment.
     * 
     * @param parentShell
     * @param dialogTitle
     * @param dialogMessage
     * @param initialDirectory
     * @param selectedProject
     */
    public ImportComponentDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialDirectory,
            IProject selectedProject) {
        super(parentShell);
        this.title = dialogTitle;
        message = dialogMessage;
        if (initialDirectory == null) {
            value = ""; //$NON-NLS-1$
        } else {
            value = initialDirectory;
        }
        this.selectedProject = selectedProject;
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            value = text.getText();
        } else {
            value = null;
        }
        super.buttonPressed(buttonId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null) {
            shell.setText(title);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        // do this here because setting the text will set enablement on the ok
        // button
        // text.setFocus();
        // if (value != null) {
        // text.setText(value);
        // text.selectAll();
        // }
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout gl = new GridLayout(5, true);
        composite.setLayout(gl);

        if (message != null) {
            Label label = new Label(composite, SWT.WRAP);
            label.setText(message);
            GridData data = new GridData(GridData.GRAB_HORIZONTAL);
            data.horizontalSpan = 5;
            label.setLayoutData(data);
            label.setFont(parent.getFont());
        }

        Label label1 = new Label(composite, SWT.NONE);
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gd.horizontalSpan = 2;
        label1.setLayoutData(gd);
        label1.setText(Messages.getString("ImportComponentDialog.SelectDirectoryLabel")); //$NON-NLS-1$

        text = new Text(composite, SWT.BORDER);
        text.setEditable(false);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 2;
        text.setLayoutData(gd);

        if (value == null) {
            value = ""; //$NON-NLS-1$
        }
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });

        Button button = new Button(composite, SWT.NONE);
        button.setText(Messages.getString("ImportComponentDialog.Browser")); //$NON-NLS-1$
        gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gd.horizontalSpan = 1;
        button.setLayoutData(gd);
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                String path = dd.open();
                if (path != null) {
                    text.setText(path);
                }
            }
        });

        Button showPaletteComponents = new Button(composite, SWT.NONE);
        gd = new GridData();
        gd.horizontalSpan = 2;
        showPaletteComponents.setLayoutData(gd);
        showPaletteComponents.setText(Messages.getString("ImportComponentDialog.ShowPaletteComponents")); //$NON-NLS-1$
        showPaletteComponents.addMouseListener(new MouseListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
             */
            public void mouseDoubleClick(MouseEvent e) {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
             */
            public void mouseDown(MouseEvent e) {
                text.setText(value);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            public void mouseUp(MouseEvent e) {

            }

        });

        label1 = new Label(composite, SWT.LEFT);
        gd = new GridData();
        gd.horizontalSpan = 1;
        label1.setLayoutData(gd);
        label1.setText(Messages.getString("ImportComponentDialog.Filter")); //$NON-NLS-1$

        filterText = new Text(composite, SWT.BORDER);
        filterText.setEditable(true);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 2;
        filterText.setLayoutData(gd);

        filterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });

        label1 = new Label(composite, SWT.LEFT);
        gd = new GridData();
        gd.horizontalSpan = 5;
        label1.setLayoutData(gd);
        label1.setText(Messages.getString("ImportComponentDialog.ChooseComponentsLabel")); //$NON-NLS-1$

        componentList = new List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        // gd.verticalSpan = 5;
        gd.horizontalSpan = 5;
        componentList.setLayoutData(gd);
        componentList.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                validateSelected();
            }

            public void mouseUp(MouseEvent e) {
                validateSelected();
            }

        });
        errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.CENTER);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 5;
        label1.setLayoutData(gd);
        errorMessageText.setLayoutData(gd);
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        applyDialogFont(composite);
        text.setText(""); //$NON-NLS-1$
        return composite;
    }

    /**
     * DOC slanglois Comment method "validateInput".
     */
    protected void validateInput() {

        String errorMessage = null;

        String value = text.getText();
        String filter = filterText.getText();
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }
        if (filter == null) {
            filter = ""; //$NON-NLS-1$
        }

        if (value.equals("")) { //$NON-NLS-1$
            errorMessage = Messages.getString("ImportComponentDialog.ErrorMSG1"); //$NON-NLS-1$
        } else {
            File file = new File(value);

            if (!file.exists() || !file.isDirectory()) {
                errorMessage = Messages.getString("ImportComponentDialog.ErrorMSG2"); //$NON-NLS-1$
            } else {
                // need to get all the available
                if (this.getComponents(value, filter).size() == 0) {
                    errorMessage = Messages.getString("ImportComponentDialog.NoComponentsAvailable"); //$NON-NLS-1$
                }
            }
        }

        if (errorMessage == null) {
            // set list items
            this.updateList(components.toArray(new String[0]));
            componentList.setSelection(0);
        } else {
            disableCompontsList();
        }
        setErrorMessage(errorMessage);
    }

    /**
     * DOC slanglois Comment method "validateSelected". Check if there is component selected.
     */
    protected void validateSelected() {
        String errorMessage = null;

        if (componentList.getSelectionCount() < 1) {
            errorMessage = Messages.getString("ImportComponentDialog.ErrorMSG4"); //$NON-NLS-1$
        }

        setErrorMessage(errorMessage);
    }

    /**
     * DOC slanglois Comment method "updateList". List all the componets name in the selected folder.
     * 
     * @param items
     */
    private void updateList(String[] items) {
        if (!componentList.isEnabled()) {
            componentList.setEnabled(true);
        }
        componentList.setItems(items);
        validateSelected();
    }

    /**
     * Sets or clears the error message. If not <code>null</code>, the OK button is disabled.
     * 
     * @param errorMessage the error message, or <code>null</code> to clear
     */
    public void setErrorMessage(String errorMessage) {
        // this.errorMessage = errorMessage;
        if (errorMessageText != null && !errorMessageText.isDisposed()) {
            errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
            boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
            errorMessageText.setEnabled(hasError);
            errorMessageText.setVisible(hasError);
            errorMessageText.getParent().update();
            Control button = getButton(IDialogConstants.OK_ID);
            if (button != null) {
                button.setEnabled(errorMessage == null);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    protected void okPressed() {
        this.getShell().setVisible(false);
        if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages
                .getString("ImportComponentDialog.WarningTitle"), //$NON-NLS-1$
                Messages.getString("ImportComponentDialog.WarningMSG"))) { //$NON-NLS-1$

            // import components.
            // try {
            // new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).run(true,
            // false,
            // new IRunnableWithProgress() {
            //
            // public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            // String[] selectedComponents = componentList.getSelection();
            // int filesToProcess = selectedComponents.length + 1;
            // monitor.beginTask("Import components from palette", filesToProcess);
            // String sourceDirectory = text.getText();
            // String targetDirectory = selectedProject.getLocation().toFile().getAbsolutePath();
            //
            // for (String component : selectedComponents) {
            // monitor.subTask("Importing component " + component + "...");
            // FileCopy.copyComponentFolder(sourceDirectory + File.separator + component, targetDirectory
            // + File.separator + component);
            // monitor.worked(1);
            // }
            // // refresh workspace
            // monitor.subTask("Refreshing workspace...");
            // try {
            // selectedProject.refreshLocal(2, null);
            // } catch (CoreException e) {
            // e.printStackTrace();
            // }
            // monitor.worked(1);
            // monitor.done();
            // }
            //
            // });
            // } catch (InvocationTargetException e) {
            // e.printStackTrace();
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }

            String[] selectedComponents = componentList.getSelection();
            String sourceDirectory = text.getText();
            String targetDirectory = selectedProject.getLocation().toFile().getAbsolutePath();

            for (String component : selectedComponents) {
                String componentName = component.contains(File.separator) ? component.substring(component
                        .lastIndexOf(File.separator)
                        + File.separator.length()) : component;
                FileCopy.copyComponentFolder(sourceDirectory + File.separator + component, targetDirectory + File.separator
                        + componentName);
            }
            // refresh workspace
            try {
                selectedProject.refreshLocal(2, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }

            MessageDialog
                    .openInformation(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            Messages.getString("ImportComponentDialog.ImportFinished"), Messages.getString("ImportComponentDialog.ImportFinishedMSG")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            MessageDialog
                    .openInformation(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            Messages.getString("ImportComponentDialog.ImportCanceled"), Messages.getString("ImportComponentDialog.ImportCanceledMSG")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        super.okPressed();
    }

    /**
     * DOC slanglois Comment method "disableCompontsList".
     */
    private void disableCompontsList() {
        componentList.setItems(new String[] { "", "", "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "", "", "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "", "", "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "", "", "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "", "", "", "", "", }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        componentList.setEnabled(false);
    }

    private java.util.List<String> getComponents(String directory, String filter) {
        String componentFilter = filter.toLowerCase();
        java.util.List<String> result = new ArrayList<String>();
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            Queue<String> queue = new LinkedList<String>();

            String[] list = file.list();
            for (String temp : list) {
                File subFile = new File(directory + File.separator + temp);
                if (subFile.exists() && subFile.isDirectory() && !temp.startsWith(".")) { //$NON-NLS-1$
                    queue.offer(temp);
                }
            }

            while (!queue.isEmpty()) {
                String temp = queue.poll();
                File tmpFile = new File(directory + File.separator + temp);
                if (isValidComponentFolder(tmpFile, componentFilter)) {
                    result.add(temp);
                    continue;
                }
                list = tmpFile.list();
                for (String tmp : list) {
                    File subFile = new File(directory + File.separator + temp + File.separator + tmp);
                    if (subFile.exists() && subFile.isDirectory() && !temp.startsWith(".")) { //$NON-NLS-1$
                        queue.offer(temp + File.separator + tmp);
                    }
                }
            }
        }

        Collections.sort(result);
        components = result;
        return result;
    }

    private boolean isValidComponentFolder(File path, String filterString) {
        if (!path.getName().toLowerCase().contains(filterString)) {
            return false;
        }
        if (path.exists() && path.isDirectory()) {
            String[] list = path.list();
            String pathString = path.getAbsolutePath();

            for (String temp : list) {
                if (temp.toLowerCase().matches("^t[a-z0-9-\\-]+_(java|perl).xml$")) { //$NON-NLS-1$
                    File subFile = new File(pathString + File.separator + temp);
                    if (subFile.exists() && subFile.isFile()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
