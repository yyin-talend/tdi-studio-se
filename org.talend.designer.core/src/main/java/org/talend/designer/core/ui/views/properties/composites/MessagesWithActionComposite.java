// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.properties.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MessagesWithActionComposite extends MessagesComposite {

    private Button actionBtn;

    public MessagesWithActionComposite(Composite parent, int style) {
        super(parent, style);
        actionBtn = new Button(this, SWT.PUSH);
        // default
        updateActionButton(null, ImageProvider.getImage(EImage.THREE_DOTS_ICON));
    }

    @Override
    public void updateTopMessages(String messages, int status) {
        super.updateTopMessages(messages, status);

        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.setBackground(backgroundColor);
        }
        setActionButtonVisible(!hidden);
    }

    public void setActionButtonVisible(boolean show) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.setVisible(show);
            if (show) {
                FormData btnData = new FormData();
                Point size = actionBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                btnData.right = new FormAttachment(100, -size.x - 5);
                btnData.top = new FormAttachment(0, 3);
                actionBtn.setLayoutData(btnData);
            }
        }
    }

    public void updateActionButton(String title, Image image) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            if (title != null) {
                actionBtn.setText(title);
            } else {
                actionBtn.setText(BLANK);
            }
            actionBtn.setImage(image);
        }
    }

    public void addActionListener(SelectionListener listener) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.addSelectionListener(listener);
        }
    }

    public void removeActionListener(SelectionListener listener) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.removeSelectionListener(listener);
        }
    }
}
