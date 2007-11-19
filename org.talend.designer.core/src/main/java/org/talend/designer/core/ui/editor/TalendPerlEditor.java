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
import org.epic.perleditor.editors.PerlEditor;
import org.talend.designer.core.ISyntaxCheckableEditor;

/**
 * Perl editor with read only content. <br/>
 * 
 * $Id$
 * 
 */
public class TalendPerlEditor extends PerlEditor implements ISyntaxCheckableEditor {

    private boolean disposed = false;

    private String currentSelection;

    /**
     * Constructs a new TalendPerlEditor.
     */
    public TalendPerlEditor() {
        super();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.ICheckable#checkCode()
     */
    public void validateSyntax() {
        revalidateSyntax();
        placeCursorToSelection();
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

}
