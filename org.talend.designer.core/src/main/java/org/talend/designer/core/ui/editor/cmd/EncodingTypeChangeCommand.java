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
package org.talend.designer.core.ui.editor.cmd;

import org.apache.commons.lang.ArrayUtils;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * This class is used for encoding type changing command. <br/>
 * 
 * $Id: EncodingTypeChangeMetadataCommand.java 2007-2-11,02:27:04 ftang $
 * 
 */
public class EncodingTypeChangeCommand extends PropertyChangeCommand {

    private String oldType;

    private String newRealValue;

    private IElementParameter paramEncoding;

    private boolean fromCombo;

    public EncodingTypeChangeCommand(Element elem, String propName, Object propValue, boolean fromCombo) {
        super(elem, propName, EmfComponent.ENCODING_TYPE_CUSTOM.equals(propValue) ? elem.getPropertyValue(propName) : propValue);
        newRealValue = (String) propValue;
        IElementParameter curParam = getElement().getElementParameter(getPropName());
        paramEncoding = curParam.getChildParameters().get(EParameterName.ENCODING_TYPE.getName());
        this.fromCombo = fromCombo;
    }

    @Override
    public void execute() {
        oldType = (String) paramEncoding.getValue();

        String tempValue = newRealValue;
        tempValue = tempValue.replaceAll("'", "");
        tempValue = tempValue.replaceAll("\"", "");

        boolean newIsCustomAndoldIsNotCustom = tempValue.equals(EmfComponent.ENCODING_TYPE_CUSTOM)
                && !paramEncoding.getValue().equals(EmfComponent.ENCODING_TYPE_CUSTOM);
        boolean newIsNotCustomAndoldIsCustom = !tempValue.equals(EmfComponent.ENCODING_TYPE_CUSTOM)
                && paramEncoding.getValue().equals(EmfComponent.ENCODING_TYPE_CUSTOM);
        boolean toRefresh = false;
        if (fromCombo && (newIsCustomAndoldIsNotCustom || newIsNotCustomAndoldIsCustom)) {
            toRefresh = true;
        }
        if (ArrayUtils.contains(paramEncoding.getListItemsValue(), tempValue)) {
            paramEncoding.setValue(tempValue);
        }
        if (toRefresh) {
            getElement().setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), Boolean.TRUE);
            if (DynamicTabbedPropertySection.getLastPropertyUsed() != null) {
                DynamicTabbedPropertySection.getLastPropertyUsed().refresh();
            }
        }
        super.execute();

    }

    @Override
    public void undo() {
        if (!oldType.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
            paramEncoding.setValue(oldType);
            paramEncoding.setShow(false);
        }
        super.undo();
    }
}
