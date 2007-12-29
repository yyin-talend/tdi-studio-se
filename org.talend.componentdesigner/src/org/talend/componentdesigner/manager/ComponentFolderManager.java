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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.ILibEntry;
import org.talend.componentdesigner.model.componentpref.ComponentPref;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.ResourceLanguageType;

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
			IProject project, String componentFolderName)
			throws FileNotFoundException, CoreException {
		this.componentPref = componentPref;
		this.project = project;
		this.componentFolderName = componentFolderName;
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
		creatNewFile(fileName);
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
				creatNewFile(fileName + suffix);
			}
			break;
		default:
			creatNewFile(fileName
					+ componentPref.getLanguageType().getFileSuffix());
		}
	}

	private void creatXmlFile() throws CoreException {
		String fileName = componentPref.getName();
		switch (componentPref.getLanguageType()) {
		case BOTHLANGUAGETYPE:
			String[] suffixs = componentPref.getLanguageType()
					.getNameSuffix().split(";");
			for (String nameSuffix : suffixs) {
				creatNewFile(fileName + nameSuffix + xmlSUFFIX);
			}
			break;
		default:
			creatNewFile(fileName
					+ componentPref.getLanguageType().getNameSuffix()
					+ xmlSUFFIX);
		}
	}

	private void creatNewFile(String fileName) throws CoreException {
		IFolder folder = project.getFolder(this.componentFolderName);
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
		IFile newLogo = folder.getFile(fileName);
		if (newLogo.exists()) {
			return;
		}
		newLogo.create(new ByteArrayInputStream(new byte[0]), false, null);
	}

	private void addComponentImage() throws CoreException,
			FileNotFoundException {
		if (componentPref.getImageURL() == null) {
			copyFileFromSrc((FileInputStream) ImageLib
					.getImageInputStream(ImageLib.COMPONENT_DEFAULT),
					ImageLib.COMPONENT_DEFAULT);
		}
		copyFileFromSrc(componentPref.getImageURL());
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
