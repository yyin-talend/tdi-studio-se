// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.managers;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.abstractmap.AbstractMapComponent;
import org.talend.designer.mapper.model.MapperSettingModel;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class MapperSettingsManager {

    public static final String DIE_ON_ERROR = "DIE_ON_ERROR"; //$NON-NLS-1$

    public static final String TEMPORARY_DATA_DIRECTORY = "TEMPORARY_DATA_DIRECTORY"; //$NON-NLS-1$

    public static final String ROWS_BUFFER_SIZE = "ROWS_BUFFER_SIZE"; //$NON-NLS-1$

    public static final String LOOKUP_IN_PARALLEL = "LOOKUP_IN_PARALLEL"; //$NON-NLS-1$

    private static MapperSettingsManager instance = null;

    private MapperSettingModel defaultModel;

    private MapperSettingModel originalModel;

    private MapperSettingModel currnentModel;

    private MapperManager manager;

    /**
     * DOC ycbai Comment method "getInstance".
     * 
     * @param manager
     * @return
     */
    public static synchronized MapperSettingsManager getInstance(MapperManager manager) {
        if (instance == null) {
            instance = new MapperSettingsManager(manager);
        }
        return instance;
    }

    /**
     * DOC ycbai Comment method "getNewInstance".
     * 
     * @param manager
     * @return
     */
    public static synchronized MapperSettingsManager getNewInstance(MapperManager manager) {
        instance = new MapperSettingsManager(manager);
        return instance;
    }

    /**
     * DOC ycbai MapperSettingsManager constructor comment.
     */
    private MapperSettingsManager(MapperManager manager) {
        this.manager = manager;
        initModelSettings();
    }

    private void initModelSettings() {
        initDefaultModel();
        initCurrnentModel();
        initOriginalModel();
    }

    private void initDefaultModel() {
        defaultModel = new MapperSettingModel();
        defaultModel.setDieOnError((Boolean) manager.getDefaultSetting().get(DIE_ON_ERROR));
        defaultModel.setLookInParallel((Boolean) manager.getDefaultSetting().get(LOOKUP_IN_PARALLEL));
        defaultModel.setTempDataDir(String.valueOf(manager.getDefaultSetting().get(TEMPORARY_DATA_DIRECTORY)));
        defaultModel.setRowBufferSize(String.valueOf(manager.getDefaultSetting().get(ROWS_BUFFER_SIZE)));
    }

    private void initCurrnentModel() {
        currnentModel = new MapperSettingModel();
        AbstractMapComponent component = manager.getAbstractMapComponent();
        IElementParameter parameter = component.getElementParameter(DIE_ON_ERROR);
        if (parameter != null && parameter.getValue() != null && parameter.getValue() instanceof Boolean) {
            currnentModel.setDieOnError((Boolean) parameter.getValue());
        }
        parameter = component.getElementParameter(TEMPORARY_DATA_DIRECTORY);
        if (parameter != null && parameter.getValue() != null) {
            currnentModel.setTempDataDir(String.valueOf(parameter.getValue()));
        }
        parameter = component.getElementParameter(ROWS_BUFFER_SIZE);
        if (parameter != null && parameter.getValue() != null) {
            currnentModel.setRowBufferSize(String.valueOf(parameter.getValue()));
        }
        // TODO: temporary set be true, handle after...
        boolean parallel = false;
        IElementParameter paraEle = manager.getAbstractMapComponent().getElementParameter("LKUP_PARALLELIZE");
        if (paraEle != null) {
            parallel = (Boolean) paraEle.getValue();
        }
        currnentModel.setLookInParallel(parallel);
    }

    private void initOriginalModel() {
        if (originalModel == null) {
            try {
                originalModel = currnentModel.clone();
            } catch (CloneNotSupportedException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public boolean isMapperSettingChanged() {
        if (originalModel != null) {
            return !originalModel.equals(currnentModel);
        } else if (currnentModel != null) {
            return !currnentModel.equals(originalModel);
        } else {
            return true;
        }
    }

    public int getChangeNumOfSettings() {
        return currnentModel.hasDifferNumWith(defaultModel);
    }

    public void saveCurrentSettings() {
        AbstractMapComponent component = manager.getAbstractMapComponent();
        IElementParameter parameter = component.getElementParameter(DIE_ON_ERROR);
        if (parameter != null) {
            parameter.setValue(currnentModel.isDieOnError());
        }
        parameter = component.getElementParameter(TEMPORARY_DATA_DIRECTORY);
        if (parameter != null) {
            parameter.setValue(currnentModel.getTempDataDir());
        }
        parameter = component.getElementParameter(ROWS_BUFFER_SIZE);
        if (parameter != null) {
            parameter.setValue(currnentModel.getRowBufferSize());
        }
    }

    /**
     * Getter for originalModel.
     * 
     * @return the originalModel
     */
    public MapperSettingModel getOriginalModel() {
        return this.originalModel;
    }

    /**
     * Getter for currnentModel.
     * 
     * @return the currnentModel
     */
    public MapperSettingModel getCurrnentModel() {
        return this.currnentModel;
    }

    /**
     * Getter for defaultModel.
     * 
     * @return the defaultModel
     */
    public MapperSettingModel getDefaultModel() {
        return this.defaultModel;
    }

}
