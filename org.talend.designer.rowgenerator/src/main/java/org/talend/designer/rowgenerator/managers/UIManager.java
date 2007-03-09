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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.data.StringParameter;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorUiProperties;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;
import org.talend.designer.rowgenerator.ui.editor.MetadataToolbarEditorViewExt;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: UIManager.java,v 1.8 2007/01/31 10:31:05 pub Exp $
 * 
 */
public class UIManager {

    private RowGeneratorUI generatorUI;

    private int rowGenResponse = SWT.NONE;

    private RowGeneratorManager rgManager;

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
    public UIManager(RowGeneratorManager rgManager) {
        this.rgManager = rgManager;
    }

    public ExternalRowGeneratorUiProperties getUiProperties() {
        if (this.uiProperties == null) {
            this.uiProperties = new ExternalRowGeneratorUiProperties();
        }
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

    public int getRowGenResponse() {
        return this.rowGenResponse;
    }

    public void setRowGenResponse(int rowGenResponse) {
        this.rowGenResponse = rowGenResponse;
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
        setRowGenResponse(response);
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
        rgManager.getRowGeneratorComponent().setNumber(generatorUI.getDataTableView().getExtendedToolbar().getNumRows());

    }

    /**
     * qzhang Comment method "saveAllData".
     */
    private void reductAllData() {
        List<Map<String, Object>> eps = this.getRowGenManager().getOrginEP();
        this.getRowGenManager().getRowGeneratorComponent().setTableElementParameter(eps);
        this.getRowGenManager().getRowGeneratorComponent().setNumber(this.getRowGenManager().getOrginNumber());
    }

    /**
     * qzhang Comment method "saveCurrentUIProperties".
     */
    private void saveCurrentUIProperties() {
        ExternalRowGeneratorUiProperties.setWeightsMainSashForm(generatorUI.getMainSashForm().getWeights());
        ExternalRowGeneratorUiProperties.setWeightsDatasFlowViewSashForm(generatorUI.getDatasFlowViewSashForm().getWeights());
        ExternalRowGeneratorUiProperties.setShellMaximized(generatorUI.getRowGenUIParent().getShell().getMaximized());
        if (!ExternalRowGeneratorUiProperties.isShellMaximized()) {
            ExternalRowGeneratorUiProperties.setBoundsRowGen(generatorUI.getRowGenUIParent().getBounds());
        }
        ExternalRowGeneratorUiProperties.setShowColumnsList(getShowColumnsList());
    }

    /**
     * qzhang Comment method "getShowColumnsList".
     * 
     * @return
     */
    private String[] getShowColumnsList() {
        List<String> cols = new ArrayList<String>();
        MetadataToolbarEditorViewExt editorViewExt = generatorUI.getDataTableView().getExtendedToolbar();
        MenuItem[] items = editorViewExt.getColumnsListmenu().getItems();
        for (MenuItem item : items) {
            if (!item.getSelection()) {
                cols.add(item.getData().toString());
            }
        }
        return cols.toArray(new String[cols.size()]);
    }

    public RowGeneratorManager getRowGenManager() {
        return this.rgManager;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void saveOneColData(MetadataColumnExt bean) {
        String newValue2 = getOneColData(bean);
        if (rgManager.getRowGeneratorComponent() != null && newValue2 != null) {
            rgManager.getRowGeneratorComponent().setColumnValue(bean.getLabel(), newValue2);
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static String getOneColData(MetadataColumnExt bean) {
        if (bean != null && bean.getFunction() != null) {
            String newValue = "sub{"; //$NON-NLS-1$
            if (bean.getFunction().getName().equals(FunctionManager.PURE_PERL_NAME)) {
                newValue = ((StringParameter) bean.getFunction().getParameters().get(0)).getValue();
            } else {
                newValue += bean.getFunction().getName() + "("; //$NON-NLS-1$
                for (Parameter pa : (List<Parameter>) bean.getFunction().getParameters()) {
                    newValue += pa.getValue() + ","; //$NON-NLS-1$
                }
                newValue = newValue.substring(0, newValue.length() - 1);
                newValue += ")}"; //$NON-NLS-1$
                if (bean.getFunction().getName() == null || "".equals(bean.getFunction().getName())) { //$NON-NLS-1$
                    newValue = ""; //$NON-NLS-1$
                }
            }
            return newValue;
        }
        return null;
    }

    public static boolean isJavaProject() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        return (codeLanguage == ECodeLanguage.JAVA);
    }
}
