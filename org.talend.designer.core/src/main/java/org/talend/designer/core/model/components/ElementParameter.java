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

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;

/**
 * Each parameter of the components are read and written in this class. <br/>
 * 
 * $Id$
 * 
 */
public class ElementParameter implements IElementParameter {

    private static final int NB_LINES_DEFAULT = 3;

    private String name, displayName;

    private EParameterFieldType field;

    private EComponentCategory category;

    private boolean show = true, required = false, readOnly = false;

    private Object value;

    private IElement parent;

    // used for CLOSED_LIST / TABLE
    private String[] itemsDisplayName;

    // used for CLOSED_LIST / TABLE
    private String[] itemsDisplayCodeName;

    // used for CLOSED_LIST / TABLE
    private String[] itemsShowIf;

    // used for CLOSED_LIST / TABLE
    private String[] itemsNotShowIf;

    // used for CLOSED_LIST
    private Object[] itemsValue;

    // used for CLOSED_LIST / TABLE
    private String[] itemsRepository;

    // used for CLOSED_LIST
    private Object defaultClosedListValue;

    private boolean basedOnSchema = false;

    private int nbLines = NB_LINES_DEFAULT, numRow = 0; // Default values

    private String repositoryValue;

    private boolean repositoryValueUsed = false;

    private String showIf = null;

    private String notShowIf = null;

    private List<IElementParameterDefaultValue> defaultValues = new ArrayList<IElementParameterDefaultValue>();

    public ElementParameter(final IElement parent) {
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setName(java.lang.String)
     */
    public void setName(final String s) {
        name = s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getVariableName()
     */
    public String getVariableName() {
        return "__" + name + "__"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void setCategory(final EComponentCategory cat) {
        category = cat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getCategory()
     */
    public EComponentCategory getCategory() {
        return this.category;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setDisplayName(java.lang.String)
     */
    public void setDisplayName(final String s) {
        displayName = s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setField(org.talend.core.model.designer.EParameterFieldType)
     */
    public void setField(final EParameterFieldType type) {
        field = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setValue(java.lang.Object)
     */
    public void setValue(final Object o) {
        value = o;
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getDisplayName()
     */
    public String getDisplayName() {
        return displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getField()
     */
    public EParameterFieldType getField() {
        return field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getValue()
     */
    public Object getValue() {
        return value;
    }

    public void setListItemsDisplayName(final String[] list) {
        itemsDisplayName = list;
    }

    public String[] getListItemsDisplayName() {
        return itemsDisplayName;
    }

    public void setListItemsDisplayCodeName(final String[] list) {
        itemsDisplayCodeName = list;
    }

    public String[] getListItemsDisplayCodeName() {
        return itemsDisplayCodeName;
    }

    public void setListItemsValue(final Object[] list) {
        itemsValue = list;
    }

    public Object[] getListItemsValue() {
        return itemsValue;
    }

    public void setDefaultClosedListValue(Object o) {
        defaultClosedListValue = o;
    }

    public Object getDefaultClosedListValue() {
        return defaultClosedListValue;
    }

    public void setListRepositoryItems(final String[] list) {
        itemsRepository = list;
    }

    public String[] getListRepositoryItems() {
        return itemsRepository;
    }

    public void setListItemsShowIf(String[] list) {
        itemsShowIf = list;
    }

    public String[] getListItemsShowIf() {
        return itemsShowIf;
    }

    public void setListItemsNotShowIf(String[] list) {
        itemsNotShowIf = list;
    }

    public String[] getListItemsNotShowIf() {
        return itemsNotShowIf;
    }

    public int getIndexOfItemFromList(String item) {
        int index = 0;
        boolean found = false;
        for (int i = 0; i < itemsDisplayCodeName.length && !found; i++) {
            String string = itemsDisplayCodeName[i];
            if (string.equals(item)) {
                found = true;
                index = i;
            }
        }
        for (int i = 0; i < itemsValue.length && !found; i++) {
            String string = (String) itemsValue[i];
            if (string.equals(item)) {
                found = true;
                index = i;
            }
        }
        return index;
    }

    public int getNbLines() {
        return this.nbLines;
    }

    public void setNbLines(final int nbLines) {
        this.nbLines = nbLines;
    }

    public int getNumRow() {
        return this.numRow;
    }

    public void setNumRow(final int numRow) {
        this.numRow = numRow;
    }

    public boolean isReadOnly() {
        if (parent != null) {
            return (this.readOnly || parent.isReadOnly());
        }
        return this.readOnly;
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public void setShow(final boolean show) {
        this.show = show;
    }

    public String getRepositoryValue() {
        return this.repositoryValue;
    }

    public void setRepositoryValue(String repositoryValue) {
        this.repositoryValue = repositoryValue;
    }

    public boolean isRepositoryValueUsed() {
        return this.repositoryValueUsed;
    }

    public void setRepositoryValueUsed(boolean repositoryUsed) {
        this.repositoryValueUsed = repositoryUsed;
    }

    public String getShowIf() {
        return showIf;
    }

    public void setShowIf(String showIf) {
        this.showIf = showIf;
    }

    public String getNotShowIf() {
        return notShowIf;
    }

    public void setNotShowIf(String notShowIf) {
        this.notShowIf = notShowIf;
    }

    public boolean isShow(List<? extends IElementParameter> listParam) {
        boolean showParameter = false;

        if (((showIf != null) || (notShowIf != null)) && show) {
            if (showIf != null) {
                showParameter = Expression.evaluate(showIf, listParam);
            } else {
                showParameter = !Expression.evaluate(notShowIf, listParam);
            }
        } else {
            showParameter = show;
        }
        return showParameter;
    }

    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        boolean showParameter = false;

        if (((conditionShowIf != null) || (conditionNotShowIf != null)) && show) {
            if (conditionShowIf != null) {
                showParameter = Expression.evaluate(conditionShowIf, listParam);
            } else {
                showParameter = !Expression.evaluate(conditionNotShowIf, listParam);
            }
        } else {
            showParameter = show;
        }
        return showParameter;
    }

    public List<IElementParameterDefaultValue> getDefaultValues() {
        return this.defaultValues;
    }

    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public void setValueToDefault(List<? extends IElementParameter> listParam) {
        for (IElementParameterDefaultValue defaultValue : defaultValues) {
            boolean setDefaultValue = false;
            String conditionIf = defaultValue.getIfCondition();
            String conditionNotIf = defaultValue.getNotIfCondition();

            if (((conditionIf != null) || (conditionNotIf != null)) && show) {
                if (conditionIf != null) {
                    setDefaultValue = Expression.evaluate(conditionIf, listParam);
                } else {
                    setDefaultValue = !Expression.evaluate(conditionNotIf, listParam);
                }
            }
            if (setDefaultValue) {
                setValue(defaultValue.getDefaultValue());
            }
        }
    }

    public IElement getParent() {
        return parent;
    }

    public void setParent(IElement element) {
        parent = element;
    }

    public boolean isBasedOnSchema() {
        return basedOnSchema;
    }

    public void setBasedOnSchema(boolean basedOnSchema) {
        this.basedOnSchema = basedOnSchema;
    }

    @Override
    public String toString() {
        return name + ": " + value.toString(); //$NON-NLS-1$
    }
}
