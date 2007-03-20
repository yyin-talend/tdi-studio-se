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
package org.talend.repository.ui.wizards.importExternalLib;

import java.io.File;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.properties.RoutineItem;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * Page of the Job Scripts Export Wizard. <br/>
 * 
 * @referto WizardArchiveFileResourceExportPage1 $Id: JobScriptsExportWizardPage.java 1 2006-12-13 下午03:09:07 bqian
 * 
 */
public class ImportExternalJarPage extends ImportExternalLibPage {

    private FileFieldEditor fileField;

    private Button requiredButton;

    private Text descriptionText;

    /**
     * DOC acer ImportExternalJarPage constructor comment.
     * 
     * @param pageName
     * @param title
     * @param titleImage
     */
    public ImportExternalJarPage(IStructuredSelection selection) {
        super(Messages.getString("ImportExternalJarPage.pageTitle"), selection); //$NON-NLS-1$
        this.setMessage(Messages.getString("ImportExternalJarPage.pageMessage")); //$NON-NLS-1$
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
        descriptionLabel.setText(Messages.getString("ImportExternalJarPage.descriptionText.label")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().applyTo(descriptionLabel);

        descriptionText = new Text(composite, SWT.BORDER);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(descriptionText);

        requiredButton = new Button(composite, SWT.CHECK);
        requiredButton.setText(Messages.getString("ImportExternalJarPage.requiredButton.label")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(3, 1).applyTo(requiredButton);

        fileField = new FileFieldEditor("ImportExternalJarPage.fileField", Messages.getString("ImportExternalJarPage.fileField.label"), composite); //$NON-NLS-1$ //$NON-NLS-2$
        fileField.setFileExtensions(new String[] { "*.jar" }); //$NON-NLS-1$
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.importExternalLib.ImportExternalLibPage#finish()
     */
    public boolean finish() {
        final String path = fileField.getStringValue();

        BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

            public void run() {
                RepositoryNode node = getSelectedRepositoryNode();
                RoutineItem routine = (RoutineItem) node.getObject().getProperty().getItem();
                final File file = new File(path);

                IMPORTType importType = ComponentFactory.eINSTANCE.createIMPORTType();
                importType.setMESSAGE(descriptionText.getText());

                importType.setMODULE(file.getName());
                importType.setNAME(routine.getProperty().getLabel());
                importType.setREQUIRED(requiredButton.getSelection());

                routine.getImports().add(importType);

                try {
                    CorePlugin.getDefault().getLibrariesService().deployLibrary(file.toURL());
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
                
                CorePlugin.getDefault().getLibrariesService().resetModulesNeeded();
                
            }
        });

        return true;
    }
}
