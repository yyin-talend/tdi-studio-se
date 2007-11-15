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
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.axis.wsdl.Java2WSDL;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.ui.utils.JavaResourcesHelper;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * DOC x class global comment. Detailled comment <br/>
 * 
 */
public class JobJavaScriptsWSManager extends JobJavaScriptsManager {

    public static final String EXPORT_METHOD = "runJob"; //$NON-NLS-1$

    private static List<String> axisLib = new ArrayList<String>();
    static {
        axisLib.add("axis.jar"); //$NON-NLS-1$
        axisLib.add("jaxrpc.jar"); //$NON-NLS-1$
        axisLib.add("saaj.jar"); //$NON-NLS-1$
        axisLib.add("wsdl4j-1.5.1.jar"); //$NON-NLS-1$
        axisLib.add("commons-discovery-0.2.jar"); //$NON-NLS-1$
        axisLib.add("commons-discovery-0.2.jar"); //$NON-NLS-1$
        axisLib.add("commons-logging-1.0.4.jar"); //$NON-NLS-1$
        axisLib.add("mail.jar"); //$NON-NLS-1$
        axisLib.add("activation.jar"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager#getExportResources
     * (org.talend.repository.ui.wizards.exportjob.ExportFileResource[], java.util.Map, java.lang.String,
     * java.lang.String, int, int, java.lang.String[])
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Boolean> exportChoice, String contextName, String launcher, int statisticPort,
            int tracePort, String... codeOptions) {

        List<ExportFileResource> list = new ArrayList<ExportFileResource>();

        boolean needJob = true;
        boolean needSource = BooleanUtils.isTrue(exportChoice.get(ExportChoice.needSource));
        boolean needContext = BooleanUtils.isTrue(exportChoice.get(ExportChoice.needContext));
        ExportFileResource libResource = new ExportFileResource(null, "WEB-INF/lib"); //$NON-NLS-1$
        ExportFileResource contextResource = new ExportFileResource(null, "WEB-INF/classes"); //$NON-NLS-1$
        ExportFileResource srcResource = new ExportFileResource(null, "WEB-INF"); //$NON-NLS-1$
        if (needJob) {
            list.add(libResource);
        }
        if (needContext) {
            list.add(contextResource);
        }
        if (needSource) {
            list.add(srcResource);
        }

        copyServerConfigFileToTempDir();

        // Gets talend libraries
        List<URL> talendLibraries = getExternalLibraries(true, process);
        libResource.addResources(talendLibraries);

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();
            // generate the source files
            String libPath = calculateLibraryPathFromDirectory(process[i].getDirectoryName());
            // use character @ as temporary classpath separator, this one will be replaced during the export.
            String standardJars = libPath + PATH_SEPARATOR + SYSTEMROUTINE_JAR
                    + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + libPath + PATH_SEPARATOR + USERROUTINE_JAR
                    + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + "."; //$NON-NLS-1$
            ProcessorUtilities.setExportConfig("java", standardJars, libPath); //$NON-NLS-1$

            if (!BooleanUtils.isTrue(exportChoice.get(ExportChoice.doNotCompileCode))) {
                generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                        tracePort != IProcessor.NO_TRACES, BooleanUtils.isTrue(exportChoice
                                .get(ExportChoice.applyToChildren)));
            }
            // generate the WSDL file
            ExportFileResource wsdlFile = getWSDLFile(processItem, BooleanUtils.isTrue(exportChoice
                    .get(ExportChoice.needWSDL)), talendLibraries);
            list.add(wsdlFile);

            // edit the WSDD file
            editWSDDFile(processItem);

            // add children jobs
            boolean needChildren = true;
            addSubJobResources(processItem, needChildren, exportChoice, libResource, contextResource, srcResource);

            // generate the context file
            getContextScripts(processItem, needContext, contextResource);

            // generate jar file for job
            libResource.addResources(getJobScripts(processItem, needJob));
        }

        // generate Server Config file
        ExportFileResource serverConfigFile = getServerConfigFile(BooleanUtils.isTrue(exportChoice
                .get(ExportChoice.needCONFIGFILE)));
        list.add(serverConfigFile);

