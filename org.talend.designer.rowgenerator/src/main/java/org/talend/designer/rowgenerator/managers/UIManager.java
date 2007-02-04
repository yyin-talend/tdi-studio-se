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
package org.talend.designer.rowgenerator.managers;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorUiProperties;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;

/**
 * amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: UIManager.java,v 1.8 2007/01/31 10:31:05 pub Exp $
 * 
 */
public class UIManager {

    private RowGeneratorUI generatorUI;

    private int mapperResponse = SWT.NONE;

    private RowGeneratorManager mapperManager;

    private boolean shiftPressed;

    private boolean ctrlPressed;

    private ExternalRowGeneratorUiProperties uiProperties;

    int currentDragDetail;

    /**
     * amaumont UIManager constructor comment.
     * 
     * @param tableManager
     * @param manager
     */
    public UIManager(RowGeneratorManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public ExternalRowGeneratorUiProperties getUiProperties() {
        return this.uiProperties;
    }

    public void setUiProperties(ExternalRowGeneratorUiProperties uiProperties) {
        this.uiProperties = uiProperties;
    }

    public RowGeneratorUI getGeneratorUI() {
        return this.generatorUI;
    }

    public void setGeneratorUI(RowGeneratorUI generatorUI) {
        this.generatorUI = generatorUI;
    }

    public int getMapperResponse() {
        return this.mapperResponse;
    }

    public void setMapperResponse(int mapperResponse) {
        this.mapperResponse = mapperResponse;
    }

    public boolean isCtrlPressed() {
        return this.ctrlPressed;
    }

    public void setCtrlPressed(boolean ctrlPressed) {
        this.ctrlPressed = ctrlPressed;
    }

    public boolean isShiftPressed() {
        return this.shiftPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    /**
     * qzhang Comment method "closeMapper".
     * 
     * @param response
     */
    public void closeRowGenerator(int response) {
        setMapperResponse(response);
        Composite parent = generatorUI.getRowGenUIParent();
        saveCurrentUIProperties();
        if (response == SWT.CANCEL) {
            reductAllData();
        }
        if (response == SWT.OK) {
            saveAllData();
        }
        if (parent instanceof Shell) {
            ((Shell) parent).close();
        }
    }

    /**
     * qzhang Comment method "saveAllData".
     */
    private void saveAllData() {
        TableItem[] items = generatorUI.getDataTableView().getTable().getItems();
        for (int i = 0; i < items.length; i++) {
            saveOneColData((MetadataColumnExt) items[i].getData());
        }
    }

    /**
     * qzhang Comment method "saveAllData".
     */
    private void reductAllData() {
        List<Map<String, Object>> eps = this.getMapperManager().getOrginEP();
        this.getMapperManager().getRowGeneratorComponent().setTableElementParameter(eps);
        this.getMapperManager().getRowGeneratorComponent().setNumber(this.getMapperManager().getOrginNumber());
    }

    /**
     * qzhang Comment method "saveCurrentUIProperties".
     */
    private void saveCurrentUIProperties() {
        uiProperties = new ExternalRowGeneratorUiProperties();
    }

    public RowGeneratorManager getMapperManager() {
        return this.mapperManager;
    }

    @SuppressWarnings("unchecked")
    protected void saveOneColData(MetadataColumnExt bean) {
        if (bean != null && bean.getFunction() != null && mapperManager.getRowGeneratorComponent() != null) {
            String newValue = "sub{";
            newValue += bean.getFunction().getName() + "(";
            for (Parameter pa : (List<Parameter>) bean.getFunction().getParameters()) {
                newValue += pa.getValue() + ",";
            }
            newValue = newValue.substring(0, newValue.length() - 1);
            newValue += ")}";
            if (bean.getFunction().getName() == null || "".equals(bean.getFunction().getName())) {
                newValue = "";
            }
            mapperManager.getRowGeneratorComponent().setColumnValue(bean.getLabel(), newValue);
        }

    }

}
