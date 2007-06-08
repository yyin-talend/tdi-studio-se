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

import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.INodeReturn;
import org.talend.designer.core.i18n.Messages;

/**
 * Return parameters for each component. These returns are described in the xml file of the component. <br/>
 * 
 * $Id$
 * 
 */
public class NodeReturn implements INodeReturn {

    private String name;

    private String displayName;

    private String type = "String"; //$NON-NLS-1$

    private String availability;

    private String varName;

    /*
     * 
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#getAvailability()
     */
    public String getAvailability() {
        return this.availability;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#setAvailability(java.lang.String)
     */
    public void setAvailability(final String availability) {
        this.availability = Messages.getString("NodeReturn.Availability." + availability); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#getCode()
     */
    public String getVarName() {
        switch (LanguageManager.getCurrentLanguage()) {
        case PERL:
            return "$_globals{__UNIQUE_NAME__}{" + varName + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        case JAVA:
            if ((type.compareTo("String") == 0) || (type.compareTo("Integer") == 0)) { //$NON-NLS-1$ //$NON-NLS-2$
                type = "id_" + type; //$NON-NLS-1$
            }
            try {
                type = JavaTypesManager.getTypeToGenerate(type, true);
            } catch (Exception e) {
                type = "String"; //$NON-NLS-1$
            }
            return "((" + type + ")globalMap.get(\"__UNIQUE_NAME___" + name + "\"))"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        default:
            return "$_globals{__UNIQUE_NAME__}{" + varName + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#setCode(java.lang.String)
     */
    public void setVarName(final String variableName) {
        varName = variableName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#getDisplayName()
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#setDisplayName(java.lang.String)
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#setName(java.lang.String)
     */
    public void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#getType()
     */
    public String getType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeReturn#setType(org.talend.core.model.metadata.EMetadataType)
     */
    public void setType(final String type) {
        this.type = type;
    }
}
