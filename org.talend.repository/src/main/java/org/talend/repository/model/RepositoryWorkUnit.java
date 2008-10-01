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

import org.talend.core.model.general.Project;

/***/
public abstract class RepositoryWorkUnit {

    private String name;

    private Project project;

    public RepositoryWorkUnit(String name) {
        this.name = name;
    }

    public RepositoryWorkUnit(Project project, String name) {
        this.project = project;
        this.name = name;
    }
        
    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }
    
    public abstract void run() throws Exception;
    
}
