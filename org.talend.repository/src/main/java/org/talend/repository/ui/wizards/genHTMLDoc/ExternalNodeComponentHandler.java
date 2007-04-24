// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.IDesignerCoreService;

/**
 * This class is external node component handler for generating HTML.
 * 
 * @author ftang
 * 
 */
public class ExternalNodeComponentHandler extends AbstractComponentHandler {

    private Map<String, List> targetConnectionMap = null;

    private Map<String, List> sourceConnectionMap = null;

    private Map<String, String> picFilePathMap;

    private List<Map> mapList;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, String> repositoryDBIdAndNameMap;

    private IDesignerCoreService designerCoreService;

    private Element externalNodeElement;

    private List<INode> componentsList;

    private Map<String, URL> externalNodeHTMLMap;

    /**
     * Contructor.
     * 
     * @param picFilePathMap
     * @param externalNodeElement
     * @param allComponentsList
     * @param sourceConnectionMap
     * @param targetConnectionMap
     * @param designerCoreService
     * @param repositoryConnectionItemMap
     * @param repositoryDBIdAndNameMap
     * @param externalNodeHTMLList
     */
    public ExternalNodeComponentHandler(Map<String, String> picFilePathMap, Element externalNodeElement,
            List<INode> allComponentsList, Map<String, List> sourceConnectionMap,
            Map<String, List> targetConnectionMap, IDesignerCoreService designerCoreService,
            Map<String, ConnectionItem> repositoryConnectionItemMap, Map<String, String> repositoryDBIdAndNameMap,
            Map<String, URL> externalNodeHTMLMap) {
        this.picFilePathMap = picFilePathMap;
        this.externalNodeElement = externalNodeElement;
        this.componentsList = allComponentsList;
        this.sourceConnectionMap = sourceConnectionMap;
        this.targetConnectionMap = targetConnectionMap;
        this.designerCoreService = designerCoreService;
        this.repositoryConnectionItemMap = repositoryConnectionItemMap;
        this.repositoryDBIdAndNameMap = repositoryDBIdAndNameMap;
        this.externalNodeHTMLMap = externalNodeHTMLMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    public void generateComponentInfo() {

        for (INode externalNode : this.componentsList) {
            Element componentElement = generateComponentDetailsInfo(true, externalNode, this.externalNodeElement,
                    this.picFilePathMap, this.sourceConnectionMap, this.targetConnectionMap,
                    this.repositoryDBIdAndNameMap);

            Element parametersElement = componentElement.addElement("parameters");
            List elementParameterList = externalNode.getElementParameters();

            String componentName = externalNode.getUniqueName();
            IComponentDocumentation componentDocumentation = externalNode.getExternalNode().getComponentDocumentation(
                    componentName, HTMLDocUtils.getTmpFolder());

            // Checks if generating html file for external node failed, generating the same information as internal node
            // instead.
            if (componentDocumentation == null) {
                generateElementParamInfo(parametersElement, elementParameterList);
                generateComponentSchemaInfo(externalNode, componentElement);
            } else {
                URL fileURL = componentDocumentation.getHTMLFile();
                this.externalNodeHTMLMap.put(componentName, fileURL);
            }
            componentElement.addComment(componentName);
        }
    }

    /**
     * Generates parameter element information only for component external node component.
     * 
     * @param parametersElement
     * @param elementParameterList
     */
    private void generateElementParamInfo(Element parametersElement, List elementParameterList) {
        if (elementParameterList != null && elementParameterList.size() != 0) {
            Element parameterElement = null;
            for (int j = 0; j < elementParameterList.size(); j++) {
                IElementParameter type = (IElementParameter) elementParameterList.get(j);
                if (type.getName().equals(IHTMLDocConstants.REPOSITORY_COMMENT)) {
                    parameterElement = parametersElement.addElement("parameter");
                    Element columnElement = parameterElement.addElement("column");
                    columnElement.addAttribute("name", IHTMLDocConstants.DISPLAY_COMMENT);
                    columnElement.setText(type.getValue().toString());
                    break;
                }
            }
        }
    }
}
