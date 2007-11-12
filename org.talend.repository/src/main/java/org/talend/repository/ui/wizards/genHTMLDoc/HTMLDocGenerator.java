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
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.HTMLHandler;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.genhtml.XMLHandler;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.Folder;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;
import org.talend.repository.utils.FileCopyUtils;
import org.talend.repository.utils.RepositoryPathProvider;

/**
 * This class is used for generating HTML file.
 * 
 * $Id: XMLGenerator.java 2007-3-8,下午01:09:34 ftang $
 * 
 */
public class HTMLDocGenerator {

    private Map<String, List> targetConnectionMap = null;

    private Map<String, List> sourceConnectionMap = null;

    private Map<String, String> picFilePathMap;

    private List<Map> mapList;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private static Map<String, String> repositoryDBIdAndNameMap;

    private IDesignerCoreService designerCoreService;

    private Map<String, URL> externalNodeHTMLMap = new HashMap<String, URL>();

    public HTMLDocGenerator() {
        designerCoreService = CorePlugin.getDefault().getDesignerCoreService();
        mapList = designerCoreService.getMaps();
        repositoryConnectionItemMap = mapList.get(0);
        repositoryDBIdAndNameMap = mapList.get(1);
    }

    /**
     * This method is used for generating HTML file base on an instance of <code>ExportFileResource</code>
     * 
     * @param resource
     */
    public void generateHTMLFile(ExportFileResource resource) {
        try {

            // Store all pictures' path.
            List<URL> picList = new ArrayList<URL>(5);

            String jobName = resource.getProcess().getProperty().getLabel();

//            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProject().getTechnicalLabel());
//            File file = project.getLocation().toFile();
//            String string = file.toString() + IPath.SEPARATOR + "documentations/generated/jobs" + IPath.SEPARATOR + "." +jobName+ "_doc";
//
//           File folder = new File(string);
//           if(!folder.exists())
//           {
//               folder.mkdir();    
//           }
//           
//           String tempFolderPath = folder.toString();
            String tempFolderPath = checkTempDirIsExists(resource);

            handleXMLFile(resource, tempFolderPath);

            String picFolderPath = checkPicDirIsExists(resource, tempFolderPath);

            final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);

            final URL xslFileUrl = FileLocator.toFileURL(FileLocator
                    .find(b, new Path(IHTMLDocConstants.MAIN_XSL_FILE_PATH), null));
            // final URL logoFileUrl = FileLocator.toFileURL(FileLocator.find(b,
            // new Path(IHTMLDocConstants.LOGO_FILE_PATH), null));

            File logoFile = new File(picFolderPath + File.separatorChar + IHTMLDocConstants.TALEND_LOGO_FILE_NAME);
            saveLogoImage(SWT.IMAGE_JPEG, logoFile);

            String xslFilePath = xslFileUrl.getPath();
            // String logoFilePath = logoFileUrl.getPath();
            // FileCopyUtils.copy(logoFilePath, picFolderPath + File.separatorChar
            // + IHTMLDocConstants.TALEND_LOGO_FILE_NAME);

            picList.add(logoFile.toURL());

            Set keySet = picFilePathMap.keySet();
            for (Object key : keySet) {
                String value = (String) picFilePathMap.get(key);
                FileCopyUtils.copy(value, picFolderPath + File.separatorChar + key);
                picList.add(new File(picFolderPath + File.separatorChar + key).toURL());
            }

            List<URL> resultFiles = parseXML2HTML(tempFolderPath, jobName, xslFilePath);

            addResources(resource, resultFiles);

            resource.addResources(IHTMLDocConstants.PIC_FOLDER_NAME, picList);

            // List<URL> externalList = getExternalHtmlPath();
            // resource.addResources(IHTMLDocConstants.EXTERNAL_FOLDER_NAME, externalList);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.process(e);

        }

