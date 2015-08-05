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
package org.talend.designer.core.ui.views.problems;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC xhuang class global comment. Detailled comment
 */
public class RoutineItemsCheck {

    private ITalendSynchronizer routineSynchronizer;

    private ECodeLanguage language;

    /*
     * add all routine items compilation errors into problmes views
     */
    public void addAllRoutineProblem() {
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext == null) {
            return;
        }
        Project project = repositoryContext.getProject();
        if (project == null) {
            return;
        }
        language = project.getLanguage();
        if (language == null) {
            return;
        }
        if (language == ECodeLanguage.PERL) {
            try {
                ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                        ICodeGeneratorService.class);
                routineSynchronizer = service.createPerlRoutineSynchronizer();

                List<IRepositoryViewObject> routineObjList = factory.getAll(ERepositoryObjectType.ROUTINES, false);
                for (IRepositoryViewObject repositoryObj : routineObjList) {
                    RoutineItem item = (RoutineItem) repositoryObj.getProperty().getItem();
                    routineSynchronizer.syncRoutine(item, true);
                    IFile file = routineSynchronizer.getFile(item);
                    addProblems(item, item.getProperty(), file);
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            } catch (SystemException e) {
                ExceptionHandler.process(e);
            }
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    Problems.refreshProblemTreeView();
                }
            });
        }

    }

    /**
     * add one routine Item errors into problems view.
     * 
     * @param item
     * @param itemLabel
     * @param file
     */

    private void addProblems(RoutineItem item, Property property, IFile file) {
        Problems.addRoutineFile(file, property);
    }

}
