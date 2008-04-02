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
package org.talend.designer.core.ui.views.statsandlogs;

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
 * ftang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (忙聵聼忙聹聼盲潞聰, 29 盲鹿聺忙聹聢 2006) nrousseau $
 * 
 */
public class StatsAndLogsComposite extends MultipleThreadDynamicComposite {

    Button reloadBtn;

    Button saveBtn;

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

            super.addComponents(true, false, initialSize.y + ITabbedPropertyConstants.VSPACE);
        }
    }

    /**
     * ftang StatAndLogsComposite constructor comment.
     * 
     * @param parentComposite
     * @param styles
     * @param section
     * @param element
     */
    public StatsAndLogsComposite(Composite parentComposite, int styles, EComponentCategory section, Element element) {
        super(parentComposite, styles, section, element, true);
    }

    /**
     * ftang Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        reloadBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {

                if (elem == null) {
                    return;
                }
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), Messages
                        .getString("StatsAndLogsComposite.StatsLogsSettings"), //$NON-NLS-1$
                        Messages.getString("StatsAndLogsComposite.ReloadMessages")); //$NON-NLS-1$
                if (isOK) {
                    StatsAndLogsViewHelper.reloadValuesFromPreferencePage(elem);

                    IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .getActiveEditor();

                    if (activeEditor != null) {
                        AbstractTalendEditor workbenchPart = ((AbstractMultiPageTalendEditor) activeEditor).getTalendEditor();
                        workbenchPart.setDirty(true);
                    }
                }
                addComponents(true);
                refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        saveBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {

                if (elem == null) {
                    return;
                }
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), Messages
                        .getString("StatsAndLogsComposite.StatsLogsSettings"), //$NON-NLS-1$
                        Messages.getString("StatsAndLogsComposite.SavePreferenceMessages")); //$NON-NLS-1$
                if (isOK) {
                    StatsAndLogsViewHelper.saveValuesToPreferencePage(elem);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

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
