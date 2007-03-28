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
package org.talend.designer.runprocess.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
        final Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FillLayout());

        final ScrolledComposite sc = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);

        final Composite child = new Composite(sc, SWT.NONE);
        child.setLayout(new GridLayout(1, false));

        // Prompt for context values ?
        for (final IContextParameter parameter : context.getContextParameterList()) {
            if (parameter.isPromptNeeded()) {
                Label label = new Label(child, SWT.NONE);
                label.setText(parameter.getPrompt());
                label.setAlignment(SWT.RIGHT);

                final Text text = new Text(child, SWT.SINGLE | SWT.BORDER);
                text.setText(parameter.getValue());
                text.addModifyListener(new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        parameter.setValue(text.getText());
                    }
                });

                if (parameter.getComment() != null) {
                    if (!parameter.getComment().equals("")) { //$NON-NLS-1$
                        label.setToolTipText(parameter.getComment());
                        text.setToolTipText(parameter.getComment());
                    }
                }

                GridData data = new GridData();
                data.minimumWidth = MINIMUM_WIDTH;
                label.setLayoutData(data);

                data = new GridData(GridData.FILL_HORIZONTAL);
                data.minimumWidth = MINIMUM_WIDTH;
                text.setLayoutData(data);
            }
        }

        sc.setContent(child);

        // Set the minimum size
        // sc.setMinSize(400, 400);

        // Expand both horizontally and vertically
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                Rectangle r = sc.getClientArea();
                sc.setMinSize(child.computeSize(r.width, SWT.DEFAULT));
            }
        });
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
        Point dialogSize = new Point(X_POSITION, Math.min((height * nbParams) + Y_POSITION, 400));
        setSize(newShell, dialogSize);

        newShell.setText(Messages.getString("PromptDialog.title", context.getName())); //$NON-NLS-1$

    }
}
