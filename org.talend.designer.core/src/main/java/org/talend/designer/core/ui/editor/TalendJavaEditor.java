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
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.builder.NameEnvironment;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.javaeditor.WorkingCopyManager;
import org.eclipse.jdt.internal.ui.text.JavaCompositeReconcilingStrategy;
import org.eclipse.jdt.internal.ui.text.JavaReconciler;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.designer.core.ISyntaxCheckableEditor;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TalendJavaEditor.java 1 2007-1-18 下午03:26:08 +0000 (下午03:26:08, 2007-1-18 2007) yzhang $
 * 
 */
public class TalendJavaEditor extends CompilationUnitEditor implements ISyntaxCheckableEditor {

    static org.eclipse.jdt.internal.compiler.env.ICompilationUnit unit;

    private static org.eclipse.jdt.core.ICompilationUnit workingCopy;

    private boolean disposed = false;

    /**
     * DOC amaumont TalendJavaEditor constructor comment.
     */
    public TalendJavaEditor() {
        super();
    }

    /*
     * Over write this method force to add complier to this editor. Beacuse by default if the editor is not editable,
     * the complier for error check will not installed.
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        addComplier();
    }

    /**
     * Add complier for error check in this Java editor.
     * 
     * DOC yzhang Comment method "addComplier".
     */
    private void addComplier() {
        JavaCompositeReconcilingStrategy strategy = new JavaCompositeReconcilingStrategy(this,
                IDocumentExtension3.DEFAULT_PARTITIONING);
        JavaReconciler reconciler = new JavaReconciler(this, strategy, false);
        reconciler.setIsIncrementalReconciler(false);
        reconciler.setIsAllowedToModifyDocument(false);
        reconciler.setProgressMonitor(new NullProgressMonitor());
        reconciler.setDelay(500);
        reconciler.install(getViewer());

        unit = (org.eclipse.jdt.internal.compiler.env.ICompilationUnit) getInputJavaElement();

        WorkingCopyManager fManager = JavaPlugin.getDefault().getWorkingCopyManager();
        workingCopy = (org.eclipse.jdt.core.ICompilationUnit) fManager.getWorkingCopy(getEditorInput());

    }

    /*
     * Edit is not allowed.
     * 
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#isEditable()
     */
    @Override
    public boolean isEditable() {
        return false;
    }

    /*
     * Check the syntax for java code.
     * 
     * @see org.talend.designer.core.ui.editor.ISyntaxCheckable#validateSyntax()
     */
    public void validateSyntax() {

        ISourceViewer sourceViewer = getSourceViewer();
        if (sourceViewer instanceof JavaSourceViewer) {
            JavaSourceViewer javaSourceViewer = (JavaSourceViewer) sourceViewer;
            selectAndReveal(0, 0);
            javaSourceViewer.doOperation(ISourceViewer.FORMAT);
            doSave(null);
        }
        setFocus();
    }

    /*
     * Save as is not allowed.
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#dispose()
     */
    @Override
    public void dispose() {
        this.disposed = true;
        super.dispose();
    }

    /**
     * 
     * 
     * DOC yzhang Comment method "isDisposed".
     * 
     * @return Whether this editor had been disposed.
     */
    public boolean isDisposed() {
        return this.disposed;
    }

