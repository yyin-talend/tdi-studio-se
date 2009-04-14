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
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeError;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ErrorMessageDialog extends Dialog {

    private Node mergeNode;

    private CLabel imageLable;

    private CLabel button;

    private SashForm sashForm;

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
        createDialogArea.setLayout(new FillLayout());
        sashForm = new SashForm(createDialogArea, SWT.VERTICAL);
        sashForm.setLayout(new GridLayout());
        sashForm.setSize(190, 300);

        Composite comSimple = new Composite(sashForm, SWT.NONE);
        FillLayout simGrid = new FillLayout();
        comSimple.setLayout(simGrid);

        FormData dataIma = new FormData();
        dataIma.left = new FormAttachment(5);
        dataIma.top = new FormAttachment(5);
        imageLable = new CLabel(comSimple, SWT.NONE);
        imageLable.setText(Messages.getString("ErrorMessageDialog.EXCEP_IN_COM") + mergeNode.getUniqueName()); //$NON-NLS-1$
        imageLable.setImage(ImageProvider.getImage(EImage.ERRORSIMPLEMESS_ICON));

        FormData dataCon = new FormData();
        dataCon.left = new FormAttachment(imageLable, 5);

        Composite comDetail = new Composite(sashForm, SWT.NONE);
        GridLayout detailGrid = new GridLayout();
        detailGrid.numColumns = 15;
        comDetail.setLayout(detailGrid);
        GridData dataBut = new GridData();
        dataBut.horizontalSpan = 15;
        button = new CLabel(comDetail, SWT.FLAT);
        button.setText(Messages.getString("ErrorMessageDialog.DETAIL")); //$NON-NLS-1$
        button.setImage(ImageProvider.getImage(EImage.RIGHTPRESS_ICON));

        textArea = new StyledText(sashForm, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
        textArea.pack();
        textArea.setText(content);
        textArea.setBackground(new Color(Display.getDefault(), new RGB(255, 255, 255)));
        textArea.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
        sashForm.setWeights(new int[] { 4, 3, 0 });

        addButtonListener();
        return createDialogArea;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ErrorMessageDialog.ERROR_MESS")); //$NON-NLS-1$
        NodeError nodeError = mergeNode.getNodeError();
        Point nodePoint = nodeError.getLocation().getCopy();
        int diaX = nodePoint.x + 30;
        int diaY = nodePoint.y + nodeError.getErrorSize().height + 150;
        newShell.setBounds(diaX, diaY, 400, 220);

    }

    private void addButtonListener() {
        button.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {

            }

            public void mouseDown(MouseEvent e) {

            }

            public void mouseUp(MouseEvent e) {
                if (button.getImage() == ImageProvider.getImage(EImage.RIGHTPRESS_ICON)) {
                    sashForm.setWeights(new int[] { 4, 3, 10 });
                    button.setImage(ImageProvider.getImage(EImage.DOWNPRESS_ICON));
                } else if (button.getImage() == ImageProvider.getImage(EImage.DOWNPRESS_ICON)) {
                    sashForm.setWeights(new int[] { 4, 3, 0 });
                    button.setImage(ImageProvider.getImage(EImage.RIGHTPRESS_ICON));
                }

            }

        });
    }
}
