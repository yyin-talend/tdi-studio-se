// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.routines;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.PropertiesWizardPage;

/**
 * Page for new project details. <br/>
 * 
 * $Id: NewProcessWizardPage.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class NewRoutineWizardPage extends PropertiesWizardPage {

    private static final String DESC = Messages.getString("NewRoutineWizardPage.description"); //$NON-NLS-1$

    /**
     * Constructs a new NewProjectWizardPage.
     * 
     */
    public NewRoutineWizardPage(Property property, IPath destinationPath) {
        super("WizardPage", property, destinationPath); //$NON-NLS-1$

        setTitle(Messages.getString("NewRoutineWizardPage.title")); //$NON-NLS-1$
        setDescription(DESC);
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        super.createControl(container);

        setControl(container);
        updateContent();
        addListeners();
        setPageComplete(false);
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.ROUTINES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.PropertiesWizardPage#evaluateTextField()
     */
    @Override
    protected void evaluateTextField() {
        super.evaluateTextField();
        if (nameStatus.getSeverity() == IStatus.OK) {
            evaluateNameInJob();
        }
    }

    @Override
    protected List<IRepositoryViewObject> loadRepViewObjectWithOtherTypes() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();

        // List for common process
        list.addAll(loadRepViewObjectWithOtherTypes(ERepositoryObjectType.PROCESS));

        // List for m/r process
        ERepositoryObjectType mrRepObjType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "PROCESS_MR");//$NON-NLS-1$
        list.addAll(loadRepViewObjectWithOtherTypes(mrRepObjType));

        // pig udf
        list.addAll(loadRepViewObjectWithOtherTypes(ERepositoryObjectType.PIG_UDF));
        return list;
    }

}
