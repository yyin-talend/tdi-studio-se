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
package org.talend.designer.runprocess.perl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;
import org.w3c.dom.Document;

/**
 * Utilities around perl stuff. <br/>
 * 
 * $Id$
 * 
 */
public final class PerlUtils {

    public static final String PERL_PROJECT_NAME = ".Perl";

    private static final String PERL_NATURE = "org.epic.perleditor.perlnature";

    public static final String PERL_LAUNCHCONFIGURATION = "org.epic.debug.launchConfigurationPerl";

    public static final String ATTR_PROJECT_NAME = "ATTR_PROJECT_NAME";

    public static final String ATTR_STARTUP_FILE = "ATTR_STARTUP_FILE";

    public static final String ATTR_WORKING_DIRECTORY = "ATTR_WORKING_DIRECTORY";

    public static final String ATTR_PROGRAM_PARAMETERS = "ATTR_PROGRAM_PARAMETERS";

    public static final String ROUTINE_FILENAME_EXT = ".pm";

    /**
     * Constructs a new PerlUtils.
     */
    private PerlUtils() {
        super();
    }

    public static IProject getProject() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(PERL_PROJECT_NAME);

        // Does the perl nature exists in the environment
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtension nature = registry.getExtension("org.eclipse.core.resources.natures", PERL_NATURE);

        if (!prj.exists()) {
            final IWorkspace workspace = ResourcesPlugin.getWorkspace();
            final IProjectDescription desc = workspace.newProjectDescription(prj.getName());
            if (nature != null) {
                desc.setNatureIds(new String[] { PERL_NATURE });
            }
            prj.create(desc, null);
            prj.open(IResource.BACKGROUND_REFRESH, null);
        } else if (prj.getNature(PERL_NATURE) == null && nature != null) {
            IProjectDescription description = prj.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = PERL_NATURE;
            description.setNatureIds(newNatures);
            prj.setDescription(description, null);
        }

        // Fix perl module includes
        String[] includeEntries = new String[] { getPerlModulePath().toOSString() };
        writePerlIncludes(prj, includeEntries);

        return prj;
    }

    public static IPath getPerlModulePath() throws CoreException {
        Bundle perlLibBundle = Platform.getBundle("org.talend.designer.codegen.perlmodule");
        try {
            if (!(perlLibBundle == null)) {
                URL entry = perlLibBundle.getEntry("perl");
                URL url = FileLocator.resolve(entry);
                return new Path(url.getFile());
            }
        } catch (IOException e) {
        }

        throw new CoreException(new Status(Status.ERROR, RunProcessPlugin.PLUGIN_ID, Status.OK,
                "Perl Module Plugin not found.", null));
    }

    private static void writePerlIncludes(IProject prj, String[] includeEntries) throws CoreException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            org.w3c.dom.Element xmlRoot = document.createElement("includepath");
            document.appendChild(xmlRoot);
            for (int i = 0; i < includeEntries.length; i++) {
                org.w3c.dom.Element entry = document.createElement("includepathentry");
                entry.setAttribute("path", includeEntries[i]);
                xmlRoot.appendChild(entry);
            }
            String file = prj.getLocation().toString() + File.separator + ".includepath";
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamResult result = new StreamResult(out);
                transformer.transform(source, result);
            } catch (TransformerException te) {
                te.printStackTrace();

            } finally {
                out.close();
            }
        } catch (ParserConfigurationException pce) {
            throw new CoreException(new Status(Status.ERROR, RunProcessPlugin.PLUGIN_ID, Status.OK,
                    "Perl Module include failure.", pce));
        } catch (IOException ioe) {
            throw new CoreException(new Status(Status.ERROR, RunProcessPlugin.PLUGIN_ID, Status.OK,
                    "Perl Module include failure.", ioe));
        }
    }

    /**
     * DOC ftang Comment method "getPerlModuleDirectoryPath".
     * 
     * @return
     * @throws CoreException
     */
    public static IPath getPerlModuleDirectoryPath() throws CoreException {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);
        URL url = null;
        try {
            url = FileLocator.toFileURL(FileLocator.find(b, new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER), null));
            return new Path(url.getFile());
        } catch (IOException e) {
            throw new CoreException(new Status(Status.ERROR, RunProcessPlugin.PLUGIN_ID, Status.OK, Messages
                    .getString("Processor.desiginerCorePluginNotFound"), null));
        }
    }
}
