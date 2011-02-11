// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.business.diagram.custom.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class BusinessDeleteListener implements PropertyChangeListener {

    private IRepositoryObject businessObjectToDelete;

    private boolean isDeleteBusinessLogical;

    private boolean isDeleteBusinessPhysical;

    private IProxyRepositoryFactory factory = null;

    public BusinessDeleteListener() {
        factory = CorePlugin.getDefault().getProxyRepositoryFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {

        isDeleteBusinessLogical = event.getPropertyName().equals(ERepositoryActionName.BUSINESS_DELETE_TO_RECYCLE_BIN.getName());
        isDeleteBusinessPhysical = event.getPropertyName().equals(ERepositoryActionName.BUSINESS_DELETE_FOREVER.getName());

        if (!isDeleteBusinessLogical && !isDeleteBusinessPhysical) {
            return;
        }

        if (!(event.getNewValue() instanceof IRepositoryObject)) {
            return;
        }
        businessObjectToDelete = (IRepositoryObject) event.getNewValue();

        if (businessObjectToDelete != null) {

            try {

                deleteSVGObjectLogicalOrPhysical(businessObjectToDelete, isDeleteBusinessLogical);

            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public void deleteSVGObjectLogicalOrPhysical(IRepositoryObject object, boolean isDeleteBusinessLogical)
            throws PersistenceException {
        IRepositoryViewObject svgObjectToDelete = ProxyRepositoryFactory.getInstance().getLastVersion("svg_" + object.getId());

        if (svgObjectToDelete != null) {
            try {
                if (isDeleteBusinessLogical) {
                    factory.deleteObjectLogical(svgObjectToDelete);
                } else {
                    factory.deleteObjectPhysical(svgObjectToDelete);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return;
            }
        }
    }

}
