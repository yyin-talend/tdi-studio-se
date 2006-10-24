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
import org.eclipse.swt.widgets.Label;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.StringUtils;
import org.talend.scheduler.i18n.Messages;

/**
 * 
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DayOfMonthGroup extends Group implements IWidgetEnableListener {

    private static final int WIDTHHINT = 58;

    private static final int DAY_SIZE = 31;

    private static final int NUM_LABEL = 32;

    private static final int NUM_COLUMN = 10;

    private IResultChangedListener changeListener = null;

    private static final String ALL_DAYS = "*"; //$NON-NLS-1$

    private List<String> days = new ArrayList<String>();

    private List<Button> buttons = new ArrayList<Button>();

    private Button btnEveryday;

    /**
     * 
     * Sets the changed listener.
     * @param changeListener  IResultChangedListener
     */
    public void setChangeListener(IResultChangedListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 
     * Adds day.
     * @param day String
     */
    private void addDays(String day) {
        if (day == null) {
            return;
        }
        days.add(day);
    }
 
    /**
     * 
     * Deletes day.
     * @param day String
     */
    private void delDays(String day) {
        if (day == null) {
            return;
        }

        days.remove(day);
    }

    /**
     * Displays days.
     */
    @SuppressWarnings("unchecked")
    private void displayDays() {
        Collections.sort(days, new Comparator() {

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
        for (Iterator it = days.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(","); //$NON-NLS-1$
            }
        }

        changeListener.dayofMonthChanged(StringUtils.format(sb.toString()));
    }

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public DayOfMonthGroup(Composite parent, int style) {
        super(parent, style);
        setText(Messages.getString("DayOfMonthGroup.dayMonthGroupTitle")); //$NON-NLS-1$
        final GridLayout gridLayout = new GridLayout();
        
        gridLayout.numColumns = NUM_COLUMN;
        this.setLayout(gridLayout);
        
        for (int i = 1; i < NUM_LABEL; i++) {
            createDayButton(String.valueOf(i));
        }

        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        btnEveryday = new Button(this, SWT.TOGGLE);
        btnEveryday.setData(ALL_DAYS);
        btnEveryday.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button btn = (Button) e.getSource();
                // Select the button
                if (btn.getSelection()) {
                    days.clear();
                    addDays((String) btn.getData());
                    setAllDaysSelection(true);
                } else {
                    delDays((String) btn.getData());
                    setAllDaysSelection(false);

                    for (Button button : buttons) {
                        if (button.getSelection()) {
                            addDays((String) button.getData());
                        }
                    }
                }

                displayDays();
            }

        });
        final GridData gridData1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
        gridData1.widthHint = WIDTHHINT;
        btnEveryday.setLayoutData(gridData1);
        btnEveryday.setText(Messages.getString("DayOfMonthGroup.everydayText")); //$NON-NLS-1$
        //
    }

    /**
     * Select all days.
     * 
     * @param isSelected boolean
     */
    private void setAllDaysSelection(boolean isSelected) {
        for (Button button : buttons) {
            button.setSelection(isSelected);
        }
    }

    /**
     *  An inner class.
     */
    private SelectionAdapter listener = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            Button btn = (Button) e.getSource();
            // Select the button
            if (btn.getSelection()) {
                addDays((String) btn.getData());
                if (days.size() == DAY_SIZE) {
                    days.clear();
                    days.add(ALL_DAYS);
                    btnEveryday.setSelection(true);
                }
            } else {
                if (days.size() == 1 && days.get(0).equals(ALL_DAYS)) {
                    days.clear();
                    for (int i = 1; i <DAY_SIZE + 1; i++) {
                        days.add(String.valueOf(i));
                    }
                }
                delDays((String) btn.getData());
                btnEveryday.setSelection(false);
            }

            displayDays();
        }
    };

    /**
     * 
     * Creates day buttons by input text.
     * @param txt String
     */
    private void createDayButton(String txt) {
        Button btn1 = new Button(this, SWT.TOGGLE);
        btn1.setData(txt);
        btn1.addSelectionListener(listener);
        btn1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        btn1.setText(txt);
        buttons.add(btn1);
    }

    /**
     * @Override
     *  
     */
    public void dispose() {
        super.dispose();
    }

    /**
     * @Override
     */   
    protected void checkSubclass() {
    }

    /**
     * 
     * Updates by input task.
     * @param task ScheduleTask.
     */
    public void update(ScheduleTask task) {
        String dayString = task.getDay();
        if (dayString == null || dayString.length() == 0) {
            return;
        }
        if (dayString.equals("*")) { //$NON-NLS-1$
            btnEveryday.setSelection(true);
            setAllDaysSelection(true);
            addDays("*"); //$NON-NLS-1$
            return;
        }

        String[] daysTmp = dayString.split(","); //$NON-NLS-1$
        for (int i = 0; i < daysTmp.length; i++) {
            String day = daysTmp[i];
            if (day.indexOf("-") != -1) {
                for (int j = Integer.valueOf(day.split("-")[0]); j <= Integer.valueOf(day.split("-")[1]); j++) {

                    if (j < 1 || j > DAY_SIZE) {
                        continue;
                    }
                    buttons.get(j - 1).setSelection(true);
                    addDays(String.valueOf(j));
                }
            } else {
                if (Integer.valueOf(day) < 1 || Integer.valueOf(day) > DAY_SIZE) {
                    continue;
                }
                buttons.get(Integer.valueOf(day) - 1).setSelection(true);
                addDays(day);
            }
        }
    }

    /**
     * @param enabled boolean.
     */
    public void enableAll(boolean enabled) {
        btnEveryday.setEnabled(enabled);
        for (Iterator<Button> iter = buttons.iterator(); iter.hasNext();) {
            iter.next().setEnabled(enabled);
        }
        this.setEnabled(enabled);
    }
}
