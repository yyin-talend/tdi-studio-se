// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.view.di.viewer.seeker;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.seeker.AbstractRepoViewSeeker;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class NormalRepoViewSeeker extends AbstractRepoViewSeeker {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.seeker.AbstractRepoViewSeeker#validType(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    protected boolean validType(ERepositoryObjectType itemType) {
        if (itemType != null) {
            if (itemType.equals(ERepositoryObjectType.PROCESS) || itemType.equals(ERepositoryObjectType.BUSINESS_PROCESS)
                    || itemType.equals(ERepositoryObjectType.CONTEXT) || itemType.equals(ERepositoryObjectType.ROUTINES)
                    || itemType.equals(ERepositoryObjectType.SQLPATTERNS)) {
                return true;
            }
        }
        return false;
    }

}
