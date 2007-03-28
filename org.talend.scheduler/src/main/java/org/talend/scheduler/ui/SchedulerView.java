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
package org.talend.scheduler.ui;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.scheduler.SchedulerPlugin;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.i18n.Messages;

/**
 * This is a scheduler viewer to diaplay all the scheduler task. <br/>
 * 
 * $Id$
 * 
 */
public class SchedulerView extends ViewPart {

    public static final int COLUMN_WIDTH = 100;

    private Action addTaskAction;

    private Action editTaskAction;

    private Action delTaskAction;

    private Action genCrontabAction;

    private List<ScheduleTask> tasks;

    private TableViewerCreator tableViewerCreator;

    /**
     * Gets all schedul tasks.
     * 
     * @return List < ScheduleTask >.
     */
    List<ScheduleTask> getAllSchedulTasks() {
        tasks = new ArrayList<ScheduleTask>();
        // todo
        return tasks;
    }

    /**
     * 
     * DOC dev SchedulerView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class SchedulerViewSorter extends ViewerSorter {
    }

    /**
     * The constructor.
     */
    public SchedulerView() {
    }

    /*
     * implememnts org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
        tableViewerCreator = new TableViewerCreator(parent);

        initialTable(tableViewerCreator.createTable());
        tableViewerCreator.init();
        TableViewer viewer = tableViewerCreator.getTableViewer();

        SchedulerViewProvider provider = new SchedulerViewProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(provider);
        viewer.setSorter(new SchedulerViewSorter());
        viewer.setCellEditors(null);
        viewer.setInput(getAllSchedulTasks());

        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();

    }

    /**
     * Initials the table.
     * 
     * @param table Table
     */
    private void initialTable(Table table) {
        TableColumn colMinute = new TableColumn(table, SWT.LEFT);
        TableColumn colHour = new TableColumn(table, SWT.LEFT);
        TableColumn colDay = new TableColumn(table, SWT.LEFT);
        TableColumn colMonth = new TableColumn(table, SWT.LEFT);
        TableColumn colWeekday = new TableColumn(table, SWT.LEFT);
        TableColumn colCommand = new TableColumn(table, SWT.LEFT);

        colMinute.setText(Messages.getString("SchedulerView.tableColumn.minute")); //$NON-NLS-1$
        colHour.setText(Messages.getString("SchedulerView.tableColumn.hour")); //$NON-NLS-1$
        colDay.setText(Messages.getString("SchedulerView.tableColumn.day")); //$NON-NLS-1$
        colMonth.setText(Messages.getString("SchedulerView.tableColumn.month")); //$NON-NLS-1$
        colWeekday.setText(Messages.getString("SchedulerView.tableColumn.weekday")); //$NON-NLS-1$
        colCommand.setText(Messages.getString("SchedulerView.tableColumn.command")); //$NON-NLS-1$

        colMinute.setWidth(COLUMN_WIDTH);
        colHour.setWidth(COLUMN_WIDTH);
        colDay.setWidth(COLUMN_WIDTH);
        colMonth.setWidth(COLUMN_WIDTH);
        colWeekday.setWidth(COLUMN_WIDTH);
        colCommand.setWidth(COLUMN_WIDTH * 3);

    }

