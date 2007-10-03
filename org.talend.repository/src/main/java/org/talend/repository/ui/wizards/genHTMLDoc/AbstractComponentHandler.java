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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.utils.RepositoryPathProvider;

/**
 * This abstract class is defined some common methods for generation HTML. <br/>
 * 
 */
public abstract class AbstractComponentHandler implements IComponentHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    public abstract void generateComponentInfo();

    /**
     * Gets the file path of component icon base on node.
     * 
     * @return a string reprenenting component icon's path
     */
    protected String getComponentIconPath(INode node) {
        String string = node.getComponent().getIcon32().toString();
        String path = string.substring("URLImageDescriptor(".length(), string.length() - 1); //$NON-NLS-1$
        try {
            return new URL(path).getPath();
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * Gets tMap's preview image path base on a list of <code>IElementParameter</code>
     * 
     * @param elementParameters
     * @return
     */
    protected String getPreviewImagePath(List<? extends IElementParameter> elementParameters) {

        for (IElementParameter parameter : elementParameters) {
            IElementParameter type = parameter;
            if (type.getName().equals("PREVIEW")) {
                return type.getValue().toString();
            }
        }

        return "";
    }

    /**
     * Generates component details informaiton.
     * 
     * @param isExternalNodeComponent
     * @param nodeElement
     * @param picFilePathMap
     * @param targetConnectionMap
     * @param repositoryDBIdAndNameMap
     * @return
     */
    protected Element generateComponentDetailsInfo(boolean isExternalNodeComponent, INode node, Element nodeElement,
            Map picFilePathMap, Map<String, List> sourceConnectionMap, Map<String, List> targetConnectionMap,
            Map<String, String> repositoryDBIdAndNameMap) {

        Element componentElement = nodeElement.addElement("component");
        String relativePath = getComponentIconPath(node);
        String componentIconFileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);

        String uniqueName = node.getUniqueName();
        componentElement.addAttribute("icon", IHTMLDocConstants.PICTUREFOLDERPATH + componentIconFileName);
        componentElement.addAttribute("uniqueName", uniqueName);

        // Stores the path of component icon.
        picFilePathMap.put(componentIconFileName, relativePath);
        List<? extends IElementParameter> elementParameters = node.getElementParameters();

        String componentLabel = getLabelFromElemParameters(elementParameters);

        componentElement.addAttribute("label", componentLabel == null ? "" : componentLabel);

        String previewImagePath, storedPreviewImagePath = "";

        // If component is external node component, gets its preview picture.
        if (isExternalNodeComponent) {
            previewImagePath = getPreviewImagePath(elementParameters);
            if (!previewImagePath.equals("")) {
                IPath filePath = RepositoryPathProvider.getPathFileName(RepositoryConstants.IMG_DIRECTORY, previewImagePath);
                File file = new File(filePath.toOSString());
                if (file.exists()) {
                    storedPreviewImagePath = filePath.toOSString();
                    picFilePathMap.put(previewImagePath, storedPreviewImagePath);
                    componentElement.addAttribute("preview", IHTMLDocConstants.PICTUREFOLDERPATH + previewImagePath);
                }
            }
        }

        List<String> sourceList = sourceConnectionMap.get(uniqueName);

        // Gets the input of current component.
        if (sourceList != null && sourceList.size() > 0) {
            for (String string : sourceList) {
                Element inputElement = componentElement.addElement("input");
                inputElement.addAttribute("link", string);
                inputElement.setText(HTMLDocUtils.checkString(string));
            }
        } else {// Sets the value of input to 'none'.
            Element inputElement = componentElement.addElement("input");
            inputElement.setText(IHTMLDocConstants.EMPTY_ELEMENT_VALUE);
        }

        List<String> targetList = targetConnectionMap.get(uniqueName);
        // Gets the output of current component.
        if (targetList != null && targetList.size() > 0) {
            for (String string : targetList) {
                Element outputElement = componentElement.addElement("output");
                outputElement.addAttribute("link", string);
                outputElement.setText(string);
            }
        } else {// Sets the value of output to 'none'.
            Element inputElement = componentElement.addElement("output");
            inputElement.setText(IHTMLDocConstants.EMPTY_ELEMENT_VALUE);
        }

        Element componentTypeElement = componentElement.addElement("componentType");
        componentTypeElement.setText(HTMLDocUtils.checkString(node.getComponent().getName()));

        return componentElement;
    }

    /**
     * Gets the value of Label format.
     * 
     * @param elementParameters
     * @return
     */
    private String getLabelFromElemParameters(List<? extends IElementParameter> elementParameters) {

        for (IElementParameter parameter : elementParameters) {
            if (parameter.getCategory() == EComponentCategory.VIEW && parameter.getName().equals("LABEL")) {
                return parameter.getValue().toString();
            }
        }

        return null;
    }

    /**
     * Gets the temporary folder.
     * 
     * @return a string representing temporary folder
     */
    protected String getTmpFolder() {
        String tmpFold = System.getProperty("user.dir") + File.separatorChar + IHTMLDocConstants.TEMP_FOLDER_NAME; //$NON-NLS-1$
        // String tmpFold = System.getProperty("osgi.instance.area") +
        return tmpFold;
    }

    /**
     * This class is used for generating component schema information.
     * 
     * @param node
     * @param componentElement
     */
    protected void generateComponentSchemaInfo(INode node, Element componentElement) {
        List metaDataList = node.getMetadataList();
        Element schemasElement = null;
        if (metaDataList != null && metaDataList.size() != 0) {
            schemasElement = componentElement.addElement("schemas");
            boolean isBuiltIn = node.getConnectorFromName(EConnectionType.FLOW_MAIN.getName()).isBuiltIn()
                    || node.getConnectorFromName(EConnectionType.TABLE.getName()).isBuiltIn();

            for (int j = 0; j < metaDataList.size(); j++) {
                if ((!isBuiltIn) && (j > 0)) {
                    // if the component's main connector is not built-in
                    break;
                }
                IMetadataTable table = (IMetadataTable) metaDataList.get(j);
                List columnTypeList = table.getListColumns();
                Element schemaElement = schemasElement.addElement("schema");

                String metaName = table.getLabel();
                if (metaName == null) {
                    metaName = table.getTableName();
                }
                schemaElement.addAttribute("name", HTMLDocUtils.checkString(metaName));
                boolean dbComponent = false;
                if (node.getComponent().getFamily().startsWith("Database") || node.getComponent().getFamily().startsWith("ELT")) {
                    dbComponent = true;
                }

                for (int k = 0; k < columnTypeList.size(); k++) {
                    IMetadataColumn columnType = (IMetadataColumn) columnTypeList.get(k);
                    Element columnElement = schemaElement.addElement("column");

                    columnElement.addAttribute("name", HTMLDocUtils.checkString(columnType.getLabel()));
                    // if (dbComponent) {
                    // columnElement.addAttribute("dbColumn",
                    // HTMLDocUtils.checkString(columnType.getOriginalDbColumnName()));
                    // }

                    columnElement.addAttribute("key", HTMLDocUtils.checkString(columnType.isKey() + ""));
                    String type = HTMLDocUtils.checkString(columnType.getTalendType());
                    if (node.getComponent().getFamily().startsWith("ELT")) {
                        // if ELT then use the db type
                        type = HTMLDocUtils.checkString(columnType.getType());
                    } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
                        type = JavaTypesManager.getTypeToGenerate(columnType.getTalendType(), columnType.isNullable());
                    }
                    columnElement.addAttribute("type", type);
                    // if (dbComponent) {
                    // columnElement.addAttribute("dbType", HTMLDocUtils.checkString(columnType.getType()));
                    // }
                    String length;
                    if ((columnType.getLength() == null) || (columnType.getLength() == 0)) {
                        length = "";
                    } else {
                        length = String.valueOf(columnType.getLength());
                    }
                    columnElement.addAttribute("length", length);
                    String precision;
                    if ((columnType.getPrecision() == null) || (columnType.getPrecision() == 0)) {
                        precision = "";
                    } else {
                        precision = String.valueOf(columnType.getPrecision());
                    }
                    columnElement.addAttribute("precision", precision);
                    columnElement.addAttribute("nullable", HTMLDocUtils.checkString(columnType.isNullable() + ""));
                    columnElement.addAttribute("comment", HTMLDocUtils.checkString(ElementParameterParser.parse(node, columnType
                            .getComment())));
                }
            }
        }
        // boolean isExternalNode = true;
        // if (isExternalNode) {
        // schemasElement.addElement("componentName", node.getProcess().getLabel());
        // }
    }
}
