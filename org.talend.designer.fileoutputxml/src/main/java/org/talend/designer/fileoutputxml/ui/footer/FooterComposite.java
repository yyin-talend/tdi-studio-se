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
package org.talend.designer.fileoutputxml.ui.footer;

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
import org.talend.designer.fileoutputxml.managers.FOXManager;

/**
 * amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: FooterComposite.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class FooterComposite extends Composite {

    private FOXManager foxManager;

    /**
     * amaumont FooterComposite constructor comment.
     * 
     * @param parent
     * @param style
     * @param mapperManager
     */
    public FooterComposite(Composite parent, int style, FOXManager generatorManager) {
        this(parent, style);
        this.foxManager = generatorManager;
    }

    public FooterComposite(Composite parent, int style) {
        super(parent, style);
        createComponents();
    }

    /**
     * amaumont Comment method "createComponents".
     */
    private void createComponents() {

        GridData footerCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
        this.setLayoutData(footerCompositeGridData);
        
        FormLayout formLayout = new FormLayout();
        this.setLayout(formLayout);
        
        Button okButton = new Button(this, SWT.NONE);
        okButton.setText("Ok");
        FormData okFormData = new FormData();
        Point minSize = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        okFormData.width = Math.max(IDialogConstants.BUTTON_WIDTH, minSize.x);
        
        okButton.setLayoutData(okFormData);
        okButton.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
            public void widgetSelected(SelectionEvent e) {
                foxManager.getUiManager().closeFOX(SWT.OK);
            }
            
        });
        
        Button cancelButton = new Button(this, SWT.NONE);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
            public void widgetSelected(SelectionEvent e) {
                foxManager.getUiManager().closeFOX(SWT.CANCEL);
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
