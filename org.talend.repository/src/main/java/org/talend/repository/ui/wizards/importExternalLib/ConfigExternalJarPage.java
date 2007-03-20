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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.properties.RoutineItem;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.repository.i18n.Messages;

/**
 * Page of the Job Scripts Export Wizard. <br/>
 * 
 * @referto WizardArchiveFileResourceExportPage1 $Id: JobScriptsExportWizardPage.java 1 2006-12-13 下午03:09:07 bqian
 * 
 */
public class ConfigExternalJarPage extends ConfigExternalLibPage {

    private Button requiredButton;

    private Text descriptionText;

    private Map<IMPORTType, File> newJarFiles = new HashMap<IMPORTType, File>();

    /**
     * DOC acer ImportExternalJarPage constructor comment.
     * 
     * @param pageName
     * @param title
     * @param titleImage
     */
    public ConfigExternalJarPage(IStructuredSelection selection) {
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

        LibraryField libField = new EditJavaRoutineExternalJARField(Messages.getString("ImportExternalJarPage.fileField.label"), composite);

        RoutineItem routine = getSelectedRoutine();
        EList routines = routine.getImports();

        libField.setInput(routines);

        setErrorMessage(null); // should not initially have error message

        setControl(composite);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.importExternalLib.ImportExternalLibPage#finish()
     */
    public boolean finish() {
        BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

            public void run() {
                // RoutineItem routine = getSelectedRoutine();
                // final File file = new File(path);
                //
                // IMPORTType importType = ComponentFactory.eINSTANCE.createIMPORTType();
                // importType.setMESSAGE(descriptionText.getText());
                //
                // importType.setMODULE(file.getName());
                // importType.setNAME(routine.getProperty().getLabel());
                // importType.setREQUIRED(requiredButton.getSelection());
                //
                // routine.getImports().add(importType);
                for (Iterator<File> iter = newJarFiles.values().iterator(); iter.hasNext();) {
                    File file = iter.next();
                    try {
                        CorePlugin.getDefault().getLibrariesService().deployLibrary(file.toURL());
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
                CorePlugin.getDefault().getLibrariesService().resetModulesNeeded();
            }
        });

        return true;
    }

    /**
     * subclass of the LibraryField. <br/>
     * 
     * $Id: ImportExternalJarPage.java Mar 20, 20074:15:28 PM bqian $
     * 
     */
    class EditJavaRoutineExternalJARField extends LibraryField {

        /**
         * 
         * @param name
         * @param parent
         */
        public EditJavaRoutineExternalJARField(String name, Composite parent) {
            super(name, parent);
        }

        /* (non-Javadoc)
         * @see org.talend.repository.ui.wizards.importExternalLib.TableField#afterDeleteSelection(java.util.List)
         */
        protected void afterDeleteSelection(List list) {
            for (int i = 0; i < list.size(); i++) {
                newJarFiles.remove(list.get(i));
            }
        }

        /* (non-Javadoc)
         * @see org.talend.repository.ui.wizards.importExternalLib.LibraryField#getNewInputObject()
         */
        protected List<IMPORTType> getNewInputObject() {
            List<IMPORTType> importTypes = new ArrayList<IMPORTType>();

            FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.MULTI);

            dialog.setFilterExtensions(new String[] { "*.jar" });
            dialog.open();
            String path = dialog.getFilterPath();
            String[] files = dialog.getFileNames();

            RoutineItem routine = getSelectedRoutine();
            for (int i = 0; i < files.length; i++) {
                File file = new File(path + File.separatorChar + files[i]);

                IMPORTType importType = ComponentFactory.eINSTANCE.createIMPORTType();
                importType.setMESSAGE(descriptionText.getText());

                importType.setMODULE(file.getName());
                importType.setNAME(routine.getProperty().getLabel());
                importType.setREQUIRED(requiredButton.getSelection());
                importTypes.add(importType);

                newJarFiles.put(importType, file);
            }

            return importTypes;
        }
    }
}
