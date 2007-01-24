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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.JavaCompositeReconcilingStrategy;
import org.eclipse.jdt.internal.ui.text.JavaReconciler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TalendJavaEditor.java 1 2007-1-18 下午03:26:08 +0000 (下午03:26:08, 2007-1-18 2007) yzhang $
 * 
 */
public class TalendJavaEditor extends CompilationUnitEditor implements ISyntaxCheckable {

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
}
