// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.codegen.temp;

import java.util.List;

import org.eclipse.core.runtime.Status;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;

/**
 * Build RoutineName for PerlHeader.
 * 
 * $Id$
 * 
 */
public final class CodeGeneratorRoutine {

    /**
     * Default Constructor. Must not be used.
     */
    private CodeGeneratorRoutine() {
    }

    // PTODO MHE
    public static String getRoutineName() {
        String routineName = "";

        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        IRepositoryFactory repositoryFactory = RepositoryFactoryProvider.getInstance();
        RootContainer<String, IRepositoryObject> container;
        try {
            container = repositoryFactory.getRoutine();
            List<IRepositoryObject> routines = container.getMembers();
            for (IRepositoryObject routine : routines) {
                routineName = project.getTechnicalLabel() + "__" + routine.getLabel();
            }
        } catch (PersistenceException e) {
            Status status = new Status(Status.ERROR, CodeGeneratorActivator.PLUGIN_ID, Status.WARNING, e.getMessage(), e);
            CodeGeneratorActivator.getDefault().getLog().log(status);
            routineName = "1";
        }
        return routineName;
    }
}
