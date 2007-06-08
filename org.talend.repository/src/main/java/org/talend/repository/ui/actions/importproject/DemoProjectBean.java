// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.actions.importproject;

import org.talend.core.language.ECodeLanguage;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class DemoProjectBean {

    private String projectName;

    private ECodeLanguage language;

    private String archiveFilePath;

    private String descriptionFilePath;

    /**
     * Getter for archiveFilePath.
     * 
     * @return the archiveFilePath
     */
    public String getArchiveFilePath() {
        return archiveFilePath;
    }

    /**
     * Sets the archiveFilePath.
     * 
     * @param archiveFilePath the archiveFilePath to set
     */
    public void setArchiveFilePath(String archiveFilePath) {
        this.archiveFilePath = archiveFilePath;
    }

    /**
     * Getter for language.
     * 
     * @return the language
     */
    public ECodeLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     * 
     * @param language the language to set
     */
    public void setLanguage(ECodeLanguage language) {
        this.language = language;
    }

    /**
     * Getter for projectName.
     * 
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the projectName.
     * 
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescriptionFilePath() {
        return this.descriptionFilePath;
    }

    public void setDescriptionFilePath(String descriptionFilePath) {
        this.descriptionFilePath = descriptionFilePath;
    }

}
