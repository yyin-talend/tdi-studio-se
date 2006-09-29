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

import java.util.List;

import org.talend.commons.exception.IllegalPluginConfigurationException;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProviders;
import org.talend.core.model.process.IExternalNode;
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
public class ExternalNodesFactory {

    public static IExternalNode getInstance(final String extensionId) {
        List<IExternalNode> listComponents;
        try {
            listComponents = ExtensionImplementationProviders.getInstance(ExtensionPointFactory.EXTERNAL_COMPONENT, extensionId);
        } catch (IllegalPluginConfigurationException e) {
            // e.printStackTrace();
            return null;
        }
        return listComponents.get(0);
    }
}
