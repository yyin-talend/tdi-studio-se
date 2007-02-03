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
    UNIQUE_NAME("Unique Name"), //$NON-NLS-1$
    TRANSLATED_UNIQUE_NAME("Translated Unique Name"), //$NON-NLS-1$
    AUTHOR(Messages.getString("EParameterName.1")), //$NON-NLS-1$
    PURPOSE(Messages.getString("EParameterName.2")), //$NON-NLS-1$
    HELP("Help"), //$NON-NLS-1$
    START("Start"), //$NON-NLS-1$
    STARTABLE("Startable"), //$NON-NLS-1$
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
    SCHEMA_TYPE("Schema Type"), //$NON-NLS-1$
    REPOSITORY_SCHEMA_TYPE("Repository"), //$NON-NLS-1$
    QUERYSTORE_TYPE("Query Type"), //$NON-NLS-1$
    REPOSITORY_QUERYSTORE_TYPE("Repository"), //$NON-NLS-1$
    PROPERTY_TYPE("Property Type"), //$NON-NLS-1$
    REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$
    CONDITION("Condition"), //$NON-NLS-1$
    COMPONENT_NAME("Component Name"), //$NON-NLS-1$
    TRANSLATED_COMPONENT_NAME("Translated Component Name"), //$NON-NLS-1$
    UPDATE_COMPONENTS("Update components"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PROCESS_TYPE("Generate Code"), //$NON-NLS-1$
    PROCESS_TYPE_PROCESS("Process"), //$NON-NLS-1$
    PROCESS_TYPE_CONTEXT("Context"), //$NON-NLS-1$
    PREVIEW("Preview"), //$NON-NLS-1$
    COLUMN_LIST("Column list"), //$NON-NLS-1$
    PREV_COLUMN_LIST("Prev. Comp. Column list"), //$NON-NLS-1$
    TSTATCATCHER_STATS("tStatCatcher Statistics"), //$NON-NLS-1$
    COMP_DEFAULT_FILE_DIR("COMP_DEFAULT_FILE_DIR"); //$NON-NLS-1$  Hidden parameter so no translation needed

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
