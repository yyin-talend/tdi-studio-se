// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.process.Problem.ProblemType;
import org.talend.core.model.properties.Item;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.core.IDesignerCoreService;
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
            ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();

            Set<String> jobIds = new HashSet<String>();
            for (JobInfo jobInfo : LastGenerationInfo.getInstance().getLastGeneratedjobs()) {
                // get source file
                IFile sourceFile = synchronizer.getProcessFile(jobInfo);

                jobIds.add(jobInfo.getJobId());

                // Property property = process.getProperty();
                Problems.addRoutineFile(sourceFile, ProblemType.JOB, jobInfo.getJobName(), jobInfo.getJobVersion(), true);
            }
            Problems.refreshProblemTreeView();

            // collect error
            List<Problem> errors = Problems.getProblemList().getProblemsBySeverity(ProblemStatus.ERROR);
            ErrorDetailTreeBuilder builder = new ErrorDetailTreeBuilder();
            List<JobErrorEntry> input = builder.createTreeInput(errors, jobIds);
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

    public static boolean checkExportErrors(IStructuredSelection selection, boolean isJob) {
        if (!selection.isEmpty()) {
            ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();
            Set<String> jobIds = new HashSet<String>();

            List<RepositoryNode> nodes = selection.toList();
            for (RepositoryNode node : nodes) {
                Item item = node.getObject().getProperty().getItem();
                try {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                        ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                                .getService(ICamelDesignerCoreService.class);
                        if (service.isInstanceofCamel(item)) {
                            synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createCamelBeanSynchronizer();
                        }
                    }
                    IFile sourceFile = synchronizer.getFile(item);
                    // check the item has compile error when export job
                    boolean ret = false;
                    IMarker[] markers = sourceFile.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
                    for (IMarker marker : markers) {
                        Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                        String message = (String) marker.getAttribute(IMarker.MESSAGE);
                        Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                        Integer start = (Integer) marker.getAttribute(IMarker.CHAR_START);
                        Integer end = (Integer) marker.getAttribute(IMarker.CHAR_END);
                        if (lineNr != null && message != null && severity != null && start != null && end != null) {
                            switch (severity) {
                            case IMarker.SEVERITY_ERROR:
                                ret = true;
                                break;
                            default:
                                break;
                            }
                        }
                    }
                    if (ret) {
                        if (isJob) {
                            throw new ProcessorException(Messages.getString("JobErrorsChecker_compile_errors") + "\n" + //$NON-NLS-1$
                                    Messages.getString("JobErrorsChecker_compile_error_content", item.getProperty().getLabel()));
                        } else {
                            throw new ProcessorException(Messages.getString("CamelJobErrorsChecker_compile_errors") + "\n" + //$NON-NLS-1$
                                    Messages.getString("CamelJobErrorsChecker_compile_error_content", item.getProperty()
                                            .getLabel()));
                        }
                    }

                    jobIds.add(item.getProperty().getId());

                    // Property property = process.getProperty();
                    Problems.addRoutineFile(sourceFile, ProblemType.JOB, item.getProperty().getLabel(), item.getProperty()
                            .getVersion(), true);
                    IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                    IProcess process = service.getProcessFromItem(item);
                    if (process instanceof IProcess2) {
                        ((IProcess2) process).checkProcess();
                    }
                    Problems.refreshProblemTreeView();
                } catch (Exception e) {
                    MessageBoxExceptionHandler.process(e);
                    return true;
                }

            }

            List<Problem> errors = Problems.getProblemList().getProblemsBySeverity(ProblemStatus.ERROR);
            ErrorDetailTreeBuilder builder = new ErrorDetailTreeBuilder();
            List<JobErrorEntry> input = builder.createTreeInput(errors, jobIds);
            try {
                if (input.size() > 0) {
                    String label = input.get(0).getLabel();
                    if (isJob) {
                        throw new ProcessorException(Messages.getString("JobErrorsChecker_compile_errors") + "\n" + //$NON-NLS-1$
                                Messages.getString("JobErrorsChecker_compile_error_content", label));
                    } else {
                        throw new ProcessorException(Messages.getString("CamelJobErrorsChecker_compile_errors") + "\n" + //$NON-NLS-1$
                                Messages.getString("CamelJobErrorsChecker_compile_error_content", label));
                    }
                }
            } catch (Exception e) {
                MessageBoxExceptionHandler.process(e);
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
