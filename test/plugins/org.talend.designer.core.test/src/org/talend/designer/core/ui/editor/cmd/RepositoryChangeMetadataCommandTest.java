// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import static org.junit.Assert.*;

import org.apache.avro.Schema;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess2;
import org.talend.designer.core.test.util.NodeTestCreator;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.test.utils.testproperties.TestProperties;

/**
 * created by ycbai on 2016年4月19日 Detailled comment
 *
 */
public class RepositoryChangeMetadataCommandTest extends AbstractMetadataCommandTest {

    private IProcess2 process;

    @Before
    public void before() {
        process = createFakeProcess();
    }

    @Test
    public void testComponentSchemaUpdated() {
        Node simpleInputNode = NodeTestCreator.createSimpleInputNode(process);
        TestProperties props = (TestProperties) new TestProperties("test").init(); //$NON-NLS-1$
        simpleInputNode.setComponentProperties(props);
        simpleInputNode.getMetadataList().clear();

        IMetadataTable table = createSimpleMetadata(props.schema);
        table.setTableName(simpleInputNode.getUniqueName());
        table.setLabel(simpleInputNode.getUniqueName());
        table.setAttachedConnector("FLOW"); //$NON-NLS-1$
        simpleInputNode.getMetadataList().add(table);
        simpleInputNode.addElementParameter(createUpdateParameter(simpleInputNode));
        removeParameterByType(simpleInputNode, EParameterFieldType.SCHEMA_TYPE.getName());
        IElementParameter schemaParameter = createSchemaParameter(simpleInputNode);
        simpleInputNode.addElementParameter(schemaParameter);

        IMetadataTable newTable = table.clone(true);
        newTable.getListColumns().remove(0);

        String fullParamName = SCHEMA_PARAM_NAME + ":REPOSITORY_SCHEMA_TYPE"; //$NON-NLS-1$
        String propValue = "testconnid" + " - " + newTable.getTableName(); //$NON-NLS-1$ //$NON-NLS-2$
        RepositoryChangeMetadataCommand changeMetadataCommand = new RepositoryChangeMetadataCommand(simpleInputNode,
                fullParamName, propValue, newTable, null, null);
        changeMetadataCommand.execute();

        newTable = simpleInputNode.getMetadataList().get(0);
        String avroSchemaStr = getComponentSchemaFromTable(newTable);
        assertNotNull(avroSchemaStr);
        Schema avroSchema = new Schema.Parser().parse(avroSchemaStr);
        assertEquals(2, avroSchema.getFields().size());
        assertNull(avroSchema.getField("C1")); //$NON-NLS-1$
        assertEquals(avroSchemaStr, schemaParameter.getValue());
    }

}
