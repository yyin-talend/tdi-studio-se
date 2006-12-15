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
package org.talend.repository.ui.login;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.Project;
import org.talend.core.prefs.PreferenceManipulator;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.license.LicenseManagement;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.registeruser.RegisterManagement;
import org.talend.repository.ui.wizards.license.LicenseWizard;
import org.talend.repository.ui.wizards.license.LicenseWizardDialog;
import org.talend.repository.ui.wizards.register.RegisterWizard;

/**
 * Login dialog. <br/>
 * 
 * $Id$
 * 
 */
public class LoginDialog extends TitleAreaDialog {

    /** The login composite. */
    private LoginComposite loginComposite;

    /**
     * Construct a new LoginDialog.
     * 
     * @param parentShell Parent shell.
     */
    public LoginDialog(Shell parentShell) {
        super(parentShell);

        ImageDescriptor imgDesc = RepositoryPlugin.imageDescriptorFromPlugin(RepositoryPlugin.PLUGIN_ID, "icons/login_h.jpg"); //$NON-NLS-1$
        if (imgDesc != null) {
            setTitleImage(imgDesc.createImage());
        }
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.getString("LoginDialog.title")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Composite container = new Composite(composite, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        container.setLayout(layout);

        new ImageCanvas(container, "icons/login_v.jpg"); //$NON-NLS-1$

        try {
            if (!LicenseManagement.isLicenseValidated()) {
                LicenseWizard licenseWizard = new LicenseWizard();
                LicenseWizardDialog dialog = new LicenseWizardDialog(getShell(), licenseWizard);
                dialog.setTitle(Messages.getString("LicenseWizard.windowTitle")); //$NON-NLS-1$
                if (dialog.open() == WizardDialog.OK) {
                    LicenseManagement.acceptLicense();
                } else {
                    System.exit(0);
                }
            }
            if (!RegisterManagement.isProductRegistered()) {
                RegisterWizard registerWizard = new RegisterWizard();
                WizardDialog dialog = new WizardDialog(getShell(), registerWizard);
                dialog.setTitle(Messages.getString("RegisterWizard.windowTitle")); //$NON-NLS-1$
                if (dialog.open() == WizardDialog.OK) {
                    RegisterManagement.register(registerWizard.getEmail(), registerWizard.getCountry(), registerWizard
                            .isProxyEnabled(), registerWizard.getProxyHost(), registerWizard.getProxyPort(),
                            org.talend.core.CorePlugin.getDefault().getBundle().getHeaders().get(
                                    org.osgi.framework.Constants.BUNDLE_VERSION).toString());
                } else {
                    RegisterManagement.decrementTry();
                }
            }
        } catch (BusinessException e) {
            ErrorDialogWidthDetailArea errorDialog = new ErrorDialogWidthDetailArea(getShell(), RepositoryPlugin.PLUGIN_ID,
                    Messages.getString("RegisterWizardPage.serverCommunicationProblem"), e.getMessage());
        }

        loginComposite = new LoginComposite(container, SWT.NONE, this);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 350;
        loginComposite.setLayoutData(data);

        return composite;
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        updateButtons();
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        boolean logged = true;

        // check if user already exists retrieve it else create it
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            boolean found = repositoryFactory.doesLoggedUserExist();
            if (!found && !loginComposite.isAuthenticationNeeded()) {
                repositoryFactory.createUser();
            }
        } catch (PersistenceException e) {
            logged = false;
            e.printStackTrace();
        }

        if (logged) {
            logIn(loginComposite.getProject());
        }
    }

    /**
     * DOC smallet Comment method "logIn".
     * 
     * @param project
     */
    protected void logIn(Project project) {
        // Save last used parameters
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        prefManipulator.setLastServer(loginComposite.getServer());
        prefManipulator.setLastContext(loginComposite.getContext());
        prefManipulator.setLastPort(loginComposite.getPort());
        prefManipulator.setLastRepository(loginComposite.getRepository().getClass().getName());
        prefManipulator.setLastProject(project.getLabel());
        prefManipulator.setLastUser(loginComposite.getUser().getLogin());

        if (loginComposite.getRepository().isAuthenticationNeeded()) {
            prefManipulator.addServer(loginComposite.getServer());
            prefManipulator.addContext(loginComposite.getContext());
            prefManipulator.addPort(loginComposite.getPort());
        }
        prefManipulator.addUser(loginComposite.getUser().getLogin());

        try {
            ProxyRepositoryFactory.getInstance().logOnProject(project);
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        }

        super.okPressed();
    }

    public void updateButtons() {
        getButton(IDialogConstants.OK_ID).setEnabled(loginComposite.canFinish());
    }

    /**
     * Canvas displaying an image.<br/>
     * 
     * $Id$
     * 
     */
    private class ImageCanvas extends Canvas {

        private Image img;

        public ImageCanvas(Composite parent, String imgPath) {
            super(parent, SWT.NONE);

            ImageDescriptor imgDesc = RepositoryPlugin.imageDescriptorFromPlugin(RepositoryPlugin.PLUGIN_ID, imgPath);
            if (imgDesc != null) {
                img = imgDesc.createImage();

                addPaintListener(new PaintListener() {

                    public void paintControl(PaintEvent e) {
                        e.gc.drawImage(img, 0, 0);
                    }
                });
            }
        }

        /*
         * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
         */
        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            Point size;
            if (img != null) {
                size = new Point(img.getBounds().width, img.getBounds().height);
            } else {
                size = super.computeSize(wHint, hHint, changed);
            }
            return size;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.widgets.Widget#dispose()
         */
        @Override
        public void dispose() {
            if (img != null) {
                img.dispose();
                img = null;
            }

            super.dispose();
        }

    }

}
