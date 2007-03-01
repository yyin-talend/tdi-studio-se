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
    NAME(Messages.getString("EParameterName.Name")), //$NON-NLS-1$
    UNIQUE_NAME(Messages.getString("EParameterName.uniqueName")), //$NON-NLS-1$
    TRANSLATED_UNIQUE_NAME("Translated Unique Name"), //$NON-NLS-1$
    AUTHOR(Messages.getString("EParameterName.Author")), //$NON-NLS-1$
    PURPOSE(Messages.getString("EParameterName.Purpose")), //$NON-NLS-1$
    HELP(Messages.getString("EParameterName.help")), //$NON-NLS-1$
    START(Messages.getString("EParameterName.start")), //$NON-NLS-1$
    STARTABLE(Messages.getString("EParameterName.startable")), //$NON-NLS-1$
    STATUS(Messages.getString("EParameterName.Status")), //$NON-NLS-1$
    DESCRIPTION(Messages.getString("EParameterName.Description")), //$NON-NLS-1$
    VERSION(Messages.getString("EParameterName.Version")), //$NON-NLS-1$
    PLATFORM(Messages.getString("EParameterName.Platform")), //$NON-NLS-1$
    FAMILY(Messages.getString("EParameterName.Family")), //$NON-NLS-1$
    LOG(Messages.getString("EParameterName.Log")), //$NON-NLS-1$
    ACTIVATE(Messages.getString("EParameterName.Activate")), //$NON-NLS-1$
    LABEL(Messages.getString("EParameterName.LabelFormat")), //$NON-NLS-1$
    HINT(Messages.getString("EParameterName.HintFormat")), //$NON-NLS-1$
    SHOW_HINT(Messages.getString("EParameterName.ShowHint")), //$NON-NLS-1$
    COMMENT(Messages.getString("EParameterName.Comment")), //$NON-NLS-1$
    LOG_FILENAME(Messages.getString("EParameterName.FileName")), //$NON-NLS-1$
    LEVEL_LOG_TO_FILE(Messages.getString("EParameterName.Level.Log.File")), //$NON-NLS-1$
    LEVEL_LOG_TO_DB(Messages.getString("EParameterName.Level.Log.Db")), //$NON-NLS-1$
    LEVEL_LOG_TO_STDOUT(Messages.getString("EParameterName.Level.Log.Stdout")), //$NON-NLS-1$
    LOG_TO_FILE(Messages.getString("EParameterName.Log.File")), //$NON-NLS-1$
    LOG_TO_DB(Messages.getString("EParameterName.Log.Db")), //$NON-NLS-1$
    LOG_TO_STDOUT(Messages.getString("EParameterName.Log.Stdout")), //$NON-NLS-1$
    SCHEMA_TYPE(Messages.getString("EParameterName.schemaType")), //$NON-NLS-1$
    REPOSITORY_SCHEMA_TYPE("Repository"),  //$NON-NLS-1$  Hidden parameter so no translation needed
    QUERYSTORE_TYPE(Messages.getString("EParameterName.queryType")), //$NON-NLS-1$
    REPOSITORY_QUERYSTORE_TYPE("Repository"),  //$NON-NLS-1$  Hidden parameter so no translation needed
    PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
    REPOSITORY_PROPERTY_TYPE("Repository"),  //$NON-NLS-1$  Hidden parameter so no translation needed
    CONDITION(Messages.getString("EParameterName.condition")), //$NON-NLS-1$
    COMPONENT_NAME(Messages.getString("EParameterName.componentName")), //$NON-NLS-1$
    TRANSLATED_COMPONENT_NAME("Translated Component Name"),  //$NON-NLS-1$  Hidden parameter so no translation needed
    UPDATE_COMPONENTS("Update components"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PROCESS_TYPE(Messages.getString("EParameterName.generateCode")), //$NON-NLS-1$
    PROCESS_TYPE_PROCESS(Messages.getString("EParameterName.process")), //$NON-NLS-1$
    PROCESS_TYPE_CONTEXT(Messages.getString("EParameterName.context")), //$NON-NLS-1$
    PREVIEW(Messages.getString("EParameterName.preview")), //$NON-NLS-1$
    COLUMN_LIST(Messages.getString("EParameterName.columnList")), //$NON-NLS-1$
    PREV_COLUMN_LIST(Messages.getString("EParameterName.prevColumnList")), //$NON-NLS-1$
    TSTATCATCHER_STATS(Messages.getString("EParameterName.tStatCatcherStats")), //$NON-NLS-1$
    COMP_DEFAULT_FILE_DIR("COMP_DEFAULT_FILE_DIR"), //$NON-NLS-1$  Hidden parameter so no translation needed
    REPOSITORY_ENCODING_TYPE("Repository"), 
    ENCODING_TYPE(Messages.getString("EParameterName.encodingType")); 

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
