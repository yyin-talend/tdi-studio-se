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

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.repository.model.IProxyRepositoryFactory;

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

    // PTODO mhelleboid
    public static List<String> getRoutineName() {
        ECodeLanguage currentLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLanguage();
        List<String> toReturn = new ArrayList<String>();

        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        IProxyRepositoryFactory repositoryFactory = CodeGeneratorActivator.getDefault().getRepositoryService()
                .getProxyRepositoryFactory();

        String builtInPath = ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + "::" + "system" + "::";
        String userPath = ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + "::" + project.getTechnicalLabel() + "::";

        try {
            List<IRepositoryObject> routines = repositoryFactory.getAll(ERepositoryObjectType.ROUTINES);
            for (IRepositoryObject routine : routines) {
                if (currentLanguage.equals(ECodeLanguage.JAVA)) {
                    toReturn.add(routine.getLabel());
                } else {
                    RoutineItem item = (RoutineItem) routine.getProperty().getItem();
                    if (item.isBuiltIn()) {
                        toReturn.add(builtInPath + routine.getLabel());
                    } else {
                        toReturn.add(userPath + routine.getLabel());
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return toReturn;
        }
        return toReturn;
    }
}
