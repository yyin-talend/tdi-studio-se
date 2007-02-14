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
package org.talend.repository.model.migration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.migration.conversions.IComponentConversion;
import org.talend.repository.model.migration.conversions.RenameComponentConversion;
import org.talend.repository.model.migration.filters.IComponentFilter;
import org.talend.repository.model.migration.filters.NameComponentFilter;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ModifyComponentsAction {

    public static void searchAndRename(String oldName, String newName) throws PersistenceException, IOException, CoreException {
        searchAndModify(new NameComponentFilter(oldName), Arrays.<IComponentConversion> asList(new RenameComponentConversion(
                newName)));
    }

    public static void searchAndModify(IComponentFilter filter, List<IComponentConversion> conversions)
            throws PersistenceException, IOException, CoreException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS, true);

        boolean modified;
        for (IRepositoryObject mainobject : list) {
            List<IRepositoryObject> allVersion = factory.getAllVersion(mainobject.getId());
            for (IRepositoryObject object : allVersion) {
                modified = false;
                ProcessItem item = (ProcessItem) object.getProperty().getItem();
                for (Object o : item.getProcess().getNode()) {
                    NodeType currentNode = (NodeType) o;

                    if (filter.accept(currentNode)) {
                        for (IComponentConversion conversion : conversions) {
                            conversion.transform(currentNode);
                        }
                        modified = true;
                    }
                }
                if (modified) {
                    factory.save(item);
                }
            }
        }
    }

}
