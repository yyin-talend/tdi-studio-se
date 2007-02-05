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
package org.talend.repository.license;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LicenseManagement {

    // LICENSE_VALIDATION_DONE = 1 : registration OK
    private static final double LICENSE_VALIDATION_DONE = 2;

    public static void acceptLicense() throws BusinessException {
        PlatformUI.getPreferenceStore().setValue("LICENSE_VALIDATION_DONE", 1); //$NON-NLS-1$
    }

    /**
     * DOC mhirt Comment method "isLicenseValidated".
     * @return
     */
    public static boolean isLicenseValidated() {
        initPreferenceStore();
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        if (prefStore.getInt("LICENSE_VALIDATION_DONE") != 1) { //$NON-NLS-1$
            return false;
        }
        return true;
    }

    /**
     * DOC mhirt Comment method "init".
     * @return
     */
    private static void initPreferenceStore() {
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        if (prefStore.getDefaultInt("LICENSE_VALIDATION_DONE") == 0) { //$NON-NLS-1$
            prefStore.setDefault("LICENSE_VALIDATION_DONE", LICENSE_VALIDATION_DONE); //$NON-NLS-1$
        }
    }
}
