// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.contexts.Contexts;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogs;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * Track the active Process being edited. <br/>
 * 
 * $Id$
 * 
 */
public class ActiveProcessTracker implements IPartListener {

    private static IProcess currentProcess;

    public IProcess getJobFromActivatedEditor(IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            mpte.setName();

            IProcess process = mpte.getTalendEditor().getProcess();
            return process;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partActivated(final IWorkbenchPart part) {
        IProcess process = getJobFromActivatedEditor(part);
        if (process != null) {
            if (process instanceof Process) {
                Process p = (Process) process;
                if (!p.isReadOnly() && p.isActivate()) {
                    if (p.checkDifferenceWithRepository()) {
                        MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
                        mpte.getTalendEditor().setDirty(true);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
     */
    public void partBroughtToTop(IWorkbenchPart part) {
        IProcess process = getJobFromActivatedEditor(part);
        if (process != null) {
            currentProcess = process;
            setContextsView(process);
            setStatsAndLogsView(process);
        }
    }

    /**
     * ftang Comment method "setStatsAndLogsView".
     * 
     * @param process
     */
    private void setStatsAndLogsView(IProcess process) {
        StatsAndLogs.setTitle("Job " + process.getProperty().getLabel()); //$NON-NLS-1$
        StatsAndLogs.switchToCurStatsAndLogsView();
    }

    /**
     * qzhang Comment method "setProblemsView".
     * 
     * @param process
     */
    private void addJobInProblemView(IProcess process) {
        Problems.addProcess(process);

        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        service.setActiveProcess(process);

        Problems.setTitle("Job " + process.getProperty().getLabel()); //$NON-NLS-1$
    }

    /**
     * qzhang Comment method "setProblemsView".
     * 
     * @param process
     */
    private void setContextsView(IProcess process) {

        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        service.setActiveProcess(process);

        Contexts.setTitle("Job " + process.getProperty().getLabel()); //$NON-NLS-1$
        Contexts.switchToCurContextsView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
     */
    public void partClosed(IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            if (mpte.isKeepPropertyLocked()) {
                return;
            }
        }

        IProcess process = getJobFromActivatedEditor(part);
        if (process != null) {
            Problems.removeProblemsByProcess(process);
            Problems.removeJob(process);
            IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
            service.removeProcess(process);

            if (currentProcess == process) {
                Problems.setTitle(""); //$NON-NLS-1$
                Problems.clearAll();
                Contexts.setTitle(""); //$NON-NLS-1$
                Contexts.clearAll();
                StatsAndLogs.setTitle("");
                StatsAndLogs.clearAll();
            }
            UIUtils.closeSqlBuilderDialogs(process.getName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partDeactivated(IWorkbenchPart part) {
        IProcess process = getJobFromActivatedEditor(part);
        if (process != null) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            mpte.getTalendEditor().savePaletteState();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
     */
    public void partOpened(IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            if (mpte.isJobAlreadyOpened()) {
                mpte.updateChildrens();
                // close the first editor and keep the new one. (so only one will remain)
                IEditorReference[] ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditors(
                        mpte.getEditorInput(), MultiPageTalendEditor.ID, IWorkbenchPage.MATCH_INPUT);
                IEditorPart editorPart = ref[0].getEditor(false);
                editorPart.doSave(new NullProgressMonitor());
                ((MultiPageTalendEditor) editorPart).setKeepPropertyLocked(true);
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editorPart, false);
            }
        }
        IProcess process = getJobFromActivatedEditor(part);
        if (process != null) {
            currentProcess = process;
            addJobInProblemView(process);
        }
    }
}
