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
package org.talend.sqlbuilder.dbstructure;

import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryObject;

/**
 * SqlBuilderRepositoryObject.
 *
 */
public class SqlBuilderRepositoryObject extends RepositoryObject {
	private String repositoryName;
	private String sourceName;
	private String image;
	private String color;
	private boolean isBuildIn;
	private String diffColor;
	private String diffImage;
	private boolean isDiffDevergency;
	private boolean isDiffSynchronised;
	
	public SqlBuilderRepositoryObject(Property property) {
		super(property);
	}
	
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public boolean isBuildIn() {
		return isBuildIn;
	}
	public void setBuildIn(boolean isBuildIn) {
		this.isBuildIn = isBuildIn;
	}
	@Override
	public String getLabel() {
		return repositoryName;
	}

	public String getDiffColor() {
		return diffColor;
	}

	public void setDiffColor(String diffColor) {
		this.diffColor = diffColor;
	}

	public String getDiffImage() {
		return diffImage;
	}

	public void setDiffImage(String diffImage) {
		this.diffImage = diffImage;
	}

	public boolean isDiffDevergency() {
		return isDiffDevergency;
	}

	public void setDiffDevergency(boolean isDiffDevergency) {
		this.isDiffDevergency = isDiffDevergency;
	}

	public boolean isDiffSynchronised() {
		return isDiffSynchronised;
	}

	public void setDiffSynchronised(boolean isDiffSynchronised) {
		this.isDiffSynchronised = isDiffSynchronised;
	}
	
}
