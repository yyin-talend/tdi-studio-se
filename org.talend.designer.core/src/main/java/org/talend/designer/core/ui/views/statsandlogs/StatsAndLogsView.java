// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

import org.eclipse.draw2d.GridData;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.properties.process.StatsAndLogsTabPropertySection;

/**
 * class global comment. Detailed comment <br/>
 * 
 */
public class StatsAndLogsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.statsandlogs.statsAndLogsView";

    private static final String VIEW_NAME = "Stats/Logs";;

    private Process process;

    private StatsAndLogsTabPropertySection section;

    private String title;

    private Composite parent;

    private Button reloadBtn;

    private Button saveBtn;

    private Composite statsAndLogsViewComposite;

    private boolean isEmptyComposite;

    /**
     * Default StatsAndLogsView constructor.
     */
    public StatsAndLogsView() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        this.getCurrentJob();
        if (this.process != null) {
            this.createFullPartControl(parent);
            createStatsAndLogsView();
            this.section.refresh();
        } else {
            this.createEmptyPartControl(parent);
        }
    }

    /**
     * Creates a empty composite if no job opened.
     * 
     * @param parent
     */
    private void createEmptyPartControl(Composite parent) {
        Composite alertComposite = new Composite(parent, SWT.NONE);
        alertComposite.setLayout(new GridLayout());
        alertComposite.setLayoutData(new GridData());
        Text alertText = new Text(alertComposite, SWT.NONE);
        alertText.setText("A Stats/Logs is not available.");
        alertText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    }

    /**
     * Creates a full composite if job is opened.
     * 
     * @param parent
     */
    private void createFullPartControl(Composite parentComposite) {

        parentComposite.setLayout(new FillLayout());
        ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL);

        Composite mainComposite = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(mainComposite);
        mainComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));// White color
        mainComposite.setLayout(new GridLayout(1, true));
        GridData data = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(data);

        Composite topComposite = new Composite(mainComposite, SWT.NONE);
        topComposite.setLayout(new GridLayout(2, false));
        topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));// White color

        reloadBtn = new Button(topComposite, SWT.PUSH);
        reloadBtn.setText("&Reload from preferences");
        reloadBtn.setToolTipText("Reload values from preference page(Shift+R)");

        saveBtn = new Button(topComposite, SWT.PUSH);
        saveBtn.setText("&Save to preferences");
        saveBtn.setToolTipText("save values to preference page(Shift+S)");

        addButtonListeners();

        createStatsAndLogsComposite(mainComposite);

        mainComposite.setSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setMinWidth(800);
        scrolledComposite.setMinHeight(400);
    }

    /**
     * ftang Comment method "createStatsAndLogsComposite".
     * 
     * @param parentComposite
     */
    private void createStatsAndLogsComposite(Composite mainComposite) {
        statsAndLogsViewComposite = new Composite(mainComposite, SWT.BORDER);
        statsAndLogsViewComposite.setLayout(new FormLayout());
        statsAndLogsViewComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));// White color
    }

    /**
     * ftang Comment method "createStatsAndLogsView".
     * 
     */
    public void createStatsAndLogsView() {
        this.parent.setVisible(true);
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        if (activeEditor == null) {
            createEmptyPartControl(this.parent);
            this.isEmptyComposite = true;
        } else {
            TalendEditor workbenchPart = ((MultiPageTalendEditor) activeEditor).getTalendEditor();
            ProcessPart part = workbenchPart.getProcessPart();
            section = new StatsAndLogsTabPropertySection();
            StructuredSelection sel = new StructuredSelection(part);
            section.setInput(activeEditor, sel);

            Control[] children = this.parent.getChildren();
            disposeControls(children);

            createFullPartControl(this.parent);
            section.setComposite(this.statsAndLogsViewComposite);
            this.parent.getParent().layout(true, true);
        }
    }

    /**
     * ftang Comment method "disposeControls".
     * 
     * @param children
     */
    private void disposeControls(Control[] children) {
        for (Control control : children) {
                control.dispose();
        }
    }

    /**
     * DOC Administrator Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        reloadBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                Element element = section.getElement();
                if (element == null) {
                    return;
                }
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Stats/Logs View",
                        "Current setting will be covered, do you want to continue?");
                if (isOK) {
                    StatsAndLogsViewHelper.reloadValuesFromPreferencePage(element);
                    section.refresh();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

        saveBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                Element element = section.getElement();
                if (element == null) {
                    return;
                }
                boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Stats/Logs View",
                        "Current preference page setting will be covered, do you want to continue?");
                if (isOK) {
                    StatsAndLogsViewHelper.saveValuesToPreferencePage(element);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        this.parent.setFocus();
        // this.statsAndLogsViewComposite.setFocus();
    }

    /**
     * Gets current opened job.
     */
    private void getCurrentJob() {
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor != null && activeEditor instanceof MultiPageTalendEditor) {
            TalendEditor talendEditor = ((MultiPageTalendEditor) activeEditor).getTalendEditor();
            Process process = talendEditor.getProcess();
            if (process != null) {
                this.process = process;
                this.title = talendEditor.getTitle();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.ViewPart#setPartName(java.lang.String)
     */
    public void setPartName(String title) {
        String viewName = VIEW_NAME;
        if (!title.equals("")) { //$NON-NLS-1$
            viewName = viewName + "(" + title + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        super.setPartName(viewName);
    }

    /**
     * ftang Comment method "refresh".
     */
    public void refresh() {
        createStatsAndLogsView();
        if (!this.isEmptyComposite) {
            this.section.refresh();
        }
    }

    /**
     * Empty view if no job is opened.
     */
    public void emptyView() {
//        this.parent.setVisible(false);
        Control[] children = this.parent.getChildren();
        disposeControls(children);
        createEmptyPartControl(parent);
        this.parent.getParent().layout(true,true);
    }

    /**
     * ftang Comment method "refreshView".
     */
    public void refreshView() {
        if (!this.isEmptyComposite) {
            this.section.refresh();
        }
    }

}
