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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.update.cmd.UpdateContextParameterCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateJobletNodeCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateMainParameterCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateNodeParameterCommand;
import org.talend.designer.core.ui.views.contexts.Contexts;
import org.talend.designer.core.ui.views.jobsettings.JobSettings;
import org.talend.designer.core.ui.views.properties.ComponentSettings;

/**
 * ggu class global comment. Detailled comment
 */
public final class UpdateManagerUtils {

    public static IComponent getComponent(Process process, final String name) {
        if (name != null) {
            AbstractMultiPageTalendEditor editor = process.getEditor();
            if (editor != null) {
                AbstractTalendEditor talendEditor = editor.getTalendEditor();
                if (talendEditor != null) {
                    return talendEditor.getComponent(name);
                }
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "getNewOutputTableForConnection".
     * 
     * @param newOutputTableList
     * @param attachedConnector
     * @return
     */
    public static IMetadataTable getNewOutputTableForConnection(List<IMetadataTable> newOutputTableList, String tableName) {
        for (IMetadataTable metadataTable : newOutputTableList) {
            if (tableName != null && tableName.equals(metadataTable.getTableName())
                    || tableName.equals(metadataTable.getTableName())) {
                return metadataTable;
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "getNewInputTableForConnection".
     * 
     * @param newInputTableList
     * @param connector
     * 
     * @return
     */
    public static IMetadataTable getNewInputTableForConnection(List<IMetadataTable> newInputTableList, String connector) {
        for (IMetadataTable metadataTable : newInputTableList) {
            if (connector != null
                    && (connector.equals(metadataTable.getAttachedConnector()) || connector.equals(metadataTable.getTableName()))) {
                return metadataTable;
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "getElemParam".
     * 
     * @param elemParams
     * @param string
     * 
     * @return
     */
    public static IElementParameter getElemParam(List<IElementParameter> elemParams, String string) {
        for (IElementParameter elementParameter : elemParams) {
            if (string != null && string.equals(elementParameter.getContext())) {
                return elementParameter;
            }
        }
        return null;
    }

    public static String addBrackets(String value) {
        if (value == null || UpdatesConstants.EMPTY.equals(value.trim())) {
            return UpdatesConstants.EMPTY;
        }
        return UpdatesConstants.SPACE + UpdatesConstants.LEFT_BRACKETS + value + UpdatesConstants.RIGHT_BRACKETS;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static boolean executeUpdates(final List<UpdateResult> results) {
        if (results == null || results.isEmpty()) {
            return false;
        }
        try {
            UpdateDetectionDialog checkDialog = new UpdateDetectionDialog(Display.getCurrent().getActiveShell(), results);

            if (checkDialog.open() == IDialogConstants.OK_ID) {
                final List<Object> selectResult = Arrays.asList(checkDialog.getResult());
                ProgressDialog progress = new ProgressDialog(Display.getCurrent().getActiveShell()) {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.setCanceled(false);
                        monitor.beginTask("Progress...", selectResult.size() + 4); //$NON-NLS-1$
                        // execute
                        executeUpdates(selectResult, monitor);
                        // refresh
                        refreshRelatedViewers(selectResult);
                        monitor.worked(2);
                        // save repository item
                        RepositoryUpdateManager.saveModifiedItem(results);
                        monitor.worked(2);
                        monitor.done();
                    }

                };
                try {
                    progress.executeProcess();
                } catch (InvocationTargetException e) {
                    // 
                } catch (InterruptedException e) {
                    // 
                }
                return !selectResult.isEmpty();
            }
        } finally {
            results.clear();
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "refreshViewers".
     */
    @SuppressWarnings("unchecked")
    private static void refreshRelatedViewers(List results) {
        boolean context = false;
        boolean jobSetting = false;
        boolean componentSettings = false;
        boolean palette = false;

        for (UpdateResult result : (List<UpdateResult>) results) {
            switch (result.getUpdateType()) {
            case CONTEXT:
            case JOBLET_CONTEXT:
                context = true;
                break;
            case JOB_PROPERTY_EXTRA:
            case JOB_PROPERTY_STATS_LOGS:
                jobSetting = true;
                break;
            case NODE_PROPERTY:
            case NODE_QUERY:
            case NODE_SCHEMA:
                componentSettings = true;
                break;
            case RELOAD:
            case JOBLET_RENAMED:
            case JOBLET_SCHEMA:
                palette = true;
                break;
            default:
                break;
            }
        }
        if (context) {
            Contexts.switchToCurContextsView();
        }
        if (jobSetting) {
            JobSettings.switchToCurJobSettingsView();
        }
        if (componentSettings) {
            ComponentSettings.switchToCurComponentSettingsView();
        }
        if (palette) {
            ComponentUtilities.updatePalette();
        }
    }

    /**
     * 
     * ggu Comment method "executeUpdates".
     * 
     * can override the is method.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static void executeUpdates(List selectResult, IProgressMonitor monitor) {
        Command command = null;
        for (UpdateResult result : (List<UpdateResult>) selectResult) {
            switch (result.getUpdateType()) {
            case NODE_PROPERTY:
            case NODE_SCHEMA:
            case NODE_QUERY:
                command = new UpdateNodeParameterCommand(result);
                break;
            case JOB_PROPERTY_EXTRA:
            case JOB_PROPERTY_STATS_LOGS:
                command = new UpdateMainParameterCommand(result);
                break;
            case CONTEXT:
                command = executeContextUpdates(result);
                break;
            case JOBLET_RENAMED:
            case JOBLET_SCHEMA:
            case RELOAD:
                command = executeJobletNodesUpdates(result);
                break;
            case JOBLET_CONTEXT:
                command = new Command() { // have update in checking.
                };
                break;
            default:
                break;
            }
            if (command != null) {
                SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1,
                        SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
                subMonitor.beginTask(UpdatesConstants.EMPTY, 1);
                subMonitor.subTask(getResultTaskInfor(result));
                //
                Object job = result.getJob();
                boolean executed = false;
                if (job != null && result.getItemProcess() == null) { // not repository item
                    if (job instanceof IProcess2) {
                        IProcess2 process = (IProcess2) job;
                        process.getCommandStack().execute(command);
                        executed = true;
                    }
                }
                if (!executed) {
                    command.execute();
                }
                subMonitor.worked(1);
                subMonitor.done();
            }
        }

    }

    private static String getResultTaskInfor(UpdateResult result) {
        if (result == null) {
            return UpdatesConstants.EMPTY;
        }
        StringBuffer infor = new StringBuffer();
        infor.append(result.getName());
        infor.append(UpdatesConstants.LEFT_BRACKETS);
        infor.append(result.getCategory());
        infor.append(UpdatesConstants.SEGMENT);
        infor.append(result.getJobInfor());
        infor.append(UpdatesConstants.RIGHT_BRACKETS);
        return infor.toString();
    }

    /*
     * context
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static Command executeContextUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        Object object = result.getUpdateObject();
        if (object instanceof Set) {
            return new UpdateContextParameterCommand(result);
        }
        return null;
    }

    /*
     * joblet
     */
    private static Command executeJobletNodesUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        Object parameter = result.getParameter();
        if (parameter instanceof PropertyChangeEvent) {
            return new UpdateJobletNodeCommand(result);
        }
        return null;
    }

}
