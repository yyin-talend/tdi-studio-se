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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.temp.ECodeLanguage;

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

    // 5 maps used for the combo box element (CLOSED_LIST)
    private Map<ECodeLanguage, String[]> mapItemsDisplayName = new HashMap<ECodeLanguage, String[]>();

    private Map<ECodeLanguage, String[]> mapItemsDisplayCodeName = new HashMap<ECodeLanguage, String[]>();

    private Map<ECodeLanguage, String[]> mapItemsShowIf = new HashMap<ECodeLanguage, String[]>();

    private Map<ECodeLanguage, String[]> mapItemsNotShowIf = new HashMap<ECodeLanguage, String[]>();

    private Map<ECodeLanguage, Object[]> mapItemsValue = new HashMap<ECodeLanguage, Object[]>();

    private Map<ECodeLanguage, String[]> mapRepositoryItems = new HashMap<ECodeLanguage, String[]>();

    private Map<ECodeLanguage, Object> mapItemValue = new HashMap<ECodeLanguage, Object>();

    private int nbLines = NB_LINES_DEFAULT, numRow = 0; // Default values

    private String repositoryValue;

    private boolean repositoryValueUsed = false;

    private String showIf = null;

    private String notShowIf = null;

    private List<IElementParameterDefaultValue> defaultValues = new ArrayList<IElementParameterDefaultValue>();

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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setListItemsValue(java.lang.String[])
     */
    public void setListItemsDisplayName(final String[] list) {
        for (ECodeLanguage language : ECodeLanguage.values()) {
            mapItemsDisplayName.put(language, list);
        }
    }

    public void setListItemsDisplayName(ECodeLanguage language, final String[] list) {
        mapItemsDisplayName.put(language, list);
    }

    public void setListItemsDisplayCodeName(final String[] list) {
        for (ECodeLanguage language : ECodeLanguage.values()) {
            mapItemsDisplayCodeName.put(language, list);
        }
    }

    public void setListItemsDisplayCodeName(ECodeLanguage language, final String[] list) {
        mapItemsDisplayCodeName.put(language, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setListItemsID(java.lang.String[])
     */
    public void setListItemsValue(final Object[] list) {
        for (ECodeLanguage language : ECodeLanguage.values()) {
            mapItemsValue.put(language, list);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setListItemsID(java.lang.String[])
     */
    public void setListItemsValue(ECodeLanguage language, final Object[] list) {
        mapItemsValue.put(language, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getName()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getListItemsValue()
     */
    public String[] getListItemsDisplayName(ECodeLanguage language) {
        return mapItemsDisplayName.get(language);
    }

    public String[] getListItemsDisplayCodeName(ECodeLanguage language) {
        return mapItemsDisplayCodeName.get(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getListItemsID()
     */
    public Object[] getListItemsValue(ECodeLanguage language) {
        return mapItemsValue.get(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getNbLines()
     */
    public int getNbLines() {
        return this.nbLines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setNbLines(int)
     */
    public void setNbLines(final int nbLines) {
        this.nbLines = nbLines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#getNumRow()
     */
    public int getNumRow() {
        return this.numRow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setNumRow(int)
     */
    public void setNumRow(final int numRow) {
        this.numRow = numRow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#isReadOnly()
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setReadOnly(boolean)
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#isRequired()
     */
    public boolean isRequired() {
        return this.required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setRequired(boolean)
     */
    public void setRequired(final boolean required) {
        this.required = required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IDesignerElementParameter#setShow(boolean)
     */
    public void setShow(final boolean show) {
        this.show = show;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getValue(org.talend.core.model.temp.ECodeLanguage)
     */
    public Object getDefaultClosedListValue(ECodeLanguage language) {
        return mapItemValue.get(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValue(org.talend.core.model.temp.ECodeLanguage,
     * java.lang.Object)
     */
    public void setDefaultClosedListValue(ECodeLanguage language, Object o) {
        mapItemValue.put(language, o);
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

    public String[] getListRepositoryItems(ECodeLanguage language) {
        return mapRepositoryItems.get(language);
    }

    public void setListRepositoryItems(ECodeLanguage language, final String[] list) {
        mapRepositoryItems.put(language, list);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsShowIf(org.talend.core.model.temp.ECodeLanguage)
     */
    public String[] getListItemsShowIf(ECodeLanguage language) {
        return mapItemsShowIf.get(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsShowIf(java.lang.String[])
     */
    public void setListItemsShowIf(String[] list) {
        for (ECodeLanguage language : ECodeLanguage.values()) {
            mapItemsShowIf.put(language, list);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsShowIf(org.talend.core.model.temp.ECodeLanguage,
     * java.lang.String[])
     */
    public void setListItemsShowIf(ECodeLanguage language, String[] list) {
        mapItemsShowIf.put(language, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsNotShowIf(org.talend.core.model.temp.ECodeLanguage)
     */
    public String[] getListItemsNotShowIf(ECodeLanguage language) {
        return mapItemsNotShowIf.get(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotShowIf(java.lang.String[])
     */
    public void setListItemsNotShowIf(String[] list) {
        for (ECodeLanguage language : ECodeLanguage.values()) {
            mapItemsNotShowIf.put(language, list);
        }
    }

    public void setListItemsNotShowIf(ECodeLanguage language, String[] list) {
        mapItemsNotShowIf.put(language, list);
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
}
