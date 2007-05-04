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
package org.talend.designer.rowgenerator.ui.footer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.managers.RowGeneratorManager;
import org.talend.designer.rowgenerator.managers.UIManager;

/**
 *  amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: FooterComposite.java,v 1.3 2007/01/31 06:17:55 pub Exp $
 * 
 */
public class FooterComposite extends Composite {

    private RowGeneratorManager generatorManager;

    /**
     *  amaumont FooterComposite constructor comment.
     * 
     * @param parent
     * @param style
     * @param mapperManager
     */
    public FooterComposite(Composite parent, int style, RowGeneratorManager generatorManager) {
        super(parent, style);
        this.generatorManager = generatorManager;
        createComponents();
    }

    /**
     *  amaumont Comment method "createComponents".
     */
    private void createComponents() {

        final UIManager uiManager = generatorManager.getUiManager();

        GridData footerCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
        this.setLayoutData(footerCompositeGridData);

        FormLayout formLayout = new FormLayout();
        this.setLayout(formLayout);

        Button okButton = new Button(this, SWT.NONE);
        okButton.setText(Messages.getString("FooterComposite.OkButton.Text")); //$NON-NLS-1$
        FormData okFormData = new FormData();
        Point minSize = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        okFormData.width = Math.max(IDialogConstants.BUTTON_WIDTH, minSize.x);
        
        okButton.setLayoutData(okFormData);
        okButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                uiManager.closeRowGenerator(SWT.OK);
            }

        });

        Button cancelButton = new Button(this, SWT.NONE);
        cancelButton.setText(Messages.getString("FooterComposite.CancelButton.Text")); //$NON-NLS-1$
        cancelButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                uiManager.closeRowGenerator(SWT.CANCEL);
            }

        });
        FormData cancelFormData = new FormData();
        minSize = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        cancelFormData.width = Math.max(IDialogConstants.BUTTON_WIDTH, minSize.x);
        cancelButton.setLayoutData(cancelFormData);

        cancelFormData.right = new FormAttachment(100, -5);
        okFormData.right = new FormAttachment(cancelButton, -5);

    }

}
