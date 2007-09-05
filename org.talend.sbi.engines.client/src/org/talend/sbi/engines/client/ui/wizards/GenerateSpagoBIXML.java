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
package org.talend.sbi.engines.client.ui.wizards;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class GenerateSpagoBIXML {

    private File file;

    private ProcessItem item;

    private String contextName;

    public static final String SPAGOBI_FILE = "spagobi.xml";

    public static final String SPAGOBI_FOLDER = "_spagobi";

    /**
     * qzhang GenerateSpagoBIXML constructor comment.
     */
    public GenerateSpagoBIXML(File tmpFold, ProcessItem item, String contextName) {
        File spagobiFolder = new File(tmpFold, item.getProperty().getLabel() + SPAGOBI_FOLDER);
        if (!spagobiFolder.exists()) {
            spagobiFolder.mkdir();
        }
        this.file = new File(spagobiFolder, SPAGOBI_FILE); //$NON-NLS-1$
        this.item = item;
        this.contextName = contextName;
        createSpagoBIXML();
    }

    public URL getResult() throws Exception {
        if (file == null) {
            createSpagoBIXML();
        }
        return file.toURL();
    }

    private void createSpagoBIXML() {
        if (file != null) {
            try {
                Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                        .getProject();
                final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
                fabrique.setValidating(true);
                final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
                analyseur.setErrorHandler(new ErrorHandler() {

                    public void error(final SAXParseException exception) throws SAXException {
                        throw exception;
                    }

                    public void fatalError(final SAXParseException exception) throws SAXException {
                        throw exception;
                    }

                    public void warning(final SAXParseException exception) throws SAXException {
                        throw exception;
                    }

                });

                Document document = analyseur.newDocument();
                Element spagobi = document.createElement("etl"); //$NON-NLS-1$
                document.appendChild(spagobi);
                // ///////////////////add job element.
                Element projectElement = document.createElement("job"); //$NON-NLS-1$
                spagobi.appendChild(projectElement);

                Attr attr = document.createAttribute("project");
                attr.setNodeValue(project.getEmfProject().getLabel());
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("jobName");
                attr.setNodeValue(item.getProperty().getLabel());
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("context");
                attr.setNodeValue(contextName);
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("language");
                attr.setNodeValue(project.getLanguage().getName());
                projectElement.setAttributeNode(attr);

                XMLSerializer serializer = new XMLSerializer();
                OutputFormat outputFormat = new OutputFormat();
                outputFormat.setIndenting(true);
                serializer.setOutputFormat(outputFormat);
                serializer.setOutputCharStream(new java.io.FileWriter(file));
                serializer.serialize(document);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
