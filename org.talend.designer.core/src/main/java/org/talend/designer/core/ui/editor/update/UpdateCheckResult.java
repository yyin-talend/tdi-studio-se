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
package org.talend.designer.core.ui.editor.update;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.jobsettings.JobSettingsView;

/**
 * ggu class global comment. Detailled comment
 */
public class UpdateCheckResult extends UpdateResult {

    /**
     * ggu UpdateCheckResult constructor comment.
     * 
     * @param item
     */
    public UpdateCheckResult(Object item) {
        super(item);
    }

    @SuppressWarnings("unchecked")
    public String getName() {
        if (getUpdateObject() != null) {
            // node
            if (getUpdateObject() instanceof Node) {
                Node node = (Node) getUpdateObject();
                if (node.getUniqueName().equals(node.getLabel())) {
                    return node.getUniqueName();
                } else {
                    return node.getLabel() + UpdatesConstants.SPACE + UpdatesConstants.LEFT_BRACKETS + node.getUniqueName()
                            + UpdatesConstants.RIGHT_BRACKETS;
                }
            }
            // context names
            if (getUpdateObject() instanceof Set || getUpdateObject() instanceof List) {
                String names = UpdatesConstants.EMPTY;
                for (String str : (Collection<String>) getUpdateObject()) {
                    names = names + UpdatesConstants.SEGMENT + str;
                }
                if (names.startsWith(UpdatesConstants.SEGMENT)) {
                    names = names.substring(1);
                }
                return names;
            }
            //
            if (getUpdateObject() instanceof String) {
                return (String) getUpdateObject();
            }
            // the item instanceof Process
            switch (getUpdateType()) {
            case JOB_PROPERTY_EXTRA:
                return EComponentCategory.EXTRA.getTitle();
            case JOB_PROPERTY_STATS_LOGS:
                return EComponentCategory.STATSANDLOGS.getTitle();
            default:
            }
        }
        return UpdatesConstants.EMPTY;
    }

    public String getCategory() {
        String category = null;
        switch (getUpdateType()) {
        case NODE_PROPERTY:
        case NODE_SCHEMA:
        case NODE_QUERY:
            category = UpdatesConstants.COMPONENT;
            break;
        case JOB_PROPERTY_EXTRA:
        case JOB_PROPERTY_STATS_LOGS:
            if (getUpdateObject() != null && getUpdateObject() instanceof org.talend.designer.core.ui.editor.process.Process) {
                if (AbstractProcessProvider.isExtensionProcessForJoblet((IProcess) getUpdateObject())) {
                    category = JobSettingsView.VIEW_NAME_JOBLET; // joblet
                    break;
                }
            }
            category = JobSettingsView.VIEW_NAME;
            break;
        case CONTEXT:
            category = UpdatesConstants.CONTEXT;
            break;
        case JOBLET_RENAMED:
        case JOBLET_SCHEMA:
        case JOBLET_CONTEXT:
        case RELOAD:
            category = UpdatesConstants.JOBLET;
            break;
        default:
        }

        return category == null ? UpdatesConstants.EMPTY : category;
    }

}
