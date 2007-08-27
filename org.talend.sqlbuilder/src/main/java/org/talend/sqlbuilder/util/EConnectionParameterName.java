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
package org.talend.sqlbuilder.util;

import org.talend.sqlbuilder.Messages;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public enum EConnectionParameterName {
    SERVER_NAME(Messages.getString("ConnectionParameterName.HostLabel")), //$NON-NLS-1$
    PORT(Messages.getString("ConnectionParameterName.PortLabel")), //$NON-NLS-1$
    SID(Messages.getString("ConnectionParameterName.DatabaseLabel")), //$NON-NLS-1$
    SCHEMA(Messages.getString("ConnectionParameterName.SchemaLabel")), //$NON-NLS-1$
    USERNAME(Messages.getString("ConnectionParameterName.UserLabel")), //$NON-NLS-1$
    PASSWORD(Messages.getString("ConnectionParameterName.PasswordLabel")), //$NON-NLS-1$
    FILE(Messages.getString("ConnectionParameterName.DbfileLabel")), //$NON-NLS-1$
    DIRECTORY(Messages.getString("ConnectionParameterName.DbPathLabel")); //$NON-NLS-1$

    private String displayName;

    /**
     * qzhang ConnectionParameterName constructor comment.
     */
    EConnectionParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
