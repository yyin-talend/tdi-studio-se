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
package org.talend.componentdesigner.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.ILibEntry;
import org.talend.componentdesigner.model.componentpref.ComponentPref;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.ResourceLanguageType;
import org.talend.componentdesigner.util.XMLUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author rli
 * 
 */
public class ComponentFolderManager {

    private static String xmlSUFFIX = ".xml";

    private ComponentPref componentPref;

    private IProject project;

    private String componentFolderName;

    public void generateComponentContent(ComponentPref componentPref, IProject project) throws CoreException, IOException {
        this.componentPref = componentPref;
        this.project = project;
        this.componentFolderName = componentPref.getName();
        this.creatJetFiles();
        this.creatResourceFiles();
        this.creatXmlFile();
        this.addComponentImage();
        this.addComponentLib();
    }

    private void creatResourceFiles() throws CoreException {
        for (ResourceLanguageType resourceType : this.componentPref.getResourceLanguageTypes()) {
            creatResourceFile(resourceType);
        }
    }

    private void creatResourceFile(ResourceLanguageType resourceType) throws CoreException {
        String s = "";
        String fileName = componentPref.getName() + resourceType.getNameSuffix();
        IFile f = creatEmptyFile(fileName);

        // add property for NAME, LONG NAME AND FAMILY
        s = "NAME=" + componentPref.getName() + "\n";
        s += "LONG_NAME=" + componentPref.getLongName() + "\n";
        s += "FAMILY=" + componentPref.getFamily() + "\n";

        // add properties for each PARAMETER of component file
        s = extractNodes(s, "PARAMETER");
        // add properties for each RETURN of component
        s = extractNodes(s, "RETURN");

        InputStream propertyStream = new ByteArrayInputStream(s.getBytes());
        f.setContents(propertyStream, true, false, null);
    }

