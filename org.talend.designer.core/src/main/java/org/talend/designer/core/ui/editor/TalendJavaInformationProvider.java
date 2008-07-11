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

import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jdt.internal.ui.text.java.hover.JavaTypeHover;
import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;

/**
 * This is a modification of the original class from eclipse to be able to support a JavaEditor in a multipage editor.
 */
public class TalendJavaInformationProvider implements IInformationProvider, IInformationProviderExtension2 {

    /**
     * Control creator.
     * 
     * @since 3.3
     */
    private static final class ControlCreator extends AbstractReusableInformationControlCreator {

        /*
         * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
         */
        public IInformationControl doCreateInformationControl(Shell parent) {
            int shellStyle = SWT.RESIZE | SWT.TOOL;
            int style = SWT.V_SCROLL | SWT.H_SCROLL;
            if (BrowserInformationControl.isAvailable(parent))
                return new BrowserInformationControl(parent, shellStyle, style);
            else
                return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
        }
    }

    /**
     * Modified from original class when close the part.
     */
    class EditorWatcher implements IPartListener {

        /**
         * @see IPartListener#partOpened(IWorkbenchPart)
         */
        public void partOpened(IWorkbenchPart part) {
        }

        /**
         * @see IPartListener#partDeactivated(IWorkbenchPart)
         */
        public void partDeactivated(IWorkbenchPart part) {
        }

        /**
         * @see IPartListener#partClosed(IWorkbenchPart)
         */
        public void partClosed(IWorkbenchPart part) {
            if (part == fEditor) {
                fEditor.getSite().getWorkbenchWindow().getPartService().removePartListener(fPartListener);
                fPartListener = null;
            } else if (part instanceof AbstractMultiPageTalendEditor
                    && ((AbstractMultiPageTalendEditor) part).getCodeEditor() == fEditor) {
                fEditor.getSite().getWorkbenchWindow().getPartService().removePartListener(fPartListener);
                fPartListener = null;
            }
        }

        /**
         * @see IPartListener#partActivated(IWorkbenchPart)
         */
        public void partActivated(IWorkbenchPart part) {
            update();
        }

        public void partBroughtToTop(IWorkbenchPart part) {
            update();
        }
    }

    protected IEditorPart fEditor;

    protected IPartListener fPartListener;

    protected String fCurrentPerspective;

    protected IJavaEditorTextHover fImplementation;

    /**
     * The presentation control creator.
     * 
     * @since 3.2
     */
    private IInformationControlCreator fPresenterControlCreator;

    public TalendJavaInformationProvider(IEditorPart editor) {

        fEditor = editor;

        if (fEditor != null) {

            fPartListener = new EditorWatcher();
            IWorkbenchWindow window = fEditor.getSite().getWorkbenchWindow();
            window.getPartService().addPartListener(fPartListener);

            update();
        }
    }

    protected void update() {

        IWorkbenchWindow window = fEditor.getSite().getWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        if (page != null) {

            IPerspectiveDescriptor perspective = page.getPerspective();
            if (perspective != null) {
                String perspectiveId = perspective.getId();

                if (fCurrentPerspective == null || fCurrentPerspective != perspectiveId) {
                    fCurrentPerspective = perspectiveId;

                    fImplementation = new JavaTypeHover();
                    fImplementation.setEditor(fEditor);
                }
            }
        }
    }

    /*
     * @see IInformationProvider#getSubject(ITextViewer, int)
     */
    public IRegion getSubject(ITextViewer textViewer, int offset) {

        if (textViewer != null)
            return JavaWordFinder.findWord(textViewer.getDocument(), offset);

        return null;
    }

    /*
     * @see IInformationProvider#getInformation(ITextViewer, IRegion)
     */
    public String getInformation(ITextViewer textViewer, IRegion subject) {
        if (fImplementation != null) {
            String s = fImplementation.getHoverInfo(textViewer, subject);
            if (s != null && s.trim().length() > 0) {
                return s;
            }
        }

        return null;
    }

    /*
     * @see IInformationProviderExtension2#getInformationPresenterControlCreator()
     * @since 3.1
     */
    public IInformationControlCreator getInformationPresenterControlCreator() {
        if (fPresenterControlCreator == null)
            fPresenterControlCreator = new ControlCreator();
        return fPresenterControlCreator;
    }
}
