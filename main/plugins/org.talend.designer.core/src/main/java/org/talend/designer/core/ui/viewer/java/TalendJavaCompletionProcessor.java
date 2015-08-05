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
package org.talend.designer.core.ui.viewer.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.talend.designer.core.ui.viewer.ReconcilerViewer;
import org.talend.designer.core.ui.viewer.proposal.TalendCompletionProposal;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class TalendJavaCompletionProcessor extends JavaCompletionProcessor {

    public TalendJavaCompletionProcessor(ContentAssistant assistant, String partition) {
        super(null, assistant, partition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor#filterAndSortProposals(java.util.List,
     * org.eclipse.core.runtime.IProgressMonitor, org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext)
     */
    @Override
    protected List filterAndSortProposals(List proposals, IProgressMonitor monitor, ContentAssistInvocationContext context) {
        List newProposals = super.filterAndSortProposals(proposals, monitor, context);

        List toRemove = new ArrayList();
        try {
            char firstChar = context.getDocument().getChar(context.getInvocationOffset() - 1);
            boolean globalFieldsDone = false;
            for (Object o : newProposals) {
                ICompletionProposal proposal = (ICompletionProposal) o;
                String longna = proposal.getDisplayString();
                int indexna = longna.indexOf("-"); //$NON-NLS-1$
                if (indexna > 0) {
                    if (longna.substring(indexna + 2, longna.length()).equals(TalendJavaSourceViewer.getClassName())) {
                        toRemove.add(proposal);
                    }
                }
                if (proposal instanceof JavaCompletionProposal) {
                    JavaCompletionProposal javaProposal = ((JavaCompletionProposal) proposal);
                    if (javaProposal.getJavaElement() instanceof SourceField) {
                        globalFieldsDone = true;
                    }
                    if (javaProposal.getJavaElement() == null && globalFieldsDone) {
                        toRemove.add(proposal);
                    }
                }

                // if (firstChar != '.') {
                // if (proposal instanceof JavaMethodCompletionProposal) {
                // toRemove.add(proposal);
                // }
                // }
                if (proposal.getDisplayString().startsWith(TalendJavaSourceViewer.VIEWER_CLASS_NAME)) {
                    toRemove.add(proposal);
                }
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        newProposals.removeAll(toRemove);
        Collections.sort(newProposals, new Comparator() {

            public int compare(Object arg0, Object arg1) {
                if (arg1 instanceof TalendCompletionProposal && (!(arg0 instanceof TalendCompletionProposal))) {
                    return 1;
                } else if (arg1 instanceof TalendCompletionProposal && (arg0 instanceof TalendCompletionProposal)) {
                    TalendCompletionProposal a = (TalendCompletionProposal) arg0;
                    TalendCompletionProposal b = (TalendCompletionProposal) arg1;
                    return a.getType().compareTo(b.getType());
                }
                return 0;
            }

        });
        return newProposals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor#createContext(org.eclipse.jface.text.ITextViewer,
     * int)
     */
    @Override
    protected ContentAssistInvocationContext createContext(ITextViewer viewer, int offset) {
        if (viewer instanceof TalendJavaSourceViewer) {
            ((TalendJavaSourceViewer) viewer).updateContents();
            ICompilationUnit compilationUnit = ((TalendJavaSourceViewer) viewer).getCompilationUnit();
            if (compilationUnit != null) {
                CompletionProposalCollector cpc = new CompletionProposalCollector(compilationUnit);
                // set an empty editor Part as it's the only constructor where the viewer can be used.
                JavaContentAssistInvocationContext invocContext = new JavaContentAssistInvocationContext(viewer, offset,
                        new NullEditorPart());
                cpc.setInvocationContext(invocContext);
                return invocContext;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * DOC nrousseau TalendJavaCompletionProcessor class global comment. Detailled comment
     */
    private class NullEditorPart implements IEditorPart {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IEditorPart#getEditorInput()
         */
        public IEditorInput getEditorInput() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IEditorPart#getEditorSite()
         */
        public IEditorSite getEditorSite() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
         */
        public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#addPropertyListener(org.eclipse.ui.IPropertyListener)
         */
        public void addPropertyListener(IPropertyListener listener) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
         */
        public void createPartControl(Composite parent) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#dispose()
         */
        public void dispose() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#getSite()
         */
        public IWorkbenchPartSite getSite() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#getTitle()
         */
        public String getTitle() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#getTitleImage()
         */
        public Image getTitleImage() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#getTitleToolTip()
         */
        public String getTitleToolTip() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#removePropertyListener(org.eclipse.ui.IPropertyListener)
         */
        public void removePropertyListener(IPropertyListener listener) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPart#setFocus()
         */
        public void setFocus() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
         */
        public Object getAdapter(Class adapter) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
         */
        public void doSave(IProgressMonitor monitor) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.ISaveablePart#doSaveAs()
         */
        public void doSaveAs() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.ISaveablePart#isDirty()
         */
        public boolean isDirty() {
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
         */
        public boolean isSaveAsAllowed() {
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
         */
        public boolean isSaveOnCloseNeeded() {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.ui.text.java.ContentAssistProcessor#computeContextInformation(org.eclipse.jface.text
     * .ITextViewer, int)
     */
    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        if (viewer instanceof ReconcilerViewer) {
            ((ReconcilerViewer) viewer).updateContents();
        }
        return super.computeContextInformation(viewer, offset);
    };
}
