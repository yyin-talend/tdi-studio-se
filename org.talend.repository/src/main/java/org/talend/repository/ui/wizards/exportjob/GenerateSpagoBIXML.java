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
package org.talend.repository.ui.wizards.exportjob;

import it.eng.spagobi.engines.talend.client.ISpagoBITalendEngineClient;
import it.eng.spagobi.engines.talend.client.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.client.SpagoBITalendEngineClient;
import it.eng.spagobi.engines.talend.client.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.client.exception.EngineUnavailableException;
import it.eng.spagobi.engines.talend.client.exception.ServiceInvocationFailedException;
import it.eng.spagobi.engines.talend.client.exception.UnsupportedEngineVersionException;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.properties.ProcessItem;
import org.talend.repository.RepositoryPlugin;
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

    public static final String SPAGOBI_FILE = "spagobi.xml";

    public static final String SPAGOBI_FOLDER = "_spagobi";

    /**
     * qzhang GenerateSpagoBIXML constructor comment.
     */
    public GenerateSpagoBIXML(File tmpFold, ProcessItem item) {
        File spagobiFolder = new File(tmpFold, item.getProperty().getLabel() + SPAGOBI_FOLDER);
        if (!spagobiFolder.exists()) {
            spagobiFolder.mkdir();
        }
        this.file = new File(spagobiFolder, SPAGOBI_FILE); //$NON-NLS-1$
        this.item = item;
        createSpagoBIXML();
        
//        PTODO cantoine : connection to SpagoBiEngineClient to publish Job.
        try {       
            
            // create the client
            ISpagoBITalendEngineClient client = new SpagoBITalendEngineClient("biadmin", "biadmin", "localhost", "8080", "SpagoBITalendEngine");
            boolean engineAvailable = client.isEngineAvailible();

            // get some informations about the engine instance referenced by the client
            System.out.println("Engine version: " + client.getEngineVersion());
            System.out.println("Engine fullname: " + client.getEngineName());
                
            // prepare parameters used during deployment
            JobDeploymentDescriptor jobDeploymentDescriptor = new JobDeploymentDescriptor("Test", "perl");
            File zipFile = this.file;//new File(deploymentFile);
                
            // deploy job on engine runtime
            boolean result = client.deployJob(jobDeploymentDescriptor, zipFile);
            if(result) System.out.println("Jobs deployed succesfully");
            else System.out.println("Jobs not deployed");
                
        
        } catch (EngineUnavailableException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch(AuthenticationFailedException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (UnsupportedEngineVersionException e) {
            System.err.println("ERROR: Unsupported engine version");    
            System.err.println("You are using TalendEngineClientAPI version " 
                    + SpagoBITalendEngineClient.CLIENTAPI_VERSION_NUMBER + ". "
                    + "The TalendEngine instance you are trying to connect to require TalendEngineClientAPI version "
                    + e.getComplianceVersion() + " or grater.");
        } catch (ServiceInvocationFailedException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.err.println("StatusLine: " + e.getStatusLine()
                               + "responseBody: " + e.getResponseBody());
        } 
        
    }

    private static boolean isSpagoBI = true;

    public static boolean isSpagoBI() {
        return GenerateSpagoBIXML.isSpagoBI;
    }

    public static void setSpagoBI(boolean isSpagoBI) {
        GenerateSpagoBIXML.isSpagoBI = isSpagoBI;
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
                Element spagobi = document.createElement("spagobi"); //$NON-NLS-1$
                document.appendChild(spagobi);
                // ///////////////////add project element.
                Element projectElement = document.createElement("project"); //$NON-NLS-1$
                spagobi.appendChild(projectElement);

                Attr attr = document.createAttribute("name");
                attr.setNodeValue(project.getEmfProject().getLabel());
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("language");
                attr.setNodeValue(project.getLanguage().getName());
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("description");
                attr.setNodeValue(project.getDescription());
                projectElement.setAttributeNode(attr);

                attr = document.createAttribute("generatedDate");
                attr.setNodeValue(DateFormat.getDateTimeInstance().format(new Date()));
                projectElement.setAttributeNode(attr);

                String currentVersion = IHTMLDocConstants.UNKNOWN;
                currentVersion = (String) RepositoryPlugin.getDefault().getBundle().getHeaders().get(
                        org.osgi.framework.Constants.BUNDLE_VERSION);

                attr = document.createAttribute("version");
                attr.setNodeValue(currentVersion);
                projectElement.setAttributeNode(attr);

                // //////////////////add job element.
                Element jobElement = document.createElement("job"); //$NON-NLS-1$
                projectElement.appendChild(jobElement);

                attr = document.createAttribute("name");
                attr.setNodeValue(item.getProperty().getLabel());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("author");
                attr.setNodeValue(item.getProperty().getAuthor().getLogin());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("version");
                attr.setNodeValue(item.getProperty().getVersion());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("purpose");
                attr.setNodeValue(item.getProperty().getPurpose());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("status");
                attr.setNodeValue(item.getProperty().getStatusCode());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("description");
                attr.setNodeValue(item.getProperty().getDescription());
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("creation");
                final Date creationDate2 = item.getProperty().getCreationDate();
                if (creationDate2 != null) {
                    attr.setNodeValue(DateFormat.getDateTimeInstance().format(creationDate2));
                } else {
                    attr.setNodeValue("");
                }
                jobElement.setAttributeNode(attr);

                attr = document.createAttribute("modification");
                final Date modificationDate = item.getProperty().getModificationDate();
                if (modificationDate != null) {
                    attr.setNodeValue(DateFormat.getDateTimeInstance().format(modificationDate));
                } else {
                    attr.setNodeValue("");
                }
                jobElement.setAttributeNode(attr);

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
