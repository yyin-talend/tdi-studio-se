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

import java.util.ArrayList;
import java.util.List;

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
 * created by ycbai on 2016年4月18日 Detailled comment
 *
 */
public class ChangeMetadataCommandTest extends AbstractMetadataCommandTest {

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

        IMetadataTable newTable = table.clone(true);
        newTable.getListColumns().remove(0);

        IElementParameter schemaParam = createSchemaParameter(simpleInputNode);

        ChangeMetadataCommand changeMetadataCommand = new ChangeMetadataCommand(simpleInputNode, schemaParam, null, null, null,
                table, newTable);
        changeMetadataCommand.execute();

        String avroSchemaStr = getComponentSchemaFromTable(table);
        assertNotNull(avroSchemaStr);
        Schema avroSchema = new Schema.Parser().parse(avroSchemaStr);
        assertEquals(2, avroSchema.getFields().size());
        assertNull(avroSchema.getField("C1")); //$NON-NLS-1$
        assertEquals(avroSchemaStr, schemaParam.getValue());
    }

    @Test
    public void testComponentSchemaPropagated() {
        Node simpleInputNode = NodeTestCreator.createSimpleInputNode(process);
        TestProperties inputProps = (TestProperties) new TestProperties("testInput").init(); //$NON-NLS-1$
        simpleInputNode.setComponentProperties(inputProps);
        simpleInputNode.getMetadataList().clear();

        IMetadataTable table = createSimpleMetadata(inputProps.schema);
        table.setTableName(simpleInputNode.getUniqueName());
        table.setLabel(simpleInputNode.getUniqueName());
        table.setAttachedConnector("FLOW"); //$NON-NLS-1$
        simpleInputNode.getMetadataList().add(table);

        List<Object> args = new ArrayList<>();
        args.add(simpleInputNode.getUniqueName());
        args.add("connectionName"); //$NON-NLS-1$
        args.add(null); // set null, the command should take the schema from the component directly
        ConnectionCreateCommand ccc = new ConnectionCreateCommand(simpleInputNode, "FLOW", args); //$NON-NLS-1$
        ConnectionCreateCommand.setCreatingConnection(true);

        Node simpleOutputNode = NodeTestCreator.createSimpleOutputNode(process);
        removeParameterByType(simpleOutputNode, EParameterFieldType.SCHEMA_TYPE.getName());
        IElementParameter schemaParameter = createSchemaParameter(simpleOutputNode);
        simpleOutputNode.addElementParameter(schemaParameter);
        TestProperties outputProps = (TestProperties) new TestProperties("testOutput").init(); //$NON-NLS-1$
        simpleOutputNode.setComponentProperties(outputProps);
        ccc.setTarget(simpleOutputNode);
        ccc.execute();

        table = simpleOutputNode.getMetadataList().get(0);
        String avroSchemaStr = getComponentSchemaFromTable(table);
        assertNotNull(avroSchemaStr);
        Schema avroSchema = new Schema.Parser().parse(avroSchemaStr);
        assertEquals(3, avroSchema.getFields().size());
        assertNotNull(avroSchema.getField("C1")); //$NON-NLS-1$
        assertNotNull(avroSchema.getField("C2")); //$NON-NLS-1$
        assertNotNull(avroSchema.getField("C3")); //$NON-NLS-1$
        assertEquals(avroSchemaStr, schemaParameter.getValue());
    }

}
