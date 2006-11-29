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
import org.talend.repository.model.extensions.ExtensionPointFactory;

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

    private static IRepositoryFactory localSingleton = null;

    private static IRepositoryFactory remoteSingleton = null;

    private static IRepositoryFactory databaseSingleton = null;

    private static boolean init = false;

    // /**
    // * Returns a IRepositoryFactory based on the default context.
    // */
    // public static IRepositoryFactory getInstance() {
    // RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
    // Context.REPOSITORY_CONTEXT_KEY);
    // init(repositoryContext);
    // return getInstance(repositoryContext);
    // }

    // public static IRepositoryFactory getInstance(RepositoryContext repositoryContext) {
    // init(repositoryContext);
    // IRepositoryFactory toReturn = null;
    // if (repositoryContext.getType() == ERepositoryType.LOCAL) {
    // toReturn = localSingleton;
    // } else if (repositoryContext.getType() == ERepositoryType.REMOTE) {
    // toReturn = remoteSingleton;
    // } else if (repositoryContext.getType() == ERepositoryType.DATABASE) {
    // toReturn = databaseSingleton;
    // }
    // toReturn.setRepositoryContext(repositoryContext);
    // return toReturn;
    // }

    // private static void init(RepositoryContext repositoryContext) {
    // if (!init) {
    // List<IRepositoryFactory> factoriesFromProvider = ExtensionImplementationProviders
    // .getInstance(ExtensionPointFactory.REPOSITORY_PROVIDER);
    //
    // for (IRepositoryFactory repositoryFactory : factoriesFromProvider) {
    // ERepositoryType type = repositoryFactory.getType();
    // switch (type) {
    // case LOCAL:
    // localSingleton = new ProxyRepositoryFactory(repositoryFactory);
    // localSingleton.setRepositoryContext(repositoryContext);
    // localSingleton.initialize();
    // init = true;
    // break;
    // case REMOTE:
    // remoteSingleton = new ProxyRepositoryFactory(repositoryFactory);
    // remoteSingleton.setRepositoryContext(repositoryContext);
    // remoteSingleton.initialize();
    // break;
    // case DATABASE:
    // databaseSingleton = new ProxyRepositoryFactory(repositoryFactory);
    // databaseSingleton.setRepositoryContext(repositoryContext);
    // databaseSingleton.initialize();
    // break;
    // }
    // }
    // }
    // }

    public static List<IRepositoryFactory> getAvailableRepositories() {
        List<IRepositoryFactory> toReturn = new ArrayList<IRepositoryFactory>();
        List<IConfigurationElement> extension = ExtensionImplementationProviders
                .getInstanceV2(ExtensionPointFactory.REPOSITORY_PROVIDER);

        for (IConfigurationElement current : extension) {
            try {
                IRepositoryFactory currentAction = (IRepositoryFactory) current.createExecutableExtension("class");
                String attribute = current.getAttribute("name");
                currentAction.setName(attribute);
                currentAction.setAuthenticationNeeded(new Boolean(current.getAttribute("authenticationNeeded")));
                toReturn.add(currentAction);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        return toReturn;
    }
}
