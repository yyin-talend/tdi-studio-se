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
package org.talend.designer.core.ui.hierarchy;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.DelegatingDropAdapter;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;
import org.talend.core.model.process.IProcess2;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.IJobHierarchyViewPart;
import org.talend.repository.ProjectManager;

/**
 * view showing the super jobs/sub jobs of its input.
 */
public class JobHierarchyViewPart extends ViewPart implements IJobHierarchyViewPart {

    public static final String ID = "org.talend.designer.core.ui.hierarchy.JobHierarchyViewPart";

    // input job or null
    private IProcess2 inputProcess;

    private ToggleViewAction[] fViewActions;

    private int fCurrentViewerIndex;

    private PageBook fPagebook;

    private Label fNoHierarchyShownLabel;

    private SashForm fTypeMethodsSplitter;

    private ViewForm fTypeViewerViewForm;

    private ViewForm fMethodViewerViewForm;

    private CLabel fMethodViewerPaneLabel;

    private PageBook fViewerbook;

    private JobHierarchyViewer[] fAllViewers;

    private Label fEmptyTypesViewer;

    private JobHierarchyLifeCycle fHierarchyLifeCycle;

    /**
     * Constructor
     */
    public JobHierarchyViewPart() {
        fHierarchyLifeCycle = new JobHierarchyLifeCycle();
        fViewActions = new ToggleViewAction[] { new ToggleViewAction(this, IJobHierarchyViewPart.HIERARCHY_MODE_SUBTYPES),
                new ToggleViewAction(this, IJobHierarchyViewPart.HIERARCHY_MODE_SUPERTYPES) };

    }

    String showEmptyLabel = "To display the job hierarchy, select a job, and select the \'Open Job Hierarchy\' menu option. Alternatively, you can drag and drop an job from repository view onto this view.";

    @Override
    public void createPartControl(Composite container) {
        fPagebook = new PageBook(container, SWT.NONE);
        // page 1 of page book (no hierarchy label)

        fNoHierarchyShownLabel = new Label(fPagebook, SWT.TOP + SWT.LEFT + SWT.WRAP);
        fNoHierarchyShownLabel.setText(showEmptyLabel);

        // page 2 of page book (viewers)
        fTypeMethodsSplitter = new SashForm(fPagebook, SWT.VERTICAL);
        fTypeMethodsSplitter.setVisible(false);

        fTypeViewerViewForm = new ViewForm(fTypeMethodsSplitter, SWT.NONE);

        Control typeViewerControl = createTypeViewerControl(fTypeViewerViewForm);
        fTypeViewerViewForm.setContent(typeViewerControl);

        fMethodViewerViewForm = new ViewForm(fTypeMethodsSplitter, SWT.NONE);
        fTypeMethodsSplitter.setWeights(new int[] { 99, 1 });

        Control methodViewerPart = createMethodViewerControl(fMethodViewerViewForm);
        fMethodViewerViewForm.setContent(methodViewerPart);

        fMethodViewerPaneLabel = new CLabel(fMethodViewerViewForm, SWT.NONE);
        fMethodViewerViewForm.setTopLeft(fMethodViewerPaneLabel);

        ToolBar methodViewerToolBar = new ToolBar(fMethodViewerViewForm, SWT.FLAT | SWT.WRAP);
        fMethodViewerViewForm.setTopCenter(methodViewerToolBar);

        fPagebook.showPage(fNoHierarchyShownLabel);

        initDragAndDrop();
        // set the filter menu items
        // IActionBars actionBars = getViewSite().getActionBars();
        // IMenuManager viewMenu = actionBars.getMenuManager();
        // for (int i = 0; i < fViewActions.length; i++) {
        // ToggleViewAction action = fViewActions[i];
        // viewMenu.add(action);
        // action.setEnabled(false);
        // }
        // viewMenu.add(new Separator());

    }

