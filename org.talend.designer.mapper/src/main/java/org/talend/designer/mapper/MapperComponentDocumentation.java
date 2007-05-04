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
package org.talend.designer.mapper;

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
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.HTMLHandler;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.genhtml.XMLHandler;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;

/**
 * This class is used for generating HTML file for Component 'tMap'. <br/>
 * 
 */
public class MapperComponentDocumentation implements IComponentDocumentation {

    private String componentName;

    private String tempFolderPath;

    private Document document;

    private ExternalMapperData externalData;

    private String previewPicPath;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IComponentDocumentation#getHTMLFile()
     */
    public URL getHTMLFile() {

        String xmlFilepath = this.tempFolderPath + File.separatorChar + this.componentName
                + IHTMLDocConstants.XML_FILE_SUFFIX;

        String htmlFilePath = this.tempFolderPath + File.separatorChar + this.componentName
                + IHTMLDocConstants.HTML_FILE_SUFFIX;

        final Bundle b = Platform.getBundle(Activator.PLUGIN_ID);

        URL xslFileUrl = null;
        try {
            xslFileUrl = FileLocator.toFileURL(FileLocator
                    .find(b, new Path(IHTMLDocConstants.TMAP_XSL_FILE_PATH), null));
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
     * @see org.talend.core.model.process.IComponentDocumentation#setComponentLabel(java.lang.String)
     */
    public void setComponentName(String componentLabel) {
        this.componentName = componentLabel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IComponentDocumentation#setPath(java.lang.String)
     */
    public void setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
    }

    /**
     * Sets the <code>externalData</code>.
     * 
     * @param externalData
     */
    public void setExternalData(ExternalMapperData externalData) {
        this.externalData = externalData;
    }

    /**
     * Generates all information which for XML file.
     */
    private void generateXMLInfo() {
        document = DocumentHelper.createDocument();
        Element externalNodeElement = document.addElement("externalNode");
        externalNodeElement.addAttribute("name", HTMLDocUtils.checkString(this.componentName));

        externalNodeElement.addAttribute("preview", HTMLDocUtils.checkString(this.previewPicPath));

        List<ExternalMapperTable> inputTables = externalData.getInputTables();
        List<ExternalMapperTable> outputTables = externalData.getOutputTables();
        List<ExternalMapperTable> varTables = externalData.getVarsTables();

        handleMapperTablesInfo(inputTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_INPUT);
        handleMapperTablesInfo(outputTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_OUPUT);
        handleMapperTablesInfo(varTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_VAR);
    }

    /**
     * Generates input tables information.
     * 
     * @param mapperTableType
     */
    private void handleMapperTablesInfo(List<ExternalMapperTable> inputTables, Element externalNodeElement,
            String mapperTableType) {
        List<ExternalMapperTable> tables = inputTables;
        if (!HTMLDocUtils.checkList(tables)) {
            return;
        }
        generateMapperTablesInfo(externalNodeElement, tables, mapperTableType);
    }

    /**
     * This method used for generating all mapper tables information into xml file.
     * 
     * @param externalNodeElement
     * @param tables
     * @param mapperTableType
     */
    private void generateMapperTablesInfo(Element externalNodeElement, List<ExternalMapperTable> tables,
            String mapperTableType) {
        Element mapperTableElement = externalNodeElement.addElement("mapperTable");
        mapperTableElement.addAttribute("type", HTMLDocUtils.checkString(mapperTableType));
        Element tableElement = null;
        for (ExternalMapperTable table : tables) {
            tableElement = mapperTableElement.addElement("table");
            generateTableSummaryInfo(mapperTableElement, tableElement, table);

            List<ExternalMapperTableEntry> metadataTableEntries = table.getMetadataTableEntries();
            if (!HTMLDocUtils.checkList(metadataTableEntries)) {
                continue;
            }

            Element metadataTableEntriesElement = tableElement.addElement("metadataTableEntries");
            for (ExternalMapperTableEntry entry : metadataTableEntries) {
                generateTablesEntriesInfo(metadataTableEntriesElement, entry);
            }

            List<ExternalMapperTableEntry> constraintTableEntries = table.getConstraintTableEntries();
            if (!HTMLDocUtils.checkList(constraintTableEntries)) {
                continue;
            }
            Element constraintTableEntriesElement = tableElement.addElement("constraintTableEntries");
            for (ExternalMapperTableEntry entry : constraintTableEntries) {
                generateTablesEntriesInfo(constraintTableEntriesElement, entry);
            }
        }
    }

    /**
     * Generates metadata tables entries information.
     * 
     * @param metadataTableEntriesElement
     * @param entry
     */
    private void generateTablesEntriesInfo(Element metadataTableEntriesElement, ExternalMapperTableEntry entry) {
        Element entryElement = metadataTableEntriesElement.addElement("entry");
        entryElement.addAttribute("name", HTMLDocUtils.checkString(entry.getName()));
        entryElement.addAttribute("type", HTMLDocUtils.checkString(entry.getType()));
        entryElement.addAttribute("expression", HTMLDocUtils.checkString(entry.getExpression()));
        entryElement.addAttribute("isNullable", String.valueOf(entry.isNullable()));
    }

    /**
     * Generates the summary information for table.
     * 
     * @param mapperTableElement
     * @param tableElement
     * @param table
     */
    private void generateTableSummaryInfo(Element mapperTableElement, Element tableElement, ExternalMapperTable table) {

        tableElement.addAttribute("name", table.getName());
        tableElement.addAttribute("isMinimized", String.valueOf(table.isMinimized()));
        tableElement.addAttribute("isReject", String.valueOf(table.isReject()));
        tableElement.addAttribute("isRejectInnerJoin", String.valueOf(table.isRejectInnerJoin()));
        tableElement.addAttribute("isInnerJoin", String.valueOf(table.isInnerJoin()));
    }

    /**
     * Sets the preview picture of component.
     * 
     * @param previewPicPath
     */
    public void setPreviewPicPath(String previewPicPath) {
        this.previewPicPath = previewPicPath;

    }
}
