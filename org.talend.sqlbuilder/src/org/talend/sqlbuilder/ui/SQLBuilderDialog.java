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
package org.talend.sqlbuilder.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.dbstructure.nodes.ColumnNode;
import org.talend.sqlbuilder.dbstructure.nodes.INode;

/**
 * This Dialog is used for building sql.
 * 
 * $Id: SQLBuilderDialog.java,v 1.29 2006/11/02 09:19:13 qianbing Exp $
 * 
 */
public class SQLBuilderDialog extends Dialog {

    private DBDetailsComposite dbDetailsComposite;

    private DBStructureComposite structureComposite;

    private SQLBuilderTabComposite editorComposite;

    /**
     * Create the dialog
     * 
     * @param parentShell
     */
    public SQLBuilderDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.RESIZE | SWT.MIN | SWT.MAX);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text
        shell.setText("SQL Builder"); //$NON-NLS-1$
    }

    /**
     * Create contents of the dialog
     * 
     * @param parent
     */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        final SashForm mainSashForm = new SashForm(container, SWT.NONE | SWT.VERTICAL);
        mainSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final SashForm sashFormStructureAndEditor = new SashForm(mainSashForm, SWT.NONE);

        final SashForm sashFormResultAndDetail = new SashForm(mainSashForm, SWT.NONE);
        mainSashForm.setWeights(new int[] { 3, 1 });

        createResult(sashFormResultAndDetail);
        createDetail(sashFormResultAndDetail);
        sashFormResultAndDetail.setWeights(new int[] { 4, 3 });

        createDatabaseStructure(sashFormStructureAndEditor);
        createSQLEditor(sashFormStructureAndEditor);
        sashFormStructureAndEditor.setWeights(new int[] { 1, 3 });

        RefreshDetailCompositeAction refreshAction = new RefreshDetailCompositeAction(structureComposite
                .getTreeViewer());

        return container;
    }

    /**
     * Creates the sql detail composite.
     * 
     * @param sashFormResultAndDetail
     */
    private void createDetail(SashForm sashFormResultAndDetail) {
        dbDetailsComposite = new DBDetailsComposite(sashFormResultAndDetail, SWT.BORDER);
    }

    /**
     * Creates the composite to display sql execution result.
     * 
     * @param sashFormResultAndDetail
     */
    private void createResult(SashForm sashFormResultAndDetail) {
        SQLResultComposite resultView = new SQLResultComposite(sashFormResultAndDetail, SWT.BORDER);

    }

    /**
     * Creates the sql editor composite.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createSQLEditor(SashForm sashFormStructureAndEditor) {
        editorComposite = new SQLBuilderTabComposite(sashFormStructureAndEditor, SWT.BORDER);
    }

    /**
     * Creates composite to display database structure.
     * 
     * @param sashFormStructureAndEditor
     */
    private void createDatabaseStructure(SashForm sashFormStructureAndEditor) {
        structureComposite = new DBStructureComposite(sashFormStructureAndEditor, SWT.BORDER, this);
    }

    /**
     * Create contents of the button bar
     * 
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog
     */
    protected Point getInitialSize() {
        return new Point(800, 600);
    }

    /**
     * RefreshDetailCompositeAction
     */
    public class RefreshDetailCompositeAction extends SelectionProviderAction {

        /**
         * 
         */
        public RefreshDetailCompositeAction(ISelectionProvider provider) {
            super(provider, "Refresh DetailComposite");
        }

        public void selectionChanged(final IStructuredSelection selection) {
            BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

                public void run() {
                    INode selectedNode = null;
                    if (selection != null && (selection.getFirstElement() instanceof INode)) {
                        selectedNode = (INode) selection.getFirstElement();
                        if (selectedNode instanceof ColumnNode) {
                            selectedNode = selectedNode.getParent();
                        }
                    }
                    dbDetailsComposite.setSelectedNode(selectedNode);
                }
            });
        }
    }

    public SQLBuilderTabComposite getEditorComposite() {
        return editorComposite;
    }

    @Override
    public boolean close() {
        SessionTreeNodeUtils.dispose();
        return super.close();
    }

}
