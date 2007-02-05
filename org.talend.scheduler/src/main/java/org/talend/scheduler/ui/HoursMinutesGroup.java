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
import org.talend.scheduler.i18n.Messages;

/**
 * 
 * This class is using for hour and minute group.<br/>
 * 
 * $Id$
 * 
 */
public class HoursMinutesGroup extends Group implements IWidgetEnableListener {

    IResultChangedListener changeListener = null;

    /**
     * Sets changed listener.
     * 
     * @param changeListener IResultChangedListener
     */
    public void setChangeListener(IResultChangedListener changeListener) {
        this.changeListener = changeListener;
    }

    ArrayList<Button> hoursButtons = new ArrayList<Button>();

    ArrayList<Button> minutesButtons = new ArrayList<Button>();

    /**
     * 
     * This class is a button listener using for minute buttons.
     * 
     * $Id$
     * 
     */
    class MinutesButtonListener extends SelectionAdapter {

        private static final int SELECTED_BUTTON_VALUE = 38;

        private static final int SELECTED_BUTTON_NUM = 12;

        int[] selectedButton = new int[SELECTED_BUTTON_NUM];

        MinutesButtonListener() {
            for (int j = 0; j < selectedButton.length; j++) {
                selectedButton[j] = SELECTED_BUTTON_VALUE;
            }
        }

        /**
         * @param e SelectionEvent
         */
        public void widgetSelected(SelectionEvent e) {
            Iterator it = minutesButtons.iterator();
            while (it.hasNext()) {
                Button b1 = (Button) it.next();
                int tmp = Integer.parseInt((String) b1.getData());
                if (b1.getSelection()) {
                    selectedButton[tmp] = Integer.parseInt(b1.getText());
                } else {
                    selectedButton[tmp] = SELECTED_BUTTON_VALUE;
                }
            }
            boolean firstFlag = true;
            String cronRunningMinutes = ""; //$NON-NLS-1$
            for (int i = 0; i < selectedButton.length; i++) {
                if (selectedButton[i] != SELECTED_BUTTON_VALUE) {
                    if (firstFlag) {
                        firstFlag = false;
                        cronRunningMinutes += selectedButton[i];
                    } else {
                        cronRunningMinutes += "," + selectedButton[i]; //$NON-NLS-1$
                    }
                }
            }
            changeListener.minuteChanged(cronRunningMinutes);
        }
    }

    /**
     * 
     * A listener for hour buttons.
     * 
     * $Id$
     * 
     */
    class HoursButtonListener extends SelectionAdapter {

        private static final int SELECTED_BUTTON_VALUE = 38;

        private static final int SELECTED_BUTTON_NUM = 24;

        int [] selectedButton = new int[SELECTED_BUTTON_NUM];

        HoursButtonListener() {
            for (int j = 0; j < selectedButton.length; j++) {
                selectedButton[j] = SELECTED_BUTTON_VALUE;
            }
        }

        /**
         * @override
         * @param e SelectionEvent
         */
        public void widgetSelected(SelectionEvent e) {
            Iterator it = hoursButtons.iterator();
            while (it.hasNext()) {
                Button b1 = (Button) it.next();
                int tmp = Integer.parseInt((String) b1.getData());
                if (b1.getSelection()) {
                    selectedButton[tmp] = tmp;
                } else {
                    selectedButton[tmp] = SELECTED_BUTTON_VALUE;
                }
            }
            boolean firstFlag = true;
            boolean segmentFlag = true;
            String cronRunningHours = ""; //$NON-NLS-1$
            int start = -1;
            int end = 0;
            for (int j = 0; j < selectedButton.length; j++) {
                if (selectedButton[j] != SELECTED_BUTTON_VALUE) {

                    if (firstFlag) {
                        firstFlag = false;
                        start = j;
                        end = j;
                        if (j != 23) {
                            continue;
                        }
                    } else {
                        end = j;
                    }
                }
                if (start == 0 && end == 23) {
                    cronRunningHours = "*"; //$NON-NLS-1$
                } else {
                    if (!firstFlag && start >= end) {

                        firstFlag = true;
                        if (segmentFlag) {
                            segmentFlag = false;
                            cronRunningHours += selectedButton[start];
                        } else {
                            cronRunningHours += "," //$NON-NLS-1$
                                    + selectedButton[start];
                        }
                    } else if (!firstFlag && (end < j || end == 23)) {

                        if (segmentFlag) {
                            segmentFlag = false;
                            cronRunningHours += start + "-" + end; //$NON-NLS-1$
                        } else {
                            cronRunningHours += "," + start + "-" + end; //$NON-NLS-1$ //$NON-NLS-2$
                        } //$NON-NLS-1$ //$NON-NLS-2$
                        firstFlag = true;
                    }
                }
            }
            changeListener.hourChanged(cronRunningHours);
        }

    }

