// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.rowgenerator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.HTMLHandler;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.genhtml.XMLHandler;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;

/**
 * This class is used for generating HTML file for Component 'tRowGenerator'. <br/> <br/>
 * 
 */
public class RowGeneratorComponentDocumentation implements IComponentDocumentation {

    private String componentName;

    private String tempFolderPath;

    private List<IMetadataTable> metadataListOut;

    private Document document;

    private String previewPicPath;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IComponentDocumentation#getHTMLFile()
     */
    public URL getHTMLFile() {
        String xmlFilepath = this.tempFolderPath + File.separatorChar + this.componentName + IHTMLDocConstants.XML_FILE_SUFFIX;

        String htmlFilePath = this.tempFolderPath + File.separatorChar + this.componentName + IHTMLDocConstants.HTML_FILE_SUFFIX;

        final Bundle b = Platform.getBundle(RowGeneratorPlugin.PLUGIN_ID);

        URL xslFileUrl = null;
        try {
            xslFileUrl = FileLocator
                    .toFileURL(FileLocator.find(b, new Path(IHTMLDocConstants.TROWGENERATOR_XSL_FILE_PATH), null));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String xslFilePath = xslFileUrl.getPath();

        generateXMLInfo();

        XMLHandler.generateXMLFile(tempFolderPath, xmlFilepath, document);
        HTMLHandler.generateHTMLFile(this.tempFolderPath, xslFilePath, xmlFilepath, htmlFilePath);

        File htmlFile = new File(htmlFilePath);
        if (htmlFile.exists()) {
            try {
                return htmlFile.toURL();
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IComponentDocumentation#setComponentName(java.lang.String)
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IComponentDocumentation#setTempFolderPath(java.lang.String)
     */
    public void setTempFolderPath(String tempFolderpath) {
        this.tempFolderPath = tempFolderpath;
    }

    /**
     * Sets an instanceof <code>List<IMetadataTable></code>
     * 
     * @param metadataListOut
     */
    public void setMetadataListOut(List<IMetadataTable> metadataListOut) {
        this.metadataListOut = metadataListOut;
    }

    /**
     * Generates all information which for XML file.
     */
    private void generateXMLInfo() {
        document = DocumentHelper.createDocument();
        Element externalNodeElement = document.addElement("externalNode");
        externalNodeElement.addAttribute("name", HTMLDocUtils.checkString(this.componentName));
        externalNodeElement.addAttribute("preview", HTMLDocUtils.checkString(this.previewPicPath));

        generateColumnInfo(externalNodeElement);
    }

    /**
     * DOC Administrator Comment method "generateColumnInfo".
     * 
     * @param externalNodeElement
     */
    private void generateColumnInfo(Element externalNodeElement) {

        for (IMetadataTable metadataTable : this.metadataListOut) {

            // List<IMetadataColumn> newMetadataTable =
            // rowGeneratorMain.getGeneratorUI().convert(metadataTable);
            for (IMetadataColumn tempColumn : metadataTable.getListColumns()) {
                MetadataColumnExt column = (MetadataColumnExt) tempColumn;
                Element columnElement = externalNodeElement.addElement("column");
                columnElement.addAttribute("name", HTMLDocUtils.checkString(column.getLabel()));
                columnElement.addAttribute("key", String.valueOf(column.isKey()));
                String type = HTMLDocUtils.checkString(column.getTalendType());
                if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
                    type = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
                }
                columnElement.addAttribute("type", type);
                columnElement.addAttribute("nullable", String.valueOf(column.isNullable()));
                String length;
                if ((column.getLength() == null) || (column.getLength() == 0)) {
                    length = "";
                } else {
                    length = String.valueOf(column.getLength());
                }

                columnElement.addAttribute("length", length);
                String precision;
                if ((column.getPrecision() == null) || (column.getPrecision() == 0)) {
                    precision = "";
                } else {
                    precision = String.valueOf(column.getPrecision());
                }
                columnElement.addAttribute("precision", precision);
                columnElement.addAttribute("default", HTMLDocUtils.checkString(column.getDefault()));
                columnElement.addAttribute("comment", HTMLDocUtils.checkString(column.getComment()));
                columnElement.addAttribute("functions", HTMLDocUtils.checkString(column.getFunction().getName()));
                columnElement.addAttribute("parameters", HTMLDocUtils.checkString(column.getParameter()));
                // columnElement.addAttribute("preview",
                // HTMLDocUtils.checkString(column.getPreview()));
            }
        }
    }

    /**
     * Sets the preview picture path of component.
     * 
     * @param previewPicPath
     */
    public void setPreviewPicPath(String previewPicPath) {
        this.previewPicPath = previewPicPath;

    }
}
