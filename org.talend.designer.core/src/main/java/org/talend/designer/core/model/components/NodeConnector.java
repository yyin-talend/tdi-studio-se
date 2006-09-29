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

import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;

/**
 * Defines connector type and name for each component. <br/>
 * 
 * $Id$
 * 
 */
public class NodeConnector implements INodeConnector {

    private EConnectionType connectionType;

    private int maxLinkOutput = -1;

    private int minLinkOutput = 0;

    private int maxLinkInput = -1;

    private int minLinkInput = 0;

    private int curLinkNbOutput = 0;

    private int curLinkNbInput = 0;

    private boolean builtIn = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeConnector#getConnectionType()
     */
    public EConnectionType getConnectionType() {
        return this.connectionType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeConnector#setConnectionType(org.talend.core.model.designer.EConnectionType)
     */
    public void setConnectionType(final EConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeConnector#isBuiltIn()
     */
    public boolean isBuiltIn() {
        return this.builtIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.INodeConnector#setBuiltIn(boolean)
     */
    public void setBuiltIn(final boolean builtIn) {
        this.builtIn = builtIn;
    }

    public int getCurLinkNbInput() {
        return this.curLinkNbInput;
    }

    public void setCurLinkNbInput(int curLinkNbInput) {
        this.curLinkNbInput = curLinkNbInput;
    }

    public int getCurLinkNbOutput() {
        return this.curLinkNbOutput;
    }

    public void setCurLinkNbOutput(int curLinkNbOutput) {
        this.curLinkNbOutput = curLinkNbOutput;
    }

    public int getMaxLinkInput() {
        return this.maxLinkInput;
    }

    public void setMaxLinkInput(int maxLinkInput) {
        this.maxLinkInput = maxLinkInput;
    }

    public int getMaxLinkOutput() {
        return this.maxLinkOutput;
    }

    public void setMaxLinkOutput(int maxLinkOutput) {
        this.maxLinkOutput = maxLinkOutput;
    }

    public int getMinLinkInput() {
        return this.minLinkInput;
    }

    public void setMinLinkInput(int minLinkInput) {
        this.minLinkInput = minLinkInput;
    }

    public int getMinLinkOutput() {
        return this.minLinkOutput;
    }

    public void setMinLinkOutput(int minLinkOutput) {
        this.minLinkOutput = minLinkOutput;
    }
}
