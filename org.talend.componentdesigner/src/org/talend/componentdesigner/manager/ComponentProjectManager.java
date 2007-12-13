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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.ComponentProperty;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;

/**
 * @author rli
 * 
 */
public class ComponentProjectManager {

	private static String xmlSUFFIX = ".xml";

	private ComponentProperty componentProperty;

	private IProject project;

	public void generateComponentContent(ComponentProperty componentProperty,
			IProject project) throws FileNotFoundException, CoreException {
		this.componentProperty = componentProperty;
		this.project = project;
		this.creatJetFiles();
		this.creatXmlFile();
		this.addComponentImage();
		this.addComponentLib();
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
		IFile newLogo = project.getFile(fileName);
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
		if (srcURL == null || srcURL.equals(PluginConstant.EMPTYSTRING)) {
			return;
		}
		IPath path = new Path(srcURL);
		IFile file = project.getFile(path.lastSegment());
		FileInputStream fileStream = new FileInputStream(srcURL);
		// "c:/MyOtherData/newLogo.png");
		file.create(fileStream, false, null);

		// IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// URI uri= workspace.getRoot().getLocationURI();
	}
}
