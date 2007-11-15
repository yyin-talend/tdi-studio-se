// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.io.File;
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

    // private String tempFolderPath;

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
            Map<String, URL> externalNodeHTMLMap/* , String tempFolderPath */) {
        this.picFilePathMap = picFilePathMap;
        this.externalNodeElement = externalNodeElement;
        this.componentsList = allComponentsList;
        this.sourceConnectionMap = sourceConnectionMap;
        this.targetConnectionMap = targetConnectionMap;
        this.designerCoreService = designerCoreService;
        this.repositoryConnectionItemMap = repositoryConnectionItemMap;
        this.repositoryDBIdAndNameMap = repositoryDBIdAndNameMap;
        this.externalNodeHTMLMap = externalNodeHTMLMap;
        // this.tempFolderPath = tempFolderPath;
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
                    componentName, HTMLDocUtils.getTmpFolder() /* checkExternalPathIsExists(tempFolderPath) */);

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

    /**
     * Checks if external node component directory is existing.
     * 
     * @param resource
     * @return
     */
    private String checkExternalPathIsExists(String tempFolderPath) {
        String tempExternalPath = tempFolderPath + File.separator + IHTMLDocConstants.EXTERNAL_FOLDER_NAME;
        File file = new File(tempExternalPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tempExternalPath;
    }

}
