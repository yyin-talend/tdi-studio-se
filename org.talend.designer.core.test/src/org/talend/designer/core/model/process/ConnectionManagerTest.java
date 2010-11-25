// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.model.process;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ConnectionManagerTest {

    private static Connection connection;

    private static Connection newConnection;

    private static Node source;

    private static Node newSource;

    private static Node target;

    private static Node newTarget;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String componentDir = getComponentsLocation(IComponentsFactory.COMPONENTS_INNER_FOLDER).getAbsolutePath();
        String sourceComponentPath = componentDir.concat(File.separator).concat("tFileInputDelimited").concat(File.separator)
                .concat("tFileInputDelimited_java.xml");
        File sourceXmlMainFile = new File(sourceComponentPath);
        EmfComponent sourceComponent = new EmfComponent(sourceXmlMainFile, IComponentsFactory.COMPONENTS_INNER_FOLDER);
        String targetComponentPath = componentDir.concat(File.separator).concat("tFileOutputDelimited").concat(File.separator)
                .concat("tFileOutputDelimited_java.xml");
        File targetXmlMainFile = new File(targetComponentPath);
        EmfComponent targetComponent = new EmfComponent(targetXmlMainFile, IComponentsFactory.COMPONENTS_INNER_FOLDER);

        Property property = getMyProperty();
        org.talend.designer.core.ui.editor.process.Process process = new Process(property);
        source = new Node(sourceComponent, process);
        source.setLocation(new Point(1, 10));
        newSource = new Node(sourceComponent, process);
        newSource.setLocation(new Point(20, 30));
        target = new Node(targetComponent, process);
        target.setLocation(new Point(100, 2));
        newTarget = new Node(targetComponent, process);
        newTarget.setLocation(new Point(150, 200));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null)
            connection.disconnect();
        if (newConnection != null)
            newConnection.disconnect();
    }

    /**
     * Test method for
     * {@link org.talend.designer.core.model.process.ConnectionManager#canConnectToSource(org.talend.designer.core.ui.editor.nodes.Node, org.talend.designer.core.ui.editor.nodes.Node, org.talend.designer.core.ui.editor.nodes.Node, org.talend.core.model.process.EConnectionType, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCanConnectToSource() {
        try {
            connection = new Connection(source, target, EConnectionType.FLOW_MAIN, EConnectionType.FLOW_MAIN.getName(), "test",
                    "test", "test", false);
            // Check the circle, certainly, you can try to test with some other conditions.
            boolean canConnect = ConnectionManager.canConnectToSource(source, target, target, EConnectionType.FLOW_MAIN,
                    EConnectionType.FLOW_MAIN.getName(), "test");
            // The right result is can't be connected.
            assertTrue(!canConnect);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test CanConnectToSource() method failure.");
        }
    }

    /**
     * Test method for
     * {@link org.talend.designer.core.model.process.ConnectionManager#canConnectToTarget(org.talend.designer.core.ui.editor.nodes.Node, org.talend.designer.core.ui.editor.nodes.Node, org.talend.designer.core.ui.editor.nodes.Node, org.talend.core.model.process.EConnectionType, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCanConnectToTarget() {
        try {
            // First time, check the situation of creating the connection.
            boolean canConnect = ConnectionManager.canConnectToTarget(source, null, target, EConnectionType.FLOW_MAIN,
                    EConnectionType.FLOW_MAIN.getName(), "test");
            assertTrue(canConnect);
            // Second time, check the situation of reconnect the connection.
            connection = new Connection(source, target, EConnectionType.FLOW_MAIN, EConnectionType.FLOW_MAIN.getName(), "test",
                    "test", "test", false);
            canConnect = ConnectionManager.canConnectToTarget(source, target, newTarget, EConnectionType.FLOW_MAIN,
                    EConnectionType.FLOW_MAIN.getName(), "test");
            assertTrue(canConnect);
            newConnection = new Connection(target, newTarget, EConnectionType.FLOW_MAIN, EConnectionType.FLOW_MAIN.getName(),
                    "test2", "test2", "test2", false);
            canConnect = ConnectionManager.canConnectToTarget(target, newTarget, target, EConnectionType.FLOW_MAIN,
                    EConnectionType.FLOW_MAIN.getName(), "test2");
            assertTrue(!canConnect);
            canConnect = ConnectionManager.canConnectToTarget(target, newTarget, newTarget, EConnectionType.FLOW_MAIN,
                    EConnectionType.FLOW_MAIN.getName(), "test2");
            assertTrue(!canConnect);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test CanConnectToSource() method failure.");
        }
    }

    /**
     * Test method for
     * {@link org.talend.designer.core.model.process.ConnectionManager#canRename(org.talend.designer.core.ui.editor.nodes.Node, org.talend.designer.core.ui.editor.nodes.Node, org.talend.core.model.process.EConnectionType, java.lang.String)}
     * .
     */
    @Test
    public void testCanRename() {
        try {
            connection = new Connection(source, target, EConnectionType.FLOW_MAIN, EConnectionType.FLOW_MAIN.getName(), "test",
                    "test", "test", false);
            boolean canRename = ConnectionManager.canRename(source, target, EConnectionType.FLOW_MAIN, "test1");
            assertTrue(canRename);
            canRename = ConnectionManager.canRename(source, target, EConnectionType.FLOW_MAIN, "test");
            assertTrue(!canRename);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test CanConnectToSource() method failure.");
        }
    }

    private static File getComponentsLocation(String folder) {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);

        File file = null;
        try {
            URL url = FileLocator.find(b, new Path(folder), null);
            if (url == null) {
                return null;
            }
            URL fileUrl = FileLocator.toFileURL(url);
            file = new File(fileUrl.getPath());
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

        return file;
    }

    private static Property getMyProperty() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(""); //$NON-NLS-1$
        property.setId("ID"); //$NON-NLS-1$
        property.setLabel("myTestJob");

        return property;
    }

}
