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
package org.talend.designer.core;

import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.context.ContextManager;

/**
 * Detailled comment <br/>
 * 
 * $Id: DesignerCoreService.java 1 2006-12-19 上午10:25:42 bqian
 * 
 */
public class DesignerCoreService implements IDesignerCoreService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IDesignerCoreService#getContextManagerFromXmlProcess(org.talend.core.model.properties.ProcessItem)
     */
    public IContextManager getContextManagerFromXmlProcess(ProcessItem processItem) {
        return ContextManager.getContextManagerFromXmlProcess(processItem);
    }

}
