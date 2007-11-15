// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
