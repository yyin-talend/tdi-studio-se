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
package org.talend.designer.core.generic.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INode;
import org.talend.core.model.repository.FakePropertyImpl;
import org.talend.core.runtime.util.GenericTypeUtils;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.daikon.properties.Property;
import org.talend.daikon.properties.presentation.Form;
import org.talend.designer.core.generic.model.GenericElementParameter;
import org.talend.designer.core.model.FakeElement;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.test.utils.testproperties.TestProperties;

/**
 * created by hcyi on Feb 16, 2016 Detailled comment
 *
 */
public class ComponentsUtilsTest {

    @Test
    public void testGetParametersFromForm() {
        ComponentProperties props = (ComponentProperties) new TestProperties("test").init(); //$NON-NLS-1$
        Form form = props.getForm(Form.MAIN);

        /*
         * Test wizard
         */
        Element element = new FakeElement(form.getName());

        // Test readonly case
        List<ElementParameter> parameters = ComponentsUtils.getParametersFromForm(element, false, null, props, form);
        for (ElementParameter parameter : parameters) {
            assertFalse(parameter.isReadOnly());
        }

        element.setReadOnly(true);
        parameters = ComponentsUtils.getParametersFromForm(element, false, null, props, form);
        for (ElementParameter parameter : parameters) {
            assertTrue(parameter.isReadOnly());
        }

        /*
         * Test component
         */
        IComponent component = ComponentsFactoryProvider.getInstance().get("tSalesforceInput", "DI"); //$NON-NLS-1$ //$NON-NLS-2$
        INode node = new Node(component, new Process(new FakePropertyImpl()));

        // Test parameter initialization case (mainly to test ComponentsUtils.getParameterValue() method).
        checkParameterInitializationStatus(node, true);
        checkParameterInitializationStatus(node, false);
    }

    private void checkParameterInitializationStatus(INode node, boolean isInitializing) {
        ComponentProperties props = node.getComponentProperties();
        Form form = props.getForm(Form.MAIN);
        List<ElementParameter> parameters = ComponentsUtils.getParametersFromForm(node, isInitializing, null, props, form);
        checkParameterInitializationStatus(parameters, isInitializing);
    }

    private void checkParameterInitializationStatus(List<ElementParameter> parameters, boolean isInitializing) {
        for (ElementParameter parameter : parameters) {
            if (parameter instanceof GenericElementParameter) {
                GenericElementParameter genericElementParameter = (GenericElementParameter) parameter;
                Property property = genericElementParameter.getProperty();
                if (property != null) {
                    if (GenericTypeUtils.isStringType(property)) {
                        String propertyValue = (String) property.getValue();
                        String parameterValue = (String) parameter.getValue();
                        // If value is empty (from default value or input) add double quotes.
                        if (StringUtils.isEmpty(propertyValue)) {
                            assertTrue("\"\"".equals(parameterValue)); //$NON-NLS-1$
                        } else { // Else value is not empty.
                            if (isInitializing) {
                                // If isInitializing is true value will be surrounded with quotes if not exist.
                                assertTrue(parameterValue.startsWith("\"") && parameterValue.endsWith("\"")); //$NON-NLS-1$ //$NON-NLS-2$
                            } else if (!propertyValue.startsWith("\"") || !propertyValue.endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                                // Else shouldn't add quotes to the value.
                                assertFalse(parameterValue.startsWith("\"") && parameterValue.endsWith("\"")); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
                    }
                }
            }
        }
    }

}
