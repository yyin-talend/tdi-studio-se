// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.login;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;

/**
 * ggu class global comment. Detailled comment
 */
public class PackProject {

    private Project project;

    private String branch;

    public PackProject(Project project, String branch) {
        super();
        this.project = project;
        this.branch = branch;
    }

    public PackProject(Project project) {
        this(project, null);
    }

    public Project getProject() {
        return this.project;
    }

    public String getBranch() {
        return this.branch;
    }

    public boolean isSVNBranch() {
        return !getProject().isLocal() && getBranch() != null;
    }

    public String getLabel() {
        return getProject().getLabel();
    }

    public ECodeLanguage getLanguage() {
        return getProject().getLanguage();
    }

    public String getTechnicalLabel() {
        return getProject().getTechnicalLabel();
    }

    @Override
    public String toString() {
        if (getBranch() != null) {
            return getProject().toString() + "--" + getBranch(); //$NON-NLS-1$
        }
        return getProject().toString();
    }

    @Override
    public int hashCode() {
        if (getBranch() != null) {
            return getProject().hashCode() & getBranch().hashCode();
        }
        return getProject().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PackProject)) {
            return false;
        }
        PackProject another = (PackProject) obj;
        if (!getProject().equals(another.getProject())) {
            return false;
        }
        if (getBranch() != null && another.getBranch() != null) {
            return getBranch().equals(another.getBranch());
        }
        return true;
    }

}
