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
package org.talend.expressionbuilder.test.shadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.RowGeneratorPlugin;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManagerExt;
import org.talend.designer.rowgenerator.shadow.TextElementParameter;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;
import org.talend.designer.runprocess.shadow.ObjectElementParameter;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;
import org.talend.expressionbuilder.ui.ExpressionComposite;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: VirtualRowGeneratorNode.java 下午05:26:21 2007-7-19 +0000 (2007-7-19) yzhang $
 * 
 */
public class VirtualRowGeneratorNode extends RowGeneratorComponent {

    private static final String ROW_GENERATOR = "tRowGenerator"; //$NON-NLS-1$

    private List<IElementParameter> parameters;

    private List<IMetadataTable> metadatas;

    /**
     * yzhang VirtualRowGeneratorNode constructor comment.
     */
    public VirtualRowGeneratorNode(Function function) {

        IComponentsFactory compFac = RowGeneratorPlugin.getDefault().getRepositoryService().getComponentsFactory();
        setComponent(compFac.get(ROW_GENERATOR));

        IMetadataTable m1 = new MetadataTable();
        m1.setTableName("tRowGenerator_1"); //$NON-NLS-1$
        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        VirtualMetadataColumn column = new VirtualMetadataColumn();
        column.setFunction(function);
        listColumns.add(column);
        m1.setListColumns(listColumns);

        metadatas = new ArrayList<IMetadataTable>();
        metadatas.add(m1);
        setMetadataList(metadatas);

        TextElementParameter p1 = new TextElementParameter("NB_ROWS", "1"); //$NON-NLS-1$ //$NON-NLS-2$
        ObjectElementParameter p2 = new ObjectElementParameter("VALUES", getArray()); //$NON-NLS-1$
        p2.setListItemsDisplayCodeName(new String[] { "SCHEMA_COLUMN", "ARRAY" }); //$NON-NLS-1$ //$NON-NLS-2$

        parameters = new ArrayList<IElementParameter>();
        parameters.add(p1);
        parameters.add(p2);

        setElementParameters(parameters);

        setUniqueName(ROW_GENERATOR + "_1"); //$NON-NLS-1$

    }

    /**
     * yzhang Comment method "initArray".
     */
    private Object getArray() {
        List<Map<String, String>> map = new ArrayList<Map<String, String>>();
        MetadataTable table = (MetadataTable) this.getMetadataList().get(0);
        for (IMetadataColumn col : table.getListColumns()) {
            VirtualMetadataColumn ext = (VirtualMetadataColumn) col;
            Map<String, String> value = new HashMap<String, String>();
            value.put(RowGeneratorComponent.COLUMN_NAME, ext.getLabel());
            List<Variable> variables = ExpressionBuilderDialog.getTestComposite().getVariableList();
            String expression = ExpressionBuilderDialog.getExpressionComposite().getExpression();
            for (Variable var : variables) {
                expression = expression.replaceAll(var.getName(), "\"" + var.getValue() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            value.put(RowGeneratorComponent.ARRAY, expression + "+\"\""); //$NON-NLS-1$
            map.add(value);
        }

        return map;

    }
}
