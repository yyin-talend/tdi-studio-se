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
package org.talend.designer.core.ui.views.problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.epic.perleditor.editors.util.PerlValidator;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.codegen.IRoutineSynchronizer;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC xhuang class global comment. Detailled comment
 */
public class RoutineItemsCheck {

    private IRoutineSynchronizer routineSynchronizer;

    private ECodeLanguage language;

    /*
     * add all routine items compilation errors into problmes views
     */
    public void addAllRoutineProblem() {
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
        language = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage();
        if (language != null && !language.getName().equalsIgnoreCase("PERL")) {
            return;
        }
        try {
            RootContainer<String, IRepositoryObject> routineContainer = factory.getRoutine();
            for (IRepositoryObject ro : routineContainer.getMembers()) {
                RoutineItem item = (RoutineItem) ro.getProperty().getItem();
                ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                        ICodeGeneratorService.class);
                routineSynchronizer = service.createPerlRoutineSynchronizer();
                IFile file = routineSynchronizer.syncRoutine(item, true);
                addProblems(item, item.getProperty().getLabel(), file);
            }

            List<Container<String, IRepositoryObject>> subContainer = routineContainer.getSubContainer();
            if (subContainer.size() <= 1) {
                return;
            }
            for (Container<String, IRepositoryObject> container : subContainer) {
                if (!RepositoryConstants.SYSTEM_DIRECTORY.equals(container.getLabel())) {
                    List<IRepositoryObject> members = container.getMembers();

                    for (IRepositoryObject ro : members) {
                        RoutineItem item = (RoutineItem) ro.getProperty().getItem();

                        ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                                ICodeGeneratorService.class);

                        routineSynchronizer = service.createPerlRoutineSynchronizer();
                        IFile file = routineSynchronizer.syncRoutine(item, true);
                        addProblems(item, item.getProperty().getLabel(), file);
                    }
                }
            }

        } catch (PersistenceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * add one routine Item errors into problems view *
     * 
     * @param item
     * @param itemLabel
     * @param file
     */

    private void addProblems(RoutineItem item, String itemLabel, IFile file) {

        // FileInputStream codeStream = file;
        try {
            IProject perlProject = CorePlugin.getDefault().getRunProcessService()
                    .getProject(LanguageManager.getCurrentLanguage());
            try {
                String sourceCode = readSourceFile(file.getLocation().makeRelative().toString());
                PerlValidator.instance().validate(file, sourceCode);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
            String routineFileName = file.getName();
            Problems.clearAllComliationError(itemLabel);
            for (IMarker marker : markers) {
                Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                String message = (String) marker.getAttribute(IMarker.MESSAGE);
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                if (severity == IMarker.SEVERITY_ERROR) {
                    Problems.add(item, ProblemStatus.ERROR, marker, itemLabel, message, lineNr);

                } else if (severity == IMarker.SEVERITY_WARNING) {
                    Problems.add(item, ProblemStatus.WARNING, marker, itemLabel, message, lineNr);

                } else if (severity == IMarker.SEVERITY_INFO) {
                    Problems.add(item, ProblemStatus.INFO, marker, itemLabel, message, lineNr);
                }
            }
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    Problems.refreshProblemTreeView();
                }
            });
        } catch (CoreException e) {
            // MessageBoxExceptionHandler.process(e);
            e.printStackTrace();
        }
    }

    /**
     * xhuang Comment method "readSourceFile".
     * 
     * @param path
     * @return
     * @throws IOException
     */
    private String readSourceFile(String path) throws IOException {
        BufferedReader in = null;

        try {
            StringWriter sourceCode = new StringWriter();

            char[] buf = new char[1024];
            in = new BufferedReader(new FileReader(path));

            int read = 0;
            while ((read = in.read(buf)) > 0) {
                sourceCode.write(buf, 0, read);
            }
            return sourceCode.toString();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }
}
