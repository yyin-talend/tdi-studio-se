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
package org.talend.repository.ui.wizards.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public class ShowAddedContextdialog extends SelectionDialog {

    private static final String TITILE = Messages.getString("ShowAddedContextdialog.Title"); //$NON-NLS-1$

    private List<String> contextList = new ArrayList<String>();

    private boolean checked = true;

    public ShowAddedContextdialog(Map<String, Set<String>> addedVarsMap) {
        super(PlatformUI.getWorkbench().getDisplay().getActiveShell());
        Assert.isNotNull(addedVarsMap);
        for (String lable : addedVarsMap.keySet()) {
            contextList.addAll(addedVarsMap.get(lable));
        }
        String label = ""; //$NON-NLS-1$
        for (String source : addedVarsMap.keySet()) {
            label += source + "/"; //$NON-NLS-1$
        }
        if (label.length() > 1) {
            label = label.substring(0, label.length() - 1);
        }

        init(label);
    }

    public ShowAddedContextdialog(Set<String> contextSet, final String label) {
        super(PlatformUI.getWorkbench().getDisplay().getActiveShell());
        Assert.isNotNull(contextSet);
        this.contextList.addAll(contextSet);
        init(label);
    }

    private void init(String label) {
        Collections.sort(contextList);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setBlockOnOpen(true);
        setDefaultImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
        setTitle(TITILE);
        setMessage(Messages.getString("ShowAddedContextdialog.Messages", label)); //$NON-NLS-1$
        setHelpAvailable(false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setFont(parent.getFont());
        createMessageArea(composite);

        //
        Group inner = new Group(composite, SWT.NONE);
        inner.setText(Messages.getString("ShowAddedContextdialog.Variables")); //$NON-NLS-1$
        inner.setFont(composite.getFont());
        inner.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumHeight = 150;
        gridData.minimumWidth = 200;
        inner.setLayoutData(gridData);

        ListViewer listViewer = new ListViewer(inner);
        listViewer.add(contextList.toArray());

        listViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        // option

        final Button checkBtn = new Button(inner, SWT.CHECK);
        checkBtn.setText(Messages.getString("ShowAddedContextdialog.CheckLabel")); //$NON-NLS-1$
        checkBtn.setSelection(checked);

        checkBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checked = checkBtn.getSelection();
                if (!checked) {
                    String messages = Messages.getString("ShowAddedContextdialog.CheckWarningMessages"); //$NON-NLS-1$
                    MessageDialog.openWarning(getParentShell(), Messages.getString("ShowAddedContextdialog.CheckWarningTitile"), //$NON-NLS-1$
                            messages);
                    checkBtn.setToolTipText(messages); //$NON-NLS-1$

                } else {
                    checkBtn.setToolTipText(""); //$NON-NLS-1$
                }
                //
                Button okBtn = getOkButton();
                if (okBtn != null) {
                    okBtn.setEnabled(checked);
                }
            }
        });
        return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        if (getOkButton() != null) {
            getOkButton().setText(Messages.getString("ShowAddedContextdialog.AddLabel")); //$NON-NLS-1$
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

}
