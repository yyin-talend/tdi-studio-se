// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.IStartup;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.runtime.process.ITalendProcessJavaProject;

/**
 * Delete all the perl and java jobs when T.O.S start up.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DeleteAllJobWhenStartUp.java 下午02:50:26 2007-5-29 +0000 (2007-5-29) yzhang $
 * 
 */
public class DeleteAllJobWhenStartUp implements IStartup {

    public static boolean executed;

    private boolean startUnderPluginModel;

    public void startup(boolean pluginModel) {
        startUnderPluginModel = pluginModel;
        earlyStartup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    @Override
    public void earlyStartup() {

        if (!startUnderPluginModel && !CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            return;
        }
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            return;
        }
        IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                IRunProcessService.class);
        ITalendProcessJavaProject talendJavaProject = processService.getTalendProcessJavaProject();
        if (talendJavaProject != null) {
            IJavaProject jProject = talendJavaProject.getJavaProject();
            IProgressMonitor moniter = new NullProgressMonitor();
            try {
                if (!jProject.isOpen()) {
                    jProject.open(moniter);
                }
                // empty the src/main/java...
                IFolder srcFolder = talendJavaProject.getSrcFolder();
                talendJavaProject.cleanFolder(moniter, srcFolder);

                // empty the outputs, target/classes
                IFolder outputFolder = talendJavaProject.getOutputFolder();
                talendJavaProject.cleanFolder(moniter, outputFolder);

                // empty lib/...
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                    ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                            ILibrariesService.class);
                    if (libService != null) {
                        libService.cleanLibs();
                    }
                }

                // rules
                IFolder rulesResFolder = talendJavaProject.getResourceSubFolder(moniter, JavaUtils.JAVA_RULES_DIRECTORY);
                talendJavaProject.cleanFolder(moniter, rulesResFolder);

                // sqltempalte
                IFolder sqlTemplateResFolder = talendJavaProject.getResourceSubFolder(moniter,
                        JavaUtils.JAVA_SQLPATTERNS_DIRECTORY);
                talendJavaProject.cleanFolder(moniter, sqlTemplateResFolder);

                // anything else to do? esb?

            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        executed = true;

    }
}
