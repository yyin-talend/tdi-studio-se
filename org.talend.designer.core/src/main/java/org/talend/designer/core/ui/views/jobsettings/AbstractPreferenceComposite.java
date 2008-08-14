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
package org.talend.designer.core.ui.views.jobsettings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;

/**
 * Add buttons for loading and saving values between preference page and job view.
 */
public abstract class AbstractPreferenceComposite extends MultipleThreadDynamicComposite {

    Button reloadBtn;

    Button saveBtn;

    String dialogTitle;

    /**
     * DOC chuang AbstractPreferenceComposite constructor comment.
     * 
     * @param parentComposite
     * @param styles
     * @param section
     * @param element
     * @param isCompactView
     */
    public AbstractPreferenceComposite(Composite parentComposite, int styles, EComponentCategory section, Element element,
            boolean isCompactView) {
        super(parentComposite, styles, section, element, isCompactView);
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    @Override
    public void addComponents(boolean forceRedraw, boolean reInitialize, int height) {
        if (forceRedraw || isNeedRedraw()) {
            disposeChildren();
            Composite topComposite = new Composite(getComposite(), SWT.NONE);
            topComposite.setLayout(new GridLayout(2, false));
            topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            reloadBtn = new Button(topComposite, SWT.PUSH);
            reloadBtn.setText(Messages.getString("StatsAndLogsComposite.Reload")); //$NON-NLS-1$
            reloadBtn.setToolTipText(Messages.getString("StatsAndLogsComposite.ReloadToolTipText")); //$NON-NLS-1$

            saveBtn = new Button(topComposite, SWT.PUSH);
            saveBtn.setText(Messages.getString("StatsAndLogsComposite.Save")); //$NON-NLS-1$
            saveBtn.setToolTipText(Messages.getString("StatsAndLogsComposite.SaveToolTipText")); //$NON-NLS-1$

            Point initialSize = topComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);

            addButtonListeners();
            refresh();
            super.addComponents(true, false, initialSize.y + ITabbedPropertyConstants.VSPACE);
        }
    }

    /**
     * ftang Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        reloadBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                onReloadButtonClick();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        saveBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                onSaveButtonClick();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

    }

    protected void onReloadButtonClick() {
        if (elem == null) {
            return;
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, Messages
                .getString("StatsAndLogsComposite.ReloadMessages")); //$NON-NLS-1$
        if (isOK) {
            onReloadPreference();

            IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

            if (activeEditor != null) {
                AbstractTalendEditor workbenchPart = ((AbstractMultiPageTalendEditor) activeEditor).getTalendEditor();
                workbenchPart.setDirty(true);
            }
        }
        addComponents(true);
        refresh();
    }

    /**
     * Override by subclass.
     */
    protected void onReloadPreference() {

    }

    protected void onSaveButtonClick() {
        if (elem == null) {
            return;
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, Messages
                .getString("StatsAndLogsComposite.SavePreferenceMessages")); //$NON-NLS-1$
        if (isOK) {
            onSavePreference();
        }
    }

    /**
     * Override by subclass.
     */
    protected void onSavePreference() {
    }

    @Override
    public void refresh() {
        super.refresh();

        Element element = getElement();
        if (element != null && element instanceof IProcess) {
            IProcess process = (IProcess) element;
            if (reloadBtn != null && !reloadBtn.isDisposed()) {
                reloadBtn.setEnabled(!process.isReadOnly());
            }
            if (saveBtn != null && !saveBtn.isDisposed()) {
                saveBtn.setEnabled(!process.isReadOnly());
            }
        }
    }

}
