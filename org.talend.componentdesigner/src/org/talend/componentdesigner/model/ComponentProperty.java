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
package org.talend.componentdesigner.model;

import java.util.List;

import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.LanguageType;

/**
 * @author rli
 *
 */
public class ComponentProperty {
    private String name;
    
    private LanguageType languageType;
    
    private List<JetFileStamp> jetFileTypes;
    
    private String imageURL;
    
    private String libFileURL;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the languageType
	 */
	public LanguageType getLanguageType() {
		return languageType;
	}

	/**
	 * @param languageType the languageType to set
	 */
	public void setLanguageType(LanguageType languageType) {
		this.languageType = languageType;
	}

	/**
	 * @return the jetFileTypes
	 */
	public List<JetFileStamp> getJetFileTypes() {
		return jetFileTypes;
	}

	/**
	 * @param jetFileTypes the jetFileTypes to set
	 */
	public void setJetFileTypes(List<JetFileStamp> jetFileTypes) {
		this.jetFileTypes = jetFileTypes;
	}

	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}

	/**
	 * @param imageURL the srcImageName to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	/**
	 * @return the libFileURL
	 */
	public String getLibFileURL() {
		return libFileURL;
	}

	/**
	 * @param libFileURL the libFileName to set
	 */
	public void setLibFileURL(String libFileURL) {
		this.libFileURL = libFileURL;
	}
}
