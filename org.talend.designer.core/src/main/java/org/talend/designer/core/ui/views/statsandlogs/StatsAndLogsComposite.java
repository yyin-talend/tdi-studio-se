// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.views.properties.DynamicComposite;

/**
 * ftang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class StatsAndLogsComposite extends DynamicComposite {

    Button reloadBtn;

    Button saveBtn;

    @Override
    public void addComponents(boolean forceRedraw, boolean reInitialize, int height) {
        updateMainParameters();
        if (forceRedraw || isNeedRedraw()) {
            disposeChildren();
            Composite topComposite = new Composite(getComposite(), SWT.NONE);
            topComposite.setLayout(new GridLayout(2, false));
            topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            reloadBtn = new Button(topComposite, SWT.PUSH);
            reloadBtn.setText("&Reload from preferences");
            reloadBtn.setToolTipText("Reload values from preference page(Shift+R)");

            saveBtn = new Button(topComposite, SWT.PUSH);
            saveBtn.setText("&Save to preferences");
            saveBtn.setToolTipText("save values to preference page(Shift+S)");

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
        super(parentComposite, styles, section, element);
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
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Stats/Logs View",
                        "Current setting will be covered, do you want to continue?");
                if (isOK) {
                    StatsAndLogsViewHelper.reloadValuesFromPreferencePage(elem);

                    IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .getActiveEditor();

                    if (activeEditor != null) {
                        TalendEditor workbenchPart = ((MultiPageTalendEditor) activeEditor).getTalendEditor();
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
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Stats/Logs View",
                        "Current preference page setting will be covered, do you want to continue?");
                if (isOK) {
                    StatsAndLogsViewHelper.saveValuesToPreferencePage(elem);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

    }

}
