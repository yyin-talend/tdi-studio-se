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
package org.talend.designer.mapper.external.data;

import java.io.Serializable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExternalMapperTableEntry implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = -6108773358595740424L;

    private String name;

    private String expression;

    /**
     * Talend types, used for var entries only.
     */
    private String type;

    /**
     * nullable, used for var entries only.
     */
    private boolean nullable;

    public ExternalMapperTableEntry() {
        super();
    }

    public ExternalMapperTableEntry(String expression) {
        super();
        this.expression = expression;
    }

    public ExternalMapperTableEntry(String name, String expression) {
        super();
        this.name = name;
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String metadataName) {
        this.name = metadataName;
    }

    /**
     * Talend types, used for var entries only.
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Talend types, used for var entries only.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for nullable.
     * 
     * @return the nullable
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Sets the nullable.
     * 
     * @param nullable the nullable to set
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    
    
}
