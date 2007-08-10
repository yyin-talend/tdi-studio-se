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
package org.talend.repository.preference;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.talend.core.model.properties.Status;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryPreferenceStore;

/**
 */
public class StatusPreferenceInitializer extends AbstractPreferenceInitializer {

    public StatusPreferenceInitializer() {
        super();
    }
    
    @Override
    public void initializeDefaultPreferences() {
        RepositoryPreferenceStore preferenceStore = new RepositoryPreferenceStore(ProxyRepositoryFactory.getInstance());
        try {
            preferenceStore.load();
            String statusString = preferenceStore.getString(Status.TECHNICAL_STATUS)
                    + preferenceStore.getString(Status.DOCUMENTATION_STATUS);
            if (statusString.equals("")) {
                preferenceStore.setToDefault(Status.TECHNICAL_STATUS);
                preferenceStore.setToDefault(Status.DOCUMENTATION_STATUS);
                preferenceStore.save();
            }
        } catch (Exception e) {
            IStatus status = new org.eclipse.core.runtime.Status(IStatus.WARNING, RepositoryPlugin.PLUGIN_ID,
                    IStatus.OK, e.getMessage(), e);
            RepositoryPlugin.getDefault().getLog().log(status);
        }

    }

}
