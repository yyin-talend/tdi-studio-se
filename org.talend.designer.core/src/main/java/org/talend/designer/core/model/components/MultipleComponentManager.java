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

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.components.IMultipleComponentParameter;
import org.talend.core.model.process.EConnectionType;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MultipleComponentManager implements IMultipleComponentManager {

    IMultipleComponentItem input;

    IMultipleComponentItem output;

    String inputName;

    String outputName;

    List<IMultipleComponentItem> itemList = new ArrayList<IMultipleComponentItem>();
    
    List<IMultipleComponentParameter> paramList = new ArrayList<IMultipleComponentParameter>();

    public MultipleComponentManager(String inputName, String outputName) {
        this.inputName = inputName;
        this.outputName = outputName;
    }

    public void addItem(String name, String connection, String linkTo) {
        MultipleComponentItem currentItem = new MultipleComponentItem();

        if (name.equals(inputName)) {
            input = currentItem;
        }
        if (name.equals(outputName)) {
            output = currentItem;
        }

        currentItem.setName(name);
        currentItem.setNameLinkTo(linkTo);
        if (connection != null) {
            currentItem.setConnectionType(EConnectionType.getTypeFromName(connection));
            currentItem.setConnectionExist(true);
        } else {
            currentItem.setConnectionExist(false);
        }
        itemList.add(currentItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IMultipleComponentManager#addParam(java.lang.String, java.lang.String)
     */
    public void addParam(String source, String target) {
        paramList.add(new MultipleComponentParameter(source, target));
    }

    public void validateItems() {
        for (IMultipleComponentItem mainItem : itemList) {
            String nameLinkedTo = mainItem.getNameLinkTo();
            if (nameLinkedTo != null) {
                boolean found = false;
                for (int i = 0; i < itemList.size() && !found; i++) {
                    IMultipleComponentItem linkedItem = itemList.get(i);
                    if (linkedItem.getName().equals(nameLinkedTo)) {
                        mainItem.setLinkTo(linkedItem);
                        linkedItem.setLinkFrom(mainItem);
                        found = true;
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentManager#getInput()
     */
    public IMultipleComponentItem getInput() {
        return this.input;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentManager#getOutput()
     */
    public IMultipleComponentItem getOutput() {
        return this.output;
    }

    public List<IMultipleComponentItem> getItemList() {
        return this.itemList;
    }
    
    public List<IMultipleComponentParameter> getParamList() {
        return this.paramList;
    }
}
