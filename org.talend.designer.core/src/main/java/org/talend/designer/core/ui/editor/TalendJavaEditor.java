// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.javaeditor.WorkingCopyManager;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.core.ui.MultiPageTalendEditor;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TalendJavaEditor.java 1 2007-1-18 下午03:26:08 +0000 (下午03:26:08, 2007-1-18 2007) yzhang $
 * 
 */
public class TalendJavaEditor extends CompilationUnitEditor implements ISyntaxCheckableEditor {

    private org.eclipse.jdt.core.ICompilationUnit unit;

    private static org.eclipse.jdt.core.ICompilationUnit workingCopy;

    private boolean disposed = false;

    private List<MultiPageTalendEditor> editPartListener = new ArrayList<MultiPageTalendEditor>();

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
        addCompiler();
    }

    /**
     * Add complier for error check in this Java editor.
     * 
     * DOC yzhang Comment method "addCompiler".
     */
    private void addCompiler() {
        unit = (org.eclipse.jdt.core.ICompilationUnit) getInputJavaElement();

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
        // check if the validation is on the active editor
        try {
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

            if (!(part instanceof MultiPageTalendEditor)) {
                return;
            }
            MultiPageTalendEditor multi = (MultiPageTalendEditor) part;
            if (!multi.getCodeEditor().equals(this)) {
                return;
            }
        } catch (NullPointerException e) {
            return;
        }

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
     * Getter for unit.
     * 
     * @return the unit
     */
    public org.eclipse.jdt.core.ICompilationUnit getUnit() {
        return unit;
    }

    /**
     * Add editor part to this editor.
     * 
     * yzhang Comment method "addEditPart".
     * 
     * @param editPart
     */
    public void addEditorPart(MultiPageTalendEditor editPart) {
        if (!this.editPartListener.contains(editPart)) {
            this.editPartListener.add(editPart);
        }
    }

    public void removeEditorPart(MultiPageTalendEditor editPart) {
        this.editPartListener.remove(editPart);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#rememberSelection()
     */
    @Override
    protected void rememberSelection() {
        super.rememberSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor#restoreSelection()
     */
    @Override
    protected void restoreSelection() {
        super.restoreSelection();
    }
}
