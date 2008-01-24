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
package org.talend.designer.codegen;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.Timer;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentCompilations;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.User;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;

/***/
public class CodeGenInit implements IApplication {

    ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

    public Object start(IApplicationContext context) throws Exception {
        Timer.getTimer("CodeGenInit").start();
        CorePlugin.getContext().setHeadless(true);
        initLocalRepository();
        init(ECodeLanguage.JAVA);
        init(ECodeLanguage.PERL);
        removeLinkedResources();
        saveWorkspace();
        addMarkersForTemplatesNextInitialization();
        Timer.getTimer("CodeGenInit").stop();
        Timer.getTimer("CodeGenInit").print();
        return EXIT_OK;
    }

    private void addMarkersForTemplatesNextInitialization() {
        info("Adding markers to workspace");
        ComponentCompilations.addMarkers();
    }

    private void saveWorkspace() throws CoreException {
        info("Saving workspace");
        ResourcesPlugin.getWorkspace().save(true, new NullProgressMonitor());
    }

    private void removeLinkedResources() throws CoreException {
        info("Remove linked resources");

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".JETEmitters");
        project.accept(new IResourceVisitor() {

            public boolean visit(IResource resource) throws CoreException {
                if (resource.isLinked()) {
                    resource.delete(true, new NullProgressMonitor());
                }
                return true;
            }
        }, IResource.DEPTH_ONE, false);
    }

    private void init(ECodeLanguage language) throws Exception, InterruptedException {
        info("create " + language.getName() + " project");
        createAndLogonProject(language);
        info("init Templates");
        new CodeGeneratorManager().initTemplate();
        info("delete " + language + " project");
        deleteProject(language);
    }

    public void stop() {
    }

    public static void info(String message) {
        IStatus status = new Status(IStatus.INFO, CodeGeneratorActivator.PLUGIN_ID, message);
        CodeGeneratorActivator.getDefault().getLog().log(status);
    }

    public void init() throws Exception {
        CodeGeneratorManager codeGeneratorManager = new CodeGeneratorManager();
        IStatus status = codeGeneratorManager.initTemplate();
        if (status.getCode() != IStatus.OK) {
            throw new Exception(status.getException());
        }
    }

    private void createAndLogonProject(ECodeLanguage language) throws Exception {
        Project project = getProject(language);
        if (project == null) {
            project = repositoryFactory.createProject(getProjectName(language), "", language, createUser());
        }
        repositoryFactory.logOnProject(project);
    }

    private void deleteProject(ECodeLanguage language) throws Exception {
        IProject project = ResourceUtils.getProject(getProjectName(language).toUpperCase());
        if (project != null) {
            project.delete(true, new NullProgressMonitor());
        }
    }

    private String getProjectName(ECodeLanguage language) {
        return "codegen_" + language.getName() + "_temp_project";
    }

    private Project getProject(ECodeLanguage language) throws PersistenceException, BusinessException {
        for (Project project : repositoryFactory.readProject()) {
            if (project.getLanguage().equals(language)) {
                return project;
            }
        }

        return null;
    }

    private void initLocalRepository() throws PersistenceException {
        RepositoryContext repositoryContext = new RepositoryContext();
        repositoryContext.setUser(createUser());

        Context ctx = CorePlugin.getContext();
        ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext);

        repositoryFactory.setRepositoryFactoryFromProvider(RepositoryFactoryProvider.getRepositoriyById("local"));
        repositoryFactory.initialize();
    }

    private User createUser() {
        User user = PropertiesFactory.eINSTANCE.createUser();
        user.setLogin("user@talend.com");
        return user;
    }

    /***/
    static class CodeGeneratorManager {

        private IStatus status;

        public IStatus initTemplate() throws InterruptedException {
            final Job initializeTemplatesJob = getCodeGenerationService().initializeTemplates();
            Job.getJobManager().addJobChangeListener(new JobChangeAdapter() {

                @Override
                public void done(IJobChangeEvent event) {
                    if (event.getJob().equals(initializeTemplatesJob)) {
                        setStatus(event.getResult());
                    }
                }
            });

            while (status == null) {
                Thread.sleep(1000);
            }

            return status;
        }

        private void setStatus(IStatus result) {
            this.status = result;
        }

        private ICodeGeneratorService getCodeGenerationService() {
            IService service = GlobalServiceRegister.getDefault().getService(ICodeGeneratorService.class);
            return (ICodeGeneratorService) service;
        }
    }

}
