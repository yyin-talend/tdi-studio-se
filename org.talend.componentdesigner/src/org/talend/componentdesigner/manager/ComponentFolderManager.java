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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

/**
 * @author rli
 * 
 */
public class ComponentFolderManager {

	private static String xmlSUFFIX = ".xml";

	private ComponentPref componentPref;

	private IProject project;

	private String componentFolderName;

	public void generateComponentContent(ComponentPref componentPref,
			IProject project)
			throws FileNotFoundException, CoreException {
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
		String fileName = componentPref.getName()
				+ resourceType.getNameSuffix();
		creatEmptyFile(fileName);
	}

	/**
	 * Copy the file resources from source component folder to destination
	 * component folder, but the .jar/.pem file will not copy.
	 * 
	 * @param project
	 * @param srcComponentFolderName
	 * @param desComponentFolderName
	 * @throws CoreException
	 */
	public void copyComponent(IProject project, String srcComponentFolderName,
			String desComponentFolderName) throws CoreException {
		IFolder srcFolder = project.getFolder(srcComponentFolderName);
		IFolder desFolder = project.getFolder(desComponentFolderName);
		if (!desFolder.exists()) {
			desFolder.create(false, true, null);
		}
		for (IResource resource : srcFolder.members()) {
			if (!(resource instanceof IFile)) {
				continue;
			}
			IFile file = (IFile) resource;
			if (file.getFileExtension().equals("jar")
					|| file.getFileExtension().equals("pem")) {
				continue;
			}
			if (file.exists()) {
				file.copy(desFolder.getFile(file.getName()).getFullPath(), false, null);
			}
		}
	}

	/**
	 * creat all the language type jet files, include begin,main or end files.
	 * 
	 * @throws CoreException
	 */
	private void creatJetFiles() throws CoreException {
		for (JetFileStamp fileStamp : this.componentPref.getJetFileStamps()) {
			creatJetLanguageFile(fileStamp);
		}
	}

	private void creatJetLanguageFile(JetFileStamp fileStamp)
			throws CoreException {
		String fileName = componentPref.getName() + "_"
				+ fileStamp.getFileStampName();
		switch (componentPref.getLanguageType()) {
		case BOTHLANGUAGETYPE:
			String[] suffixs = componentPref.getLanguageType()
					.getFileSuffix().split(";");
			for (String suffix : suffixs) {
				creatTemplateJetFile(fileName + suffix);
			}
			break;
		default:
			creatTemplateJetFile(fileName
					+ componentPref.getLanguageType().getFileSuffix());
		}
	}

	private void creatXmlFile() throws CoreException {
		// this.project.getFullPath();
		URL[] folderUrls = FileLocator.findEntries(ComponentDesigenerPlugin
				.getDefault().getBundle(), project.getProjectRelativePath());
		String tempXMLFileName = null;
		FileInputStream in = null;
		File f = null;
		try {
			tempXMLFileName = FileLocator.toFileURL(folderUrls[0]).getFile()
					+ componentPref.getName() + xmlSUFFIX;
			XMLUtil.toSave(tempXMLFileName, componentPref.getXmlDocument(),
					"UTF-8");
			XMLUtil.formatXMLFile(tempXMLFileName, "UTF-8");

			f = new File(tempXMLFileName);
			// String fileName = componentPref.getName();
			switch (componentPref.getLanguageType()) {
			case BOTHLANGUAGETYPE:
				String[] suffixs = componentPref.getLanguageType()
						.getNameSuffix().split(";");
				for (String nameSuffix : suffixs) {
					in = new FileInputStream(f);
					this.copyFileFromSrc(in, this.componentFolderName
							+ nameSuffix + xmlSUFFIX);
					in.close();
				}
				break;
			default:
				in = new FileInputStream(f);
				this.copyFileFromSrc(in, this.componentFolderName
						+ componentPref.getLanguageType().getNameSuffix()
						+ xmlSUFFIX);
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in = null;
			if (!f.delete()) {
				f.deleteOnExit();
			}
		}
		
	}
	

	private void addComponentImage() throws CoreException,
			FileNotFoundException {
		if (componentPref.getImageURL() == null) {
			copyFileFromSrc((FileInputStream) ImageLib
					.getImageInputStream(ImageLib.COMPONENT_DEFAULT),
					componentPref.getName() + "_icon32.png");
		}
		copyFileFromSrc(componentPref.getImageURL());
	}
	
	private void creatTemplateJetFile(String fileName) throws CoreException {
		FileInputStream templateFileStream = (FileInputStream) ComponentFolderManager.class
				.getResourceAsStream("template.javajet");
		copyFileFromSrc(templateFileStream, fileName);
	}

	private void creatEmptyFile(String fileName) throws CoreException {
		IFolder folder = project.getFolder(this.componentFolderName);
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
		IFile newFile = folder.getFile(fileName);
		if (newFile.exists()) {
			return;
		}
		newFile.create(new ByteArrayInputStream(new byte[0]), false, null);
	}
	

	private void copyFileFromSrc(String srcURL) throws FileNotFoundException,
			CoreException {
		if (srcURL == null || srcURL.equals(PluginConstant.EMPTY_STRING)) {
			return;
		}
		IPath path = new Path(srcURL);
		IFolder folder = project.getFolder(this.componentFolderName);
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
		IFile file = folder.getFile(path.lastSegment());
		if (file.exists()) {
			return;
		}
		FileInputStream fileStream = new FileInputStream(srcURL);
		file.create(fileStream, false, null);
	}
	
	private void copyFileFromSrc(FileInputStream inputStream, String fileName)
			throws CoreException {
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
				copyFileFromSrc(libEntries[i].getLocation());
			} else {
				// TODO: Add the lib configuration to xml file.
			}
		}
		
	}
}
