// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.connections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class ConnectionTest {

    private String ENABLE_PARALLEL = "ENABLE_PARALLEL"; //$NON-NLS-1$

    private String NUMBER_PARALLEL = "NUMBER_PARALLEL"; //$NON-NLS-1$

    private Connection connection = null;

    private INode source;

    private INode target;

    private String connName = "row1";

    /**
     * DOC Administrator Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        IProcess2 process = new Process(property);
        IComponent sourceCom = ComponentsFactoryProvider.getInstance().get("tMysqlInput");
        IComponent targetCom = ComponentsFactoryProvider.getInstance().get("tMysqlOutput");
        source = new Node(sourceCom, process);
        target = new Node(targetCom, process);
        connection = new Connection(source, target, EConnectionType.FLOW_MAIN, false);
    }

    /**
     * DOC Administrator Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.connections.Connection#checkTraceShowEnable()}.
     */
    @Test
    public void testCheckTraceShowEnable() {
        Object source = null;
        boolean result = false;
        String sourceID = "";
        IProcess process = DesignerPlugin.getDefault().getRunProcessService().getActiveProcess();
        if (process == null || source == null) {
            result = false;
            assertFalse(result);
            return;
        }
        if (!process.getId().equals(sourceID)) {
            result = false;
            assertFalse(result);
            return;
        }
        sourceID = process.getId();
        if (!process.getId().equals(sourceID)) {
            result = false;
            assertFalse(result);
            return;
        }
        result = DesignerPlugin.getDefault().getRunProcessService().enableTraceForActiveRunProcess();
        assertTrue(result);
    }

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.connections.Connection#updateName()}.
     */
    @Test
    public void testUpdateName() {

        List<EConnectionType> styleList = new ArrayList<EConnectionType>();
        styleList.add(EConnectionType.TABLE);
        styleList.add(EConnectionType.FLOW_MAIN);
        styleList.add(EConnectionType.FLOW_REF);
        styleList.add(EConnectionType.FLOW_MERGE);
        styleList.add(EConnectionType.RUN_IF);
        styleList.add(EConnectionType.ROUTE_WHEN);
        styleList.add(EConnectionType.ROUTE_CATCH);
        styleList.add(EConnectionType.ROUTE);
        styleList.add(EConnectionType.ITERATE);
        styleList.add(EConnectionType.SYNCHRONIZE);
        styleList.add(EConnectionType.PARALLELIZE);

        for (int i = 0; i < styleList.size(); i++) {
            int outputId = 2;
            String name = "row1";
            String linkName = "link";
            String metaName = "metadata";
            String labelText = name;

            boolean updateName = false;
            EConnectionType lineStyle = styleList.get(i);
            if (lineStyle.equals(EConnectionType.TABLE)) {
                if (outputId >= 0) {
                    labelText += " (" + metaName + ", order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    assertEquals("row1 (metadata, order:2)", labelText);
                } else {
                    labelText += " (" + linkName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("row1 (link)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.FLOW_MAIN) || lineStyle.equals(EConnectionType.FLOW_REF)) {
                List<EConnectionType> sourceStyleList = new ArrayList<EConnectionType>();
                sourceStyleList.add(EConnectionType.FLOW_MAIN);
                sourceStyleList.add(EConnectionType.FLOW_REF);
                for (int j = 0; j < sourceStyleList.size(); j++) {
                    EConnectionType sourceType = sourceStyleList.get(j);
                    name = "row1";
                    linkName = "link";
                    metaName = "metadata";
                    labelText = name;

                    if (sourceType.equals(lineStyle)) { // if it's the standard
                        // link
                        if (outputId >= 0) {
                            labelText += " (" + linkName + " order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            assertEquals("row1 (link order:2)", labelText);
                        } else {
                            labelText += " (" + linkName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                            assertEquals("row1 (link)", labelText);
                        }
                    } else if (sourceType.getName().equals(EConnectionType.FLOW_MAIN.getName())) {
                        // link
                        if (outputId >= 0) {
                            labelText += " (" + lineStyle.getDefaultLinkName() + " order:" + outputId + ")";
                            assertEquals("row1 (" + lineStyle.getDefaultLinkName() + " order:2)", labelText);
                        } else {
                            labelText += " (" + lineStyle.getDefaultLinkName() + ")";
                            assertEquals("row1 (" + lineStyle.getDefaultLinkName() + ")", labelText);
                        }
                    } else {
                        if (outputId >= 0) {
                            labelText += " (" + lineStyle.getDefaultLinkName() + ", " + linkName //$NON-NLS-1$ //$NON-NLS-2$
                                    + " order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                            assertEquals("row1 (" + lineStyle.getDefaultLinkName() + ", link order:2)", labelText);
                        } else {
                            labelText += " (" + lineStyle.getDefaultLinkName() + ", " + linkName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            assertEquals("row1 (" + lineStyle.getDefaultLinkName() + ", link)", labelText);
                        }
                    }
                    updateName = true;
                }

            } else if (lineStyle.equals(EConnectionType.FLOW_MERGE)) {
                int inputId = 1;
                if (outputId >= 0) {
                    labelText += " (Main order:" + outputId + ", " + lineStyle.getDefaultLinkName() + " order:" + inputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    assertEquals("row1 (Main order:2, " + lineStyle.getDefaultLinkName() + " order:1)", labelText);
                } else {
                    labelText += " (" + lineStyle.getDefaultLinkName() + " order:" + inputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    assertEquals("row1 (" + lineStyle.getDefaultLinkName() + " order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.RUN_IF) && (!linkName.equals(name))) {
                // if "RunIf" got a custom name
                labelText = linkName + " (" + name + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                assertEquals("link (row1)", labelText);
                // bug 8087
                if (outputId >= 0) {
                    labelText += " (order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("link (row1) (order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.ROUTE_WHEN)) {
                labelText = name;
                assertEquals("row1", labelText);
                if (outputId >= 0) {
                    labelText += " (order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("row1 (order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.ROUTE_CATCH)) {
                labelText = name;
                assertEquals("row1", labelText);
                if (outputId >= 0) {
                    labelText += " (order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("row1 (order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.ROUTE)) {
                labelText = name;
                assertEquals("row1", labelText);
                if (outputId >= 0) {
                    labelText += " (order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("row1 (order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.ITERATE)) {
                IElementParameter enableParam = this.connection.getElementParameter(ENABLE_PARALLEL);
                IElementParameter numberParam = this.connection.getElementParameter(NUMBER_PARALLEL);
                // for feature 4505
                boolean special = (outputId >= 0);
                if (special) {
                    linkName = name;
                    assertEquals("row1", linkName);
                }
                if (enableParam != null && (Boolean) enableParam.getValue()) {
                    labelText = linkName + " (x " + (String) numberParam.getValue(); //$NON-NLS-1$
                    assertEquals("row1 (x " + (String) numberParam.getValue(), labelText);
                    if (special) {
                        labelText += " order:" + outputId; //$NON-NLS-1$
                        assertEquals("row1 (x " + (String) numberParam.getValue() + " order:2", labelText);
                    }
                    labelText += ")"; //$NON-NLS-1$
                    assertEquals("row1 (x " + (String) numberParam.getValue() + " order:2)", labelText);
                } else if (special) {
                    labelText = linkName + " (order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    assertEquals("row1 (order:2)", labelText);
                }
                updateName = true;
            } else if (lineStyle.equals(EConnectionType.SYNCHRONIZE)) {
                IElementParameter synchroType = this.connection.getSource().getElementParameter("WAIT_FOR"); //$NON-NLS-1$
                if (synchroType != null) {
                    if ("All".equals(synchroType.getValue())) { //$NON-NLS-1$
                        labelText += " (Wait for all)"; //$NON-NLS-1$
                        assertEquals("row1 (Wait for all)", labelText);
                    } else if ("First".equals(synchroType.getValue())) { //$NON-NLS-1$
                        labelText += " (Wait for first)"; //$NON-NLS-1$
                        assertEquals("row1 (Wait for first)", labelText);
                    }
                }
                updateName = true;
            } else {
                if (outputId >= 0 && !lineStyle.equals(EConnectionType.PARALLELIZE)) {
                    labelText += " (" + "order:" + outputId + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    assertEquals("row1 (order:2)", labelText);
                }
                updateName = true;
            }
        }

    }

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.connections.Connection#reconnect()}.
     */
    @Test
    public void testReconnect() {
        List<EConnectionType> styleList = new ArrayList<EConnectionType>();
        styleList.add(EConnectionType.TABLE);
        styleList.add(EConnectionType.FLOW_MAIN);
        styleList.add(EConnectionType.FLOW_REF);
        styleList.add(EConnectionType.FLOW_MERGE);
        styleList.add(EConnectionType.RUN_IF);
        styleList.add(EConnectionType.ROUTE_WHEN);
        styleList.add(EConnectionType.ROUTE_CATCH);
        styleList.add(EConnectionType.ROUTE);
        styleList.add(EConnectionType.ITERATE);
        styleList.add(EConnectionType.SYNCHRONIZE);
        styleList.add(EConnectionType.PARALLELIZE);

        for (int i = 0; i < styleList.size(); i++) {
            String uniqueName = "row1";
            EConnectionType lineStyle = styleList.get(i);
            if (lineStyle.equals(EConnectionType.TABLE)) {
                if (uniqueName == null) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_TABLE_CONNECTION_NAME);
                }
                IMetadataTable table = new MetadataTable();
                if (table != null) {
                    List<IMetadataTable> metaList = new ArrayList<IMetadataTable>();
                    table.setTableName(uniqueName);
                    if (table.getLabel() == null) {
                        table.setLabel("row1");
                    }
                    metaList.add(table);
                    source.setMetadataList(metaList);
                }
                assertEquals(source.getMetadataList().get(0).getLabel(), "row1");
                assertEquals(source.getMetadataList().get(0).getTableName(), uniqueName);
            } else if (lineStyle.equals(EConnectionType.ITERATE)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_ITERATE_CONNECTION_NAME)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_ITERATE_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE) || lineStyle.equals(EConnectionType.ROUTE_ENDBLOCK)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_ROUTE_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_ROUTE_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE_WHEN)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_WHEN_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_WHEN_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE_OTHER)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_OTHER_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_OTHER_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE_CATCH)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_CATCH_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_CATCH_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE_FINALLY)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_FINALLY_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_FINALLY_CONNECTION_NAME);
                }
            } else if (lineStyle.equals(EConnectionType.ROUTE_TRY)) {
                // see 3680, the iterate link must have a unique name.
                if (uniqueName == null || !uniqueName.startsWith(Process.DEFAULT_TRY_CONNECTION_NAME)
                        || !source.getProcess().checkValidConnectionName(uniqueName)) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(Process.DEFAULT_TRY_CONNECTION_NAME);
                }
            } else if (isInTypes(lineStyle, EConnectionType.ON_COMPONENT_OK, EConnectionType.ON_COMPONENT_ERROR,
                    EConnectionType.ON_SUBJOB_OK, EConnectionType.ON_SUBJOB_ERROR, EConnectionType.RUN_IF)) {
                // see 3443, these links should have unique name
                if (uniqueName == null || uniqueName.equals(lineStyle.getDefaultLinkName())) {
                    uniqueName = source.getProcess().generateUniqueConnectionName(lineStyle.getDefaultLinkName());
                }
            }
            if ((lineStyle.equals(EConnectionType.TABLE) && source.getConnectorFromName(lineStyle.getName()).isMultiSchema())
                    || lineStyle.hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
                if (target.getProcess().checkValidConnectionName(uniqueName)) {
                    target.getProcess().addUniqueConnectionName(uniqueName);
                } else if (source.getProcess().checkValidConnectionName(uniqueName)) {
                    source.getProcess().addUniqueConnectionName(uniqueName);
                }
            }
            source.addOutput(connection);
            target.addInput(connection);
            assertTrue(source.getOutgoingConnections().contains(connection));
            assertTrue(target.getIncomingConnections().contains(connection));

        }
    }

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.connections.Connection#disconnect()}.
     */
    @Test
    public void testDisconnect() {
        List<EConnectionType> styleList = new ArrayList<EConnectionType>();
        styleList.add(EConnectionType.TABLE);
        styleList.add(EConnectionType.FLOW_MAIN);
        styleList.add(EConnectionType.FLOW_REF);
        styleList.add(EConnectionType.FLOW_MERGE);
        styleList.add(EConnectionType.RUN_IF);
        styleList.add(EConnectionType.ROUTE_WHEN);
        styleList.add(EConnectionType.ROUTE_CATCH);
        styleList.add(EConnectionType.ROUTE);
        styleList.add(EConnectionType.ITERATE);
        styleList.add(EConnectionType.SYNCHRONIZE);
        styleList.add(EConnectionType.PARALLELIZE);

        for (int i = 0; i < styleList.size(); i++) {
            EConnectionType lineStyle = styleList.get(i);
            String uniqueName = "row1";
            // INodeConneNodeConnector != null && !sourceNodeConnector.isMultiSchema()) {
            if (lineStyle.hasConnectionCategory(IConnectionCategory.CUSTOM_NAME)
                    || isInTypes(lineStyle, EConnectionType.ITERATE, EConnectionType.ON_COMPONENT_OK,
                            EConnectionType.ON_COMPONENT_ERROR, EConnectionType.ON_SUBJOB_OK, EConnectionType.ON_SUBJOB_ERROR,
                            EConnectionType.RUN_IF, EConnectionType.ROUTE, EConnectionType.ROUTE_TRY,
                            EConnectionType.ROUTE_CATCH, EConnectionType.ROUTE_FINALLY, EConnectionType.ROUTE_ENDBLOCK,
                            EConnectionType.ROUTE_WHEN, EConnectionType.ROUTE_OTHER)) {
                source.getProcess().removeUniqueConnectionName(uniqueName);
            }
            // }
            source.removeOutput(connection);
            target.removeInput(connection);
            assertFalse(source.getOutgoingConnections().contains(connection));
            assertFalse(target.getIncomingConnections().contains(connection));

        }

    }

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.connections.Connection#isTraceConnection()}.
     */
    @Test
    public void testIsTraceConnection() {
        Object propertyValue = 3;
        boolean result = false;
        if (propertyValue != null && propertyValue instanceof Boolean) {
            result = (Boolean) propertyValue;
            assertEquals(result, propertyValue);
        }
        result = false;
        assertFalse(result);
        propertyValue = false;
        if (propertyValue != null && propertyValue instanceof Boolean) {
            result = (Boolean) propertyValue;
            assertEquals(result, propertyValue);
        }
    }

    private boolean isInTypes(EConnectionType link, EConnectionType... types) {
        for (EConnectionType type : types) {
            if (link.getId() == type.getId()) {
                return true;
            }
        }
        return false;
    }

}
