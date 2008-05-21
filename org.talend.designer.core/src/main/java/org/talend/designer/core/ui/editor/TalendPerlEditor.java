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
package org.talend.designer.core.ui.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.widgets.Display;
import org.epic.perleditor.editors.PerlEditor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.views.problems.Problems;

/**
 * Perl editor with read only content. <br/>
 * 
 * $Id$
 * 
 */
public class TalendPerlEditor extends PerlEditor implements ISyntaxCheckableEditor {

    private boolean disposed = false;

    private String currentSelection;

    private IProcess2 process;

    /**
     * Constructs a new TalendPerlEditor.
     */
    public TalendPerlEditor(IProcess2 process) {
        super();
        this.process = process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.editors.text.TextEditor#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#isEditable()
     */
    @Override
    public boolean isEditable() {
        return false;
    }

    private static boolean codeSynchronized;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.ICheckable#checkCode()
     */
    public void validateSyntax() {
        revalidateSyntax();
        placeCursorToSelection();

        Property property = process.getProperty();
        ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();

        try {
            Problems.addRoutineFile(synchronizer.getFile(property.getItem()), property);
        } catch (SystemException e) {
            ExceptionHandler.process(e);
        }

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                Problems.refreshRepositoryView();
                Problems.refreshProblemTreeView();
            }
        });

        codeSynchronized = false;
    }

    /*
     * Return if this perl editor had been disposed.
     * 
     * @see org.talend.designer.core.ISyntaxCheckableEditor#isDisposed()
     */
    public boolean isDisposed() {
        return this.disposed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.epic.perleditor.editors.PerlEditor#dispose()
     */
    @Override
    public void dispose() {
        this.disposed = true;
        super.dispose();
    }

    private void placeCursorToSelection() {
        String mainPart = "[" + currentSelection + " main ] start";
        String assignmentPart = "$current_component=\"" + currentSelection + "\";";
        IDocument doc = getDocumentProvider().getDocument(getEditorInput());
        FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(doc);
        try {
            Region region = (Region) frda.find(0, mainPart, true, false, false, false);
            if (region != null) {
                Region region2 = (Region) frda.find(region.getOffset(), assignmentPart, true, false, false, false);
                if (region2 != null) {
                    selectAndReveal(region2.getOffset(), assignmentPart.length());
                } else {
                    selectAndReveal(region.getOffset(), mainPart.length());
                }
            } else {
                selectAndReveal(0, 0);
            }
        } catch (BadLocationException e) {
            selectAndReveal(0, 0);
        }
    }

    /**
     * DOC nrousseau Comment method "placeCursorTo".
     * 
     * @param string
     */
    public void placeCursorTo(String currentSelection) {
        this.currentSelection = currentSelection;
        placeCursorToSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.ITalendCodeEditor#removeEditorPart(org.talend.designer.core.ui.AbstractMultiPageTalendEditor)
     */
    public void removeEditorPart(AbstractMultiPageTalendEditor abstractMultiPageTalendEditor) {

    }
}
