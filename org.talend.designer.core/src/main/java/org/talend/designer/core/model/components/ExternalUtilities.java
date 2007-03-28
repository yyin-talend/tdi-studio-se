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
package org.talend.designer.core.model.components;

import java.util.List;

import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalUtilities {

    public static IExternalNode getExternalNodeReadyToOpen(Node node) {
        IExternalNode externalNode = node.getExternalNode();

        if (externalNode == null) {
            return null;
        }
        externalNode.setExternalData(node.getExternalData());

        IODataComponentContainer inAndOut = new IODataComponentContainer();

        List<IODataComponent> inputs = inAndOut.getInputs();
        for (IConnection currentConnection : node.getIncomingConnections()) {
            IODataComponent component = new IODataComponent(currentConnection);
            inputs.add(component);
        }
        List<IODataComponent> outputs = inAndOut.getOuputs();
        for (IConnection currentConnection : node.getOutgoingConnections()) {
            IODataComponent component = new IODataComponent(currentConnection);
            outputs.add(component);
        }

        externalNode.setIODataComponents(inAndOut);

        return externalNode;
    }
}
