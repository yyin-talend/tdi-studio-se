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
package org.talend.designer.abstractmap.managers;

import org.apache.log4j.Level;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.designer.abstractmap.ui.properties.LINK_STYLE;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public abstract class AbstractUIManager {

    private LINK_STYLE linkStyle;

    /**
     * DOC amaumont AbstractUIManager constructor comment.
     */
    public AbstractUIManager() {
        super();
    }

    /**
     * DOC amaumont Comment method "getCountLinks".
     */
    public int getCountLinks() {
        return getAbstractMapperManager().getLinkManager().getCurrentNumberLinks();
    }
    
    public LINK_STYLE getLinkStyle() {
        if (linkStyle == null) {
            
            String elementParameterValue = (String) getAbstractMapperManager().getElementParameterValue("LINK_STYLE");
            if ("AUTO".equals(elementParameterValue)) {
                if (getCountLinks() > 50) {
                    linkStyle = LINK_STYLE.LINE;
                } else {
                    linkStyle = LINK_STYLE.BEZIER_CURVE;
                }
            } else {
                LINK_STYLE defaultLinkStyle = LINK_STYLE.BEZIER_CURVE;
                if (elementParameterValue != null) {
                    try {
                        linkStyle = LINK_STYLE.valueOf(elementParameterValue);
                    } catch (RuntimeException e) {
                        ExceptionHandler.process(e, Level.WARN);
                        linkStyle = defaultLinkStyle;
                    }
                } else {
                    linkStyle = defaultLinkStyle;
                }
            }
        }
        return linkStyle;
    }

    public abstract AbstractMapperManager getAbstractMapperManager();

}
