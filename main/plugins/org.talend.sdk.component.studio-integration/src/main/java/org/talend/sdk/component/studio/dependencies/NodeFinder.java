package org.talend.sdk.component.studio.dependencies;

import java.util.Map;
import java.util.Optional;

import org.talend.sdk.component.studio.model.parameter.PropertyDefinitionDecorator;
import org.talend.sdk.component.studio.model.parameter.PropertyNode;
import org.talend.sdk.component.studio.model.parameter.TaCoKitElementParameter;
import org.talend.sdk.component.studio.model.parameter.TableElementParameter;

public class NodeFinder {

    public PropertyNode find(final TaCoKitElementParameter parameter) {

        final Optional<PropertyNode> propertyNode;
        if (parameter instanceof TableElementParameter) {
            propertyNode = Optional.ofNullable(parameter)
                    .map(TaCoKitElementParameter::getPropertyNode);
        }
        else {
            propertyNode = Optional.ofNullable(parameter)
                    .map(TaCoKitElementParameter::getPropertyNode)
                    .map(PropertyNode::getParent);
        }

        Optional<Map<String, String>> metadata = propertyNode
                .map(PropertyNode::getProperty)
                .map(PropertyDefinitionDecorator::getMetadata);

        if (metadata != null
                && metadata.isPresent()
                && metadata.get().containsKey("dependencies::connector")) {
            return propertyNode.get();
        }
        return null;
    }
}