    private Control createMethodViewerControl(ViewForm methodViewerViewForm) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOC bqian Comment method "createTypeViewerControl".
     * 
     * @param typeViewerViewForm
     * @return
     */
    private Control createTypeViewerControl(Composite parent) {
        fViewerbook = new PageBook(parent, SWT.NULL);

        // Create the viewers
        JobHierarchyViewer superTypesViewer = new SuperJobHierarchyViewer(fViewerbook, fHierarchyLifeCycle, this);

        JobHierarchyViewer subTypesViewer = new SubJobHierarchyViewer(fViewerbook, fHierarchyLifeCycle, this);

        fAllViewers = new JobHierarchyViewer[2];
        fAllViewers[HIERARCHY_MODE_SUBTYPES] = subTypesViewer;
        fAllViewers[HIERARCHY_MODE_SUPERTYPES] = superTypesViewer;

        int currViewerIndex;
        try {
            // TODO
            // currViewerIndex = fDialogSettings.getInt(DIALOGSTORE_HIERARCHYVIEW);
            currViewerIndex = HIERARCHY_MODE_SUBTYPES;
            if (currViewerIndex < 0 || currViewerIndex > 1) {
                currViewerIndex = HIERARCHY_MODE_SUBTYPES;
            }
        } catch (NumberFormatException e) {
            currViewerIndex = HIERARCHY_MODE_SUBTYPES;
        }

        fEmptyTypesViewer = new Label(fViewerbook, SWT.TOP | SWT.LEFT | SWT.WRAP);

        for (int i = 0; i < fAllViewers.length; i++) {
            fAllViewers[i].setInput(fAllViewers[i]);
        }

        // force the update
        fCurrentViewerIndex = -1;
        setHierarchyMode(currViewerIndex);

        return fViewerbook;
    }

    @Override
    public void setFocus() {
        fPagebook.setFocus();

    }

    private JobHierarchyViewer getCurrentViewer() {
        return fAllViewers[fCurrentViewerIndex];
    }

    public void setHierarchyMode(int viewerIndex) {
        Assert.isNotNull(fAllViewers);
        if (viewerIndex < fAllViewers.length && fCurrentViewerIndex != viewerIndex) {
            fCurrentViewerIndex = viewerIndex;

            updateHierarchyViewer(true);
            if (inputProcess != null) {
                ISelection currSelection = getCurrentViewer().getSelection();
                if (currSelection == null || currSelection.isEmpty()) {
                    internalSelectType(inputProcess, false);
                    currSelection = getCurrentViewer().getSelection();
                }
            }
            updateTitle();

            // fDialogSettings.put(DIALOGSTORE_HIERARCHYVIEW, viewerIndex);
            getCurrentViewer().getTree().setFocus();
        }
        for (int i = 0; i < fViewActions.length; i++) {
            ToggleViewAction action = fViewActions[i];
            action.setChecked(fCurrentViewerIndex == action.getViewerIndex());
        }
    }

    private void internalSelectType(IProcess2 process, boolean reveal) {
        JobHierarchyViewer viewer = getCurrentViewer();
        // viewer.removePostSelectionChangedListener(fSelectionChangedListener);
        // viewer.setSelection(elem != null ? new StructuredSelection(elem) : StructuredSelection.EMPTY, reveal);
        // viewer.addPostSelectionChangedListener(fSelectionChangedListener);
    }

