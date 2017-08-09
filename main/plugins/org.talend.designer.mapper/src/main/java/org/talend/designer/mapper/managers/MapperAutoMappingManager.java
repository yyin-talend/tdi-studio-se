// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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


/**
 * DOC xwen  class global comment. Detailled comment
 */
public class MapperAutoMappingManager {

    private static MapperAutoMappingManager instance = null;

    private MapperManager manager;

    /**
     * DOC xwen MapperAutoMappingManager constructor comment.
     * 
     * @param manager
     */
    public MapperAutoMappingManager(MapperManager manager) {
        this.manager = manager;
        initModelSettings();
    }


    public static synchronized MapperAutoMappingManager getInstance(MapperManager manager) {
        if (instance == null) {
            instance = new MapperAutoMappingManager(manager);
        }
        return instance;
    }

    /**
     * DOC xwen Comment method "initModelSettings".
     */
    private void initModelSettings() {

    }

}
