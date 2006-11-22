// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.repository.ui.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.utils.XmlArray;
import org.talend.repository.preview.IPreview;
import org.talend.repository.preview.ProcessDescription;

/**
 * Create a ProcessDescription to use in the step2 & step3 of CSV File Wizard on Shadow mode.
 * 
 * $Id$
 * 
 */
public class ShadowProcessHelper {

    private static Logger log = Logger.getLogger(ShadowProcessHelper.class);

    /**
     * Create a ProcessDescription and set it width the value of FileConnection. Particularity : field FieldSeparator,
     * RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param FileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final FileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();

        processDescription.setServer(connection.getServer());
        processDescription.setFilepath(connection.getFilePath());
        processDescription.setFieldSeparator("\"" + connection.getFieldSeparatorValue() + "\"");
        processDescription.setRowSeparator("\"" + connection.getRowSeparatorValue() + "\"");

        processDescription.setPattern("'" + connection.getFieldSeparatorValue() + "'");

        processDescription.setHeaderRow(connection.getHeaderValue());
        processDescription.setFooterRow(connection.getFooterValue());
        processDescription.setLimitRows(connection.getLimitValue());

        if (connection.getEscapeChar() != null && !connection.getEscapeChar().equals("") && !connection.getEscapeChar().equals("Empty")) {
            processDescription.setEscapeCharacter("'" + connection.getEscapeChar() + "'");
        } else {
            processDescription.setEscapeCharacter("''");
        }
        if (connection.getTextEnclosure() != null
                && !connection.getTextEnclosure().equals("") && !connection.getTextEnclosure().equals("Empty")) {
            processDescription.setTextEnclosure("'" + connection.getTextEnclosure() + "'");
        } else {
            processDescription.setTextEnclosure("''");
        }

        processDescription.setRemoveEmptyRow(connection.isRemoveEmptyRow());
        processDescription.setEncoding("\"" + connection.getEncoding() + "\"");
        return processDescription;
    }

    /**
     * Create a ProcessDescription and set it width the value of XmlFileConnection.
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param XmlFileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final XmlFileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        //PTODO cantoine voir les param envoyé pour le ProcessDescription
        return processDescription;
    }
    
    /**
     * Create a ProcessDescription and set it width the value of LdifFileConnection. 
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param LdifFileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final LdifFileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        processDescription.setFilepath(connection.getFilePath());
        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();
        
        IMetadataTable table = new MetadataTable();
        
        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        IMetadataColumn iMetadataDn =  new MetadataColumn();
        iMetadataDn.setLabel("dn");
        iMetadataDn.setKey(false);
        iMetadataDn.setLength(0);
        iMetadataDn.setNullable(false);
        iMetadataDn.setType("String");
        iMetadataDn.setTalendType("String");
        
        schema.add(iMetadataDn);
        
        if(connection.getValue() != null && !connection.getValue().isEmpty()){
            Iterator<String> iterate = connection.getValue().iterator();
            
            while(iterate.hasNext()){
                
                IMetadataColumn iMetadataColumn =  new MetadataColumn();
                iMetadataColumn.setLabel(iterate.next());
                iMetadataColumn.setKey(false);
                iMetadataColumn.setLength(0);
                iMetadataColumn.setNullable(false);
                iMetadataColumn.setType("String");
                iMetadataColumn.setTalendType("String");
                
                schema.add(iMetadataColumn);
            }
        }
        table.setTableName("ldif");
        table.setListColumns(schema);
        tableSchema.add(table);
        processDescription.setSchema(tableSchema);
        return processDescription;
    }
    
    
    /**
     * parse a file describe by a fileConnection in XmlArray. Simple method to run the shadow process from the
     * fileConnection.
     * 
     * @param fileConnection
     * @return xmlArray
     * @throws CoreException
     */
    public static XmlArray getXmlArray(final FileConnection fileConnection, String type) throws CoreException {
        return getXmlArray(getProcessDescription(fileConnection), type);
    }

    /**
     * parse a file describe by a processDescription in XmlArray.
     * 
     * @param processDescription
     * @return xmlArray
     */
    public static XmlArray getXmlArray(final ProcessDescription processDescription, String type) throws CoreException {

        XmlArray xmlArray = null;

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        // use the org.talend.repository.filepreview_provider
        IConfigurationElement[] configurationElements = registry.getConfigurationElementsFor("org.talend.repository.filepreview_provider");

        if (configurationElements.length > 0) {
            IPreview iPreview = (IPreview) configurationElements[0].createExecutableExtension("class");
            xmlArray = iPreview.preview(processDescription, type);
        } else {
            log.error("\nThe ShadowProcess use to extract data or metadata on a File don't run."
                    + "\nConfigurationElementsFor(\"org.talend.repository.filepreview_provider\").length == 0 ??");
        }
        return xmlArray;
    }
}
