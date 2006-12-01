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
package org.talend.designer.core.model.components;

import org.talend.designer.core.i18n.Messages;

/**
 * Enumeration that describes all the standard name used in the properties.
 * 
 * $Id$
 * 
 */
public enum EParameterName {
    NAME(Messages.getString("EParameterName.0")), //$NON-NLS-1$
    UNIQUE_NAME("Unique Name"),
    AUTHOR(Messages.getString("EParameterName.1")), //$NON-NLS-1$
    PURPOSE(Messages.getString("EParameterName.2")), //$NON-NLS-1$
    HELP("Help"),
    START("Start"),
    STARTABLE("Startable"),
    STATUS(Messages.getString("EParameterName.3")), //$NON-NLS-1$
    DESCRIPTION(Messages.getString("EParameterName.4")), //$NON-NLS-1$
    VERSION(Messages.getString("EParameterName.5")), //$NON-NLS-1$
    PLATFORM(Messages.getString("EParameterName.6")), //$NON-NLS-1$
    FAMILY(Messages.getString("EParameterName.7")), //$NON-NLS-1$
    LOG(Messages.getString("EParameterName.8")), //$NON-NLS-1$
    ACTIVATE(Messages.getString("EParameterName.9")), //$NON-NLS-1$
    LABEL(Messages.getString("EParameterName.10")), //$NON-NLS-1$
    HINT(Messages.getString("EParameterName.11")), //$NON-NLS-1$
    SHOW_HINT(Messages.getString("EParameterName.12")), //$NON-NLS-1$
    COMMENT(Messages.getString("EParameterName.13")), //$NON-NLS-1$
    LOG_FILENAME(Messages.getString("EParameterName.14")), //$NON-NLS-1$
    LEVEL_LOG_TO_FILE(Messages.getString("EParameterName.15")), //$NON-NLS-1$
    LEVEL_LOG_TO_DB(Messages.getString("EParameterName.16")), //$NON-NLS-1$
    LEVEL_LOG_TO_STDOUT(Messages.getString("EParameterName.17")), //$NON-NLS-1$
    LOG_TO_FILE(Messages.getString("EParameterName.18")), //$NON-NLS-1$
    LOG_TO_DB(Messages.getString("EParameterName.19")), //$NON-NLS-1$
    LOG_TO_STDOUT(Messages.getString("EParameterName.20")), //$NON-NLS-1$
    SCHEMA_TYPE("Schema Type"),
    REPOSITORY_SCHEMA_TYPE("Repository"),
    PROPERTY_TYPE("Property Type"),
    REPOSITORY_PROPERTY_TYPE("Repository"),
    CONDITION("Condition"),
    COMPONENT_NAME("Component Name"),
    UPDATE_COMPONENTS("Update components"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PROCESS_TYPE_PROCESS("Process"),
    PROCESS_TYPE_CONTEXT("Context"),
    PREVIEW("Preview"),
    COLUMN_LIST("Column list"),
    PREV_COLUMN_LIST("Prev. Comp. Column list"),
    TSTATCATCHER_STATISTICS("tStatCatcher Statistics");

    private String displayName;

    EParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
