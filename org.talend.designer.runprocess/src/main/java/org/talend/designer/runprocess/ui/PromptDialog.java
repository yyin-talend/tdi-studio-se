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
package org.talend.designer.runprocess.ui;

import org.eclipse.jface.dialogs.Dialog;
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
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PromptDialog extends Dialog {

    /**
     * 
     */
    private static final int Y_POSITION = 100;

    /**
     * 
     */
    private static final int X_POSITION = 300;

    /**
     * 
     */
    private static final int CELLPADDING = 10;

    /**
     * 
     */
    private static final int MINIMUM_WIDTH = 50;

    /**
     * 
     */
    private static final int MARGIN_HEIGHT = 4;

    /**
     * 
     */
    private static final int MARGIN_WIDTH = 7;

    /**
     * 
     */
    private static final int MAX_LABEL_LENGTH_ONE_LINE = 16;

    IContext context;

    /**
     * DOC nrousseau PromptDialog constructor comment.
     * 
     * @param parentShell
     */
    protected PromptDialog(Shell parentShell, IContext context) {
        super(parentShell);
        this.context = context;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = MARGIN_WIDTH;
        layout.marginHeight = MARGIN_HEIGHT;
        composite.setLayout(layout);

        int position = 0;
        // Prompt for context values ?
        for (final IContextParameter parameter : context.getContextParameterList()) {
            if (parameter.isPromptNeeded()) {
                Label label = new Label(composite, SWT.NONE);
                label.setText(parameter.getPrompt());
                label.setAlignment(SWT.RIGHT);

                final Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
                text.setText(parameter.getValue());
                text.addModifyListener(new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        parameter.setValue(text.getText());
                    }
                });

                if (parameter.getComment() != null) {
                    if (!parameter.getComment().equals("")) {
                        label.setToolTipText(parameter.getComment());
                        text.setToolTipText(parameter.getComment());
                    }
                }

                GridData data = new GridData();
                data.minimumWidth = MINIMUM_WIDTH;
                label.setLayoutData(data);
                data.horizontalSpan = 2;
                
                data = new GridData(GridData.FILL_HORIZONTAL);
                data.horizontalSpan = 2;
                data.minimumWidth = MINIMUM_WIDTH;
                text.setLayoutData(data);

                position += text.getLineHeight() + CELLPADDING;
            }
        }
        return composite;
    }

    @Override
    protected void setShellStyle(int newShellStyle) {
        newShellStyle = newShellStyle | SWT.RESIZE;
        super.setShellStyle(newShellStyle);
    }

    protected void setSize(Shell shell, Point size) {
        Point centerScreen = new Point(shell.getDisplay().getClientArea().width / 2,
                shell.getDisplay().getClientArea().height / 2);

        Point newPosition = new Point(size.x / 2, size.x / 2);
        newPosition.x = centerScreen.x - newPosition.x;
        newPosition.y = centerScreen.y - newPosition.y;
        shell.setLocation(newPosition);
        shell.setSize(size.x + (size.x / 2), size.y + (size.y / 2));
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        Text t = new Text(newShell, SWT.None);
        int height = t.getLineHeight() + CELLPADDING;
        t.dispose();
        int nbParams = 0;
        for (IContextParameter parameter : context.getContextParameterList()) {
            if (parameter.isPromptNeeded()) {
                nbParams++;
            }
        }
        Point dialogSize = new Point(X_POSITION, (height * nbParams) + Y_POSITION);
        setSize(newShell, dialogSize);

        newShell.setText(Messages.getString("PromptDialog.title", context.getName()));

    }
}
