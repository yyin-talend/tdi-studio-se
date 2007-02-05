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

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.sqlbuilder.Messages;

/**
 * This class is used for creating a dialog for saving current editor's sql texts. <br/>
 * 
 * $Id: SaveSQLDialog.java,v 1.0 2006/11/15 05:38:28 ftang Exp $
 * @author ftang
 */
public class SQLPropertyDialog extends TitleAreaDialog {

    private Text commentText;

    private Text nameText;

    private List<String> names;

    private Query query;

    private String sql;

    /**
     * Sets the sql.
     * 
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Getter for query.
     * 
     * @return the query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the query.
     * 
     * @param query the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
        if (query != null) {
            names.remove(query.getLabel());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        this.setTitle(Messages.getString("SQLEditor.Actions.SaveSQL")); //$NON-NLS-1$
        this.setMessage(Messages.getString("SQLEditor.Actions.InputMessage"), IMessageProvider.INFORMATION); //$NON-NLS-1$

        final Label nameLabel = new Label(container, SWT.NONE);
        nameLabel.setText(Messages.getString("SQLEditor.SQLPropertyDialog.Name")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        final Label commentLabel = new Label(container, SWT.NONE);
        commentLabel.setText(Messages.getString("SQLEditor.SQLPropertyDialog.Comment")); //$NON-NLS-1$

        commentText = new Text(container, SWT.BORDER);
        commentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        this.addListener();
        return area;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    protected void okPressed() {

        if (query == null) {
            query = ConnectionFactory.eINSTANCE.createQuery();
            query.setValue(""); //$NON-NLS-1$
        }
        query.setComment(this.commentText.getText());
        query.setLabel(this.nameText.getText());
        if (sql != null) {
            query.setValue(sql);
        }

        super.okPressed();
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, Messages.getString("SQLEditor.Actions.SaveMessage"), true); //$NON-NLS-1$
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(500, 242);
    }

    /**
     * SaveSQLDialog constructor.
     * 
     * @param shell
     * @param string
     */
    public SQLPropertyDialog(Shell shell, List<String> existentNames) {
        super(shell);
        this.names = existentNames;
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.MIN | SWT.MAX);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text
        shell.setText(Messages.getString("SQLEditor.SaveSQLDialog.Title")); //$NON-NLS-1$
    }

    /**
     * Adds modify listener.
     */
    /**
     * DOC dev Comment method "addListener".
     */
    /**
     * DOC dev Comment method "addListener".
     */
    private void addListener() {
        nameText.addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                String result = validate(nameText.getText());
                if (result == null) {
                    SQLPropertyDialog.this.getButton(IDialogConstants.OK_ID).setEnabled(true);
                    SQLPropertyDialog.this.setMessage(Messages.getString("SQLEditor.Actions.InputMessage"), //$NON-NLS-1$
                            IMessageProvider.INFORMATION);
                } else {
                    SQLPropertyDialog.this.setMessage(result, IMessageProvider.ERROR);
                    SQLPropertyDialog.this.getButton(IDialogConstants.OK_ID).setEnabled(false);
                }
            }

            /**
             * Validates input text.
             * 
             * @param text
             * @return a string
             */
            private String validate(String text) {
                if (nameText.getText().length() == 0) {
                    return Messages.getString("SQLEditor.Actions.EmptyNameMessage"); //$NON-NLS-1$
                } else if (names.contains(text)) {
                    // Checks if name is existing.
                    return Messages.getString("SQLEditor.Actions.DuplicateNameMessage"); //$NON-NLS-1$
                }
                return null;
            }
        });

        commentText.addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                if (commentText.getText().length() == 0) {
                    SQLPropertyDialog.this.setMessage(Messages.getString("SQLEditor.Actions.EmptyCommentMessage"), //$NON-NLS-1$
                            IMessageProvider.INFORMATION);
                }
            }
        });
    }
}
