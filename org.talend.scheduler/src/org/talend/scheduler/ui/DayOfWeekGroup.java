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

import org.talend.scheduler.i18n.Messages;
import org.talend.scheduler.core.ScheduleTask;

/**
 * 
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DayOfWeekGroup extends Group implements IWidgetEnableListener {

    private static final int WIDTHHINT = 85;

    private static final int ZERO = 0;

    private static final int NUM_COLUMN = 3;

    IResultChangedListener changeListener = null;

    /**
     * 
     * Sets changed listener.
     * 
     * @param changeListener IResultChangedListener
     */
    public void setChangeListener(IResultChangedListener changeListener) {
        this.changeListener = changeListener;
    }

    List<Button> buttons = new ArrayList<Button>();

    int buttonStyle = SWT.TOGGLE;

    /**
     * 
     * An inner class extending SelectionAdapter.
     * 
     * $Id$
     * 
     */
    class WeekDayButtonListener extends SelectionAdapter {

        private static final int FIXED_NUM_FOR_SELECTED_BUTTON = 38;

        private static final int SELECTED_BUTTON_NUM = 7;

        int[] selectedButton = new int[SELECTED_BUTTON_NUM];

        WeekDayButtonListener() {

            for (int j = 0; j < selectedButton.length; j++) {
                selectedButton[j] = FIXED_NUM_FOR_SELECTED_BUTTON;
            }
        }

        /**
         * @param e SelectionEvent.
         */
        public void widgetSelected(SelectionEvent e) {

            Iterator it = buttons.iterator();
            while (it.hasNext()) {
                Button b1 = (Button) it.next();
                int tmp = Integer.parseInt((String) b1.getData());
                if (b1.getSelection()) {
                    selectedButton[tmp] = tmp;
                } else {
                    selectedButton[tmp] = FIXED_NUM_FOR_SELECTED_BUTTON;
                }
            }
            boolean firstFlag = true;
            String cronRunningDays = ""; //$NON-NLS-1$
            if ("0123456".equalsIgnoreCase("" + selectedButton[0] + //$NON-NLS-1$ //$NON-NLS-2$
                    selectedButton[1] + selectedButton[2] + selectedButton[3] + selectedButton[4] + selectedButton[5]
                    + selectedButton[6])) {
                cronRunningDays = "*"; //$NON-NLS-1$
            } else {
                for (int i = 0; i < selectedButton.length; i++) {
                    if (selectedButton[i] != FIXED_NUM_FOR_SELECTED_BUTTON) {
                        if (firstFlag) {
                            firstFlag = false;
                            cronRunningDays += selectedButton[i];
                        } else {
                            cronRunningDays += "," + selectedButton[i]; //$NON-NLS-1$
                        }
                    }
                }
            }

            changeListener.dayofWeekChanged(cronRunningDays);

        }
    }

    public DayOfWeekGroup(Composite parent, int style) {
        super(parent, style);
        setText(Messages.getString("DayOfWeekGroup.weekGroupTitle")); //$NON-NLS-1$
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUM_COLUMN;
        setLayout(gridLayout);

        createButton(Messages.getString("DayOfWeekGroup.mondayName"), "1"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.tuesdayName"), "2"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.wednesdayName"), "3"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.thursdayName"), "4"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.fridayName"), "5"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.saturdayName"), "6"); //$NON-NLS-1$ //$NON-NLS-2$

        createButton(Messages.getString("DayOfWeekGroup.sundayName"), "0"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    /**
     * 
     * Creates button by input text and data.
     * 
     * @param txt String
     * @param data String
     */
    private void createButton(String txt, String data) {
        final Button btn = new Button(this, SWT.TOGGLE);
        btn.addSelectionListener(new WeekDayButtonListener());
        btn.setData(data);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false, false);
        gd.widthHint = WIDTHHINT;
        btn.setLayoutData(gd);
        btn.setText(txt);
        buttons.add(btn);
    }

    /**
     * @Override
     */
    public void dispose() {
        super.dispose();
    }

    /**
     * @Override
     */
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * Updates by input task.
     * 
     * @param task ScheduleTask
     */
    public void update(ScheduleTask task) {

        String cronRunningDays = task.getWeekly();
        if (cronRunningDays.equalsIgnoreCase("*")) { //$NON-NLS-1$
            Iterator it = buttons.iterator();
            while (it.hasNext()) {
                Button b1 = (Button) it.next();
                b1.setSelection(true);
            }

        } else {
            while (cronRunningDays != null && cronRunningDays.length() != 0) {
                int douIndex = cronRunningDays.indexOf(","); //$NON-NLS-1$
                int tmp = 0;
                if (douIndex == -1) {
                    tmp = Integer.parseInt(cronRunningDays);
                    cronRunningDays = ""; //$NON-NLS-1$
                } else {
                    tmp = Integer.parseInt(cronRunningDays.substring(0, douIndex));
                    cronRunningDays = cronRunningDays.substring(douIndex + 1);
                }
                if (tmp == ZERO) {
                    buttons.get(6).setSelection(true);
                } else {
                    buttons.get(tmp - 1).setSelection(true);
                }
            }
        }

    }

    /**
     * Enables all buttons.
     * 
     * @param enable boolean
     */
    public void enableAll(boolean enable) {
        for (Button b : buttons) {
            b.setEnabled(enable);
        }

    }
}
