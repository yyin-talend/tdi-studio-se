// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards;

import java.util.List;

import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.ILastVersionChecker;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.wizards.context.ContextWizard;
import org.talend.repository.ui.wizards.documentation.DocumentationCreateWizard;
import org.talend.repository.ui.wizards.documentation.DocumentationUpdateWizard;

/**
 * DOC hywang class global comment. Detailled comment
 */
public abstract class CheckLastVersionRepositoryWizard extends RepositoryWizard implements ILastVersionChecker {

    public CheckLastVersionRepositoryWizard(IWorkbench workbench, boolean creation) {
        super(workbench, creation, false);
    }

    public CheckLastVersionRepositoryWizard(IWorkbench workbench, boolean creation, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
    }

    public boolean performFinish() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRepositoryObjectEditable() {
        if (getVersionItem() != null) {
            if (this instanceof ContextWizard || this instanceof DocumentationCreateWizard
                    || this instanceof DocumentationUpdateWizard) {
                return super.isRepositoryObjectEditable() && isLastVersion(getVersionItem());
            }
        }
        if (getConnectionItem() != null) {
            return super.isRepositoryObjectEditable() && isLastVersion(getConnectionItem());
        }
        return super.isRepositoryObjectEditable();
    }

    public Item getVersionItem() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.ILastVersionChecker#isLastVersion(org.talend.core.model.properties.Item)
     */
    public boolean isLastVersion(Item item) {
        if (item.getProperty() != null) {
            try {
                List<IRepositoryObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
                        item.getProperty().getId());
                if (allVersion != null && !allVersion.isEmpty()) {
                    if (allVersion.get(allVersion.size() - 1).getVersion().equals(item.getProperty().getVersion())
                            || (super.creation && item.getProperty().getId() == null)) {
                        return true;
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }
}
