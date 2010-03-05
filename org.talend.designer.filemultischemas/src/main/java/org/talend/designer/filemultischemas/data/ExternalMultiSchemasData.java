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
package org.talend.designer.filemultischemas.data;

import java.util.List;
import java.util.Map;

import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.node.IExternalMapEntry;
import org.talend.core.model.process.node.IExternalMapTable;

/**
 * cLi class global comment. Detailled comment
 */
public class ExternalMultiSchemasData implements IExternalData {

    private static final long serialVersionUID = 4723596595913156591L;

    public IExternalData clone() throws CloneNotSupportedException {
        return (IExternalData) super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalData#getExpressionColumns(java.lang.String,
     * org.talend.core.model.process.IExternalData.ExternalDataType[])
     */
    public Map<IExternalMapTable, List<IExternalMapEntry>> getExpressionColumns(String expression, ExternalDataType... types) {
        return null;
    }

}
