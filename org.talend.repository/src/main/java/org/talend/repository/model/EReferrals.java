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
package org.talend.repository.model;

/**
 * This class is used for storing Referrals for LDAP schema. <br/>
 * @author ftang, 18/09/2007
 * 
 */
public enum EReferrals {

    IGNORE("Ignore","ignore"),
    FOLLOW("Follow","follow");

    /**
     * 
     */
    private String displayName;
    
    /**
     * 
     */
    private String repositoryName;
    

    /**
     * EReferrals constructor comment.
     * @param displayName
     * @param repositoryName
     */
    private EReferrals(String displayName,String repositoryName) {
        this.displayName = displayName;
        this.repositoryName = repositoryName;
    }

    /**
     * Comment method "getDisplayName".
     * @return
     */
    public String getDisplayName() {
        return this.displayName;
    }
    
    /**
     *Comment method "getRepositoryName".
     * @return
     */
    public String getRepositoryName() {
        return this.repositoryName;
    }
}
