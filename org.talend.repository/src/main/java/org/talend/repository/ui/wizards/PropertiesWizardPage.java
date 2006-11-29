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
package org.talend.repository.ui.wizards;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.properties.PurposeStatusSection;
import org.talend.repository.ui.properties.StatusHelper;

/**
 * Wizard page contains common properties fields.<br/>
 * 
 * $Id$
 * 
 */
public abstract class PropertiesWizardPage extends WizardPage {

    /** Name text. */
    protected Text nameText;

    /** Purpose text. */
    protected Text purposeText;

    /** Comment text. */
    protected Text descriptionText;

    /** Author text. */
    protected Text authorText;

    /** Version text. */
    protected Text versionText;

    /** Status text. */
    // protected Text statusText;
    private CCombo statusText;

    /** Version upgrade major button. */
    private Button versionMajorBtn;

    /** Version upgrade minor button. */
    private Button versionMinorBtn;

    private IStatus nameStatus;

    private IStatus purposeStatus;

    private IStatus commentStatus;

    private boolean nameModifiedByUser = false;

    private boolean update = false;

    protected Property property;

    protected IPath destinationPath;

    private boolean readOnly;

    private StatusHelper statusHelper = new StatusHelper(ProxyRepositoryFactory.getInstance());

    protected PropertiesWizardPage(String pageName, Property property, IPath destinationPath) {
        this(pageName, property, destinationPath, false);
    }

    protected PropertiesWizardPage(String pageName, Property property, IPath destinationPath, boolean readOnly) {
        super(pageName);
        this.destinationPath = destinationPath;

        this.readOnly = readOnly;

        nameStatus = createOkStatus();
        purposeStatus = createOkStatus();
        commentStatus = createOkStatus();

        this.property = property;
    }

    /**
     * Getter for isUpdate.
     * 
     * @return the isUpdate
     */
    public boolean isUpdate() {
        return this.update;
    }

    /**
     * Sets the isUpdate.
     * 
     * @param isUpdate the isUpdate to set
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Getter for nameModifiedByUser.
     * 
     * @return the nameModifiedByUser
     */
    public boolean isNameModifiedByUser() {
        return this.nameModifiedByUser;
    }

    /**
     * Sets the nameModifiedByUser.
     * 
     * @param nameModifiedByUser the nameModifiedByUser to set
     */
    public void setNameModifiedByUser(boolean nameModifiedByUser) {
        this.nameModifiedByUser = nameModifiedByUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        GridData data;

        // Name
        Label nameLab = new Label(parent, SWT.NONE);
        nameLab.setText("Name");

        nameText = new Text(parent, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.setEditable(!readOnly);

        // Purpose
        Label purposeLab = new Label(parent, SWT.NONE);
        purposeLab.setText("Purpose");

        purposeText = new Text(parent, SWT.BORDER);
        purposeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        purposeText.setEditable(!readOnly);

        // Description
        Label descriptionLab = new Label(parent, SWT.NONE);
        descriptionLab.setText("Description");
        descriptionLab.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        descriptionText = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 60;
        descriptionText.setLayoutData(data);
        descriptionText.setEditable(!readOnly);

        // Author
        Label authorLab = new Label(parent, SWT.NONE);
        authorLab.setText("Author");

        authorText = new Text(parent, SWT.BORDER);
        authorText.setEnabled(false);
        authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Version
        Label versionLab = new Label(parent, SWT.NONE);
        versionLab.setText("Version");

        Composite versionContainer = new Composite(parent, SWT.NONE);
        versionContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout versionLayout = new GridLayout(3, false);
        versionLayout.marginHeight = 0;
        versionLayout.marginWidth = 0;
        versionLayout.horizontalSpacing = 0;
        versionContainer.setLayout(versionLayout);

        versionText = new Text(versionContainer, SWT.BORDER);
        versionText.setEnabled(false);
        versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        versionMajorBtn = new Button(versionContainer, SWT.PUSH);
        versionMajorBtn.setText("M");
        versionMajorBtn.setEnabled(!readOnly);

        versionMinorBtn = new Button(versionContainer, SWT.PUSH);
        versionMinorBtn.setText("m");
        versionMinorBtn.setEnabled(!readOnly);

        // Status
        Label statusLab = new Label(parent, SWT.NONE);
        statusLab.setText("Status");

        statusText = new CCombo(parent, SWT.BORDER);
        List<org.talend.core.model.properties.Status> statusList;
        try {
            statusList = statusHelper.getStatusList(property);
            statusText.setItems(PurposeStatusSection.toArray(statusList));
            statusText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            statusText.setEditable(!readOnly);
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void updateContent() {
        nameText.setText(StringUtils.trimToEmpty(property.getLabel()));
        purposeText.setText(StringUtils.trimToEmpty(property.getPurpose()));
        descriptionText.setText(StringUtils.trimToEmpty(property.getDescription()));
        authorText.setText(property.getAuthor().getLogin());
        versionText.setText(property.getVersion());
        statusText.setText(statusHelper.getStatusLabel(property.getStatusCode()));

        evaluateFields();
    }

    protected void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                // if (!update) {
                if (nameText.getText().length() == 0) {
                    nameModifiedByUser = false;
                } else {
                    nameModifiedByUser = true;
                }
                // }
                evaluateTextField();
            }
        });

