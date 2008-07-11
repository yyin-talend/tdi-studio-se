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

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.InformationPresenter;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class TalendJavaSourceViewerConfiguration extends JavaSourceViewerConfiguration {

    /**
     * DOC nrousseau TalendJavaSourceViewerConfiguration constructor comment.
     * 
     * @param colorManager
     * @param preferenceStore
     * @param editor
     * @param partitioning
     */
    public TalendJavaSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore, ITextEditor editor,
            String partitioning) {
        super(colorManager, preferenceStore, editor, partitioning);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration#getInformationPresenter(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IInformationPresenter getInformationPresenter(ISourceViewer sourceViewer) {
        InformationPresenter presenter = new InformationPresenter(getInformationPresenterControlCreator2(sourceViewer));
        presenter.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

        // Register information provider
        IInformationProvider provider = new TalendJavaInformationProvider(getEditor());
        String[] contentTypes = getConfiguredContentTypes(sourceViewer);
        for (int i = 0; i < contentTypes.length; i++)
            presenter.setInformationProvider(provider, contentTypes[i]);

        presenter.setSizeConstraints(60, 10, true, true);
        return presenter;
    }

    private IInformationControlCreator getInformationPresenterControlCreator2(ISourceViewer sourceViewer) {
        return new IInformationControlCreator() {

            public IInformationControl createInformationControl(Shell parent) {
                int shellStyle = SWT.RESIZE | SWT.TOOL;
                int style = SWT.V_SCROLL | SWT.H_SCROLL;
                return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
            }
        };
    }
}
