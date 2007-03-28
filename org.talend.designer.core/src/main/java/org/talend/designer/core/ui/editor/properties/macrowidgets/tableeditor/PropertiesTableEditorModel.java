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
package org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataTableEditor.java 801 2006-11-30 16:28:36Z amaumont $
 * 
 * @param <B>
 */
public class PropertiesTableEditorModel<B> extends ExtendedTableModel<B> {

    private Element element;

    private IElementParameter elemParameter;

    private Process process;

    private List<String> columnList;

    private List<String> prevColumnList;

    private String[] columnArrayList;

    private String[] prevColumnArrayList;

    private boolean dynamicData;

    /**
     * DOC amaumont PropertiesTableEditorModel constructor comment.
     */
    public PropertiesTableEditorModel() {
        super();
    }

    public PropertiesTableEditorModel(String titleName) {
        super(titleName);
    }

    public void setData(Element element, IElementParameter elemParameter, Process process, List<String> columnList,
            List<String> prevColumnList) {
        this.element = element;
        this.process = process;
        this.elemParameter = elemParameter;
        this.columnList = columnList;
        this.prevColumnList = prevColumnList;
        columnArrayList = columnList.toArray(new String[0]);
        prevColumnArrayList = prevColumnList.toArray(new String[0]);

        Object[] itemsValue = getItemsValue();

        if (elemParameter.isBasedOnSchema()) {
            dynamicData = true;
        }

        for (int i = 0; i < itemsValue.length; i++) {
            if (itemsValue[i] instanceof IElementParameter) {
                IElementParameter tmpParam = (IElementParameter) itemsValue[i];
                if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST) {
                    dynamicData = true;
                    tmpParam.setListItemsDisplayCodeName(columnArrayList);
                    tmpParam.setListItemsDisplayName(columnArrayList);
                    tmpParam.setListItemsValue(columnArrayList);
                    if (columnArrayList.length != 0) {
                        tmpParam.setDefaultClosedListValue(columnArrayList[0]);
                    } else {
                        tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                    }
                }
                if (tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                    dynamicData = true;
                    tmpParam.setListItemsDisplayCodeName(prevColumnArrayList);
                    tmpParam.setListItemsDisplayName(prevColumnArrayList);
                    tmpParam.setListItemsValue(prevColumnArrayList);
                    if (prevColumnArrayList.length != 0) {
                        tmpParam.setDefaultClosedListValue(prevColumnArrayList[0]);
                    } else {
                        tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                    }
                }
            }
        }

        registerDataList((List<B>) elemParameter.getValue());
    }

    public String getTitleName() {
        return super.getName();
    }

    public B createNewEntry() {
        IElementParameter param = elemParameter;
        Map<String, Object> line = new HashMap<String, Object>();
        String[] items = (String[]) param.getListItemsDisplayCodeName();
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        IElementParameter tmpParam;

        tmpParam = (IElementParameter) itemsValue[0];
        switch (tmpParam.getField()) {
        case CLOSED_LIST:
        case COLUMN_LIST:
        case PREV_COLUMN_LIST:
            line.put(items[0], null);
            break;
        case CHECK:
            line.put(items[0], tmpParam.getValue());
            break;
        default: // TEXT
            if ((tmpParam.getValue() == null) || (tmpParam.getValue().equals(""))) { //$NON-NLS-1$
                line.put(items[0], new String("'newLine'")); //$NON-NLS-1$
            } else {
                line.put(items[0], tmpParam.getValue());
            }
        }

        for (int i = 1; i < items.length; i++) {
            tmpParam = (IElementParameter) itemsValue[i];
            switch (tmpParam.getField()) {
            case CLOSED_LIST:
            case COLUMN_LIST:
            case PREV_COLUMN_LIST:
                line.put(items[i], new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam.getDefaultClosedListValue())));
                break;
            default: // TEXT or CHECK (means String or Boolean)
                line.put(items[i], tmpParam.getValue());
            }
        }

        // Map<String, Object> line = new HashMap<String, Object>();
        // String[] items = (String[]) elemParameter.getListItemsDisplayCodeName();
        // Object[] itemsValue = (Object[]) elemParameter.getListItemsValue();
        //
        // if (itemsValue[0] instanceof IElementParameter) {
        // IElementParameter tmpParam = (IElementParameter) itemsValue[0];
        // line.put(items[0], new Integer(tmpParam.getIndexOfItemFromList((String)
        // tmpParam.getDefaultClosedListValue())));
        // } else {
        // line.put(items[0], new String("'newLine'"));
        // }
        // for (int i = 1; i < items.length; i++) {
        // if (itemsValue[i] instanceof IElementParameter) {
        // IElementParameter tmpParam = (IElementParameter) itemsValue[i];
        // line.put(items[i], new Integer(tmpParam.getIndexOfItemFromList((String)
        // tmpParam.getDefaultClosedListValue())));
        // } else {
        // line.put(items[i], new String(""));
        // }
        // }
        // return new ElemParamValueWrapper(line);
        return (B) line;
    }

    /**
     * Getter for columnArrayList.
     * 
     * @return the columnArrayList
     */
    public String[] getColumnArrayList() {
        return this.columnArrayList;
    }

    /**
     * Getter for columnList.
     * 
     * @return the columnList
     */
    public List<String> getColumnList() {
        return this.columnList;
    }

    /**
     * Getter for dynamicData.
     * 
     * @return the dynamicData
     */
    public boolean isDynamicData() {
        return dynamicData;
    }

    /**
     * Getter for element.
     * 
     * @return the element
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * Getter for elemParameter.
     * 
     * @return the elemParameter
     */
    public IElementParameter getElemParameter() {
        return this.elemParameter;
    }

    /**
     * Getter for items.
     * 
     * @return the items
     */
    public String[] getItems() {
        return (String[]) elemParameter.getListItemsDisplayCodeName();
    }

    /**
     * Getter for itemsNotShowIf.
     * 
     * @return the itemsNotShowIf
     */
    public String[] getItemsNotShowIf() {
        return (String[]) elemParameter.getListItemsNotShowIf();
    }

    /**
     * Getter for itemsShowIf.
     * 
     * @return the itemsShowIf
     */
    public String[] getItemsShowIf() {
        return (String[]) elemParameter.getListItemsShowIf();
    }

    /**
     * Getter for itemsValue.
     * 
     * @return the itemsValue
     */
    public Object[] getItemsValue() {
        return (Object[]) elemParameter.getListItemsValue();
    }

    /**
     * Getter for prevColumnArrayList.
     * 
     * @return the prevColumnArrayList
     */
    public String[] getPrevColumnArrayList() {
        return this.prevColumnArrayList;
    }

    /**
     * Getter for prevColumnList.
     * 
     * @return the prevColumnList
     */
    public List<String> getPrevColumnList() {
        return this.prevColumnList;
    }

    /**
     * Getter for process.
     * 
     * @return the process
     */
    public Process getProcess() {
        return this.process;
    }

    /**
     * Getter for titles.
     * 
     * @return the titles
     */
    public String[] getTitles() {
        return elemParameter.getListItemsDisplayName();
    }

}
