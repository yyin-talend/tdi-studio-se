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
package org.talend.designer.rowgenerator.ui.editor;

import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.expressionbuilder.test.shadow.Variable;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataColumnExt.java,v 1.7 2007/02/02 07:47:00 pub Exp $
 * 
 */
public class MetadataColumnExt extends MetadataColumn {

    public final static String PARAMETER_BEGIN = "<paramater>";

    public final static String PARAMETER_END = "</paramater>";

    public final static String VARIABLE_BEGIN = "<variable>";

    public final static String VARIABLE_END = "</variable>";

    private boolean isChanged;

    /**
     * qzhang MetadataColumnExt constructor comment.
     */
    public MetadataColumnExt() {
        super();
    }

    /**
     * qzhang MetadataColumnExt constructor comment.
     */
    public MetadataColumnExt(MetadataColumn metadataColumn) {
        this.setComment(metadataColumn.getComment());
        this.setDefault(metadataColumn.getDefault());
        this.setLabel(metadataColumn.getLabel());
        this.setTalendType(metadataColumn.getTalendType());
        this.setType(metadataColumn.getType());
        this.setId(metadataColumn.getId());

        this.setLength(metadataColumn.getLength());
        this.setPrecision(metadataColumn.getPrecision());
        this.setDefault(metadataColumn.getDefault());

        this.setKey(metadataColumn.isKey());

        this.setNullable(metadataColumn.isNullable());
        this.setPattern(metadataColumn.getPattern());
    }

    private Function function;

    public Function getFunction() {

        return this.function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public String getParameter() {
        String currentPara = ""; //$NON-NLS-1$
        if (this.function != null) {
            for (Parameter para : (List<Parameter>) function.getParameters()) {
                currentPara += para.getName() + "=>" + para.getValue() + " ; "; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return currentPara;
    }

    /**
     * yzhang Comment method "getVariables".
     * 
     * @return
     */
    public String getVariables() {
        StringBuffer buffer = new StringBuffer();

        for (Parameter para : (List<Parameter>) function.getParameters()) {
            if (para.getVars() != null) {
                buffer.append(PARAMETER_BEGIN);
                buffer.append(para.getName());
                for (Variable var : para.getVars()) {
                    buffer.append(VARIABLE_BEGIN);
                    buffer.append(var.getName() + "=>");
                    buffer.append(var.getValue());
                    buffer.append(VARIABLE_END);
                }
                buffer.append(PARAMETER_END);
            }

        }
        return buffer.toString();
    }

    private String[] arrayFunctions;

    public String[] getArrayFunctions() {
        return this.arrayFunctions;
    }

    public void setArrayFunctions(String[] arrayFunctions) {
        this.arrayFunctions = arrayFunctions;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.metadata.MetadataColumn#clone()
     */
    @Override
    public IMetadataColumn clone() {
        MetadataColumnExt ext = new MetadataColumnExt((MetadataColumn) super.clone());
        if (getFunction() != null) {
            ext.setFunction((Function) getFunction().clone());
            ext.setArrayFunctions(getArrayFunctions());
        }
        return ext;
    }

    private String preview;

    /**
     * Getter for preview.
     * 
     * @return the preview
     */
    public String getPreview() {
        return this.preview;
    }

    /**
     * Sets the preview.
     * 
     * @param preview the preview to set
     */
    public void setPreview(String preview) {
        this.preview = preview;
        function.setPreview(preview);
    }

    public boolean sameMetacolumnAs2(IMetadataColumn metaColumn) {
        boolean b = super.sameMetacolumnAs(metaColumn);
        if (metaColumn instanceof MetadataColumnExt) {
            final MetadataColumnExt another = (MetadataColumnExt) metaColumn;
            if (this.function == null) {
                if (another.function != null) {
                    return false;
                }
            } else if (!this.function.sameFunctionAs(another.function)) {
                return false;
            }
        }
        return b;

    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

}
