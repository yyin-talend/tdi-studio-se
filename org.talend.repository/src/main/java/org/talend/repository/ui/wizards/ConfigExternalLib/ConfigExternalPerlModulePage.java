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
package org.talend.repository.ui.wizards.ConfigExternalLib;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Page of the Job Scripts Export Wizard. <br/>
 * 
 * @referto WizardArchiveFileResourceExportPage1 $Id: JobScriptsExportWizardPage.java 1 2006-12-13 下午03:09:07 bqian
 * 
 */
public class ConfigExternalPerlModulePage extends ConfigExternalLibPage {

    /**
     * DOC acer ImportExternalJarPage constructor comment.
     * 
     * @param pageName
     * @param title
     * @param titleImage
     */
    public ConfigExternalPerlModulePage(IStructuredSelection selection) {
        super("Import External Jar", selection);
        this.setMessage("Import external JAR");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setLayout(new GridLayout(3, false));
        composite.setFont(parent.getFont());

        Label descriptionLabel = new Label(composite, SWT.NONE);
        descriptionLabel.setText("Description:");
        GridDataFactory.fillDefaults().applyTo(descriptionLabel);

        Text descriptionText = new Text(composite, SWT.BORDER);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(descriptionText);

        Button requiredButton = new Button(composite, SWT.CHECK);
        requiredButton.setText("Required");
        GridDataFactory.fillDefaults().span(3, 1).applyTo(requiredButton);

        FileFieldEditor fileField = new FileFieldEditor("ImportExternalJarPage.fileField", "Jar File:", composite);
        fileField.setFileExtensions(new String[] { "*.jar" });
        // createDestinationGroup(composite);
        //
        // createOptionsGroup(composite);
        //
        // restoreResourceSpecificationWidgetValues(); // ie.- local
        // restoreWidgetValues(); // ie.- subclass hook
        //
        // updateWidgetEnablements();
        // setPageComplete(determinePageCompletion());
        setErrorMessage(null); // should not initially have error message

        setControl(composite);

    }

    public boolean finish() {
        return true;
    }
}