    /**
     * HookS context menu.
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager(Messages.getString("SchedulerView.popupMenuName")); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                SchedulerView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(tableViewerCreator.getTableViewer().getControl());
        tableViewerCreator.getTableViewer().getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, tableViewerCreator.getTableViewer());

    }

    /**
     * Contributes to action bars.
     */
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        // fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    /**
     * Fills context menu.
     * 
     * @param manager IMenuManager
     */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(addTaskAction);
        manager.add(editTaskAction);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(delTaskAction);
    }

    /**
     * Fills local toolbar.
     * 
     * @param manager IToolBarManager
     */
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(genCrontabAction);
        manager.add(addTaskAction);
        manager.add(editTaskAction);
        manager.add(new Separator());
        manager.add(delTaskAction);

    }

    /**
     * 
     * DOC dev SchedulerView class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class ModifyTaskAction extends SelectionProviderAction {

        public ModifyTaskAction(ISelectionProvider provider, String text) {
            super(provider, text);
            setEnabled(false);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
         */
        public void selectionChanged(IStructuredSelection selection) {
            if (selection.size() != 1) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run() {
            editSelectedTaskProperty();
        }

    }

    /**
     * Uses this action to delete scheduler task. <br/> $Id: org.eclipse.jdt.ui.prefs,v 1.1 2006/10/20 16:09:46 qianbing
     * Exp $
     */
    class DelTaskAction extends SelectionProviderAction {

        public DelTaskAction(ISelectionProvider provider, String text) {
            super(provider, text);
            setEnabled(false);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
         */
        public void selectionChanged(IStructuredSelection selection) {
            if (selection.size() == 0) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run() {
            boolean sure = false;
            sure = MessageDialog.openConfirm(tableViewerCreator.getTableViewer().getControl().getShell(), Messages
                    .getString("SchedulerView.deleteDialogTitle"), //$NON-NLS-1$
                    Messages.getString("SchedulerView.deleteDialogContent")); //$NON-NLS-1$
            if (sure) {
                StructuredSelection selection = (StructuredSelection) tableViewerCreator.getTableViewer()
                        .getSelection();
                List list = selection.toList();
                tasks.removeAll(list);
                tableViewerCreator.getTableViewer().remove(list.toArray());
            } else {
                return;
            }
        }

    }

    /**
     * Creates all the actions.
     */
    private void makeActions() {
        final Image imgAdd = ImageProvider.getImage(EImage.ADD_ICON);
        final Image imgDel = ImageProvider.getImage(EImage.DELETE_ICON);
        final Image imgSave = ImageProvider.getImage(EImage.SAVE_ICON);

        final ImageDescriptor imgDesAdd = ImageDescriptor.createFromImage(imgAdd);
        final ImageDescriptor imgDesDel = ImageDescriptor.createFromImage(imgDel);
        final ImageDescriptor imgDesSave = ImageDescriptor.createFromImage(imgSave);

        addTaskAction = new Action() {

            public void run() {
                addScheduleTask();
            }
        };
        addTaskAction.setText(Messages.getString("SchedulerView.addTaskText")); //$NON-NLS-1$
        addTaskAction.setToolTipText(Messages.getString("SchedulerView.addTaskToolTip")); //$NON-NLS-1$
        addTaskAction.setImageDescriptor(imgDesAdd);

        editTaskAction = new ModifyTaskAction(tableViewerCreator.getTableViewer(), Messages
                .getString("SchedulerView.modifyTaskText")); //$NON-NLS-1$
        editTaskAction.setToolTipText(Messages.getString("SchedulerView.modifyTaskToolTip")); //$NON-NLS-1$
        editTaskAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                ISharedImages.IMG_OBJS_INFO_TSK));

        delTaskAction = new DelTaskAction(tableViewerCreator.getTableViewer(), Messages
                .getString("SchedulerView.deleteTaskText")); //$NON-NLS-1$
        delTaskAction.setToolTipText(Messages.getString("SchedulerView.deleteTaskToolTip")); //$NON-NLS-1$
        delTaskAction.setImageDescriptor(imgDesDel);

        genCrontabAction = new Action() {

            public void run() {
                genCrontab2File();
            }
        };
        genCrontabAction.setText(Messages.getString("SchedulerView.generateFileToolTip")); //$NON-NLS-1$
        genCrontabAction.setImageDescriptor(imgDesSave);

    }

    /**
     * Generate the crontab file according to this table.
     */
    protected void genCrontab2File() {
        FileDialog fd = new FileDialog(this.getSite().getShell(), SWT.SAVE);
        String fileName = fd.open();
        if (fileName == null) {
            return;
        }
        PrintWriter pw = null;
        // output
        try {
            pw = new PrintWriter(new FileWriter(fileName), true);
            print2File(pw, tasks);
        } catch (Exception e) {
            // todo
            e.printStackTrace();
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    SchedulerPlugin.log(e);
                }
            }
        }
    }

    /**
     * Prints List< ScheduleTask > to plain file.
     * 
     * @param pw PrintWriter
     * @param list List< ScheduleTask >
     */
    private void print2File(PrintWriter pw, List<ScheduleTask> list) {
        for (ScheduleTask task : list) {
            StringBuilder sb = new StringBuilder();
            String blank = " "; //$NON-NLS-1$
            sb.append(task.getMinute()).append(blank).append(task.getHour()).append(blank).append(task.getDay())
                    .append(blank).append(task.getMonth()).append(blank).append(task.getWeekly()).append(blank).append(
                            task.getCommand());

            pw.println(sb.toString());
        }
    }

    /**
     * Adds a scheduler task to scheduler viewer.
     */
    private void addScheduleTask() {
        try {
            SchedulerTaskPropertyDialog d = new SchedulerTaskPropertyDialog(getViewSite().getShell());
            if (d.open() != IDialogConstants.OK_ID) {
                return;
            }
            ScheduleTask newTask = d.getResult();
            tasks.add(newTask);
            tableViewerCreator.getTableViewer().add(newTask);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * Edits a scheduler task in scheduler viewer.
     */
    protected void editSelectedTaskProperty() {
        ScheduleTask task = getSelectedTask();
        SchedulerTaskPropertyDialog d = new SchedulerTaskPropertyDialog(getViewSite().getShell(), task);

        if (d.open() != IDialogConstants.OK_ID) {
            return;
        }

        ScheduleTask newTask = d.getResult();
        tableViewerCreator.getTableViewer().refresh(newTask);
    }

    /**
     * Gets the selected one scheduler task.
     * 
     * @return ScheduleTask
     */
    private ScheduleTask getSelectedTask() {
        Object o = ((IStructuredSelection) tableViewerCreator.getTableViewer().getSelection()).getFirstElement();
        return (ScheduleTask) o;
    }

    /**
     * Hooks doubleclick action.
     */
    private void hookDoubleClickAction() {
        tableViewerCreator.getTableViewer().addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
//                new ModifyTaskAction(tableViewerCreator.getTableViewer(), Messages
//                        .getString("SchedulerView.modifyTaskTitle")).run(); //$NON-NLS-1$
                editTaskAction.run();
            }
        });
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        tableViewerCreator.getTableViewer().getControl().setFocus();
    }

}

/**
 * Scheduler viewer's label provider and content provider.
 * 
 * @author dev
 * 
 */
class SchedulerViewProvider extends LabelProvider implements IStructuredContentProvider, ITableLabelProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#dispose()
     */
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object parent) {
        return ((List) parent).toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object obj, int index) {

        ScheduleTask schedule = (ScheduleTask) obj;

        switch (index) {

        case 0:
            return schedule.getMinute();
        case 1:
            return schedule.getHour();
        case 2:
            return schedule.getDay();
        case 3:
            return schedule.getMonth();
        case 4:
            return schedule.getWeekly();
        case 5:
            return schedule.getCommand();
        default:
            return ""; //$NON-NLS-1$

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object obj, int index) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object obj) {
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }

}
