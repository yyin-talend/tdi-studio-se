// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.designer.runprocess.LastGenerationInfo;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class JobJavaScriptOSGIForESBManager extends JobJavaScriptsManager {

	public JobJavaScriptOSGIForESBManager(
			Map<ExportChoice, Object> exportChoiceMap, String contextName,
			String launcher, int statisticPort, int tracePort) {
		super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
	}

	private static final String PACKAGE_SEPARATOR = ".";

	private static final String JAVA = "java";

	private static final String ROUTE = "route";

	private static final String JOB = "job";

	private static Logger logger = Logger
			.getLogger(JobJavaScriptOSGIForESBManager.class);

	private static final String BLUEPRINT = "blueprint"; //$NON-NLS-1$

	private static final String OSGI_INF = "OSGI-INF"; //$NON-NLS-1$

	private String jobName;

	private String jobClassName;

	private String jobVersion;

	private String itemType = null;

	public List<ExportFileResource> getExportResources(
			ExportFileResource[] process, String... codeOptions)
			throws ProcessorException {
		List<ExportFileResource> list = new ArrayList<ExportFileResource>();

		boolean needJob = true;
		ExportFileResource libResource = new ExportFileResource(null,
				LIBRARY_FOLDER_NAME); //$NON-NLS-1$
		ExportFileResource osgiResource = getOsgiResource();
		ExportFileResource jobScriptResource = new ExportFileResource(null, ""); //$NON-NLS-1$

		List<ProcessItem> itemToBeExport = new ArrayList<ProcessItem>();

		if (needJob) {
			list.add(libResource);
		}
		list.add(osgiResource);
		list.add(jobScriptResource);

		// set export config mode now only to be sure that the libraries will be
		// setup for an export mode, and not
		// editor mode.
		ProcessorUtilities.setExportConfig(JAVA, "", ""); //$NON-NLS-1$

		// Gets talend libraries

		Set<String> neededLibraries = null;
		for (int i = 0; i < process.length; i++) {
			ProcessItem processItem = (ProcessItem) process[i].getItem();
			itemToBeExport.add(processItem);
			jobName = processItem.getProperty().getLabel();
			jobClassName = getPackageName(processItem) + PACKAGE_SEPARATOR
					+ jobName;

			jobVersion = processItem.getProperty().getVersion();
			if (!isMultiNodes() && this.getSelectedJobVersion() != null) {
				jobVersion = this.getSelectedJobVersion();
			}
			ERepositoryObjectType type = ERepositoryObjectType
					.getItemType(processItem);
			if (type.equals(ERepositoryObjectType.PROCESS)) {
				itemType = JOB;
			} else {
				itemType = ROUTE;
			}
			boolean esbJob = JOB.equals(itemType) && isESBJob(processItem);

			// generate the source files
			String libPath = calculateLibraryPathFromDirectory(process[i]
					.getDirectoryName());
			// use character @ as temporary classpath separator, this one will
			// be replaced during the export.
			String standardJars = libPath + PATH_SEPARATOR + SYSTEMROUTINE_JAR
					+ ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR
					+ libPath + PATH_SEPARATOR + USERROUTINE_JAR
					+ ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR
					+ PACKAGE_SEPARATOR; //$NON-NLS-1$

			// Add additional route dependencies jars LiXiaopeng 2011-9-22
			if (itemType.equals(ROUTE)) {
				String addtionalLibPath = computeAddtionalLibPath(processItem);
				standardJars += addtionalLibPath;
			}
			ProcessorUtilities.setExportConfig(JAVA, standardJars, libPath); //$NON-NLS-1$

			if (!isOptionChoosed(ExportChoice.doNotCompileCode)) {
				if (neededLibraries == null) {
					neededLibraries = new HashSet<String>();
				}
				generateJobFiles(processItem, contextName, jobVersion,
						statisticPort != IProcessor.NO_STATISTICS,
						tracePort != IProcessor.NO_TRACES,
						isOptionChoosed(ExportChoice.applyToChildren),
						true /* isExportAsOSGI */, progressMonitor);
				neededLibraries.addAll(LastGenerationInfo.getInstance()
						.getModulesNeededWithSubjobPerJob(
								processItem.getProperty().getId() + "-osgi",
								jobVersion));
			} else {
				LastGenerationInfo.getInstance()
						.setModulesNeededWithSubjobPerJob(
								processItem.getProperty().getId() + "-osgi",
								processItem.getProperty().getVersion(),
								neededLibraries);
				LastGenerationInfo.getInstance().setLastMainJob(null);
			}

			// generate jar file for job
			getJobScriptsUncompressed(jobScriptResource, processItem);

			// dynamic db xml mapping
			addXmlMapping(process[i],
					isOptionChoosed(ExportChoice.needSourceCode));

			List<String> esbFiles = generateESBFiles(process[i].getItem(),
					esbJob);

			List<URL> urlList = new ArrayList<URL>();
			try {
				for (String file : esbFiles) {
					urlList.add(new File(file).toURL());
				}
			} catch (MalformedURLException e) {
				ExceptionHandler.process(e);
				logger.error(e);
			}
			osgiResource.addResources(getOSGIInfFolder(), urlList);

		}

		// Gets talend libraries
		List<URL> talendLibraries = getExternalLibraries(true, process,
				neededLibraries);
		libResource.addResources(talendLibraries);

		// Gets system routines
		List<URL> systemRoutineList = getSystemRoutine(process, true);
		libResource.addResources(systemRoutineList);
		// Gets user routines
		List<URL> userRoutineList = getUserRoutine(process, true);
		libResource.addResources(userRoutineList);

		// generate the META-INFO folder
		ExportFileResource metaInfoFolder = genMetaInfoFolder(libResource,
				itemToBeExport);
		list.add(0, metaInfoFolder);

		return list;
	}

	/**
	 * 
	 * Add additional dependency libraries.
	 * 
	 * @param processItem
	 * @param libPath
	 * @return
	 */
	private List<URL> computeAddtionalLibs(ProcessItem processItem,
			IPath libPath) {
		List<File> libFiles = new ArrayList<File>();
		ProcessType processType = processItem.getProcess();
		for (Object o : processType.getNode()) {
			if (o instanceof NodeType) {
				NodeType currentNode = (NodeType) o;
				String componentName = currentNode.getComponentName();
				if ("cMessagingEndpoint".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("HOTLIBS".equals(p.getName())) {
							for (Object pv : p.getElementValue()) {
								ElementValueType evt = (ElementValueType) pv;
								String evtValue = evt.getValue();
								IPath path = libPath.append(evtValue);
								libFiles.add(path.toFile());
							}
						}
					}
				}
				if ("cConfig".equals(componentName)
						|| "cJMS".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("DRIVER_JAR".equals(p.getName())) {
							for (Object pv : p.getElementValue()) {
								ElementValueType evt = (ElementValueType) pv;
								String evtValue = evt.getValue();
								IPath path = libPath.append(evtValue);
								libFiles.add(path.toFile());
							}
						}
					}
				}
				// Deal with cTalendJob. LiXiaopeng 2011-9-19 TESB 3121
				if ("cTalendJob".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("LIBRARY".equals(p.getName())) {
							String evtValue = p.getValue();
							evtValue = unquotes(evtValue);
							IPath path = libPath.append(evtValue);
							libFiles.add(path.toFile());
						}
					}
				}
			}
		}

		Set<URL> list = new HashSet<URL>();
		try {
			for (File lib : libFiles) {
				URL url = lib.toURL();
				list.add(url);
			}

		} catch (Exception e) {
			ExceptionHandler.process(e);
		}
		return new ArrayList<URL>(list);
	}

	/**
	 * 
	 * Ensure that the string is not surrounded by quotes.
	 * 
	 * @param string
	 * @return
	 */
	protected String unquotes(String string) {
		String result = string;
		if (result.startsWith("\"")) {
			result = result.substring(1);
		}

		if (result.endsWith("\"")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * This method will return <code>true</code> if given job contains
	 * tESBProviderRequest or tESBConsumer component
	 * 
	 * @param processItem
	 * @author rzubairov
	 * @return
	 */
	private boolean isESBJob(ProcessItem processItem) {
		boolean result = false;
		ProcessType processType = processItem.getProcess();
		for (Object o : processType.getNode()) {
			if (o instanceof NodeType) {
				String componentName = ((NodeType) o).getComponentName();
				if ("tESBRProviderRequest".equals(componentName)
						|| "tESBConsumer".equals(componentName)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Add user input dependency library path. DOC LiXP Comment method
	 * "computeAddtionalLibPath".
	 * 
	 * @param processItem
	 * @return
	 */
	private String computeAddtionalLibPath(ProcessItem processItem) {
		StringBuffer sb = new StringBuffer();
		ProcessType processType = processItem.getProcess();
		for (Object o : processType.getNode()) {
			if (o instanceof NodeType) {
				NodeType currentNode = (NodeType) o;
				String componentName = currentNode.getComponentName();
				if ("cMessagingEndpoint".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("HOTLIBS".equals(p.getName())) {
							for (Object pv : p.getElementValue()) {
								ElementValueType evt = (ElementValueType) pv;
								String evtValue = evt.getValue();
								sb.append(evtValue);
								sb.append(ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR);
							}
						}
					}
				}
				if ("cConfig".equals(componentName)
						|| "cJMS".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("DRIVER_JAR".equals(p.getName())) {
							for (Object pv : p.getElementValue()) {
								ElementValueType evt = (ElementValueType) pv;
								String evtValue = evt.getValue();
								sb.append(evtValue);
								sb.append(ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR);
							}
						}
					}
				}
				if ("cTalendJob".equals(componentName)) {
					for (Object e : currentNode.getElementParameter()) {
						ElementParameterType p = (ElementParameterType) e;
						if ("LIBRARY".equals(p.getName())) {
							for (Object pv : p.getElementValue()) {
								ElementValueType evt = (ElementValueType) pv;
								String evtValue = evt.getValue();
								sb.append(evtValue);
								sb.append(ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR);
							}
						}
					}
				}
			}
		}
		return sb.toString();
	}

	protected ExportFileResource getOsgiResource() {
		return new ExportFileResource(null, ""); //$NON-NLS-1$;
	}

	private String getPackageName(ProcessItem processItem) {
		return JavaResourcesHelper.getProjectFolderName(processItem)
				+ PACKAGE_SEPARATOR
				+ JavaResourcesHelper.getJobFolderName(processItem
						.getProperty().getLabel(), processItem.getProperty()
						.getVersion());
	}

	protected List<String> generateESBFiles(Item processItem, boolean isESBJob) {
		List<String> files = new ArrayList<String>();
		final Bundle b = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
		try {
			if (itemType == null)
				itemType = JOB;
			String inputFile = FileLocator.toFileURL(
					FileLocator.find(b, new Path(
							"resources/" + itemType + "-template.xml"), null)) //$NON-NLS-1$
					.getFile();
			String targetFile = getTmpFolder() + PATH_SEPARATOR + "job.xml"; //$NON-NLS-1$

			readAndReplaceInRouteXmlTemplate(processItem, inputFile,
					targetFile, jobName, jobClassName, itemType, isESBJob);
			files.add(targetFile);
		} catch (IOException e) {
			ExceptionHandler.process(e);
		}
		return files;
	}

	protected ElementParameterType findElementParameterByName(String paramName,
			EList<?> elementParameterTypes) {
		for (Object obj : elementParameterTypes) {
			ElementParameterType cpType = (ElementParameterType) obj;
			if (paramName.equals(cpType.getName())) {
				return cpType;
			}
		}
		return null;
	}

	protected boolean computeCheckElementValue(String paramName,
			EList<?> elementParameterTypes) {
		ElementParameterType cpType = findElementParameterByName(paramName,
				elementParameterTypes);
		if (cpType == null) {
			return false;
		}
		String isNone = cpType.getValue();
		return "true".equals(isNone);
	}

	/**
	 * 
	 * Read and replace ESB feature for CXF if there is some cCXF component.
	 * 
	 * @param processItem
	 * @param inputFile
	 * @param targetFile
	 * @param jobName
	 * @param jobClassName
	 * @param itemType
	 * @param isESBJob
	 */
	private void readAndReplaceInRouteXmlTemplate(Item processItem,
			String inputFile, String targetFile, String jobName,
			String jobClassName, String itemType, boolean isESBJob) {

		// http://jira.talendforge.org/browse/TESB-3677
		boolean hasSL = false;
		boolean hasSAM = false;

		if (ROUTE.equals(itemType)) {

			ProcessType process = ((ProcessItem) processItem).getProcess();
			if (process != null) {
				EList nodes = process.getNode();
				Iterator iterator = nodes.iterator();

				while (iterator.hasNext()) {
					NodeType next = (NodeType) iterator.next();

					if ("cCXF".equals(next.getComponentName())) {
						if (!hasSL) {
							hasSL = computeCheckElementValue("ENABLE_SL",
									next.getElementParameter());
						}
						if (!hasSAM) {
							hasSAM = computeCheckElementValue("ENABLE_SAM",
									next.getElementParameter());
						}
					}

					if (hasSL && hasSAM) {
						break;
					}

				}
			}
		}

		FileReader fr = null;
		String additionalJobIF = "<value>routines.system.api.TalendESBJob</value>";
		if (!isESBJob) {
			additionalJobIF = "";
		}
		try {
			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fw = new FileWriter(targetFile);
			BufferedWriter bw = new BufferedWriter(fw);

			String line = br.readLine();
			while (line != null) {
				line = line
						.replace("@JOBNAME@", jobName).replace("@TYPE@", itemType).replace("@JOBCLASSNAME@", jobClassName).replace("@ADDITIONAL_JOB_INTERFACE@", additionalJobIF); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

				if (hasSL) {
					line = line
							.replace("@ESBSLFeatureProperty@",
									"<property name=\"locatorFeature\" ref=\"locatorFeature\"/>");
					line = line
							.replace(
									"@ESBSLFeaturePropertyRef@",
									"<reference id=\"locatorFeature\" interface=\"org.talend.esb.servicelocator.cxf.LocatorFeature\"/>");
				}
				if (hasSAM) {
					line = line
							.replace("@ESBSAMFeatureProperty@",
									"<property name=\"eventFeature\" ref=\"eventFeature\"/>");
					line = line
							.replace(
									"@ESBSAMFeaturePropertyRef@",
									"<reference id=\"eventFeature\" interface=\"org.talend.esb.sam.agent.feature.EventFeature\"/>");
				}

				line = line.replace("@ESBSLFeatureProperty@", "");
				line = line.replace("@ESBSLFeaturePropertyRef@", "");
				line = line.replace("@ESBSAMFeatureProperty@", "");
				line = line.replace("@ESBSAMFeaturePropertyRef@", "");

				bw.write(line + "\n"); //$NON-NLS-1$
				line = br.readLine();
			}
			bw.flush();
			fr.close();
			fw.close();
		} catch (FileNotFoundException e) {
			ExceptionHandler.process(e);
			logger.error(e);
		} catch (IOException e) {
			ExceptionHandler.process(e);
			logger.error(e);
		}

	}

	private String getOSGIInfFolder() {
		return OSGI_INF.concat(PATH_SEPARATOR).concat(BLUEPRINT);
	}

	protected void readAndReplaceInXmlTemplate(String inputFile,
			String outputFile, String jobName, String jobClassName,
			String itemType, boolean isESBJob) {
		FileReader fr = null;
		String additionalJobIF = "<value>routines.system.api.TalendESBJob</value>";
		if (!isESBJob) {
			additionalJobIF = "";
		}
		try {
			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fw = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(fw);

			String line = br.readLine();
			while (line != null) {
				line = line
						.replace("@JOBNAME@", jobName).replace("@TYPE@", itemType).replace("@JOBCLASSNAME@", jobClassName).replace("@ADDITIONAL_JOB_INTERFACE@", additionalJobIF); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				bw.write(line + "\n"); //$NON-NLS-1$
				line = br.readLine();
			}
			bw.flush();
			fr.close();
			fw.close();
		} catch (FileNotFoundException e) {
			ExceptionHandler.process(e);
			logger.error(e);
		} catch (IOException e) {
			ExceptionHandler.process(e);
			logger.error(e);
		}
	}

	protected void getContextScripts(ProcessItem processItem,
			Boolean needContext, ExportFileResource contextResource,
			String version) {
		String jobName = processItem.getProperty().getLabel();
		addContextScripts(processItem, jobName, version, contextResource,
				needContext);
	}

	protected ExportFileResource genMetaInfoFolder(
			ExportFileResource libResource, List<ProcessItem> itemToBeExport) {
		ExportFileResource metaInfoResource = new ExportFileResource(null,
				"META-INF"); //$NON-NLS-1$

		// generate the MANIFEST.MF file in the temp folder
		String manifestPath = getTmpFolder() + PATH_SEPARATOR + "MANIFEST.MF"; //$NON-NLS-1$

		FileOutputStream fos = null;
		try {
			Manifest manifest = getManifest(libResource, itemToBeExport,
					jobName);
			fos = new FileOutputStream(manifestPath);
			manifest.write(fos);
		} catch (FileNotFoundException e1) {
			ExceptionHandler.process(e1);
			logger.error(e1);
		} catch (IOException e1) {
			ExceptionHandler.process(e1);
			logger.error(e1);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					ExceptionHandler.process(e);
					logger.error(e);
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

	protected Manifest getManifest(ExportFileResource libResource,
			List<ProcessItem> itemToBeExport, String bundleName)
			throws IOException {
		Manifest manifest = new Manifest();
		Attributes a = manifest.getMainAttributes();
		a.put(Attributes.Name.MANIFEST_VERSION, "1.0"); //$NON-NLS-1$
		a.put(new Attributes.Name("Bundle-Name"), bundleName); //$NON-NLS-1$
		a.put(new Attributes.Name("Bundle-SymbolicName"), bundleName); //$NON-NLS-1$
		a.put(new Attributes.Name("Bundle-Version"), jobVersion); //$NON-NLS-1$
		a.put(new Attributes.Name("Bundle-ManifestVersion"), "2"); //$NON-NLS-1$ //$NON-NLS-2$
		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (ProcessItem pi : itemToBeExport) {
			sb.append(delim).append(getPackageName(pi));
			delim = ",";
		}
		a.put(new Attributes.Name("Export-Package"), sb.toString()); //$NON-NLS-1$
		if (ROUTE.equals(itemType)) {
			/*
			 * add external import-packages for Activemq
			 */
			// http://jira.talendforge.org/browse/TESB-3624 Xiaopeng Li
			// Add necessary for CXF dependency
			String externalAMQImport = "";
			String externalCXFImport = "";
			for (ProcessItem pi : itemToBeExport) {
				ProcessType process = pi.getProcess();
				if (process == null) {
					continue;
				}
				EList nodes = process.getNode();
				Iterator iterator = nodes.iterator();

				while (iterator.hasNext()) {
					NodeType next = (NodeType) iterator.next();
					if ("cActiveMQ".equals(next.getComponentName())) {
						externalAMQImport = ",javax.jms,org.apache.activemq,org.apache.activemq.camel.component";
						continue;
					}

					if ("cCXF".equals(next.getComponentName())) {
						externalCXFImport = ",org.apache.camel.component.cxf,org.apache.cxf.feature,"
								+ "org.talend.esb.servicelocator.cxf;version=\"[2.0.0,6.0.0)\","
								+ "org.talend.esb.sam.agent.feature;version=\"[2.0.0,6.0.0)\"";
						continue;
					}

				}
			}
			// end add
			a.put(new Attributes.Name("Require-Bundle"),
					"org.apache.camel.camel-core");
			a.put(new Attributes.Name("Import-Package"), "javax.xml.bind,org.apache.camel;version=\"[2.7,3)\",org.apache.camel.builder;" + //$NON-NLS-1$
							"version=\"[2.7,3)\",org.apache.camel.impl;version=\"[2.7,3)\",org.apache.camel.management;version=\"[2.7,3)\","
							+ //$NON-NLS-1$
							"org.apache.camel.model;version=\"[2.7,3)\",org.apache.camel.osgi;version=\"[2.7,3)\","
							+ //$NON-NLS-1$
							"org.apache.camel.spi;version=\"[2.7,3)\",org.apache.camel.view;version=\"[2.7,3)\","
							+ //$NON-NLS-1$
							"org.osgi.framework;version=\"[1.5,2)\","
							+ //$NON-NLS-1$
							"org.osgi.service.blueprint;version=\"[1.0.0,2.0.0)\",routines.system.api"
							+ externalAMQImport + externalCXFImport); //$NON-NLS-1$
		} else {
			a.put(new Attributes.Name("Import-Package"), //$NON-NLS-1$
					"routines.system.api;resolution:=optional" + //$NON-NLS-1$
							",org.dom4j;resolution:=optional" + //$NON-NLS-1$
							",org.dom4j.io;resolution:=optional" + //$NON-NLS-1$
							",org.dom4j.tree;resolution:=optional" + //$NON-NLS-1$
							",org.jaxen;resolution:=optional" + //$NON-NLS-1$
							",javax.xml.soap;resolution:=optional" + //$NON-NLS-1$
							",javax.xml.ws.soap;resolution:=optional" + //$NON-NLS-1$
							",javax.ws.rs;resolution:=optional" + //$NON-NLS-1$
							",javax.ws.rs.core;resolution:=optional" + //$NON-NLS-1$
							",javax.ws.rs.ext;resolution:=optional"); //$NON-NLS-1$
		}
		if (itemToBeExport != null && !itemToBeExport.isEmpty()) {
			for (ProcessItem pi : itemToBeExport) {
				/*
				 * need to fill bundle depedence informations for every
				 * component,feature 0023460
				 */
				String requiredBundles = caculateDependenciesBundles(pi);
				if (requiredBundles != null && !"".equals(requiredBundles)) {
					a.put(new Attributes.Name("Require-Bundle"),
							requiredBundles);
				}
			}
		}
		if (!libResource.getAllResources().isEmpty()) {
			a.put(new Attributes.Name("Bundle-ClassPath"), getClassPath(libResource)); //$NON-NLS-1$
		}
		a.put(new Attributes.Name("Export-Service"), "routines.system.api.TalendJob;name=" + bundleName + ";type=" + itemType); //$NON-NLS-1$

		return manifest;
	}

	/**
	 * DOC hywang Comment method "caculateDependenciesBundles".
	 * 
	 * @return
	 */
	private String caculateDependenciesBundles(ProcessItem processItem) {
		StringBuffer requiredBundles = new StringBuffer();
		IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister
				.getDefault().getService(IDesignerCoreService.class);
		IProcess fakeProcess = designerCoreService
				.getProcessFromProcessItem(processItem);
		List<? extends INode> generateNodes = fakeProcess.getGeneratingNodes();
		// this list is used to avoid add dumplicated bundle
		List<String> alreadyAddedBundles = new ArrayList<String>();

		List<String> segments = new ArrayList<String>();
		generateBundleSegmemnts(generateNodes, alreadyAddedBundles, segments);
		int index = 0;
		for (String segment : segments) {
			if (index != segments.size() - 1) {
				segment = segment + ",";
			}
			requiredBundles.append(segment);
			index++;
		}
		segments = null;
		alreadyAddedBundles = null;
		return requiredBundles.toString();
	}

	protected void generateBundleSegmemnts(List<? extends INode> generateNodes,
			List<String> alreadyAddedBundles, List<String> segments) {
		for (INode generateNode : generateNodes) {
			List<ModuleNeeded> modelneededForGenerateNode = generateNode
					.getComponent().getModulesNeeded();
			for (ModuleNeeded module : modelneededForGenerateNode) {
				String bundleName = module.getBundleName();
				String bundleVersion = module.getBundleVersion();
				// the last dependence should not contain "," and "\n"
				String bundleToAdd = bundleName;
				if (bundleVersion != null && !"".equals(bundleVersion)) {
					bundleToAdd = bundleName + ";bundle-version="
							+ TalendTextUtils.addQuotes(bundleVersion);
				}

				if (bundleToAdd != null && !"".equals(bundleToAdd)) {
					if (!alreadyAddedBundles.contains(bundleToAdd)) {
						segments.add(bundleToAdd);
						alreadyAddedBundles.add(bundleToAdd);
					}
				}
			}
		}
	}

	private String getClassPath(ExportFileResource libResource) {
		StringBuffer libBuffer = new StringBuffer();
		libBuffer.append(PACKAGE_SEPARATOR).append(","); //$NON-NLS-1$ //$NON-NLS-2$
		Set<String> relativePathList = libResource.getRelativePathList();
		for (String path : relativePathList) {
			Set<URL> resources = libResource.getResourcesByRelativePath(path);
			for (URL url : resources) {
				File currentResource = new File(url.getPath());
				libBuffer.append(
						libResource.getDirectoryName() + PATH_SEPARATOR
								+ currentResource.getName()).append(","); //$NON-NLS-1$
			}
		}
		libBuffer.deleteCharAt(libBuffer.length() - 1);
		return libBuffer.toString();
	}

	@Override
	protected List<URL> getExternalLibraries(boolean needLibraries,
			ExportFileResource[] process, Set<String> neededLibraries) {
		List<URL> list = new ArrayList<URL>();
		if (!needLibraries) {
			return list;
		}
		// jar from routines
		List<IRepositoryViewObject> collectRoutines = new ArrayList<IRepositoryViewObject>();
		boolean useBeans = false;
		if (GlobalServiceRegister.getDefault().isServiceRegistered(
				ICamelDesignerCoreService.class)) {
			ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister
					.getDefault().getService(ICamelDesignerCoreService.class);
			if (camelService.isInstanceofCamel(process[0].getItem())) {
				useBeans = true;
			}
		}
		// Lists all the needed jar files
		Set<String> listModulesReallyNeeded = new HashSet<String>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject prj = root.getProject(JavaUtils.JAVA_PROJECT_NAME);
		IJavaProject project = JavaCore.create(prj);
		IPath libPath = project.getResource().getLocation()
				.append(JavaUtils.JAVA_LIB_DIRECTORY);
		File file = libPath.toFile();
		File[] files = file.listFiles(FilesUtils.getAcceptModuleFilesFilter());

		// Add additional route dependencies jars LiXiaopeng 2011-9-22
		for (ExportFileResource export : process) {
			Item item = export.getItem();
			if (item instanceof ProcessItem) {
				list.addAll(computeAddtionalLibs((ProcessItem) item, libPath));
			}
		}

		if (!useBeans) {
			// Gets all the jar files
			if (neededLibraries == null) {
				// in case export as been done with option "not recompile", then
				// libraires can't be retrieved when
				// build.
				IDesignerCoreService designerService = RepositoryPlugin
						.getDefault().getDesignerCoreService();
				for (int i = 0; i < process.length; i++) {
					ExportFileResource resource = process[i];
					ProcessItem item = (ProcessItem) resource.getItem();
					String version = item.getProperty().getVersion();
					if (!isMultiNodes() && this.getSelectedJobVersion() != null) {
						version = this.getSelectedJobVersion();
					}
					ProcessItem selectedProcessItem;
					if (resource.getNode() != null) {
						selectedProcessItem = ItemCacheManager.getProcessItem(
								resource.getNode().getRoot().getProject(), item
										.getProperty().getId(), version);
					} else {
						// if no node given, take in the current project only
						selectedProcessItem = ItemCacheManager.getProcessItem(
								item.getProperty().getId(), version);
					}
					IProcess iProcess = designerService
							.getProcessFromProcessItem(selectedProcessItem);
					neededLibraries = iProcess.getNeededLibraries(true);
					if (neededLibraries != null) {
						listModulesReallyNeeded.addAll(neededLibraries);
					}
				}
			} else {
				listModulesReallyNeeded.addAll(neededLibraries);
			}
		}

		collectRoutines.addAll(collectRoutines(process, useBeans));

		for (IRepositoryViewObject object : collectRoutines) {
			Item item = object.getProperty().getItem();
			if (item instanceof RoutineItem) {
				RoutineItem routine = (RoutineItem) item;
				EList imports = routine.getImports();
				for (Object o : imports) {
					IMPORTType type = (IMPORTType) o;
					listModulesReallyNeeded.add(type.getMODULE());
				}
			}
		}

		for (int i = 0; i < files.length; i++) {
			File tempFile = files[i];
			try {
				if (listModulesReallyNeeded.contains(tempFile.getName())) {
					list.add(tempFile.toURL());
				}
			} catch (MalformedURLException e) {
				ExceptionHandler.process(e);
			}
		}

		return list;
	}

	@Override
	public void setTopFolder(List<ExportFileResource> resourcesToExport) {
		return;
	}

	@Override
	public String getOutputSuffix() {
		return ".jar";
	}

}
