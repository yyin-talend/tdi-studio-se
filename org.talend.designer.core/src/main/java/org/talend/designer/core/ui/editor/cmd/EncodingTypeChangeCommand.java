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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * This class is used for encoding type changing command. <br/>
 * 
 * $Id: EncodingTypeChangeMetadataCommand.java 2007-2-11,02:27:04 ftang $
 * 
 */
public class EncodingTypeChangeCommand extends PropertyChangeCommand {

    @Override
    public void modifyValue(String value) {
        this.setPropertyValue(value);
        super.modifyValue(value);
    }

    public EncodingTypeChangeCommand(Element elem, String propName, Object propValue) {
        super(elem, propName, propValue);
    }

    @Override
    public void execute() {
        this.setPropertyValue(getNewValue());
        super.execute();
    }

    @Override
    public void undo() {
        this.setPropertyValue(getOldValue());
        super.undo();
    }

    @Override
    public void redo() {
        this.setPropertyValue(getNewValue());
        super.redo();

    }

    /**
     * DOC ftang Comment method "getEncodingTypeRepositoryName".
     * 
     * @param type
     * @return
     */
    private String getEncodingTypeRepositoryName(EParameterFieldType type) {
        for (IElementParameter param : (List<IElementParameter>) getElement().getElementParameters()) {
            if (param.getField() == type) {
                return (String) param.getName();
            }
        }
        return null;
    }

    /**
     * DOC ftang Comment method "setPropertyValue".
     */
    private void setPropertyValue(Object value) {
        if (value.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
            getElement().setPropertyValue(getEncodingTypeRepositoryName(EParameterFieldType.ENCODING_TYPE),
                    getElement().getPropertyValue(EParameterName.REPOSITORY_ENCODING_TYPE.getName()));
        } else {
            String tempValue = (String) value;
            tempValue = tempValue.replaceAll("'", "");
            tempValue = tempValue.replaceAll("\"", "");
            tempValue = TalendTextUtils.addQuotes(tempValue);

            getElement().setPropertyValue(getEncodingTypeRepositoryName(EParameterFieldType.ENCODING_TYPE), tempValue);
        }
    }
}