    /*
     * When the input changed or the hierarchy pane becomes visible, <code>updateHierarchyViewer<code> brings up the
     * correct view and refreshes the current tree
     */
    private void updateHierarchyViewer(final boolean doExpand) {
        if (inputProcess == null) {
            fNoHierarchyShownLabel.setText(showEmptyLabel);
            fPagebook.showPage(fNoHierarchyShownLabel);
        } else {
            if (getCurrentViewer().containsElements() != null) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        getCurrentViewer().updateContent(doExpand); // refresh
                    }
                };
                BusyIndicator.showWhile(getDisplay(), runnable);
                if (!isChildVisible(fViewerbook, getCurrentViewer().getControl())) {
                    setViewerVisibility(true);
                }
            } else {
                fEmptyTypesViewer.setText("there is some reason.");
                setViewerVisibility(false);
            }
        }
    }

    /*
     * Toggles between the empty viewer page and the hierarchy
     */
    private void setViewerVisibility(boolean showHierarchy) {
        if (showHierarchy) {
            fViewerbook.showPage(getCurrentViewer().getControl());
        } else {
            fViewerbook.showPage(fEmptyTypesViewer);
        }
    }

    public IProcess2 getInputProcess() {
        return inputProcess;
    }

    public void setInputProcess(IProcess2 process) {
        // TODO FOR HISTORY
        // if (element != null && !element.equals(fInputElement)) {
        // addHistoryEntry(element);
        // }
        updateInput(process);
    }

    public int getHierarchyMode() {
        return fCurrentViewerIndex;
    }

    private Display getDisplay() {
        if (fPagebook != null && !fPagebook.isDisposed()) {
            return fPagebook.getDisplay();
        }
        return null;
    }

    private boolean isChildVisible(Composite pb, Control child) {
        Control[] children = pb.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] == child && children[i].isVisible())
                return true;
        }
        return false;
    }

    /*
     * Changes the input to a new type
     * 
     * @param inputElement
     */
    private void updateInput(IProcess2 newProcess) {
        IProcess2 prevInput = inputProcess;

        // synchronized (this) {
        // if (fRestoreStateJob != null) {
        // fRestoreStateJob.cancel();
        // try {
        // fRestoreStateJob.join();
        // } catch (InterruptedException e) {
        // // ignore
        // } finally {
        // fRestoreStateJob = null;
        // }
        // }
        // }

        // Make sure the UI got repainted before we execute a long running
        // operation. This can be removed if we refresh the hierarchy in a
        // separate thread.
        // Work-around for http://dev.eclipse.org/bugs/show_bug.cgi?id=30881
        // processOutstandingEvents();
        if (newProcess == null) {
            clearInput();
        } else {
            inputProcess = newProcess;
            fNoHierarchyShownLabel.setText(Messages.getString("JobHierarchyMessages.JobHierarchyViewPart_createinput",
                    getJobLabel()));
            try {
                fHierarchyLifeCycle.ensureRefreshedTypeHierarchy(inputProcess, PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow());
                // fHierarchyLifeCycle.ensureRefreshedTypeHierarchy(inputElement, getSite().getWorkbenchWindow());
            } catch (InvocationTargetException e) {
                org.talend.commons.exception.ExceptionHandler.process(e);
                clearInput();
                return;
            } catch (InterruptedException e) {
                fNoHierarchyShownLabel.setText(showEmptyLabel);
                return;
            }

            // internalSelectType(null, false); // clear selection
            updateHierarchyViewer(true);
            internalSelectType(inputProcess, true);
            updateToolbarButtons();
            updateTitle();
            fPagebook.showPage(fTypeMethodsSplitter);
        }
    }

    private void clearInput() {
        inputProcess = null;
        fHierarchyLifeCycle.freeHierarchy();

        updateHierarchyViewer(false);
        updateToolbarButtons();
    }

    private void updateToolbarButtons() {
        // boolean isType = inputProcess instanceof IType;
        // for (int i = 0; i < fViewActions.length; i++) {
        // ToggleViewAction action = fViewActions[i];
        // if (action.getViewerIndex() == HIERARCHY_MODE_CLASSIC) {
        // action.setEnabled(fInputElement != null);
        // } else {
        // action.setEnabled(isType);
        // }
        // }
    }

    private void updateTitle() {
        String viewerTitle = getCurrentViewer().getTitle();

        String tooltip;
        String title;
        if (inputProcess != null) {
            String[] args = new String[] { viewerTitle, getJobLabel(), getProjectLabel() };
            title = Messages.getString("JobHierarchyMessages.JobHierarchyViewPart_title", args);
            tooltip = Messages.getString("JobHierarchyMessages.JobHierarchyViewPart_tooltip", args);

        } else {
            title = ""; //$NON-NLS-1$
            tooltip = viewerTitle;
        }
        setContentDescription(title);
        setTitleToolTip(tooltip);
    }

    private String getJobLabel() {
        return inputProcess.getLabel();
    }

    private String getProjectLabel() {
        org.talend.core.model.properties.Project project = ProjectManager.getInstance().getProject(
                inputProcess.getProperty().getItem());
        return project.getTechnicalLabel();
    }

    private void initDragAndDrop() {
        for (int i = 0; i < fAllViewers.length; i++) {
            addDropAdapters(fAllViewers[i]);
        }

        // DND on empty hierarchy
        DropTarget dropTarget = new DropTarget(fPagebook, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT);
        dropTarget.setTransfer(new Transfer[] { LocalSelectionTransfer.getInstance() });
        dropTarget.addDropListener(new JobHierarchyTransferDropAdapter(this));
    }

    private void addDropAdapters(AbstractTreeViewer viewer) {
        Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getInstance() };
        int ops = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT;
        DelegatingDropAdapter delegatingDropAdapter = new DelegatingDropAdapter();
        delegatingDropAdapter.addDropTargetListener(new JobHierarchyTransferDropAdapter(this));
        viewer.addDropSupport(ops, transfers, delegatingDropAdapter);
    }

}
