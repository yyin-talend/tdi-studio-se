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
package org.talend.designer.core.generic.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.daikon.properties.property.Property;
import org.talend.designer.core.generic.constants.IGenericConstants;
import org.talend.designer.core.generic.utils.TestProperties;

/**
 * created by hcyi on Feb 17, 2016 Detailled comment
 *
 */
public class ComponentTest {

    private Component component;

    protected Component getComponent() {
        return component;
    }

    @Before
    public void setUp() throws Exception {
        initComponent();
    }

    protected void initComponent() throws Exception {
        component = mock(Component.class);
        ComponentDefinition componentDefinition = mock(ComponentDefinition.class);
        when(component.getComponentDefinition()).thenReturn(componentDefinition);
        component.setPaletteType("DI"); //$NON-NLS-1$
        when(component.getName()).thenReturn("tComponentName");//$NON-NLS-1$
        when(component.getLongName()).thenReturn("tComponentName");//$NON-NLS-1$
        //
    }

    @Test
    public void testGetElementParameterValueFromComponentProperties() {
        INode iNode = mock(INode.class);
        ComponentProperties iNodeComponentProperties = mock(ComponentProperties.class);
        iNode.setComponentProperties(iNodeComponentProperties);
        IElementParameter param = mock(GenericElementParameter.class);
        Object obj = component.getElementParameterValueFromComponentProperties(iNode, param);
        Assert.assertNull(obj);
    }

    @Test
    public void testGetCodegenValue_quotesTag() {
        TestProperties props = (TestProperties) new TestProperties("test").init(); //$NON-NLS-1$
        Property userIdProperty = props.userId;

        // without add quotes tag
        String codegenValue = component.getCodegenValue(userIdProperty, "id1");
        assertEquals("id1", codegenValue);

        // with add quotes tag
        userIdProperty.setTaggedValue(IGenericConstants.ADD_QUOTES, true);

        codegenValue = component.getCodegenValue(userIdProperty, "id1");
        assertEquals("\"id1\"", codegenValue);

        codegenValue = component.getCodegenValue(userIdProperty, "\"id1\"");
        assertEquals("\"id1\"", codegenValue);
    }

}
