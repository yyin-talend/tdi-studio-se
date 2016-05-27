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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.core.model.process.Element;
import org.talend.daikon.NamedThing;
import org.talend.daikon.SimpleNamedThing;
import org.talend.daikon.properties.Property;
import org.talend.daikon.properties.StringProperty;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.designer.core.generic.model.GenericElementParameter;
import org.talend.designer.core.model.FakeElement;
import org.talend.designer.core.model.components.ElementParameter;

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
        List<ElementParameter> parameters = ComponentsUtils.getParametersFromForm(element, null, props, form);
        for (ElementParameter parameter : parameters) {
            assertFalse(parameter.isReadOnly());
        }

        element.setReadOnly(true);
        parameters = ComponentsUtils.getParametersFromForm(element, null, props, form);
        for (ElementParameter parameter : parameters) {
            assertTrue(parameter.isReadOnly());
        }

    }

    @Test
    public void testGetFormalPossibleValues() {
        ComponentService componentService = ComponentsUtils.getComponentService();
        ComponentProperties props = (ComponentProperties) new TestProperties("test").init(); //$NON-NLS-1$
        Form form = props.getForm(Form.MAIN);
        Element element = new FakeElement(form.getName());
        Widget testWidget = null;
        Property testProperty = null;
        Collection<Widget> widgets = form.getWidgets();
        Iterator<Widget> widgetsIterator = widgets.iterator();
        while (widgetsIterator.hasNext()) {
            Widget widget = widgetsIterator.next();
            NamedThing content = widget.getContent();
            if (content instanceof Property) {
                testWidget = widget;
                testProperty = (Property) content;
                break;
            }
        }
        assertNotNull(testWidget);
        assertNotNull(testProperty);

        // Test NamedThing type
        List<NamedThing> namedThings = new ArrayList<>();
        namedThings.add(new SimpleNamedThing("p1", "Test P1"));
        namedThings.add(new SimpleNamedThing("p2", "Test P2"));
        namedThings.add(new SimpleNamedThing("p3", "Test P3"));
        testProperty.setPossibleValues(namedThings);

        GenericElementParameter param = new GenericElementParameter(element, props, form, testWidget, componentService);
        param.setPossibleValues(testProperty.getPossibleValues());
        List<NamedThing> possibleValues = ComponentsUtils.getFormalPossibleValues(param);
        assertEquals(3, possibleValues.size());
        namedThings.retainAll(possibleValues);
        assertEquals(3, namedThings.size());

        // Test Integer type which is not support yet
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        testProperty.setPossibleValues(ints);

        param = new GenericElementParameter(element, props, form, testWidget, componentService);
        param.setPossibleValues(testProperty.getPossibleValues());
        possibleValues = ComponentsUtils.getFormalPossibleValues(param);
        assertEquals(0, possibleValues.size());

        // Test StringProperty
        testWidget = null;
        StringProperty testStringProperty = null;
        widgetsIterator = widgets.iterator();
        while (widgetsIterator.hasNext()) {
            Widget widget = widgetsIterator.next();
            NamedThing content = widget.getContent();
            if (content instanceof StringProperty) {
                testWidget = widget;
                testStringProperty = (StringProperty) content;
                break;
            }
        }
        assertNotNull(testWidget);
        assertNotNull(testStringProperty);

        testStringProperty.setPossibleNamedThingValues(namedThings);
        param = new GenericElementParameter(element, props, form, testWidget, componentService);
        param.setPossibleValues(testStringProperty.getPossibleValues());
        possibleValues = ComponentsUtils.getFormalPossibleValues(param);
        assertEquals(3, possibleValues.size());
        List<String> pvNames = new ArrayList<>();
        for (NamedThing pv : possibleValues) {
            pvNames.add(pv.getName());
        }
        for (NamedThing nt : namedThings) {
            assertTrue(pvNames.contains(nt.getName()));
        }
    }

}