    /**
     * DOC mhirt Comment method "extractNodes".
     * 
     * @param stringToReturn
     * @param tagToFind
     * @return
     */
    private String extractNodes(String stringToReturn, String tagToFind) {
        NodeList nl = componentPref.getXmlDocument().getElementsByTagName(tagToFind);
        if (nl != null) {
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n != null) {
                    NamedNodeMap attributes = n.getAttributes();
                    n = attributes.getNamedItem("NAME");
                    if ((n != null) && (n.getNodeValue() != null) && (n.getNodeValue().compareTo("") != 0)) {
                        stringToReturn += n.getNodeValue().toUpperCase() + ".NAME=" + n.getNodeValue() + "\n";
                    }
                }
            }
        }
        return stringToReturn;
    }

    /**
     * Copy the file resources from source component folder to destination component folder, but the .jar/.pm file will
     * not copy.
     * 
     * @param project
     * @param srcComponentFolderName
     * @param desComponentFolderName
     * @throws CoreException
     * @throws IOException TODO
     */
    public void copyComponent(IProject project, String srcComponentFolderName, String desComponentFolderName)
            throws CoreException, IOException {
        IFolder srcFolder = project.getFolder(srcComponentFolderName);
        IFolder desFolder = project.getFolder(desComponentFolderName);
        String newDestinationFileName;
        if (!desFolder.exists()) {
            desFolder.create(false, true, null);
        }
        for (IResource resource : srcFolder.members()) {
            if (!(resource instanceof IFile)) {
                continue;
            }
            IFile file = (IFile) resource;
            if (file.getFileExtension().equals("jar") || file.getFileExtension().equals("pm")) {
                continue;
            }
            if (file.exists()) {
                newDestinationFileName = desComponentFolderName
                        + file.getName().substring(
                                file.getName().indexOf(srcComponentFolderName) + srcComponentFolderName.length());
                file.copy(desFolder.getFile(newDestinationFileName).getFullPath(), false, null);

                // modify NAME's value in properties file.
                if (file.getFileExtension().equals("properties")) {

                    Properties properties = new Properties();
                    InputStream propertiesInputStream = desFolder.getFile(newDestinationFileName).getContents();
                    properties.load(propertiesInputStream);
                    propertiesInputStream.close();

                    properties.setProperty("NAME", desComponentFolderName);

                    ByteArrayOutputStream propertiesOutputStream = new ByteArrayOutputStream();
                    properties.store(propertiesOutputStream, "");
                    propertiesOutputStream.close();

                    InputStream inputStream = new ByteArrayInputStream(propertiesOutputStream.toByteArray());

                    desFolder.getFile(newDestinationFileName).setContents(inputStream, true, false, null);
                    inputStream.close();

                }
            }
        }
    }

    /**
     * creat all the language type jet files, include begin,main or end files.
     * 
     * @throws CoreException
     * @throws IOException
     */
    private void creatJetFiles() throws CoreException, IOException {
        for (JetFileStamp fileStamp : this.componentPref.getJetFileStamps()) {
            creatJetLanguageFile(fileStamp);
        }
    }

    private void creatJetLanguageFile(JetFileStamp fileStamp) throws CoreException, IOException {
        String fileName = componentPref.getName() + "_" + fileStamp.getFileStampName();
        switch (componentPref.getLanguageType()) {
        case BOTHLANGUAGETYPE:
            String[] suffixs = componentPref.getLanguageType().getFileSuffix().split(";");
            for (String suffix : suffixs) {
                creatTemplateJetFile(fileName + suffix);
            }
            break;
        default:
            creatTemplateJetFile(fileName + componentPref.getLanguageType().getFileSuffix());
        }
    }

    private void creatXmlFile() throws CoreException {
        // this.project.getFullPath();
        URL[] folderUrls = FileLocator.findEntries(ComponentDesigenerPlugin.getDefault().getBundle(), project
                .getProjectRelativePath());
        String tempXMLFileName = null;
        InputStream in = null;
        File f = null;
        try {
            tempXMLFileName = FileLocator.toFileURL(folderUrls[0]).getFile() + componentPref.getName() + xmlSUFFIX;

            // String fileName = componentPref.getName();
            switch (componentPref.getLanguageType()) {
            case BOTHLANGUAGETYPE:
                String[] suffixs = componentPref.getLanguageType().getNameSuffix().split(";");
                for (String nameSuffix : suffixs) {
                    f = getTempXMLFile(tempXMLFileName, nameSuffix);
                    in = new FileInputStream(f);
                    this.copyFileFromSrc(in, this.componentFolderName + nameSuffix + xmlSUFFIX);
                    in.close();
                    in = null;
                    if (!f.delete()) {
                        f.deleteOnExit();
                    }
                }
                break;
            default:
                f = getTempXMLFile(tempXMLFileName, componentPref.getLanguageType().getNameSuffix());
                in = new FileInputStream(f);
                this.copyFileFromSrc(in, this.componentFolderName + componentPref.getLanguageType().getNameSuffix() + xmlSUFFIX);
                in.close();
                in = null;
                if (!f.delete()) {
                    f.deleteOnExit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File getTempXMLFile(String tempXMLFileName, String xmlType) throws FileNotFoundException {
        File f = null;
        XMLUtil.toSave(tempXMLFileName, componentPref.getCurrentTypeDocument(xmlType), "UTF-8");
        XMLUtil.formatXMLFile(tempXMLFileName, "UTF-8");

        f = new File(tempXMLFileName);
        return f;
    }

    private void addComponentImage() throws CoreException, FileNotFoundException {
        if (componentPref.getImageURL() == null) {
            copyFileFromSrc(ImageLib.getImageInputStream(ImageLib.COMPONENT_DEFAULT), componentPref.getName() + "_icon32.png");
        }
        copyFileFromSrc(componentPref.getImageURL());
    }

    private void creatTemplateJetFile(String fileName) throws CoreException, IOException {
        // FileInputStream templateFileStream = (FileInputStream) ComponentFolderManager.class
        // .getResourceAsStream("template.javajet");
        InputStream templateFileStream = null;
        try {
            templateFileStream = ComponentDesigenerPlugin.getDefault().getBundle().getEntry("/data/template.javajet")
                    .openStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // .getResourceAsStream("template.javajet");
        copyFileFromSrc(templateFileStream, fileName);
    }

    private IFile creatEmptyFile(String fileName) throws CoreException {
        IFolder folder = project.getFolder(this.componentFolderName);
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
        IFile newFile = folder.getFile(fileName);
        if (!newFile.exists()) {
            newFile.create(new ByteArrayInputStream(new byte[0]), false, null);
        }
        return newFile;
    }

    private void copyFileFromSrc(String srcURL, String... particularPath) throws FileNotFoundException, CoreException {
        if (srcURL == null || srcURL.equals(PluginConstant.EMPTY_STRING)) {
            return;
        }

        IPath path = new Path(srcURL);
        IFolder folder = project.getFolder(this.componentFolderName);
        if (!folder.exists()) {
            folder.create(false, true, null);
        }

        String projectFolder = this.componentFolderName;
        if (particularPath != null) {
            for (String mParticularPath : particularPath) {
                projectFolder += "/" + mParticularPath;
                folder = project.getFolder(projectFolder);
                if (!folder.exists()) {
                    folder.create(false, true, null);
                }
            }
        } else {
            projectFolder = this.componentFolderName;
        }

        IFile file = folder.getFile(path.lastSegment());
        if (file.exists()) {
            return;
        }
        FileInputStream fileStream = new FileInputStream(srcURL);
        file.create(fileStream, false, null);
    }

    private void copyFileFromSrc(InputStream inputStream, String fileName) throws CoreException {
        if (inputStream == null) {
            return;
        }
        IFolder folder = project.getFolder(this.componentFolderName);
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
        IFile file = folder.getFile(fileName);
        if (file.exists()) {
            return;
        }
        file.create(inputStream, false, null);
    }

    private void addComponentLib() throws FileNotFoundException, CoreException {
        addLibEntries(componentPref.getLibEntries());

    }

    private void addLibEntries(ILibEntry[] libEntries) throws FileNotFoundException, CoreException {
        if (libEntries == null) {
            return;
        }
        for (int i = 0; i < libEntries.length; i++) {
            if (libEntries[i].isExternal()) {
                if (libEntries[i].getType() == ILibEntry.PM) {
                    copyFileFromSrc(libEntries[i].getLocation(), "modules", this.componentFolderName);
                } else {
                    copyFileFromSrc(libEntries[i].getLocation());
                }
            }
        }
    }
}
