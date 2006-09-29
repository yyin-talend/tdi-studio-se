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
package org.talend.repository.ui.wizards.documentation;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ui.wizards.PropertiesWizardPage;

/**
 * Wizard page collecting informations to create a new IDocumentation. <br/>
 * 
 * $Id$
 * 
 */
public class DocumentationPage extends PropertiesWizardPage {

    /** Filename text. */
    private Text filenameText;

    /** CutFilename text. */
    private Text originalFilenameText;

    /** Browse button. */
    private Button browseBtn;

    private IStatus filenameStatus;

    /**
     * Constructs a new DocumentationCreatePage.
     */
    public DocumentationPage(Property property, IPath destinationPath) {
        super("DocumentationCreatePage", property, destinationPath);

        setTitle("Documentation");
        this.filenameStatus = createOkStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        GridData data;

        // Source file
        Label filenameLab = new Label(container, SWT.NONE);
        filenameLab.setText("Source document file");

        Composite filenameContainer = new Composite(container, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = layout.numColumns - 1;
        filenameContainer.setLayoutData(data);
        GridLayout filenameLayout = new GridLayout(2, false);
        filenameLayout.marginHeight = 0;
        filenameLayout.marginWidth = 0;
        filenameContainer.setLayout(filenameLayout);

        filenameText = new Text(filenameContainer, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        filenameText.setLayoutData(data);

        super.createControl(container);

        // Original Name
        Label originalNameLab = new Label(container, SWT.NONE);
        originalNameLab.setText("Original Name");

        originalFilenameText = new Text(container, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = layout.numColumns - 1;
        originalFilenameText.setLayoutData(data);
        originalFilenameText.setEditable(false);
        originalFilenameText.setEnabled(false);

        browseBtn = new Button(filenameContainer, SWT.PUSH);
        browseBtn.setText("Browse...");

        setControl(container);
        updateContent();
        evaluateFields();
        addListeners();
        updatePageComplete();
    }

    protected void evaluateFields() {
        super.evaluateFields();
        evaluateFileNameField();
    }

    protected void evaluateFileNameField() {

        IPath filePath = null;
        if (filenameText.getText().length() == 0) {
            if (isUpdate()) {
                filenameStatus = createOkStatus();
            } else {
                filenameStatus = createStatus(IStatus.ERROR, "Source document is not set.");
            }
        } else {
            filePath = new Path(filenameText.getText());
            File file = filePath.toFile();
            if (!file.exists() || !file.isFile()) {
                filenameStatus = createStatus(IStatus.ERROR, "Source document does not exists.");
                filePath = null;
            } else {
                filenameStatus = createOkStatus();
            }
        }

        getDocumentation().setDocFilePath(filePath);

        // if (nameText == null || nameText.getText().length() == 0) {
        if (!isNameModifiedByUser() && !isUpdate()) {
            property.setLabel(filenameText.getText());
        }
        originalFilenameText.setText(getDocumentation().getDocOriginalName());
        // if (nameText.getText().compareTo("") == 0) {
        if (!isNameModifiedByUser() && !isUpdate()) {
            nameText.setText(getDocumentation().getDocOriginalName());
            if (!isUpdate()) {
                setNameModifiedByUser(false);
            }
        }

        updatePageStatus();
    }

    protected void addListeners() {
        super.addListeners();
        filenameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                evaluateFileNameField();
            }
        });

        browseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
                String filename = dlg.open();
                filenameText.setText((filename != null ? filename : ""));
            }
        });
    }

    protected void updateContent() {
        super.updateContent();
        originalFilenameText.setText(StringUtils.trimToEmpty(getDocumentation().getDocOriginalName()));
    }

    private IDocumentationContext getDocumentation() {
        return (IDocumentationContext) getWizard();
    }

    protected IStatus[] getStatuses() {
        IStatus[] toReturn = super.getStatuses();
        return (IStatus[]) ArrayUtils.addAll(toReturn, new IStatus[] { filenameStatus });
    }

    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.DOCUMENTATION;
    }
}
