package org.talend.sdk.component.studio.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.sdk.component.server.front.model.ComponentDetail;
import org.talend.sdk.component.studio.Lookups;
import org.talend.sdk.component.studio.model.parameter.ListPropertyNode;
import org.talend.sdk.component.studio.model.parameter.PropertyNode;

public interface ComponentReferenceFinder {
    Stream<ComponentReference> find(final PropertyNode property, final INode node);

    static ComponentReferenceFinder getFinder(final PropertyNode property) {
        if (property instanceof ListPropertyNode) {
            return new ListPropertyReferenceFinder();
        }
        return new PropertyReferenceFinder();
    }

    class PropertyReferenceFinder implements ComponentReferenceFinder {
        @Override
        public Stream<ComponentReference> find(PropertyNode property, INode node) {
            final List<PropertyNode> children = property.getChildren();
            String family = null;
            String name = null;
            String mavenReferences = null;
            for (PropertyNode child : children) {
                final String path = child.getProperty().getPath();
                final IElementParameter elementParameter = node.getElementParameter(path);
                final Object value = elementParameter.getValue();
                if (value != null && path.endsWith(".family")) {
                    family = String.valueOf(value);
                } else if (value != null && path.endsWith(".name")) {
                    name = String.valueOf(value);
                } else if (value != null && path.endsWith(".mavenReferences")) {
                    mavenReferences = String.valueOf(value);
                }
            }
            return Stream.of(new ComponentReference(family, name, mavenReferences));
        }
    }

    class ListPropertyReferenceFinder implements ComponentReferenceFinder {
        @Override
        public Stream<ComponentReference> find(PropertyNode property, INode node) {
            if (!(property instanceof ListPropertyNode)) {
                return Stream.empty();
            }
            final List<ComponentReference> details = new ArrayList<>();
            final String path = property.getProperty().getPath();
            final IElementParameter elementParameter = node.getElementParameter(path);
            final Object values = elementParameter.getValue();
            if (values instanceof List) {
                for (Object value : (List) values) {
                    if (value instanceof Map) {
                        final String family = this.extract((Map) value, "family");
                        final String name = this.extract((Map) value, "name");
                        final String mavenReferences = this.extract((Map) value, "mavenReferences");

                        details.add(new ComponentReference(family, name, mavenReferences));
                    }
                }
            }
            return details.stream();
        }

        private String extract(Map values, String name) {
            final Object value = ((Map) values).get('"' + name + '"');
            if (!(value instanceof String)) {
                return null;
            }
            return ((String) value).substring(1, ((String) value).length() - 1);
        }
    }
}
