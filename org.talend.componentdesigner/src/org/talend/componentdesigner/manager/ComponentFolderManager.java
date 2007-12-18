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
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.ComponentProperty;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.ResourceLanguageType;

/**
 * @author rli
 * 
 */
public class ComponentFolderManager {

	private static String xmlSUFFIX = ".xml";

	private ComponentProperty componentProperty;

	private IProject project;

	private String componentFolderName;

	public void generateComponentContent(ComponentProperty componentProperty,
			IProject project, String componentFolderName)
			throws FileNotFoundException, CoreException {
		this.componentProperty = componentProperty;
		this.project = project;
		this.componentFolderName = componentFolderName;
		this.creatJetFiles();
		this.creatResourceFiles();
		this.creatXmlFile();
		this.addComponentImage();
		this.addComponentLib();
	}
	
	private void creatResourceFiles() throws CoreException {
		for (ResourceLanguageType resourceType : this.componentProperty.getResourceLanguageTypes()) {
			creatResourceFile(resourceType);
		}
	}

	private void creatResourceFile(ResourceLanguageType resourceType) throws CoreException {
		String fileName = componentProperty.getName()
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
		for (JetFileStamp fileStamp : this.componentProperty.getJetFileTypes()) {
			creatJetLanguageFile(fileStamp);
		}
	}

	private void creatJetLanguageFile(JetFileStamp fileStamp)
			throws CoreException {
		String fileName = componentProperty.getName() + "_"
				+ fileStamp.getFileStampName();
		switch (componentProperty.getLanguageType()) {
		case BOTHLANGUAGETYPE:
			String[] suffixs = componentProperty.getLanguageType()
					.getFileSuffix().split(";");
			for (String suffix : suffixs) {
				creatNewFile(fileName + suffix);
			}
			break;
		default:
			creatNewFile(fileName
					+ componentProperty.getLanguageType().getFileSuffix());
		}
	}

	private void creatXmlFile() throws CoreException {
		String fileName = componentProperty.getName();
		switch (componentProperty.getLanguageType()) {
		case BOTHLANGUAGETYPE:
			String[] suffixs = componentProperty.getLanguageType()
					.getNameSuffix().split(";");
			for (String nameSuffix : suffixs) {
				creatNewFile(fileName + nameSuffix + xmlSUFFIX);
			}
			break;
		default:
			creatNewFile(fileName
					+ componentProperty.getLanguageType().getNameSuffix()
					+ xmlSUFFIX);
		}
	}

	private void creatNewFile(String fileName) throws CoreException {
		IFolder folder = project.getFolder(this.componentFolderName);
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
		IFile newLogo = folder.getFile(fileName);
		newLogo.create(new ByteArrayInputStream(new byte[0]), false, null);
	}

	private void addComponentImage() throws CoreException,
			FileNotFoundException {
		copyFileFromSrc(componentProperty.getImageURL());
	}

	private void addComponentLib() throws FileNotFoundException, CoreException {
		copyFileFromSrc(componentProperty.getLibFileURL());

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
		FileInputStream fileStream = new FileInputStream(srcURL);
		file.create(fileStream, false, null);
	}
}
