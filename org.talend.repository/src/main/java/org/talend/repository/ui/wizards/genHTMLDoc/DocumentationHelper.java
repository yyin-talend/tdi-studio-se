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
package org.talend.repository.ui.wizards.genHTMLDoc;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC ftang class global comment. Detailled comment <br/>
 * 
 */
public class DocumentationHelper {

    /**
     * ftang Comment method "isSyncWithDocumentation".
     * 
     * @return
     */
    public static boolean isSyncWithDocumentation() {
        boolean isSync = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.DOC_GENERATION);
        return isSync;
    }

    /**
     * ftang Comment method "isFolderExisting".
     * @param type
     * @param path
     * @param folderName
     * @return
     */
    public static boolean isPathValid(ERepositoryObjectType type, IPath path, String folderName) {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            return repositoryFactory.isPathValid(type, path, folderName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }
}
