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
package org.talend.designer.core.debug;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.editor.RepositoryEditorInput;

/**
 * bqian A launch delegate for launching local job. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class JobLaunchConfigurationDelegate extends org.eclipse.debug.core.model.LaunchConfigurationDelegate {

    /**
     * Convenience method to get the launch manager.
     * 
     * @return the launch manager
     */
    protected ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }

    public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
        return null;
    }

    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
            throws CoreException {
        String jobName = configuration.getAttribute(TalendDebugUIConstants.JOB_NAME, (String) null);
        String jobVersion = configuration.getAttribute(TalendDebugUIConstants.JOB_VERSION, (String) null);
        // find process from open editor.
        IProcess process = findProcessFromEditors(jobName,jobVersion);
        // find process from repository
        if (process == null) {
            process = findProcessFromRepository(jobName);
        }

        if (process == null) {
            ExceptionHandler.log("Job " + jobName + " can not be found. Maybe it is removed.");
            return;
        }

        final IProcess p = process;
        // Run job
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
                service.setActiveProcess(p);
                service.getRunProcessAction().run();
            }
        });

    }

    /**
     * DOC bqian Comment method "findProcessFromRepository".
     * 
     * @param jobName
     * @return
     */
    private IProcess findProcessFromRepository(String jobName) {
        try {
            List<IRepositoryObject> list = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().getAll(
                    ERepositoryObjectType.PROCESS, true);
            for (IRepositoryObject repositoryObject : list) {
                if (repositoryObject.getProperty().getLabel().equals(jobName)) {
                    ProcessEditorInput fileEditorInput = new ProcessEditorInput((ProcessItem) repositoryObject.getProperty()
                            .getItem(), true);
                    IProcess process = fileEditorInput.getLoadedProcess();
                    return process;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * DOC bqian Comment method "findProcessFromEditors".
     * 
     * @param jobName
     * @param jobVersion 
     */
    private IProcess findProcessFromEditors(final String jobName, final String jobVersion) {
        final IProcess[] process = new IProcess[1];

        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getEditorReferences();
                for (IEditorReference editorReference : editors) {
                    IEditorPart editor = editorReference.getEditor(false);
                    IEditorInput input = editor.getEditorInput();
                    if (input instanceof RepositoryEditorInput) {
                        RepositoryEditorInput rInput = (RepositoryEditorInput) input;
                        IProcess p = rInput.getLoadedProcess();
                        if (p != null && p.getLabel().equals(jobName) && p.getVersion().equals(jobVersion)) {
                            process[0] = p;
                            break;
                        }
                    }
                }
            }
        });

        return process[0];
    }
}
