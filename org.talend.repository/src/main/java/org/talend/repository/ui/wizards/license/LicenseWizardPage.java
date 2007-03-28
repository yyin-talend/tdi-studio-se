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
package org.talend.repository.ui.wizards.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.PersistenceException;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;

/**
 * Page for new project details. <br/>
 * 
 * $Id$
 * 
 */
public class LicenseWizardPage extends WizardPage {

    /** CLUF field. */
    private Text clufText;

    /**
     * Constructs a new LicenseWizardPage.
     * 
     * @param server
     * @param password
     * @param author
     */
    public LicenseWizardPage() {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(Messages.getString("LicenseWizard.title")); //$NON-NLS-1$
        setDescription(Messages.getString("LicenseWizard.description")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);
 
        Label subTitleLabel = new Label(container, SWT.NONE);
        subTitleLabel.setText(Messages.getString("LicenseWizard.subtitle")); //$NON-NLS-1$

        clufText = new Text(container, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT | SWT.BORDER);
        clufText.setBackground(new Color(null, 255, 255, 255));
        clufText.setEditable(false);
        String license = getLicense();
        clufText.setText(license);

        GridData data = new GridData(450, 300);
        clufText.setLayoutData(data);

        Label footerLabel = new Label(container, SWT.NONE);
        footerLabel.setText(Messages.getString("LicenseWizard.footer")); //$NON-NLS-1$

        setControl(container);
        setPageComplete(true);
    }

    /**
     * DOC mhirt Comment method "getLicense".
     * 
     * @return
     * @throws PersistenceException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String getLicense() {
        String license = ""; //$NON-NLS-1$
        try {
            final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path("resources/license.txt"), null)); //$NON-NLS-1$

            FileReader fileReader = new FileReader(new File(url.getPath()));
            BufferedReader in = new BufferedReader(fileReader);

            String licenseLine = ""; //$NON-NLS-1$
            while ((licenseLine = in.readLine()) != null) {
                license += licenseLine + "\n"; //$NON-NLS-1$
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return license;
    }
}
