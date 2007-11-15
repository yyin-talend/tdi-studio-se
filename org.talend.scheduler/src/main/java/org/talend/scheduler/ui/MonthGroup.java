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
package org.talend.scheduler.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.StringUtils;
import org.talend.scheduler.i18n.Messages;

/**
 * 
 * A month group composite. <br/>
 * 
 * $Id$
 * 
 */
public class MonthGroup extends Group implements IWidgetEnableListener {

    IResultChangedListener changeListener = null;

    private List<Button> buttons = new ArrayList<Button>();

    protected final String allMonths = "*"; //$NON-NLS-1$

    private List<String> months = new ArrayList<String>();

    /**
     * Sets ChangeListener.
     * 
     * @param changeListener IResultChangedListener
     */
    public void setChangeListener(IResultChangedListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * add Month.
     * 
     * @param month String
     */
    private void addMonths(String month) {
        if (month == null) {
            return;
        }
        months.add(month);
    }

    /**
     * Deletes month.
     * 
     * @param month String
     */
    private void delMonths(String month) {
        if (month == null) {
            return;
        }
        months.remove(month);
    }

    /**
     * DOC qianbing Comment method "displayMonths".
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void displayMonths() {
        Collections.sort(months, new Comparator() {

            public int compare(Object o1, Object o2) {
                int day1 = Integer.valueOf((String) o1).intValue();
                int day2 = Integer.valueOf((String) o2).intValue();
                if (day1 > day2) {
                    return 1;
                } else if (day1 < day2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        StringBuffer sb = new StringBuffer();
        for (Iterator it = months.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(","); //$NON-NLS-1$
            }
        }

        changeListener.monthChanged(StringUtils.format(sb.toString()));
    }

    private SelectionAdapter listener = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            Button btn = (Button) e.getSource();
            // Select the button
            if (btn.getSelection()) {
                addMonths((String) btn.getData());
                if (months.size() == 12) {
                    months.clear();
                    months.add(allMonths);
                }
            } else {
                if (months.size() == 1 && months.get(0).equals(allMonths)) {
                    months.clear();
                    for (int i = 1; i < 13; i++) {
                        months.add(String.valueOf(i));
                    }
                }
                delMonths((String) btn.getData());
            }

            displayMonths();
        }
    };

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public MonthGroup(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        setLayout(gridLayout);
        setText(Messages.getString("MonthGroup.monthGroupTitle")); //$NON-NLS-1$

        createMonthButton(Messages.getString("MonthGroup.januaryButtonText"), "1"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.februaryButtonText"), "2"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.marchButtonText"), "3"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.aprilButtonText"), "4"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.mayButtonText"), "5"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.juneButtonText"), "6"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.julyButtonText"), "7"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.augustButtonText"), "8"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.septemberButtonText"), "9"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.octoberButtonText"), "10"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.novemberButtonText"), "11"); //$NON-NLS-1$ //$NON-NLS-2$

        createMonthButton(Messages.getString("MonthGroup.decemberButtonText"), "12"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Creates Month Button.
     * 
     * @param txt String
     * @param data String
     */
    private void createMonthButton(String txt, String data) {
        final Button btn = new Button(this, SWT.TOGGLE);
        btn.addSelectionListener(listener);
        btn.setData(data);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false, false);
        gd.widthHint = 80;
        btn.setLayoutData(gd);
        btn.setText(txt);

        buttons.add(btn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Group#checkSubclass()
     */
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * Update GUI according to the task input.
     * 
     * @param task ScheduleTask
     */
    public void update(ScheduleTask task) {
        String monthString = task.getMonth();
        if (monthString == null || monthString.length() == 0) {
            return;
        }
        if (monthString.equals("*")) { //$NON-NLS-1$
            setAllButtonSelection(true);
            addMonths("*"); //$NON-NLS-1$
            return;
        }

        String[] monthArrays = monthString.split(","); //$NON-NLS-1$
        for (int i = 0; i < monthArrays.length; i++) {
            String month = monthArrays[i];
            if (month.indexOf("-") != -1) { //$NON-NLS-1$
                for (int j = Integer.valueOf(month.split("-")[0]); j <= Integer.valueOf(month.split("-")[1]); j++) { //$NON-NLS-1$ //$NON-NLS-2$
                    if (j < 1 || j > 12) {
                        continue;
                    }
                    buttons.get(j - 1).setSelection(true);
                    addMonths(String.valueOf(j));
                }
            } else {
                if (Integer.valueOf(month) < 1 || Integer.valueOf(month) > 12) {
                    continue;
                }
                buttons.get(Integer.valueOf(month) - 1).setSelection(true);
                addMonths(month);
            }
        }
    }

    /**
     * Sets all Buttons' Selection status.
     * @param isSelection boolean
     */
    private void setAllButtonSelection(boolean isSelection) {
        for (Button button : buttons) {
            button.setSelection(isSelection);
        }
    }

    /* (non-Javadoc)
     * @see org.talend.scheduler.ui.IWidgetEnableListener#enableAll(boolean)
     */
    public void enableAll(boolean enable) {
        for (Button button : buttons) {
            button.setEnabled(enable);
        }
    }
}
