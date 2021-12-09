package org.talend.sdk.studio.process;

import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ElementParameterTypeImpl;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl;
import org.talend.designer.core.model.utils.emf.talendfile.impl.NodeTypeImpl;
import org.talend.sdk.component.server.front.model.ComponentDetail;
import org.talend.sdk.component.server.front.model.ComponentId;
import org.talend.sdk.component.studio.Lookups;

import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;

import java.util.Map;

class TaCoKitNodeTest {

    @Test
    void getPropertiesToMigrate() {
        final Runnable runnable = Lookups.init();
        final Jsonb build = JsonbProvider.provider("org.apache.johnzon.jsonb.JohnzonProvider").create().build();

        NodeTypeImplTest node = new NodeTypeImplTest();
        node.init();
        this.addParameter(node.getElementParameter(), "parameter1", "value1");
        this.addParameter(node.getElementParameter(), "parameter2", "composed\"value");

        final ElementParameterFake elem = new ElementParameterFake();
        elem.setName("paramTable");
        elem.setField(EParameterFieldType.TABLE.name());
        EList list = elem.getElementValue();
        ElementValueTypeFake t1 = new ElementValueTypeFake();
        t1.setElementRef("table1");
        t1.setValue("p1Value");
        list.add(t1);
        ElementValueTypeFake t2 = new ElementValueTypeFake();
        t2.setElementRef("table1");
        t2.setValue("p2composed\"value");
        list.add(t2);
        //this.addParameter(list, "p1", "p1value");
       // this.addParameter(list, "p2", "p2composed\"value");
        node.getElementParameter().add(elem);

        TaCoKitNode tckNode = new TaCoKitNode(node, this::getDetails);
        final Map<String, String> migrate = tckNode.getPropertiesToMigrate();

        final String json = build.toJson(migrate);
        Assertions.assertNotNull(json);


        runnable.run();
    }

    private void addParameter(EList elements, final String name, String value) {
        final ElementParameterFake elem = new ElementParameterFake();
        elem.setName(name);
        elem.setValue(value);
        elements.add(elem);
    }

    private ComponentDetail getDetails(String id) {
        final ComponentDetail details = new ComponentDetail();
        details.setId(new ComponentId());
        return details;
    }

    static class NodeTypeImplTest extends NodeTypeImpl {
        public NodeTypeImplTest() {
            super();
        }

        public void init() {
            final EList elementParameter = this.getElementParameter();
            final ElementParameterFake tck = new ElementParameterFake();
            tck.setName(TaCoKitNode.TACOKIT_COMPONENT_ID);
            tck.setValue("1.0");
            elementParameter.add(tck);
        }
    }

    static class ElementParameterFake extends ElementParameterTypeImpl {
        public ElementParameterFake() {
            super();
        }
    }

    private static class ElementValueTypeFake extends ElementValueTypeImpl {

        /**
         * Extends super class constructor visibility
         */
        public ElementValueTypeFake() {
            super();
        }
    }
}