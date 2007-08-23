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
package org.talend.designer.core.ui.views.problems;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.talend.core.model.process.Problem;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.problems.Problems.Group;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProblemsView extends ViewPart {

    public static final String PROBLEM_TYPE_SELECTION = "PROBLEM.TYPE.SELECTION";//$NON-NLS-1$

    private static final String ID = "org.talend.designer.core.ui.views.ProblemsView"; //$NON-NLS-1$

    public static ProblemsView show() {
        IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
        try {
            part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID);
        } catch (Exception e) {

        }
        return (ProblemsView) part;
    }

    private TreeViewer viewer;

    public ProblemsView() {
        setPartName(""); //$NON-NLS-1$
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());

        viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

        final Tree tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Problem problem = (Problem) selection.getFirstElement();
                if (problem != null && problem.isConcrete()) {
                    selectInEditor((Node) problem.getElement());
                }
            }
        });

        TreeColumn column1 = new TreeColumn(tree, SWT.CENTER);
        column1.setText(Messages.getString("ProblemsView.description")); //$NON-NLS-1$
        column1.setWidth(400);
        column1.setAlignment(SWT.LEFT);
        column1.setResizable(true);

        TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
        column2.setText(Messages.getString("ProblemsView.resource")); //$NON-NLS-1$
        column2.setWidth(200);
        column2.setResizable(true);

        ProblemViewProvider provider = new ProblemViewProvider();
        viewer.setLabelProvider(provider);
        viewer.setContentProvider(provider);
        resetContent();

        IActionBars actionBars = getViewSite().getActionBars();
        initMenu(actionBars.getMenuManager());
    }

    public void resetContent() {
        viewer.setInput(Problems.getRoot());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        Problems.setProblemView(null);
    }

    /**
     * initialize the Menu of problem view.
     * 
     * @param menuManager
     */
    private void initMenu(IMenuManager menu) {
        MenuManager groupByMenu = new MenuManager(MarkerMessages.ProblemView_GroupByMenu);
        groupByMenu.add(new GroupingAction(MarkerMessages.ProblemView_Type, Group.TYPE, this));

        groupByMenu.add(new GroupingAction(Messages.getString("ProblemsView.severity"), Group.SEVERITY, this)); //$NON-NLS-1$

        groupByMenu.add(new GroupingAction(MarkerMessages.ProblemView_None, Group.NONE, this));
        menu.add(groupByMenu);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void selectInEditor(Node node) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

        IEditorReference[] editorParts = page.getEditorReferences();

        for (IEditorReference reference : editorParts) {
            IEditorPart editor = reference.getEditor(false);
            if (MultiPageTalendEditor.ID.equals(editor.getSite().getId())) {
                MultiPageTalendEditor mpte = (MultiPageTalendEditor) editor;
                if (mpte.getTalendEditor().getProcess().equals(node.getProcess())) {
                    page.bringToTop(mpte);
                    mpte.selectNode(node);
                }
            }
        }
    }

    @Override
    public void setFocus() {
        viewer.getTree().setFocus();
    }

    public void setPartName(String title) {
        String viewName = Messages.getString("ProblemsView.problems.defaultTitle"); //$NON-NLS-1$

        if (!title.equals("")) { //$NON-NLS-1$
            viewName = viewName + "(" + title + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        super.setPartName(viewName);
    }

    /**
     * DOC bqian Comment method "refresh".
     */
    public void refresh() {
        viewer.refresh();
        setContentDescription(Problems.getSummary());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.TableView#getDialogSettings()
     */
    protected IPreferenceStore getDialogSettings() {
        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();
        return store;
    }

    /**
     * An internal Action that can be used to group the problems.
     * 
     */
    private class GroupingAction extends Action {

        Group groupingField;

        ProblemsView problemView;

        /**
         * Creates a new instance of the receiver.
         * 
         * @param label
         * @param field
         * @param view
         */
        public GroupingAction(String label, Group group, ProblemsView view) {
            super(label, IAction.AS_RADIO_BUTTON);
            groupingField = group;
            problemView = view;
            Group categoryField = Problems.getGroupField();
            setChecked(categoryField.equals(groupingField));
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run() {

            if (isChecked()) {
                Problems.setGroupField(groupingField);
                problemView.resetContent();
                getDialogSettings().setValue(PROBLEM_TYPE_SELECTION, groupingField.toString());
            }
        }
    }
}