    public HoursMinutesGroup(Composite parent, int style) {
        super(parent, style);
        setText(Messages.getString("HoursMinutesGroup.hoursminutesGroupTitle")); //$NON-NLS-1$
        final GridLayout gridLayout1 = new GridLayout();
        gridLayout1.verticalSpacing = 4;
        gridLayout1.marginTop = 10;
        gridLayout1.marginLeft = 15;
        gridLayout1.marginHeight = 8;
        gridLayout1.marginBottom = 10;
        gridLayout1.horizontalSpacing = 6;
        gridLayout1.numColumns = 14;
        setLayout(gridLayout1);
        final Label hoursLabel = new Label(this, SWT.NONE);
        hoursLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
        hoursLabel.setText(Messages.getString("HoursMinutesGroup.hoursLabelText")); //$NON-NLS-1$
        final Label amLabel = new Label(this, SWT.NONE);
        amLabel.setText(Messages.getString("HoursMinutesGroup.amLabelText")); //$NON-NLS-1$
        int i = 0;
        for (i = 0; i < 12; i++) {
            createButton(String.valueOf(i), String.valueOf(i), new HoursButtonListener(), hoursButtons);
        }
        final Label pmLabel = new Label(this, SWT.NONE);
        pmLabel.setText(Messages.getString("HoursMinutesGroup.pmLabelText")); //$NON-NLS-1$
        for (; i < 24; i++) {
            createButton(String.valueOf(i), String.valueOf(i), new HoursButtonListener(), hoursButtons);
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
        // Begin modify the defect 0000220: Size of button
        new Label(this, SWT.NONE);
        final Label minutesLabel = new Label(this, SWT.NONE);
        minutesLabel.setText(Messages.getString("HoursMinutesGroup.minutesLabelText")); //$NON-NLS-1$
        new Label(this, SWT.NONE);
        // End modify the defect 0000220: Size of button
        for (int j = 0; j < 60; j += 5) {
            createButton(String.valueOf(j), String.valueOf(j / 5), new MinutesButtonListener(), minutesButtons);
        }
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
     * 
     * Updates.
     * 
     * @param task ScheduleTask.
     */
    public void update(ScheduleTask task) {

        updateSelectedMinutesButtons(task.getMinute());
        updateSelectedHoursButtons(task.getHour());
    }

    /**
     * 
     * Updates selected minute buttons.
     * 
     * @param cronRunningMinutes String
     */
    private void updateSelectedMinutesButtons(String cronRunningMinutes) {

        while (cronRunningMinutes != null && cronRunningMinutes.length() != 0) {
            int douIndex = cronRunningMinutes.indexOf(","); //$NON-NLS-1$
            int tmp = 0;
            if (douIndex == -1) {
                tmp = Integer.parseInt(cronRunningMinutes);
                cronRunningMinutes = ""; //$NON-NLS-1$

            } else {
                tmp = Integer.parseInt(cronRunningMinutes.substring(0, douIndex));
                cronRunningMinutes = cronRunningMinutes.substring(douIndex + 1);
            }
            minutesButtons.get(tmp / 5).setSelection(true);
        }
    }

    /**
     * 
     * Updates selected hour buttons.
     * 
     * @param cronRunningHours cronRunningHours
     */
    private void updateSelectedHoursButtons(String cronRunningHours) {
        if (cronRunningHours.equalsIgnoreCase("*")) { //$NON-NLS-1$
            Iterator it = hoursButtons.iterator();
            while (it.hasNext()) {
                Button b1 = (Button) it.next();
                b1.setSelection(true);
            }
        } else {
            while (cronRunningHours != null && cronRunningHours.length() != 0) {
                int douIndex = cronRunningHours.indexOf(","); //$NON-NLS-1$
                int tmp = 0;
                if (douIndex == -1) {
                    int gIndex = cronRunningHours.indexOf("-"); //$NON-NLS-1$
                    if (gIndex == -1) {
                        tmp = Integer.parseInt(cronRunningHours);
                        hoursButtons.get(tmp).setSelection(true);
                    } else {
                        int start = Integer.parseInt(cronRunningHours.substring(0, gIndex));
                        int end = Integer.parseInt(cronRunningHours.substring(gIndex + 1));
                        for (int n = start; n <= end; n++) {
                            hoursButtons.get(n).setSelection(true);
                        }
                    }
                    cronRunningHours = ""; //$NON-NLS-1$
                } else {
                    String tmp1 = cronRunningHours.substring(0, douIndex);
                    int gIndex = tmp1.indexOf("-"); //$NON-NLS-1$
                    if (gIndex == -1) {
                        tmp = Integer.parseInt(tmp1);
                        hoursButtons.get(tmp).setSelection(true);
                    } else {
                        int start = Integer.parseInt(tmp1.substring(0, gIndex));
                        int end = Integer.parseInt(tmp1.substring(gIndex + 1));
                        for (int n = start; n <= end; n++) {
                            hoursButtons.get(n).setSelection(true);
                        }
                    }
                    cronRunningHours = cronRunningHours.substring(douIndex + 1);
                }

            }
        }
    }

    /**
     * 
     * Creates button.
     * 
     * @param txt String
     * @param data String
     * @param listener SelectionAdapter
     * @param buttons the list of buttons
     */
    private void createButton(String txt, String data, SelectionAdapter listener, ArrayList<Button> buttons) {
        final Button btn = new Button(this, SWT.TOGGLE);
        btn.addSelectionListener(listener);
        btn.setData(data);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        btn.setLayoutData(gd);
        btn.setText(txt);
        buttons.add(btn);
    }

    /**
     * Enable all buttons.
     * 
     * @param enable boolean
     */
    public void enableAll(boolean enable) {
        for (Button b : hoursButtons) {
            b.setEnabled(enable);
        }
        for (Button b : minutesButtons) {
            b.setEnabled(enable);
        }
    }
}
