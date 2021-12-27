package org.talend.sdk.component.studio.websocket;

import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.server.front.model.ConfigTypeNodes;

class ServicesClientTest {

    @Test
    void v1ComponentDependencies() {
        final ServicesClient.WebSocketClient ws = new MockWebSocket("dependencies.json");
        final ServicesClient client = new ServicesClient(ws);

        final Map<String, ?> dependencies1 = client.v1().component().dependencies("123");

        final Collection<String> coordinates = Collection.class.cast(Map.class
                .cast(Map.class.cast(dependencies1.get("dependencies")).values().iterator().next())
                .get("dependencies"));

        Assertions.assertNotNull(coordinates);
        Assertions.assertTrue(coordinates.contains("org.talend.components:common:jar:1.30.0"));
    }

    @Test
    void v1getRepositoryModel() {
        final ServicesClient.WebSocketClient ws = new MockWebSocket("configType.json");
        final ServicesClient client = new ServicesClient(ws);
        final ConfigTypeNodes configTypeNodes = client.v1().configurationType().getRepositoryModel();
        Assertions.assertNotNull(configTypeNodes);
        final ConfigTypeNode datasetNode = configTypeNodes.getNodes().get("cmVzdCNSRVNUI2RhdGFzZXQjRGF0YXNldA");
        Assertions.assertEquals("Dataset", datasetNode.getName());
    }

}