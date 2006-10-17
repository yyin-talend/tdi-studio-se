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
import org.eclipse.swt.layout.GridData;
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
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.scheduler.core.ScheduleTask;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SchedulerView extends ViewPart {
	private TableViewer viewer;

	private Table table;

	private Action addTaskAction;

	private Action editTaskAction;

	private Action delTaskAction;

	private Action genCrontabAction;

	private List<ScheduleTask> tasks;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	List getAllSchedulTasks() {
		tasks = new ArrayList<ScheduleTask>();
		// todo

		return tasks;
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SchedulerView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI |SWT.FULL_SELECTION| SWT.H_SCROLL
				| SWT.V_SCROLL);

		table = viewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// TableColumn colButton = new TableColumn(table, SWT.LEFT);
		TableColumn colMinute = new TableColumn(table, SWT.LEFT);
		TableColumn colHour = new TableColumn(table, SWT.LEFT);
		TableColumn colDay = new TableColumn(table, SWT.LEFT);
		TableColumn colMonth = new TableColumn(table, SWT.LEFT);
		TableColumn colWeekday = new TableColumn(table, SWT.LEFT);
		TableColumn colCommand = new TableColumn(table, SWT.LEFT);

		colMinute.setText("Minute");
		colHour.setText("Hour");
		colDay.setText("Day");
		colMonth.setText("Month");
		colWeekday.setText("Weekday");
		colCommand.setText("Command");

		// colButton.setWidth(80);
		colMinute.setWidth(50);
		colHour.setWidth(50);
		colDay.setWidth(50);
		colMonth.setWidth(50);
		colWeekday.setWidth(80);
		colCommand.setWidth(150);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getAllSchedulTasks());

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SchedulerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		// fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/*
	 * private void fillLocalPullDown(IMenuManager manager) {
	 * manager.add(addTaskAction); manager.add(editTaskAction); manager.add(new
	 * Separator()); manager.add(delTaskAction); }
	 */

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addTaskAction);
		manager.add(editTaskAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(delTaskAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(genCrontabAction);
		manager.add(addTaskAction);
		manager.add(editTaskAction);
		manager.add(new Separator());
		manager.add(delTaskAction);

	}

	class ModifyTaskAction extends SelectionProviderAction {

		public ModifyTaskAction(ISelectionProvider provider, String text) {
			super(provider, text);
			setEnabled(false);
		}

		public void selectionChanged(IStructuredSelection selection) {
			if (selection.size() != 1) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}

		public void run() {
			editSelectedTaskProperty();
		}

	}

	class DelTaskAction extends SelectionProviderAction {

		public DelTaskAction(ISelectionProvider provider, String text) {
			super(provider, text);
			setEnabled(false);
		}

		public void selectionChanged(IStructuredSelection selection) {
			if (selection.size() == 0) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}

		public void run() {
			boolean sure = false;
			sure = MessageDialog.openConfirm(viewer.getControl().getShell(),
					"Confirm", "Do you want to delete the item(s)");
			if (sure) {
				StructuredSelection selection = (StructuredSelection) viewer
						.getSelection();
				List list = selection.toList();
				tasks.removeAll(list);
				viewer.remove(list.toArray());
			} else {
				return;
			}
		}

	}

	private void makeActions() {
		final Image imgAdd = ImageProvider.getImage(EImage.ADD_ICON);
		final Image imgDel = ImageProvider.getImage(EImage.DELETE_ICON);
		final Image imgSave = ImageProvider.getImage(EImage.SAVE_ICON);

		final ImageDescriptor imgDesAdd = ImageDescriptor
				.createFromImage(imgAdd);
		final ImageDescriptor imgDesDel = ImageDescriptor
				.createFromImage(imgDel);
		final ImageDescriptor imgDesSave = ImageDescriptor
				.createFromImage(imgSave);

		addTaskAction = new Action() {
			public void run() {
				addScheduleTask();
			}
		};
		addTaskAction.setText("Add Task");
		addTaskAction.setToolTipText("Add Task");
		addTaskAction.setImageDescriptor(imgDesAdd);

		editTaskAction = new ModifyTaskAction(viewer, "Modify Task");
		editTaskAction.setToolTipText("Modify Task");
		editTaskAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJS_INFO_TSK));
		
		delTaskAction = new DelTaskAction(viewer, "Delete Task");
		delTaskAction.setToolTipText("Delete Task");
		delTaskAction.setImageDescriptor(imgDesDel);
		
		genCrontabAction = new Action() {
			public void run() {
				genCrontab2File();
			}
		};
		genCrontabAction.setText("Generate crontab file");
		genCrontabAction.setImageDescriptor(imgDesSave);
		
	}

	/**
	 * Generate the crontab file according to this table
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
				}
			}
		}
	}

	private void print2File(PrintWriter pw, List<ScheduleTask> list) {
		for (ScheduleTask task : list) {
			StringBuilder sb = new StringBuilder();
			String blank = " ";
			sb.append(task.getMinute()).append(blank).append(task.getHour())
					.append(blank).append(task.getDay()).append(blank).append(
							task.getMonth()).append(blank).append(
							task.getWeekly()).append(blank).append(
							task.getCommand());

			pw.println(sb.toString());
		}
	}

	private void addScheduleTask() {
		try {
			SchedulerTaskPropertyDialog d = new SchedulerTaskPropertyDialog(
					getViewSite().getShell());
			if (d.open() != IDialogConstants.OK_ID) {
				return;
			}
			ScheduleTask newTask = d.getResult();
			tasks.add(newTask);
			viewer.add(newTask);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	protected void editSelectedTaskProperty() {
		ScheduleTask task = getSelectedTask();
		SchedulerTaskPropertyDialog d = new SchedulerTaskPropertyDialog(
				getViewSite().getShell(), task);

		if (d.open() != IDialogConstants.OK_ID) {
			return;
		}

		ScheduleTask newTask = d.getResult();
		viewer.refresh(newTask);
	}

	private ScheduleTask getSelectedTask() {
		Object o = ((IStructuredSelection) viewer.getSelection())
				.getFirstElement();
		return (ScheduleTask) o;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				new ModifyTaskAction(viewer, "Modify Task").run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}

class ViewContentProvider implements IStructuredContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		return ((List) parent).toArray();
	}
}

class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getColumnText(Object obj, int index) {

		ScheduleTask schedule = (ScheduleTask) obj;

		switch (index) {
		// case 0:
		//
		// // Button buttonModify = schedule.getButtonModify();
		// //
		// // // Set attributes of the button
		// // buttonModify.setText("Modify...");
		// // buttonModify.computeSize(SWT.DEFAULT, table.getItemHeight());
		// //
		// // // Set attributes of the editor
		// // editor.grabHorizontal = true;
		// // editor.minimumHeight = buttonModify.getSize().y;
		// // editor.minimumWidth = buttonModify.getSize().x;
		// //
		// // editor.setEditor(buttonModify, editor.getItem(), 0);
		// //
		// // buttonModify.addSelectionListener(new SelectionAdapter() {
		// //
		// // public void widgetSelected(SelectionEvent e) {
		// //
		// // editSelectedTaskProperty();
		// // }
		// // });
		// return "";
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
			return "";

		}
	}

	public Image getColumnImage(Object obj, int index) {
		return null;
	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_ELEMENT);
	}

}