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
package org.talend.designer.mapper.ui.footer;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.help.perl.OpenPerlHelpAction;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FooterComposite extends Composite {

    /**
     * Image registry key for help image (value <code>"dialog_help_image"</code>).
     * 
     * @since 3.2
     */
    public static final String DLG_IMG_HELP = "dialog_help_image"; //$NON-NLS-1$

    private MapperManager mapperManager;

    /**
     * DOC amaumont FooterComposite constructor comment.
     * 
     * @param parent
     * @param style
     * @param mapperManager
     */
    public FooterComposite(Composite parent, int style, MapperManager mapperManager) {
        super(parent, style);
        this.mapperManager = mapperManager;
        createComponents();
    }

    /**
     * DOC amaumont Comment method "createComponents".
     */
    private void createComponents() {

        final UIManager uiManager = mapperManager.getUiManager();

        GridData footerCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
        this.setLayoutData(footerCompositeGridData);

        FormLayout formLayout = new FormLayout();
        formLayout.spacing = 15;
        this.setLayout(formLayout);

        if (LanguageProvider.getCurrentLanguage().getCodeLanguage() == ECodeLanguage.PERL) {
            Image helpImage = JFaceResources.getImage(DLG_IMG_HELP);
            if (helpImage != null) {
                ToolBar toolBar = createHelpImageButton(this, helpImage);
                FormData helpFormData = (FormData) toolBar.getLayoutData();
                helpFormData.left = new FormAttachment();
            }
        }

        Button okButton = new Button(this, SWT.NONE);
        okButton.setEnabled(!mapperManager.componentIsReadOnly());
        okButton.setText(Messages.getString("FooterComposite.button.OK")); //$NON-NLS-1$
        FormData okFormData = new FormData();

        okFormData.width = 100;
        okButton.setLayoutData(okFormData);
        okButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                uiManager.closeMapper(SWT.OK);
            }

        });

        // final Button applyButton = new Button(this, SWT.NONE);
        // applyButton.setText("Apply");
        // FormData applyFormData = new FormData();
        // applyButton.setLayoutData(applyFormData);
        // applyButton.addSelectionListener(new SelectionListener() {
        //
        // public void widgetDefaultSelected(SelectionEvent e) {
        // }
        //
        // public void widgetSelected(SelectionEvent e) {
        // MessageBox messageBox = new MessageBox(uiManager.retrieveShellParent(footerComposite));
        // messageBox.setText("Info");
        // messageBox.setMessage("Not implemented yet !");
        // messageBox.open();
        // }
        //
        // });

        Button cancelButton = new Button(this, SWT.NONE);
        cancelButton.setText(Messages.getString("FooterComposite.button.Cancel")); //$NON-NLS-1$
        cancelButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                uiManager.closeMapper(SWT.CANCEL);
            }

        });
        FormData cancelFormData = new FormData();
        cancelFormData.width = 100;
        cancelButton.setLayoutData(cancelFormData);

        // applyFormData.right = new FormAttachment(100);
        // cancelFormData.right = new FormAttachment(applyButton, -5);
        cancelFormData.right = new FormAttachment(100, -5);
        okFormData.right = new FormAttachment(cancelButton, -5);

    }

    /*
     * Creates a button with a help image. This is only used if there is an image available.
     */
    private ToolBar createHelpImageButton(Composite parent, Image image) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
        // ((GridLayout) parent.getLayout()).numColumns++;
        toolBar.setLayoutData(new FormData());
        final Cursor cursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
        toolBar.setCursor(cursor);
        toolBar.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                cursor.dispose();
            }
        });
        ToolItem item = new ToolItem(toolBar, SWT.NONE);
        item.setImage(image);
        item.setToolTipText(JFaceResources.getString("helpToolTip")); //$NON-NLS-1$
        item.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                helpPressed();
            }

        });
        return toolBar;
    }

    private void helpPressed() {
        OpenPerlHelpAction perlHelpAction = new OpenPerlHelpAction();
        perlHelpAction.run();
    }

}
