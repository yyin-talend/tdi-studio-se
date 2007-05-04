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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.EParameterName;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.IDesignerCoreService;

/**
 * This class is internal node component handler for generating HTML. <br/>
 * 
 */
public class InternalNodeComponentHandler extends AbstractComponentHandler {

    private Map<String, List> targetConnectionMap = null;

    private Map<String, List> sourceConnectionMap = null;

    private Map<String, String> picFilePathMap;

    private List<Map> mapList;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, String> repositoryDBIdAndNameMap;

    private IDesignerCoreService designerCoreService;

    private Element internalNodeElement;

    private List<INode> componentsList;

    /**
     * DOC Administrator InternalNodeComponentHandler constructor comment.
     * 
     * @param picFilePathMap
     * @param internalNodeElement
     * @param allComponentsList
     * @param sourceConnectionMap
     * @param targetConnectionMap
     * @param designerCoreService
     * @param name2
     * @param name
     */
    public InternalNodeComponentHandler(Map<String, String> picFilePathMap, Element internalNodeElement,
            List<INode> allComponentsList, Map<String, List> sourceConnectionMap,
            Map<String, List> targetConnectionMap, IDesignerCoreService designerCoreService,
            Map<String, ConnectionItem> repositoryConnectionItemMap, Map<String, String> repositoryDBIdAndNameMap) {

        this.picFilePathMap = picFilePathMap;
        this.internalNodeElement = internalNodeElement;
        this.componentsList = allComponentsList;
        this.sourceConnectionMap = sourceConnectionMap;
        this.targetConnectionMap = targetConnectionMap;
        this.designerCoreService = designerCoreService;
        this.repositoryConnectionItemMap = repositoryConnectionItemMap;
        this.repositoryDBIdAndNameMap = repositoryDBIdAndNameMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    public void generateComponentInfo() {
        for (INode node : this.componentsList) {
            Element componentElement = generateComponentDetailsInfo(false, node, this.internalNodeElement,
                    this.picFilePathMap, this.sourceConnectionMap, this.targetConnectionMap,
                    this.repositoryDBIdAndNameMap);

            generateComponentElemParamters(node, componentElement);
        }
    }

    /**
     * DOC Administrator Comment method "generateComponentElemParamters".
     * 
     * @param node
     * @param componentElement
     */
    protected void generateComponentElemParamters(INode node, Element componentElement) {
        Element parametersElement = componentElement.addElement("parameters");

        List elementParameterList = node.getElementParameters();

        boolean istRunJob = node.getComponent().getName().equals("tRunJob");
        generateComponentElementParamInfo(istRunJob, parametersElement, elementParameterList, node);

        generateComponentSchemaInfo(node, componentElement);
    }

    /**
     * Generates the element parameters information of component.
     * 
     * @param istMap
     * @param istRunJob
     * @param parametersElement
     * @param elementParameterList
     * @param node
     */
    private void generateComponentElementParamInfo(boolean istRunJob, Element parametersElement,
            List elementParameterList, INode node) {

        List<IElementParameter> copyElementParameterList = new ArrayList(elementParameterList);

        if (elementParameterList != null && elementParameterList.size() != 0) {
            for (int j = 0; j < elementParameterList.size(); j++) {
                IElementParameter elemparameter = (IElementParameter) elementParameterList.get(j);

                if ((istRunJob && elemparameter.getName().equals("PROCESS"))
                        || (!elemparameter.isShow(copyElementParameterList) && (!elemparameter.getName().equals(
                                EParameterFieldType.SCHEMA_TYPE.getName())))
                        || elemparameter.getCategory().equals(EComponentCategory.MAIN)
                        || elemparameter.getCategory().equals(EComponentCategory.VIEW)
                        || elemparameter.getName().equals(IHTMLDocConstants.REPOSITORY)
                        || elemparameter.getName().equals("SCHEMA") || elemparameter.getName().equals("QUERYSTORE")
                        || elemparameter.getName().equals("PROPERTY")) {
                    continue;
                }

                // String value = checkString(elemparameter.getValue().toString());
                String value = HTMLDocUtils.checkString(elemparameter.getValue().toString());

                if (elemparameter.getName().equals(EParameterFieldType.PROPERTY_TYPE.getName())
                        && elemparameter.getValue().equals(IHTMLDocConstants.REPOSITORY)) {
                    value = elemparameter.getValue().toString().toLowerCase()
                            + ": "
                            + getRepositoryValueForPropertyType(copyElementParameterList,
                                    EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                } else if (elemparameter.getName().equals(EParameterFieldType.SCHEMA_TYPE.getName())
                        && elemparameter.getValue().equals(IHTMLDocConstants.REPOSITORY)) {
                    value = elemparameter.getValue().toString().toLowerCase()
                            + ": "
                            + getRepositoryValueForSchemaType(copyElementParameterList,
                                    EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                }

                else if (elemparameter.getName().equals(EParameterFieldType.QUERYSTORE_TYPE.getName())
                        && elemparameter.getValue().equals(IHTMLDocConstants.REPOSITORY)) {
                    value = elemparameter.getValue().toString().toLowerCase()
                            + ": "
                            + getRepositoryValueForQueryStoreType(copyElementParameterList,
                                    EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
                }
                // } else if (type.getName().equals("TYPE")) {
                // int index = type.getIndexOfItemFromList(type.getDisplayName());
                // value = checkString(type.getListItemsDisplayName()[index]);
                // }

                Element columnElement = parametersElement.addElement("column");
                columnElement.addAttribute("name", HTMLDocUtils.checkString(elemparameter.getDisplayName()));

                if (value.equalsIgnoreCase(IHTMLDocConstants.REPOSITORY_BUILT_IN)) {
                    value = IHTMLDocConstants.DISPLAY_BUILT_IN;
                }
                columnElement.setText(value);
            }
        }
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForPropertyType(List<IElementParameter> copyElementParameterList,
            String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                ConnectionItem connectionItem = repositoryConnectionItemMap.get(elemParameter.getValue());
                String aliasName = designerCoreService.getRepositoryAliasName(connectionItem);
                value = aliasName + ":" + connectionItem.getProperty().getLabel();
                break;
            }
        }
        return value;
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForSchemaType(List<IElementParameter> copyElementParameterList,
            String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                value = elemParameter.getValue().toString();
                String newValue = value.substring(0, value.indexOf("-")).trim();
                if (repositoryDBIdAndNameMap.containsKey(newValue)) {
                    value = value.replace(newValue, repositoryDBIdAndNameMap.get(newValue));
                    break;
                }

            }
        }

        return value;
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForQueryStoreType(List<IElementParameter> copyElementParameterList,
            String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                value = elemParameter.getValue().toString();
                String newValue = value.substring(0, value.indexOf("-")).trim();
                if (repositoryDBIdAndNameMap.containsKey(newValue)) {
                    value = value.replace(newValue, repositoryDBIdAndNameMap.get(newValue));
                    break;
                }
            }
        }
        return value;
    }
}
