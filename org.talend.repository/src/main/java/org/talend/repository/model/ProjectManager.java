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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProjectReference;

/**
 * ggu class global comment. Detailled comment
 */
public final class ProjectManager {

    private static ProjectManager singleton;

    private Project currentProject;

    private List<Project> referencedprojects = new ArrayList<Project>();

    private ProjectManager() {
        initCurrentProject();
    }

    public static synchronized ProjectManager getInstance() {
        if (singleton == null) {
            singleton = new ProjectManager();
        }
        return singleton;
    }

    private void initCurrentProject() {
        Context ctx = CorePlugin.getContext();
        if (ctx != null) {
            RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
            if (repositoryContext != null) {
                currentProject = repositoryContext.getProject();
                return;
            }
        }
        currentProject = null;
    }

    /**
     * 
     * retrieve the referenced projects of current project.
     */
    public void retrieveReferencedProjects() {
        referencedprojects.clear();
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory != null) {
            List<org.talend.core.model.properties.Project> rProjects = factory.getReferencedProjects();
            if (rProjects != null) {
                for (org.talend.core.model.properties.Project p : rProjects) {
                    Project project = new Project(p);
                    referencedprojects.add(project);
                }
            }
        }
    }

    /**
     * return current project.
     * 
     */
    public Project getCurrentProject() {
        if (this.currentProject == null) {
            initCurrentProject();
        }
        return this.currentProject;

    }

    /**
     * 
     * return the referenced projects of current project.
     */
    public List<Project> getReferencedProjects() {
        if (this.referencedprojects.isEmpty()) {
            retrieveReferencedProjects();
        }
        return this.referencedprojects;
    }

    /**
     * 
     * return the referenced projects of the project.
     */
    @SuppressWarnings("unchecked")
    public List<Project> getReferencedProjects(Project project) {
        if (project != null) {

            if (project.equals(this.currentProject)) {
                // retrieveReferencedProjects();
                return this.referencedprojects;
            }

            List<Project> refProjects = new ArrayList<Project>();
            for (ProjectReference refProject : (List<ProjectReference>) project.getEmfProject().getReferencedProjects()) {
                refProjects.add(new Project(refProject.getReferencedProject()));
            }
            return refProjects;
        }
        return Collections.emptyList();
    }

}
