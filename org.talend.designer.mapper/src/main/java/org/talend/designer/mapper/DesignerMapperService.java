// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.utils.MapperHelper;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class DesignerMapperService implements IDesignerMapperService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.IDesignerMapperSerivce#isVirtualComponent(org.talend.core.model.process.INode)
     */
    public boolean isVirtualComponent(INode node) {
        return MapperHelper.isGeneratedAsVirtualComponent(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.IDesignerMapperService#renameJoinTable(java.lang.Process,
     * org.talend.core.model.process.IExternalData, java.util.List)
     */
    public void renameJoinTable(IProcess process, IExternalData data, List<String> createdNames) {
        if (data instanceof ExternalMapperData) {
            ExternalMapperData extenalData = (ExternalMapperData) data;
            final List<ExternalMapperTable> outputTables = extenalData.getOutputTables();
            if (outputTables != null) {
                for (ExternalMapperTable table : outputTables) {
                    if (table.getIsJoinTableOf() != null && !"".equals(table.getIsJoinTableOf())) {
                        final String newName = createNewConnectionName(process, table.getName(), createdNames);
                        table.setName(newName);
                    }
                }
            }
        }

    }

    private String createNewConnectionName(IProcess process, String oldName, List<String> createdNames) {
        String newName = null;

        if (process.checkValidConnectionName(oldName, true)) {
            newName = oldName;
        } else {
            newName = checkExistingNames(process, "copyOf" + oldName); //$NON-NLS-1$
        }
        newName = checkNewNames(process, newName, createdNames);

        createdNames.add(newName);
        return newName;
    }

    private String checkExistingNames(IProcess process, final String oldName) {
        String tmpName = oldName + "_"; //$NON-NLS-1$
        String newName = oldName;

        int index = 0;
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
        }
        return newName;
    }

    private String checkNewNames(IProcess process, final String oldName, List<String> createdNames) {
        String tmpName = oldName + "_"; //$NON-NLS-1$

        String newName = oldName;
        int index = 0;
        while (createdNames.contains(newName)) {
            newName = tmpName + index++;
        }
        // check the name again in process.
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
        }
        return newName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.mapper.IDesignerMapperService#getJoinTableNames(org.talend.core.model.process.IExternalData)
     */
    public List<String> getJoinTableNames(IExternalData data) {
        List<String> namesList = new ArrayList<String>();
        if (data instanceof ExternalMapperData) {
            ExternalMapperData extenalData = (ExternalMapperData) data;
            final List<ExternalMapperTable> outputTables = extenalData.getOutputTables();
            if (outputTables != null) {
                for (ExternalMapperTable table : outputTables) {
                    if (table.getIsJoinTableOf() != null && !"".equals(table.getIsJoinTableOf())) {
                        namesList.add(table.getName());
                    }
                }
            }
        }

        return namesList;
    }

}
