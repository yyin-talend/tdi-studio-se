package org.talend.designer.dbmap;

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
import org.talend.core.model.process.IExternalData;
import org.talend.designer.dbmap.external.data.ExternalDbMapData;
import org.talend.designer.dbmap.external.data.ExternalDbMapEntry;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;

public abstract class AbstractMapperComponentDocumentation implements IComponentDocumentation {

    private String componentName;

    private String tempFolderPath;

    private Document document;

    private ExternalDbMapData externalData;

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

        final Bundle b = Platform.getBundle(DbMapActivator.PLUGIN_ID);

        URL xslFileUrl = null;
        try {
            xslFileUrl = FileLocator.toFileURL(FileLocator.find(b, new Path(
                    IHTMLDocConstants.MAPPERCOMPONENT_XSL_FILE_PATH), null));
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
     * @param data
     */
    public void setExternalData(IExternalData data) {
        this.externalData = (ExternalDbMapData) data;
    }

    /**
     * Generates all information which for XML file.
     */
    private void generateXMLInfo() {
        document = DocumentHelper.createDocument();
        Element externalNodeElement = document.addElement("externalNode");
        externalNodeElement.addAttribute("name", HTMLDocUtils.checkString(this.componentName));

        externalNodeElement.addAttribute("preview", HTMLDocUtils.checkString(this.previewPicPath));

        List<ExternalDbMapTable> inputTables = externalData.getInputTables();
        List<ExternalDbMapTable> outputTables = externalData.getOutputTables();
        List<ExternalDbMapTable> varTables = externalData.getVarsTables();

        handleMapperTablesInfo(inputTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_INPUT);
        handleMapperTablesInfo(outputTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_OUPUT);
        handleMapperTablesInfo(varTables, externalNodeElement, IHTMLDocConstants.MAPPER_TABLE_VAR);
    }

    /**
     * Generates input tables information.
     * 
     * @param mapperTableType
     */
    private void handleMapperTablesInfo(List<ExternalDbMapTable> inputTables, Element externalNodeElement,
            String mapperTableType) {
        List<ExternalDbMapTable> tables = inputTables;
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
    private void generateMapperTablesInfo(Element externalNodeElement, List<ExternalDbMapTable> tables,
            String mapperTableType) {
        Element mapperTableElement = externalNodeElement.addElement("mapperTable");
        mapperTableElement.addAttribute("type", HTMLDocUtils.checkString(mapperTableType));
        Element tableElement = null;
        for (ExternalDbMapTable table : tables) {
            tableElement = mapperTableElement.addElement("table");
            generateTableSummaryInfo(mapperTableElement, tableElement, table);

            List<ExternalDbMapEntry> metadataTableEntries = table.getMetadataTableEntries();
            if (!HTMLDocUtils.checkList(metadataTableEntries)) {
                continue;
            }

            Element metadataTableEntriesElement = tableElement.addElement("metadataTableEntries");
            for (ExternalDbMapEntry entry : metadataTableEntries) {
                generateTablesEntriesInfo(metadataTableEntriesElement, entry);
            }

            List<ExternalDbMapEntry> customConditionsEntries = table.getCustomConditionsEntries();
            if (!HTMLDocUtils.checkList(customConditionsEntries)) {
                continue;
            }
            Element customConditionsEntriesElement = tableElement.addElement("customConditionsEntries");
            for (ExternalDbMapEntry entry : customConditionsEntries) {
                generateTablesEntriesInfo(customConditionsEntriesElement, entry);
            }
        }
    }

    /**
     * Generates metadata tables entries information.
     * 
     * @param metadataTableEntriesElement
     * @param entry
     */
    private void generateTablesEntriesInfo(Element metadataTableEntriesElement, ExternalDbMapEntry entry) {
        Element entryElement = metadataTableEntriesElement.addElement("entry");
        entryElement.addAttribute("name", HTMLDocUtils.checkString(entry.getName()));
        entryElement.addAttribute("expression", HTMLDocUtils.checkString(entry.getExpression()));
        entryElement.addAttribute("operator", HTMLDocUtils.checkString(entry.getOperator()));
        entryElement.addAttribute("isJoin", String.valueOf(entry.isJoin()));
    }

    /**
     * Generates the summary information for table.
     * 
     * @param mapperTableElement
     * @param tableElement
     * @param table
     */
    private void generateTableSummaryInfo(Element mapperTableElement, Element tableElement, ExternalDbMapTable table) {

        tableElement.addAttribute("name", HTMLDocUtils.checkString(table.getName()));
        tableElement.addAttribute("tableName", HTMLDocUtils.checkString(table.getTableName()));
        tableElement.addAttribute("alias", HTMLDocUtils.checkString(table.getAlias()));
        tableElement.addAttribute("joinType", HTMLDocUtils.checkString(table.getJoinType()));
        tableElement.addAttribute("alias", HTMLDocUtils.checkString(table.getAlias()));
        tableElement.addAttribute("isMinimized", String.valueOf(table.isMinimized()));
    }

    /**
     * Sets the path of preview picture.
     * 
     * @param previewPicPath
     */
    public void setPreviewPicPath(String previewPicPath) {
        this.previewPicPath = previewPicPath;

    }
}
