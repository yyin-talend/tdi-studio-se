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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.process.Problem;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProblemsView extends ViewPart {

    // private TreeItem warningItem, errorItem;

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
                if (problem.isConcrete()) {
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
        viewer.setInput(Problems.getRoot());

        Problems.recheckCurrentProblems(this);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void selectInEditor(Node node) {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

        if (editorPart instanceof MultiPageTalendEditor) {
            MultiPageTalendEditor multi = (MultiPageTalendEditor) editorPart;
            GraphicalViewer viewer = multi.getTalendEditor().getViewer();
            Object object = viewer.getRootEditPart().getChildren().get(0);
            if (object instanceof ProcessPart) {
                for (EditPart editPart : (List<EditPart>) ((ProcessPart) object).getChildren()) {
                    if (editPart instanceof NodePart) {
                        if (((NodePart) editPart).getModel().equals(node)) {
                            viewer.select(editPart);
                            multi.setFocus();
                        }
                    }
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
}
