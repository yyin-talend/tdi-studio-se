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
package org.talend.designer.core.ui.dialog.mergeorder;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeError;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ErrorMessageDialog extends Dialog {

    private Node mergeNode;

    private StyledText textArea;

    private String content;

    /**
     * DOC Administrator ErrorMessageDialog constructor comment.
     * 
     * @param parentShell
     */
    protected ErrorMessageDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
     * DOC nrousseau MergeOrderDialog constructor comment.
     * 
     * @param parentShell
     */
    public ErrorMessageDialog(Shell parentShell, Node mergeNode) {
        this(parentShell);
        this.mergeNode = mergeNode;
        content = mergeNode.getErrorInfo().getContent();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite createDialogArea = (Composite) super.createDialogArea(parent);
        createDialogArea.setLayout(new GridLayout());
        textArea = new StyledText(createDialogArea, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.BORDER);
        textArea.setText(content);
        textArea.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
        GridData data = new GridData(GridData.FILL_BOTH);
        data.minimumHeight = 100;
        data.minimumWidth = 300;
        textArea.setLayoutData(data);
        return createDialogArea;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("ERROR!");
        NodeError nodeError = mergeNode.getNodeError();
        Point nodePoint = nodeError.getLocation().getCopy();
        int diaX = nodePoint.x + 30;
        int diaY = nodePoint.y + nodeError.getErrorSize().height + 150;
        newShell.setBounds(diaX, diaY, 340, 180);

    }
}
