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
package org.talend.designer.codegen.config;

import org.talend.core.model.process.IConnection;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class SubTreeArgument {

    private IConnection inputSubtreeConnection;
    
    private boolean sourceComponentHasConditionnalOutputs;
    
    private boolean isMultiplyingOutputComponents = false;

    
    /**
     * Getter for inputSubtreeConnection.
     * @return the inputSubtreeConnection
     */
    public IConnection getInputSubtreeConnection() {
        return this.inputSubtreeConnection;
    }

    
    /**
     * Sets the inputSubtreeConnection.
     * @param inputSubtreeConnection the inputSubtreeConnection to set
     */
    public void setInputSubtreeConnection(IConnection inputSubtreeConnection) {
        this.inputSubtreeConnection = inputSubtreeConnection;
    }

    
    /**
     * Getter for sourceComponentHasConditionnalOutputs.
     * @return the sourceComponentHasConditionnalOutputs
     */
    public boolean isSourceComponentHasConditionnalOutputs() {
        return this.sourceComponentHasConditionnalOutputs;
    }

    
    /**
     * Sets the sourceComponentHasConditionnalOutputs.
     * @param sourceComponentHasConditionnalOutputs the sourceComponentHasConditionnalOutputs to set
     */
    public void setSourceComponentHasConditionnalOutputs(boolean sourceComponentHasConditionnalOutputs) {
        this.sourceComponentHasConditionnalOutputs = sourceComponentHasConditionnalOutputs;
    }
    
    /**
     * Getter for isMultiplyingOutputComponents.
     * @return the isMultiplyingOutputComponents
     */
    public boolean isMultiplyingOutputComponents() {
        return isMultiplyingOutputComponents;
    }
    
    /**
     * Sets the isMultiplyingOutputComponents.
     * @param isMultiplyingOutputComponents the isMultiplyingOutputComponents to set
     */
    public void setMultiplyingOutputComponents(boolean isMultiplyingOutputComponents) {
        this.isMultiplyingOutputComponents = isMultiplyingOutputComponents;
    }    
}
