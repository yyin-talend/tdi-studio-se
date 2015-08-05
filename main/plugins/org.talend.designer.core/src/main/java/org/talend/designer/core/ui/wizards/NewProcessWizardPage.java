// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.core.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.ui.wizards.PropertiesWizardPage;

/**
 * Page for new project details. <br/>
 * 
 * $Id$
 * 
 */
public class NewProcessWizardPage extends PropertiesWizardPage {

    private static final String DESC = Messages.getString("NewProcessWizard.description"); //$NON-NLS-1$

    /**
     * Constructs a new NewProjectWizardPage.
     * 
     */
    public NewProcessWizardPage(Property property, IPath destinationPath) {
        super("WizardPage", property, destinationPath); //$NON-NLS-1$

        setTitle(Messages.getString("NewProcessWizard.title")); //$NON-NLS-1$
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
        // setPageComplete(false);
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.PROCESS;
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
            evaluateNameInRoutine();
        }
    }

    @Override
    protected List<IRepositoryViewObject> loadRepViewObjectWithOtherTypes() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();

        // List for m/r process
        ERepositoryObjectType mrRepObjType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "PROCESS_MR");//$NON-NLS-1$
        if (mrRepObjType != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);

                List<IRepositoryViewObject> mrList = service.getProxyRepositoryFactory().getAll(mrRepObjType, true, false);
                if (mrList != null && mrList.size() > 0) {
                    list.addAll(mrList);
                }
            }
        }

        // List for routine
        if (ERepositoryObjectType.ROUTINES != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);

                List<IRepositoryViewObject> mrList = service.getProxyRepositoryFactory().getAll(ERepositoryObjectType.ROUTINES,
                        true, false);
                if (mrList != null && mrList.size() > 0) {
                    list.addAll(mrList);
                }
            }
        }
        return list;
    }

}