        // generate the WSDD file
        ExportFileResource wsddFile = getWSDDFile(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needWSDD)));
        list.add(wsddFile);

        // generate the WEB-INFO folder
        ExportFileResource webInfoFolder = getWebXMLFile(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needWEBXML)));
        list.add(webInfoFolder);

        // generate the META-INFO folder
        ExportFileResource metaInfoFolder = genMetaInfoFolder(BooleanUtils.isTrue(exportChoice
                .get(ExportChoice.needMetaInfo)));
        list.add(metaInfoFolder);

        // Gets system routines
        List<URL> systemRoutineList = getSystemRoutine(true);
        libResource.addResources(systemRoutineList);
        // Gets user routines
        List<URL> userRoutineList = getUserRoutine(true);
        libResource.addResources(userRoutineList);

        // Gets axis libraries
        List<URL> axisLibList = getAxisLib(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needAXISLIB)));
        libResource.addResources(axisLibList);

        // check the list avoid duplication

        return list;
    }

    private void addSubJobResources(ProcessItem process, boolean needChildren, Map<ExportChoice, Boolean> exportChoice,
            ExportFileResource libResource, ExportFileResource contextResource, ExportFileResource srcResource) {

        List<String> list = new ArrayList<String>();

        if (needChildren) {
            String projectName = getCurrentProjectName();
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName,
                        processedJob, srcResource, exportChoice);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String jobName = iter.next();
            libResource.addResources(getJobScripts(jobName, true));
            addContextScripts(jobName, contextResource, true);
        }

    }

    private void copyServerConfigFileToTempDir() {
        final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
        String sourceFileName;
        try {
            sourceFileName = FileLocator.toFileURL(FileLocator.find(b, new Path("resources/server-config.wsdd"), null)) //$NON-NLS-1$
                    .getFile();
            String targetFileName = getTmpFolder() + PATH_SEPARATOR + "server-config.wsdd"; //$NON-NLS-1$
            FilesUtils.copyFile(new File(sourceFileName), new File(targetFileName));
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

    }

    protected void getContextScripts(ProcessItem processItem, Boolean needContext, ExportFileResource contextResource) {
        String jobName = processItem.getProperty().getLabel();
        addContextScripts(jobName, contextResource, needContext);
    }

    protected List<URL> getAxisLib(Boolean needAxisLib) {
        List<URL> list = new ArrayList<URL>();
        if (!needAxisLib) {
            return list;
        }

        try {
            ILibrariesService librariesService = CorePlugin.getDefault().getLibrariesService();
            String path = librariesService.getLibrariesPath();
            // Gets all the jar files
            File file = new File(path);
            File[] files = file.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar") || name.toLowerCase().endsWith(".properties") //$NON-NLS-1$ //$NON-NLS-2$
                            || name.toLowerCase().endsWith(".zip") ? true : false; //$NON-NLS-1$
                }
            });

            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                try {
                    if (axisLib.contains(tempFile.getName())) {
                        list.add(tempFile.toURL());
                    }
                } catch (MalformedURLException e) {
                    ExceptionHandler.process(e);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    /**
     * DOC x Comment method "genWSDLFolder".
     * 
     * @param list
     */
    private ExportFileResource getWSDLFile(ProcessItem processItem, Boolean needWSDL, List<URL> externalLibs) {
        ExportFileResource wsdl = new ExportFileResource(null, "wsdl"); //$NON-NLS-1$

        List<URL> wsdlUrlList = new ArrayList<URL>();
        try {
            String projectName = getCurrentProjectName();
            String jobName = processItem.getProperty().getLabel();
            String jobFolderName = JavaResourcesHelper.getJobFolderName(escapeFileNameSpace(processItem));

            String classRoot = getClassRootLocation();

            String wsdlFilePath = getTmpFolder() + PATH_SEPARATOR + jobName + ".wsdl"; //$NON-NLS-1$
            String classFileName = classRoot + PATH_SEPARATOR + projectName + PATH_SEPARATOR + jobFolderName
                    + PATH_SEPARATOR + jobName + ".class"; //$NON-NLS-1$

            File classFile = new File(classFileName);
            if (!classFile.exists()) {
                return wsdl;
            }

            StringBuffer libPaths = new StringBuffer();
            if (externalLibs != null) {
                for (URL libUrl : externalLibs) {
                    libPaths.append(libUrl.getFile());
                    libPaths.append(";");
                }
            }

            TalendJava2WSDL.generateWSDL(new String[] { "-T1.2", "-yDOCUMENT", "-uLITERAL", "-o" + wsdlFilePath, "-d", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    "-l http://localhost:8080/" + jobName, "-X" + classRoot + ";" + libPaths, "-m" + EXPORT_METHOD, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    projectName + "." + jobFolderName + "." + jobName }); //$NON-NLS-1$ //$NON-NLS-2$

            wsdlUrlList.add(new File(wsdlFilePath).toURL());

            if (!needWSDL) {
                return wsdl;
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        wsdl.addResources(wsdlUrlList);
        return wsdl;
    }

    private ExportFileResource getWSDDFile(Boolean needWSDD) {
        ExportFileResource wsdd = new ExportFileResource(null, "wsdd"); //$NON-NLS-1$

        if (!needWSDD) {
            return wsdd;
        }

        List<URL> wsddUrlList = new ArrayList<URL>();
        try {
            String projectName = getCurrentProjectName();

            String deployFile = getTmpFolder() + PATH_SEPARATOR + projectName;
            File file = new File(deployFile);
            if (file.exists()) {
                wsddUrlList.add(file.toURL());
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        wsdd.addResources(wsddUrlList);
        return wsdd;
    }

    private ExportFileResource getWebXMLFile(Boolean needWebXMLFile) {
        // generate the web.xml file
        ExportFileResource webInfo = new ExportFileResource(null, "WEB-INF"); //$NON-NLS-1$

        if (!needWebXMLFile) {
            return webInfo;
        }

        List<URL> urlList = new ArrayList<URL>();
        final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
        try {

            URL webFileUrl = FileLocator.toFileURL(FileLocator.find(b, new Path("resources/web.xml"), null)); //$NON-NLS-1$
            urlList.add(webFileUrl);

        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        webInfo.addResources(urlList);
        return webInfo;
    }

    /**
     * DOC x Comment method "genWebInfoForder".
     * 
     * @param list
     */
    private void editWSDDFile(ProcessItem processItem) {
        String projectName = getCurrentProjectName();
        String jobName = processItem.getProperty().getLabel();

        String deployFileName = getTmpFolder() + PATH_SEPARATOR + projectName + PATH_SEPARATOR + jobName
                + PATH_SEPARATOR + "deploy.wsdd"; //$NON-NLS-1$
        String serverConfigFile = getTmpFolder() + PATH_SEPARATOR + "server-config.wsdd"; //$NON-NLS-1$

        File deployFile = new File(deployFileName);
        if (!deployFile.exists()) {
            return;
        }
        // edit the server-config.wsdd file
        try {

            File wsddFile = new File(serverConfigFile);
            BufferedReader reader = new BufferedReader(new FileReader(wsddFile));

            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(reader);

            BufferedReader wsdlreader = new BufferedReader(new FileReader(deployFile));
            SAXReader wsdlsaxReader = new SAXReader();
            Document wsdldoc = wsdlsaxReader.read(wsdlreader);
            Element wsdlroot = wsdldoc.getRootElement();
            Node node = null;
            int count = wsdlroot.nodeCount();
            for (int j = 0; j < count; j++) {
                Node nodeTemp = wsdlroot.node(j);
                if (nodeTemp instanceof Element) {
                    if (nodeTemp.getName().equals("service")) { //$NON-NLS-1$
                        node = nodeTemp;
                    }
                }
            }

            Element root = doc.getRootElement();
            List<Node> content = root.content();
            for (int i = 0; i < content.size(); i++) {
                Node n = content.get(i);
                if (n instanceof Element) {
                    if (n.getName().equals("transport")) { //$NON-NLS-1$
                        content.add(i - 1, node);
                        break;
                    }
                }
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(serverConfigFile),
                    "UTF-8")); //$NON-NLS-1$

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter output = new XMLWriter(writer, format);
            output.write(doc);
            output.flush();
            output.close();

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private ExportFileResource getServerConfigFile(Boolean needConfigFile) {
        ExportFileResource webInfo = new ExportFileResource(null, "WEB-INF"); //$NON-NLS-1$

        if (!needConfigFile) {
            return webInfo;
        }

        String serverConfigFile = getTmpFolder() + PATH_SEPARATOR + "server-config.wsdd"; //$NON-NLS-1$

        List<URL> urlList = new ArrayList<URL>();

        try {
            urlList.add(new File(serverConfigFile).toURL());
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        }

        webInfo.addResources(urlList);

        return webInfo;
    }

    /**
     * DOC x Comment method "genMetaInfoForder".
     * 
     * @param list
     * @return
     */
    private ExportFileResource genMetaInfoFolder(Boolean needMetaInfo) {
        ExportFileResource metaInfoResource = new ExportFileResource(null, "META-INF"); //$NON-NLS-1$
        if (!needMetaInfo) {
            return metaInfoResource;
        }

        // generate the MANIFEST.MF file in the temp folder
        String manifestPath = getTmpFolder() + PATH_SEPARATOR + "MANIFEST.MF"; //$NON-NLS-1$

        Manifest manifest = new Manifest();
        Map<String, Attributes> m = manifest.getEntries();
        Attributes a = new Attributes();
        a.put(Attributes.Name.IMPLEMENTATION_VERSION, "1.0"); //$NON-NLS-1$
        a.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Talend Open Studio"); //$NON-NLS-1$
        m.put("talendWebService", a); //$NON-NLS-1$
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(manifestPath);
            manifest.write(fos);
        } catch (FileNotFoundException e1) {
            ExceptionHandler.process(e1);
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        List<URL> urlList = new ArrayList<URL>();
        try {
            urlList.add(new File(manifestPath).toURL());
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        }
        metaInfoResource.addResources(urlList);

        return metaInfoResource;
    }

    /**
     * DOC x Comment method "main".
     * 
     * @param args
     */
    static class TalendJava2WSDL extends Java2WSDL {

        public static void generateWSDL(String[] args) {
            TalendJava2WSDL java2WSDL = new TalendJava2WSDL();
            java2WSDL.run(args);
        }

    }

}