    /**
     * Temporary method
     */
    @SuppressWarnings("restriction")
    public static List<DetailedProblem> getProblems() {

        //        
        // getEditorInput().get

        IJavaProject javaProject = unit != null ? ((IJavaElement) unit).getJavaProject() : null;
        HashMap preferences = null;
        if (javaProject == null)
            preferences = new HashMap(JavaCore.getOptions());
        else
            preferences = new HashMap(javaProject.getOptions(true));

        ICompilerRequestor compilerRequestor = new ICompilerRequestor() {

            public void acceptResult(CompilationResult compilationResult) {
                // do nothing
            }
        };
        NameEnvironment nameEnvironment = new NameEnvironment(javaProject);
        CompilerOptions compilerOptions = new CompilerOptions(preferences);
        DefaultProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
        IErrorHandlingPolicy errorHandlingPolicy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
        org.eclipse.jdt.internal.compiler.CompilationResult compilationResult = new CompilationResult(unit, 1, 1, 100);
        org.eclipse.jdt.internal.compiler.problem.ProblemReporter problemReporter = new ProblemReporter(
                DefaultErrorHandlingPolicies.proceedWithAllProblems(), compilerOptions, new DefaultProblemFactory());
        CompilationUnitDeclaration compilationUnitDeclaration = new CompilationUnitDeclaration(problemReporter,
                compilationResult, unit.getContents().length);
        Compiler compiler = new Compiler(nameEnvironment, errorHandlingPolicy, compilerOptions, compilerRequestor, problemFactory);

        compiler.compile(new ICompilationUnit[] { (ICompilationUnit) unit });
        compiler.resolve((org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration) compilationUnitDeclaration,
                (ICompilationUnit) unit, true, true, false);

        Map options = JavaCore.getOptions();
        options.remove(JavaCore.COMPILER_TASK_TAGS); // no need to parse task tags

        LookupEnvironment lookupEnvironment = new LookupEnvironment(compiler, compilerOptions, problemReporter, nameEnvironment);
        compilationUnitDeclaration.scope = new CompilationUnitScope(compilationUnitDeclaration, lookupEnvironment);
        //
        compilationUnitDeclaration.resolve();

        final ArrayList<DetailedProblem> iproblems = new ArrayList<DetailedProblem>();

        // create requestor for accumulating discovered problems
        IProblemRequestor problemRequestor = new IProblemRequestor() {

            public void acceptProblem(IProblem problem) {
                if (problem.isError()) {
                    // System.out.println(problem.getID() + ": " + problem.getMessage());
                    String source = String.copyValueOf(unit.getContents(), problem.getSourceStart(), problem.getSourceEnd()
                            - problem.getSourceStart() + 1);
                    String key = extractKey(unit.getContents(), problem.getSourceStart());
                    DetailedProblem detailedProblem = new DetailedProblem(problem, key, source);
                    iproblems.add(detailedProblem);
                }

            }

            private String extractKey(char[] contents, int start) {

                Perl5Compiler compiler = new Perl5Compiler();
                Perl5Matcher matcher = new Perl5Matcher();
                String key = null;
                Pattern pattern = null;
                try {
                    pattern = compiler.compile(".*//\\s*([\\w:]+)\\s*$");
                } catch (MalformedPatternException e) {
                    ExceptionHandler.process(e);
                    return null;
                }

                // search previous cr
                int indexStartOfLine = -1;
                int indexEndOfLine = -1;

                for (int i = start; i >= 0; i--) {
                    if (contents[i] == '\n') {
                        if (indexEndOfLine == -1) {
                            indexEndOfLine = i;
                        } else if (indexStartOfLine == -1) {
                            indexStartOfLine = i + 1;
                        }
                    }

                    if (indexStartOfLine != -1 && indexEndOfLine != -1) {
                        String line = String.copyValueOf(contents, indexStartOfLine, indexEndOfLine - indexStartOfLine);
                        if (matcher.matches(line, pattern)) {
                            MatchResult matchResult = matcher.getMatch();
                            key = matchResult.group(1);
                            break;
                        }

                        indexEndOfLine = indexStartOfLine - 1;
                        indexStartOfLine = -1;

                    }

                }

                return key;

            }

            // private String extractKey(char[] contents, int start) {
            // // search previous cr
            // int previousCrIndex = -1;
            // int nextCrIndex = -1;
            // for (int i = start, j = start; i >= 0 && j < contents.length; i--, j++) {
            // if (previousCrIndex == -1) {
            // if (contents[i] == '\n') {
            // previousCrIndex = i + 1;
            // }
            // }
            // if (i == 0 && previousCrIndex == -1) {
            // previousCrIndex = 0;
            // }
            //                    
            // if (nextCrIndex == -1) {
            // if (contents[j] == '\n' || contents[j] == '\r') {
            // nextCrIndex = j;
            // }
            // }
            //                    
            // if (j == contents.length - 1 && nextCrIndex == -1) {
            // nextCrIndex = contents.length;
            // }
            //                    
            // if (previousCrIndex != -1 && nextCrIndex != -1) {
            // break;
            // }
            //                    
            // }
            //                
            // return String.copyValueOf(contents, previousCrIndex, nextCrIndex - previousCrIndex);
            //                
            // }
            //            
            public void beginReporting() {
            }

            public void endReporting() {
            }

            public boolean isActive() {
                return true;
            } // will detect problems if active
        };

        // use working copy to hold source with error
        org.eclipse.jdt.core.ICompilationUnit workingCopy;
        try {
            workingCopy = ((org.eclipse.jdt.core.ICompilationUnit) unit).getWorkingCopy(new WorkingCopyOwner() {
            }, problemRequestor, null);
            ((IOpenable) workingCopy).getBuffer().setContents(unit.getContents());
            // workingCopy.reconcile(1, true, null, null);
            // workingCopy.reconcile(true, null);
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return iproblems;
    }


}