        purposeText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (purposeText.getText().length() == 0) {
                    purposeStatus = createStatus(IStatus.WARNING, "Empty purpose is discouraged.");
                } else {
                    purposeStatus = createOkStatus();
                }
                property.setPurpose(StringUtils.trimToNull(purposeText.getText()));
                updatePageStatus();
            }
        });

        descriptionText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (descriptionText.getText().length() == 0) {
                    commentStatus = createStatus(IStatus.WARNING, "Empty description is discouraged.");
                } else {
                    commentStatus = createOkStatus();
                }
                property.setDescription(StringUtils.trimToNull(descriptionText.getText()));
                updatePageStatus();
            }
        });

        versionMajorBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String version = property.getVersion();
                version = VersionUtils.upMajor(version);
                versionText.setText(version);
                property.setVersion(version);
            }
        });

        versionMinorBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String version = property.getVersion();
                version = VersionUtils.upMinor(version);
                versionText.setText(version);
                property.setVersion(version);
            }
        });

        statusText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                property.setStatusCode(statusHelper.getStatusCode(statusText.getText()));
            }

        });
    }

    protected void evaluateFields() {
        evaluateTextField();
    }

    protected void evaluateTextField() {
        if (nameText.getText().length() == 0) {
            nameStatus = createStatus(IStatus.ERROR, "Name is empty.");
        } else if (!Pattern.matches(RepositoryConstants.FILE_PATTERN, nameText.getText())) {
            nameStatus = createStatus(IStatus.ERROR, "Name contains incorrect characters.");
        } else if (!isValid(nameText.getText()) && nameModifiedByUser) {
            nameStatus = createStatus(IStatus.ERROR, "Item with the same name already exists");
        } else {
            nameStatus = createOkStatus();
        }

        property.setLabel(StringUtils.trimToNull(nameText.getText()));
        updatePageStatus();
    }

    protected IStatus[] getStatuses() {
        return new IStatus[] { nameStatus, purposeStatus, commentStatus };
    }

    protected static IStatus createOkStatus() {
        return new Status(IStatus.OK, RepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }

    protected static IStatus createStatus(int severity, String message) {
        return new Status(severity, RepositoryPlugin.PLUGIN_ID, IStatus.OK, message, null);
    }

    protected void updatePageStatus() {
        setMessage(findMostSevere());
        updatePageComplete();
    }

    protected void updatePageComplete() {
        setMessage(findMostSevere());
        setPageComplete(findMostSevere().getSeverity() != IStatus.ERROR);
    }

    protected IStatus findMostSevere() {
        IStatus[] statuses = getStatuses();
        IStatus severeStatus = statuses[0];
        for (IStatus status : statuses) {
            if (status.getSeverity() > severeStatus.getSeverity()) {
                severeStatus = status;
            }
        }
        return severeStatus;
    }

    protected void setMessage(IStatus status) {
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(status.getMessage());
            // setMessage(""); //$NON-NLS-1$
        } else {
            if (StringUtils.isNotEmpty(status.getMessage())) {
                setMessage(status.getMessage(), status.getSeverity());
            } else {
                setMessage(getDescription());
            }
            setErrorMessage(null);
        }
    }

    public boolean isValid(String itemName) {
        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            return repositoryFactory.isNameAvailable(property.getItem(), itemName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    public abstract ERepositoryObjectType getRepositoryObjectType();
}
