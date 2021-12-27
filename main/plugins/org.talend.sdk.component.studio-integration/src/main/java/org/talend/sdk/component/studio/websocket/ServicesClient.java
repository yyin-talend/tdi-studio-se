/**
 * Copyright (C) 2006-2021 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.talend.sdk.component.studio.websocket;

import org.talend.sdk.component.server.front.model.ActionList;
import org.talend.sdk.component.server.front.model.ComponentDetail;
import org.talend.sdk.component.server.front.model.ComponentDetailList;
import org.talend.sdk.component.server.front.model.ComponentIndex;
import org.talend.sdk.component.server.front.model.ComponentIndices;
import org.talend.sdk.component.server.front.model.ConfigTypeNodes;
import org.talend.sdk.component.server.front.model.DocumentationContent;
import org.talend.sdk.component.server.front.model.Environment;
import org.talend.sdk.component.server.front.model.error.ErrorPayload;
import org.talend.sdk.component.studio.lang.Pair;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import javax.json.JsonObject;

// we shouldn't need the execution runtime so don't even include it here
//
// technical note: this client includes the transport (websocket) but also the protocol/payload formatting/parsing
// todo: better error handling, can need some server bridge love too to support ERROR responses
public class ServicesClient implements AutoCloseable {

    private final WebSocketClient socketClient;

    public ServicesClient(WebSocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public synchronized V1 v1() {
        return new V1(this.socketClient);
    }

    @Override
    public synchronized void close() {
        this.socketClient.close();
    }

    public interface WebSocketClient extends AutoCloseable {
        <T> T sendAndWait(final String id, final String uri, final Object payload, final Class<T> expectedResponse, final boolean doCheck);

        void close();
    }

    public static class V1 {
        private final WebSocketClient root;

        V1(final WebSocketClient root) {
            this.root = root;
        }

        public V1Action action() {
            return new V1Action(root);
        }

        public V1ConfigurationType configurationType() {
            return new V1ConfigurationType(root);
        }

        public V1Component component() {
            return new V1Component(root);
        }

        public V1Documentation documentation() {
            return new V1Documentation(root);
        }

        public boolean healthCheck() {
            root.sendAndWait("/v1/get/environment", "/environment", null, Environment.class, false);
            return true;
        }
    }


    public static class V1ConfigurationType {
        private final WebSocketClient root;

        private V1ConfigurationType(final WebSocketClient root) {
            this.root = root;
        }

        public ConfigTypeNodes getRepositoryModel() {
            return root.sendAndWait("/v1/get/configurationtype/index", "/configurationtype/index?language=" + Locale.getDefault().getLanguage() + "&lightPayload=false", null, ConfigTypeNodes.class, true);
        }

        public ConfigTypeNodes getRepositoryModel(final boolean lightPayload) {
            return root.sendAndWait("/v1/get/configurationtype/index", "/configurationtype/index?language=" + Locale.getDefault().getLanguage() + "&lightPayload="+lightPayload, null, ConfigTypeNodes.class, true);
        }

        public Map<String, String> migrate(final String id, final int configurationVersion, final Map<String, String> payload) {
            return root.sendAndWait("/v1/post/configurationtype/migrate/{id}/{configurationVersion}",
                    "/configurationtype/migrate/" + id + "/" + configurationVersion, payload, Map.class, true);
        }
    }


    public static class V1Action {
        private final WebSocketClient root;

        private V1Action(final WebSocketClient root) {
            this.root = root;
        }

        public <T> T execute(final Class<T> expectedResponse, final String family, final String type, final String action, final Map<String, String> payload) {
            return root.sendAndWait("/v1/post/action/execute", "/action/execute?family=" + family + "&type=" + type + "&action=" + action, payload, expectedResponse, true);
        }
        
        public ActionList getActionList(final String family) {
            return root.sendAndWait("/v1/get/action/index/", "/action/index?family=" + family, null, ActionList.class, true);
        }
    }

    public static class V1Documentation {

        private final WebSocketClient root;

        private V1Documentation(final WebSocketClient root) {
            this.root = root;
        }

        public DocumentationContent getDocumentation(final String language, final String componentId, final String format) {
            return root.sendAndWait("/v1/get/documentation/component",
                    "/documentation/component/" + componentId + "?language=" + language + "&format=" + format, null,
                    DocumentationContent.class, true);
        }

    }


    public static class V1Component {
        private static final int BUNDLE_SIZE = 25;
        private final WebSocketClient root;

        private V1Component(final WebSocketClient root) {
            this.root = root;
        }

        public ComponentIndices getIndex(final String language) {
            return root.sendAndWait("/v1/get/component/index", "/component/index?language=" + language + "&includeIconContent=true", null, ComponentIndices.class, true);
        }

        public Map<String, ?> dependencies(final String id) {
            return root.sendAndWait("/v1/get/component/dependencies", "/component/dependencies?identifier=" + id, null, Map.class, true);
        }

        public byte[] icon(final String id) {
            return root.sendAndWait("/v1/get/component/icon/" + id, "/component/icon/" + id, null, byte[].class, true);
        }

        public byte[] familyIcon(final String id) {
            return root.sendAndWait("/v1/get/component/icon/family/" + id, "/component/icon/family/" + id, null, byte[].class, true);
        }

        public ComponentDetailList getDetail(final String language, final String[] identifiers) {
            if (identifiers == null || identifiers.length == 0) {
                return new ComponentDetailList(emptyList());
            }
            return root.sendAndWait("/v1/get/component/details", "/component/details?language=" + language + Stream.of(identifiers).map(i -> "identifiers=" + i).collect(Collectors.joining("&", "&", "")), null, ComponentDetailList.class, true);
        }
        
        public Stream<Pair<ComponentIndex, ComponentDetail>> details(final String language) {
            final List<ComponentIndex> components = getIndex(language).getComponents();
            // create bundles
            int bundleCount = components.size() / BUNDLE_SIZE;
            bundleCount = bundleCount * BUNDLE_SIZE >= components.size() ? bundleCount : (bundleCount + 1);
            return IntStream.range(0, bundleCount).mapToObj(i -> {
                final int from = BUNDLE_SIZE * i;
                final int to = from + BUNDLE_SIZE;
                return components.subList(from, Math.min(to, components.size()));
            }).flatMap(bundle -> {
                final Map<String, ComponentIndex> byId = bundle.stream().collect(toMap(c -> c.getId().getId(), identity()));
                return getDetail(language, bundle.stream().map(i -> i.getId().getId()).toArray(String[]::new)).getDetails().stream().map(d -> new Pair<>(byId.get(d.getId().getId()), d));
            });
        }

        public Map<String, String> migrate(final String id, final int configurationVersion, final Map<String, String> payload) {
            return root.sendAndWait("/v1/post/component/migrate/{id}/{configurationVersion}", "/component/migrate/" + id + "/" + configurationVersion, payload, Map.class, true);
        }
    }

    public static class ClientException extends RuntimeException {
        private ErrorPayload errorPayload;

        ClientException(final String raw, final ErrorPayload errorPayload) {
            super(raw);
            this.errorPayload = errorPayload;
        }

        public ErrorPayload getErrorPayload() {
            return this.errorPayload;
        }
    }
}
