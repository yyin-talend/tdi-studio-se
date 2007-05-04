// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.components.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.designer.components.Activator;
import org.talend.designer.components.ui.ComponentsPreferencePage;

/**
 * This class aim is to retrieve components from a file system folder to the location they must be for loading.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentsRetriever {

    public static void retrieveComponents(File target) {
        File externalComponentsLocation = getExternalComponentsLocation();
        if (externalComponentsLocation != null) {
            try {
                FilesUtils.copyFolder(externalComponentsLocation, target, true, null, null, true);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private static File getExternalComponentsLocation() {
        IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
        String path = prefStore.getString(ComponentsPreferencePage.USER_COMPONENTS_FOLDER);
        return (path == null || path.length() == 0 ? null : new File(path));
    }
}
