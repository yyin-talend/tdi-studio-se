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
package org.talend.designer.core.model.context;

import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.process.IContextParameter;

/**
 * Parameter in a context. <br/>
 * 
 * $Id$
 * 
 */
public class ContextParameter implements IContextParameter, Cloneable {

    String name;

    EMetadataType type;

    String value;

    String prompt;

    String comment;

    boolean promptNeeded;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setName(java.lang.String)
     */
    public void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#getPrompt()
     */
    public String getPrompt() {
        return this.prompt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setPrompt(java.lang.String)
     */
    public void setPrompt(final String prompt) {
        this.prompt = prompt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#getType()
     */
    public EMetadataType getType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setType(org.talend.core.model.metadata.EMetadataType)
     */
    public void setType(final EMetadataType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#getValue()
     */
    public String getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setValue(java.lang.String)
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setComment(java.lang.String)
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#isPromptNeeded()
     */
    public boolean isPromptNeeded() {
        return this.promptNeeded;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.context.IDesignerContextParameter#setPromptNeeded(boolean)
     */
    public void setPromptNeeded(boolean promptNeeded) {
        this.promptNeeded = promptNeeded;
    }

    public IContextParameter clone() {
        IContextParameter clonedContextParameter = null;
        try {
            clonedContextParameter = (IContextParameter) super.clone();
        } catch (CloneNotSupportedException e) {
            // nothing
        }
        return clonedContextParameter;
    }
}