        targetConnectionMap = null;
        sourceConnectionMap = null;
    }

    /**
     * Checks if pictures directory is existing.
     * 
     * @param resource
     */
    private static String checkPicDirIsExists(ExportFileResource resource, String tempFolderPath) {
        String picFolderPath = tempFolderPath + File.separator + IHTMLDocConstants.PIC_FOLDER_NAME;
        File file = new File(picFolderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return picFolderPath;

    }

    /**
     * Checks if temporary directory is existing.
     * 
     * @param resource
     * @return
     */
    private String checkTempDirIsExists(ExportFileResource resource) {
        String tempDirPath = HTMLDocUtils.getTmpFolder() + File.separator + resource.getDirectoryName();
        File file = new File(tempDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tempDirPath;
    }

    /**
     * Using xslt to parse the xml to html.
     * 
     * @param jobName
     * @param tempFolderPath
     * @param xslFileName
     * 
     * @return top folder path of this job.
     * @throws Exception
     */
    private List<URL> parseXML2HTML(String tempFolderPath, String jobName, String xslFilePath) throws Exception {
        List<URL> list = new ArrayList<URL>(1);

        String htmlFilePath = tempFolderPath + File.separatorChar + jobName + IHTMLDocConstants.HTML_FILE_SUFFIX;
        String xmlFilePath = tempFolderPath + File.separatorChar + jobName + IHTMLDocConstants.XML_FILE_SUFFIX;
        HTMLHandler.generateHTMLFile(tempFolderPath, xslFilePath, xmlFilePath, htmlFilePath, this.externalNodeHTMLMap);

        File tmpFolder = new File(tempFolderPath);

        File[] files = tmpFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            // Checks if current file is html file or xml file, otherwise ignore it.
            if (!(files[i].isDirectory())) {
                list.add(files[i].toURL());
            }
        }

        return list;
    }

    /**
     * Generates the xml file base on an instance of <code>ExportFileResource</code> and the temporary folder path.
     * 
     * @param resource
     * @param tempFolderPath
     */
    private void handleXMLFile(ExportFileResource resource, String tempFolderPath) throws Exception {
        ProcessItem processItem = resource.getProcess();
        targetConnectionMap = new HashMap<String, List>();
        sourceConnectionMap = new HashMap<String, List>();
        getSourceAndTargetConnection(processItem);

        Document document = DocumentHelper.createDocument();
        Element projectElement = generateProjectInfo(document);

        Element jobElement = generateJobInfo(processItem, projectElement);

        List<List> allList = seperateNodes(processItem);
        List<INode> allComponentsList = allList.get(0);
        List<INode> internalNodeComponentsList = allList.get(1);
        List<INode> externalNodeComponentsList = allList.get(2);

        // Generates information for 'Component List' part in exported HTML file.
        generateAllComponentsSummaryInfo(processItem, jobElement, allComponentsList);

        Element internalNodeElement = jobElement.addElement("internalNodeComponents");
        Element externalNodeElement = jobElement.addElement("externalNodeComponents");

        InternalNodeComponentHandler internalNodeComponentHandler = new InternalNodeComponentHandler(this.picFilePathMap,
                internalNodeElement, internalNodeComponentsList, this.sourceConnectionMap, this.targetConnectionMap,
                this.designerCoreService, this.repositoryConnectionItemMap, this.repositoryDBIdAndNameMap);

        ExternalNodeComponentHandler externalNodeComponentHandler = new ExternalNodeComponentHandler(this.picFilePathMap,
                externalNodeElement, externalNodeComponentsList, this.sourceConnectionMap, this.targetConnectionMap,
                this.designerCoreService, this.repositoryConnectionItemMap, this.repositoryDBIdAndNameMap, externalNodeHTMLMap/*
                                                                                                                                 * ,
                                                                                                                                 * tempFolderPath
                                                                                                                                 */);

        // Generates internal node components information.
        internalNodeComponentHandler.generateComponentInfo();

        // Generates external node components(tMap etc.) information.
        externalNodeComponentHandler.generateComponentInfo();

        // Generates all connection information(include internal node and external node).
        EList connectionList = processItem.getProcess().getConnection();
        if (connectionList != null || connectionList.size() != 0) {
            generateConnectionsInfo(jobElement, connectionList);
        }

        String filePath = tempFolderPath + File.separatorChar + processItem.getProperty().getLabel()
                + IHTMLDocConstants.XML_FILE_SUFFIX;

        XMLHandler.generateXMLFile(tempFolderPath, filePath, document);
    }

    /**
     * Generates all components summary information.
     * 
     * @param processItem
     * 
     * @param inputJobElement
     * @param allComponentsList
     */
    public void generateAllComponentsSummaryInfo(ProcessItem processItem, Element inputJobElement, List<INode> allComponentsList) {
        Element componentNameListElement = null;
        Point screenshotOffset = new Point();

        if (processItem.getProcess().getParameters() != null) {
            List<ElementParameterType> elemParamList = processItem.getProcess().getParameters().getElementParameter();
            for (ElementParameterType curElem : elemParamList) {
                if (curElem.getName().equals(IProcess.SCREEN_OFFSET_X)) {
                    screenshotOffset.x = Integer.valueOf("".equals(HTMLDocUtils.checkString(curElem.getValue())) ? "0" : curElem
                            .getValue());
                } else if (curElem.getName().equals(IProcess.SCREEN_OFFSET_Y)) {
                    screenshotOffset.y = Integer.valueOf("".equals(HTMLDocUtils.checkString(curElem.getValue())) ? "0" : curElem
                            .getValue());
                }
            }
        }

        int x = 0, y = 0, width = 0, height = 0;
        for (INode node : allComponentsList) {
            if (node.getLocation() != null) {
                Point point = node.getLocation();
                x = point.x + screenshotOffset.x;
                y = point.y + screenshotOffset.y;
            }

            ImageData imageData = node.getComponent().getIcon32().getImageData();
            if (imageData != null) {
                width = imageData.width;
                height = imageData.height;
            }

            if (componentNameListElement == null) {
                componentNameListElement = inputJobElement.addElement("componentList");
            }
            Element componentItemElement = null;
            componentItemElement = componentNameListElement.addElement("componentItem");
            componentItemElement.addAttribute("name", node.getUniqueName());
            componentItemElement.addAttribute("link", node.getUniqueName());
            componentItemElement.addAttribute("type", node.getComponent().getName());
            componentItemElement.addAttribute("leftTopX", x + "");
            componentItemElement.addAttribute("leftTopY", y + "");
            componentItemElement.addAttribute("rightBottomX", x + width + "");
            componentItemElement.addAttribute("rightBottomY", y + height + "");
        }
    }

    /**
     * This method is used for seperating all nodes into internal and external.
     * 
     * @param processItem
     * @return
     */
    private List<List> seperateNodes(ProcessItem processItem) {
        IProcess process = CorePlugin.getDefault().getDesignerCoreService().getProcessFromProcessItem(processItem);
        List<INode> graphicalNodeList = (List<INode>) process.getGraphicalNodes();

        List<INode> internalNodeComponentList = new ArrayList<INode>();
        List<INode> externalNodeComponentList = new ArrayList<INode>();
        List<INode> allNodeComponentList = new ArrayList<INode>();
        List<List> componentsList = new ArrayList<List>();
        for (INode node : graphicalNodeList) {
            // If component is not activate, do not need to get it's information
            if (!node.isActivate()) {
                continue;
            }

            allNodeComponentList.add(node);

            if (node.getExternalNode() != null) {
                externalNodeComponentList.add(node);
            } else {
                internalNodeComponentList.add(node);
            }

        }
        componentsList.add(allNodeComponentList);
        componentsList.add(internalNodeComponentList);
        componentsList.add(externalNodeComponentList);
        return componentsList;
    }

    /**
     * Generates connections information base on <code>jobElement</code>,<code>connectionList</code>
     * 
     * @param jobElement
     * @param connectionList
     */
    private void generateConnectionsInfo(Element jobElement, EList connectionList) {
        Element connectionsElement = jobElement.addElement("connections");
        for (int j = 0; j < connectionList.size(); j++) {
            ConnectionType type = (ConnectionType) connectionList.get(j);
            Element connectionElement = connectionsElement.addElement("connection");
            connectionElement.addAttribute("label", HTMLDocUtils.checkString(type.getLabel()));
            connectionElement.addAttribute("lineStyle", HTMLDocUtils.checkString(type.getLineStyle() + ""));
            connectionElement.addAttribute("metaname", HTMLDocUtils.checkString(type.getMetaname()));
            connectionElement.addAttribute("offsetLabelX", HTMLDocUtils.checkString(type.getOffsetLabelX() + ""));
            connectionElement.addAttribute("offsetLabelY", HTMLDocUtils.checkString(type.getOffsetLabelY() + ""));
            connectionElement.addAttribute("source", HTMLDocUtils.checkString(type.getSource()));
            connectionElement.addAttribute("target", HTMLDocUtils.checkString(type.getTarget()));
        }
    }

    /**
     * Generates job(process) information in XML base on <code>ProcessItem</code> and project element.
     * 
     * @param processItem <code>ProcessItem</code>
     * @param projectElement <code>Element</code>
     * @return an instance of <code>Element</code>
     */
    private Element generateJobInfo(ProcessItem processItem, Element projectElement) {

        picFilePathMap = new HashMap<String, String>();
        // IProcess process = CorePlugin.getDefault().getDesignerCoreService().getProcessFromProcessItem(processItem);

        Property property = processItem.getProperty();
        String jobName = property.getLabel();
        Element jobElement = projectElement.addElement("job");
        jobElement.addAttribute("name", HTMLDocUtils.checkString(jobName));

        jobElement.addAttribute("author", HTMLDocUtils.checkString(property.getAuthor().toString()));
        jobElement.addAttribute("version", HTMLDocUtils.checkString(property.getVersion()));
        jobElement.addAttribute("purpose", HTMLDocUtils.checkString(property.getPurpose()));
        jobElement.addAttribute("status", HTMLDocUtils.checkString(property.getStatusCode()));
        jobElement.addAttribute("description", HTMLDocUtils.checkString(property.getDescription()));

        jobElement.addAttribute("creation", HTMLDocUtils.checkDate(property.getCreationDate()));
        jobElement.addAttribute("modification", HTMLDocUtils.checkDate(property.getModificationDate()));

        String picName = jobName + IHTMLDocConstants.JOB_PREVIEW_PIC_SUFFIX;
        IPath filePath = RepositoryPathProvider.getPathFileName(RepositoryConstants.IMG_DIRECTORY_OF_JOB_OUTLINE, picName);
        String filePathStr = filePath.toOSString();
        File file = new File(filePathStr);
        if (file.exists()) {
            Element previewElement = jobElement.addElement("preview");
            previewElement.addAttribute("picture", IHTMLDocConstants.PICTUREFOLDERPATH + picName);
            picFilePathMap.put(picName, filePathStr);
        }
        return jobElement;
    }

    /**
     * Generates project element information in XML file.
     * 
     * @param document <code>Document</code>
     * @return an instance of <code>Element</code>
     */
    private Element generateProjectInfo(Document document) {
        Element projectElement = document.addElement("project");
        projectElement.addAttribute("name", getProject().getLabel());
        projectElement.addAttribute("logo", IHTMLDocConstants.PICTUREFOLDERPATH + IHTMLDocConstants.TALEND_LOGO_FILE_NAME);
        projectElement.addAttribute("title", IHTMLDocConstants.TITLE_GEN + getFullProductName());
        projectElement.addAttribute("link", IHTMLDocConstants.WEBSITE_LINK);
        projectElement.addAttribute("language", getProject().getLanguage().getName());
        projectElement.addAttribute("description", getProject().getDescription());
        projectElement.addAttribute("generatedDate", DateFormat.getDateTimeInstance().format(new Date()));
        projectElement.addAttribute("versionName", getProductVersionName());
        projectElement.addAttribute("version", getCurrentTOSVersion());
        return projectElement;
    }

    /**
     * Add resources.
     * 
     * @param resource <code>ExportFileResource</code>
     * @param resultFiles a <code>List</code> of <code>URL</code>
     */
    private void addResources(ExportFileResource resource, List<URL> resultFiles) {
        resource.addResources(resultFiles);
    }

    /**
     * Get the current project.
     * 
     * @return an instance of <code>Project</code>
     */
    private Project getProject() {
        return ((org.talend.core.context.RepositoryContext) CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject();
    }

    /**
     * Get source connections and target connections base on given <code>ProcessItem</code>.
     * 
     * @param processItem ProcessItem
     */
    private void getSourceAndTargetConnection(ProcessItem processItem) {
        EList connectionList = processItem.getProcess().getConnection();

        List<String> targetList = new ArrayList<String>();
        List<String> sourceList = new ArrayList<String>();

        if (connectionList != null || connectionList.size() != 0) {
            for (int j = 0; j < connectionList.size(); j++) {
                ConnectionType type = (ConnectionType) connectionList.get(j);
                if (!targetConnectionMap.containsKey(type.getSource())) {
                    targetList = new ArrayList<String>();
                }
                if (!targetList.contains(type.getTarget())) {
                    targetList.add(type.getTarget());
                }

                targetConnectionMap.put(type.getSource(), targetList);

                if (!sourceConnectionMap.containsKey(type.getTarget())) {
                    sourceList = new ArrayList<String>();
                }
                sourceList.add(type.getSource());
                sourceConnectionMap.put(type.getTarget(), sourceList);
            }
        }
    }

    /**
     * This method is used for generating current T.O.S version.
     * 
     * @return
     */
    private String getCurrentTOSVersion() {
        String currentVersion = IHTMLDocConstants.UNKNOWN;
        currentVersion = (String) RepositoryPlugin.getDefault().getBundle().getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION);
        return currentVersion;
    }

    /**
     * 
     * DOC ggu Comment method "getExternalHtmlPath".<br>
     * 
     * add external Doc file list
     * 
     * @return
     * @throws MalformedURLException
     */
    private List<URL> getExternalHtmlPath() throws MalformedURLException {
        List<URL> externalList = new ArrayList<URL>();

        Set keySet = externalNodeHTMLMap.keySet();
        for (Object key : keySet) {
            URL html = externalNodeHTMLMap.get(key);
            if (html != null) {
                externalList.add(html);// html

                String htmlStr = html.toString();
                String xmlStr = htmlStr.substring(0, htmlStr.lastIndexOf(IHTMLDocConstants.HTML_FILE_SUFFIX))
                        + IHTMLDocConstants.XML_FILE_SUFFIX;
                externalList.add(new URL(xmlStr));// xml
            }

        }

        return externalList;
    }

    /**
     * 
     * DOC ggu Comment method "getProductName".
     * 
     * @return
     */
    private String getFullProductName() {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);

        return brandingService.getFullProductName();
    }

    private String getProductVersionName() {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);

        return brandingService.getShortProductName() + IHTMLDocConstants.VERSION;
    }

    private void saveLogoImage(int type, File file) throws IOException {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        ImageData imageData = brandingService.getLoginHImage().getImageData();
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.data = new ImageData[] { imageData };

        imageLoader.save(result, type);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(result.toByteArray());
        fos.close();

    }
}
