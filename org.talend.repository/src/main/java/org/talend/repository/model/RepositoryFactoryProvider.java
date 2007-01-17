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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProviders;
import org.talend.commons.utils.workbench.extensions.ExtensionPointImpl;
import org.talend.commons.utils.workbench.extensions.ISimpleExtensionPoint;

/**
 * Provides, using extension points, implementation of many factories.
 * 
 * <ul>
 * <li>IProcessFactory</li>
 * </ul>
 * 
 * $Id$
 */
public class RepositoryFactoryProvider {

    private static List<IRepositoryFactory> list = null;

    public static final ISimpleExtensionPoint REPOSITORY_PROVIDER = new ExtensionPointImpl("org.talend.core.repository_provider",
            "RepositoryFactory", 1, -1);

    public static List<IRepositoryFactory> getAvailableRepositories() {
        if (list == null) {
            list = new ArrayList<IRepositoryFactory>();
            List<IConfigurationElement> extension = ExtensionImplementationProviders.getInstanceV2(REPOSITORY_PROVIDER);

            for (IConfigurationElement current : extension) {
                try {
                    IRepositoryFactory currentAction = (IRepositoryFactory) current.createExecutableExtension("class");
                    currentAction.setId(current.getAttribute("id"));
                    currentAction.setName(current.getAttribute("name"));
                    currentAction.setAuthenticationNeeded(new Boolean(current.getAttribute("authenticationNeeded")));

                    // Getting dynamic login fields:
                    for (IConfigurationElement currentLoginField : current.getChildren("loginField")) {
                        DynamicFieldBean key = new DynamicFieldBean(currentLoginField.getAttribute("id"), currentLoginField
                                .getAttribute("name"), new Boolean(currentLoginField.getAttribute("required")));
                        currentAction.getFields().add(key);
                    }

                    list.add(currentAction);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static IRepositoryFactory getRepositoriyById(String id) {
        for (IRepositoryFactory current : getAvailableRepositories()) {
            if (current.getId().equals(id)) {
                return current;
            }
        }
        return null;
    }
}
