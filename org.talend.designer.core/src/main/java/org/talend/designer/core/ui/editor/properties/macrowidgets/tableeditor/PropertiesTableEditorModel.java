// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor;

import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.TableController;

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

    public void setData(Element element, IElementParameter elemParameter, Process process) {
        this.element = element;
        this.process = process;
        this.elemParameter = elemParameter;
        registerDataList((List<B>) elemParameter.getValue());
    }

    public String getTitleName() {
        return super.getName();
    }

    public B createNewEntry() {
        return (B) TableController.createNewLine(elemParameter);
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
