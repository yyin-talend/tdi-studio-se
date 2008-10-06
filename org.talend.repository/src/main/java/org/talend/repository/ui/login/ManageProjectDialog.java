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
package org.talend.repository.ui.login;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.ERepositoryImages;
import org.talend.repository.ui.actions.importproject.ImportDemoProjectAction;
import org.talend.repository.ui.actions.importproject.ImportProjectAsAction;

/**
 * DOC stephane class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ManageProjectDialog extends TrayDialog {

    private LoginComposite loginComposite;

    public ManageProjectDialog(Shell parentShell, LoginComposite loginComposite) {
        super(parentShell);
        this.loginComposite = loginComposite;

    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        ManageProjectsComposite projectsComposite = new ManageProjectsComposite(composite, SWT.NONE, loginComposite);
        return composite;
    }

    /**
     * DOC stephane ManageProjectDialog class global comment. Detailled comment
     */
    private class ManageProjectsComposite extends Composite {

        private LoginComposite loginComposite;

        private Button newProjectButton;

        private Button importProjectsButton;

        private Button importDemoProjectButton;

        private Button deleteProjectButton;

        public ManageProjectsComposite(Composite parent, int style, LoginComposite loginComposite) {
            super(parent, style);

            parent.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

            this.loginComposite = loginComposite;

            FormToolkit toolkit = new FormToolkit(this.getDisplay());
            Form form = toolkit.createForm(this);
            Composite formBody = form.getBody();

            formBody.setBackgroundMode(SWT.INHERIT_DEFAULT);
            formBody.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

            GridLayout layout = new GridLayout();
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            setLayout(layout);
            form.setLayoutData(new GridData(GridData.FILL_BOTH));

            layout = new GridLayout(2, false);
            formBody.setLayout(layout);

            newProjectButton = toolkit.createButton(formBody, null, SWT.PUSH);
            newProjectButton.setText(Messages.getString("LoginComposite.buttons.newProject")); //$NON-NLS-1$
            newProjectButton.setToolTipText(Messages.getString("LoginComposite.buttons.newProject.tooltip")); //$NON-NLS-1$
            newProjectButton.setImage(ImageProvider.getImage(ERepositoryImages.NEW_PROJECT_ACTION));

            toolkit.createLabel(formBody, Messages.getString("LoginComposite.buttons.newProject.desc")); //$NON-NLS-1$

            importProjectsButton = toolkit.createButton(formBody, null, SWT.PUSH);
            ImportProjectAsAction ipa = ImportProjectAsAction.getInstance();
            importProjectsButton.setText(Messages.getString("LoginComposite.buttons.importProject")); //$NON-NLS-1$
            importProjectsButton.setToolTipText(ipa.getToolTipText());
            importProjectsButton.setImage(ImageProvider.getImage(ipa.getImageDescriptor()));

            toolkit.createLabel(formBody, Messages.getString("LoginComposite.buttons.importProject.desc")); //$NON-NLS-1$

            importDemoProjectButton = toolkit.createButton(formBody, null, SWT.PUSH);
            ImportDemoProjectAction importDemoProjectAction = ImportDemoProjectAction.getInstance();
            importDemoProjectButton.setText(importDemoProjectAction.getText());
            importDemoProjectButton.setToolTipText(importDemoProjectAction.getToolTipText());
            importDemoProjectButton.setImage(ImageProvider.getImage(importDemoProjectAction.getImageDescriptor()));

            toolkit.createLabel(formBody, Messages.getString("LoginComposite.buttons.importDemoProject.desc")); //$NON-NLS-1$

            deleteProjectButton = toolkit.createButton(formBody, null, SWT.PUSH);
            deleteProjectButton.setText(Messages.getString("LoginComposite.buttons.deleteProject"));
            deleteProjectButton.setImage(ImageProvider.getImage(ERepositoryImages.DELETE_PROJECT_ACTION));

            toolkit.createLabel(formBody, Messages.getString("LoginComposite.buttons.deleteProject.desc")); //$NON-NLS-1$

            Point point = new Point(80, 25);
            newProjectButton.setSize(point);
            importProjectsButton.setSize(point);
            importDemoProjectButton.setSize(point);
            deleteProjectButton.setSize(point);

            addListeners();
        }

        private void addListeners() {
            newProjectButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    loginComposite.createNewProject();
                }
            });
            importProjectsButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    loginComposite.importProjects();
                }
            });
            importDemoProjectButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    loginComposite.importDemoProject();
                }
            });
            deleteProjectButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    loginComposite.deleteProject();
                }
            });
        }
    }
}
