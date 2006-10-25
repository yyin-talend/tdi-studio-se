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
package org.talend.designer.core.model.components;

import org.talend.core.model.components.IComponent;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ModuleNeeded {

    private static final String GLOBAL_MODULE_NAME = "--";

    private IComponent component;

    private String moduleName;

    private String informationMsg;

    private boolean required;

    private int status = UNKNOWN;

    public static final int UNKNOWN = 0;

    public static final int INSTALLED = 1;

    public static final int NOT_INSTALLED = 2;

    /**
     * DOC smallet ModuleNeeded constructor comment.
     * 
     * @param component
     * @param moduleName
     * @param informationMsg
     * @param required
     * @param status
     */
    public ModuleNeeded(IComponent component, String moduleName, String informationMsg, boolean required) {
        super();
        this.component = component;
        this.moduleName = moduleName;
        this.informationMsg = informationMsg;
        this.required = required;
    }

    /**
     * Getter for component.
     * 
     * @return the component
     */
    public String getComponentName() {
        if (component == null) {
            return GLOBAL_MODULE_NAME;
        } else {
            return this.component.getName();
        }
    }

    /**
     * Sets the component.
     * 
     * @param component the component to set
     */
    public void setComponent(IComponent component) {
        this.component = component;
    }

    public String getInformationMsg() {
        return this.informationMsg;
    }

    public void setInformationMsg(String informationMsg) {
        this.informationMsg = informationMsg;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
