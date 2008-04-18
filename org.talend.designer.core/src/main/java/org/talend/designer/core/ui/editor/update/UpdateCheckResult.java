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

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.List;

import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.update.EUpdateResult;
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public String getName() {
        String displayName = getUpdateType().getDisplayName();
        switch (getUpdateType()) {
        case NODE_PROPERTY:
        case NODE_SCHEMA:
        case NODE_QUERY:
        case JOBLET_SCHEMA:
            break;
        case JOB_PROPERTY_EXTRA:
            displayName = displayName + UpdateManagerUtils.addBrackets(EComponentCategory.EXTRA.getTitle());
            break;
        case JOB_PROPERTY_STATS_LOGS:
            displayName = displayName + UpdateManagerUtils.addBrackets(EComponentCategory.STATSANDLOGS.getTitle());
            break;
        case CONTEXT:
        case JOBLET_CONTEXT:
            if (getResultType() == EUpdateResult.UPDATE || getResultType() == EUpdateResult.BUIL_IN) {
                if (getUpdateObject() != null && getUpdateObject() instanceof Collection) {
                    displayName = UpdatesConstants.EMPTY;
                    for (String str : (Collection<String>) getUpdateObject()) {
                        displayName = displayName + UpdatesConstants.SEGMENT + str;
                    }
                    if (displayName.startsWith(UpdatesConstants.SEGMENT)) {
                        displayName = displayName.substring(1);
                    }
                }
            } else if (getResultType() == EUpdateResult.RENAME) {
                // old name
                displayName = (String) ((List<Object>) getParameter()).get(1);
                displayName = displayName + UpdatesConstants.RENAME_SIGN;
                displayName = displayName + (String) ((List<Object>) getParameter()).get(2);
            }

            break;
        case JOBLET_RENAMED:
        case RELOAD:
            if (getUpdateObject() != null && getUpdateObject() instanceof List) {
                String label = UpdatesConstants.EMPTY;
                for (Node node : (List<Node>) getUpdateObject()) {
                    label = label + UpdatesConstants.SEGMENT + node.getLabel();
                }
                if (label.startsWith(UpdatesConstants.SEGMENT)) {
                    label = label.substring(1);
                }
                displayName = displayName + UpdateManagerUtils.addBrackets(label);
            } else if (getParameter() != null && getParameter() instanceof PropertyChangeEvent) {
                PropertyChangeEvent event = (PropertyChangeEvent) getParameter();
                // reload all compoennts.
                if (event.getSource() != null && !(event.getSource() instanceof IProcess)) {
                    displayName = getUpdateType().getDisplayName();
                    break;
                }
            }
            break;
        default:
        }

        return displayName;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public String getCategory() {
        String category = null;
        switch (getUpdateType()) {
        case NODE_PROPERTY:
        case NODE_SCHEMA:
        case NODE_QUERY:
        case JOBLET_SCHEMA:
            if (getUpdateObject() != null) {
                if (getUpdateObject() instanceof Node) {
                    Node node = (Node) getUpdateObject();
                    if (node.getUniqueName().equals(node.getLabel())) {
                        category = node.getUniqueName();
                    } else {
                        category = node.getLabel() + UpdateManagerUtils.addBrackets(node.getUniqueName());
                    }
                }
            }
            break;
        case JOB_PROPERTY_EXTRA:
        case JOB_PROPERTY_STATS_LOGS:
            boolean isJoblet = false;
            if (getUpdateObject() != null) {
                if (getUpdateObject() instanceof org.talend.designer.core.ui.editor.process.Process) {
                    if (AbstractProcessProvider.isExtensionProcessForJoblet((IProcess) getUpdateObject())) {
                        isJoblet = true;
                    }
                }
            }
            if (isJoblet) {
                category = JobSettingsView.VIEW_NAME_JOBLET; // joblet
            } else {
                category = JobSettingsView.VIEW_NAME;
            }
            break;
        case CONTEXT:
        case JOBLET_CONTEXT:
            category = UpdatesConstants.CONTEXT;
            break;
        case JOBLET_RENAMED:
        case RELOAD:
            if (getUpdateObject() != null && getUpdateObject() instanceof List) {
                Node node = ((List<Node>) getUpdateObject()).get(0);
                if (node != null) {
                    category = node.getComponent().getName();
                    break;
                }
            } else if (getParameter() != null && getParameter() instanceof PropertyChangeEvent) {
                PropertyChangeEvent event = (PropertyChangeEvent) getParameter();
                // reload all compoennts.
                if (event.getSource() != null && !(event.getSource() instanceof IProcess)) {
                    category = UpdatesConstants.COMPONENT;
                    break;
                }
            }
            category = UpdatesConstants.JOBLET;
            break;
        default:
        }

        return category == null ? UpdatesConstants.EMPTY : category;
    }

    @Override
    public String getJobInfor() {
        if (getJob() != null) {
            StringBuffer infor = new StringBuffer();
            String prefix = UpdatesConstants.JOB;
            String label = null;
            String version = null;
            String others = null;
            if (getJob() instanceof IProcess2) {
                IProcess2 process = (IProcess2) getJob();
                if (process.disableRunJobView()) { // ?? for joblet
                    prefix = UpdatesConstants.JOBLET;
                }
                label = process.getLabel();
                version = process.getVersion();
            }
            if (getItemProcess() != null) { // update item
                others = UpdatesConstants.START;
            }
            infor.append(prefix);
            if (label != null) {
                infor.append(UpdatesConstants.SPACE);
                infor.append(label);
                infor.append(UpdatesConstants.SPACE);
                infor.append(version);
                infor.append(UpdateManagerUtils.addBrackets(others));
            }
            return infor.toString();
        }
        return UpdatesConstants.EMPTY;
    }

}
