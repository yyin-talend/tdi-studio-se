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
package org.talend.designer.runprocess;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.process.Problem.ProblemType;
import org.talend.core.model.properties.Item;
import org.talend.core.service.IDesignerPerlService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.ErrorDetailTreeBuilder.JobErrorEntry;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * Check if there is error in jobs before running.
 */
public class JobErrorsChecker {

    public static boolean hasErrors(Shell shell) {

        try {
            boolean isPerl = false;
            if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
                isPerl = true;
            }

            ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();

            Set<String> jobNames = new HashSet<String>();
            for (JobInfo jobInfo : LastGenerationInfo.getInstance().getLastGeneratedjobs()) {
                // get source file
                IFile sourceFile = synchronizer.getProcessFile(jobInfo);

                if (isPerl) {
                    // check syntax error in perl. java use auto build to check syntax
                    validatePerlScript(sourceFile);
                }

                jobNames.add(jobInfo.getJobName());

                // Property property = process.getProperty();
                Problems.addRoutineFile(sourceFile, ProblemType.JOB, jobInfo.getJobName(), jobInfo.getJobVersion(), true);
            }
            Problems.refreshProblemTreeView();

            // collect error
            List<Problem> errors = Problems.getProblemList().getProblemsBySeverity(ProblemStatus.ERROR);
            ErrorDetailTreeBuilder builder = new ErrorDetailTreeBuilder();
            List<JobErrorEntry> input = builder.createTreeInput(errors, jobNames);
            if (input.size() > 0) {
                ErrorDetailDialog dialog = new ErrorDetailDialog(shell, input);
                if (dialog.open() != IDialogConstants.OK_ID) {
                    // stop running
                    return true;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return false;
    }

    public static void validatePerlScript(IFile file) {
        try {
            String sourceCode = getSourceCode(file.getContents());
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerPerlService.class)) {
                IDesignerPerlService service = (IDesignerPerlService) GlobalServiceRegister.getDefault().getService(
                        IDesignerPerlService.class);
                service.perlValidator(file, sourceCode);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    public static boolean checkExportErrors(IStructuredSelection selection) {
        if (!selection.isEmpty()) {
            boolean isPerl = false;
            if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
                isPerl = true;
            }

            ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();
            Set<String> jobNames = new HashSet<String>();

            List<RepositoryNode> nodes = (List<RepositoryNode>) selection.toList();
            for (RepositoryNode node : nodes) {
                Item item = node.getObject().getProperty().getItem();
                try {
                    IFile sourceFile = synchronizer.getFile(item);
                    if (isPerl) {
                        // check syntax error in perl. java use auto build to check syntax
                        validatePerlScript(sourceFile);
                    }
                    jobNames.add(item.getProperty().getLabel());

                    // Property property = process.getProperty();
                    Problems.addRoutineFile(sourceFile, ProblemType.JOB, item.getProperty().getLabel(), item.getProperty()
                            .getVersion(), true);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

            }

            Problems.refreshProblemTreeView();

            List<Problem> errors = Problems.getProblemList().getProblemsBySeverity(ProblemStatus.ERROR);
            ErrorDetailTreeBuilder builder = new ErrorDetailTreeBuilder();
            List<JobErrorEntry> input = builder.createTreeInput(errors, jobNames);
            if (input.size() > 0) {
                String label = ((JobErrorEntry) input.get(0)).getLabel();
                IBrandingService breaningService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                        IBrandingService.class);
                String processLabel = breaningService.getBrandingConfiguration().getJobDesignName();
                if (!processLabel.equals("Routes")) {
                    MessageDialog.openError(Display.getDefault().getActiveShell(),
                            Messages.getString("JobErrorsChecker_compile_errors"), //$NON-NLS-1$
                            Messages.getString("JobErrorsChecker_compile_error_content", label)); //$NON-NLS-1$
                } else {
                    MessageDialog.openError(Display.getDefault().getActiveShell(),
                            Messages.getString("CamelJobErrorsChecker_compile_errors"), //$NON-NLS-1$
                            Messages.getString("CamelJobErrorsChecker_compile_error_content", label)); //$NON-NLS-1$
                }
                return true;
            }

        }
        return false;
    }

    /**
     * DOC chuang Comment method "getSourceCode".
     * 
     * @param contents
     * @return
     */
    private static String getSourceCode(InputStream contents) {
        String sourceCode = ""; //$NON-NLS-1$
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(new BufferedInputStream(contents));
            StringBuffer buffer = new StringBuffer();
            char[] readBuffer = new char[2048];
            int n = in.read(readBuffer);
            while (n > 0) {
                buffer.append(readBuffer, 0, n);
                n = in.read(readBuffer);
            }
            sourceCode = buffer.toString();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (contents != null) {
                try {
                    contents.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }

        return sourceCode;
    }

}
