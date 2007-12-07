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
package org.talend.designer.core.ui.views.problems;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IMarker;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.RoutineProblem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.problems.Problems.Group;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.ui.actions.routines.AbstractRoutineAction;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProblemsView extends ViewPart implements PropertyChangeListener {

    public static final String PROBLEM_TYPE_SELECTION = "PROBLEM.TYPE.SELECTION";//$NON-NLS-1$

    private static final String ID = "org.talend.designer.core.ui.views.ProblemsView"; //$NON-NLS-1$

    private RoutineItemsCheck routineItemsCheck;

    private ECodeLanguage language;

    private String editorID;

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
        setPartName(Messages.getString("ProblemsView.problems.defaultTitle")); //$NON-NLS-1$
    }

    @Override
    public void createPartControl(Composite parent) {
        CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().addPropertyChangeListener(this);
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
                    if (problem.getElement() instanceof Node)
                        selectInDesigner((Node) problem.getElement());
                    else if (problem instanceof RoutineProblem) {
                        selectInRoutine(((RoutineProblem) problem).getMarker());
                    }
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
        column2.setWidth(400);
        column2.setResizable(true);

        ProblemViewProvider provider = new ProblemViewProvider();
        viewer.setLabelProvider(provider);
        viewer.setContentProvider(provider);
        resetContent();

        IActionBars actionBars = getViewSite().getActionBars();
        initMenu(actionBars.getMenuManager());
    }

    public void resetContent() {
        routineItemsCheck = new RoutineItemsCheck();
        routineItemsCheck.addAllRoutineProblem();
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
        CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().removePropertyChangeListener(this);
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
    private void selectInDesigner(Node node) {
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

    private void selectInRoutine(IMarker marker) {
        OpenRoutineAction openRoutineAction = new OpenRoutineAction(marker);
        openRoutineAction.run();

    }

    @Override
    public void setFocus() {
        viewer.getTree().setFocus();
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

    /**
     * 
     * DOC denny ProblemsView class global comment. Detailled comment
     */
    private class OpenRoutineAction extends AbstractRoutineAction {

        IMarker marker;

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
         * org.eclipse.jface.viewers.IStructuredSelection)
         * 
         */
        public OpenRoutineAction(IMarker marker) {
            super();
            this.marker = marker;
        }

        public void run() {
            try {
                RoutineItem routine = getRoutineItem();
                openRoutineEditor(routine, false);
            } catch (SystemException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (PartInitException e) {
                MessageBoxExceptionHandler.process(e);
            }
        }

        /**
         * bqian Comment method "getRoutineItem".
         * 
         * @return
         */
        private RoutineItem getRoutineItem() throws PersistenceException {
            List<IRepositoryObject> list = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().getAll(
                    ERepositoryObjectType.ROUTINES);

            for (IRepositoryObject repositoryObject : list) {
                String name = repositoryObject.getProperty().getLabel();
                String id = repositoryObject.getProperty().getId();
                if (matchRoutine(id, name, marker.getResource().getName())) {
                    return (RoutineItem) repositoryObject.getProperty().getItem();
                }
            }
            return null;
        }

        /**
         * Check whether the routine name is correct. <br>
         * For example: routineLabel AAA <br>
         * resourceName AAA.java| AAA.pem
         * 
         * @param string
         * 
         * @param name
         * @param string
         * @return
         */
        public boolean matchRoutine(String routineID, String routineLabel, String resourceName) {
            if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
                try {
                    Boolean foundMatch = resourceName.matches(routineLabel + "(\\.java");
                    return foundMatch.booleanValue();
                } catch (PatternSyntaxException ex) {
                    // Syntax error in the regular expression
                }
            } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
                try {
                    Boolean foundMatch = resourceName.matches("tempRoutine" + routineID);
                    return foundMatch.booleanValue();
                } catch (PatternSyntaxException ex) {
                    // Syntax error in the regular expression
                }
            }

            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
         * org.eclipse.jface.viewers.IStructuredSelection)
         */
        public void init(TreeViewer viewer, IStructuredSelection selection) {
            // setEnabled(true);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(ERepositoryActionName.JOB_DELETE_TO_RECYCLE_BIN.getName())) {
            return;
        }
        if (!(evt.getNewValue() instanceof IRepositoryObject)) {
            return;
        }
        IRepositoryObject object = (IRepositoryObject) evt.getNewValue();
        if (object.getType() != ERepositoryObjectType.ROUTINES) {
            return;
        }
        String routineLabel = object.getProperty().getLabel();
        Problems.removeProblemsByRoutine(routineLabel);
    }
}
